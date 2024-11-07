package software.digvijay.dinedash.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.digvijay.dinedash.entity.restaurant.Restaurant;
import software.digvijay.dinedash.entity.user.User;

public interface RestaurantRepository extends MongoRepository<Restaurant, ObjectId> {
    Restaurant findByUsername(String username);
    void deleteByUsername(String username);
}
