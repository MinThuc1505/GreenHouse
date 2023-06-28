package com.greenhouse.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Details_Bill")
@Data
public class DetailBill implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private BigDecimal price;
}
