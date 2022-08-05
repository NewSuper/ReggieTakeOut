package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.Category;
import cn.tqxd.reggie.entity.CategoryDao;
import cn.tqxd.reggie.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
}
