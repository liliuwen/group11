package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.service.OrderItemService;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.WeixinPayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderItem")
public class OrderItemController {
    @Reference(timeout = 10000)
    private OrderService orderService;
    @Reference(timeout = 10000)
    private OrderItemService orderItemService;


    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;


    /*分页查询订单*/
    @GetMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows){
        return orderItemService.findByPage(null,page,rows);
    }



    /*查询全部品牌*/
    @GetMapping("/findAll")
    public List<OrderItem> findAll(){
        return orderItemService.findAll();
    }




    /** 生成微信支付二维码 */
    @GetMapping("/genPayCode")
    public Map<String, Object> genPayCode(String orderId){
        /** 创建分布式id生成器 */
        OrderItem orderItem = orderItemService.findOne(orderId);
        String totalFee = String.valueOf(orderItem.getTotalFee());

        /** 调用生成微信支付二维码服务方法 */
        return weixinPayService.genPayCode(orderId,totalFee);
    }

}
