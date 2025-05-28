package com.gharelu.auth_service.controller;

import com.gharelu.auth_service.model.Credential;
import com.gharelu.auth_service.model.CredentialView;
import com.gharelu.auth_service.repository.CredentialRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.micrometer.common.util.StringUtils.isBlank;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*")
public class CredentialController {

    @Autowired  // Dependency Injection
    CredentialRepository credentialRepository;

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
        if (isBlank(credentialView.getEmail())) {
            Credential credential = credentialRepository.findByPhone(credentialView.getPhone()).stream().findFirst().get();
            if (credential.getPassword().equals(credentialView.getPassword())) {

                return ResponseEntity.ok().body(credential.getId().toString());

            } else {
                return ResponseEntity.badRequest().body("Invalid credentials!");
            }
        } else {
            Credential credential = credentialRepository.findByEmail(credentialView.getEmail()).get();
            if (credential.getPassword().equals(credentialView.getPassword())) {
                return ResponseEntity.ok().body(credential.getId().toString());
            } else {
                return ResponseEntity.badRequest().body("Invalid credentials!");
            }
        }

    }
}