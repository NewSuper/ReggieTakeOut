package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.AddressBook;
import cn.tqxd.reggie.mapper.AddressBookMapper;
import cn.tqxd.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
