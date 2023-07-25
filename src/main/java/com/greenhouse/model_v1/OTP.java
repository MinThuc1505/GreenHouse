package com.greenhouse.model_v1;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

//@Entity
@Table(name = "OTP")
@Data
public class OTP implements Serializable{
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
    private Account account;

    @Column(name = "OTPCode")
    private String otpCode;

    @Column(name = "ExpirationTime")
    private Date expirationTime;

    @Column(name = "Status")
    private String status;

    @Column(name = "CreateAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "UpdateAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
}
