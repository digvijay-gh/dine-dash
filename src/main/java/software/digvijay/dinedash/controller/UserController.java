package software.digvijay.dinedash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import software.digvijay.dinedash.dto.*;
import software.digvijay.dinedash.entity.user.MyCart;
import software.digvijay.dinedash.entity.user.User;
import software.digvijay.dinedash.entity.user.UserAddress;
import software.digvijay.dinedash.service.*;
import software.digvijay.dinedash.utils.JwtUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "User APIs", description = "User related services")
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

    //    @GetMapping("/get-all-user")
//    public ResponseEntity<?> getAllUsers() {
//        try {
//            List<User> allUsers = userService.getAllUsers();
//            List<UserDTO> outputUsers=new ArrayList<>();
//            for(User user:allUsers){
//                outputUsers.add(new UserDTO(user));
//            }
//            return new ResponseEntity<>(outputUsers, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }
    @Operation(summary = "Get personal data")
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

    @Operation(summary = "User signup")
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserSignUpDTO userDTO) {
        try {
            User user = new User(userDTO);
            userService.saveNewUser(user);
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDetails) {
        try {
            User user = new User(loginDetails);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "get nearby restaurants")
    @GetMapping("/get-restaurants")
    public ResponseEntity<?> getRestaurantInCity() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            List<NearbyRestaurantDTO> restaurantByCity = restaurantService.getRestaurantByCity(user.getAddress().getLocation(), user.getAddress().getCity());
            return new ResponseEntity<>(restaurantByCity, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "update user details")
    @PutMapping("/update-details")
    public ResponseEntity<?> updateUser(@RequestBody UserSignUpDTO inputUserDTO) {
        try {
            User inputUser = new User(inputUserDTO);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            userService.updateUser(user, inputUser);
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "delete user from db")
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

    @Operation(summary = "get details of user cart")
    @GetMapping("/get-cart")
    public ResponseEntity<?> getCartDetails() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            return new ResponseEntity<>(new MyCart(user.getCart()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "add an item to cart")
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

    @Operation(summary = "remove an item from cart")
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

    @Operation(summary = "clear cart")
    @DeleteMapping("/empty-cart")
    public ResponseEntity<?> emptyCart() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            cartService.emptyCart(user);
            return new ResponseEntity<>(user.getCart(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "update user address")
    @PutMapping("/update-address")
    public ResponseEntity<?> setAddress(@RequestBody UserAddressDTO userAddressDTO) {
        try {
            UserAddress userAddress = new UserAddress(userAddressDTO);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            addressService.updateAddressForUser(userAddress, user);
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "user places order")
    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder() {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(name);
            cartService.placeOrder(user);
            return new ResponseEntity<>(user.getPastOrders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
