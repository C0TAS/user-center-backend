package com.cotas.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cotas.usercenter.common.ErrorCode;
import com.cotas.usercenter.exception.BusinessException;
import com.cotas.usercenter.model.domain.User;
import com.cotas.usercenter.service.UserService;
import com.cotas.usercenter.mapper.UserMapper;
import com.cotas.usercenter.utils.MD5Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.cotas.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.cotas.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author COTAS
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-13 17:04:31
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;


    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword, String inviteCode) {
        //1.校验
        //非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_BLANK);
        }

        //长度,邀请码为6位或为空
        if(userAccount.length() < 6 || userPassword.length() < 8
                || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(inviteCode != null && !inviteCode.isEmpty() &&  inviteCode.length() != 6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"邀请码格式错误");
        }

        //不能有特殊字符，正则表达式
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能有特殊字符");
        }

        //密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致");
        }

        //账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已被注册，请更换账号");
        }

        //2.加密
        String MD5userPassword = MD5Utils.string2MD5(userPassword);

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassWord(MD5userPassword);
        user.setInviteCode(inviteCode);
        //设置默认头像
        user.setAvatarUrl("https://pic.imgdb.cn/item/67678eebd0e0a243d4e7efb9.png");
        //设置默认用户名是账号
        user.setUserName(userAccount);
        int insert = userMapper.insert(user);

        //4.返回新用户id
        Long id = user.getId();
        return id;
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        //非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_BLANK);
        }

        //长度
        if(userAccount.length() < 6 || userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //不能有特殊字符，正则表达式
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能有特殊字符");
        }

        //2.将密码转换为加密密码
        String MD5userPassword = MD5Utils.string2MD5(userPassword);

        //3.查询数据库中是否有该用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount)
                .eq("PassWord",MD5userPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user login failed. userAccount or userPassWord is unavailable");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名不存在或密码错误");
        }

        //4.将用户信息脱敏
        User returnUser = secureUser(user);

        //5.用session存储用户数据
        request.getSession().setAttribute(USER_LOGIN_STATE,returnUser);

        //6.返回脱敏后的用户信息
        return returnUser;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object object = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) object;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    @Override
    public User secureUser(User user) {
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_BLANK);
        }
        User returnUser = new User();
        returnUser.setId(user.getId());
        returnUser.setUserName(user.getUserName());
        returnUser.setUserAccount(user.getUserAccount());
        returnUser.setGender(user.getGender());
        returnUser.setPhone(user.getPhone());
        returnUser.setEmail(user.getEmail());
        returnUser.setAvatarUrl(user.getAvatarUrl());
        returnUser.setStatus(user.getStatus());
        returnUser.setCreateTime(user.getCreateTime());
        returnUser.setUpdateTime(user.getUpdateTime());
        //添加用户权限后的字段后，不要忘记设置这个
        returnUser.setUserRole(user.getUserRole());
        returnUser.setInviteCode(user.getInviteCode());
        returnUser.setTags(user.getTags());
        return returnUser;
    }

    @Override
    public void logout(HttpServletRequest request) {
        if(request == null){
            return;
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }


    @Deprecated
    public List<User> searchUserByTagsBySQL(List<String> tagNameList) {
        //判空
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.拼接方式查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //循环在查询条件加标签，注意用like的方式加
        //tag是数据库字段名
        for (String tagName : tagNameList) {
            queryWrapper.like("tags",tagName);
        }
        //查询
        List<User> users = userMapper.selectList(queryWrapper);
        //将用户信息脱敏后返回
        return  users.stream().map(this::secureUser).collect(Collectors.toList());
        /*
        等于
        List<User> secureUsers = new ArrayList<>();
        for (User user : users) {
            User secureUser = this.secureUser(user);
            secureUsers.add(secureUser);
           }
         return secureUsers;

         */
    }

    @Override
    public List<User> searchUserByTags(List<String> tagNameList) {
        //判空
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.取出所有用户，再在内存中判断
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);

        //Gson 进行序列化和反序列化
        Gson gson = new Gson();
        return users.stream().filter(user->{
            String userTag = user.getTags();
//            if(StringUtils.isBlank(userTag)){
//                return false;
//            }
            Set<String> tagNameSet = gson.fromJson(userTag, new TypeToken<Set<String>>(){}.getType());
            //java8  Optional 来判断空
            tagNameSet = Optional.ofNullable(tagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if(!tagNameSet.contains(tagName)){
                    return false;
                }
            }
            return true;
        }).map(this::secureUser).collect(Collectors.toList());
    }

}




