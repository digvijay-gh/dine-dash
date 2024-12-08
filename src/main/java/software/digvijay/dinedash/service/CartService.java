package software.digvijay.dinedash.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.digvijay.dinedash.entity.order.Order;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.restaurant.Restaurant;
import software.digvijay.dinedash.entity.user.MyCart;
import software.digvijay.dinedash.entity.user.User;
import software.digvijay.dinedash.enums.PaymentStatus;
import software.digvijay.dinedash.repository.MenuItemRepository;
import software.digvijay.dinedash.repository.OrderRepository;
import software.digvijay.dinedash.repository.RestaurantRepository;
import software.digvijay.dinedash.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class CartService {
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private DistanceAPIService distanceAPIService;
    @Autowired
    private OrderRepository orderRepository;

    public void addItemToUserCart(ObjectId menuItemId, User user) {
        try {
            Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(menuItemId);
            if (optionalMenuItem.isPresent()) {
                MenuItem menuItem = optionalMenuItem.get();
                if (user.getCart() == null || user.getCart().getCartItems().isEmpty() || !user.getCart().getRestaurantId().equals(menuItem.getRestaurantId())) {
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
                    log.info("Adding to existing cart", menuItem.getName());
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

    public void emptyCart(User user) {
        try {
            user.setCart(new MyCart());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Transactional
    public void placeOrder(User user) {
        try {
            Order order = new Order(user.getId(), user.getCart());
            Random random = new Random();
            PaymentStatus[] statuses = {PaymentStatus.SUCCESSFUL, PaymentStatus.FAILED};
            order.setPaymentStatus(statuses[random.nextInt(statuses.length)]);
            orderRepository.save(order);
            log.info("Added order to db");
            // adding order to user history
//            if (user.getPastOrders()== null || user.getPastOrders().isEmpty()) {
//                user.setPastOrders(List.of(order));
//            } else {
                user.getPastOrders().add(order);
//            }
            userRepository.save(user);
            log.info("Added order to user ");
            //get restaurant
            ObjectId restaurantId = order.getRestaurantId();
            Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
            if (optionalRestaurant.isPresent()) {
                //adding order to restaurant past orders
                Restaurant restaurant = optionalRestaurant.get();
                restaurant.getPastOrders().add(order);
                restaurantRepository.save(restaurant);
                log.info("Added order to restaurant");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
