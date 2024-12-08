package software.digvijay.dinedash.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.digvijay.dinedash.entity.order.Order;
import software.digvijay.dinedash.entity.restaurant.MenuItem;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

}
