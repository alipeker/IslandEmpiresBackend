package com.islandempires.authservice.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private HttpStatus statusCode;
    private Date date = new Date();
    private String message = "";
}