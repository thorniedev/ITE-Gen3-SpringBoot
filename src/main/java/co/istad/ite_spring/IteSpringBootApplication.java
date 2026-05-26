package co.istad.ite_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties
@SpringBootApplication
public class IteSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(IteSpringBootApplication.class, args);
	}

}
