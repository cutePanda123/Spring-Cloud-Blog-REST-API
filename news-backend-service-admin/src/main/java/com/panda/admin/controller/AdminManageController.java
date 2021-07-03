package com.panda.admin.controller;

import com.panda.admin.service.AdminUserService;
import com.panda.api.controller.BaseController;
import com.panda.api.controller.admin.AdminManageControllerApi;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AdminUser;
import com.panda.pojo.bo.AdminLoginBO;
import com.panda.pojo.bo.NewAdminBO;
import com.panda.utils.PaginationResult;
import com.panda.utils.RedisAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
public class AdminManageController extends BaseController implements AdminManageControllerApi {
    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RedisAdaptor  redisAdaptor;

    @Override
    public ResponseResult adminLogin(@Valid AdminLoginBO adminLoginBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        AdminUser adminUser = adminUserService.queryAdminUserByUsername(adminLoginBO.getUsername());
        if (adminUser == null) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIST_ERROR);
        }
        boolean isMatchedPassword = BCrypt.checkpw(adminLoginBO.getPassword(), adminUser.getPassword());
        if (!isMatchedPassword) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIST_ERROR);
        }
        loginSetup(adminUser, request, response);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult isExistingUsername(String username) {
        throwExceptionIfUsernameExists(username);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult addNewAdmin(NewAdminBO newAdminBO, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(newAdminBO.getImg64())) {
            if (StringUtils.isBlank(newAdminBO.getPassword()) ||
            StringUtils.isBlank(newAdminBO.getConfirmedPassword())) {
                return ResponseResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }
        if (StringUtils.isNotBlank(newAdminBO.getPassword())) {
            if (!newAdminBO.getPassword().equalsIgnoreCase(newAdminBO.getConfirmedPassword())) {
                return ResponseResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        throwExceptionIfUsernameExists(newAdminBO.getUsername());

        adminUserService.createAdminUser(newAdminBO);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult getAdminList(Integer page, Integer pageSize) {
        if (page == null) {
            page = DEFAULT_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PaginationResult result = adminUserService.listAdminUsers(page, pageSize);
        return ResponseResult.ok(result);
    }

    private void throwExceptionIfUsernameExists(String username) {
        AdminUser user = adminUserService.queryAdminUserByUsername(username);
        if (user != null) {
            EncapsulatedException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

    private void loginSetup(AdminUser adminUser, HttpServletRequest request, HttpServletResponse response) {
        String redisToken = UUID.randomUUID().toString();
        redisAdaptor.set(REDIS_ADMIN_TOKEN_PREFIX + ":" + adminUser.getId(), redisToken);

        setCookie(request, response, "atoken", redisToken, COOKIE_DURATION);
        setCookie(request, response, "aid", adminUser.getId(), COOKIE_DURATION);
        setCookie(request, response, "aname", adminUser.getAdminName(), COOKIE_DURATION);
    }
}
