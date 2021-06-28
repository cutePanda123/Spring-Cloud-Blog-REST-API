package com.panda.admin.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class AdminPasswordGeneratorTest {
    public static void main(String[] args) {
        String pwd = BCrypt.hashpw("admin", BCrypt.gensalt());
        System.out.println(pwd);
    }
}
