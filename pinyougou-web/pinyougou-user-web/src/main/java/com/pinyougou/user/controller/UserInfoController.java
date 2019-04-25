package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Reference
    private UserService userService;

    /**修改用户个人信息*/
    @RequestMapping("/update")
    public boolean updateInfo(@RequestBody User user){
        return false;
    }

    /**回显用户个人信息*/
    /*@RequestMapping("/get")
    public User getInfo(String username){
        try {
            User user = userService.findUserByUsername(username);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /**回显用户个人信息*/
    @RequestMapping("/get")
    public Map<String,Object> getInfo(String username){
        try {
            User user = userService.findUserByUsername(username);
            Map<String,Object> map = new HashMap<>();
            map.put("id",user.getId().toString());
            map.put("address",user.getAddress());
            map.put("birthday",user.getBirthday());
            map.put("nickName",user.getNickName());
            map.put("profession",user.getProfession());
            map.put("sex",user.getSex());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/getProvince")
    public  List<Map<String,String>> getProvince(){
        try {
            return userService.findAllProvince();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/getAreas")
    public  List<Map<String,String>> getAreas(String cityId){
        try {
            return userService.findAreaByCity(cityId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/getCitys")
    public  List<Map<String,String>> getCitys(String provinceId){
        try {
            return userService.findCityByProvince(provinceId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 上传头像 */
    @RequestMapping("/upload")
    public String uploadHead(){
        return null;
    }

}
