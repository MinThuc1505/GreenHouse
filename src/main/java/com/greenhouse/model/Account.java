package com.greenhouse.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Accounts")
@Data
public class Account implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Fullname")
    private String fullName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Gender")
    private Boolean gender;

    @Column(name = "Address")
    private String address;

    @Column(name = "Image")
    private String image;

    @Column(name = "Role")
    private Boolean role;

    @Column(name = "Createdate")
    private Date createDate;
}
