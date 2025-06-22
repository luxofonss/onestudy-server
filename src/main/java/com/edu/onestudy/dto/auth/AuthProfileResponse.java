package com.edu.onestudy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthProfileResponse {

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String gender;

    private String role;

    private String avatar;

    private Boolean isVerified;

}
