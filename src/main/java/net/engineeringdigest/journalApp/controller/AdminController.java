package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.connection.UserDBImpl;
import net.engineeringdigest.journalApp.schema.UserSchema;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserDBImpl userDBImpl;
    private final EmailService emailService;

    public AdminController(UserService userService, UserDBImpl userDBImpl, EmailService emailService) {
        this.userService = userService;
        this.userDBImpl = userDBImpl;
        this.emailService = emailService;
    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserSchema> all = userService.getUsers();
        if (all != null && !all.isEmpty()) {
            return ResponseEntity.ok(all);
        }
        return ResponseEntity.ok().body("No users found");
    }

    @PostMapping("/new")
    public ResponseEntity<?> createUser(@RequestBody UserSchema user) {
        boolean success = userService.createAdmin(user);
        if (success)
            return ResponseEntity.ok().body(user);
        return ResponseEntity.badRequest().body("Error creating user");
    }

    @GetMapping("/sa")
    public ResponseEntity<String> getSAUser() {
        List<UserSchema> all = userDBImpl.getUsersForSA();
        if (all != null && !all.isEmpty()) {
            all.forEach(u -> emailService.sendEmail(u.getEmail(), "Weekly Sentiment Analysis", "Your Sentiment Analysis for Weekly Sentiment Analysis"));
            return ResponseEntity.ok().body("Emails sent to Weekly Sentiment Analysis");
        }
        return ResponseEntity.ok().body("No users found");
    }
}
