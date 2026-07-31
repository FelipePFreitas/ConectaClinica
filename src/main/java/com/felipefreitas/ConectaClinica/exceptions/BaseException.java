package com.felipefreitas.ConectaClinica.exceptions;

import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorEnum errorEnum;

    public BaseException(ErrorEnum errorEnum) {
        super(errorEnum.getErrorMessage());

        this.errorEnum = errorEnum;
    }

}
