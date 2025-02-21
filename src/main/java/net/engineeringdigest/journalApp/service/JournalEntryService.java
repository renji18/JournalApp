package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.config.AuthenticatedData;
import net.engineeringdigest.journalApp.connection.UserDB;
import net.engineeringdigest.journalApp.schema.JournalSchema;
import net.engineeringdigest.journalApp.connection.JournalDB;
import net.engineeringdigest.journalApp.schema.UserSchema;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    private final JournalDB journalDB;
    private final UserDB userDB;
    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    public JournalEntryService(JournalDB journalDB, UserDB userDB) {
        this.journalDB = journalDB;
        this.userDB = userDB;
    }

    @Transactional
    public boolean saveEntry(JournalSchema journalSchema) {
        try {
            String username = AuthenticatedData.getAuthenticatedUsername();
            UserSchema user = userDB.findByUsername(username);
            if(user == null)
                return false;
            JournalSchema journal = journalDB.save(journalSchema);
            user.getJournalEntries().add(journal);
            userDB.save(user);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public List<JournalSchema> getJournalEntries() {
        String username = AuthenticatedData.getAuthenticatedUsername();
        UserSchema user = userDB.findByUsername(username);
        return user.getJournalEntries();
    }

    public Optional<JournalSchema> getJournalEntry(ObjectId journalEntryId) {
        return journalDB.findById(journalEntryId);
    }

    public void updateJournalEntry(JournalSchema journalSchema) {
        journalDB.save(journalSchema);
    }

    @Transactional
    public void deleteJournalEntry(ObjectId journalEntryId) {
        journalDB.deleteById(journalEntryId);
    }

    public UserSchema getUser(String username) {
        return userDB.findByUsername(username);
    }

    public boolean isUserAssociated(ObjectId journalEntryId) {
        String username = AuthenticatedData.getAuthenticatedUsername();
        UserSchema user = userDB.findByUsername(username);
        if(user == null) return false;
        return user.getJournalEntries().stream().anyMatch(entry -> entry.getId().equals(journalEntryId));
    }
}
