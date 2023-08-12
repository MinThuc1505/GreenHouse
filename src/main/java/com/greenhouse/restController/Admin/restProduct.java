package com.greenhouse.restController.Admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.greenhouse.DAO.PriceHistoryDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.DTO.ProductDTO;
import com.greenhouse.model.PriceHistory;
import com.greenhouse.model.Product;

@RestController
@RequestMapping(value = "/rest/products")
public class restProduct {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    ProductDAO productDAO;
    @Autowired
    PriceHistoryDAO priceHistoryDAO;

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
    private ResponseEntity<?> create(@RequestBody ProductDTO productDTO) {
    
        Map<String, String> errors = validateProductDTO(productDTO);
    
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
    
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        Product savedProduct = productDAO.save(product);
    
        ProductDTO savedProductDTO = new ProductDTO();
        BeanUtils.copyProperties(savedProduct, savedProductDTO);
    
        return ResponseEntity.ok(savedProductDTO);
    }
    
    @PutMapping(value = "/{id}")
    private ResponseEntity<ProductDTO> update(@PathVariable("id") Integer id,
            @RequestParam("productDTO") String productDTOJson,
            @RequestParam(value = "image", required = false) MultipartFile file) {
        // Parse dữ liệu từ productDTOJson thành đối tượng ProductDTO
        ProductDTO productDTO = new Gson().fromJson(productDTOJson, ProductDTO.class);
        if (!productDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
         Map<String, String> errors = validateProductDTOs(productDTO);
    
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Kiểm tra các trường bắt buộc trước khi cập nhật sản phẩm
        if (productDTO.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Tìm sản phẩm cần cập nhật
        Product existingProduct = productDAO.findById(id).orElse(null);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        // Lưu giá cũ của sản phẩm trước khi cập nhật
        Double oldProductPrice = existingProduct.getPrice();

        // Chuyển đổi từ DTO sang Entity để cập nhật vào cơ sở dữ liệu
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        // Lưu tệp ảnh vào thư mục /src/main/resources/static
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                String filePath = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/images/"
                        + fileName;
                File dest = new File(filePath);
                file.transferTo(dest);
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
        }

        product.setId(id); // Đảm bảo cập nhật cho sản phẩm với id đã cho
        Product updatedProduct = productDAO.save(product);
        // Kiểm tra nếu giá đã thay đổi thì lưu lịch sử giá
        if (!oldProductPrice.equals(updatedProduct.getPrice())) {
            // Tạo và lưu lịch sử giá với giá trị cũ của sản phẩm trước khi cập nhật
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setProduct(updatedProduct);
            priceHistory.setPrice(oldProductPrice); // Sử dụng giá cũ của sản phẩm
            priceHistory.setChangeDate(new Date());
            priceHistoryDAO.save(priceHistory); // Gọi phương thức lưu của PriceHistoryDAO
        }

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

        // Xóa lịch sử giá liên quan đến sản phẩm
        Product productToDelete = productDAO.findById(id).orElse(null);
        if (productToDelete != null) {
            priceHistoryDAO.deleteByProduct(productToDelete);
        }

        // Xóa sản phẩm chính
        productDAO.deleteById(id);
        return ResponseEntity.ok().build();
    }
    private Map<String, String> validateProductDTO(ProductDTO productDTO) {
    Map<String, String> errors = new HashMap<>();

    if (StringUtils.isEmpty(productDTO.getName())) {
        errors.put("name", "Tên sản phẩm không được bỏ trống.");
    }

    if (productDTO.getPrice() == null || productDTO.getPrice() <= 0 || productDTO.getPrice() >= 100000000) {
        errors.put("price", "Giá sản phẩm phải lớn hơn 0 và nhỏ hơn 100 triệu.");
    }

    if (productDTO.getQuantity() == null || productDTO.getQuantity() <= 0 || productDTO.getQuantity() >= 100) {
        errors.put("quantity", "Số lượng sản phẩm phải lớn hơn 0 và nhỏ hơn 100.");
    }
    if (productDTO.getName() != null && productDAO.existsByName(productDTO.getName())) {
        errors.put("nameExists", "Tên sản phẩm đã tồn tại.");
    }


    if (productDTO.getStatus() == null) {
        errors.put("status", "Trạng thái sản phẩm không được bỏ trống.");
    }
    return errors;
}
 private Map<String, String> validateProductDTOs(ProductDTO productDTO) {
    Map<String, String> errors = new HashMap<>();

    if ( productDTO.getPrice() <= 0 || productDTO.getPrice() >= 100000000) {
        errors.put("price", "Giá sản phẩm phải lớn hơn 0 và nhỏ hơn 100 triệu.");
    }

    if ( productDTO.getQuantity() <= 0 || productDTO.getQuantity() >= 100) {
        errors.put("quantity", "Số lượng sản phẩm phải lớn hơn 0 và nhỏ hơn 100.");
    }
    return errors;
}

}
