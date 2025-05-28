package com.gharelu.auth_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "credentials")
@Getter
@Setter
public class Credential {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "password", length = 20)
    private String password;

}
