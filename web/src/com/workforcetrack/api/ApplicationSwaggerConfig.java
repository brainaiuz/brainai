package com.workforcetrack.api;

import com.workforcetrack.api.base.CustomObjectMapper;
import com.workforcetrack.api.base.RestServiceUtils;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Anvar Akramov on 8/7/17.
 */
//@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.springdoc"})
@Import({SpringDocConfiguration.class,
        SpringDocConfigProperties.class,
        SpringDocWebMvcConfiguration.class,
        SwaggerConfig.class,
        SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class,
        JacksonAutoConfiguration.class})
@PropertySource("classpath:/swagger-v1.properties")
public class ApplicationSwaggerConfig implements WebMvcConfigurer {

    @Bean
    public RestServiceUtils restServiceUtils() {
        return new RestServiceUtils();
    }

    @Bean
    public CustomObjectMapper customObjectMapper() {
        return new CustomObjectMapper();
    }

    @Bean
    public OpenAPI openAPIV1() {
        return new OpenAPI()
                .info(new Info().title("Web API v1.0")
                        .description("Created by KPI.com")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Terms of Service")
                        .url("https://www.kpi.com/en/company/terms/"));
    }

    @Bean
    public GroupedOpenApi accountingApiV1() {
        return GroupedOpenApi.builder()
                .group("accounting-v1")
                .displayName("Accounting API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.accounting")
                .build();
    }

    @Bean
    public GroupedOpenApi authApiV1() {
        return GroupedOpenApi.builder()
                .group("auth-v1")
                .displayName("Auth API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.auth")
                .build();
    }

    @Bean
    public GroupedOpenApi coreApiV1() {
        return GroupedOpenApi.builder()
                .group("core-v1")
                .displayName("Core API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.core")
                .build();
    }

    @Bean
    public GroupedOpenApi crmApiV1() {
        return GroupedOpenApi.builder()
                .group("crm-v1")
                .displayName("CRM API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.crm")
                .build();
    }

    @Bean
    public GroupedOpenApi documentApiV1() {
        return GroupedOpenApi.builder()
                .group("document-v1")
                .displayName("Document API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.document")
                .build();
    }

    @Bean
    public GroupedOpenApi hrmsApiV1() {
        return GroupedOpenApi.builder()
                .group("hrms-v1")
                .displayName("HRMS API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.hrms")
                .build();
    }

    @Bean
    public GroupedOpenApi payrollApiV1() {
        return GroupedOpenApi.builder()
                .group("payroll-v1")
                .displayName("Payroll API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.payroll")
                .build();
    }

    @Bean
    public GroupedOpenApi pmApiV1() {
        return GroupedOpenApi.builder()
                .group("project-v1")
                .displayName("Project Management API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.pm")
                .build();
    }

    @Bean
    public GroupedOpenApi fingerPrintApiV1() {
        return GroupedOpenApi.builder()
                .group("fingerprint-v1")
                .displayName("Finger Print API Group v1.0")
                .packagesToScan("com.edatasite.workforce.rest.v1.release10.fingerprint")
                .build();
    }
}
