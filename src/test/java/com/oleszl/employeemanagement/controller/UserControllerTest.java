package com.oleszl.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oleszl.employeemanagement.ModelUtils;
import com.oleszl.employeemanagement.dto.UserData;
import com.oleszl.employeemanagement.dto.UserDto;
import com.oleszl.employeemanagement.dto.UserRequestDto;
import com.oleszl.employeemanagement.dto.UserResponseDto;
import com.oleszl.employeemanagement.dto.UserUpdateDto;
import com.oleszl.employeemanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String userControllerLink = "/api/users";

    @Test
    void saveUser() throws Exception {
        UserRequestDto userRequestDto = ModelUtils.getUserRequestDto();
        UserDto userDto = ModelUtils.getUserDto();

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(userDto);

        mockMvc.perform(post(userControllerLink)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/1"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.email").value("oles@gmail.com"))
                .andExpect(jsonPath("$.data.firstName").value("Oles"))
                .andExpect(jsonPath("$.data.lastName").value("Sukmanovskyi"));
    }

    @Test
    public void UpdateUserTest() throws Exception {
        UserRequestDto userRequestDto = ModelUtils.getUserRequestDto();
        UserDto userDto = ModelUtils.getUserDto();

        when(userService.updateUser(anyLong(), any(UserRequestDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(userControllerLink + "/{userId}", 1)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.email").value("oles@gmail.com"))
                .andExpect(jsonPath("$.data.firstName").value("Oles"))
                .andExpect(jsonPath("$.data.lastName").value("Sukmanovskyi"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(userControllerLink + "/{userId}", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User deleted successfully"));
    }

    @Test
    public void testGetUsersByBirthDateRange() throws Exception {
        List<UserData> users = new ArrayList<>();
        users.add(new UserData(1L, "oles@gmail.com", "Oles", "Sukmanovskyi",
                LocalDate.of(2001, 3, 15), "Lviv", "380678456984"));
        users.add(new UserData(2L, "oleg@gmail.com", "Oleg", "Jane",
                LocalDate.of(1985, 5, 20), "", ""));

        when(userService.getUsersByBirthDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new UserResponseDto(users));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(userControllerLink)
                        .param("from", "1990-01-01")
                        .param("to", "2020-12-31")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email").value("oles@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].email").value("oleg@gmail.com"));
    }

    @Test
    public void partialUpdateUserTest() throws Exception {
        UserDto updatedUserDto = ModelUtils.getUserDto();

        when(userService.partialUpdateUser(anyLong(), any(UserUpdateDto.class)))
                .thenReturn(updatedUserDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch(userControllerLink + "/{userId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content("{\"phoneNumber\": \"380678456984\"}")
                        .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phoneNumber").value("380678456984"));
    }
}
