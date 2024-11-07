package software.digvijay.dinedash.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import software.digvijay.dinedash.entity.restaurant.MenuItem;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCart {
    private ObjectId restaurantId;
    private int total;
    private int deliveryCost;
    private int deliveryDistance;
    private int deliveryDuration;
    private List<MenuItem> cartItems = new ArrayList<>();
}
