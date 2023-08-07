package com.greenhouse.DTO;

import java.io.Serializable;

import com.greenhouse.model.Material;
import com.greenhouse.model.Size;

import lombok.Data;

@Data
public class ProductDTO implements Serializable {
    private Integer id;
    private String name;
    private Double price;
    private Integer quantity;
    private String description;
    private Boolean status;
    private String image;
    // Định nghĩa thuộc tính cho Size và Material mà bạn muốn hiển thị
    private Size size;
    private Material material;
}
