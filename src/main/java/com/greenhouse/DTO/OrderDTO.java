package com.greenhouse.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class OrderDTO {
    private String username;
    private Date createDate;
    private long amount;
    private String discountCode;
    private double discountPercent;
    private long newAmount;
    private String receiverFullname;
    private String receiverPhone;
    private String receiverAddress;
    private String paymentMethod;
}
