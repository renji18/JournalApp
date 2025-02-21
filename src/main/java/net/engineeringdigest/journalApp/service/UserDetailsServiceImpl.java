package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.connection.UserDB;
import net.engineeringdigest.journalApp.schema.UserSchema;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDB userDB;

    public UserDetailsServiceImpl(UserDB userDB) {
        this.userDB = userDB;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSchema user = userDB.findByUsername(username);
        if(user != null) {
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}
