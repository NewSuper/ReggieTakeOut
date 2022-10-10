package cn.tqxd.reggie.service;

import cn.tqxd.reggie.dto.DishDto;
import cn.tqxd.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作2张表 dish dish_flavor
    void saveWithFlavor(DishDto dishDto);
    //根据id 查询菜品信息和对应的口味信息
    DishDto getByIdWithFlavor(Long id);
    //更新菜品信息，同时更新对应的口味信息
    void updateWithFlavor(DishDto dishDto);
}
