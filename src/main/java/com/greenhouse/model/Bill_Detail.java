package com.greenhouse.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Bill_Detail")
@Data
public class Bill_Detail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BillId")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Price")
    private Double price;

    @Column(name = "Amount")
    private Double amount;
}
