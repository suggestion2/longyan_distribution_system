<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>龙岩</title>
    <link href="/resources/images/favicon.ico" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="/resources/css/jquery-ui.css">
    <link rel="stylesheet" href="/resources/css/style.css">
    <link rel="stylesheet" href="/resources/css/tab.css">
    <link rel="stylesheet" href="/resources/js/mask/mask.css">
    <link rel="stylesheet" href="/resources/js/JsonParser/JsonParser.css">
</head>
<body>
<div id="tabs" class="ui-widget-content">
    <div class="tab">
        <button class="tablinks" onclick="openTab(1)">api</button>
        <button class="tablinks" onclick="openTab(2)">management</button>
    </div>
    <div id="tabs-1" style="width: 2000px;display: none">
        <div style="float: left; width: 400px;">
            <h2>用户(customer)</h2>
            <div>
                <strong>[注册]</strong><br/>
                真实姓名:<input class="textbox" type="text" id="customer-create-realName" style="width: 100px;"
                            value="王"/><br/>
                手机号码:<input class="textbox" type="text" id="customer-create-phone" style="width: 100px;"
                            value="15059592132"/><br/>
                登录密码:<input class="textbox" type="text" id="customer-create-loginPassword" style="width: 100px;"
                            value="123456"/><br/>
                支付密码:<input class="textbox" type="text" id="customer-create-paymentPassword" style="width: 100px;"
                            value="123456"/><br/>
                银行名称:<input class="textbox" type="text" id="customer-create-customerBank" style="width: 100px;"
                            value="建行"/><br/>
                银行卡号:<input class="textbox" type="text" id="customer-create-customerBankAccount" style="width: 100px;"
                            value="6222600260001072444"/><br/>
                邀请id:<input class="textbox" type="text" id="customer-create-parentId" style="width: 100px;"
                            value=""/><br/>
                <input type="button" value="注册" onclick="customerModule.create()"/><br>
            </div>
            <div>
                <strong>[登录]</strong><br/>
                手机:<input class="textbox" type="text" id="customer-a-login-phone" style="width: 100px;"
                          value="15059592132"/>
                密码:<input class="textbox" type="text" id="customer-a-login-loginPassword" style="width: 100px;"
                          value="123456"/>
                <input type="button" value="登录" onclick="customerModule.login()"/><br>
            </div>
            <div>
                <strong>[修改登录密码]</strong><br/>
                旧密码:<input class="textbox" type="text" id="customer-resetLogPwd-a-originPwd" style="width: 100px;"
                           value="123456"/>
                新密码:<input class="textbox" type="text" id="customer-resetLogPwd-a-newPwd" style="width: 100px;"
                           value="123456"/>
                <input type="button" value="修改" onclick="customerModule.resetLogPwd()"/><br>
            </div>
            <div>
                <strong>[当前用户]</strong><br/>
                <input type="button" value="当前用户" onclick="customerModule.current()"/><br>
            </div>
            <div>
                <strong>[修改支付密码]</strong><br/>
                旧密码:<input class="textbox" type="text" id="customer-resetPayPwd-a-originPwd" style="width: 100px;"
                           value="123456"/>
                新密码:<input class="textbox" type="text" id="customer-resetPayPwd-a-newPwd" style="width: 100px;"
                           value="123456"/>
                <input type="button" value="修改" onclick="customerModule.resetPayPwd()"/><br>
            </div>
            <div>
                <strong>[登出]</strong><br/>
                <input type="button" value="登出" onclick="customerModule.logout()"/><br>
            </div>
            <div>
                <strong>[修改]</strong><br/>
                真实姓名:<input class="textbox" type="text" id="customer-a-update-realName" style="width: 100px;"
                            value="真实姓名"/><br>
                银行:<input class="textbox" type="text" id="customer-a-update-customerBank" style="width: 100px;"
                          value="农行"/><br>
                银行账号:<input class="textbox" type="text" id="customer-a-update-customerBankAccount" style="width: 100px;"
                            value="6222600260001072442"/><br>
                收货地址:<input class="textbox" type="text" id="customer-a-update-address" style="width: 100px;"
                            value="收货地址"/><br>
                <input type="button" value="修改" onclick="customerModule.update()"/><br>
            </div>
            <div>
                <strong>[商户列表]</strong><br/>
                商户账户或商户名称:<input class="textbox" type="text" id="customer-a-businessList-content" style="width: 100px;"
                                 value=""/>
                startIndex:<input class="textbox" type="text" id="customer-a-businessList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-a-businessList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerModule.businessList()"/><br>
            </div>
            <div>
                <strong>[商户详情]</strong><br/>
                id:<input class="textbox" type="text" id="customer-a-businessDetail-id" style="width: 100px;"
                                 value=""/>
                <input type="button" value="列表" onclick="customerModule.businessDetail()"/><br>
            </div>
            <div>
                <strong>[邀请列表]</strong><br/>
                startIndex:<input class="textbox" type="text" id="customer-a-inviteList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-a-inviteList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerModule.inviteList()"/><br>
            </div>
            <div>
                <strong>[申请成为商户]</strong><br/>
                businessName:<input class="textbox" type="text" id="customer-a-businessApplication-businessName"
                                    style="width: 100px;"
                                    value="商户"/>
                <input type="button" value="列表" onclick="customerModule.businessApplication()"/><br>
            </div>
            <div>
                <strong>[生成邀请链接]</strong><br/>
                <input type="button" value="列表" onclick="customerModule.inviteCode()"/><br>
            </div>
            <div>
                <strong>[兑换金币]</strong><br/>
                钢蹦数amount:<input class="textbox" type="text" id="customer-a-exchangeGold-amount"
                                    style="width: 100px;" value="100"/>
                支付密码:<input class="textbox" type="text" id="customer-a-exchangeGold-paymentPassword"
                                    style="width: 100px;" value="100"/>
                <input type="button" value="列表" onclick="customerModule.exchangeGold()"/><br>
            </div>
            <div>
                <strong>[兑换油钻]</strong><br/>
                钢蹦数amount:<input class="textbox" type="text" id="customer-a-exchangeOilDrill-amount"
                                    style="width: 100px;" value="100"/>
                支付密码:<input class="textbox" type="text" id="customer-a-exchangeOilDrill-paymentPassword"
                                    style="width: 100px;" value="100"/>
                <input type="button" value="列表" onclick="customerModule.exchangeOilDrill()"/><br>
            </div>
            <div>
                <strong>[钢蹦提现]</strong><br/>
                amount:<input class="textbox" type="text" id="customer-a-coinWithdraw-amount"
                              style="width: 100px;"
                              value=""/>
                paymentPassword:<input class="textbox" type="text" id="customer-a-coinWithdraw-paymentPassword" style="width: 100px;"
                                       value=""/><br>
                <input type="button" value="提现" onclick="customerModule.coinWithdraw()"/><br>
            </div>
            <div>
                <strong>[钢蹦收支记录]</strong><br/>
                startIndex:<input class="textbox" type="text" id="customer-a-coinRecordList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-a-coinRecordList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerModule.coinRecordList()"/><br>
            </div>
            <div>
                <strong>[金币收支记录]</strong><br/>
                startIndex:<input class="textbox" type="text" id="customer-a-goldRecordList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-a-goldRecordList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerModule.goldRecordList()"/><br>
            </div>
            <div>
                <strong>[金币转账]</strong><br/>
                businessId:<input class="textbox" type="text" id="customer-a-goldTransfer-customerId"
                                  style="width: 100px;"
                                  value=""/><br>
                amount:<input class="textbox" type="text" id="customer-a-goldTransfer-amount"
                              style="width: 100px;"
                              value=""/>
                paymentPassword:<input class="textbox" type="text" id="customer-a-goldTransfer-paymentPassword" style="width: 100px;"
                                       value=""/><br>
                <input type="button" value="转账" onclick="customerModule.goldTransfer()"/><br>
            </div>
            <div>
                <strong>[油钻收支记录]</strong><br/>
                startIndex:<input class="textbox" type="text" id="customer-a-oilDrillRecordList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-a-oilDrillRecordList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerModule.oilDrillRecordList()"/><br>
            </div>
            <div>
                <strong>[油钻转账]</strong><br/>
                businessId:<input class="textbox" type="text" id="customer-a-oilDrillTransfer-customerId"
                                  style="width: 100px;"
                                  value=""/><br>
                amount:<input class="textbox" type="text" id="customer-a-oilDrillTransfer-amount"
                              style="width: 100px;"
                              value=""/>
                paymentPassword:<input class="textbox" type="text" id="customer-a-oilDrillTransfer-paymentPassword" style="width: 100px;"
                                       value=""/><br>
                <input type="button" value="转账" onclick="customerModule.oilDrillTransfer()"/><br>
            </div>
        </div>
        <div style="float: left; width: 400px;">
            <h2>商户(business)</h2>
            <div>
                <strong>[登录]</strong><br/>
                手机:<input class="textbox" type="text" id="business-a-login-phone" style="width: 100px;"
                          value="15059592132"/>
                密码:<input class="textbox" type="text" id="business-a-login-loginPassword" style="width: 100px;"
                          value="123456"/>
                <input type="button" value="登录" onclick="businessModule.login()"/><br>
            </div>
            <div>
                <strong>[当前商户信息]</strong><br/>
                <input type="button" value="详情" onclick="businessModule.current()"/><br>
            </div>
            <div>
                <strong>[修改登录密码]</strong><br/>
                使用用户修改密码接口
            </div>
            <div>
                <strong>[修改支付密码]</strong><br/>
                使用用户修改密码接口
            </div>
            <div>
                <strong>[修改]</strong><br/>
                商户名:<input class="textbox" type="text" id="business-a-update-businessName" style="width: 100px;"
                           value="真实姓名"/><br>
                真实姓名:<input class="textbox" type="text" id="business-a-update-realName" style="width: 100px;"
                            value="真实姓名"/><br>
                银行:<input class="textbox" type="text" id="business-a-update-customerBank" style="width: 100px;"
                          value="农行"/><br>
                银行账号:<input class="textbox" type="text" id="business-a-update-customerBankAccount" style="width: 100px;"
                            value="6222600260001072442"/><br>
                <input type="button" value="修改" onclick="businessModule.update()"/><br>
            </div>
            <div>
                <strong>[用户列表]</strong><br/>
                用户手机:<input class="textbox" type="text" id="business-a-customerList-phone" style="width: 100px;"
                            value=""/><br>
                startIndex:<input class="textbox" type="text" id="business-a-customerList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="business-a-customerList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="businessModule.customerList()"/><br>
            </div>
            <div>
                <strong>[金币记录]</strong><br/>
                startIndex:<input class="textbox" type="text" id="business-a-goldRecordList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="business-a-goldRecordList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="businessModule.goldRecordList()"/><br>
            </div>
            <div>
                <strong>[金币提现记录]</strong><br/>
                startIndex:<input class="textbox" type="text" id="business-a-goldWithdrawList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="business-a-goldWithdrawList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="businessModule.goldWithdrawList()"/><br>
            </div>
            <div>
                <strong>[金币提现]</strong><br/>
                amount:<input class="textbox" type="text" id="business-a-goldWithdraw-amount"
                              style="width: 100px;"
                              value=""/>
                paymentPassword:<input class="textbox" type="text" id="business-a-goldWithdraw-paymentPassword" style="width: 100px;"
                                       value=""/><br>
                <input type="button" value="提现" onclick="businessModule.goldWithdraw()"/><br>
            </div>
            <div>
                <strong>[金币转账]</strong><br/>
                customerId:<input class="textbox" type="text" id="business-a-goldTransfer-customerId"
                                  style="width: 100px;"
                                  value=""/><br>
                amount:<input class="textbox" type="text" id="business-a-goldTransfer-amount"
                              style="width: 100px;"
                              value=""/>
                paymentPassword:<input class="textbox" type="text" id="business-a-goldTransfer-paymentPassword" style="width: 100px;"
                                       value=""/><br>
                <input type="button" value="转账" onclick="businessModule.goldTransfer()"/><br>
            </div>
            <div>
                <strong>[油钻记录]</strong><br/>
                startIndex:<input class="textbox" type="text" id="business-a-oilDrillRecordList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="business-a-oilDrillRecordList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="businessModule.oilDrillRecordList()"/><br>
            </div>
            <div>
                <strong>[油钻提现记录]</strong><br/>
                startIndex:<input class="textbox" type="text" id="business-a-oilDrillWithdrawList-startIndex"
                                  style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="business-a-oilDrillWithdrawList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="businessModule.oilDrillWithdrawList()"/><br>
            </div>
            <div>
                <strong>[油钻提现]</strong><br/>
                amount:<input class="textbox" type="text" id="business-a-oilDrillWithdraw-amount"
                              style="width: 100px;"
                              value=""/>
                paymentPassword:<input class="textbox" type="text" id="business-a-oilDrillWithdraw-paymentPassword" style="width: 100px;"
                                       value=""/><br>
                <input type="button" value="提现" onclick="businessModule.oilDrillWithdraw()"/><br>
            </div>
            <div>
                <strong>[油钻转账]</strong><br/>
                customerId:<input class="textbox" type="text" id="business-a-oilDrillTransfer-customerId"
                                  style="width: 100px;"
                                  value=""/><br>
                amount:<input class="textbox" type="text" id="business-a-oilDrillTransfer-amount"
                              style="width: 100px;"
                              value=""/>
                paymentPassword:<input class="textbox" type="text" id="business-a-oilDrillTransfer-paymentPassword" style="width: 100px;"
                                       value=""/><br>
                <input type="button" value="转账" onclick="businessModule.oilDrillTransfer()"/><br>
            </div>
        </div>
        <div style="float: left; width: 400px;">
            <h2>商品品类(goods_category)</h2>
            <div>
                <strong>[列表]</strong><br/>
                <input type="button" value="列表" onclick="goodsCategoryModule.listApi()"/><br>
            </div>
            <h2>商品(goods)</h2>
            <div>
                <strong>[列表]</strong><br/>
                content:<input class="textbox" type="text" id="goods-api-list-content" style="width: 100px;"
                               value=""/>
                categoryId1:<input class="textbox" type="text" id="goods-api-list-categoryId1" style="width: 100px;"
                                   value=""/><br>
                pageIndex:<input class="textbox" type="text" id="goods-api-list-pageIndex" style="width: 100px;"
                                 value="0"/>
                pageSize:<input class="textbox" type="text" id="goods-api-list-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="goodsModule.listApi()"/><br>
            </div>
            <div>
                <strong>[详情]</strong><br/>
                id:<input class="textbox" type="text" id="goods-api-detail-id" style="width: 100px;"
                          value=""/>
                <input type="button" value="详情" onclick="goodsModule.detailApi()"/><br>
            </div>
            <div>
                <strong>[获取商品图片]</strong><br/>
                fileName:<input class="textbox" type="text" id="goods-api-image-fileName" style="width: 100px;"
                          value="1.jpg"/>
                <input type="button" value="图片" onclick="goodsModule.imageApi()"/><br>
            </div>
            <h2>订单(order)</h2>
            <div>
                <strong>[列表]</strong><br/>
                content:<input class="textbox" type="text" id="order-api-list-content" style="width: 100px;"
                               value=""/>
                status:<select class="textbox" id="order-api-list-status" style="width: 100px;">
                <option value="">all</option>
                <option value="-1">取消</option>
                <option value="0">创建</option>
                <option value="1">已支付</option>
                <option value="2">已确认</option>
            </select><br>
                pageIndex:<input class="textbox" type="text" id="order-api-list-pageIndex" style="width: 100px;"
                                 value="0"/>
                pageSize:<input class="textbox" type="text" id="order-api-list-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="orderModule.listApi()"/><br>
            </div>
            <div>
                <strong>[详情]</strong><br/>
                id:<input class="textbox" type="text" id="order-api-detail-id" style="width: 100px;"
                          value=""/>
                <input type="button" value="详情" onclick="orderModule.detailApi()"/><br>
            </div>
            <div>
                <strong>[创建]</strong><br/>
                amount:<input class="textbox" type="text" id="order-api-create-amount" style="width: 100px;"
                               value=""/>
                recharge:<select class="textbox" id="order-api-create-recharge" style="width: 100px;">
                <option value="0">vipCard</option>
                <option value="1">normal</option>
                <option value="2">recharge</option>
            </select><br>
                remark(unnecessary):<input class="textbox" type="text" id="order-api-create-remark" style="width: 100px;"
                                 value=""/><br>

                has item?:<select class="textbox" id="order-api-create-hasItem" style="width: 100px;">
                <option value="1">yes</option>
                <option value="2">no</option>
            </select><br>

                orderItem:<br>
                goodsId:<input class="textbox" type="text" id="order-api-create-goodsId" style="width: 100px;"
                              value=""/>
                goodsName:<input class="textbox" type="text" id="order-api-create-goodsName" style="width: 100px;"
                                           value=""/><br>
                price:<input class="textbox" type="text" id="order-api-create-price" style="width: 100px;"
                               value=""/>
                count:<input class="textbox" type="text" id="order-api-create-count" style="width: 100px;"
                                 value=""/><br>
                <input type="button" value="列表" onclick="orderModule.createApi()"/><br>
            </div>
            <div>
                <strong>[确认支付]</strong><br/>
                id:<input class="textbox" type="text" id="order-api-paid-id" style="width: 100px;"
                          value=""/>
                <input type="button" value="详情" onclick="orderModule.paid()"/><br>
            </div>
            <h2>系统参数(system_params)</h2>
            <div>
                <strong>[列表]</strong><br/>
                <input type="button" value="列表" onclick="systemApiParamsModule.list()"/><br>
            </div>
        </div>
    </div>
    <div id="tabs-2" style="width: 2000px;display: block">
        <div style="float: left; width: 400px;">
            <h2>管理员(user)</h2>
            <div>
                <strong>[登录]</strong><br/>
                管理员:<input class="textbox" type="text" id="login-m-name" style="width: 100px;"
                           value="admin"/>
                密码:<input class="textbox" type="text" id="login-m-password" style="width: 100px;"
                          value="123456"/>
                <input type="button" value="登录" onclick="commonModule.login()"/><br>
            </div>
            <div>
                <strong>[修改密码]</strong><br/>
                旧密码:<input class="textbox" type="text" id="resetPwd-m-originPwd" style="width: 100px;"
                           value=""/>
                新密码:<input class="textbox" type="text" id="resetPwd-m-newPwd" style="width: 100px;"
                           value=""/>
                <input type="button" value="修改" onclick="commonModule.resetPwd()"/><br>
            </div>
            <div>
                <strong>[当前管理员]</strong><br/>
                <input type="button" value="获取" onclick="commonModule.currentUser()"/><br>
            </div>
            <div>
                <strong>[登出]</strong><br/>
                <input type="button" value="登出" onclick="commonModule.logout()"/><br>
            </div>

            <h2>系统参数(system_params)</h2>
            <div>
                <strong>[列表]</strong><br/>
                <input type="button" value="列表" onclick="systemParamsModule.list()"/><br>
            </div>
            <div>
                <strong>[修改参数]</strong><br/>
                id:<input class="textbox" type="text" id="systemParam-update-id" style="width: 100px;"
                          value=""/>
                value:<input class="textbox" type="text" id="systemParam-update-value" style="width: 100px;"
                             value=""/><br>
                <input type="button" value="修改" onclick="systemParamsModule.update()"/><br>
            </div>
        </div>
        <div style="float: left; width: 400px;">
            <h2>商品品类(goods_category)</h2>
            <div>
                <strong>[列表]</strong><br/>
                <input type="button" value="列表" onclick="goodsCategoryModule.list()"/><br>
            </div>
            <div>
                <strong>[创建]</strong><br/>
                name:<input class="textbox" type="text" id="goodsCategory-create-name" style="width: 100px;"
                            value=""/>
                <input type="button" value="创建" onclick="goodsCategoryModule.create()"/><br>
            </div>
            <div>
                <strong>[修改]</strong><br/>
                id:<input class="textbox" type="text" id="goodsCategory-update-id" style="width: 100px;"
                          value=""/>
                name:<input class="textbox" type="text" id="goodsCategory-update-name" style="width: 100px;"
                            value=""/><br>
                <input type="button" value="修改" onclick="goodsCategoryModule.update()"/><br>
            </div>
            <div>
                <strong>[上架/下架]</strong><br/>
                id:<input class="textbox" type="text" id="goodsCategory-status-id" style="width: 100px;"
                          value=""/>
                status:<select class="textbox" id="goodsCategory-status-status" style="width: 100px;">
                <option value="0">disable</option>
                <option value="1">enable</option>
            </select><br>
                <input type="button" value="修改" onclick="goodsCategoryModule.updateStatus()"/><br>
            </div>
            <div>
                <strong>[删除]</strong><br/>
                id:<input class="textbox" type="text" id="goodsCategory-delete-id" style="width: 100px;"
                          value=""/>
                <input type="button" value="删除" onclick="goodsCategoryModule.deleteById()"/><br>
            </div>
        </div>
        <div style="float: left; width: 400px;">
            <h2>商品(goods)</h2>
            <div>
                <strong>[列表]</strong><br/>
                content:<input class="textbox" type="text" id="goods-list-content" style="width: 100px;"
                               value=""/>
                categoryId1:<input class="textbox" type="text" id="goods-list-categoryId1" style="width: 100px;"
                                   value=""/><br>
                status:<select class="textbox" id="goods-list-status" style="width: 100px;">
                <option value="">all</option>
                <option value="0">disable</option>
                <option value="1">enable</option>
            </select><br>
                pageIndex:<input class="textbox" type="text" id="goods-list-pageIndex" style="width: 100px;"
                                 value="0"/>
                pageSize:<input class="textbox" type="text" id="goods-list-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="goodsModule.list()"/><br>
            </div>
            <div>
                <strong>[详情]</strong><br/>
                id:<input class="textbox" type="text" id="goods-detail-id" style="width: 100px;"
                          value=""/>
                <input type="button" value="详情" onclick="goodsModule.detail()"/><br>
            </div>
            <div>
                <strong>[创建]</strong><br/>
                categoryId1:<input class="textbox" type="text" id="goods-create-categoryId1" style="width: 100px;"
                                   value=""/>
                category1:<input class="textbox" type="text" id="goods-create-category1" style="width: 100px;"
                                 value=""/><br>
                name:<input class="textbox" type="text" id="goods-create-name" style="width: 100px;"
                            value=""/>
                description:<input class="textbox" type="text" id="goods-create-description" style="width: 100px;"
                                   value=""/><br>
                imagesUrl:<input class="textbox" type="text" id="goods-create-imagesUrl" style="width: 100px;"
                                 value=""/>
                price:<input class="textbox" type="text" id="goods-create-price" style="width: 100px;"
                             value=""/><br>
                <input type="button" value="创建" onclick="goodsModule.create()"/><br>

            </div>
            <div>
                <strong>[修改]</strong><br/>
                id:<input class="textbox" type="text" id="goods-update-id" style="width: 100px;"
                          value=""/><br>
                categoryId1:<input class="textbox" type="text" id="goods-update-categoryId1" style="width: 100px;"
                                   value=""/>
                category1:<input class="textbox" type="text" id="goods-update-category1" style="width: 100px;"
                                 value=""/><br>
                name:<input class="textbox" type="text" id="goods-update-name" style="width: 100px;"
                            value=""/>
                description:<input class="textbox" type="text" id="goods-update-description" style="width: 100px;"
                                   value=""/><br>
                imagesUrl:<input class="textbox" type="text" id="goods-update-imagesUrl" style="width: 100px;"
                                 value=""/>
                price:<input class="textbox" type="text" id="goods-update-price" style="width: 100px;"
                             value=""/><br>
                <input type="button" value="修改" onclick="goodsModule.update()"/><br>
            </div>
            <div>
                <strong>[上架/下架]</strong><br/>
                id:<input class="textbox" type="text" id="goods-status-id" style="width: 100px;"
                          value=""/>
                status:<select class="textbox" id="goods-status-status" style="width: 100px;">
                <option value="0">disable</option>
                <option value="1">enable</option>
            </select><br>
                <input type="button" value="修改" onclick="goodsModule.updateStatus()"/><br>
            </div>
            <div>
                <strong>[删除]</strong><br/>
                id:<input class="textbox" type="text" id="goods-delete-id" style="width: 100px;"
                          value=""/>
                <input type="button" value="删除" onclick="goodsModule.deleteById()"/><br>
            </div>
            <div>
                <strong>[上传图片]</strong><br/>
                fileName:<input class="textbox" type="text" id="goods-m-image-fileName" style="width: 100px;"
                             value="test.jpg"/>
                image:<input class="textbox" type="file" id="goods-m-image-file" accept="image/png, image/jpeg" style="width: 100px;"
                          value="1.jpg"/><br>
                <input type="button" value="图片" onclick="goodsModule.imageManagement()"/><br>
            </div>
            <div>
                <strong>[获取商品图片]</strong><br/>
                fileName:<input class="textbox" type="text" id="goods-mG-image-fileName" style="width: 100px;"
                                value="test.jpg"/>
                <input type="button" value="图片" onclick="goodsModule.imageGetM()"/><br>
            </div>
        </div>
        <div style="float: left; width: 400px;">
            <h2>用户</h2>
            <strong>[用户列表]</strong><br/>
            搜索内容:<input class="textbox" type="text" id="customer-m-list-managementContent" style="width: 100px;"
                      value=""/><br/>
            status:<select class="textbox" id="customer-m-list-status" style="width: 100px;">
            <option value="" selected>全部</option>
            <option value="0" >会员</option>
            <option value="1" >商户</option>
            <option value="2" >申请成为商户</option>
            </select><br>
            startIndex:<input class="textbox" type="text" id="customer-m-List-startIndex" style="width: 100px;"
                              value="0"/>
            pageSize:<input class="textbox" type="text" id="customer-m-List-pageSize" style="width: 100px;"
                            value="10"/><br>
            <input type="button" value="列表" onclick="customerMaModule.list()"/><br>
            <div>
                <strong>[详情]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-detail-id" style="width: 100px;"
                          value=""/><br/>
                <input type="button" value="详情" onclick="customerMaModule.deleteById()"/><br>
            </div>
            <div>
                <strong>[重置登陆密码]</strong><br/>
                商户id:<input class="textbox" type="text" id="customer-m-resetLoginPassword-id" style="width: 100px;"
                            value="1"/>
                <input type="button" value="重置" onclick="customerMaModule.resetLoginPasswordById()"/><br>
            </div>
            <div>
                <strong>[重置支付密码]</strong><br/>
                商户id:<input class="textbox" type="text" id="customer-m-resetPaymentPassword-id" style="width: 100px;"
                            value="1"/>
                <input type="button" value="重置" onclick="customerMaModule.resetPaymentPassword()"/><br>
            </div>
            <div>
                <strong>[商户启用/禁用]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-status-id" style="width: 100px;"
                          value=""/>
                商户状态（1启用0   禁用）businessStatus:<select class="textbox" id="customer-m-status-businessStatus" style="width: 100px;">
                <option value="0">disable</option>
                <option value="1">enable</option>
            </select><br>
                <input type="button" value="修改" onclick="customerMaModule.resetStatus()"/><br>
            </div>
            <div>
                <strong>[设为商户]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-business-id" style="width: 100px;"
                          value=""/>
                商户名称:<input class="textbox" type="text" id="customer-m-business-businessName" style="width: 100px;"
                          value=""/><br>
                <input type="button" value="修改" onclick="customerMaModule.resetBusiness()"/><br>
            </div>
            <div>
                <strong>[审核商户]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-businessVerify-id" style="width: 100px;"
                          value=""/>
                审核结果:<select class="textbox" id="customer-m-businessVerify-status" style="width: 100px;">
                <option value="0">审核不通过</option>
                <option value="1">审核通过</option>
            </select><br>
                <input type="button" value="修改" onclick="customerMaModule.businessVerify()"/><br>
            </div>
            <div>
                <strong>[设为等级]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-level-id" style="width: 100px;"
                          value=""/>
                设置等级:<input class="textbox" type="text" id="customer-m-level-level" style="width: 100px;"
                          value=""/>
            <br>
                <input type="button" value="修改" onclick="customerMaModule.resetLevel()"/><br>
            </div>
            <div>
                <strong>[上级]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-customerParent-id" style="width: 100px;"
                          value=""/>
            <br>
                <input type="button" value="查找" onclick="customerMaModule.customerParent()"/><br>
            </div>
            <div>
                <strong>[下级列表]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-customerLowList-id" style="width: 100px;"
                          value=""/>
                startIndex:<input class="textbox" type="text" id="customer-m-customerLowList-startIndex" style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-m-customerLowList-pageSize" style="width: 100px;"
                                value="10"/><br>
            <br>
                <input type="button" value="列表" onclick="customerMaModule.customerLowList()"/><br>
            </div>
            <h2>增减金币</h2>
            <div>
                <strong>[用户增减金币]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-addReduceGold-id" style="width: 100px;"
                          value="1"/><br/>
                增加或减少（1充值，4后台减少，5后台增加）:<br/>
                <select class="textbox" id="customer-m-addReduceGold-type" style="width: 100px;">
                    <option value="1">充值</option>
                    <option value="4">后台减少</option>
                    <option value="5">后台增加</option>
                </select><br>
                充值（人民币）:<input class="textbox" type="text" id="customer-m-addReduceGold-amount" style="width: 100px;"
                          value="900"/>
                <input type="button" value="提交" onclick="customerMaModule.addReduceGold()"/><br>
                <strong>[商户增减金币]</strong><br/>
                id:<input class="textbox" type="text" id="business-m-addReduceGold-id" style="width: 100px;"
                          value="1"/><br/>
                增加或减少（4后台减少，5后台增加）:<br/>
                <select class="textbox" id="business-m-addReduceGold-type" style="width: 100px;">
                    <option value="4">后台减少</option>
                    <option value="5">后台增加</option>
                </select><br>
                充值（人民币）:<input class="textbox" type="text" id="business-m-addReduceGold-amount" style="width: 100px;"
                          value="900"/>
                <input type="button" value="提交" onclick="customerMaModule.businessAddReduceGold()"/><br>
            </div>
            <h2>增减油钻</h2>
            <div>
                <strong>[用户增减油钻]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-addReduceOilDrill-id" style="width: 100px;"
                          value="1"/><br/>
                增加或减少（1充值，4后台减少，5后台增加）:<br/>
                <select class="textbox" id="customer-m-addReduceOilDrill-type" style="width: 100px;">
                    <option value="1">充值</option>
                    <option value="4">后台减少</option>
                    <option value="5">后台增加</option>
                </select><br>
                充值（人民币）:<input class="textbox" type="text" id="customer-m-addReduceOilDrill-amount" style="width: 100px;"
                          value="900"/>
                <input type="button" value="提交" onclick="customerMaModule.addReduceOilDrill()"/><br>
                <strong>[商户增减油钻]</strong><br/>
                id:<input class="textbox" type="text" id="business-m-businessAddReduceOil-id" style="width: 100px;"
                          value="1"/><br/>
                增加或减少（4后台减少，5后台增加）:<br/>
                <select class="textbox" id="business-m-businessAddReduceOil-type" style="width: 100px;">
                    <option value="4">后台减少</option>
                    <option value="5">后台增加</option>
                </select><br>
                充值（人民币）:<input class="textbox" type="text" id="business-m-businessAddReduceOil-amount" style="width: 100px;"
                          value="900"/>
                <input type="button" value="提交" onclick="customerMaModule.businessAddReduceOil()"/><br>
            </div>
            <h2>列表</h2>
            <div>
                <strong>[用户金币收支记录]</strong><br/>
                customerId:<input class="textbox" type="text" id="customer-m-GoldList-customerId"
                                  style="width: 100px;" value="0"/>
                startIndex:<input class="textbox" type="text" id="customer-m-GoldList-startIndex"
                                  style="width: 100px;" value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-m-GoldList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerMaModule.customerGoldList()"/><br>
            </div>
            <div>
                <strong>[商户金币收支记录]</strong><br/>
                businessId:<input class="textbox" type="text" id="business-m-GoldList-businessId"
                                  style="width: 100px;" value="1"/>
                startIndex:<input class="textbox" type="text" id="business-m-GoldList-startIndex"
                                  style="width: 100px;" value="0"/>
                pageSize:<input class="textbox" type="text" id="business-m-GoldList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerMaModule.businessGoldList()"/><br>
            </div>
            <div>
                <strong>[用户油钻收支记录]</strong><br/>
                customerId:<input class="textbox" type="text" id="customer-m-oilDrillList-customerId"
                                  style="width: 100px;" value="1"/>
                startIndex:<input class="textbox" type="text" id="customer-m-oilDrillList-startIndex"
                                  style="width: 100px;" value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-m-oilDrillList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerMaModule.customerOilDrillList()"/><br>
            </div>
            <div>
                <strong>[商户油钻收支记录]</strong><br/>
                businessId:<input class="textbox" type="text" id="business-m-oilDrillList-businessId"
                                  style="width: 100px;" value="1"/>
                startIndex:<input class="textbox" type="text" id="business-m-oilDrillList-startIndex"
                                  style="width: 100px;" value="0"/>
                pageSize:<input class="textbox" type="text" id="business-m-oilDrillList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerMaModule.businessOilDrillList()"/><br>
            </div>
            <div>
                <strong>[用户钢蹦收支记录]</strong><br/>
                customerId:<input class="textbox" type="text" id="customer-m-coinList-customerId"
                                  style="width: 100px;" value="1"/>
                startIndex:<input class="textbox" type="text" id="customer-m-coinList-startIndex"
                                  style="width: 100px;" value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-m-coinList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerMaModule.customerCoinList()"/><br>
            </div>
            <h2>升级vip</h2>
            <div>
                <strong>[用户升级vip]</strong><br/>
                id:<input class="textbox" type="text" id="customer-m-becomeVip-id" style="width: 100px;"
                          value="1"/><br/>
                <input type="button" value="提交" onclick="customerMaModule.becomeVip()"/><br>
            </div>
        </div>
        <div style="float: left; width: 400px;">
            <h2>金币提现管理</h2>
            <strong>[提现列表]</strong><br/>
            搜索内容:<input class="textbox" type="text" id="goldRecord-m-list-phone" style="width: 100px;"
                        value=""/><br/>
            status:<select class="textbox" id="goldRecord-m-list-status" style="width: 100px;">
            <option value="" selected>全部</option>
            <option value="0" >待提现</option>
            <option value="1" >提现记录</option>
            </select><br>
            startIndex:<input class="textbox" type="text" id="goldRecord-m-List-startIndex" style="width: 100px;"
                              value="0"/>
            pageSize:<input class="textbox" type="text" id="goldRecord-m-List-pageSize" style="width: 100px;"
                            value="10"/><br>
            <input type="button" value="列表" onclick="goldRecordMaModule.list()"/><br>
            <strong>[打款处理]</strong><br/>
            金币提现记录id:<input class="textbox" type="text" id="goldRecord-m-moneyHandle-id" style="width: 100px;"
                        value=""/><br/>
            <input type="button" value="提交" onclick="goldRecordMaModule.moneyHandle()"/><br>
            <strong>[改变状态]</strong><br/>
            金币提现记录id:<input class="textbox" type="text" id="goldRecord-m-resetStatus-id" style="width: 100px;"
                        value=""/><br/>
            status:<select class="textbox" id="goldRecord-m-resetStatus-status" style="width: 100px;">
            <option value="1" >审核通过</option>
            <option value="-2" >审核驳回</option>
             </select><br>
            拒绝理由：<input class="textbox" type="text" id="goldRecord-m-resetStatus-refuseReason" style="width: 100px;"
                        value="不给提"/><br/>
            <input type="button" value="提交" onclick="goldRecordMaModule.resetStatus()"/><br>
            <h2>油钻提现管理</h2>
            <strong>[提现列表]</strong><br/>
            搜索内容:<input class="textbox" type="text" id="oilDrillRecord-m-list-phone" style="width: 100px;"
                        value=""/><br/>
            status:<select class="textbox" id="oilDrillRecord-m-list-status" style="width: 100px;">
            <option value="" selected>全部</option>
            <option value="0" >待提现</option>
            <option value="1" >提现记录</option>
            </select><br>
            startIndex:<input class="textbox" type="text" id="oilDrillRecord-m-List-startIndex" style="width: 100px;"
                              value="0"/>
            pageSize:<input class="textbox" type="text" id="oilDrillRecord-m-List-pageSize" style="width: 100px;"
                            value="10"/><br>
            <input type="button" value="列表" onclick="oilDrillRecordMaModule.list()"/><br>
            <strong>[打款处理]</strong><br/>
            油钻提现记录id:<input class="textbox" type="text" id="oilDrillRecord-m-moneyHandle-id" style="width: 100px;"
                        value=""/><br/>
            <input type="button" value="提交" onclick="oilDrillRecordMaModule.moneyHandle()"/><br>
            <strong>[改变状态]</strong><br/>
            油钻提现记录id:<input class="textbox" type="text" id="oilDrillRecord-m-resetStatus-id" style="width: 100px;"
                        value=""/><br/>
            status:<select class="textbox" id="oilDrillRecord-m-resetStatus-status" style="width: 100px;">
            <option value="1" >审核通过</option>
            <option value="-2" >审核驳回</option>
             </select><br>
            拒绝理由：<input class="textbox" type="text" id="oilDrillRecord-m-resetStatus-refuseReason" style="width: 100px;"
                        value="不给提"/><br/>
            <input type="button" value="提交" onclick="oilDrillRecordMaModule.resetStatus()"/><br>
            <h2>钢镚提现管理</h2>
            <strong>[提现列表]</strong><br/>
            搜索内容:<input class="textbox" type="text" id="coinRecord-m-list-phone" style="width: 100px;"
                        value=""/><br/>
            type:<select class="textbox" id="coinRecord-m-list-status" style="width: 100px;">
            <option value="" selected>全部</option>
            <option value="0" >待提现</option>
            <option value="1" >提现记录</option>
            </select><br>
            startIndex:<input class="textbox" type="text" id="coinRecord-m-List-startIndex" style="width: 100px;"
                              value="0"/>
            pageSize:<input class="textbox" type="text" id="coinRecord-m-List-pageSize" style="width: 100px;"
                            value="10"/><br>
            <input type="button" value="列表" onclick="coinRecordMaModule.list()"/><br>
            <strong>[打款处理]</strong><br/>
            油钻提现记录id:<input class="textbox" type="text" id="coinRecord-m-moneyHandle-id" style="width: 100px;"
                        value=""/><br/>
            <input type="button" value="提交" onclick="coinRecordMaModule.moneyHandle()"/><br>
            <strong>[改变状态]</strong><br/>
            油钻提现记录id:<input class="textbox" type="text" id="coinRecord-m-resetStatus-id" style="width: 100px;"
                        value=""/><br/>
            type:<select class="textbox" id="coinRecord-m-resetStatus-status" style="width: 100px;">
            <option value="1" >审核通过</option>
            <option value="-2" >审核驳回</option>
             </select><br>
            拒绝理由：<input class="textbox" type="text" id="coinRecord-m-resetStatus-refuseReason" style="width: 100px;"
                        value="不给提"/><br/>
            <input type="button" value="提交" onclick="coinRecordMaModule.resetStatus()"/><br>
        </div>


        <div style="float: left; width: 400px;">
            <h2>订单(order)</h2>
            <div>
                <strong>[列表]</strong><br/>
                content:<input class="textbox" type="text" id="order-list-content" style="width: 100px;"
                            value=""/>
                status:<select class="textbox" id="order-list-status" style="width: 100px;">
                <option value="">all</option>
                <option value="-1">取消</option>
                <option value="2">确认</option>
                <option value="1">待处理</option>
            </select><br>
                pageIndex:<input class="textbox" type="text" id="order-list-pageIndex" style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="order-list-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="orderModule.list()"/><br>
            </div>
            <div>
                <strong>[详情]</strong><br/>
                id:<input class="textbox" type="text" id="order-detail-id" style="width: 100px;"
                               value=""/>
                <input type="button" value="详情" onclick="orderModule.detail()"/><br>
            </div>
            <div>
                <strong>[修改状态]</strong><br/>
                id:<input class="textbox" type="text" id="order-status-id" style="width: 100px;"
                               value=""/>
                status:<select class="textbox" id="order-status-status" style="width: 100px;">
                <option value="-1">取消</option>
                <option value="2">确认</option>
            </select><br>
                cancelReason(unnecessary):<input class="textbox" type="text" id="order-status-cancelReason" style="width: 100px;"
                                 value=""/><br>
                remarks(unnecessary):<input class="textbox" type="text" id="order-status-remarks" style="width: 100px;"
                                value=""/><br>
                <input type="button" value="修改" onclick="orderModule.updateStatus()"/><br>
            </div>
        </div>
    </div>
    <div style="clear: both;"></div>
</div>

<div style="margin-top: 7px; height:27px;" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
    <b>Requested URL: </b>
    <input type="text" id="requestedUrl" style="width:60%;"
           domain="${request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()?c}"/>
</div>

<div id="d_param" class="ui-tabs ui-corner-all">
    <div style="float:left;width:300px; margin:0 10px 10px 0;height: 450px;"
         class="ui-tabs ui-widget ui-widget-content ui-corner-all">
        <strong style="margin-left: 5px;">Requested Method:</strong>

        <lable id="requestedMethod"></lable>
        <br/><br/>
        <strong style="margin-left: 5px;">HTTP Header:</strong>
        <table id="tb_h_param">
        <#--<tr>
            <td>ClientId:</td>
            <td><input type="text" id="p_clientId"/></td>
        </tr>
        <tr>
            <td>SecretKey:</td>
            <td><input type="text" id="p_secretKey"/></td>
        </tr>-->
            <tr>
                <td colspan="2"><textarea id="p_data" rows="20" cols="35"></textarea></td>
            </tr>
        </table>
    </div>

    <div style="margin-left:315px; z-index:9999; position:relative;">

        <div id="resultShow"></div>
    </div>
</div>


</body>

<script src="/resources/js/jquery-1.10.2.js"></script>
<script src="/resources/js/jquery-ui.js"></script>
<script src="/resources/js/JsonParser/JsonParser.js"></script>
<script src="/resources/js/crypto/hmac-sha1.js"></script>
<script src="/resources/js/crypto/enc-base64-min.js"></script>
<script src="/resources/js/tool.js"></script>
<script src="/resources/js/mask/mask.js"></script>

</html>
