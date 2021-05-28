package com.panda.mybatis.generator;

import java.io.File;

public class AdminGenerator {
    public static void main(String[] args) throws Exception {
        BaseGenerator.generator(
                "mybatis-generator-database" +
                        File.separator + "generatorConfig-admin.xml",
                true);
    }
}
