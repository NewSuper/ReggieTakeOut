package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.OrderDetail;
import cn.tqxd.reggie.entity.OrderDetailDao;
import cn.tqxd.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {

}
