package com.cotas.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cotas.usercenter.common.BaseResponse;
import com.cotas.usercenter.common.ErrorCode;
import com.cotas.usercenter.common.Result;
import com.cotas.usercenter.exception.BusinessException;
import com.cotas.usercenter.model.domain.User;
import com.cotas.usercenter.model.domain.request.UserLoginRequest;
import com.cotas.usercenter.model.domain.request.UserRegisterRequest;
import com.cotas.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.cotas.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 注册请求体
     * @return 新用户id
     */
    @PostMapping("register")
    public BaseResponse<Long> UserRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_BLANK,"request请求为空");
        }

        //获取各数据存在变量里
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String inviteCode = userRegisterRequest.getInviteCode();

        //预先检验是否为空，邀请码可以为空
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_BLANK);
        }

        Long l = userService.userRegister(userAccount, userPassword, checkPassword, inviteCode);
        return Result.success(l);

    }

    @PostMapping("login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest,HttpServletRequest request){
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_BLANK,"request请求为空");
        }

        //获取各数据存在变量里
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        //预先检验是否为空
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_BLANK);
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return Result.success(user);
    }

    /**
     * 获取用户当前信息
     * @param request
     * @return
     */
    @GetMapping("current")
    public BaseResponse<User> getCurrent(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) attribute;
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN,"请先登录");
        }
        //注意不能直接返回session中的用户信息，需要从数据库中获取最新的用户信息
        Long id = user.getId();
        User byId = userService.getById(id);
        //找到数据库的用户可能为空，需要在secureUser中判断
        User returnUser = userService.secureUser(byId);
        return Result.success(returnUser);
    }

    @GetMapping("search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){

        //1.鉴权
        if(!userService.isAdmin(request)){
           throw new BusinessException(ErrorCode.NOT_AUTH,"您不是管理员，无访问权限");
        }

        //2.查询数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> list = userService.list(queryWrapper);

        //3.数据脱敏，然后返回
        List<User> collect = list.stream().map(user -> userService.secureUser(user)).collect(Collectors.toList());
        return Result.success(collect);

    }

    @PostMapping("delete")
    public BaseResponse<Boolean> deleteById(@RequestBody Long id, HttpServletRequest request){
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"您输入的id小于0,请重新输入");
        }
        //1.鉴权
        if(!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NOT_AUTH,"您不是管理员，无访问权限");
        }

        //2.删除
        userService.removeById(id);

        //3.返回结果
        return Result.success(true);
    }

    @PostMapping("logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request){
        userService.logout(request);
        return Result.success(true);
    }

    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @return
     */
    @GetMapping("search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (tagNameList == null) {
            throw new BusinessException(ErrorCode.PARAMS_BLANK);
        }
        List<User> users = userService.searchUserByTags(tagNameList);
        return Result.success(users);

    }
}
