package software.digvijay.dinedash.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.user.MyCart;
import software.digvijay.dinedash.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private ObjectId orderId;
    private ObjectId customerId;
    private ObjectId restaurantId;
    private int amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderDate;
    @DBRef
    List<MenuItem> items = new ArrayList<>();
    public Order(ObjectId userId,MyCart cart){
        this.customerId=userId;
        this.restaurantId=cart.getRestaurantId();
        this.amount=cart.getDeliveryCost()+cart.getTotal();
        this.items=cart.getCartItems();
        this.setOrderDate(LocalDateTime.now());
    }
}
