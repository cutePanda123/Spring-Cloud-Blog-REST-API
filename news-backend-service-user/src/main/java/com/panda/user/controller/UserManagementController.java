package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.UserManagementControllerApi;
import com.panda.json.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class UserManagementController extends BaseController implements UserManagementControllerApi {
    @Override
    public ResponseResult queryAll(String nickname, String status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return ResponseResult.ok();
    }
}
