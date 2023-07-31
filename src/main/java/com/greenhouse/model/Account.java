package com.greenhouse.model;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "Accounts")
@Data
public class Account implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "Username")
	@NotBlank(message = "{NotBlank.Account.username}")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "{Pattern.Account.username}")
    private String username;

    @Column(name = "Password")
//    @NotBlank(message = "{NotBlank.Account.password}")
    @Length(max = 200, message = "{Length.Account.password}") 
    private String password;

    @Column(name = "Fullname")
//    @NotBlank(message = "{NotBlank.Account.fullname}")
    @Length(max = 50, message = "{Length.Account.fullname}")
    private String fullName;

    @Column(name = "Email")
//    @NotBlank(message = "{NotBlank.Account.email}")
    @Email(message = "{Email.Account.email}")
    private String email;

    @Column(name = "Phone")
//    @NotBlank(message = "{NotBlank.Account.phone}")
//    @Pattern(regexp = "^(09|03|07|08|05)\\d{8}$", message = "{Pattern.Account.phone}")
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
