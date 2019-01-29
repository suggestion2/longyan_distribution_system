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
                "customerBankAccount": $("#customer-a-update-customerBankAccount").val()
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
    }
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
                "pageIndex": $("#customer-m-List-startIndex").val(),
                "pageSize": $("#customer-m-List-pageSize").val()
            })
        };
        showResult(settings);
    },
};