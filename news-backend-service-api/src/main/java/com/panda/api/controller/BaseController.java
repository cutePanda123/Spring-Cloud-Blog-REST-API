package com.panda.api.controller;

import com.panda.utils.RedisAdaptor;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    @Autowired
    protected RedisAdaptor redisAdaptor;

    protected static final String MOBILE_SMSCODE = "mobile:smscode";
}
