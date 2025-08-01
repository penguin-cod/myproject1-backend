package com.charlie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.entity.Product;

import java.util.Map;
//Iservice提供業務邏輯方法，get,save,remove,update,list...
public interface ProductService extends IService<Product> {
    //分頁查詢
    Map<String,Object>getProductPage(int page,int size,String keyword);
}
