package com.urchin.release.mgt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Binary not found")
public class BinaryNotFoundException extends RuntimeException {

    public BinaryNotFoundException(String msg){
        super(msg);
    }

}
