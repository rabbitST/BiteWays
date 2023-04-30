package com.codecool.biteways;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
        info = @Info(
                title = "${biteWays.api.title}",
                version = "${biteWays.api.version}",
                description = "${biteWays.api.description}",
                license = @License(name = "${biteWays.api.license}"),
                contact = @Contact(name="${biteWays.api.contact.name}", email = "${biteWays.api.contact.email}")
        )
)
@SpringBootApplication
public class BiteWaysApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiteWaysApplication.class, args);
    }

}
