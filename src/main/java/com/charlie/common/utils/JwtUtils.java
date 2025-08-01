package com.charlie.common.utils;



import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component//註冊為bean
public class JwtUtils {
    //取得properties檔案的資料
    @Value("${jwt.secret}") //必須是組件或是@bean才能使用@Value
    private String secret;
    private SecretKey secretKey;


    //根據提供的字串用於HS256，了正確處理編碼（例如非英文文字），使用了 UTF-8。
    @PostConstruct//在 Bean 建立完成並且依賴注入完成之後，要自動執行的方法
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    private final long expireTime = 1000 * 60 * 60 * 24; // 過期時間 1 天

    public String generateToken(String username,String role) {
        return Jwts.builder()
                .setSubject(username) //payload 使用者識別資訊
                .claim("role",role)//payload權限，claim就是自訂義payload
                .setIssuedAt(new Date()) //payload 簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))//payload 過期時間
                .signWith(secretKey)//簽名
                .compact();//組成字串
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()//這是 JWT 的解析器建構器，用來建造一個能解析 JWT 的物件。
                .setSigningKey(secretKey)//JWT 通常會被簽名，這一步是用來確認這個 token 沒被篡改。
                .build()//建立解析器（Parser）物件
                .parseClaimsJws(token)//對傳入的 JWT token 進行解析，並驗證簽名。 這個方法會回傳一個 Jws<Claims> 物件，裡面有 JWT 的內容（Claims）也就是（payload 資料）。
                .getBody()//取得 JWT 的主體內容（Claims），也就是 JWT 裡面的有效負載部分。
                .getSubject();//取得 subject 欄位的值。
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()//這是 JWT 的解析器建構器，用來建造一個能解析 JWT 的物件。
                    .setSigningKey(secretKey)// secretKey 設定簽名密鑰，指定「用什麼密鑰去驗證簽名」。如果成功，表示 token 是有效的。
                    .build()//建立解析器（Parser）物件
                    .parseClaimsJws(token);//對傳入的 JWT token 進行解析，並驗證簽名。 這個方法會回傳一個 Jws<Claims> 物件，裡面有 JWT 的內容（Claims）也就是（payload 資料）。
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token 已過期");
        } catch (MalformedJwtException e) {
            System.out.println("Token 格式錯誤");
        } catch (SignatureException e) {
            System.out.println("簽名驗證失敗");
        } catch (Exception e) {
            System.out.println("Token 驗證失敗：" + e.getMessage());
        }
        return false;
    }
}