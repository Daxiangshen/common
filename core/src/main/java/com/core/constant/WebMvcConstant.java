package com.core.constant;

/**
 * MVC web常量
 */
public class WebMvcConstant {
    private WebMvcConstant() {
    }

    // token 用户id键
    public static final String LOGIN_USER_TOKEN_KEY = "login_user_token";
    //  验证码键
    public static final String VERIFY_CODE_KEY = "verifyCode";
    //  密码输入错误次数
    public static final String VERIFY_PASSWORD_KEY = "passwordTime";
    //请求header 鉴权token的KEY
    public static final String HEADER_TOKEN = "Authorization";
}