package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.User;
import cn.tqxd.reggie.mapper.UserMapper;
import cn.tqxd.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
}
