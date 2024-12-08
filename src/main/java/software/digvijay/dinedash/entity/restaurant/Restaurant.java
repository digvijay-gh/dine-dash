package software.digvijay.dinedash.entity.restaurant;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import software.digvijay.dinedash.dto.LoginDTO;
import software.digvijay.dinedash.dto.RestaurantDetailsDTO;
import software.digvijay.dinedash.dto.RestaurantSignUpDTO;
import software.digvijay.dinedash.entity.Location;
import software.digvijay.dinedash.entity.order.Order;


import java.util.ArrayList;
import java.util.List;

//import software.digvijay.dinedash.entity.Location;
@Document(collection = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant {
    @Id
    private ObjectId id;
    private String name;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;
    private Location location;
    private String address;
    private String city;
    private String phoneNumber;
    @DBRef
    private List<MenuItem> menu = new ArrayList<>();
    @DBRef
    private List<Order> pastOrders = new ArrayList<>();
    private List<String> roles;

    public Restaurant(RestaurantSignUpDTO restaurantDTO) {
        this.name = restaurantDTO.getName();
        this.username = restaurantDTO.getUsername();
        this.password = restaurantDTO.getPassword();
        this.location = restaurantDTO.getLocation();
        this.city = restaurantDTO.getCity();
        this.address = restaurantDTO.getAddress();
    }

    public Restaurant(RestaurantDetailsDTO restaurantDTO) {
        this.name = restaurantDTO.getName();
        this.username = restaurantDTO.getUsername();
        this.password = restaurantDTO.getPassword();
        this.location = restaurantDTO.getLocation();
        this.city = restaurantDTO.getCity();
        this.address = restaurantDTO.getAddress();
        this.phoneNumber = restaurantDTO.getPhoneNumber();
    }

    public Restaurant(LoginDTO details) {
        this.username = details.username;
        this.password = details.password;
    }

}
