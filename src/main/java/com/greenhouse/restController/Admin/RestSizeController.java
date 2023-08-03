package com.greenhouse.restcontrollerAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.greenhouse.DAO.SizeDAO;
import com.greenhouse.dto.SizeRequest;
import com.greenhouse.model.Size;

@RestController
@RequestMapping(value = "/rest/sizes")
public class RestSizeController {

    @Autowired
    SizeDAO sizeDAO;

    @GetMapping
    public ResponseEntity<List<Size>> getAllSizes() {
        List<Size> sizes = sizeDAO.findAll();
        return ResponseEntity.ok(sizes);
    }

    @PostMapping
    public ResponseEntity<Size> createSize(@RequestBody SizeRequest sizeRequest) {
        // Kiểm tra xem các thông số chiều cao, chiều rộng và chiều dài không được để trống
        if ( sizeRequest.getHeight() == null || sizeRequest.getWidth() == null || sizeRequest.getLength() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Tạo mới đối tượng Size từ thông số chiều cao, chiều rộng và chiều dài
        String size = sizeRequest.getHeight() + "x" + sizeRequest.getWidth() + "x" + sizeRequest.getLength();
        Size newSize = new Size();
        newSize.setSize(size);
        System.out.println(newSize);

        return ResponseEntity.ok(sizeDAO.save(newSize));
    }

}
