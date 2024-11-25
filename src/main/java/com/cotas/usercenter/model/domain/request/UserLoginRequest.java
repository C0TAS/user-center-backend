package com.cotas.usercenter.model.domain.request;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -4992093902889609772L;

    private String userAccount;

    private String userPassword;

}
