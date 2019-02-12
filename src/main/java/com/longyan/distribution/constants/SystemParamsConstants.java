package com.longyan.distribution.constants;

import com.sug.core.util.RegexUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class SystemParamsConstants {
    public static final String VIPINVITECOIN = "VIPINVITECOIN ";//VIP邀请获得的钢镚百分比
    public static final String INVITEVIPCOIN = "/1";//合伙人邀请VIP获得的钢镚百分比
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
    public static final String VIPINVITENUM = "/2";//VIP邀请x人升级为合伙人
    public static final String COINCASH = "COINCASH";//钢镚提现手续费费率
    public static final String BUSINESSGOLDCASH = "BUSINESSGOLDCASH";//商户金币提现手续费费率
    public static final String BUSINESSOILDRILLCASH = "BUSINESSOILDRILLCASH";//商户油钻提现手续费费率
    public static final String COINCHANGEGOLD= "COINCHANGEGOLD";//钢镚转金币手续费费率
    public static final String COINCHANGEOIL = "COINCHANGEOIL";//钢镚转油钻的手续费费率
    public static final String OILPRICE = "/6";//当日油价,展示在前台页面，仅做展示
    public static final String INVITECOMMONBECOMEVIPCOIN = "INVITECOMMONBECOMEVIPCOIN";//合伙人或VIP邀请普通会员,普通会员升级vip，vip获得的钢镚百分比
    public static final String INVITEVIPINVITECOMMONCOIN = "INVITEVIPINVITECOMMONCOIN";//合伙人邀请vip，vip邀请普通会员,普通会员升级vip，合伙人获得的钢蹦百分比
    public static final String VIPCARD = "VIPCARD";//vip会员卡

    public static final String VIPINVITECOIN_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//VIP邀请获得的钢镚百分比
    public static final String INVITEVIPCOIN_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//合伙人邀请VIP获得的钢镚百分比
    public static final String INVITEVIPRECHARGEGOLDCOIN_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//合伙人邀请VIP,vip充值金币，合伙人获得的钢镚百分比
    public static final String INVITEVIPRECHARGEOILCOIN_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//合伙人邀请VIP,vip充值油钻，合伙人获得的钢镚百分比
    public static final String VIPINVITECOMMONRECHARGEGOLDCOIN_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//VIP邀请普通会员,普通会员充值金币，vip获得的钢镚百分比
    public static final String VIPINVITECOMMONRECHARGEOILCOIN_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//VIP邀请普通会员,普通会员充值油钻，vip获得的钢镚百分比
    public static final String COMMONGOLDCHARGE_REGEX  = "^[0-9]+(\\.[0-9]{1,2})?$";//普通会员充值金币的充值折扣
    public static final String VIPREGOLDCHARGE_REGEX  = "^[0-9]+(\\.[0-9]{1,2})?$";//VIP充值金币的充值折扣
    public static final String PARTNEGOLDRRECHARGE_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//合伙人充值金币的充值折扣
    public static final String COMMONOILCHARGE_REGEX  = "^[0-9]+(\\.[0-9]{1,2})?$";//普通会员充值油钻的充值折扣
    public static final String VIPREOILCHARGE_REGEX  = "^[0-9]+(\\.[0-9]{1,2})?$";//VIP充值油钻的充值折扣
    public static final String PARTNEOILRRECHARGE_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//合伙人充值油钻的充值折扣
    public static final String VIPINVITENUM_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//VIP邀请x人升级为合伙人
    public static final String COINCASH_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//钢镚提现手续费费率
    public static final String BUSINESSGOLDCASH_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//商户金币提现手续费费率
    public static final String COINCHANGEGOLD_REGEX= "^[0-9]+(\\.[0-9]{1,2})?$";//钢镚转金币手续费费率
    public static final String COINCHANGEOIL_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//钢镚转油钻的手续费费率
    public static final String OILPRICE_REGEX = "/6";//当日油价,展示在前台页面，仅做展示
    public static final String INVITECOMMONBECOMEVIPCOIN_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//合伙人或VIP邀请普通会员,普通会员升级vip，vip获得的钢镚百分比
    public static final String INVITEVIPINVITECOMMONCOIN_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";//合伙人邀请vip，vip邀请普通会员,普通会员升级vip，合伙人获得的钢蹦百分比
    public static final String VIPCARD_REGEX = "^[0-9]*$";//vip会员卡


    public static boolean matches(String key,String value){
        switch (key){
            case VIPINVITECOIN:
                return Pattern.matches(VIPINVITECOIN_REGEX,value);
            case VIPCARD:
                return Pattern.matches(VIPCARD_REGEX,value);
            case INVITEVIPCOIN:
                return Pattern.matches(INVITEVIPCOIN_REGEX,value);
            case INVITEVIPRECHARGEGOLDCOIN:
                return Pattern.matches(INVITEVIPRECHARGEGOLDCOIN_REGEX,value);
            case INVITEVIPRECHARGEOILCOIN:
                return Pattern.matches(INVITEVIPRECHARGEOILCOIN_REGEX,value);
            case VIPINVITECOMMONRECHARGEGOLDCOIN:
                return Pattern.matches(VIPINVITECOMMONRECHARGEGOLDCOIN_REGEX,value);
            case VIPINVITECOMMONRECHARGEOILCOIN:
                return Pattern.matches(VIPINVITECOMMONRECHARGEOILCOIN_REGEX,value);
            case COMMONGOLDCHARGE :
                return Pattern.matches(COMMONGOLDCHARGE_REGEX,value);
            case VIPREGOLDCHARGE :
                return Pattern.matches(VIPREGOLDCHARGE_REGEX,value);
            case PARTNEGOLDRRECHARGE:
                return Pattern.matches(PARTNEGOLDRRECHARGE_REGEX,value);
            case COMMONOILCHARGE :
                return Pattern.matches(COMMONOILCHARGE_REGEX,value);
            case VIPREOILCHARGE :
                return Pattern.matches(VIPREOILCHARGE_REGEX,value);
            case PARTNEOILRRECHARGE:
                return Pattern.matches(PARTNEOILRRECHARGE_REGEX,value);
            case VIPINVITENUM:
                return Pattern.matches(VIPINVITENUM_REGEX,value);
            case COINCASH:
                return Pattern.matches(COINCASH_REGEX,value);
            case BUSINESSGOLDCASH:
                return Pattern.matches(BUSINESSGOLDCASH_REGEX,value);
            case COINCHANGEGOLD:
                return Pattern.matches(COINCHANGEGOLD_REGEX,value);
            case COINCHANGEOIL:
                return Pattern.matches(COINCHANGEOIL_REGEX,value);
            case OILPRICE:
                return Pattern.matches(OILPRICE_REGEX,value);
            case INVITECOMMONBECOMEVIPCOIN:
                return Pattern.matches(INVITECOMMONBECOMEVIPCOIN_REGEX,value);
            case INVITEVIPINVITECOMMONCOIN:
                return Pattern.matches(INVITEVIPINVITECOMMONCOIN_REGEX,value);
            default:return false;
        }
    }
}
