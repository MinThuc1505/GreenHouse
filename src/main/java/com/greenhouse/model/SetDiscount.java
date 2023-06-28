package com.greenhouse.model;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Set_Discounts")
@Data
public class SetDiscount implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discountId; 

    @Column(name = "quantity_used")
    private int quantityUsed;

}
