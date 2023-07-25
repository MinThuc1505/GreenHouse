package com.greenhouse.model_v1;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

//@Entity
@Table(name = "ImportProducts")
@Data
public class ImportProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BillImportProductId")
    private BillImportProduct billImportProduct;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;

    @Column(name = "PriceImport")
    private Double priceImport;

    @Column(name = "QuantityImport")
    private Integer quantityImport;

    @Column(name = "AmountImport")
    private Double amountImport;

    @Column(name = "Description")
    private String description;

    @Column(name = "Createdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
}
