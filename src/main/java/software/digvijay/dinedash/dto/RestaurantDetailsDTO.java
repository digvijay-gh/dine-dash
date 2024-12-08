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
public class RestaurantDetailsDTO {
    private String id;
    private String name;
    private String username;
    private String password;
    private String city;
    private String phoneNumber;
    private String address;
    private List<MenuItemDTO> menuItems=new ArrayList<>(); // Assuming MenuItem is your existing menu item class
    private Location location; // If you want to include location details
    private List<Order> pastOrders;

    public  RestaurantDetailsDTO(Restaurant restaurant){
        this.id=restaurant.getId().toString();
        this.name=restaurant.getName();
        this.username=restaurant.getUsername();
        this.password=restaurant.getPassword();
        this.city=restaurant.getCity();
        this.address=restaurant.getAddress();
        this.phoneNumber=restaurant.getPhoneNumber();
        this.location=restaurant.getLocation();
        for(MenuItem item:restaurant.getMenu()){
            this.menuItems.add(new MenuItemDTO(item));
        }
        this.pastOrders=restaurant.getPastOrders();
    }
}
