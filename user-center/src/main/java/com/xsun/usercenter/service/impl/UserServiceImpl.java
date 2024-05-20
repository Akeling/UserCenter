package com.xsun.usercenter.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsun.usercenter.common.ErrorCode;
import com.xsun.usercenter.exception.BusinessException;
import com.xsun.usercenter.pojo.User;
import com.xsun.usercenter.service.UserService;
import com.xsun.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.xsun.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author sun
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-03-31 20:42:17
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值,混淆密码(加密)
     */

    private final static String SALT = "xsun";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        if (userAccount == null || userPassword == null || checkPassword == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8 || !userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不一致");
        }

        System.out.println("用户账号和密码长度符合要求");
        // 校验用户不包含特殊字符 [a-zA-Z0-9_]{4,16}
        String checkAccount = "[a-zA-Z0-9_]{4,16}";
        boolean flag = userAccount.matches(checkAccount);
        if (!flag) {
            System.out.println("用户账号包含特殊字符");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号包含特殊字符");
        }

        System.out.println("账户书写规范");
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            System.out.println("账户已存在");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }

        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"保存用户失败");
        }

        System.out.println("注册成功");

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        /*if (userAccount == null || userPassword == null) {
            return null;
        }*/
        if(StringUtils.isAnyBlank(userAccount, userPassword))
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }

        // 校验用户不包含特殊字符 [a-zA-Z0-9_]{4,16}
        String checkAccount = "[a-zA-Z0-9_]{4,16}";
        boolean flag = userAccount.matches(checkAccount);
        if (!flag) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        }

        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        System.out.println("用户账号：" + userAccount);
        // 用户不存在
        if (user == null) {
            System.out.println("用户名或密码错误");
            log.info("user login failed, userAccount can not match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码错误");
        }

        //用户信息脱敏
        User safetyUser = getSafetyUser(user);

        // 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        System.out.println("登录成功");
        return safetyUser;
    }

    /**
     * 用户信息脱敏
     * @param user
     * @return
     */
    @Override
    public User getSafetyUser(User user){
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"需脱敏的用户未传入");
        }
        // 用户脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);

        return 1;
    }
}




