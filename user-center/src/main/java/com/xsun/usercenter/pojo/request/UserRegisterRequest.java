package com.xsun.usercenter.pojo.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2837960155369042281L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
