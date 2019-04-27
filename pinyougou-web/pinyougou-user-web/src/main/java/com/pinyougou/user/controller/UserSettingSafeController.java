package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import com.sun.javafx.collections.MappingChange;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @author LLW
 * @date 2019/4/25
 */
@RestController
@RequestMapping("/user/setting")
public class UserSettingSafeController {

    @Reference(timeout = 10000)
    private UserService userService;

    //修改密码
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

    //判断短信验证码
    @PostMapping("/checkSmsCode")
    public boolean checkSmsCode(@RequestBody User user,String smsCode){
        return userService.checkSmsCode(user.getPhone(),smsCode);
    }

    @GetMapping("/checkPicCode")
    public Map<String,Object> checkPicCode(String picCode, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            String oldCode = (String)request.getSession()
                    .getAttribute(VerifyController.VERIFY_CODE);
            System.out.println("图片验证码： " + oldCode);
            System.out.println("用户的验证码： " + picCode);
            if (picCode.equals(oldCode)){
                map.put("result",true);
            }else {
                map.put("result",false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

}
