package com.longyan.distribution.constants;

import com.sug.core.util.RegexUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class SystemParamsConstants {
    public static final String VIPINVITECOIN = "VIPINVITECOIN ";//VIP邀请获得的钢镚百分比
    public static final String INVITEVIPCOIN = "/create";//合伙人邀请VIP获得的钢镚百分比
    public static final String INVITEVIPRECHARGEGOLDCOIN = "INVITEVIPRECHARGEGOLDCOIN";//合伙人邀请VIP,vip充值金币，合伙人获得的钢镚百分比
    public static final String INVITEVIPRECHARGEOILCOIN = "INVITEVIPRECHARGEOILCOIN";//合伙人邀请VIP,vip充值油钻，合伙人获得的钢镚百分比
    public static final String VIPINVITECOMMONRECHARGEGOLDCOIN = "VIPINVITECOMMONRECHARGEGOLDCOIN";//VIP邀请普通会员,普通会员充值金币，vip获得的钢镚百分比
    public static final String VIPINVITECOMMONRECHARGEOILCOIN = "VIPINVITECOMMONRECHARGEOILCOIN";//VIP邀请普通会员,普通会员充值油钻，vip获得的钢镚百分比
    public static final String COMMONGOLDCHARGE  = "COMMONGOLDCHARGE";//普通会员充值金币的充值折扣
    public static final String VIPREGOLDCHARGE  = "VIPREGOLDCHARGE";//VIP充值金币的充值折扣
    public static final String PARTNEGOLDRRECHARGE = "PARTNEGOLDRRECHARGE";//合伙人充值金币的充值折扣
    public static final String COMMONOILCHARGE  = "COMMONOILCHARGE";//普通会员充值油钻的充值折扣
    public static final String VIPREOILCHARGE  = "VIPREOILCHARGE";//VIP充值油钻的充值折扣
    public static final String PARTNEOILRRECHARGE = "PARTNEOILRRECHARGE";//合伙人充值油钻的充值折扣
    public static final String VIPINVITENUM = "/create";//VIP邀请x人升级为合伙人
    public static final String COINCASH = "/create";//钢镚提现手续费费率
    public static final String BUSINESSCASH = "/create";//商户金币提现手续费费率
    public static final String COINCHANGEGOLD= "/create";//钢镚转金币手续费费率
    public static final String COINCHANGEOIL = "/create";//钢镚转油钻的手续费费率
    public static final String OILPRICE = "/create";//当日油价,展示在前台页面，仅做展示
    public static final String INVITECOMMONBECOMEVIPCOIN = "INVITECOMMONBECOMEVIPCOIN";//合伙人或VIP邀请普通会员,普通会员升级vip，vip获得的钢镚百分比
    public static final String INVITEVIPINVITECOMMONCOIN = "INVITEVIPINVITECOMMONCOIN";//合伙人邀请vip，vip邀请普通会员,普通会员升级vip，合伙人获得的钢蹦百分比



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
