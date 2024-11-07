package software.digvijay.dinedash.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.digvijay.dinedash.entity.restaurant.MenuItem;
import software.digvijay.dinedash.entity.user.UserAddress;

public interface UserAddressRepository extends MongoRepository<UserAddress, ObjectId> {
//    public void deleteById(ObjectId id);
}
