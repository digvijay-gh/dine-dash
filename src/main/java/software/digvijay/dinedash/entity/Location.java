package software.digvijay.dinedash.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {

    private double latitude;
    private double longitude;
    public String getLocationString(){
        return latitude+","+longitude;
    }
}
