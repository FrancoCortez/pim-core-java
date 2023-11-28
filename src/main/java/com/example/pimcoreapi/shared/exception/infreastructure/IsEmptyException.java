package com.example.pimcoreapi.shared.exception.infreastructure;

import com.example.pimcoreapi.shared.exception.base.InfrastructureException;

public class IsEmptyException extends InfrastructureException {
    public IsEmptyException(String field, String object) {
        super("The argument: " + field + " is empty for object: " + object);
    }
//
//    public IsEmptyException(String field, String object, Throwable cause) {
//        super("The argument: " + field + " is empty for object: " + object, cause);
//    }
}
