package com.greenhouse.model_v1;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

//@Entity
@Table(name = "Discounts")
@Data
public class Discount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "DiscountCode")
    private String discountCode;

    @Column(name = "DiscountPercent")
    private Double discountPercent;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "UsedQuantity")
    private Integer usedQuantity;

    @Column(name = "Startdate")
    @Temporal(TemporalType.TIMESTAMP) 
    private Date startDate;

    @Column(name = "Enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "Status")
    private String status;
}
