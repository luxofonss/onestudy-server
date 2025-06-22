package com.edu.onestudy.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginRequest {

    @NotBlank
    String username;

    @NotBlank
    String password;

}
