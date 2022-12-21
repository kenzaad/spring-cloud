package me.addi.orderservice;

import me.addi.orderservice.entities.Order;
import me.addi.orderservice.entities.ProductItem;
import me.addi.orderservice.enums.OrderStatus;
import me.addi.orderservice.feign.CustomerRestClientService;
import me.addi.orderservice.feign.ProductItemRestClient;
import me.addi.orderservice.models.Customer;
import me.addi.orderservice.models.Product;
import me.addi.orderservice.repositories.OrderRepository;
import me.addi.orderservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(OrderRepository or,
							ProductItemRepository pir,
							CustomerRestClientService crcs,
							ProductItemRestClient pirc)
	{
		return args -> {
			List<Customer> customers = crcs.getCustomers().getContent().stream().toList();
			List<Product> products = pirc.pageProducts(0, 20).getContent().stream().toList();
			Random random = new Random();
			for (int i = 0; i < 20; i++)
			{
				Order order = Order.builder()
						.customerId(customers.get(random.nextInt(customers.size())).getId())
						.created(new Date())
						.status(Math.random() > 0.5 ? OrderStatus.CREATED : OrderStatus.DELIVERED)
						.build();
				order.setId(UUID.randomUUID().toString());
				//Customer customer = crcs.getCustomer(order.getCustomerId());
				//order.setCustomer(customer);
				//System.out.println(crcs.getCustomer(order.getCustomerId()).getName());
				Order savedOrder = or.save(order);
				for (int j = 0; j < products.size(); j++)
				{
					if (Math.random() > 0.60)
					{
						ProductItem productItem = ProductItem.builder()
								.order(savedOrder)
								.productId(products.get(j).getId())
								.price(products.get(j).getPrice())
								.quantity(random.nextInt(5) + 1)
								.product(products.get(j))
								.build();
						pir.save(productItem);
					}
				}
			}
		};
	}

}
