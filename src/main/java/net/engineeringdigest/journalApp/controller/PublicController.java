package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.schema.UserSchema;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
class PublicController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public PublicController(UserService userService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
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

    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(@RequestBody UserSchema userSchema) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSchema.getUsername(), userSchema.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(userSchema.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok().body(jwt);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Incorrect username or password");
        }
    }
}
