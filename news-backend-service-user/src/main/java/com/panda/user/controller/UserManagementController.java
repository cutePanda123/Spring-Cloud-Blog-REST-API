package com.panda.user.controller;

import com.panda.api.controller.BaseController;
import com.panda.api.controller.user.UserManagementControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.user.service.UserManagementService;
import com.panda.utils.PaginationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class UserManagementController extends BaseController implements UserManagementControllerApi {
    @Autowired
    private UserManagementService userManagementService;

    @Override
    public ResponseResult queryAll(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PaginationResult result = userManagementService.listUsers(
                nickname,
                status,
                startDate,
                endDate,
                page,
                pageSize
        );
        return ResponseResult.ok(result);
    }
}
