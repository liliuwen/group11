package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Reference(timeout = 5000)
    private UserService userService;
    /** 注入文件服务器访问地址 */
    @Value("${fileServerUrl}")
    private String fileServerUrl;

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
            map.put("headPic",user.getHeadPic());
            map.put("sex",user.getSex());
            map.put("phone",user.getPhone());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //修改保存个人资料
    @PostMapping("/sendApply")
    public boolean sendApply(@RequestBody User user){
        try {
            userService.updateByUserInfo(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile multipartFile) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", 500);
        try {
            /** 加载配置文件，产生该文件绝对路径 */
            String conf_filename = this.getClass()
                    .getResource("/fastdfs_client.conf").getPath();
            /** 初始化客户端全局对象 */
            ClientGlobal.init(conf_filename);
            /** 创建存储客户端对象 */
            StorageClient storageClient = new StorageClient();
            /** 获取原文件名 */
            String originalFilename =
                    multipartFile.getOriginalFilename();
            /** 上传文件到FastDFS服务器 */
            String[] arr = storageClient
                    .upload_file(multipartFile.getBytes(),
                            FilenameUtils.getExtension(originalFilename), null);
            /** 拼接返回的 url 和 ip 地址，拼装成完整的 url */
            StringBuilder url = new StringBuilder(fileServerUrl);
            for (String str : arr){
                url.append("/" + str);
            }
            data.put("status", 200);
            data.put("url", url.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    //获取头像地址和昵称
    @GetMapping("/getHead")
    public Map<String,String> getHead(String username){
        try {
            User user = userService.getHeadByUsername(username);
            Map<String,String> map = new HashMap<>();
            map.put("headPic",user.getHeadPic());
            map.put("nickName",user.getNickName());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
