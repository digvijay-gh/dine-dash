package software.digvijay.dinedash.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.restaurant.Restaurant;

public interface MenuItemRepository extends MongoRepository<MenuItem, ObjectId> {
//    Restaurant findByUsername(String username);
//    void deleteByUsername(String username);
}
