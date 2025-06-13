package com.sdgp.MediPass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sdgp.MediPass.controller"))      //scanning controller package for APIs
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiDetails());
    }
    private void apiInfo(ApiInfo apiInfo) {
    }


    private ApiInfo apiDetails(){
        return new ApiInfoBuilder()
                .title("MediPass API documentation")
                .description("API documentation using Swagger on MediPass application")
                .version("1.0.0")
                .build();
    }

}
