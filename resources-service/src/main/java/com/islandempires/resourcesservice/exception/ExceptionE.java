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
    SERVER_ERROR {
        Integer status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

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
    CANCEL_TRANSPORT_ERROR{
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
            return "You can not cancel transport!";
        }
    },
    INSUFFICIENT_SHIP_NUMBER{
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
            return "Insufficient ship number error!";
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

