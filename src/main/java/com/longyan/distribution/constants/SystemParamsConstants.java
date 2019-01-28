package com.longyan.distribution.constants;

import com.sug.core.util.RegexUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class SystemParamsConstants {

    public static boolean matches(String key,String value){
        switch (key){
            case "test":
                return Pattern.matches("^[A-Za-z]+$",value);
            /*case "RechargeRate1":
                return Pattern.matches("^(1|0\\.[0-9]?[1-9]|0)$",value);*/
            default:return false;
        }
    }
}
