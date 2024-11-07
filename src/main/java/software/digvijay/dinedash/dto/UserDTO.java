package software.digvijay.dinedash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.digvijay.dinedash.entity.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;

    private String username;

    private String password;

    private String email;

    private UserAddressDTO address;

    public UserDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.address=new UserAddressDTO(user.getAddress());

//        for (UserAddress userAddress : user.getAddressList()) {
//            this.addressList.add(new UserAddressDTO(userAddress));
//        }
    }

}
