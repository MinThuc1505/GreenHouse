package com.greenhouse.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Products")
@Data
public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Price")
    private Double price;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Description")
    private String description;

    @Column(name = "Status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "SizeId")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "MeterialId")
    private Material material;

    @Column(name = "Image")
    private String image;
}
