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
                <input type="button" value="修改" onclick="customerModule.update()"/><br>
            </div>
            <div>
                <strong>[列表]</strong><br/>
                商户账户或商户名称:<input class="textbox" type="text" id="customer-a-businessList-content" style="width: 100px;"
                                  value=""/>
                startIndex:<input class="textbox" type="text" id="customer-a-businessList-startIndex" style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-a-businessList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerModule.businessList()"/><br>
            </div>
            <div>
                <strong>[商户列表]</strong><br/>
                startIndex:<input class="textbox" type="text" id="customer-a-inviteList-startIndex" style="width: 100px;"
                                  value="0"/>
                pageSize:<input class="textbox" type="text" id="customer-a-inviteList-pageSize" style="width: 100px;"
                                value="10"/><br>
                <input type="button" value="列表" onclick="customerModule.inviteList()"/><br>
            </div>
            <div>
                <strong>[申请成为商户]</strong><br/>
                businessName:<input class="textbox" type="text" id="customer-a-businessApplication-businessName" style="width: 100px;"
                                  value="商户"/>
                <input type="button" value="列表" onclick="customerModule.businessApplication()"/><br>
            </div>
            <div>
                <strong>[生成邀请链接]</strong><br/>
                <input type="button" value="列表" onclick="customerModule.inviteCode()"/><br>
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
        <div>
            <strong>[用户列表]</strong><br/>
            搜索内容:<input class="textbox" type="text" id="customer-m-list-managementContent" style="width: 100px;"
                      value=""/><br/>
            startIndex:<input class="textbox" type="text" id="customer-m-List-startIndex" style="width: 100px;"
                              value="0"/>
            pageSize:<input class="textbox" type="text" id="customer-m-List-pageSize" style="width: 100px;"
                            value="10"/><br>
            <input type="button" value="列表" onclick="customerMaModule.list()"/><br>
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
