package com.cotas.usercenter.service;

import com.cotas.usercenter.common.BaseResponse;
import com.cotas.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author COTAS
* @description 针对表【user】的数据库操作Service
* @createDate 2024-11-13 17:04:31
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 二次校验密码
     * @param inviteCode
     * @return 用户id
     */
   Long userRegister(String userAccount, String userPassword, String checkPassword, String inviteCode);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return  脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 是否为管理员（鉴权函数）
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 将user进行脱敏
     * @param user
     * @return
     */
    User secureUser(User user);

    /**
     * 注销
     * @param request
     */
    void logout(HttpServletRequest request);
}

