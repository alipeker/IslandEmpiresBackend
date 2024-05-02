package com.islandempires.islandservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomRunTimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer status;
    private HttpStatus httpStatus;

    public CustomRunTimeException(ExceptionE exception) {
        super(exception.toString());
        this.status = exception.getStatus();
        this.httpStatus = exception.getHttpStatus();
    }

}
