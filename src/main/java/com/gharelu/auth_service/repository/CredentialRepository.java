package com.gharelu.auth_service.repository;

import com.gharelu.auth_service.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    List<Credential> findByPhone(String phone);

    Optional<Credential> findByEmail(String email);

}


