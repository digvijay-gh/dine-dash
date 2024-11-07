package software.digvijay.dinedash.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.user.MyCart;
import software.digvijay.dinedash.entity.user.User;
import software.digvijay.dinedash.repository.MenuItemRepository;
import software.digvijay.dinedash.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
public class CartService {
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DistanceAPIService distanceAPIService;

    public void addItemToUserCart(ObjectId menuItemId, User user) {
        try {
            Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(menuItemId);
            if (optionalMenuItem.isPresent()) {
                MenuItem menuItem = optionalMenuItem.get();
                if (user.getCart() == null || user.getCart().getCartItems().isEmpty() || user.getCart().getRestaurantId() != menuItem.getRestaurantId()) {

                    // clear the cart
                    user.setCart(new MyCart());

                    //set restaurant id for cart
                    user.getCart().setRestaurantId(menuItem.getRestaurantId());

                    //add item to cart
                    user.getCart().getCartItems().add(menuItem);

                    //update total cost
                    user.getCart().setTotal(user.getCart().getTotal() + menuItem.getPrice());

                    //fetch distance and duration
                    Pair<Integer, Integer> distanceAndDurationFromItem = distanceAPIService.getDistanceAndDurationFromItem(user.getAddress().getLocation(), menuItemId);
                    //set delivery cost,distance,duration
                    user.getCart().setDeliveryCost(distanceAndDurationFromItem.getFirst() / 100);
                    user.getCart().setDeliveryDistance(distanceAndDurationFromItem.getFirst());
                    user.getCart().setDeliveryDuration(distanceAndDurationFromItem.getSecond());
                } else {
                    user.getCart().getCartItems().add(menuItem);
                    user.getCart().setTotal(user.getCart().getTotal() + menuItem.getPrice());
                }
                userRepository.save(user);
            }
        } catch (Exception e) {
            log.error("Error adding to cart for {}, item id: {}", user.getUsername(), menuItemId, e);
            throw new RuntimeException(e);
        }

    }

    public void removeFromCart(ObjectId id, User user) {
        try {
            boolean removed = false;
            removed = user.getCart().getCartItems().removeIf(x -> x.getItemId().equals(id));
            if (removed) {
                Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(id);
                if (optionalMenuItem.isPresent()) {
                    MenuItem menuItem = optionalMenuItem.get();
                    user.getCart().setTotal(user.getCart().getTotal() - menuItem.getPrice());
                    if (user.getCart().getCartItems().isEmpty()) {
                        user.setCart(new MyCart());
                    }
                    userRepository.save(user);
                }
            }
        } catch (Exception e) {
            log.error("Error removing from cart for {}", user.getUsername(), e);
            throw new RuntimeException("An error occured while removing from cart ", e);
        }
    }

}
