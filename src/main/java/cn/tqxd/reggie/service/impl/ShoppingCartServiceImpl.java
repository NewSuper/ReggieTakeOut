package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.ShoppingCart;
import cn.tqxd.reggie.mapper.ShoppingCartMapper;
import cn.tqxd.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
