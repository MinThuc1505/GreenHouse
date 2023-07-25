package com.greenhouse.model_v1;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

//@Entity
@Table(name = "Meterials")
@Data
public class Material implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Meterial")
    private String material;
}
