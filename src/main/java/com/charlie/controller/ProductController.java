package com.charlie.controller;

import com.charlie.common.exception.ProductNotFoundException;
import com.charlie.common.response.Result;
import com.charlie.entity.Product;
import com.charlie.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result<Map<String, Object>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        log.info("查詢商品列表，page:{},size:{},keyword:{}",page,size,keyword);
        Map<String, Object> data = productService.getProductPage(page, size, keyword);
        return Result.success(data);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Product product) {//@RequestBody:Spring 會自動幫你把這個 JSON 轉成 Java 物件 Product
        log.info("新增商品:{}",product.getName());
        productService.save(product); // 如果 save() 出錯，拋 Exception，自動交給 GlobalExceptionHandler
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Product product) {
        log.info("嘗試更新商品ID:{}",product.getProductId());
        Product currentProduct = productService.getById(product.getId());
        if (currentProduct == null){
            log.warn("商品更新失敗，ID: {} 不存在",product.getProductId());
            throw new ProductNotFoundException("更新失敗,id:" + product.getId() + "不存在");
        }
        log.info("商品更新成功，ID: {}",product.getProductId());
        productService.updateById(product);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("嘗試刪除商品 ID:{}",id);
        boolean removed = productService.removeById(id);
        if (removed) {
            log.info("刪除商品成功 ID:{}",id);
            return Result.success();
        } else {
            log.warn("刪除商品失敗 ID:{}",id);
            // 可以用你自己定義的失敗Result格式，也可以拋異常
            return Result.fail("商品不存在，刪除失敗");
        }
    }
}
