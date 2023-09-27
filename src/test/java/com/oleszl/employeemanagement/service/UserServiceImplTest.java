package com.oleszl.employeemanagement.service;

import com.oleszl.employeemanagement.ModelUtils;
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
import com.oleszl.employeemanagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(userService, "minAge", 18);
    }

    @Test
    void saveEmployeeTest() {
        UserRequestDto requestDto = ModelUtils.getUserRequestDto();
        User userToSave = ModelUtils.getUser();
        UserDto expectedUserDto = ModelUtils.getUserDto();

        when(userMapper.mapUserRequestDtoToUser(requestDto)).thenReturn(userToSave);
        when(userRepository.save(any(User.class))).thenReturn(userToSave);
        when(userMapper.mapUserToUserDto(userToSave)).thenReturn(expectedUserDto);

        UserDto result = userService.createUser(requestDto);

        assertEquals(expectedUserDto, result);

    }

    @Test
    void saveEmployeeThrowAgeValidationExceptionTest() {
        UserRequestDto requestDto = ModelUtils.getUserRequestDto();
        requestDto.setBirthDate(LocalDate.now().minusYears(10));
        assertThrows(AgeValidationException.class, () -> userService.createUser(requestDto));
    }

    @Test
    void updateUserTest() {
        Long userId = 1L;
        UserRequestDto requestDto = ModelUtils.getUserRequestDto();
        User existingUser = ModelUtils.getUserWithId();
        User updatedUser = ModelUtils.getUserWithId();
        UserDto expectedUserDto = ModelUtils.getUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(existingUser));
        when(userMapper.updateUserFromDto(existingUser, requestDto)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.mapUserToUserDto(updatedUser)).thenReturn(expectedUserDto);

        UserDto result = userService.updateUser(userId, requestDto);

        assertEquals(expectedUserDto, result);

    }

    @Test
    void updateUserThrowResourceNotFoundExceptionTest() {
        Long userId = 1L;
        UserRequestDto requestDto = ModelUtils.getUserRequestDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, requestDto));
    }

    @Test
    void deleteUserTest() {
        Long userId = 1L;
        User existingUser = ModelUtils.getUserWithId();

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(existingUser));

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);

    }

    @Test
    void deleteUserThrowResourceNotFoundExceptionTest() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void getUsersByBirthDateRangeTest() {
        LocalDate fromDate = LocalDate.of(2001, 1, 1);
        LocalDate toDate = LocalDate.of(2001, 12, 31);
        ;
        List<User> existingUsers = List.of(ModelUtils.getUserWithId());

        when(userRepository.findByBirthDateBetween(fromDate, toDate)).thenReturn(existingUsers);
        when(userMapper.userToUserData(any(User.class))).thenReturn(new UserData());

        UserResponseDto result = userService.getUsersByBirthDateRange(fromDate, toDate);

        assertNotNull(result);

    }

    @Test
    public void getUsersByBirthDateRangeThrowInvalidDateRangeExceptionTest() {
        LocalDate fromDate = LocalDate.of(2000, 1, 1);
        LocalDate toDate = LocalDate.of(1990, 12, 31);

        assertThrows(InvalidDateRangeException.class, () -> userService.getUsersByBirthDateRange(fromDate, toDate));
    }

    @Test
    void partialUpdateUserTest() {
        Long userId = 1L;
        UserUpdateDto userUpdateDto = ModelUtils.getUserUpdateDto();
        User existingUser = ModelUtils.getUserWithId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.mapUserToUserDto(existingUser)).thenReturn(new UserDto());

        UserDto result = userService.partialUpdateUser(userId, userUpdateDto);

        assertNotNull(result);
        assertEquals(userUpdateDto.getPhoneNumber(), existingUser.getPhoneNumber());

    }

    @Test
    void partialUpdateUserThrowResourceNotFoundExceptionTest() {
        Long userId = 1L;
        UserUpdateDto userUpdateDto = ModelUtils.getUserUpdateDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.partialUpdateUser(userId, userUpdateDto));
    }

}
