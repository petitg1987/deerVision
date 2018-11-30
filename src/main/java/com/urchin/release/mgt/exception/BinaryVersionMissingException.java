package com.urchin.release.mgt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Missing binary version")
public class BinaryVersionMissingException extends RuntimeException {

    public BinaryVersionMissingException(String msg){
        super(msg);
    }

}