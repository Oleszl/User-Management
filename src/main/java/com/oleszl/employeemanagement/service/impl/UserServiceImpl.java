package com.oleszl.employeemanagement.service.impl;

import com.oleszl.employeemanagement.dto.UserData;
import com.oleszl.employeemanagement.dto.UserDto;
import com.oleszl.employeemanagement.dto.UserRequestDto;
import com.oleszl.employeemanagement.dto.UserResponseDto;
import com.oleszl.employeemanagement.dto.UserUpdateDto;
import com.oleszl.employeemanagement.entity.User;
import com.oleszl.employeemanagement.exception.AgeValidationException;
import com.oleszl.employeemanagement.exception.InvalidDateRangeException;
import com.oleszl.employeemanagement.exception.ResourceNotFoundException;
import com.oleszl.employeemanagement.mapper.UserMapper;
import com.oleszl.employeemanagement.repository.UserRepository;
import com.oleszl.employeemanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${user.minAge}")
    private Integer minAge;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDto createUser(UserRequestDto userRequestDto) {
        if (isOlderThanMinAge(userRequestDto.getBirthDate())) {

            User userToSave = userMapper.mapUserRequestDtoToUser(userRequestDto);
            User savedUser = userRepository.save(userToSave);

            log.info("User with id: {} was saved", savedUser.getId());
            return userMapper.mapUserToUserDto(savedUser);
        } else {
            log.error("User must be at least {} years old. Birthdate provided: {}", minAge, userRequestDto.getBirthDate());
            throw new AgeValidationException("User must be at least " + minAge + " years old");
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        userMapper.updateUserFromDto(existingUser, userRequestDto);
        User updatedUser = userRepository.save(existingUser);

        log.info("User with id: {} was updated", updatedUser.getId());

        return userMapper.mapUserToUserDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        userRepository.deleteById(userId);
        log.info("User with id: {} was deleted", existingUser.getId());
    }

    @Override
    @Transactional
    public UserResponseDto getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            log.error("Invalid date range was provided: from date: {} to date: {}", fromDate, toDate);
            throw new InvalidDateRangeException("'From' date must be before 'To' date");
        }

        List<User> existingUsers = userRepository.findByBirthDateBetween(fromDate, toDate);
        List<UserData> userDataList = existingUsers.stream()
                .map(userMapper::userToUserData)
                .collect(Collectors.toList());

        log.info("Retrieved user data for date range: from date: {} to date: {}", fromDate, toDate);

        return new UserResponseDto(userDataList);
    }

    @Override
    public UserDto partialUpdateUser(Long userId, UserUpdateDto userRequestDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        existingUser.setPhoneNumber(userRequestDto.getPhoneNumber());

        log.info("User with id: {} was updated", existingUser.getId());

        return userMapper.mapUserToUserDto(userRepository.save(existingUser));
    }

    private boolean isOlderThanMinAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        long years = ChronoUnit.YEARS.between(birthDate, currentDate);

        return years >= minAge;
    }
}
