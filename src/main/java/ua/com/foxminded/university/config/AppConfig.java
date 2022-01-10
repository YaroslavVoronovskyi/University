package ua.com.foxminded.university.config;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ua.com.foxminded.university.controller.dao")
@ComponentScan("ua.com.foxminded.university")
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(AccessLevel.PRIVATE);
        return modelMapper;
    }
    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                   
          .apis(RequestHandlerSelectors.basePackage("ua.com.foxminded.university"))              
          .paths(PathSelectors.ant("/rest/*"))
          .build()
          .apiInfo(apiDetails());                                           
    }

    private ApiInfo apiDetails() {
        return new ApiInfo("University API", 
                "Sample API for University tutorial", 
                "1.0", 
                "Free to use", 
                new springfox.documentation.service.Contact("Yaroslav Voronovskyi", "https://www.linkedin.com/in/yaroslav-voronovskyi-b8a5a3191/", "yaroslav.voronovskyi@gmail.com"), 
                "API License", 
                "http://localhost:8081/",
                Collections.emptyList());
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
