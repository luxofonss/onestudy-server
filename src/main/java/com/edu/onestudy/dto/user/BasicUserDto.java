package com.edu.onestudy.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BasicUserDto {

    private UUID id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

}
