package com.example.pimcoreapi.shared.exception.domain;

import com.example.pimcoreapi.shared.exception.base.BusinessException;
import com.example.pimcoreapi.shared.exception.base.InfrastructureException;

public class ObjectNullException extends BusinessException {
    public ObjectNullException(String object) {
        super("The object: " + object + " is null");
    }

//    public ObjectNullException(String object, Throwable cause) {
//        super("The object: " + object + " is null", cause);
//    }
}
