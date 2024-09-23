package com.project.bakerymanagementsystem.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
@Entity
@Getter
@Setter
@Table(name = "order_items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private long itemId;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    @Column(name = "QUANTITY")
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public int setQuantity(int quantity) {
        this.quantity = quantity;
        return  quantity;
    }
}
