package com.greenhouse.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "bill_import_product")
@Data
public class BillImportProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Username")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "ProviderId")
    private Provider provider;

    @Column(name = "Createdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "Amount")
    private Double amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "Status")
    private Boolean status;
}
