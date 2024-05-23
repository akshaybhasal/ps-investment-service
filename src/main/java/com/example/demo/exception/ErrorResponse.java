package com.example.demo.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private Long timestamp;
    private int code;
    private HttpStatus status;
    private String error;
    private String path;
}
