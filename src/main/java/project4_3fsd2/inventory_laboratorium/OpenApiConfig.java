package project4_3fsd2.inventory_laboratorium;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /*
     * ======================================================
     * GLOBAL METADATA
     * ======================================================
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Laboratorium API")
                        .version("1.0.0")
                        .description("""
                                Dokumentasi API internal Inventory Laboratorium.

                                Catatan desain:
                                - API berbasis REST
                                - Digunakan untuk manajemen inventaris laboratorium
                                - Swagger hanya sebagai dokumentasi manusia
                                """)
                        .contact(new Contact()
                                .name("Sarah & Neyza Team")
                                .email("neyzamayylanies@gmail.com")
                                .url("https://github.com/neyzamaylanies/inventory_laboratorium")
                        )
                );
    }

    /*
     * ======================================================
     * GROUP: CATEGORY
     * ======================================================
     */
    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
                .group("Category")
                .pathsToMatch("/api/categories/**")
                .build();
    }

    /*
     * ======================================================
     * GROUP: EQUIPMENT
     * ======================================================
     */
    @Bean
    public GroupedOpenApi equipmentApi() {
        return GroupedOpenApi.builder()
                .group("Equipment")
                .pathsToMatch("/api/equipments/**")
                .build();
    }

    /*
     * ======================================================
     * GROUP: CONDITION LOG
     * ======================================================
     */
    @Bean
    public GroupedOpenApi conditionLogApi() {
        return GroupedOpenApi.builder()
                .group("Condition Log")
                .pathsToMatch("/api/condition-logs/**")
                .build();
    }

    /*
     * ======================================================
     * GROUP: TRANSACTION
     * ======================================================
     */
    @Bean
    public GroupedOpenApi transactionApi() {
        return GroupedOpenApi.builder()
                .group("Transaction")
                .pathsToMatch("/api/transactions/**")
                .build();
    }

    /*
     * ======================================================
     * GROUP: USER
     * ======================================================
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/api/users/**")
                .build();
    }

    /*
     * ======================================================
     * GROUP: STUDENT
     * ======================================================
     */
    @Bean
    public GroupedOpenApi studentApi() {
        return GroupedOpenApi.builder()
                .group("Student")
                .pathsToMatch("/api/students/**")
                .build();
    }
}
