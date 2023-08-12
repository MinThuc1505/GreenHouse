package com.greenhouse.restController.Admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.greenhouse.DAO.ImportProductDAO;
import com.greenhouse.DAO.ProductDAO; // Thêm import cho ProductDAO
import com.greenhouse.model.Account;
import com.greenhouse.model.ImportProduct;
import com.greenhouse.model.Material;
import com.greenhouse.model.Product; // Thêm import cho Product
import com.greenhouse.model.Size;

@RestController
@RequestMapping(value = "/rest/importProduct")
public class RestImportProductController {

    @Autowired
    ImportProductDAO importProductDAO;

    @Autowired
    ProductDAO productDAO; // Khai báo ProductDAO

    @GetMapping
    private ResponseEntity<List<ImportProduct>> getAllImportProducts() {
        return ResponseEntity.ok(importProductDAO.findAll());
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<ImportProduct> getOne(@PathVariable("id") Integer id) {
        if (!importProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(importProductDAO.findById(id).get());
    }

    @PostMapping
    private ResponseEntity<ImportProduct> create(@RequestBody ImportProduct importProduct) {
        if (importProduct.getId() != null && importProductDAO.existsById(importProduct.getId())) {
            return ResponseEntity.badRequest().build();
        }

        // Tính tổng giá trị (amountImport)
        Double amountImport = importProduct.getPriceImport() * importProduct.getQuantityImport();
        importProduct.setAmountImport(amountImport);

        // Lấy thông tin sản phẩm từ ImportProduct
        Product product = importProduct.getProduct();
        if (product != null) {
            Integer quantity = product.getQuantity();
            if (quantity != null) {
                // Cập nhật số lượng sản phẩm
                product.setQuantity(quantity + importProduct.getQuantityImport());
                productDAO.save(product);
            }
        }

        return ResponseEntity.ok(importProductDAO.save(importProduct));
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<ImportProduct> update(@PathVariable("id") Integer id,
            @RequestBody ImportProduct importProduct) {
        if (!importProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Lấy ImportProduct cũ từ cơ sở dữ liệu
        ImportProduct existingImportProduct = importProductDAO.findById(id).orElse(null);
        if (existingImportProduct == null) {
            return ResponseEntity.notFound().build();
        }

        // Lấy thông tin sản phẩm từ ImportProduct
        Product product = importProduct.getProduct();
        if (product != null) {
            Integer oldQuantityImport = existingImportProduct.getQuantityImport();
            Integer newQuantityImport = importProduct.getQuantityImport();

            // Lấy ImportProduct cũ từ cơ sở dữ liệu
            Product existingProduct = productDAO.findById(id).orElse(null);
            if (existingProduct == null) {
                return ResponseEntity.notFound().build();
            }
            // Lấy giá trị cũ của kích thước và chất liệu
            Size oldSize = existingProduct.getSize();
            Material oldMaterial = existingProduct.getMaterial();

            // Cập nhật số lượng sản phẩm
            Integer quantity = product.getQuantity();
            if (quantity != null && oldQuantityImport != null) {
                int updatedQuantity = quantity - oldQuantityImport + newQuantityImport;
                product.setQuantity(updatedQuantity);
            }

            // Đặt lại kích thước và chất liệu về giá trị cũ
            product.setSize(oldSize);
            product.setMaterial(oldMaterial);
            productDAO.save(product);
        }

        // Cập nhật các thông tin khác của ImportProduct
        existingImportProduct.setProduct(importProduct.getProduct());
        existingImportProduct.setBillImportProduct(importProduct.getBillImportProduct());
        existingImportProduct.setPriceImport(importProduct.getPriceImport());
        existingImportProduct.setQuantityImport(importProduct.getQuantityImport());
        existingImportProduct.setDescription(importProduct.getDescription());

        // Tính tổng giá trị (amountImport)
        Double amountImport = importProduct.getPriceImport() * importProduct.getQuantityImport();
        existingImportProduct.setAmountImport(amountImport);

        // Cập nhật ngày tạo
        existingImportProduct.setCreateDate(importProduct.getCreateDate());

        // Lưu ImportProduct đã được cập nhật
        ImportProduct updatedImportProduct = importProductDAO.save(existingImportProduct);

        return ResponseEntity.ok(updatedImportProduct);
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!importProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        importProductDAO.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ImportProduct>> getAllAccounts(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        try {
            System.out.println("Requested Page: " + page);
            System.out.println("Page Size: " + size);

            Pageable pageable = PageRequest.of(page, size);
            Page<ImportProduct> importproducts = importProductDAO.findAll(pageable);

            return ResponseEntity.ok(importproducts);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
