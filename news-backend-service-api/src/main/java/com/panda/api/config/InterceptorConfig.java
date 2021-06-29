package com.panda.api.config;

import com.panda.api.interceptors.AdminAuthenticationInterceptor;
import com.panda.api.interceptors.PassportInterceptor;
import com.panda.api.interceptors.UserAccountStatusInterceptor;
import com.panda.api.interceptors.UserAuthenticationInterceptor;
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
    }
}
