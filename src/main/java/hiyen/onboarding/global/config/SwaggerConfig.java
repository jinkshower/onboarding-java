package hiyen.onboarding.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("onboarding-java")
                .description("한달인턴 온보딩을 위한 과제");
        return new OpenAPI()
                .info(info);
    }
}
