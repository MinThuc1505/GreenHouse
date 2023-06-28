package com.greenhouse.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "Discounts")
@Data
public class Discount implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotBlank(message = "{NotBlank.Discount.discountCode}")
  @Column(name = "discount_code")
  private String discountCode;

  @Min(value = 0, message = "{DecimalMin.Discount.discountPercent}")
  @Max(value = 100, message = "{DecimalMax.Discount.discountPercent}")
  @NotNull(message = "{NotBlank.DisCount.discountPercent}")
  @Column(name = "discount_percent")
  private Float discountPercent;

  @Min(value = 0, message = "{Min.Discount.quantity}")
  @NotNull(message = "{NotBlank.DisCount.quantity}")
  @Column(name = "quantity")
  private Integer quantity;

  @NotNull(message = "{NotBlank.DisCount.startDate}")
  @DateTimeFormat(pattern = "dd/MM/yyyy") // Xác định định dạng ngày
  @Column(name = "start_date")
  private Date startDate;

  @NotNull(message = "{NotBlank.DisCount.endDate}")
  @DateTimeFormat(pattern = "dd/MM/yyyy") // Xác định định dạng ngày
  @Column(name = "end_date")
  private Date endDate;

  @OneToMany(mappedBy = "discountId")
  List<SetDiscount> setDiscounts;

}