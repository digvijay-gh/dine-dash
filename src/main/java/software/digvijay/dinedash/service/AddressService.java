package software.digvijay.dinedash.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.digvijay.dinedash.entity.user.User;
import software.digvijay.dinedash.entity.user.UserAddress;
import software.digvijay.dinedash.repository.UserAddressRepository;

import java.util.Optional;

@Slf4j
@Service
public class AddressService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAddressRepository userAddressRepository;

//    public void addAddressForUser(UserAddress userAddress, User user) {
//        try {
//            user.getAddressList().add(userAddress);
//            userAddressRepository.save(userAddress);
//            userService.saveUser(user);
//        } catch (Exception e) {
//            log.error("Error while adding address- {} for {}", userAddress.toString(), user.getUsername(), e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void deleteAddressByID(ObjectId id, User user) {
//        try {
//            boolean removed = false;
//            removed = user.getAddressList().removeIf(x -> x.getId().equals(id));
//            if (removed) {
//                userService.saveUser(user);
//                userAddressRepository.deleteById(id);
//            }
//        } catch (Exception e) {
//            log.error("Error while deleting address- {} for {}", id, user.getUsername(), e);
//            throw new RuntimeException(e);
//        }
//    }
    public void saveAddress(UserAddress address){
        userAddressRepository.save(address);
    }
    public void deleteById(ObjectId id) {
        userAddressRepository.deleteById(id);
    }

    public void updateAddressForUser(UserAddress newAddress, User user) {
        try {

            if (user.getAddress() != null) {
            ObjectId addressId = user.getAddress().getId();

                Optional<UserAddress> optionalUserAddress = userAddressRepository.findById(addressId);
                if (optionalUserAddress.isPresent()) {
                    UserAddress addressInDb = optionalUserAddress.get();
                    if (newAddress.getStreetAddress() != null && !newAddress.getStreetAddress().isEmpty())
                        addressInDb.setStreetAddress(newAddress.getStreetAddress());
                    if (newAddress.getCity() != null && !newAddress.getCity().isEmpty())
                        addressInDb.setCity(newAddress.getCity());
                    if (newAddress.getLocation() != null)
                        addressInDb.setLocation(newAddress.getLocation());
                    userAddressRepository.save(addressInDb);
                }
            }else{
                userAddressRepository.save(newAddress);
                user.setAddress(newAddress);
                userService.saveUser(user);
            }

        } catch (Exception e) {
            log.error("Error while updating address- {} for {}", newAddress, user.getUsername(), e);
            throw new RuntimeException(e);
        }
    }
}
