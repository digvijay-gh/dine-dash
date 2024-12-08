package software.digvijay.dinedash.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.restaurant.Restaurant;
import software.digvijay.dinedash.entity.user.User;
import software.digvijay.dinedash.entity.user.UserAddress;
import software.digvijay.dinedash.service.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CartService cartService;
    @Autowired
    private MenuItemService menuItemService;


    @GetMapping("/get-all-user")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> allUsers = userService.getAllUsers();
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-all-restaurant")
    public ResponseEntity<?> getAllRestaurant() {
        try {
            List<Restaurant> restaurantList = restaurantService.getAllUsers();
            return new ResponseEntity<>(restaurantList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-all-address")
    public ResponseEntity<?> getAllAddress() {
        try {
            List<UserAddress> userAddressList = addressService.getAllAddress();
            return new ResponseEntity<>(userAddressList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-all-menuitem")
    public ResponseEntity<?> getAllMenuItem() {
        try {
            List<MenuItem> allItems = menuItemService.getAllItems();
            return new ResponseEntity<>(allItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/health-check")
    public ResponseEntity<?> healthCheck(){
        return new  ResponseEntity("Server is running smooth",HttpStatus.OK);
    }
}
