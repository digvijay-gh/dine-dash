package software.digvijay.dinedash.service;


import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import software.digvijay.dinedash.entity.Location;
import software.digvijay.dinedash.entity.restaurant.Restaurant;

import java.util.List;
import java.util.stream.Stream;

public class ListRestaurantArgsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(Arguments.of(List.of(
                        Restaurant.builder().username("1").password("1").location(Location.builder().latitude(28.382085).longitude(79.426722).build()).build(),
                        Restaurant.builder().username("2").password("2").location(Location.builder().latitude(28.377403).longitude(79.428986).build()).build(),
                        Restaurant.builder().username("3").password("3").location(Location.builder().latitude(28.394519).longitude(79.456603).build()).build(),
//                        Restaurant.builder().username("4").password("4").location(Location.builder().latitude(28.347366).longitude(28.347366).build()).build(),
                        Restaurant.builder().username("5").password("5").location(new Location(28.356534, 79.422193)).build()))
//                        Restaurant.builder().username("6").location(Location.builder().latitude(1.23122).longitude(1.13124).build()).build()))



        );
    }
}