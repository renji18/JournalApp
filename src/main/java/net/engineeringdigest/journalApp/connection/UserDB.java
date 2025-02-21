package net.engineeringdigest.journalApp.connection;

import net.engineeringdigest.journalApp.schema.UserSchema;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDB extends MongoRepository<UserSchema, ObjectId> {
    UserSchema findByUsername(String username);
}
