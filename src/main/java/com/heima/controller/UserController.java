package com.heima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.R;
import com.heima.entity.User;
import com.heima.service.UserService;
import com.heima.utils.SMSUtils;
import com.heima.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /*
     * 发送手机短信验证码
     * */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code;{}", code);
            //调用阿里云提供的短信服务api发送短信
//            SMSUtils.sendMessage("瑞吉外卖", "瑞吉外卖", phone, code);
            //需要将生成的验证码保存到session
            session.setAttribute(phone, code);
            return R.success("手机验证码短信发送成功");
        }

        return R.error("短信发送失败");
    }

    /*
     * 移动端用户登录
     * */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码的比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功,说明登录成功

            //判断手机对应的用户是否为新用户,如果是新用户则自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断手机对应的用户是否为新用户,如果是新用户则自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }

        return R.error("登录失败");
    }
}
