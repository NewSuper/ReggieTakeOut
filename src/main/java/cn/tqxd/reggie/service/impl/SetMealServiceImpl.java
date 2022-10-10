package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.dto.SetMealDto;
import cn.tqxd.reggie.entity.SetMeal;
import cn.tqxd.reggie.entity.SetMealDish;
import cn.tqxd.reggie.exception.CustomException;
import cn.tqxd.reggie.mapper.SetMealMapper;
import cn.tqxd.reggie.service.SetMealDishService;
import cn.tqxd.reggie.service.SetMealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements SetMealService {
    private SetMealDishService setMealDishService;

    @Autowired
    public void setSetMealDishService(SetMealDishService setMealDishService) {
        this.setMealDishService = setMealDishService;
    }

    /**
     * 新增套餐，同时需要保存套餐和菜品关联关系
     *
     * @param setMealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetMealDto setMealDto) {
        //保存套餐的基本信息.操作setmeal,执行insert
        this.save(setMealDto);

        List<SetMealDish> setMealDishes = setMealDto.getSetMealDishes();
        setMealDishes.stream().map((item) -> {
            item.setSetmealId(setMealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息，操作setmeal_dish 执行insert
        setMealDishService.saveBatch(setMealDishes);
    }

    /**
     * 删除套餐。同时需要删除套餐和菜品的关联数据
     *
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in(1,2,3) and status =1
        //查询套餐状态，确定是否可删除
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetMeal::getId, ids);
        queryWrapper.eq(SetMeal::getStatus, 1);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //  如果可删除，先删除套餐表中的数据 setmeal
        this.removeByIds(ids);
        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetMealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetMealDish::getSetmealId, ids);
        //删除关系表中的数据--setmeal_dish
        setMealDishService.remove(lambdaQueryWrapper);
    }
}
