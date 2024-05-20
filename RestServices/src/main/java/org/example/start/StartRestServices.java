package org.example.start;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@ComponentScan("org.example") //@ComponentScan("chat"): This annotation specifies the base packages to scan for Spring-managed components, such as @RestController, @Service, and @Repository beans. By specifying "chat", it ensures that all components in the "chat" package and its sub-packages are detected and registered in the Spring context.
@SpringBootApplication //This is a convenience annotation that combines three annotations: @EnableAutoConfiguration, @ComponentScan, and @Configuration. It marks this class as the entry point for the Spring Boot application and enables various Spring Boot features, including auto-configuration.
public class StartRestServices {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }
    @Primary
    @Bean(name="properties")

    public Properties getBdProperties(){
        Properties props = new Properties();
        try {
            System.out.println("Searching db.config in directory "+((new File(".")).getAbsolutePath()));
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.err.println("Configuration file bd.cong not found" + e);

        }
        return props;
    }
}