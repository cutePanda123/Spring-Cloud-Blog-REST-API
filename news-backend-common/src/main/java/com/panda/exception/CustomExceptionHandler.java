package com.panda.exception;

import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult returnException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> map = getErrors(result);
        return ResponseResult.errorMap(map);
    }

    protected Map<String, String> getErrors(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorMap;
    }
}
