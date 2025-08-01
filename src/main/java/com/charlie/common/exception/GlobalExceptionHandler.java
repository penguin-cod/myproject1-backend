package com.charlie.common.exception;

import com.charlie.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice//全域控制器的攔截與處理
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)//所有controller受監控
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.warn("發生錯誤:{}",e.getMessage(),e); // 印到 console 方便除錯
        Result<Void> result = Result.fail("發生錯誤：" + e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);//ResponseEntity<>封裝http回應
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Result<Void>> handleProductNotFoundException(ProductNotFoundException e) {
        Result<Void> result = Result.fail(e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}

