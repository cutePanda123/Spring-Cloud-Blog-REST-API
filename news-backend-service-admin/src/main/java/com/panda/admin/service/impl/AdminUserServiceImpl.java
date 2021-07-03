package com.panda.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.admin.mapper.AdminUserMapper;
import com.panda.admin.service.AdminUserService;
import com.panda.exception.EncapsulatedException;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AdminUser;
import com.panda.pojo.bo.NewAdminBO;
import com.panda.utils.PaginationResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private Sid sid;

    @Override
    public AdminUser queryAdminUserByUsername(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username", username);

        AdminUser adminUser = adminUserMapper.selectOneByExample(adminExample);

        return adminUser;
    }

    @Transactional
    @Override
    public void createAdminUser(NewAdminBO newAdminBO) {
        String adminId = sid.nextShort();
        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(newAdminBO.getUsername());
        adminUser.setAdminName(newAdminBO.getAdminName());
        if (StringUtils.isNotBlank(newAdminBO.getPassword())) {
            String encryptedPassword = BCrypt.hashpw(newAdminBO.getPassword(), BCrypt.gensalt());
            adminUser.setPassword(encryptedPassword);
        }
        if (StringUtils.isNotBlank(newAdminBO.getFaceId())) {
            adminUser.setFaceId(newAdminBO.getFaceId());
        }

        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());
        int affectedRowNum = adminUserMapper.insert(adminUser);
        if (affectedRowNum != 1) {
            EncapsulatedException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public PaginationResult listAdminUsers(Integer page, Integer pageSize) {
        Example listAdminExample = new Example(AdminUser.class);
        listAdminExample.orderBy("createdTime").desc();

        PageHelper.startPage(page, pageSize);
        List<AdminUser> adminUserList = adminUserMapper.selectByExample(listAdminExample);

        return paginationResultBuilder(adminUserList, page);
    }

    private PaginationResult paginationResultBuilder(List<?> userList, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(userList);
        PaginationResult result = new PaginationResult();
        result.setPage(page);
        result.setRows(userList);
        result.setTotal(pageInfo.getTotal());
        result.setRecords(pageInfo.getPages());
        return  result;
    }
}
