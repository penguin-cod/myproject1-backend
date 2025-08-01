package com.charlie.controller;

import com.charlie.common.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class UploadController {

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("檔案為空");
        }

        // 圖片儲存位置
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 建立唯一檔名避免重複
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + "_" + originalFilename;

        try {
            file.transferTo(new File(uploadDir + filename));
            String fileUrl = "http://localhost:8080/uploads/" + filename;
            return Result.success(fileUrl);
        } catch (IOException e) {
            return Result.fail("上傳失敗：" + e.getMessage());
        }
    }
}
