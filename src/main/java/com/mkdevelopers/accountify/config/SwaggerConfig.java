package com.mkdevelopers.accountify.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI accountifyOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Accountify API")
                        .description("REST API documentation for Accountify application")
                        .version("v1.0.0")
                        .contact(new Contact().name("MK Developers").email("contact@mkdevelopers.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Bean
    public OpenApiCustomizer openApiSortCustomizer() {
        return openApi -> openApi.getPaths().values()
                .forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                    if (operation.getParameters() != null) {
                        var sortParameter = operation.getParameters().stream()
                                .filter(parameter -> "sort".equals(parameter.getName()))
                                .findFirst();
                        sortParameter.ifPresent(parameter -> parameter.setDescription("Sorting criteria in the format: property(,asc|desc). "
                                + "Default sort order is ascending. " + "Multiple sort criteria are supported."));
                    }
                }));
    }
}
