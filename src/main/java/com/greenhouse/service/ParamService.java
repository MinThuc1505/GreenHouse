package com.greenhouse.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ParamService {
    @Autowired
    HttpServletRequest request;

    @Autowired
	ServletContext app;
    
    public String getString(String name, String defaultValue) {
        String value = request.getParameter(name);
        return value != null ? value : defaultValue;
    }

    public int getInt(String name, int defaultValue) {
        String value = request.getParameter(name);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public double getDouble(String name, double defaultValue) {
        String value = request.getParameter(name);
        return value != null ? Double.parseDouble(value) : defaultValue;
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        String value = request.getParameter(name);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public Date getDate(String name, String pattern) {
        String value = request.getParameter(name);
        if (value == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format");
        }
    }

    public File save(MultipartFile file, String path) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            byte[] bytes = file.getBytes();
            Path uploadPath = Paths.get(path);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
           Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, bytes);
            return filePath.toFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public File save1(MultipartFile file, String folder) {
        try {
            File dir = new File(request.getServletContext().getRealPath(folder));
            if (!dir.exists())
                dir.mkdirs();

            File saveFile = new File(dir, StringUtils.cleanPath(file.getOriginalFilename()));
            file.transferTo(saveFile);
            return saveFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public String getImageUrl(String imageName) {
		String imageUrl = "/images/" + imageName;
		return imageUrl;
	}
}
