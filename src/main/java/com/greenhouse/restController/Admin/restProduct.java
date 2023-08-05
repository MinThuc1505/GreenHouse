package com.greenhouse.restController.Admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.dto.ProductDTO;
import com.greenhouse.model.Product;
@RestController
@RequestMapping(value = "/rest/products")
public class restProduct {

    @Autowired
    ProductDAO productDAO;

    @GetMapping
    private ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> productList = productDAO.findAll();
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : productList) {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(product, productDTO);
            productDTOList.add(productDTO);
        }
        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<ProductDTO> getOne(@PathVariable("id") Integer id) {
        if (!productDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Product product = productDAO.findById(id).get();
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping
    private ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
        // Kiểm tra các trường bắt buộc trước khi tạo sản phẩm
        if (productDTO.getId() != null && productDAO.existsById(productDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }

        // Chuyển đổi từ DTO sang Entity để lưu vào cơ sở dữ liệu
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        Product savedProduct = productDAO.save(product);

        // Chuyển đổi từ Entity sang DTO để trả về kết quả
        ProductDTO savedProductDTO = new ProductDTO();
        BeanUtils.copyProperties(savedProduct, savedProductDTO);

        return ResponseEntity.ok(savedProductDTO);
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<ProductDTO> update(@PathVariable("id") Integer id, @RequestBody ProductDTO productDTO) {
        if (!productDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Kiểm tra các trường bắt buộc trước khi cập nhật sản phẩm
        if (productDTO.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Chuyển đổi từ DTO sang Entity để cập nhật vào cơ sở dữ liệu
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setId(id); // Đảm bảo cập nhật cho sản phẩm với id đã cho
        Product updatedProduct = productDAO.save(product);

        // Chuyển đổi từ Entity sang DTO để trả về kết quả
        ProductDTO updatedProductDTO = new ProductDTO();
        BeanUtils.copyProperties(updatedProduct, updatedProductDTO);

        return ResponseEntity.ok(updatedProductDTO);
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!productDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productDAO.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
