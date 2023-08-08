package com.greenhouse.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class OrderDTO {
    private String username;
    private Date createDate;
    private long amount;
    private String discountCode;
    private String bankCode;
    private double discountPercent;
    private long newAmount;
    private String receiverAddress;
    private String paymentMethod;
}
