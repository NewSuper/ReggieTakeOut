package cn.tqxd.reggie.dto;

import cn.tqxd.reggie.entity.SetMeal;
import cn.tqxd.reggie.entity.SetMealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetMealDto extends SetMeal {

    private List<SetMealDish> setMealDishes;

    private String categoryName;
}

