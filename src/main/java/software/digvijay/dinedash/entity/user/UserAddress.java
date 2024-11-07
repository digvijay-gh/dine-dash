package software.digvijay.dinedash.entity.user;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import software.digvijay.dinedash.entity.Location;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_addresses")
public class UserAddress {
    private ObjectId id;
    @NonNull
    private String streetAddress;
    @NonNull
    private String city;
    @NonNull
    private Location location;

}
