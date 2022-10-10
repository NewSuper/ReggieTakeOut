package cn.tqxd.reggie.service.impl;

import cn.tqxd.reggie.entity.*;
import cn.tqxd.reggie.exception.CustomException;
import cn.tqxd.reggie.mapper.OrderMapper;
import cn.tqxd.reggie.service.*;
import cn.tqxd.reggie.utils.BaseContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    private ShoppingCartService shoppingCartService;

    private UserService userService;

    private AddressBookService addressBookService;

    private OrderDetailService orderDetailService;

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAddressBookService(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @Autowired
    public void setOrderDetailService(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    @Override
    public void submit(Orders orders) {

        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<ShoppingCart>wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);
        if (shoppingCarts == null || shoppingCarts.size()==0){
            throw new CustomException("购物车为空，不能下单");
        }
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null){
            throw new CustomException("用户地址信息有误，不能下单");
        }
        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail>orderDetails = shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetMealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        //总金额
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        this.save(orders);//向订单表插入一条数据

        orderDetailService.saveBatch(orderDetails); //向订单明细表插入数据

        shoppingCartService.remove(wrapper);//清空购物车数据
    }

}
