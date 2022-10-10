package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.Employee;
import cn.tqxd.reggie.mapper.EmployeeMapper;
import cn.tqxd.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>implements EmployeeService {
}
