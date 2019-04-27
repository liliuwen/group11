package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.pojo.Brand;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.OrderItemService")
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemMapper orderItemMapper;





    public PageResult findByPage(OrderItem orderItem, int page, int rows) {
        /*开始分页*/
        PageInfo<Brand> pageInfo = PageHelper.startPage(page, rows)
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        //orderItemMapper.select(orderItem);
                        orderItemMapper.selectAll();
                    }
                });
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void save(OrderItem orderItem) {

    }

    @Override
    public void update(OrderItem orderItem) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public OrderItem findOne(Serializable id) {
        return null;
    }

    @Override
    public List<OrderItem> findAll() {
        // 开始分页
        PageInfo<OrderItem> pageInfo = PageHelper.startPage(1, 10)
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        orderItemMapper.selectAll();
                    }
                });
        System.out.println("总记录数：" + pageInfo.getTotal());
        System.out.println("总页数：" + pageInfo.getPages());
        return pageInfo.getList();
    }


}
