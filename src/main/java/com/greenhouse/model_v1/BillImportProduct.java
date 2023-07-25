package com.greenhouse.model_v1;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

//@Entity
@Table(name = "BillImportProduct")
@Data
public class BillImportProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Username")
    @ToString.Exclude
    private Account account;

    @ManyToOne
    @JoinColumn(name = "ProviderId")
    private Provider provider;

    @Column(name = "Createdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "Amount")
    private Double amount;

    @Column(name = "PaymentMethod")
    private String paymentMethod;

    @Column(name = "Status")
    private Boolean status;
}
