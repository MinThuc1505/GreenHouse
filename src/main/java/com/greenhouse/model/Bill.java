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
    private Long amount;

    @Column(name = "Discount_Code")
    private String discountCode;

    @Column(name = "Discount_Percent")
    private Double discountPercent;

    @Column(name = "New_Amount")
    private Long newAmount;

    @Column(name = "Bank_Code")
    private String bankCode;

    @Column(name = "Payment_Method")
    private String paymentMethod;

    @Column(name = "Receiver_Fullname")
    private String receiverFullname;

    @Column(name = "Receiver_Phone")
    private String receiverPhone;

    @Column(name = "Receiver_Address")
    private String receiverAddress;
    @Column(name = "Receiver_Fullname")
    private String receiverFullname;
    @Column(name = "Receiver_Phone")
    private String receiverPhone;

    @Column(name = "Status")
    private Integer status;
}
