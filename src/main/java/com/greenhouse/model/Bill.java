package com.greenhouse.model;
import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "Bills")
@Data
public class Bill implements Serializable{
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

    @Column(name = "Createdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "Amount")
    private Double amount;

    @Column(name = "Discount_Code")
    private String discountCode;

    @Column(name = "Discount_Percent")
    private Double discountPercent;

    @Column(name = "New_Amount")
    private Double newAmount;

    @Column(name = "Payment_Method")
    private String paymentMethod;

    @Column(name = "Receiver_Address")
    private String receiverAddress;

    @Column(name = "Status")
    private Boolean status;
}
