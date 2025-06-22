package com.edu.onestudy.mapper;

import com.edu.onestudy.dto.auth.AuthProfileResponse;
import com.edu.onestudy.dto.auth.AuthRegisterRequest;
import com.edu.onestudy.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User authRegisterRequestToUser(AuthRegisterRequest request);

    AuthProfileResponse userToAuthProfileResponse(User user);

}
