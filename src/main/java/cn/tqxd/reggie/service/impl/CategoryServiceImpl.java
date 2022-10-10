package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.Category;
import cn.tqxd.reggie.entity.Dish;
import cn.tqxd.reggie.entity.SetMeal;
import cn.tqxd.reggie.exception.CustomException;
import cn.tqxd.reggie.mapper.CategoryMapper;
import cn.tqxd.reggie.service.CategoryService;
import cn.tqxd.reggie.service.DishService;
import cn.tqxd.reggie.service.SetMealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private DishService dishService;
    private SetMealService setMealService;


    @Autowired
    public void setDishService(DishService dishService) {
        this.dishService = dishService;
    }

    @Autowired
    public void setSetMealService(SetMealService setMealService) {
        this.setMealService = setMealService;
    }

    /**
     * 根据id 删除分类，删除之前要判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        //添加查询条件，根据id
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如已关联，抛异常
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联 了套餐，如已关联抛异常
        LambdaQueryWrapper<SetMeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(SetMeal::getCategoryId, id);
        long count2 = setMealService.count();
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //根据id查询
        super.removeById(id);
    }
}
