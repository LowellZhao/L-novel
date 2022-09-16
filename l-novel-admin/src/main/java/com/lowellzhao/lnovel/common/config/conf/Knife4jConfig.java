//package com.lowellzhao.lnovel.common.config.conf;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
//
//import java.util.Collections;
//
///**
// * Knife4j配置类
// *
// * @author lowellzhao
// */
//@Configuration
//@EnableSwagger2WebMvc
//public class Knife4jConfig {
//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .protocols(Collections.singleton("http"))
//                .host("http://127.0.0.1:8080")
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.lowellzhao.lnovel.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("【l-novel】api文档")
//                .description("L小说站项目")
//                .contact(new Contact("lowellzhao", "https://github.com/LowellZhao", "lowellzhao@163.com"))
//                .termsOfServiceUrl("http://127.0.0.1:8080/api")
//                .version("1.0")
//                .build();
//    }
//
//}
