package com.tqxd.reggie.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tqxd.reggie.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl  extends ServiceImpl<EmployeeService, Employee> implements EmployeeService {
}
