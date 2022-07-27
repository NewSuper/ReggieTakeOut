package com.tqxd.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tqxd.reggie.entity.Employee;
import com.tqxd.reggie.service.EmployeeService;
import com.tqxd.reggie.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.将页面明文提交的密码做md5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名查db
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());  //得看数据库有无加唯一索引
        Employee emp = employeeService.getOne(queryWrapper);

        //如果没有查询到则返回失败结果
        if (emp == null) return Result.error("登录失败");
        if (!emp.getPassword().equals(password)) return Result.error("密码错误");
        //3.查看员工状态，是否禁用
        if (emp.getStatus() == 0) return Result.error("账户已禁用");
        request.getSession().setAttribute("employee", employee.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        //清理session中保存的员工id
        request.getSession().removeAttribute("employee");
        return Result.error("退出成功");
    }

    /**
     * 新增员工
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增用户", employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        try {
             employeeService.save(employee);
        }catch (Exception r){
            return Result.success("新增员工失败");
        }
        return Result.success("新增员工成功");
    }
}
