package cn.tqxd.reggie.dto;

import cn.tqxd.reggie.entity.Dish;
import cn.tqxd.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装页面提交的数据。实现dish表所有
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
