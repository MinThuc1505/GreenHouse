package com.greenhouse.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Import_Product")
@Data
public class ImportProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bill_import_product_id")
    private BillImportProduct billImportProduct;

    @ManyToOne
    @JoinColumn(name = "Product_Id")
    private Product product;

    @Column(name = "price_import")
    private Double priceImport;

    @Column(name = "quantity_import")
    private Integer quantityImport;

    @Column(name = "amount_import")
    private Double amountImport;

    @Column(name = "Description")
    private String description;

    @Column(name = "Createdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

}
