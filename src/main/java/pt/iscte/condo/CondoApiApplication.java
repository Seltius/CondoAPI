package pt.iscte.condo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CondoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CondoApiApplication.class, args);
	}

}
