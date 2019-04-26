package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LLW
 * @date 2019/4/25
 */
@RestController
@RequestMapping("/user/setting")
public class UserSettingSafeController {

    @Reference(timeout = 10000)
    private UserService userService;

    @RequestMapping("/updatePwd")
    public boolean updatePwd(@RequestBody User user){
        try {
            if (user != null && StringUtils.isNoneBlank(user.getUsername())){
                userService.update(user);
                System.out.println(user.getUsername() + " ----->  " + user.getPassword());
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
