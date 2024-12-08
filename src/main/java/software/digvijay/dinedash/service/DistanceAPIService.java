package software.digvijay.dinedash.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import software.digvijay.dinedash.dto.NearbyRestaurantDTO;

import software.digvijay.dinedash.entity.*;
import software.digvijay.dinedash.entity.responses.GoogleMapsResponse;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.restaurant.Restaurant;
import software.digvijay.dinedash.repository.RestaurantRepository;

import java.util.*;

@Slf4j
@Service
public class DistanceAPIService {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    @Value("${API.GOOGLE_MAPS}")
    private String API_KEY ;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemService menuItemService;

    public List<NearbyRestaurantDTO> restaurantsInCityResponse(List<Restaurant> restaurants, Location source) {
        try {
            String origin = source.getLocationString();
            String destinations = getDestinationString(restaurants);
            GoogleMapsResponse body = getGoogleMapsResponse(origin, destinations);
            if (body != null) {
                List<NearbyRestaurantDTO> restaurantDetailsList = addDistanceAndDurationToRestaurantList(restaurants, body);
                restaurantDetailsList.sort(Comparator.comparingInt(NearbyRestaurantDTO::getDistance));
                return restaurantDetailsList;
            }
            return Collections.emptyList();
        } catch (RestClientException e) {
            log.error("Error while fetching restaurants for {}", source.toString(), e);
            throw new RuntimeException(e);
        }

    }

    private List<NearbyRestaurantDTO> addDistanceAndDurationToRestaurantList(List<Restaurant> restaurants, GoogleMapsResponse body) {
        List<GoogleMapsResponse.Element> elements = body.getRows().get(0).getElements();
        List<NearbyRestaurantDTO> restaurantDetailsList = new ArrayList<>();
        for (int i = 0; i < restaurants.size(); i++) {
            NearbyRestaurantDTO restaurantDetails = new NearbyRestaurantDTO(restaurants.get(i));
            if (elements.get(i).getDistance() != null)
                restaurantDetails.setDistance(elements.get(i).getDistance().getValue());
            if (elements.get(i).getDuration() != null)
                restaurantDetails.setDuration(elements.get(i).getDuration().getValue());
            restaurantDetailsList.add(restaurantDetails);
        }
        return restaurantDetailsList;
    }

    private GoogleMapsResponse getGoogleMapsResponse(String origin, String destinations) {
        String url = String.format("%s?origins=%s&destinations=%s&key=%s", BASE_URL, origin, destinations, API_KEY);
        ResponseEntity<GoogleMapsResponse> googleMapsResponseResponseEntity = restTemplate.exchange(url, HttpMethod.GET, null, GoogleMapsResponse.class);
        return googleMapsResponseResponseEntity.getBody();
    }

    private String getDestinationString(List<Restaurant> restaurants) {
        List<String> destinationList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            destinationList.add(restaurant.getLocation().getLocationString());
        }
        return String.join("|", destinationList);
    }


    public Pair<Integer, Integer> getDistanceAndDurationFromItem(Location userLocation, ObjectId menuItemId) {
        Optional<MenuItem> optionalMenuItem = menuItemService.getMenuItemById(menuItemId);
        if (optionalMenuItem.isPresent()) {
            MenuItem menuItem = optionalMenuItem.get();
            ObjectId restaurantId = menuItem.getRestaurantId();
            Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
            if (optionalRestaurant.isPresent()) {
                Restaurant restaurant = optionalRestaurant.get();
                Location restaurantLocation = restaurant.getLocation();
                String origin = userLocation.getLocationString();
                String destination = restaurantLocation.getLocationString();
                // Creating final URL
                GoogleMapsResponse body = getGoogleMapsResponse(origin, destination);
                if (body != null) {
                    GoogleMapsResponse.Element element = body.getRows().get(0).getElements().get(0);
                    return Pair.of(element.getDistance().getValue(), element.getDuration().getValue());
                }
            }
        }
        return Pair.of(-1, -1);
    }
}
