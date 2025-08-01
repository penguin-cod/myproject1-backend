package com.charlie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.entity.User;

import java.util.Map;


public interface UserService extends IService<User> {
    User getByUsername(String username);
    //分頁查詢
    Map<String,Object> getUserPage(int page,int size,String keyword);

}
