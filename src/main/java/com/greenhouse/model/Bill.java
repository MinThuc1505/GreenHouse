package com.greenhouse.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "Bills")
@Data
public class Bill implements Serializable {
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

    @Column(name = "BankCode")
    private String bankCode;

    @Column(name = "DiscountCode")
    private String discountCode;

    @Column(name = "DiscountPercent")
    private Double discountPercent;

    @Column(name = "NewAmount")
    private Long newAmount;

    @Column(name = "PaymentMethod")
    private String paymentMethod;

    @Column(name = "Receiver_Address")
    private String receiverAddress;

    @Column(name = "Status")
    private int status;
}
