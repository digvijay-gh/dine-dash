package software.digvijay.dinedash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import software.digvijay.dinedash.entity.Location;
import software.digvijay.dinedash.entity.user.UserAddress;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDTO {
    private String id;

    private String streetAddress;

    private String city;

    private Location location;

    public UserAddressDTO(UserAddress userAddress){
        if(userAddress!=null){
            this.id=userAddress.getId().toString();
            this.streetAddress=userAddress.getStreetAddress();
            this.city=userAddress.getCity();
            this.location=userAddress.getLocation();
        }
    }
}
