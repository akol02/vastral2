package com.sunbeam.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define the security scheme (JWT Bearer token)
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                // 1. This adds the API metadata (title, version, etc.)
                .info(new Info().title("Vastral E-commerce API")
                                .version("v1.0")
                                .description("API documentation for the Vastral E-commerce platform."))
                
                // 2. This defines the components, including the security scheme for JWT
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                
                // 3. This adds a global security requirement to use the defined scheme
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}