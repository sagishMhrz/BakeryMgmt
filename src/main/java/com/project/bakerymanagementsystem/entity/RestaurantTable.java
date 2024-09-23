package com.project.bakerymanagementsystem.entity;


import com.project.bakerymanagementsystem.data.TableStatus;
import lombok.Data;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "restaurant_tables")
@Data
public class RestaurantTable {
    @NotNull
    @Id
    @Column(name = "TABLE_ID")
    private long tableId;

    @Column(name = "TABLE_CAPACITY")
    private int capacity;

    @Column(name = "TABLE_STATUS")
    private TableStatus status;
}
