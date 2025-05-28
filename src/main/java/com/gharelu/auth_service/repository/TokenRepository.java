package com.gharelu.auth_service.repository;

import com.gharelu.auth_service.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
    Token findByToken(String token);
    /*Optional<Token> findByUserId(Long userId);*/
}

