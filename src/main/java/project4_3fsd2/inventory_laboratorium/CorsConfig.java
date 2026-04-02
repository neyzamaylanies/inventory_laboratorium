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
        
        // Biar HP Infinix bisa masuk tanpa ditolak satpam Spring
        config.setAllowCredentials(false); 
        
        // Izinkan semua origin agar lebih fleksibel saat debugging
        config.addAllowedOriginPattern("*"); 
        
        // Daftar IP yang diizinkan (Rumah, HP, Kampus)
        config.addAllowedOrigin("http://192.168.1.5:8080");
        config.addAllowedOrigin("http://192.168.1.6:8080");
        config.addAllowedOrigin("http://192.168.10.113:8080");
        
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}