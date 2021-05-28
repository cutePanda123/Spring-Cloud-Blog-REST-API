package com.panda.mybatis.generator;

import java.io.File;

public class ArticleGenerator {
    public static void main(String[] args) throws Exception {
        BaseGenerator.generator(
                "mybatis-generator-database" +
                        File.separator + "generatorConfig-article.xml",
                true);
    }
}
