package project4_3fsd2.inventory_laboratorium;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow credentials
        config.setAllowCredentials(false);
        
        // Allow frontend origins
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("http://localhost:80");
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://localhost:5000");
        config.addAllowedOrigin("http://localhost:5500");
        config.addAllowedOriginPattern("*"); // semua port Flutter web
        config.addAllowedOrigin("http://127.0.0.1");
        config.addAllowedOriginPattern("http://127.0.0.1:*");
        
        // Allow all headers
        config.addAllowedHeader("*");
        
        // Allow all HTTP methods (GET, POST, PUT, DELETE, etc)
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}