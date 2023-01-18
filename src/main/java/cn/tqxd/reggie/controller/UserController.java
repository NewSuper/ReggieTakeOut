package cn.tqxd.reggie.controller;

import cn.tqxd.reggie.entity.R;
import cn.tqxd.reggie.entity.User;
import cn.tqxd.reggie.service.UserService;
import cn.tqxd.reggie.utils.SMSUtils;
import cn.tqxd.reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CacheManager cacheManager;

    /**
     * 移动端 短信验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            //调用阿里云提供的短信服务
            // SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            //需要将生成的验证码保存到session
            session.setAttribute(phone, code);
            //将生成的验证码缓存到redis中，有效期5min
           // redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        //从session中获取验证码
        Object codeInSession = session.getAttribute(phone);
        //从redis 中获取缓存的验证码
        //Object codeInSession = redisTemplate.opsForValue().get(phone);

        if (codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            //如果用户登录成功。删除redis 中的验证码
           // redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
