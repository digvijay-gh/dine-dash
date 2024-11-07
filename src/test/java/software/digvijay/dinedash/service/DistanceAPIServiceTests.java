package software.digvijay.dinedash.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.digvijay.dinedash.entity.Location;
import software.digvijay.dinedash.entity.responses.GoogleMapsResponse;
import software.digvijay.dinedash.entity.restaurant.Restaurant;

import java.util.List;

@SpringBootTest
@Slf4j
public class DistanceAPIServiceTests {
    @Autowired
    private DistanceAPIService distanceAPIService;
//@Disabled
//    @ParameterizedTest
//    @ArgumentsSource(ListRestaurantArgsProvider.class)
//    public void apiTest(List<Restaurant> restaurants){
//
//        GoogleMapsResponse googleMapsResponse = distanceAPIService.locationResponse(restaurants, new Location(28.358359, 79.420591));
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            String str=mapper.writeValueAsString(googleMapsResponse);
//            System.out.println(str);
//        } catch (JsonProcessingException e) {
//            log.error("JsonProcessingException");
//            throw new RuntimeException(e);
//        }
//    }

}
