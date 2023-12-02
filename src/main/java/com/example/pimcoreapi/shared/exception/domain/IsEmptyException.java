package com.example.pimcoreapi.shared.exception.domain;

import com.example.pimcoreapi.shared.exception.base.BusinessException;

public class IsEmptyException extends BusinessException {
    public IsEmptyException(String field, String object) {
        super("The argument: " + field + " is empty for object: " + object);
    }
//
//    public IsEmptyException(String field, String object, Throwable cause) {
//        super("The argument: " + field + " is empty for object: " + object, cause);
//    }
}
