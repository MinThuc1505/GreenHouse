package com.greenhouse.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Carts")
@Data
public class Cart implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;

    @Column(name = "Username")
    private String username;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Price")
    private Double price;

    @Column(name = "Amount")
    private Double amount;

    @Column(name = "Status")
    private Boolean status;
}
