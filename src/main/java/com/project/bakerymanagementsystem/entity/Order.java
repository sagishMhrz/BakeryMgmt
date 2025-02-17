package com.project.bakerymanagementsystem.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "orders")
@Getter
@Setter
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "ORDER_DATE")
    private LocalDateTime orderDate;

    @Column(name = "TOTAL_PRICE")
    private double totalPrice;

    @JoinColumn(name = "TABLE_ID")
    @ManyToOne(cascade = CascadeType.DETACH)
    @NotNull
    private RestaurantTable table;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "order", orphanRemoval = true)
    private Set<Item> items;
}
