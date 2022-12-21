package me.addi.Customerservice;

import me.addi.Customerservice.entities.Customer;
import me.addi.Customerservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(CustomerRepository cr)
	{
		return args -> {
			Stream.of("addi","kenza", "Sanae", "Ali").forEach(c -> {
				Customer customer = new Customer();
				customer.setEmail(c.toLowerCase()+"@gmail.com");
				customer.setName(c.toUpperCase());
				cr.save(customer);
			});
		};
	}

}
