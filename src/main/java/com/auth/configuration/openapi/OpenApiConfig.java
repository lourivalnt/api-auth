package com.auth.configuration.openapi;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME =
        "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

            .info(
                new Info()
                    .title("Auth API")
                    .description("""
                        Plataforma enterprise de autenticação
                        baseada em JWT, Refresh Token Rotation,
                        Session Management e Redis.
                    """)
                    .version("1.0.0")
                    .contact(
                        new Contact()
                            .name("Lourival Linard")
                            .email("contato@authapi.com")
                    )
                    .license(
                        new License()
                            .name("MIT")
                    )
            )

            .externalDocs(
                new ExternalDocumentation()
                    .description("Projeto Auth API")
            )

            .addSecurityItem(
                new SecurityRequirement()
                    .addList(SECURITY_SCHEME)
            )

            .schemaRequirement(
                SECURITY_SCHEME,
                new SecurityScheme()
                    .name(SECURITY_SCHEME)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            );
    }
}