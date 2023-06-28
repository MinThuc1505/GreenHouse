package com.greenhouse.model;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Set_Categories")
@Data
public class SetCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "Product_id")
    Product productId;

    @ManyToOne
    @JoinColumn(name = "Category_id")
    Category categoryId;
}
