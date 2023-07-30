package com.greenhouse.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Provider")
@Data
public class Provider implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Address")
    private String address;

    @Column(name = "Email")
    private String email;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Description")
    private String description;
}
