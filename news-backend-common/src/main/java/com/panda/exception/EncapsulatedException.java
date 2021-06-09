package com.panda.exception;

import com.panda.json.result.ResponseStatusEnum;

public class EncapsulatedException {
    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new CustomException(responseStatusEnum);
    }
}
