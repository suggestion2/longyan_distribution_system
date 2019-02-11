package com.longyan.distribution.constants;

public class CommonConstants {
    //URL
    public static final String CREATE = "/create";
    public static final String LIST = "/list";
    public static final String COUNT = "/count";
    public static final String DETAIL = "/{id}";
    public static final String UPDATE = "/update";
    public static final String DELETE_BY_ID = "/delete/{id}";
    public static final String DELETE = "/delete";
    public static final String VERIFY = "/verify";
    public static final String REJECT = "/reject";
    public static final String CURRENT = "/current";
    public static final String CHECK = "/check";
    public static final String CANCEL = "/cancel";
    public static final String DETAIL_ID = "/detail/{id}";

    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String RESET_PASSWORD = "/password/reset";
    public static final String FORGET_PASSWORD = "/password/forget";

    public static final String REGISTER = "/register";
    public static final String CAPTCHA_REGISTER = "/captcha/register";
    public static final String CAPTCHA_RESETPASSWORD = "/captcha/resetPassword";
    public static final String CHECK_PHONE = "/check/phone";

    public static final String INDEX = "/index";

    //params
    public static final String MD5_SALT = "longyan";

    public static final int CREATE_BY_SERVER = 0;
}
