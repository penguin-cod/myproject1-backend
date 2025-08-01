package com.charlie.controller;

import com.charlie.common.response.Result;
import com.charlie.entity.User;
import com.charlie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Result<Map<String,Object>> getUsers(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "10")int size,
            @RequestParam(required = false)String keyword
    ){
        log.info("查詢使用者列表，page={},size={},keyword={}",page,size,keyword);
        Map<String, Object> data = userService.getUserPage(page, size, keyword);
        log.info("查詢結果為:{}",data);
        return Result.success(data);
    }
    @PostMapping
    public Result<Void> add(@RequestBody User user){
        log.info("新增使用者:{}",user);
        userService.save(user);
        return Result.success();
    }
    @PutMapping
    public Result<Void> update(@RequestBody User user){
        log.info("更新使用者:{}",user);
        User currentUser = userService.getById(user.getId());
        if(currentUser==null){
            log.warn("更新失敗，找不到使用者 ID={}",user.getId());
            return Result.fail("更新使用者失敗");
        }
        userService.updateById(user);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){
        log.info("刪除使用者，ID={}",id);
        boolean removed = userService.removeById(id);
        if (removed) {
            return Result.success();
        } else {
            log.warn("刪除失敗，使用者不存在 ID={}",id);
            // 可以用你自己定義的失敗Result格式，也可以拋異常
            return Result.fail("商品不存在，刪除失敗");
        }
    }
}
