package com.greenhouse.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Price_History")
@Data
public class PriceHistory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Product_Id")
    private Product product;

    @Column(name = "Price")
    private Double price;

    @Column(name = "change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;
}
