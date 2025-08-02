package com.sunbeam.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Vastral E-commerce API")
                        .version("v1.0")
                        .description("API documentation for the Vastral E-commerce platform.")
                        .contact(new Contact()
                                .name("Anish Kolhe")
                                .email("anish@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .servers(List.of(
                        new Server().url("https://vastral2-production.up.railway.app").description("Production server"),
                        new Server().url("http://localhost:8080").description("Local development server")))
                .addTagsItem(new Tag().name("Auth").description("Authentication related endpoints"))
                .addTagsItem(new Tag().name("Products").description("Product management endpoints"));
    }
}
