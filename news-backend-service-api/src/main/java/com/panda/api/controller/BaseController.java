package com.panda.api.controller;

import com.panda.utils.RedisAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class BaseController {
    @Autowired
    protected RedisAdaptor redisAdaptor;

    protected static final String MOBILE_SMSCODE = "mobile:smscode";

    protected Map<String, String> getErrors(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorMap;
    }
}
