package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.schema.UserSchema;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserSchema> getUserById() {
        UserSchema userInDB = userService.getUserByUsername();
        return ResponseEntity.ok(userInDB);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserSchema user) {
        UserSchema userInDB = userService.getUserByUsername();
        userInDB.setUsername(user.getUsername());
        userInDB.setPassword(user.getPassword());
        boolean success = userService.createUser(userInDB);
        if (success) return ResponseEntity.ok().body(userInDB);
        return ResponseEntity.badRequest().body("Error Creating user");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        UserSchema userInDB = userService.getUserByUsername();
        userService.deleteUser(userInDB.getId());
        return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");
    }
}
