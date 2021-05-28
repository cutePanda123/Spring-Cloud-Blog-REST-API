package com.panda.mybatis.generator;

import java.io.File;

public class UserGenerator {
    public static void main(String[] args) throws Exception {
        BaseGenerator.generator(
                "mybatis-generator-database" +
                        File.separator + "generatorConfig-user.xml",
                true);
    }
}
