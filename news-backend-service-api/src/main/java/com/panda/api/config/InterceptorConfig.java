package com.panda.api.config;

import com.panda.api.interceptors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public PassportInterceptor passportInterceptor() {
        return new PassportInterceptor();
    }

    @Bean
    public UserAccountStatusInterceptor userAccountStatusInterceptor() {
        return new UserAccountStatusInterceptor();
    }

    @Bean
    public UserAuthenticationInterceptor userAuthenticationInterceptor() { return new UserAuthenticationInterceptor(); }

    @Bean
    public AdminAuthenticationInterceptor adminAuthenticationInterceptor() {
        return new AdminAuthenticationInterceptor();
    }

    @Bean
    public AdminTokenInterceptor adminTokenInterceptor() {
        return new AdminTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/api/service-user/passport/getSMSCode");

        registry.addInterceptor(userAuthenticationInterceptor())
                .addPathPatterns("/api/service-user/user/getAccountInfo")
                .addPathPatterns("/api/service-user/user/updateUserInfo")
                .addPathPatterns("/fs/uploadAvatar");
//        registry.addInterceptor(userAccountStatusInterceptor())
//                .addPathPatterns("/api/service-user/");
//        registry.addInterceptor(adminAuthenticationInterceptor())
//                .addPathPatterns("/api/service-admin/isExistingUsername");
//                .addPathPatterns("/api/service-admin/addNewAdmin");
//                .addPathPatterns("/api/service-admin/getAdminList");
//                .addPathPatterns("/api/service-files/fs/uploadToGridFS");
//                .addPathPatterns("/api/service-files/fs/readInGridFS");
//                .addPathPatterns("/api/service-admin/relatedLinkMng/saveOrUpdateRelatedLink");
//                .addPathPatterns("/api/service-admin/relatedLinkMng/listRelatedLinks");
//                .addPathPatterns("/api/service-admin/relatedLinkMng/delete");
        //registry.addInterceptor(adminAuthenticationInterceptor());
    }
}
