package com.charlie.service;

import com.charlie.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.userdetails.User.withUsername;
@Slf4j
// Spring Security 用的自定義使用者驗證服務，它的用途是 在使用者登入時，從資料庫查出使用者資料，提供給 Spring Security 做驗證。
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // 你自己的服務，用來從 DB 查 user
    private final UserService userService;
    //Spring 會自動幫你注入 UserService
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }
    //登入或驗證 token 時會自動呼叫的方法，參數是帳號。
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 記錄查詢用戶名稱
        log.debug("嘗試載入使用者資訊：{}", username);
        // 從資料庫查帳號
        User user = userService.getByUsername(username);

        if (user == null) {
            log.warn("找不到使用者：{}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        log.info("成功載入使用者:{},角色:{}",user.getUsername(),user.getRole());
        // 回傳 UserDetails 物件 (Spring Security 需要的格式)
        return  withUsername(user.getUsername())
                .password(user.getPassword())  // 加密後的密碼
                .authorities("ROLE_"+user.getRole())      // Spring Security 的角色名稱有標準的 "ROLE_" 前綴，方便你在設定權限時用 .hasRole("ADMIN") 或 .hasRole("USER") 等判斷。
                .build();
    }
}