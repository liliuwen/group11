package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商家服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-01<p>
 */
@Service(interfaceName = "com.pinyougou.service.SellerService")
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public void save(Seller seller) {
        try {
            // 审核状态：未审核
            seller.setStatus("0");
            // 创建时间
            seller.setCreateTime(new Date());
            sellerMapper.insertSelective(seller);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(Seller seller) {
        try {
            sellerMapper.updateByPrimaryKeySelective(seller);
        } catch (Exception ex) {
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
    public Seller findOne(Serializable id) {
        return sellerMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Seller> findAll(Seller seller) {
        try {
            Example example = new Example(Seller.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name","%"+(seller.getName()==null?"":seller.getName())+"%");
            criteria.andLike("nickName","%"+(seller.getNickName()==null?"":seller.getNickName())+"%");
            if (!seller.getStatus().equals("-1")) {
                criteria.andEqualTo("status",seller.getStatus());
            }
            return sellerMapper.selectByExample(example);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult findByPage(Seller seller, int page, int rows) {
        try {
            // 开始分页
            PageInfo<Seller> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                        @Override
                        public void doSelect() {
                            sellerMapper.findAll(seller);
                        }
                    });
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 商家审核
     */
    public void updateStatus(String sellerId, String status) {
        try {
            sellerMapper.updateStatus(sellerId, status);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //查询原来的密码
    @Override
    public String findOldPassword(String sellerId) {
        try {
          String password = sellerMapper.findOldPassword(sellerId);
          return password;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


    //修改密码
    @Override
    public void updatePassword(String sellerId, String newPassword) {
        try {
            sellerMapper.updatePassword(sellerId,newPassword);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


}
