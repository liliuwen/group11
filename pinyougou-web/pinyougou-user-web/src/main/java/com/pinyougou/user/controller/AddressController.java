package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.AddressService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户地址控制器
 */
@RestController
public class AddressController {

    @Reference(timeout = 10000)
    private AddressService addressService;
    /**
     * 查询用户地址信息
     */
    @GetMapping("/user/findAddressByUser")
    public List<Address> findAddressByUser(String loginName) {
        return addressService.findAddressByUser(loginName);
    }

    /**
     * 查询全部省份
     */
    @GetMapping("/user/findProvinces")
    public List<Provinces> findProvinces() {
        return addressService.getProvinces();
    }

    /**
     * 根据省份ID查询地级市
     */
    @GetMapping("/user/findCitiesByProvinceId")
    public List<Cities> findCitiesByProvinceId(String provinceId) {
        return addressService.getCities(provinceId);
    }

    /**
     * 根据市ID查询区县
     */
    @GetMapping("/user/findAreasByCityId")
    public List<Areas> findAreasByCityId(String cityId) {
        return addressService.getAreas(cityId);
    }

    /**
     * 新增地址
     */
    @PostMapping("/user/saveAddress")
    public boolean save(@RequestBody Address address) {
        try {
            //获取登录用户名
            SecurityContext context = SecurityContextHolder.getContext();
            String loginName = context.getAuthentication().getName();
            //设置用户名
            address.setUserId(loginName);
            addressService.save(address);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
            return false;
    }

    /**
     * 根据ID删除地址
     */
    @GetMapping("/user/deleteAddress")
    public boolean deleteAddress(Long id) {
        try {
            addressService.delete(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改地址信息
     */
    @PostMapping("/user/updateAddress")
    public boolean updateAddress(@RequestBody Address address) {
        try {
            addressService.update(address);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置默认地址
     */
    @GetMapping("/user/setDefaultAddress")
    public boolean setDefaultAddress(Long id,String isDefault) {
        try{
            //获取登录用户名
            SecurityContext context = SecurityContextHolder.getContext();
            String loginName = context.getAuthentication().getName();
            addressService.setDefaultAddress(loginName,id,isDefault);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据省ID查询省
     */
    @GetMapping("/user/findProviceByProviceId")
    public List<Provinces> findProvinceByProvinceId(String proviceId){
      return addressService.findProvinceByProvinceId(proviceId);
    }
}
