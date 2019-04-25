package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 商家控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-01<p>
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference(timeout = 10000)
    private SellerService sellerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 商家申请入驻 */
    @PostMapping("/save")
    public boolean save(@RequestBody Seller seller){
        try{
            String password = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(password);
            sellerService.save(seller);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    //显示商家资料
    @GetMapping("/findOne")
    public Seller findOne(){
         try {
             // 获取登录用户名
             String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
             return sellerService.findOne(sellerId);

         }catch (Exception ex){
             ex.printStackTrace();
         }
        return null;
    }


    //商家资料修改
    @PostMapping("/update")
    public boolean update(@RequestBody Seller seller){
         try {
             // 获取安全上下文对象
             SecurityContext context = SecurityContextHolder.getContext();
             // 获取用户名
             String sellerId = context.getAuthentication().getName();
             System.out.println("loginName = " + sellerId);
             sellerService.update(seller);
             return true;
             }catch (Exception ex){
             ex.printStackTrace();
             }
        return false;
    }

    //查询原来密码
    @PostMapping("/findOldPassword")
    public boolean findOldPassword(String oldPassword){
        try {
            // 获取安全上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            // 获取用户名
            String sellerId = context.getAuthentication().getName();
            //查询数据库加密的原密码
           String password = sellerService.findOldPassword(sellerId);
            System.out.println("loginName = " + sellerId);
           //对比密码
            boolean matches = passwordEncoder.matches(oldPassword, password);

            return matches;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


    /** 修改密码 */
    @PostMapping("/updatePassword")
    public boolean updatePassword(String newPassword){
        try {
            // 获取安全上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            // 获取用户名
            String sellerId = context.getAuthentication().getName();
            //根据用户id修改密码
            String password = passwordEncoder.encode(newPassword);
            sellerService.updatePassword(sellerId, password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
