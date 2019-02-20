$(function () {

    window.jsonParser = new JsonParser("");

    /*$("#secretKey").val($("option:selected", $("#selClient")).attr("secretKey"));

    $("#selClient").change(function(){
        var optionSelected = $("option:selected", this);
        $("#secretKey").val($(optionSelected).attr("secretKey"));

        $("#p_clientId").val( $("#selClient").val());
        $("#p_secretKey").val( $("#secretKey").val());
    });

    $("#p_clientId").val( $("#selClient").val());
    $("#p_secretKey").val( $("#secretKey").val());*/

});

function openTab(tab) {
    if (tab === 1) {
        $("#tabs-2").hide();
        $("#tabs-1").show();
    } else if (tab === 2) {
        $("#tabs-1").hide();
        $("#tabs-2").show();
    }
}

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

function showResult(settings) {
    $.fn.mask();

    $("#requestedUrl").val($("#requestedUrl").attr("domain") + settings.url);
    $("#requestedMethod").text(settings.type);
    $("#p_data").text(settings.data == null ? "" : settings.data);

    var timestamp = Math.round(new Date().getTime());
    /*var requestId = uuid();
    var visitorId = $("#visitorId").val();*/

    /*var signature = [];
    signature.push("uri=" + settings.url);
    signature.push("&method=" + settings.type.toUpperCase());
    if (settings.data != null && settings.data.length > 0)
        signature.push("&body=" + settings.data);
    signature.push("&timestamp=" + timestamp);
    signature.push("&requestId=" + requestId);
    if(visitorId != null && visitorId.length > 0)
        signature.push("&visitorId=" + visitorId);
    console.log(signature.join(''));
    var signatureStr =  CryptoJS.HmacSHA1(signature.join(''),$("#secretKey").val()).toString(CryptoJS.enc.Base64);
    console.log(signatureStr);*/

    $.ajax({
        type: settings.type,
        url: settings.url,
        dataType: settings.dataType,
        data: settings.data,
        contentType: settings.contentType === null ? settings.contentType : "application/json",
        processData: settings.processData === null ? settings.processData : true,
        async: settings.async === null ? settings.async : true,
        headers: {
            "Timestamp": timestamp
            /*,"Client-Id": $("#selClient").val(),
            "Visitor-Id" : $("#visitorId").val(),
            "Request-Id" : requestId,
            "Client-Signature": signatureStr*/
        },
        success: function (data) {

            window.jsonParser.jsonContent = JSON.stringify(data);
            window.jsonParser.init();

            if (settings.success)
                settings.success(data);

            $.fn.mask.close();
        },
        error: function (data) {
            window.jsonParser.jsonContent = JSON.stringify(data);
            window.jsonParser.init();
            $.fn.mask.close();
        }
    });
}

var commonModule = {
    login: function () {
        var settings = {
            type: "POST",
            url: "/management/login",
            dataType: "json",
            data: JSON.stringify({
                "name": $("#login-m-name").val(),
                "password": $("#login-m-password").val()
            })
        };
        showResult(settings);
    },
    resetPwd: function () {
        var settings = {
            type: "PUT",
            url: "/management/password",
            dataType: "json",
            data: JSON.stringify({
                "originPassword": $("#resetPwd-m-originPwd").val(),
                "newPassword": $("#resetPwd-m-newPwd").val()
            })
        };
        showResult(settings);
    },
    currentUser: function () {
        var settings = {
            type: "GET",
            url: "/management/current",
            dataType: "json"
        };
        showResult(settings);
    },
    logout: function () {
        var settings = {
            type: "GET",
            url: "/management/logout",
            dataType: "json"
        };
        showResult(settings);
    }

};
var customerModule = {
    create: function () {
        var settings = {
            type: "POST",
            url: "/api/customer/create",
            dataType: "json",
            data: JSON.stringify({
                "realName": $("#customer-create-realName").val(),
                "phone": $("#customer-create-phone").val(),
                "loginPassword": $("#customer-create-loginPassword").val(),
                "paymentPassword": $("#customer-create-paymentPassword").val(),
                "customerBank": $("#customer-create-customerBank").val(),
                "customerBankAccount": $("#customer-create-customerBankAccount").val(),
                "parentId": $("#customer-create-parentId").val()
            })
        };
        showResult(settings);
    },
    login: function () {
        var settings = {
            type: "POST",
            url: "/api/customer/login",
            dataType: "json",
            data: JSON.stringify({
                "phone": $("#customer-a-login-phone").val(),
                "loginPassword": $("#customer-a-login-loginPassword").val()
            })
        };
        showResult(settings);
    },
    resetLogPwd: function () {
        var settings = {
            type: "PUT",
            url: "/api/customer/loginPassword",
            dataType: "json",
            data: JSON.stringify({
                "originPassword": $("#customer-resetLogPwd-a-originPwd").val(),
                "newPassword": $("#customer-resetLogPwd-a-newPwd").val()
            })
        };
        showResult(settings);
    },
    current: function () {
        var settings = {
            type: "GET",
            url: "/api/customer/current",
            dataType: "json"
        };
        showResult(settings);
    },
    resetPayPwd: function () {
        var settings = {
            type: "PUT",
            url: "/api/customer/paymentPassword",
            dataType: "json",
            data: JSON.stringify({
                "originPassword": $("#customer-resetPayPwd-a-originPwd").val(),
                "newPassword": $("#customer-resetPayPwd-a-newPwd").val()
            })
        };
        showResult(settings);
    },
    logout: function () {
        var settings = {
            type: "GET",
            url: "/api/customer/logout",
            dataType: "json"
        };
        showResult(settings);
    },
    update: function () {
        var settings = {
            type: "PUT",
            url: "/api/customer/update",
            dataType: "json",
            data: JSON.stringify({
                "realName": $("#customer-a-update-realName").val(),
                "customerBank": $("#customer-a-update-customerBank").val(),
                "customerBankAccount": $("#customer-a-update-customerBankAccount").val(),
                "address": $("#customer-a-update-address").val()
            })
        };
        showResult(settings);
    },
    addAddress: function () {
        var settings = {
            type: "PUT",
            url: "/api/customer/addAddress",
            dataType: "json",
            data: JSON.stringify({
                "address": $("#customer-a-addAddress-address").val()
            })
        };
        showResult(settings);
    },
    businessList: function () {
        var settings = {
            type: "POST",
            url: "/api/customer/businessList",
            dataType: "json",
            data: JSON.stringify({
                "content": $("#customer-a-businessList-content").val(),
                "pageIndex": $("#customer-a-businessList-startIndex").val(),
                "pageSize": $("#customer-a-businessList-pageSize").val()
            })
        };
        showResult(settings);
    },
    businessDetail: function () {
        var settings = {
            type: "GET",
            url: "/api/customer/" + $("#customer-a-businessDetail-id").val(),
            dataType: "json"
        };
        showResult(settings);
    },
    inviteList: function () {
        var settings = {
            type: "POST",
            url: "/api/customer/inviteList",
            dataType: "json",
            data: JSON.stringify({
                "pageIndex": $("#customer-a-inviteList-startIndex").val(),
                "pageSize": $("#customer-a-inviteList-pageSize").val()
            })
        };
        showResult(settings);
    },
    businessApplication: function () {
        var settings = {
            type: "PUT",
            url: "/api/customer/businessApplication",
            dataType: "json",
            data: JSON.stringify({
                "businessName": $("#customer-a-businessApplication-businessName").val()
            })
        };
        showResult(settings);
    },
    inviteCode: function () {
        var settings = {
            type: "GET",
            url: "/api/customer/inviteCode",
            dataType: "json"
        };
        showResult(settings);
    },
    exchangeGold: function () {
        var settings = {
            type: "PUT",
            url: "/api/coinRecord/exchangeGold",
            dataType: "json",
            data: JSON.stringify({
                "amount": $("#customer-a-exchangeGold-amount").val(),
                "paymentPassword": $("#customer-a-exchangeGold-paymentPassword").val()
            })
        };
        showResult(settings);
    },
    exchangeOilDrill: function () {
        var settings = {
            type: "PUT",
            url: "/api/coinRecord/exchangeOilDrill",
            dataType: "json",
            data: JSON.stringify({
                "amount": $("#customer-a-exchangeOilDrill-amount").val(),
                "paymentPassword": $("#customer-a-exchangeOilDrill-paymentPassword").val()
            })
        };
        showResult(settings);
    },
    coinWithdraw: function () {
        var settings = {
            type: "POST",
            url: "/api/coinRecord/coinWithdraw",
            dataType: "json",
            data: JSON.stringify({
                "amount": $("#customer-a-coinWithdraw-amount").val(),
                "paymentPassword": $("#customer-a-coinWithdraw-paymentPassword").val()
            })
        };
        showResult(settings);
    },
    coinRecordList: function () {
        var settings = {
            type: "POST",
            url: "/api/coinRecord/coinRecordList",
            dataType: "json",
            data: JSON.stringify({
                "pageIndex": $("#customer-a-coinRecordList-startIndex").val(),
                "pageSize": $("#customer-a-coinRecordList-pageSize").val()
            })
        };
        showResult(settings);
    },
    goldRecordList: function () {
        var settings = {
            type: "POST",
            url: "/api/goldRecord/goldRecordList",
            dataType: "json",
            data: JSON.stringify({
                "pageIndex": $("#customer-a-goldRecordList-startIndex").val(),
                "pageSize": $("#customer-a-goldRecordList-pageSize").val()
            })
        };
        showResult(settings);
    },
    oilDrillRecordList: function () {
        var settings = {
            type: "POST",
            url: "/api/oilDrillRecord/oilDrillRecordList",
            dataType: "json",
            data: JSON.stringify({
                "pageIndex": $("#customer-a-oilDrillRecordList-startIndex").val(),
                "pageSize": $("#customer-a-oilDrillRecordList-pageSize").val()
            })
        };
        showResult(settings);
    },
    oilDrillTransfer: function () {
        var settings = {
            type: "POST",
            url: "/api/customer/oilDrillTransfer",
            dataType: "json",
            data: JSON.stringify({
                "businessId": $("#customer-a-oilDrillTransfer-customerId").val(),
                "amount": $("#customer-a-oilDrillTransfer-amount").val(),
                "paymentPassword": $("#customer-a-oilDrillTransfer-paymentPassword").val()
            })
        };
        showResult(settings);
    },
    goldTransfer: function () {
        var settings = {
            type: "POST",
            url: "/api/customer/goldTransfer",
            dataType: "json",
            data: JSON.stringify({
                "businessId": $("#customer-a-goldTransfer-customerId").val(),
                "amount": $("#customer-a-goldTransfer-amount").val(),
                "paymentPassword": $("#customer-a-goldTransfer-paymentPassword").val()
            })
        };
        showResult(settings);
    },

};

var businessModule = {
    login: function () {
        var settings = {
            type: "POST",
            url: "/api/business/login",
            dataType: "json",
            data: JSON.stringify({
                "phone": $("#business-a-login-phone").val(),
                "loginPassword": $("#business-a-login-loginPassword").val()
            })
        };
        showResult(settings);
    },
    current: function () {
        var settings = {
            type: "GET",
            url: "/api/business/current",
            dataType: "json"
        };
        showResult(settings);
    },
    update: function () {
        var settings = {
            type: "PUT",
            url: "/api/business/update",
            dataType: "json",
            data: JSON.stringify({
                "businessName": $("#business-a-update-businessName").val(),
                "realName": $("#business-a-update-realName").val(),
                "customerBank": $("#business-a-update-customerBank").val(),
                "customerBankAccount": $("#business-a-update-customerBankAccount").val()
            })
        };
        showResult(settings);
    },
    customerList: function () {
        var settings = {
            type: "POST",
            url: "/api/business/customerList",
            dataType: "json",
            data: JSON.stringify({
                "phone": $("#business-a-customerList-phone").val(),
                "pageIndex": $("#business-a-customerList-startIndex").val(),
                "pageSize": $("#business-a-customerList-pageSize").val()
            })
        };
        showResult(settings);
    },
    goldRecordList: function () {
        var settings = {
            type: "POST",
            url: "/api/business/goldRecordList",
            dataType: "json",
            data: JSON.stringify({
                "pageIndex": $("#business-a-goldRecordList-startIndex").val(),
                "pageSize": $("#business-a-goldRecordList-pageSize").val()
            })
        };
        showResult(settings);
    },
    goldWithdrawList: function () {
        var settings = {
            type: "POST",
            url: "/api/business/goldWithdrawList",
            dataType: "json",
            data: JSON.stringify({
                "pageIndex": $("#business-a-goldWithdrawList-startIndex").val(),
                "pageSize": $("#business-a-goldWithdrawList-pageSize").val()
            })
        };
        showResult(settings);
    },
    goldWithdraw: function () {
        var settings = {
            type: "POST",
            url: "/api/business/goldWithdraw",
            dataType: "json",
            data: JSON.stringify({
                "amount": $("#business-a-goldWithdraw-amount").val(),
                "paymentPassword": $("#business-a-goldWithdraw-paymentPassword").val()
            })
        };
        showResult(settings);
    },
    goldTransfer: function () {
        var settings = {
            type: "POST",
            url: "/api/business/goldTransfer",
            dataType: "json",
            data: JSON.stringify({
                "customerId": $("#business-a-goldTransfer-customerId").val(),
                "amount": $("#business-a-goldTransfer-amount").val(),
                "paymentPassword": $("#business-a-goldTransfer-paymentPassword").val()
            })
        };
        showResult(settings);
    },
    oilDrillRecordList: function () {
        var settings = {
            type: "POST",
            url: "/api/business/oilDrillRecordList",
            dataType: "json",
            data: JSON.stringify({
                "pageIndex": $("#business-a-oilDrillRecordList-startIndex").val(),
                "pageSize": $("#business-a-oilDrillRecordList-pageSize").val()
            })
        };
        showResult(settings);
    },
    oilDrillWithdrawList: function () {
        var settings = {
            type: "POST",
            url: "/api/business/oilDrillWithdrawList",
            dataType: "json",
            data: JSON.stringify({
                "pageIndex": $("#business-a-oilDrillWithdrawList-startIndex").val(),
                "pageSize": $("#business-a-oilDrillWithdrawList-pageSize").val()
            })
        };
        showResult(settings);
    },
    oilDrillWithdraw: function () {
        var settings = {
            type: "POST",
            url: "/api/business/oilDrillWithdraw",
            dataType: "json",
            data: JSON.stringify({
                "amount": $("#business-a-oilDrillWithdraw-amount").val(),
                "paymentPassword": $("#business-a-oilDrillWithdraw-paymentPassword").val()
            })
        };
        showResult(settings);
    },
    oilDrillTransfer: function () {
        var settings = {
            type: "POST",
            url: "/api/business/oilDrillTransfer",
            dataType: "json",
            data: JSON.stringify({
                "customerId": $("#business-a-oilDrillTransfer-customerId").val(),
                "amount": $("#business-a-oilDrillTransfer-amount").val(),
                "paymentPassword": $("#business-a-oilDrillTransfer-paymentPassword").val()
            })
        };
        showResult(settings);
    }
};

var systemParamsModule= {
    list: function () {
        var settings = {
            type: "GET",
            url: "/management/systemParams/list",
            dataType: "json"
        };
        showResult(settings);
    },
    update: function () {
        var settings = {
            type: "PUT",
            url: "/management/systemParams/update",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#systemParam-update-id").val(),
                "value": $("#systemParam-update-value").val()
            })
        };
        showResult(settings);
    },
};

var goodsCategoryModule= {
    list: function () {
        var settings = {
            type: "GET",
            url: "/management/goodsCategory/list",
            dataType: "json"
        };
        showResult(settings);
    },
    listApi: function () {
        var settings = {
            type: "GET",
            url: "/api/goodsCategory/list",
            dataType: "json"
        };
        showResult(settings);
    },
    create: function () {
        var settings = {
            type: "POST",
            url: "/management/goodsCategory/create",
            dataType: "json",
            data: JSON.stringify({
                "name": $("#goodsCategory-create-name").val()
            })
        };
        showResult(settings);
    },
    update: function () {
        var settings = {
            type: "PUT",
            url: "/management/goodsCategory/update",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#goodsCategory-update-id").val(),
                "name": $("#goodsCategory-update-name").val()
            })
        };
        showResult(settings);
    },
    updateStatus: function () {
        var settings = {
            type: "PUT",
            url: "/management/goodsCategory/resetStatus",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#goodsCategory-status-id").val(),
                "status": $("#goodsCategory-status-status").val()
            })
        };
        showResult(settings);
    },
    deleteById: function () {
        var settings = {
            type: "DELETE",
            url: "/management/goodsCategory/delete/" + $("#goodsCategory-delete-id").val(),
            dataType: "json"
        };
        showResult(settings);
    }
};

var goodsModule= {
    list: function () {
        var settings = {
            type: "POST",
            url: "/management/goods/list",
            dataType: "json",
            data: JSON.stringify({
                "content": $("#goods-list-content").val(),
                "categoryId1": $("#goods-list-categoryId1").val(),
                "status": $("#goods-list-status").val(),
                "pageIndex": $("#goods-list-pageIndex").val(),
                "pageSize": $("#goods-list-pageSize").val()
            })
        };
        showResult(settings);
    },
    listApi: function () {
        var settings = {
            type: "POST",
            url: "/api/goods/list",
            dataType: "json",
            data: JSON.stringify({
                "content": $("#goods-api-list-content").val(),
                "categoryId1": $("#goods-api-list-categoryId1").val(),
                "pageIndex": $("#goods-api-list-pageIndex").val(),
                "pageSize": $("#goods-api-list-pageSize").val()
            })
        };
        showResult(settings);
    },
    detail: function () {
        var settings = {
            type: "GET",
            url: "/management/goods/" + $("#goods-detail-id").val(),
            dataType: "json"
        };
        showResult(settings);
    },
    detailApi: function () {
        var settings = {
            type: "GET",
            url: "/api/goods/" + $("#goods-api-detail-id").val(),
            dataType: "json"
        };
        showResult(settings);
    },
    imageApi: function () {
        var url = window.location.href;
        var arr = url.split("/");
        var result = arr[0] + "//" + arr[2];
        window.open(
            result +"/api/goods/image/" + $("#goods-api-image-fileName").val(),
            '_blank' // <- This is what makes it open in a new window.
        );
        /*var settings = {
            type: "GET",
            url: ,
            dataType: "json"
        };
        showResult(settings);*/
    },
    create: function () {
        var settings = {
            type: "POST",
            url: "/management/goods/create",
            dataType: "json",
            data: JSON.stringify({
                "categoryId1": $("#goods-create-categoryId1").val(),
                "category1": $("#goods-create-category1").val(),
                "name": $("#goods-create-name").val(),
                "description": $("#goods-create-description").val(),
                "imagesUrl": $("#goods-create-imagesUrl").val(),
                "price": $("#goods-create-price").val()
            })
        };
        showResult(settings);
    },
    update: function () {
        var settings = {
            type: "PUT",
            url: "/management/goods/update",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#goods-update-id").val(),
                "categoryId1": $("#goods-update-categoryId1").val(),
                "category1": $("#goods-update-category1").val(),
                "name": $("#goods-update-name").val(),
                "description": $("#goods-update-description").val(),
                "imagesUrl": $("#goods-update-imagesUrl").val(),
                "price": $("#goods-update-price").val()
            })
        };
        showResult(settings);
    },
    updateStatus: function () {
        var settings = {
            type: "PUT",
            url: "/management/goods/resetStatus",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#goods-status-id").val(),
                "status": $("#goods-status-status").val()
            })
        };
        showResult(settings);
    },
    deleteById: function () {
        var settings = {
            type: "DELETE",
            url: "/management/goods/delete/" + $("#goods-delete-id").val(),
            dataType: "json"
        };
        showResult(settings);
    },
    imageManagement: function () {
        var formData = new FormData();
        formData.append('file', $('#goods-m-image-file')[0].files[0]);
        formData.append('fileName', $('#goods-m-image-fileName').val());
        $.ajax({
            url: '/management/goods/upload',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false
        })
    },
    imageGetM: function () {
        var url = window.location.href;
        var arr = url.split("/");
        var result = arr[0] + "//" + arr[2];
        window.open(
            result +"/management/goods/image/" + $("#goods-mG-image-fileName").val(),
            '_blank' // <- This is what makes it open in a new window.
        );
        /*var settings = {
            type: "GET",
            url: ,
            dataType: "json"
        };
        showResult(settings);*/
    }
};

var orderModule= {
    list: function () {
        var settings = {
            type: "POST",
            url: "/management/order/list",
            dataType: "json",
            data: JSON.stringify({
                "content": $("#order-list-content").val(),
                "status": $("#order-list-status").val(),
                "pageIndex": $("#order-list-pageIndex").val(),
                "pageSize": $("#order-list-pageSize").val()
            })
        };
        showResult(settings);
    },
    listApi: function () {
        var settings = {
            type: "POST",
            url: "/api/order/list",
            dataType: "json",
            data: JSON.stringify({
                "content": $("#order-api-list-content").val(),
                "status": $("#order-api-list-status").val(),
                "pageIndex": $("#order-api-list-pageIndex").val(),
                "pageSize": $("#order-api-list-pageSize").val()
            })
        };
        showResult(settings);
    },
    detail: function () {
        var settings = {
            type: "GET",
            url: "/management/order/" + $("#order-detail-id").val(),
            dataType: "json"
        };
        showResult(settings);
    },
    detailApi: function () {
        var settings = {
            type: "GET",
            url: "/api/order/" + $("#order-api-detail-id").val(),
            dataType: "json"
        };
        showResult(settings);
    },
    createApi: function () {
        var jsonObject = {
            "amount": $("#order-api-create-amount").val(),
            "recharge": $("#order-api-create-recharge").val(),
            "remark": $("#order-api-create-remark").val(),
            "list":[]
        };
        if($("#order-api-create-hasItem").val() == '1'){
            jsonObject.list.push({
                "goodsId": $("#order-api-create-goodsId").val(),
                "goodsName": $("#order-api-create-goodsName").val(),
                "price": $("#order-api-create-price").val(),
                "count": $("#order-api-create-count").val()
            })
        }
        var settings = {
            type: "POST",
            url: "/api/order/create",
            dataType: "json",
            data: JSON.stringify(jsonObject)
        };
        showResult(settings);
    },
    updateStatus: function () {
        var settings = {
            type: "PUT",
            url: "/management/order/resetStatus",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#order-status-id").val(),
                "status": $("#order-status-status").val(),
                "cancelReason": $("#order-status-cancelReason").val(),
                "remarks": $("#order-status-remarks").val()
            })
        };
        showResult(settings);
    },
    paid: function () {
        var settings = {
            type: "PUT",
            url: "/api/order/paid",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#order-api-paid-id").val()
            })
        };
        showResult(settings);
    }
};

var customerMaModule= {
    list: function () {
        var settings = {
            type: "POST",
            url: "/management/customer/list",
            dataType: "json" ,
            data: JSON.stringify({
                "managementContent": $("#customer-m-list-managementContent").val(),
                "type": $("#customer-m-list-status").val(),
                "pageIndex": $("#customer-m-List-startIndex").val(),
                "pageSize": $("#customer-m-List-pageSize").val()
            })
        };
        showResult(settings);
    },
    deleteById: function () {
        var settings = {
            type: "GET",
            url: "/management/customer/" + $("#customer-m-detail-id").val(),
            dataType: "json"
        };
        showResult(settings);
    },
    resetLoginPasswordById: function () {
        var settings = {
            type: "PUT",
            url: "/management/customer/loginPassword",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-resetLoginPassword-id").val()
            })
        };
        showResult(settings);
    },
    resetPaymentPassword: function () {
        var settings = {
            type: "PUT",
            url: "/management/customer/paymentPassword",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-resetPaymentPassword-id").val()
            })
        };
        showResult(settings);
    },
    resetStatus: function () {
        var settings = {
            type: "PUT",
            url: "/management/customer/resetBusinessStatus",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-status-id").val(),
                "businessStatus": $("#customer-m-status-businessStatus").val()
            })
        };
        showResult(settings);
    },
    resetBusiness: function () {
        var settings = {
            type: "PUT",
            url: "/management/customer/resetBusiness",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-business-id").val(),
                "businessName": $("#customer-m-business-businessName").val()
            })
        };
        showResult(settings);
    },
    businessVerify: function () {
        var settings = {
            type: "PUT",
            url: "/management/customer/verifyBusiness",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-businessVerify-id").val(),
                "status": $("#customer-m-businessVerify-status").val()
            })
        };
        showResult(settings);
    },
    resetLevel: function () {
        var settings = {
            type: "PUT",
            url: "/management/customer/resetLevel",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-level-id").val(),
                "level": $("#customer-m-level-level").val()
            })
        };
        showResult(settings);
    },
    customerParent: function () {
        var settings = {
            type: "GET",
            url: "/management/customer/customerParent/"+ $("#customer-m-customerParent-id").val(),
            dataType: "json"
        };
        showResult(settings);
    },
    customerLowList: function () {
        var settings = {
            type: "POST",
            url: "/management/customer/customerLowList",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-customerLowList-id").val(),
                "pageIndex": $("#customer-m-customerLowList-startIndex").val(),
                "pageSize": $("#customer-m-customerLowList-pageSize").val()
            })
        };
        showResult(settings);
    },
    addReduceGold: function () {
        var settings = {
            type: "POST",
            url: "/management/goldRecord/customerAddReduceGold",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-addReduceGold-id").val(),
                "type": $("#customer-m-addReduceGold-type").val(),
                "amount": $("#customer-m-addReduceGold-amount").val()
            })
        };
        showResult(settings);
    },
    businessAddReduceGold: function () {
        var settings = {
            type: "POST",
            url: "management/goldRecord/businessAddGoldRecord",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#business-m-addReduceGold-id").val(),
                "type": $("#business-m-addReduceGold-type").val(),
                "amount": $("#business-m-addReduceGold-amount").val()
            })
        };
        showResult(settings);
    },
    addReduceOilDrill: function () {
        var settings = {
            type: "POST",
            url: "management/oilDrillRecord/customerAddReduceOil",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-addReduceOilDrill-id").val(),
                "type": $("#customer-m-addReduceOilDrill-type").val(),
                "amount": $("#customer-m-addReduceOilDrill-amount").val()
            })
        };
        showResult(settings);
    },
    businessAddReduceOil: function () {
        var settings = {
            type: "POST",
            url: "management/oilDrillRecord/businessAddGoldOil",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#business-m-businessAddReduceOil-id").val(),
                "type": $("#business-m-businessAddReduceOil-type").val(),
                "amount": $("#business-m-businessAddReduceOil-amount").val()
            })
        };
        showResult(settings);
    },
    becomeVip: function () {
        var settings = {
            type: "put",
            url: "/management/customer/becomeVip",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#customer-m-becomeVip-id").val()
            })
        };
        showResult(settings);
    },
    customerGoldList: function () {
        var settings = {
            type: "POST",
            url: "/management/goldRecord/customerList",
            dataType: "json",
            data: JSON.stringify({
                "customerId": $("#customer-m-GoldList-customerId").val(),
                "pageIndex": $("#customer-m-GoldList-startIndex").val(),
                "pageSize": $("#customer-m-GoldList-pageSize").val()
            })
        };
        showResult(settings);
    },
    businessGoldList: function () {
        var settings = {
            type: "POST",
            url: "/management/goldRecord/businessList",
            dataType: "json",
            data: JSON.stringify({
                "businessId": $("#business-m-GoldList-businessId").val(),
                "pageIndex": $("#business-m-GoldList-startIndex").val(),
                "pageSize": $("#business-m-GoldList-pageSize").val()
            })
        };
        showResult(settings);
    },
    customerOilDrillList: function () {
        var settings = {
            type: "POST",
            url: "/management/oilDrillRecord/customerList",
            dataType: "json",
            data: JSON.stringify({
                "customerId": $("#customer-m-oilDrillList-customerId").val(),
                "pageIndex": $("#customer-m-oilDrillList-startIndex").val(),
                "pageSize": $("#customer-m-oilDrillList-pageSize").val()
            })
        };
        showResult(settings);
    },
    businessOilDrillList: function () {
        var settings = {
            type: "POST",
            url: "/management/oilDrillRecord/businessList",
            dataType: "json",
            data: JSON.stringify({
                "businessId": $("#business-m-oilDrillList-businessId").val(),
                "pageIndex": $("#business-m-oilDrillList-startIndex").val(),
                "pageSize": $("#business-m-oilDrillList-pageSize").val()
            })
        };
        showResult(settings);
    },
    customerCoinList: function () {
        var settings = {
            type: "POST",
            url: "/management/coinRecord/list",
            dataType: "json",
            data: JSON.stringify({
                "customerId": $("#customer-m-coinList-customerId").val(),
                "pageIndex": $("#customer-m-coinList-startIndex").val(),
                "pageSize": $("#customer-m-coinList-pageSize").val()
            })
        };
        showResult(settings);
    },
};

var goldRecordMaModule= {
    list: function () {
        var settings = {
            type: "POST",
            url: "/management/goldRecord/cashList",
            dataType: "json",
            data: JSON.stringify({
                "phone": $("#goldRecord-m-list-phone").val(),
                "status": $("#goldRecord-m-list-status").val(),
                "pageIndex": $("#goldRecord-m-List-startIndex").val(),
                "pageSize": $("#goldRecord-m-List-pageSize").val()
            })
        };
        showResult(settings);
    },
    moneyHandle: function () {
        var settings = {
            type: "POST",
            url: "/management/goldRecord/moneyHandle",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#goldRecord-m-moneyHandle-id").val()
            })
        };
        showResult(settings);
    },
    resetStatus: function () {
        var settings = {
            type: "PUT",
            url: "/management/goldRecord/resetStatus",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#goldRecord-m-resetStatus-id").val(),
                "status": $("#goldRecord-m-resetStatus-status").val(),
                "refuseReason": $("#goldRecord-m-resetStatus-refuseReason").val()
            })
        };
        showResult(settings);
    },

};

var oilDrillRecordMaModule= {
    list: function () {
        var settings = {
            type: "POST",
            url: "/management/oilDrillRecord/cashList",
            dataType: "json",
            data: JSON.stringify({
                "phone": $("#oilDrillRecord-m-list-phone").val(),
                "status": $("#oilDrillRecord-m-list-status").val(),
                "pageIndex": $("#oilDrillRecord-m-List-startIndex").val(),
                "pageSize": $("#oilDrillRecord-m-List-pageSize").val()
            })
        };
        showResult(settings);
    },
    moneyHandle: function () {
        var settings = {
            type: "POST",
            url: "/management/oilDrillRecord/moneyHandle",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#oilDrillRecord-m-moneyHandle-id").val()
            })
        };
        showResult(settings);
    },
    resetStatus: function () {
        var settings = {
            type: "PUT",
            url: "/management/oilDrillRecord/resetStatus",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#oilDrillRecord-m-resetStatus-id").val(),
                "status": $("#oilDrillRecord-m-resetStatus-status").val(),
                "refuseReason": $("#oilDrillRecord-m-resetStatus-refuseReason").val()
            })
        };
        showResult(settings);
    },
};
var coinRecordMaModule= {
    list: function () {
        var settings = {
            type: "POST",
            url: "/management/coinRecord/cashList",
            dataType: "json",
            data: JSON.stringify({
                "phone": $("#coinRecord-m-list-phone").val(),
                "status": $("#coinRecord-m-list-status").val(),
                "pageIndex": $("#coinRecord-m-List-startIndex").val(),
                "pageSize": $("#coinRecord-m-List-pageSize").val()
            })
        };
        showResult(settings);
    },
    moneyHandle: function () {
        var settings = {
            type: "POST",
            url: "/management/coinRecord/moneyHandle",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#coinRecord-m-moneyHandle-id").val()
            })
        };
        showResult(settings);
    },
    resetStatus: function () {
        var settings = {
            type: "PUT",
            url: "/management/coinRecord/resetStatus",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#coinRecord-m-resetStatus-id").val(),
                "status": $("#coinRecord-m-resetStatus-status").val(),
                "refuseReason": $("#coinRecord-m-resetStatus-refuseReason").val()
            })
        };
        showResult(settings);
    },
};
var systemApiParamsModule= {
    list: function () {
        var settings = {
            type: "GET",
            url: "/api/systemParams/list",
            dataType: "json"
        };
        showResult(settings);
    },
};