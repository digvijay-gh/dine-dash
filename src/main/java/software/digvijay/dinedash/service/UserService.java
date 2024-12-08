package software.digvijay.dinedash.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.digvijay.dinedash.entity.user.User;
import software.digvijay.dinedash.repository.MenuItemRepository;
import software.digvijay.dinedash.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private AddressService addressService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);

    }

    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error while saving user: {}", user.getUsername(), e);
            throw new RuntimeException(e);
        }
    }

    public boolean saveNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            if (user.getAddress() != null)
                addressService.saveAddress(user.getAddress());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Exception for {}", user.getUsername());
            log.error("Error occured: ", e);
            return false;
        }
    }

    public void updateUser(User userInDb, User inputUser) {
        try {
            if (userInDb != null) {
                if (inputUser.getUsername() != null && !inputUser.getUsername().isEmpty())
                    userInDb.setUsername(inputUser.getUsername());
                if (inputUser.getPassword() != null && !inputUser.getPassword().isEmpty())
                    userInDb.setPassword(passwordEncoder.encode(inputUser.getPassword()));
                if (inputUser.getEmail() != null)
                    userInDb.setEmail(inputUser.getEmail());

                userRepository.save(userInDb);
            }
        } catch (Exception e) {
            log.error("Error while updating user {}", userInDb.getUsername(), e);
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(User user) {

        try {
//            for (UserAddress userAddress : user.getAddressList()) {
//                addressService.deleteById(userAddress.getId());
//            }
            addressService.deleteById(user.getAddress().getId());
            userRepository.deleteByUsername(user.getUsername());
        } catch (Exception e) {
            log.error("Error while deleting {}", user.getUsername(), e);
            throw new RuntimeException(e);
        }

    }


}
