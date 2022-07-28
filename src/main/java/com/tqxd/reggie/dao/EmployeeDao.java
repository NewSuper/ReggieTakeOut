package com.tqxd.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tqxd.reggie.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeDao extends BaseMapper<Employee> {
}
