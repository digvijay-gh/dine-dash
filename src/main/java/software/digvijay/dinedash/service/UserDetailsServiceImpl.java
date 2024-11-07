package software.digvijay.dinedash.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import software.digvijay.dinedash.entity.restaurant.Restaurant;
import software.digvijay.dinedash.entity.user.User;
import software.digvijay.dinedash.repository.RestaurantRepository;
import software.digvijay.dinedash.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find the user
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }

        // If not found, try to find the restaurant
        Restaurant restaurant = restaurantRepository.findByUsername(username); // Assuming you have this method in your repository
        if (restaurant != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(restaurant.getUsername())
                    .password(restaurant.getPassword())
                    .roles(restaurant.getRoles().toArray(new String[0]))
                    .build();
        }
        throw new UsernameNotFoundException("Username not found  " +username);
    }
}
