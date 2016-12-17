package com.shedhack.tool.spring.boost;

import com.google.gson.Gson;
import com.shedhack.exception.controller.spring.ExceptionInterceptor;
import com.shedhack.exception.controller.spring.config.EnableExceptionController;
import com.shedhack.spring.actuator.config.EnableActuatorsAndInterceptors;
import com.shedhack.spring.actuator.interceptor.ActuatorExceptionInterceptor;
import com.shedhack.spring.actuator.interceptor.ActuatorTraceRequestInterceptor;
import com.shedhack.thread.context.config.EnableThreadContextAspect;
import com.shedhack.tool.spring.boost.controller.AdminHelpController;
import com.shedhack.trace.request.filter.DefaultTraceRequestInterceptor;
import com.shedhack.trace.request.filter.RequestTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

/**
 * @author imamchishty
 */
@Configuration
@EnableSwagger2
@EnableExceptionController
@EnableThreadContextAspect
@EnableActuatorsAndInterceptors
public class SpringBoostConfiguration extends WebMvcConfigurerAdapter {

    // --------------------
    // Bean configurations:
    // --------------------

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.boost.api.root:DEFAULT}")
    private String apiRoot;

    @Autowired
    private ActuatorTraceRequestInterceptor actuatorTraceRequestInterceptor;

    @Autowired
    private ActuatorExceptionInterceptor actuatorExceptionInterceptor;

    @Bean
    public Gson gson() {
        return new Gson();
    }

    /**
     * Filter records and logs all HTTP requests.
     * This requires implementation(s) of TraceRequestInterceptors.
     */
    @Bean
    public FilterRegistrationBean requestIdFilterRegistrationBean() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new RequestTraceFilter(appName,
                Arrays.asList(new DefaultTraceRequestInterceptor(gson()), actuatorTraceRequestInterceptor)));

        if(apiRoot.isEmpty() || apiRoot.equals("DEFAULT")) {
            filter.addUrlPatterns("/*");
        }
        else {
            filter.addUrlPatterns(apiRoot + "/*");
        }

        return filter;
    }

    @Bean
    public List<ExceptionInterceptor> exceptionInterceptors() {
        return Arrays.asList(actuatorExceptionInterceptor);
    }

    @Bean
    public AdminHelpController adminHelpController() {
        return new AdminHelpController();
    }

    // ---------------------------------------
    // Swagger setup for the API documentation
    // ---------------------------------------

    // If you add spring security then you can easily secure these resources.

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(apiRoot + "/v2/api-docs", "/v2/api-docs");
        registry.addRedirectViewController(apiRoot + "/configuration/ui", "/configuration/ui");
        registry.addRedirectViewController(apiRoot +"/configuration/security", "/configuration/security");
        registry.addRedirectViewController(apiRoot +"/swagger-resources", "/swagger-resources");
        registry.addRedirectViewController(apiRoot, "/swagger-ui.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/" + apiRoot  + "/**")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Documentation")
                .description("API Documentation")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }


}