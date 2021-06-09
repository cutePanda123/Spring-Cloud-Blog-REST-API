package com.panda.exception;

import com.panda.json.result.ResponseStatusEnum;
import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    private ResponseStatusEnum responseStatusEnum;

    public CustomException(ResponseStatusEnum responseStatusEnum) {
        super("exception code: " + responseStatusEnum.status() +
                ", exception details: " + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }
}
