package software.digvijay.dinedash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.digvijay.dinedash.entity.Location;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSignUpDTO {
    private String name;
    private String username;
    private String password;
    private Location location;
    private String city;
    private String address;
}
