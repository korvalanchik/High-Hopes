package com.example.highhopes.auth;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class AuthenticateResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    public AuthenticateResponseDto(String jwttoken) {
        this.jwttoken = jwttoken;
    }

}