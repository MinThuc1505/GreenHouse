package com.greenhouse.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "Categories")
@Data
public class Category implements Serializable{
   @Id
   @Column(name = "id")
    @NotEmpty(message = "{id.NotEmpty}")
    private String id;

    @Column(name = "categories_name")
    @NotEmpty(message = "{categoryName.NotEmpty}")
    private String categoryName;

    @OneToMany(mappedBy = "categoryId")
    private List<SetCategory> setCategory;
    
}
