package net.engineeringdigest.journalApp.connection;

import net.engineeringdigest.journalApp.schema.JournalSchema;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalDB extends MongoRepository<JournalSchema, ObjectId> {
}
