package me.addi.orderservice.web;

import lombok.AllArgsConstructor;
import me.addi.orderservice.entities.Order;
import me.addi.orderservice.feign.CustomerRestClientService;
import me.addi.orderservice.feign.ProductItemRestClient;
import me.addi.orderservice.models.Customer;
import me.addi.orderservice.models.Product;
import me.addi.orderservice.repositories.OrderRepository;
import me.addi.orderservice.repositories.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController @AllArgsConstructor
public class OrderRestController {

    private OrderRepository or;
    private ProductItemRepository pir;
    private ProductItemRestClient pirc;
    private CustomerRestClientService crcs;

    @GetMapping("orders/{id}")
    public Order getOrder(@PathVariable String id)
    {
        Order order = or.findById(id).orElseThrow(() -> new RuntimeException(String.format("order %s not found", id)));
        Customer customer = crcs.getCustomer(order.getCustomerId());
        order.setCustomer(customer);
        order.getProducts().forEach(p -> {
            Product product = pirc.getProductById(p.getProductId());
            p.setProduct(product);
        });
        return order;
    }

    @GetMapping("orders")
    public Collection<Order> getOrders()
    {
        Collection<Order> orders = or.findAll();
        orders.forEach(order -> {
            Customer customer = crcs.getCustomer(order.getCustomerId());
            order.setCustomer(customer);
            order.getProducts().forEach(p -> {
                Product product = pirc.getProductById(p.getProductId());
                p.setProduct(product);
            });
        });
        return orders;
    }

    @GetMapping("ordersbyCustomerid/{id}")
    public Collection<Order> getOrdersByCustomerId(@PathVariable Long id)
    {
        Collection<Order> orders = or.findAllByCustomerId(id);
        orders.forEach(order -> {
            Customer customer = crcs.getCustomer(order.getCustomerId());
            order.setCustomer(customer);
            order.getProducts().forEach(p -> {
                Product product = pirc.getProductById(p.getProductId());
                p.setProduct(product);
            });
        });
        return orders;
    }
}
