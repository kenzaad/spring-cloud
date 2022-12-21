package me.addi.orderservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.addi.orderservice.enums.OrderStatus;
import me.addi.orderservice.models.Customer;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data @Builder
@Table(name = "orders")
public class Order {
    @Id
    private String id;
    private Date created;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Long customerId;
    @Transient
    private Customer customer;
    @OneToMany(mappedBy = "order")
    private Collection<ProductItem> products;

    public double getTotal()
    {
        double sum = 0;
        for (ProductItem p : products)
        {
            sum += p.getAmount();
        }
        return sum;
    }
}
