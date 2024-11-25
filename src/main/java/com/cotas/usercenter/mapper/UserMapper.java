package com.cotas.usercenter.mapper;

import com.cotas.usercenter.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import javax.xml.ws.soap.MTOM;

/**
* @author COTAS
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-11-13 17:04:31
* @Entity generator.domain.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




