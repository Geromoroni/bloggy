package org.blogapp.blogbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorseConfig {

    @Bean
    // Renombrado para ser más genérico, ya que ahora configura más que solo CORS
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // permitir todos los endpoints
                        .allowedOrigins("http://localhost:4200") // permitir Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // los métodos usados
                        .allowedHeaders("*") // permitir cualquier header
                        .allowCredentials(true);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Servir archivos desde la carpeta 'uploads'
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:uploads/");
            }
        };
    }
}
