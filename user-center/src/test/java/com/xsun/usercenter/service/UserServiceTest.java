package com.xsun.usercenter.service;

import com.xsun.usercenter.pojo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * 用户服务测试
 */
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void testAddUser(){

        User user = new User();

        user.setUserAccount("2818");
        user.setUsername("testxsun");
        user.setAvatarUrl("1.jpg");
        user.setGender(0);
        user.setUserPassword("1234");
        user.setPhone("1893564");
        user.setEmail("2135@qq.com");


        boolean result = userService.save(user);

        System.out.println(user.getId());

        Assertions.assertTrue(result);

    }


    @Test
    void testUserRegister() {
        String userAccount = "xsun";
        String userPassword = "";
        String checkPassword = "120xswxsw";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "xs";
        userPassword = "120xswxsw";
        checkPassword = "120xswxsw";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "xsun";
        userPassword = "12011";
        checkPassword = "12011";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "x sun";
        userPassword = "120xswxsw";
        checkPassword = "120xswxsw";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "xsun";
        userPassword = "120xswxsw";
        checkPassword = "120xswxsw1";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "xsun02";
        userPassword = "120xswxsw";
        checkPassword = "120xswxsw";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
        //Assertions.assertEquals(-1,result);

    }


}
