package com.cotas.usercenter.service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.cotas.usercenter.mapper.UserMapper;
import com.cotas.usercenter.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserserviceTest {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Test
    public void insertTest(){
        User user = new User();
        user.setUserName("xiaoming");
        user.setUserAccount("123456");
        user.setPassWord("123456");
        user.setGender(1);
        user.setPhone("123456");
        user.setEmail("123456");
        user.setAvatarUrl("123456");
        user.setStatus(0);
        user.setUserRole(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);

        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    /*@Test
    void userRegister() {
       String userAccount = "xiao";
       String userPassword = "123456";
       String checkUserPassword = "123456";
       String inviteCode = "123456";
       Long result = userService.userRegister(userAccount, userPassword, checkUserPassword, inviteCode);
        Assertions.assertNull(result);

        userAccount = "xiaohong";
        userPassword = "123";
        result = userService.userRegister(userAccount, userPassword, checkUserPassword, inviteCode);
        Assertions.assertNull(result);

        userAccount = "xiao hong";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkUserPassword, inviteCode);
        Assertions.assertNull(result);

        userAccount = "xiaoming";
        result = userService.userRegister(userAccount, userPassword, checkUserPassword, inviteCode);
        Assertions.assertNull(result);

        userAccount = "";
        result = userService.userRegister(userAccount, userPassword, checkUserPassword, inviteCode);
        Assertions.assertNull(result);

        userAccount = "xiaohong";
        userPassword = "123456";
        checkUserPassword = "1234567";
        result = userService.userRegister(userAccount, userPassword, checkUserPassword, inviteCode);
        Assertions.assertNull(result);

       userAccount = "xiaoming";
       userPassword = "123456";
       checkUserPassword = "123456";
        inviteCode = "1234567";
        result = userService.userRegister(userAccount, userPassword, checkUserPassword, inviteCode);
        Assertions.assertNull(result);

        System.out.println("ok");

    }*/

    @Test
    public void tagTest(){
        List<String> list = Arrays.asList("cpp", "java");
        List<User> users = userService.searchUserByTags(list);
        System.out.println("users = " + users);
        Assertions.assertNotNull(users);
    }
}
