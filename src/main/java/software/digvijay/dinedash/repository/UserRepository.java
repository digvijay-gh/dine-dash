package software.digvijay.dinedash.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.digvijay.dinedash.entity.user.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
    void deleteByUsername(String username);
}
