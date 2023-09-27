package com.oleszl.employeemanagement.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto {
    @Pattern(regexp = "\\d{12}", message = "Invalid phone number format")
    private String phoneNumber;
}
