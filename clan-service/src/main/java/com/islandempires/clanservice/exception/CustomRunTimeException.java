package com.islandempires.clanservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomRunTimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer status;
    private HttpStatus httpStatus;

    private List<Long> idList = new ArrayList<>();

    public CustomRunTimeException(ExceptionE exception) {
        super(exception.toString());
        this.status = exception.getStatus();
        this.httpStatus = exception.getHttpStatus();
    }

    public CustomRunTimeException(ExceptionE exception, List<Long> idList) {
        super(exception.toString());
        this.status = exception.getStatus();
        this.httpStatus = exception.getHttpStatus();
        this.idList = idList;
    }
}
