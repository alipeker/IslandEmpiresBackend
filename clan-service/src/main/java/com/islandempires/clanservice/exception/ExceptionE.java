package com.islandempires.clanservice.exception;

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
    INVITE_ONLY {
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
            return "Only invited players can join this clan.";
        }
    },
    MAX_FRIEND_ERROR {
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
            return "You have reached the maximum number of friends! To add a new friend, please remove an existing one.";
        }
    },
    BLOCKED{
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
            return "You have been blocked by this user!";
        }
    },
    BLOCKED_FROM_YOU{
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
            return "You blocked this user! Before send friend request unblock user.!";
        }
    },
    PARTICIPANT_NOT_FOUND{
        Integer status = 2002;
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
            return "These participants does not exist!";
        }
    },
    PARTICIPANT_IS_NOT_FRIEND{
        Integer status = 2002;
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
            return "These participants does not friend!";
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
    PRIVILEGES_ERROR{
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
            return "Privileges error!";
        }
    },
    EMBASSY_LVL_ADMIN_PRIVILEGES_ERROR{
        Integer status = 2001;
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
            return "You cannot grant this user admin privileges because the user's embassy level is insufficient.";
        }
    },
    EMBASSY_LVL_CREATE_CLAN_ERROR{
        Integer status = 2001;
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
            return "The embassy level is not sufficient to create a clan! Please upgrade the embassy level.";
        }
    },
    CLAN_MEMBER_MAX_NUMBER_ERROR{
        Integer status = 2001;
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
            return "Users registered in the clan have reached the maximum leve!";
        }
    },
    ADMIN_ERROR{
        Integer status = 3000;
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
            return "You are the sole administrator! Delegate your authority to someone else.";
        }
    },
    ENEMY_CLAN_ERROR {
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
            return "You are at war this clan!";
        }
    },
    ENEMY_CLAN_ADD_FRIEND_ERROR {
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
            return "Your clan enemy with this clan! Before end war with this clan.";
        }
    },
    ENEMY_CLAN_FRIEND_ERROR {
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
            return "Your clan friend with this clan! Remove the friendship before proceeding.";
        }
    },
    UNAUTHORIZED {
        Integer status = HttpStatus.UNAUTHORIZED.value();
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
            return httpStatus.toString();
        }
    };

    abstract public Integer getStatus();

    abstract public HttpStatus getHttpStatus();

}

