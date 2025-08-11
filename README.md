# myproject1 - 後端 (Backend)

本倉庫為此專案的後端，使用 Spring Boot 架構，提供完整 API 與 JWT 驗證機制，並進行權限控管。

🔗 [前端程式碼倉庫（Vue 3）](https://github.com/penguin-cod/myproject1-frontend)

---

## 🛠 技術棧

- Java 22
- Spring Boot 3
- Spring Security
- Maven
- JWT
- MySQL
- Druid
- Lombok
- MyBatisPlus
- Docker
- Swagger
- Git

---

## 📘 功能說明

- 使用者登入（JWT）
- 商品 API：CRUD
- 成員 API：CRUD + 權限
- Admin 專屬 API 保護

---

## ⚙️ 本機啟動方式

請先設定好資料庫與 JDK 22

匯入 sql檔案 project1_backup.sql

bash:
mvn spring-boot:run

---

## Swagger
http://localhost:8080/swagger-ui/index.html

須先進行登入api "username": "penguin","password": "123456"
     
在Authorization欄位填入 jwt，才能進行其他api測試

---

## Docker

下載DockerDesktop
前後端必須先設置好

---

## JWT流程架構

[前端AXIOS登入請求] ──▶ 前端 → 透過Request Body傳送帳號密碼
                         │
                      後端 → 產生JWT 透過包奘類Result 經由Response Body回傳
                         │
                      前端 → 從Response Body將JWT取出後存到localstorage
                         │
[前端發送API請求] ──▶ 觸發攔截器，header攜帶令牌Authorization:`Bearer ${token}`]
                                   │
                            後端 → 驗證 JWT 合法性與過期
                                   │
                            後端 → 載入使用者資料(權限控管)
                                   │
                            後端 → 設定為 Spring Security 已登入狀態
                                   │
                            後端 → 放行給 Controller
                                   │
                            後端 → 完成API請求 透過包裝類 經由Response Body回傳

---

## API請求細節

前端發出 AXIOS 請求
        ↓
瀏覽器自動發送 CORS 預檢請求 (OPTIONS)
        ↓
後端 Filter 放行預檢請求
        ↓
Controller 接收實際請求
        ↓
Service 層處理業務邏輯與調度
        ↓
ServiceImpl 層實作實際邏輯操作
        ↓
Mapper 執行 SQL 與資料庫互動 (MyBatisPlus)
        ↓
結果逐層回傳至 Controller
        ↓
Controller 統一使用 Result 類封裝回應格式
        ↓
前端接收 JSON 回應資料與 HTTP 狀態碼





