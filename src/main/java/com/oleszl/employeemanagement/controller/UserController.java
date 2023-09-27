package com.oleszl.employeemanagement.controller;

import com.oleszl.employeemanagement.dto.UserDto;
import com.oleszl.employeemanagement.dto.UserRequestDto;
import com.oleszl.employeemanagement.dto.UserResponseDto;
import com.oleszl.employeemanagement.dto.UserUpdateDto;
import com.oleszl.employeemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserDto userDto = userService.createUser(userRequestDto);
        String location = "/api/users/" + userDto.getData().getId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", location)
                .body(userDto);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "User deleted successfully";
    }

    @GetMapping
    public UserResponseDto getUsersByBirthDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return userService.getUsersByBirthDateRange(from, to);
    }

    @PatchMapping("/{userId}")
    public UserDto partialUpdateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.partialUpdateUser(userId, userUpdateDto);
    }
}
