package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.schema.UserSchema;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserSchema> all = userService.getUsers();
        if(all != null && !all.isEmpty()) {
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
}
