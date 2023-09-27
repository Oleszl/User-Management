package com.oleszl.employeemanagement.mapper;

import com.oleszl.employeemanagement.dto.UserData;
import com.oleszl.employeemanagement.dto.UserDto;
import com.oleszl.employeemanagement.dto.UserRequestDto;
import com.oleszl.employeemanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "data", source = "user")
    UserDto mapUserToUserDto(User user);
    UserData userToUserData(User user);
    User mapUserRequestDtoToUser(UserRequestDto userRequestDto);

    @Mapping(target = "id", ignore = true)
    User updateUserFromDto(@MappingTarget User existingUser, UserRequestDto userRequestDto);

}
