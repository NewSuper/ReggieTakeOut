package cn.tqxd.reggie.controller;

import cn.tqxd.reggie.dto.SetMealDto;
import cn.tqxd.reggie.entity.Category;
import cn.tqxd.reggie.entity.SetMeal;
import cn.tqxd.reggie.service.CategoryService;
import cn.tqxd.reggie.service.SetMealService;
import cn.tqxd.reggie.entity.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */

/**
 * 新增套餐交互过程：
 *  1.页面（backend/page/combo/add.html）发ajax请求，请求服务端获取套餐分类并展示到下拉框
 *  2.页面发送ajax 请求，请求服务端获取菜品分类并展示到添加菜品窗口中‘
 *  3.页面发ajax 请求，请求服务端，根据菜品分类查询对应菜品并展示
 *  4.图片上传。下载并回显
 *  5.点击保存，发ajax 。将套餐数据以json形式提交
 */
@RestController
@Slf4j
@RequestMapping(value = {"/setmeal"})
public class SetMealController {

    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     *
     * @param setMealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetMealDto setMealDto) {
        log.info("套餐信息：{}", setMealDto);
        setMealService.saveWithDish(setMealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐管理分页
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<SetMeal> pageInfo = new Page<>(page, pageSize);
        Page<SetMealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, SetMeal::getName, name);
        queryWrapper.orderByDesc(SetMeal::getUpdateTime);
        setMealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<SetMeal> records = pageInfo.getRecords();
        List<SetMealDto> list = records.stream().map((item) -> {
            SetMealDto setMealDto = new SetMealDto();
            BeanUtils.copyProperties(item, setMealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setMealDto.setCategoryName(categoryName);
            }
            return setMealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐。同时需要删除套餐和菜品的关联数据
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long>ids){
        log.info("ids:{}",ids);
        setMealService.removeWithDish(ids);
        return R.success("删除套餐成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setMeal
     * @return
     */
    @GetMapping("/list")
    public R<List<SetMeal>>list(SetMeal setMeal){
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(setMeal.getCategoryId()!= null,SetMeal::getCategoryId,setMeal.getCategoryId());
        queryWrapper.eq(setMeal.getStatus()!= null,SetMeal::getStatus,setMeal.getStatus());
        queryWrapper.orderByDesc(SetMeal::getUpdateTime);
        List<SetMeal> list = setMealService.list(queryWrapper);
        return R.success(list);
    }
}
