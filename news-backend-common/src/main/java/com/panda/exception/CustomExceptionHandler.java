package com.panda.exception;

import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult returnExceptionResult(CustomException e) {
        e.printStackTrace();
        return ResponseResult.exception(e.getResponseStatusEnum());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseResult returnMaxUploadSizeExceededExceptionResult(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return ResponseResult.exception(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
    }
}
