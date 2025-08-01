package com.charlie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.entity.Product;
import com.charlie.entity.User;
import com.charlie.mapper.UserMapper;
import com.charlie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper);//查詢單筆
    }
    @Override
    public Map<String,Object> getUserPage(int page, int size, String keyword){
        //分頁物件
        Page<User> pageInfo=new Page<>(page,size);
        //查詢條件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(keyword!=null&&!keyword.isEmpty()){
            queryWrapper.like("username",keyword);
        }
        //查詢
        this.page(pageInfo,queryWrapper);
        //回傳資料
        Map<String, Object> result = new HashMap<>();
        result.put("records",pageInfo.getRecords());
        result.put("total",pageInfo.getTotal());
        return result;
   }
}
