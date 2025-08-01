package com.charlie.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//設定哪些路徑需要驗證、註冊過濾器
@Configuration
@EnableWebSecurity//開啟 Spring Security 的自定義設定能力。定義「誰可以存取什麼」、「怎麼驗證登入」等等安全邏輯。
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //設定安全策略
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())//前後端分離 + JWT 的情境下，通常不需要 CSRF（跨站請求偽造）防護，這行是將其關閉。因為前端用的是 token。
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // 登入開放，不需要 token
                        .requestMatchers("/admin/**").hasRole("admin")//只有admin身分才能訪問/admin/api
                        .requestMatchers(// 放行swagger,
                                "/v3/api-docs/**",        // Swagger 3
                                "/swagger-resources/**",
                                "/swagger-ui/**",         // Swagger 3
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()         // 其他 API 都要驗證 token
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//不使用 HTTP Session，符合 REST API 無狀態的設計原則
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 註冊你自己的過濾器，每次請求都會先驗證 JWT，再交給後面的預設認證邏輯。
                .build();//建構為一個 SecurityFilterChain 實例並註冊到 Spring 容器中。
    }
}

//[前端登入送出帳密] ──▶ /login
//                         │
//                    認證成功 → JwtUtils 產生 JWT
//                         │
//                    將 JWT 回傳給前端 (存在 Header 或 localStorage)
//                         │
// [前端帶著 JWT 發 API 請求] ──▶ JwtAuthenticationFilter
//                                   │
//                              驗證 JWT 合法性與過期
//                                   │
//                              載入使用者資料（CustomUserDetailsService）
//                                   │
//                              設定為 Spring Security 已登入狀態
//                                   │
//                              放行給 Controller