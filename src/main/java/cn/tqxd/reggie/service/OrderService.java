package cn.tqxd.reggie.service;

import cn.tqxd.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {

    //用户下单
    void submit(Orders orders);
}
