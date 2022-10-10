package cn.tqxd.reggie.service;

import cn.tqxd.reggie.dto.SetMealDto;
import cn.tqxd.reggie.entity.SetMeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetMealService extends IService<SetMeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品关联关系
     *
     * @param setMealDto
     */
    void saveWithDish(SetMealDto setMealDto);

    /**
     * 删除套餐。同时需要删除套餐和菜品的关联数据
     *
     * @param ids
     */
    void removeWithDish(List<Long> ids);
}
