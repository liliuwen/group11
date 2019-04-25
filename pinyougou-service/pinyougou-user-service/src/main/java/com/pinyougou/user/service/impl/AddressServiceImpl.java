package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.AddressMapper;
import com.pinyougou.mapper.AreasMapper;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 地址服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-20<p>
 */
@Service(interfaceName = "com.pinyougou.service.AddressService")
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private ProvincesMapper provincesMapper;
    @Autowired
    private CitiesMapper citiesMapper;
    @Autowired
    private AreasMapper areasMapper;

    /**
     * 添加地址
     * @param address
     */
    @Override
    public void save(Address address) {
        try {
            //设置为非默认地址
            address.setIsDefault("0");
            //设置地址创建时间
            address.setCreateDate(new Date());
            addressMapper.insert(address);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Address address) {
        try {
            addressMapper.updateByPrimaryKeySelective(address);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据主键ID删除地址
     */
    @Override
    public void delete(Serializable id) {
        try{
            addressMapper.deleteByPrimaryKey(id);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Address findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Address> findAll() {
        return null;
    }

    @Override
    public List<Address> findByPage(Address address, int page, int rows) {
        return null;
    }

    /**
     * 查询用户地址信息
     * @param userId
     * @return
     */
    @Override
    public List<Address> findAddressByUser(String userId) {
        try{
            // SELECT * FROM tb_address WHERE user_id = 'itcast' ORDER BY is_default DESC
            // 创建示范对象
            Example example = new Example(Address.class);
            // 创建条件对象
            Example.Criteria criteria = example.createCriteria();
            // user_id = 'itcast'
            criteria.andEqualTo("userId", userId);
            // ORDER BY is_default DESC
            example.orderBy("isDefault").desc();
            // 条件查询
            return addressMapper.selectByExample(example);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Provinces> getProvinces() {
        try {
            return provincesMapper.selectAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据省份ID查询市
     */
    @Override
    public List<Cities> getCities(String provinceId) {
        try {
           Cities cities = new Cities();
           cities.setProvinceId(provinceId);
           return citiesMapper.select(cities);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Areas> getAreas(String cityId) {
        try{
          Areas areas = new Areas();
          areas.setCityId(cityId);
          return areasMapper.select(areas);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
