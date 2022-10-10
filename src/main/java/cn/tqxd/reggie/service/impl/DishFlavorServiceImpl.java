package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.DishFlavor;
import cn.tqxd.reggie.mapper.DishFlavorMapper;
import cn.tqxd.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>implements DishFlavorService {
}
