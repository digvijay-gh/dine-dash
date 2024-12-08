package software.digvijay.dinedash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import software.digvijay.dinedash.entity.order.Order;
import software.digvijay.dinedash.entity.user.MyCart;
import software.digvijay.dinedash.entity.user.User;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;

    private String username;

    private String password;

    private String email;

    private UserAddressDTO address;
    private MyCart cart=new MyCart();
    private List<Order> pastOrders=new ArrayList<>();
    private List<String> roles;

    public UserDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.address=new UserAddressDTO(user.getAddress());
        this.cart=user.getCart();
        this.pastOrders=user.getPastOrders();
        this.roles=user.getRoles();

//        for (UserAddress userAddress : user.getAddressList()) {
//            this.addressList.add(new UserAddressDTO(userAddress));
//        }
    }

}
