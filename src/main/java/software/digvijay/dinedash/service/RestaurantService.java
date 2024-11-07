package software.digvijay.dinedash.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.digvijay.dinedash.dto.RestaurantDetailsDTO;
import software.digvijay.dinedash.entity.Location;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.restaurant.Restaurant;
import software.digvijay.dinedash.repository.RestaurantRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RestaurantService {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private DistanceAPIService distanceAPIService;

    public Restaurant getRestaurantDetails(String username) {
        return restaurantRepository.findByUsername(username);
    }

    public Optional<Restaurant> getRestaurantById(ObjectId id) {
        return restaurantRepository.findById(id);
    }

    public List<RestaurantDetailsDTO> getRestaurantByCity(Location origin, String city) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("city").is(city));
            List<Restaurant> restaurants = mongoTemplate.find(query, Restaurant.class);
            List<RestaurantDetailsDTO> restaurantDetailsDTOS = distanceAPIService.restaurantsInCityResponse(restaurants, origin);
            return restaurantDetailsDTOS;
        } catch (Exception e) {
            log.error("Error while getting restaurant for {}", city, e);
            throw new RuntimeException(e);
        }

    }

    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public void saveNewRestaurant(Restaurant restaurant) {
        try {
            restaurant.setPassword(passwordEncoder.encode(restaurant.getPassword()));
            restaurant.setRoles(Arrays.asList("RESTAURANT"));
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            log.error("Error while saving restaurant: {}", restaurant.getName(), e);
            throw new RuntimeException(e);
        }
    }

    public void updateRestaurantDetails(Restaurant restaurant, Restaurant newDetails) {
        try {
            if (newDetails.getUsername() != null && !newDetails.getUsername().isEmpty()) {
                restaurant.setUsername(newDetails.getUsername());
            }

            if (newDetails.getPassword() != null && !newDetails.getPassword().isEmpty()) {
                restaurant.setPassword(passwordEncoder.encode(newDetails.getPassword()));
            }

            if (newDetails.getName() != null && !newDetails.getName().isEmpty()) {
                restaurant.setName(newDetails.getName());
            }

            if (newDetails.getLocation() != null) {
                restaurant.setLocation(newDetails.getLocation());
            }

            if (newDetails.getCity() != null && !newDetails.getCity().isEmpty()) {
                restaurant.setCity(newDetails.getCity());
            }
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            log.error("Error while updating restaurant details: {}", restaurant.getUsername(), e);
            throw new RuntimeException(e);
        }
    }

    public void deleteRestaurant(Restaurant restaurant) {
        try {
            for (MenuItem item : restaurant.getMenu()) {
                menuItemService.deleteItemById(item.getItemId());
            }
            restaurantRepository.delete(restaurant);
        } catch (Exception e) {
            log.error("Error while deleting restaurant {}", restaurant.getUsername(), e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void addItemsToMenu(Restaurant restaurant, List<MenuItem> items) {
        try {
            for (MenuItem item : items) {
                item.setRestaurantId(restaurant.getId());
                menuItemService.saveItem(item);
            }
            restaurant.getMenu().addAll(items);
            saveRestaurant(restaurant);
            log.info("Saved restaurant: {}", restaurant);
        } catch (Exception e) {
            log.error("Error while adding items {} to {}", items.toString(), restaurant.getUsername(), e);
            throw new RuntimeException(e);
        }

    }

    public void updateMenuItem(Restaurant restaurant, MenuItem newItem, ObjectId id) {
        try {
            List<MenuItem> list = restaurant.getMenu().stream().filter(x -> x.getItemId().equals(id)).collect(Collectors.toList());
            if (!list.isEmpty()) {
                menuItemService.updateItem(id, newItem);
            }
        } catch (Exception e) {
            log.error("Error while updating menu item {} for restaurant {}", id, restaurant.getUsername(), e);
            throw new RuntimeException(e);
        }
    }

    public void deleteMenuItem(Restaurant restaurant, ObjectId id) {
        try {
            boolean removed = restaurant.getMenu().removeIf(x -> x.getItemId().equals(id));
            if (removed) {
                menuItemService.deleteItemById(id);
            }
        } catch (Exception e) {
            log.error("Error while deleting menuItem {} from restaurant {}", id, restaurant.getUsername(), e);
            throw new RuntimeException(e);
        }
    }


}
