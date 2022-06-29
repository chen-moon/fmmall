package com.qfedu.fmmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author chen
 * @date 2021/8/7-18:26
 */

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /*
        如何获取一个接口/抽象类
        1、new接口，然后需要在构造器的{}中实现所有接口中的抽象方法
        2、new他的子类或者实现类
        3、使用工厂模式进行创建
     */


    @Bean
    public Docket getDocket(){

        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        docket.apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qfedu.fmmall.controller"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }


    public ApiInfo apiInfo(){
        Contact contact = new Contact("chen","www.baidu.com","2215447830");
        return new ApiInfo(
                "《果蔬商城》后端接口文档",
                "前后端分离项目",
                "v1.0.0",
                "2333333",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }

}
