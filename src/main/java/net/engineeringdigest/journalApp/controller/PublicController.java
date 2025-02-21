package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.schema.UserSchema;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
class PublicController {
    private final UserService userService;

    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health-check")
    String healthCheck() {
        return "OK";
    }

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody UserSchema userSchema) {
        boolean success = userService.createUser(userSchema);
        if (success)
            return ResponseEntity.ok().body(userSchema);
        return ResponseEntity.badRequest().body("Error creating user");
    }
}
