package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.AreasMapper;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProvincesMapper provincesMapper;
    @Autowired
    private CitiesMapper citiesMapper;
    @Autowired
    private AreasMapper areasMapper;

    @Override
    public void save(User user) {
        try{
            // 密码加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            // 创建时间
            user.setCreated(new Date());
            // 修改时间
            user.setUpdated(user.getCreated());
            // 添加数据
            userMapper.insertSelective(user);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(User user) {
        try{
            // 密码加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            // 创建时间
            user.setCreated(new Date());
            // 修改时间
            user.setUpdated(user.getCreated());


            Example example =new Example(User.class);

            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("username",user.getUsername());

            // 添加数据
            int i = userMapper.updateByExampleSelective(user, example);

        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public User findOne(Serializable id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findByPage(User user, int page, int rows) {
        return null;
    }

    /** 发送短信验证码 */
    public boolean sendSmsCode(String phone){
        try{
            // 1. 随机生成6位数字的验证码 95db9eb9-94e8-48e7-a5b2-97c622644e70
            String code = UUID.randomUUID().toString().replaceAll("-", "")
                    .replaceAll("[a-zA-Z]", "").substring(0,6);
            System.out.println("code= " + code);


            // 2. 调用短信发送接口(HttpClientUtils)
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            // 定义Map集合封装请求参数 18502903967
            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            params.put("signName", signName);
            params.put("templateCode", templateCode);
            params.put("templateParam", "{'number' : '"+ code +"'}");
            // 发送post请求
            String content = httpClientUtils.sendPost(smsUrl, params);
            System.out.println("content = " + content);

            // 3. 判断短信是否发送成功，如果发送成功，就需要把验证存储到Redis(时间90秒)
            // {success : true}
            Map map = JSON.parseObject(content, Map.class);
            boolean success = (boolean)map.get("success");
            if (success){
                // 把验证存储到Redis(时间90秒)
                redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            }

            return success;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 检验验证码是否正确 */
    public boolean checkSmsCode(String phone, String code){
        try{
            String oldCode = (String)redisTemplate.boundValueOps(phone).get();
            return code.equals(oldCode);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public User findUserByUsername(String username) {
        try {
            User user = new User();
            user.setUsername(username);
            return userMapper.selectOne(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 获取省份选项*/
    @Override
    public List<Map<String,String>> findAllProvince() {
        try {
            List<Provinces> provinces = provincesMapper.selectAll();
            List<Map<String,String>> data = new ArrayList<>();
            for (Provinces province : provinces) {
                Map<String,String> map = new HashMap<>();
                map.put("id",province.getProvinceId().toString());
                map.put("name",province.getProvince());
                data.add(map);
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**  根据省份查询城市*/
    @Override
    public List<Map<String, String>> findCityByProvince(String provinceId) {
        try {
            Cities cities = new Cities();
            cities.setProvinceId(provinceId);
            List<Cities> citiesList = citiesMapper.select(cities);

            List<Map<String,String>> data = new ArrayList<>();
            for (Cities city: citiesList) {
                Map<String,String> map = new HashMap<>();
                map.put("id",city.getCityId().toString());
                map.put("name",city.getCity());
                data.add(map);
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**  根据城市查询区域*/
    @Override
    public List<Map<String, String>> findAreaByCity(String areaId) {
        try {
            Areas areas = new Areas();
            areas.setCityId(areaId);
            List<Areas> areaList = areasMapper.select(areas);

            List<Map<String,String>> data = new ArrayList<>();
            for (Areas area: areaList) {
                Map<String,String> map = new HashMap<>();
                map.put("id",area.getAreaId().toString());
                map.put("name",area.getArea());
                data.add(map);
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**  修改内容 */
    @Override
    public void updateByUserInfo(User user) {
        try {
            if (user.getId() == null) {
                throw new RuntimeException("没有找到ID的值");
            }
            int count = userMapper.updateByPrimaryKeySelective(user);
            if (count>1) {
                throw new RuntimeException("修改行数超过1,操作影响行数为"+count);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("修改异常!");
        }
    }

}
