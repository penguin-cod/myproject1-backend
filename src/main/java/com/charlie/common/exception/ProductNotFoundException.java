package com.charlie.common.exception;
//自訂義Exception捕捉更新失敗
public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
