package com.gharelu.auth_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialView
{
    private String email;
    private String phone;
    private String password;
}
