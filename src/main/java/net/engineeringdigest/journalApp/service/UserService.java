package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.config.AuthenticatedData;
import net.engineeringdigest.journalApp.connection.UserDB;
import net.engineeringdigest.journalApp.schema.UserSchema;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {
    private final UserDB userDB;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserDB userDB) {
        this.userDB = userDB;
    }

    @Transactional
    public boolean createUser(UserSchema user) {
        try{
            UserSchema userExists = userDB.findByUsername(user.getUsername());
            if (userExists != null) return false;

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singletonList("USER"));
            userDB.save(user);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean createAdmin(UserSchema user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userDB.save(user);
        return true;
    }

    public List<UserSchema> getUsers() {
        return userDB.findAll();
    }

    public Optional<UserSchema> getUser(ObjectId id) {
        return userDB.findById(id);
    }

    @Transactional
    public void deleteUser(ObjectId id) {
        userDB.deleteById(id);
    }

    public UserSchema getUserByUsername() {
        String username = AuthenticatedData.getAuthenticatedUsername();
        return userDB.findByUsername(username);
    }
}
