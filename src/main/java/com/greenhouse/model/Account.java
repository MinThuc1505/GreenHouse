package com.greenhouse.model;

import java.io.Serializable;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "Accounts")
@Data
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Username", nullable = false)
    @NotBlank(message = "{NotBlank.Account.username}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "{Pattern.Account.username}")
    private String username;

    @Column(name = "Fullname", nullable = false)
    @NotBlank(message = "{NotBlank.Account.fullname}")
    @Length(max = 50, message = "{Length.Account.fullname}")
    private String fullname;

    @Column(name = "Password", nullable = false)
    @NotBlank(message = "{NotBlank.Account.password}")
    @Length(max = 200, message = "{Length.Account.password}") 
    private String password;

    @Column(name = "Email", nullable = false)
    @NotBlank(message = "{NotBlank.Account.email}")
    @Email(message = "{Email.Account.email}")
    private String email;

    @Column(name = "Phone")
    @NotBlank(message = "{NotBlank.Account.phone}")
    @Pattern(regexp = "^(09|03|07|08|05)\\d{8}$", message = "{Pattern.Account.phone}")
    private String phone;

    @Column(name = "Address")
    private String address;

    @Column(name = "Image")
    private String image;

    @Column(name = "Role", nullable = false)
    private boolean role;

    @Column(name = "Active", nullable = false)
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "accountId")
    private List<Bill> bills;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "accountId")
    private List<Cart> carts;

}
