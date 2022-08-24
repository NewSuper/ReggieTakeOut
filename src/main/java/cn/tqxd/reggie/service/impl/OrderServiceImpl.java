package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.OrderDao;
import cn.tqxd.reggie.entity.Orders;
import cn.tqxd.reggie.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Orders> implements OrderService {

    @Override
    public void submit(Orders orders) {

    }
}
