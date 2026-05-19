package com.engineeringdigest.http_request_demo.mapper;

import com.engineeringdigest.http_request_demo.dto.UserPatchRequest;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.dto.UserRequest;
import com.engineeringdigest.http_request_demo.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserRequest userRequest);

    User toUser(UserPatchRequest userRequest);

    UserResponse toUserResponse(User user);
}
