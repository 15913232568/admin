package com.example.wealth.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Wealth Management API")
                    .version("1.0")
                    .description("财富管理系统API文档")
                    .termsOfService("https://example.com/terms")
                    .contact(
                        Contact()
                            .name("技术支持")
                            .email("support@example.com")
                            .url("https://example.com/support")
                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0")
                    )
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080/api")
                    .description("开发环境")
            )
            .addServersItem(
                Server()
                    .url("https://api.example.com")
                    .description("生产环境")
            )
    }
}