package com.panda.api.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {
    //    http://localhost:8003/swagger-ui.html     classic path
    //    http://localhost:8003/doc.html            new path

    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> adminPredicate = RequestHandlerSelectors.basePackage("com.panda.admin.controller");
        Predicate<RequestHandler> articlePredicate = RequestHandlerSelectors.basePackage("com.panda.article.controller");
        Predicate<RequestHandler> userPredicate = RequestHandlerSelectors.basePackage("com.panda.user.controller");
        Predicate<RequestHandler> filesPredicate = RequestHandlerSelectors.basePackage("com.panda.files.controller");

        return new Docket(DocumentationType.SWAGGER_2)  // set api type: swagger2
                .apiInfo(apiInfo())                 // api doc info
                .select()
                .apis(Predicates.or(userPredicate))
//                .apis(Predicates.or(adminPredicate, articlePredicate, userPredicate, filesPredicate))
                .paths(PathSelectors.any())         // path to all controllers
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("news backend api")                       // doc title
                .contact(new Contact("panda",
                        "https://www.panda.com",
                        "abc@panda.com"))                   // contact info
                .description("news backend api doc")      // details info
                .version("1.0.1")                               // version
                .termsOfServiceUrl("https://www.panda.com")     // website url
                .build();
    }
}