package com.greenhouse.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @NotBlank(message = "{Product.id.notBlank}")
    private String id;

    @Column(name = "product_name")
    @NotBlank(message = "{Product.productName.notBlank}")
    private String productName;

    @Column(name = "in_stock")
    @Min(value = 1, message = "{Product.inStock.min}")
    @Max(value = 100, message = "{Product.inStock.max}")
    private int inStock;

    @NotNull(message = "{Product.price.notNull}")
    @DecimalMin(value = "1", inclusive = false, message = "{Product.price.decimalMin}")
    @DecimalMax(value = "1000000000", inclusive = false, message = "{Product.price.decimalMax}")
    private BigDecimal price;

    @Column(name = "description")
    @Length(max = 250, message = "{Product.description.length}")
    private String description;

    @Column(name = "images")
    String image;

    @OneToMany(mappedBy = "productId")
    List<SetCategory> setCategory;

    @OneToMany(mappedBy = "productId")
    List<SetDiscount> setDiscounts;
}
