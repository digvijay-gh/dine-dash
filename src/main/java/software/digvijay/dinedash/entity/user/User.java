package software.digvijay.dinedash.entity.user;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import software.digvijay.dinedash.dto.LoginDTO;
import software.digvijay.dinedash.dto.UserAddressDTO;
import software.digvijay.dinedash.dto.UserDTO;
import software.digvijay.dinedash.dto.UserSignUpDTO;
import software.digvijay.dinedash.entity.order.Order;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;

    private String email;
    @DBRef
    private UserAddress address;
    private MyCart cart;
    @DBRef
    private List<Order> pastOrders=new ArrayList<>();
    private List<String> roles;

    public User(LoginDTO inputData){
        this.username=inputData.username;
        this.password=inputData.password;

    }
    public User(UserDTO details){
        this.username = details.getUsername();
        this.password = details.getPassword();
        this.email = details.getEmail();
        this.address=new UserAddress(details.getAddress());
    }
    public User(UserSignUpDTO details){
        this.username = details.getUsername();
        this.password = details.getPassword();
        this.email = details.getEmail();
//        this.address=new UserAddress(details.getAddress());
    }
}
