package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.Dish;
import cn.tqxd.reggie.entity.DishDao;
import cn.tqxd.reggie.entity.DishDto;
import cn.tqxd.reggie.entity.DishFlavor;
import cn.tqxd.reggie.service.DishFlavorService;
import cn.tqxd.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl  extends ServiceImpl<DishDao, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto){
      //保存菜品的基本信息到菜品表
        this.save(dishDto);
        //菜品id
        Long dishDtoId = dishDto.getId();
        //菜品口味
        List<DishFlavor>dishFlavors = dishDto.getFlavors();
        dishFlavors = dishFlavors.stream().map((item)->{
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到表
        dishFlavorService.saveBatch(dishFlavors);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id){
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        DishDto dishDto =new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询菜品信息和对应的口味信息
        LambdaQueryWrapper<DishFlavor>queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor>flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
