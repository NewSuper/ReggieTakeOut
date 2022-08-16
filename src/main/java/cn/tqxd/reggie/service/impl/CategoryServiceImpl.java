package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.Category;
import cn.tqxd.reggie.entity.CategoryDao;
import cn.tqxd.reggie.entity.Dish;
import cn.tqxd.reggie.entity.SetMeal;
import cn.tqxd.reggie.service.CategoryService;
import cn.tqxd.reggie.service.DishService;
import cn.tqxd.reggie.service.SetMealService;
import cn.tqxd.reggie.utils.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    DishService dishService;

    @Autowired
    SetMealService setMealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如已关联，抛异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如已关联，抛异常
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(SetMeal::getCategoryId, id);
        long count2 = setMealService.count(setMealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
