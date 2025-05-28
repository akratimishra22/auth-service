package com.gharelu.auth_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "token")
@Getter
@Setter
public class Token {

    @Id
    @Column(name = "token", nullable = false, length = 255)
    private String token;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", length = 20)
    private String status;
}
