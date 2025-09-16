package com.charlie.config;

import com.charlie.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.charlie.common.utils.JwtUtils;
import java.io.IOException;
@Slf4j
//攔截請求，驗證 JWT，有效的話設置用戶資料，過濾器觸發jwt驗證機制
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {//確保每個請求只被過濾一次
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    //swagger白名單
    private final RequestMatcher swaggerWhitelist = new OrRequestMatcher(
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-ui/**"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        //當瀏覽器發出 CORS 預檢請求（OPTIONS 方法）時，直接放行，不處理 JWT 驗證。
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.info("OPTIONS預檢請求");
            filterChain.doFilter(request, response);//執行下一個filter或到最終的controller
            return;
        }
        //登入 API 不做 JWT 驗證
        String path = request.getRequestURI();
        if ("/login".equals(path)) {
            log.info("登入 API，跳過 JWT 驗證");
            filterChain.doFilter(request, response);
            return;
        }
        //swagger頁面 不做 JWT 驗證
        if (swaggerWhitelist.matches(request)){
            log.info("swagger 頁面，跳過 JWT 驗證");
            filterChain.doFilter(request, response);
            return;
        }



        
        String authHeader = request.getHeader("Authorization");//取得前端 Authorization 標頭

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("token具有Authorization標頭且為Bearer");
            String token = authHeader.substring(7);

            if (jwtUtils.validateToken(token)) {
                log.info("token驗證成功");
                String username = jwtUtils.getUsernameFromToken(token);

                try {
                    // 載入 UserDetails（權限資料）
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    // 建立認證物件，userDetails.getAuthorities()拿到使用者權限
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // 設定安全上下文（讓 Spring Security 知道這是已認證使用者）
                    //設定到 SecurityContextHolder 中，這樣後面的控制器就可以透過 @AuthenticationPrincipal 或 SecurityContext 取得使用者資訊。
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }catch(UsernameNotFoundException e) {
                    log.warn("資料庫找不到使用者");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                log.warn("token驗證失敗");
                //token驗證失敗 狀態碼設為401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }else{
            log.warn("未提供 Authorization 標頭，或格式錯誤");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //放行請求（給下一個 Filter 或 Controller）
        filterChain.doFilter(request, response);
    }

}