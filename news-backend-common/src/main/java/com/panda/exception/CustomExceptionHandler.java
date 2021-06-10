package com.panda.exception;

import com.panda.json.result.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult returnExceptionResult(CustomException e) {
        e.printStackTrace();
        return ResponseResult.exception(e.getResponseStatusEnum());
    }
}
