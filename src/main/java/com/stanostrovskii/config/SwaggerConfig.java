package com.stanostrovskii.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${swagger.contact.name}")  
    private String contactName;  
   
    @Value("${swagger.contact.url}")  
    private String contactURL;  
   
    @Value("${swagger.contact.email}")  
    private String contactEmail;  
	private List<Parameter> listDocketParameters;

	public SwaggerConfig() {
		Parameter oAuthHeader = new ParameterBuilder().name("Authorization")
				.description("JWT Bearer Token").modelRef(new ModelRef("string")).parameterType("header").build();
		listDocketParameters = new ArrayList<Parameter>();
		listDocketParameters.add(oAuthHeader);
	}

	@Bean
	public Docket appDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters(listDocketParameters)
				.groupName("public-api").apiInfo(apiInfo()).select()
				.paths(postPaths()).build();
	}

	private ApiInfo apiInfo() { 
		return new ApiInfoBuilder().title("e-Office Management Suite API")
				.description("API reference for e-Office Management Suite")
				.termsOfServiceUrl("https://github.com/stanostr/OfficeManagementSystem/blob/master/README.md")
				.contact(new Contact(contactName, contactURL, contactEmail)).build();
	}

	private Predicate<String> postPaths() {
		return or(or(regex("/employees.*"), regex("/departments.*")), regex("/login"));
	}

}