package com.islandempires.buildingservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "FeignClient call failed")
@Data
public class FeignClientException extends RuntimeException {
    private ExceptionE exception;
    public FeignClientException(ExceptionE e) {
        super(e.toString());
        this.exception = e;
    }

    public FeignClientException() {
        System.out.println("a");
    }
}
