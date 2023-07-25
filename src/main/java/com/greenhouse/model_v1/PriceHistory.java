package com.greenhouse.model_v1;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

//@Entity
@Table(name = "PriceHistory")
@Data
public class PriceHistory implements Serializable{
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

    @Column(name = "Price")
    private Double price;

    @Column(name = "ChangeDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;
}
