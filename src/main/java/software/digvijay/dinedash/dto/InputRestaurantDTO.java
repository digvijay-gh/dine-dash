package software.digvijay.dinedash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import software.digvijay.dinedash.entity.Location;
import software.digvijay.dinedash.entity.order.Order;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputRestaurantDTO {
    private String name;
    private String username;
    private String password;
    private Location location;
    private String city;

}
