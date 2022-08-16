package cn.tqxd.reggie.service;

import cn.tqxd.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
