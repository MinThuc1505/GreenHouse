package com.greenhouse.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Discounts")
@Data
public class Discount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "DiscountCode")
    private String discount_code;

    @Column(name = "DiscountPercent")
    private Double discountPercent;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "UsedQuantity")
    private Integer usedQuantity;


    @Temporal(TemporalType.TIMESTAMP) 
    @Column(name = "Startdate")	
    private Date startDate;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Enddate")
    private Date endDate;

    @Column(name = "Status")
    private String status;
}
