package com.islandempires.authservice.exception;


import org.springframework.http.HttpStatus;

public enum ExceptionE {
    TOKEN_EXPIRED {
        Integer status = 5003;
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return "Lütfen tekrar giriş yapiniz!";
        }
    },

    NOT_AUTHORIZED {
        Integer status = 5004;
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return "Yetkiniz yok!";
        }
    },

    BAD_CREDENTIALS {
        Integer status = 5001;
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return "Şifreniz yanlış!";
        }
    },

    BAD_USERNAME {
        Integer status = 5000;
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return "Gecersiz Kullanıcı adı!";
        }
    },
    USER_NOT_FOUND {
        Integer status = 5005;
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        @Override
        public Integer getStatus() {
            return this.status;
        }

        public String toString() {
            return "Kullanıcı Bulunamadı!";
        }
    };
    abstract public Integer getStatus();

    abstract public HttpStatus getHttpStatus();
}
