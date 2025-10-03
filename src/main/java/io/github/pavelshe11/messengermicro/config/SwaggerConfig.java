package io.github.pavelshe11.messengermicro.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration

@OpenAPIDefinition(
        servers = @io.swagger.v3.oas.annotations.servers.Server(url = "/")
)
public class SwaggerConfig {
    @Value("${SWAGGER_API_VERSION}")
    private String appVersion;

    @Value("${SWAGGER_APP_DESCRIPTION}")
    private String appDescription;

    @Value("${SWAGGER_LICENSE}")
    private String licenseName;

    @Value("${SWAGGER_LICENSE_URL}")
    private String licenseUrl;

    @Value("${SWAGGER_DEV_SERVER}")
    private String devServerUrl;

    @Bean
    public OpenAPI customOpenApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Messenger Microservice API")
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name(licenseName).url(licenseUrl))
                )
                .servers(List.of(
                        new Server().url(devServerUrl).description("Local Dev")
                ));
    }
}
