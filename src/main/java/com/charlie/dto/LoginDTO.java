package com.charlie.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "名稱不能為空")
    private String username;
    @NotBlank(message = "密碼不能為空")
    private String password;
}
