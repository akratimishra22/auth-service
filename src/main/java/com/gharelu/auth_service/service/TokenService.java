package com.gharelu.auth_service.service;

import com.gharelu.auth_service.model.Token;
import com.gharelu.auth_service.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken(Long userId) {
        // generate a random token
        String token = UUID.randomUUID().toString(); // better than Math.random()

        Token tokenObj = new Token();
        tokenObj.setToken(token);
        tokenObj.setUserId(userId);
        tokenObj.setStatus("ACTIVE");

        tokenRepository.save(tokenObj);
        return token;
    }

    /*public String getTokenByUserId(Long userId) {
        Optional<Token> existingToken = tokenRepository.findByUserId(userId);
        return existingToken.map(Token::getToken).orElse(null);
    }*/

    public boolean validateToken(String token) {
        Token tokenObj = tokenRepository.findByToken(token);
        return tokenObj != null && "ACTIVE".equalsIgnoreCase(tokenObj.getStatus());
    }

    public boolean invalidateToken(String token) {
        Token tokenOpt = tokenRepository.findByToken(token);
        tokenOpt.setStatus("INACTIVE");
        tokenRepository.save(tokenOpt);
        return true;
    }

}

