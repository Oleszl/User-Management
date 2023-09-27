package com.oleszl.employeemanagement;

import com.oleszl.employeemanagement.dto.UserData;
import com.oleszl.employeemanagement.dto.UserDto;
import com.oleszl.employeemanagement.dto.UserRequestDto;
import com.oleszl.employeemanagement.dto.UserResponseDto;
import com.oleszl.employeemanagement.dto.UserUpdateDto;
import com.oleszl.employeemanagement.entity.User;

import java.time.LocalDate;
import java.util.List;

public class ModelUtils {
    public static UserRequestDto getUserRequestDto() {
        return UserRequestDto.builder()
                .firstName("Oles")
                .lastName("Sukmanovskyi")
                .email("oles@gmail.com")
                .address("Lviv")
                .birthDate(LocalDate.of(2001, 3, 15))
                .phoneNumber("380678456984")
                .build();
    }

    public static User getUser() {
        return User.builder()
                .firstName("Oles")
                .lastName("Sukmanovskyi")
                .email("oles@gmail.com")
                .address("Lviv")
                .birthDate(LocalDate.of(2001, 3, 15))
                .phoneNumber("380678456984")
                .build();
    }

    public static User getUserWithId() {
        return User.builder()
                .id(1L)
                .firstName("Oles")
                .lastName("Sukmanovskyi")
                .email("oles@gmail.com")
                .address("Lviv")
                .birthDate(LocalDate.of(2001, 3, 15))
                .phoneNumber("380678456984")
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .data(UserData.builder()
                        .id(1L)
                        .firstName("Oles")
                        .lastName("Sukmanovskyi")
                        .email("oles@gmail.com")
                        .address("Lviv")
                        .birthDate(LocalDate.of(2001, 3, 15))
                        .phoneNumber("380678456984")
                        .build())
                .build();
    }

    public static UserUpdateDto getUserUpdateDto() {
        return UserUpdateDto.builder()
                .phoneNumber("380678456982")
                .build();
    }

    public static UserResponseDto getUserResponseDto() {
        return UserResponseDto.builder()
                .data(List.of(UserData.builder()
                        .id(1L)
                        .firstName("Oles")
                        .lastName("Sukmanovskyi")
                        .email("oles@gmail.com")
                        .address("Lviv")
                        .birthDate(LocalDate.of(2001, 3, 15))
                        .phoneNumber("380678456984")
                        .build()))
                .build();
    }
}
