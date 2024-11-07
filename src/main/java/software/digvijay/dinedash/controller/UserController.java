package software.digvijay.dinedash.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import software.digvijay.dinedash.dto.RestaurantDetailsDTO;
import software.digvijay.dinedash.dto.UserDTO;
import software.digvijay.dinedash.entity.user.User;
import software.digvijay.dinedash.entity.user.UserAddress;
import software.digvijay.dinedash.repository.UserAddressRepository;
import software.digvijay.dinedash.service.*;
import software.digvijay.dinedash.utils.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CartService cartService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/get-all-user")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> allUsers = userService.getAllUsers();
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-details")
    public ResponseEntity<?> getUserDetails() {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(name);
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/get-restaurants")
    public ResponseEntity<?> getRestaurantInCity() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            List<RestaurantDetailsDTO> restaurantByCity = restaurantService.getRestaurantByCity(user.getAddress().getLocation(), user.getAddress().getCity());
            return new ResponseEntity<>(restaurantByCity, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update-details")
    public ResponseEntity<?> updateUser(@RequestBody User inputUser) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            userService.updateUser(user, inputUser);
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            userService.deleteUser(user);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-cart")
    public ResponseEntity<?> getCartDetails() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            return new ResponseEntity<>(user.getCart(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/add-to-cart/{id}")
    public ResponseEntity<?> addToCart(@PathVariable ObjectId id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            cartService.addItemToUserCart(id, user);
            return new ResponseEntity<>(user.getCart(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove-from-cart/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable ObjectId id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            cartService.removeFromCart(id, user);
            return new ResponseEntity<>(user.getCart(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-address")
    public ResponseEntity<?> setAddress(@RequestBody UserAddress userAddress) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            addressService.updateAddressForUser(userAddress, user);
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
