package com.sas.ims.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Ajay
 */
@Getter
public class InternalServerErrorException extends Exception {
    private static final long serialVersionUID = 635343809670844351L;

    private int code;

    private HttpStatus httpStatus;

    private Object responseObject;

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, HttpStatus status) {
        super(message);
        this.code = status.value();
        this.httpStatus = status;
    }

    public InternalServerErrorException(String message, HttpStatus status, Object responseObject) {
        super(message);
        this.code = status.value();
        this.httpStatus = status;
        this.responseObject = responseObject;
    }
}
