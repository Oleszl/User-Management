package com.oleszl.employeemanagement.exception.handler;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDetail {

    private Integer status;
    private String detail;

}
