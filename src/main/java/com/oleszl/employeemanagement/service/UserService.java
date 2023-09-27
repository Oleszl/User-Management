package com.oleszl.employeemanagement.service;

import com.oleszl.employeemanagement.dto.UserDto;
import com.oleszl.employeemanagement.dto.UserRequestDto;
import com.oleszl.employeemanagement.dto.UserResponseDto;
import com.oleszl.employeemanagement.dto.UserUpdateDto;

import java.time.LocalDate;

public interface UserService {

    UserDto createUser(UserRequestDto user);

    UserDto updateUser(Long userId, UserRequestDto updatedUser);

    void deleteUser(Long userId);

    UserResponseDto getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate);

    UserDto partialUpdateUser(Long userId, UserUpdateDto userUpdateDto);
}
