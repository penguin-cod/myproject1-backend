package com.charlie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.entity.Product;
import com.charlie.mapper.ProductMapper;
import com.charlie.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service//因為這個註解，讓controller注入Service時，就也會自動注入impl實現類
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Override
    //分頁查詢
    public Map<String, Object> getProductPage(int page, int size, String keyword) {
        //分頁物件
        Page<Product> pageInfo=new Page<>(page,size);
        //查詢條件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if(keyword!=null&&!keyword.isEmpty()){
            queryWrapper.like("name",keyword);
        }
        //查詢，this代表ServiceImpl的實體
        this.page(pageInfo,queryWrapper);
        //回傳資料
        Map<String, Object> result = new HashMap<>();
        result.put("records",pageInfo.getRecords());
        result.put("total",pageInfo.getTotal());
        return result;
    }
}
