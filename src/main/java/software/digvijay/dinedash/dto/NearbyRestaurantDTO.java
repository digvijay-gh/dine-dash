package software.digvijay.dinedash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.digvijay.dinedash.entity.Location;
import software.digvijay.dinedash.entity.order.Order;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbyRestaurantDTO {
    private String id;
    private String name;
    private String city;
    private String address;

    private String phoneNumber;
    private int distance;
    private int duration;
    private List<MenuItemDTO> menuItems = new ArrayList<>(); // Assuming MenuItem is your existing menu item class
    private Location location; // If you want to include location details


    public NearbyRestaurantDTO(Restaurant restaurant) {
        this.id = restaurant.getId().toString();
        this.name = restaurant.getName();
        this.city = restaurant.getCity();
        this.address=restaurant.getAddress();
        this.location = restaurant.getLocation();
        for (MenuItem item : restaurant.getMenu()) {
            this.menuItems.add(new MenuItemDTO(item));
        }
    }
}