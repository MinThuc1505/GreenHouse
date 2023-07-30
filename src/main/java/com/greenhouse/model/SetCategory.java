package com.greenhouse.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SetCategory")
@Data
public class SetCategory implements Serializable{
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

    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category category;
}
