package cn.tqxd.reggie.controller;

import cn.tqxd.reggie.entity.AddressBook;
import cn.tqxd.reggie.service.AddressBookService;
import cn.tqxd.reggie.utils.BaseContext;
import cn.tqxd.reggie.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/addressBook")
public class AddressBookController {

    private AddressBookService addressBookService;

    @Autowired
    public void setAddressBookService(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    public Result<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 查询默认地址
     *
     * @return
     */
    @GetMapping(value = {"default"})
    public Result<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getById(queryWrapper);
        if (null == addressBook) {
            return Result.error("没有查询到地址");
        } else {
            return Result.success(addressBook);
        }
    }

    /**
     * 查询用户的全部地址
     *
     * @param addressBook
     * @return
     */
    @GetMapping(value = {"/list"})
    public Result<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        return Result.success(addressBookService.list(queryWrapper));
    }

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @GetMapping(value = {"/{id}"})
    public Result get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return Result.success(addressBook);
        } else {
            return Result.error("没有查询到地址");
        }
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping(value = {"default"})
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(queryWrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }
}
