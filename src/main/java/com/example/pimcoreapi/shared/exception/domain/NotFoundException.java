package com.example.pimcoreapi.shared.exception.domain;

import com.example.pimcoreapi.shared.exception.base.BusinessException;

public class NotFoundException extends BusinessException {

    public NotFoundException(String object, String entity) {
        super("The " + entity + " object for id " + object + " not found");
    }
}
