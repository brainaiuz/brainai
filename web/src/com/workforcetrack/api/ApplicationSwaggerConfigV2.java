package com.workforcetrack.api;

import com.edatasite.workforce.rest.v2.release10.core.APIMultipartResolver;
import com.edatasite.workforce.rest.v2.release10.exceptionhandling.RestExceptionHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kpi.com on 8/7/17.
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
@PropertySource("classpath:/swagger-v2.properties")
public class ApplicationSwaggerConfigV2 implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(stringHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
        converters.add(mappingJackson2XmlHttpMessageConverter());
    }

    @Bean
    public RestExceptionHandler handlerExceptionResolver() {
        RestExceptionHandler handlerExceptionResolver = new RestExceptionHandler();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(stringHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
        converters.add(mappingJackson2XmlHttpMessageConverter());
        handlerExceptionResolver.setMessageConverters(converters.toArray(new HttpMessageConverter<?>[]{}));
        return handlerExceptionResolver;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        /*Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_EMPTY);*/
        return new MappingJackson2HttpMessageConverter(objectMapper());
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        /*SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new DateDeserializer());
        objectMapper.registerModule(module);*/
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return objectMapper;
    }

    @Bean
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
        return new MappingJackson2XmlHttpMessageConverter();
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter();
    }

    @Bean
    public APIMultipartResolver multipartResolver() {
        return new APIMultipartResolver();
    }

    @Bean
    public AcceptHeaderLocaleResolver acceptHeaderResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Bean
    public OpenAPI openAPIV2() {
        return new OpenAPI()
                .info(new Info().title("Web API v2.0")
                        .description("Created by KPI.com")
                        .version("2.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Terms of Service")
                        .url("https://www.kpi.com/en/company/terms/"));
    }

    @Bean
    public GroupedOpenApi authApiV2() {
        return GroupedOpenApi.builder()
                .group("auth-v2")
                .displayName("Auth API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.auth")
                .build();
    }

    @Bean
    public GroupedOpenApi accountingApiV2() {
        return GroupedOpenApi.builder()
                .group("accounting-v2")
                .displayName("Accounting API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.accounting")
                .build();
    }

    @Bean
    public GroupedOpenApi hrmsApiV2() {
        return GroupedOpenApi.builder()
                .group("hrms-v2")
                .displayName("HRMS API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.hrms")
                .build();
    }

    @Bean
    public GroupedOpenApi settingsApiV2() {
        return GroupedOpenApi.builder()
                .group("settings-v2")
                .displayName("Settings API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.settings")
                .build();
    }

    @Bean
    public GroupedOpenApi crmApiV2() {
        return GroupedOpenApi.builder()
                .group("crm-v2")
                .displayName("CRM API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.crm")
                .build();
    }

    @Bean
    public GroupedOpenApi documentsApiV2() {
        return GroupedOpenApi.builder()
                .group("documents-v2")
                .displayName("Documents API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.documents")
                .build();
    }

    @Bean
    public GroupedOpenApi coreApiV2() {
        return GroupedOpenApi.builder()
                .group("core-v2")
                .displayName("Core API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.core")
                .build();
    }

    @Bean
    public GroupedOpenApi payrollApiV2() {
        return GroupedOpenApi.builder()
                .group("payroll-v2")
                .displayName("Payroll API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.payroll")
                .build();
    }

    @Bean
    public GroupedOpenApi pmApiV2() {
        return GroupedOpenApi.builder()
                .group("projects-v2")
                .displayName("Project Management API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.pm")
                .build();
    }

    @Bean
    public GroupedOpenApi noteApiV2() {
        return GroupedOpenApi.builder()
                .group("note-v2")
                .displayName("Note API Group v2.0")
                .packagesToScan("com.edatasite.workforce.rest.v2.release10.note")
                .build();
    }
}
