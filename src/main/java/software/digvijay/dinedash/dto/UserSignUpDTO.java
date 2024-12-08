package software.digvijay.dinedash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import software.digvijay.dinedash.entity.user.UserAddress;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDTO {
    private String username;
    private String password;
    private String email;
//    private UserAddressDTO address;

}
