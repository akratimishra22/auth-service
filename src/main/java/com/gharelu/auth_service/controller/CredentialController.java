package com.gharelu.auth_service.controller;

import com.gharelu.auth_service.model.Credential;
import com.gharelu.auth_service.model.CredentialView;
import com.gharelu.auth_service.repository.CredentialRepository;
import com.gharelu.auth_service.service.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.jar.JarOutputStream;

import static io.micrometer.common.util.StringUtils.isBlank;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*")
public class CredentialController {

    @Autowired  // Dependency Injection
    CredentialRepository credentialRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody CredentialView credentialView) {
        Credential credential = new Credential();
        credential.setId((long) (Math.random() * 100000000));
        credential.setEmail(credentialView.getEmail());
        credential.setPhone(credentialView.getPhone());
        credential.setPassword(credentialView.getPassword());
        credentialRepository.save(credential);
        return ResponseEntity.ok("Credential saved successfully!");
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody CredentialView credentialView, HttpServletResponse response) {
        if (isBlank(credentialView.getEmail()) && isBlank(credentialView.getPhone())) {
            return ResponseEntity.badRequest().body("Email or Phone cannot be empty!");
        }
        if (isBlank(credentialView.getPassword())) {
            return ResponseEntity.badRequest().body("Password cannot be empty!");
        }

        Credential credential;

        if (isBlank(credentialView.getEmail())) {
            credential = credentialRepository.findByPhone(credentialView.getPhone())
                    .stream()
                    .findFirst()
                    .orElse(null);
        } else {
            credential = credentialRepository.findByEmail(credentialView.getEmail())
                    .orElse(null);
        }

        if (credential == null) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }

        if (!credential.getPassword().equals(credentialView.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }

        // Generate token for authenticated user
        String token = tokenService.generateToken(credential.getId());
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body("Login successful.");
    }

    /*@PostMapping("getToken")
    public ResponseEntity<String> getToken(@RequestBody Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID cannot be null!");
        }

        String token = tokenService.getTokenByUserId(userId);
        if (token == null) {
            // Generate a new token if none exists
            token = tokenService.generateToken(userId);
        }

        return ResponseEntity.ok(token);
    }*/

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println("enter validateToken auth srevice");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }
        System.out.println("authheader"+authHeader.substring(7));
        String token = authHeader.substring(7); // Remove "Bearer " prefix

        if (tokenService.validateToken(token)) {
            return ResponseEntity.ok("Valid");
        } else {
            return ResponseEntity.status(401).body("Invalid");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        boolean success = tokenService.invalidateToken(token);
        if (success) {
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }


}