package org.blogapp.blogbackend.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI blogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BlogApp API")
                        .description("API REST para gestión de usuarios, autenticación JWT y posts.")
                        .version("1.0.0")
                        .termsOfService("https://example.com/terms")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Gero Moroni")
                                .email("gero@example.com")
                                .url("https://github.com/geromoroni"))
                );
    }
}