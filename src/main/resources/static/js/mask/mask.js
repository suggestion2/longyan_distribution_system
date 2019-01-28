(function ($) {
    $.fn.mask = function (options) {
        var opts = $.extend({}, $.fn.mask.defaults, options);

        $.fn.mask.show(opts);
    };
    //参数设置
    $.fn.mask.defaults = {
        type: "whole", //默认是整个页面的遮罩1.whole 2.append (追加模式使用局部小图标)
        toRender: $(document.body) //默认插入到document
    };

    //生成html
    $.fn.mask.render = function (opts) {
        var html = '';
        switch (opts.type) {
            case "whole":
                html = '<div id="mask" class="w_mask"><div class="w_center"></div></div>';
                $(opts.toRender).append(html);
                break;
            case "append":
                html = '<div id="mask" class="l_mask"></div>';
                $(opts.toRender).after(html);
                $("#mask").css("display", "inline-block");
                break;
        }
    };
    //show
    $.fn.mask.show = function (opts) {
        if ($("#mask").length > 0)
            $("#mask").remove();
        $.fn.mask.render(opts);
        $("#mask").show();
    };

    $.fn.mask.close = function () {
        $("#mask").remove();
    };

})(jQuery);

