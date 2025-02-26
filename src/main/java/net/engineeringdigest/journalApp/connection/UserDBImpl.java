package net.engineeringdigest.journalApp.connection;

import net.engineeringdigest.journalApp.schema.UserSchema;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDBImpl {

    private final MongoTemplate mongoTemplate;

    public UserDBImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<UserSchema> getUsersForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        return mongoTemplate.find(query, UserSchema.class);
    }
}
