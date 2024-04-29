package com.islandempires.resourcesservice.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionE {
    NOT_FOUND {
        Integer status = HttpStatus.NOT_FOUND.value();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return httpStatus.toString();
        }
    },
    ALREADY_EXIST {
        Integer status = HttpStatus.CONFLICT.value();
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return httpStatus.toString();
        }
    },
    INSUFFICIENT_RESOURCES{
        Integer status = 1001;
        HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return "Insufficient resources error!";
        }
    },
    ENUM_NOT_FOUND {
        Integer status = HttpStatus.NOT_ACCEPTABLE.value();
        HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return httpStatus.toString();
        }
    };

    abstract public Integer getStatus();

    abstract public HttpStatus getHttpStatus();

}

