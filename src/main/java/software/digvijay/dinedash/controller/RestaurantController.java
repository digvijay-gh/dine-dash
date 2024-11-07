package software.digvijay.dinedash.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import software.digvijay.dinedash.dto.InputRestaurantDTO;
import software.digvijay.dinedash.dto.RestaurantDetailsDTO;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.restaurant.Restaurant;
import software.digvijay.dinedash.service.MenuItemService;
import software.digvijay.dinedash.service.RestaurantService;
import software.digvijay.dinedash.service.UserDetailsServiceImpl;
import software.digvijay.dinedash.utils.JwtUtil;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/getDetails")
    public ResponseEntity<?> getRestaurantDetails() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restaurantDetails = restaurantService.getRestaurantDetails(username);
        return new ResponseEntity<>(new RestaurantDetailsDTO(restaurantDetails), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody InputRestaurantDTO inputRestaurant) {
        try {
            Restaurant restaurant = new Restaurant(inputRestaurant);
            restaurantService.saveNewRestaurant(restaurant);
            return new ResponseEntity<>(new RestaurantDetailsDTO(restaurant), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Not saved", HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Restaurant restaurant){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(restaurant.getUsername(), restaurant.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(restaurant.getUsername());
            // Check if the user has the 'RESTAURANT' role
            boolean isRestaurant = userDetails.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_RESTAURANT"));

            if (!isRestaurant) {
                return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
            }
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-details")
    public ResponseEntity<?> updateRestaurantDetails(@RequestBody Restaurant newDetails) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restaurant = restaurantService.getRestaurantDetails(name);
        restaurantService.updateRestaurantDetails(restaurant, newDetails);
        return new ResponseEntity<>(new RestaurantDetailsDTO(restaurant), HttpStatus.OK);

    }

    @DeleteMapping("/delete-restaurant")
    public ResponseEntity<?> deleteRestaurant() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restaurant = restaurantService.getRestaurantDetails(name);
        restaurantService.deleteRestaurant(restaurant);
        return new ResponseEntity<>("Deleted Restaurant", HttpStatus.OK);
    }

    @PutMapping("/add-items")
    public ResponseEntity<?> addNewItems(@RequestBody List<MenuItem> items) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restaurant = restaurantService.getRestaurantDetails(name);
        restaurantService.addItemsToMenu(restaurant, items);
        return new ResponseEntity<>(new RestaurantDetailsDTO(restaurant), HttpStatus.OK);
    }

    @PutMapping("/update-item/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable ObjectId id, @RequestBody MenuItem item) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restaurant = restaurantService.getRestaurantDetails(name);
        restaurantService.updateMenuItem(restaurant, item, id);
        return new ResponseEntity<>(new RestaurantDetailsDTO(restaurant), HttpStatus.OK);
    }

    @DeleteMapping("/delete-item/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable ObjectId id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant restaurant = restaurantService.getRestaurantDetails(name);
        restaurantService.deleteMenuItem(restaurant, id);
        return new ResponseEntity<>(new RestaurantDetailsDTO(restaurant), HttpStatus.OK);
    }

}
