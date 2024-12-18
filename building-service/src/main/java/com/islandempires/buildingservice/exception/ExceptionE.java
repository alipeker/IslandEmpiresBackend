package com.islandempires.buildingservice.exception;

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
    },
    BUILDING_CONDITIONS_ERROR {
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
            return "The conditions were not met! Please fulfill the requirements first.";
        }
    },
    ISLAND_PRIVILEGES{
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
            return "This island is not yours!";
        }
    };

    abstract public Integer getStatus();

    abstract public HttpStatus getHttpStatus();

}

