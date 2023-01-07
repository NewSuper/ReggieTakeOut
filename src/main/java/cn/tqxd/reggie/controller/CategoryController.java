package cn.tqxd.reggie.controller;

import cn.tqxd.reggie.entity.Category;
import cn.tqxd.reggie.service.CategoryService;
import cn.tqxd.reggie.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * 梳理流程：
     * 1.页面（backend/page/category/list.html）发送ajax请求，将新增分类窗口输入数据以json 形式提交后台
     * 2，后台controller 接收数据并调用service 将数据保存
     * 3，service调用mapper 操作数据库保存数据
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);
        return Result.success("新增分类成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Category>> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 根据条件 type 查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 根据id修改分类信息
     *
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        log.info("修改分类信息：{}", category);

        categoryService.updateById(category);

        return Result.success("修改分类信息成功");
    }

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long id) {
        //categoryService.removeById(id);   这个是框架自带的，无法满足我们的场景，在删除前得称判断是否关联了菜品或套餐。有则不许删除
        categoryService.remove(id);
        return Result.success("分类信息删除成功");
    }
}

