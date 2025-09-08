package com.charlie.controller;

import com.charlie.common.response.Result;
import com.charlie.dto.LoginDTO;
import com.charlie.entity.User;
import com.charlie.service.UserService;
import com.charlie.common.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")//登入使用post代表執行一個動作
    public Result<Map<String, String>> login(@RequestBody @Valid LoginDTO dto){//@Valid 會啟用你在 DTO裡面用的規則
        log.info("嘗試使用者登入:{}",dto.getUsername());
        User dbuser=userService.getByUsername(dto.getUsername());
        if (dbuser == null || !dbuser.getPassword().equals(dto.getPassword())) {
            log.info("登入失敗，使用者{}",dto.getUsername());   
            return Result.fail("帳號或密碼錯誤");
        }
        String token=jwtUtils.generateToken(dbuser.getUsername(), dbuser.getRole());
        log.info("登入成功：使用者 {}，角色 {}", dbuser.getUsername(), dbuser.getRole());
        return Result.success(Map.of("token", token));//鍵值對， 不可變 Map
    }
}
