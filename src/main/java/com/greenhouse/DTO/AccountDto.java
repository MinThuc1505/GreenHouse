package com.greenhouse.DTO;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class AccountDto implements Serializable{
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phone;
    private Boolean gender;
    private String address;
    private String image;
    private Boolean role;
    private Date createDate;
}
