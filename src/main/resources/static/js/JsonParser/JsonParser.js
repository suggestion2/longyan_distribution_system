function JsonParser(jsonContent) {
    this.jsonContent = jsonContent;
    this.dateObj = new Date();
    this.regexpObj = new RegExp();
    this.id = "resultShow";
    this.thisObj = "jsonParser";
    this.defaultHeight = 1000; //Json页面初始化最大高度
    //页面可控参数
    this.content = null; //Json显示结果
    this.tab = "    ";
    this.quoteKeys = true;
    this.isCollapsible = true;
}

JsonParser.prototype = {
    init: function () {
        document.getElementById(this.id).innerHTML = this.buildHtml();
        this.process();
        this.initHeight();
    },

    process: function () {
        this.content = document.getElementById(this.id + "Canvas");
        var html = "";
        try {
            if (this.jsonContent == "") this.jsonContent = "\"\"";
            var obj = eval("[" + this.jsonContent + "]");
            html = this.processObject(obj[0], 0, false, false, false);
            this.content.innerHTML = "<PRE class='CodeContainer'>" + html + "</PRE>";
        }
        catch (e) {
            this.content.innerHTML = "JSON data is not validated:\n" + e.message;
        }
    },

    initHeight: function (obj) {
        for (var i = 8; i > 1; i--) {
            if (document.getElementById(this.id + "Canvas").offsetHeight > this.defaultHeight) {
                this.collapseLevel(i);
            }
            else break;
        }
    },

    isArray: function (obj) {
        return obj && typeof obj === 'object' && typeof obj.length === 'number' && !(obj.propertyIsEnumerable('length'));
    },

    buildHtml: function () {
        var html = [];
        html.push('<div class="JsonParser">');
        html.push('    <div class="ControlsRow">');
//        html.push('        <span class="TabSizeHolder">Indent');
//        html.push('            <select id="' + this.id + 'TabSize" style="width:auto;" onchange="' + this.thisObj + '.tabSizeChanged()">');
//        html.push('                <option value="1">1</option>');
//        html.push('                <option value="2" selected="true">2</option>');
//        html.push('                <option value="3">3</option>');
//        html.push('                <option value="4">4</option>');
//        html.push('                <option value="5">5</option>');
//        html.push('                <option value="6">6</option>');
//        html.push('            </select>');
//        html.push('        </span>');
//        html.push('        <label for="' + this.id + 'quoteKeys" style="display:inline;">');
//        html.push('            <input type="checkbox" id="' + this.id + 'quoteKeys" onclick="' + this.thisObj + '.quoteKeysClicked()" checked="check" />');
//        html.push('            Quotes');
//        html.push('        </label>');
//        html.push('        <label for="' + this.id + 'CollapsibleView" style="display:inline;">');
//        html.push('            <input type="checkbox" id="' + this.id + 'CollapsibleView" onclick="' + this.thisObj + '.collapsibleViewClicked()" checked="check" />');
//        html.push('            Show Control');
//        html.push('        </label>');
        html.push('        <span id="' + this.id + 'CollapsibleViewDetail">');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.selectAllClicked()">Select All</a>&nbsp;');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.expandAllClicked()">Expand</a>&nbsp;');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.collapseAllClicked()">Collapse</a>&nbsp;');
        html.push('            Level: ');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.collapseLevel(2)">2</a>');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.collapseLevel(3)">3</a>');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.collapseLevel(4)">4</a>');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.collapseLevel(5)">5</a>');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.collapseLevel(6)">6</a>');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.collapseLevel(7)">7</a>');
        html.push('            <a href="javascript:void(0);" onclick="' + this.thisObj + '.collapseLevel(8)">8</a>&nbsp;');
        html.push('        </span>');
        html.push('    </div>');
        html.push('    <div id="' + this.id + 'Canvas" class="Canvas"></div>');
        html.push('</div>');

        return html.join('');
    },

    processObject: function (obj, indent, addComma, isArray, isPropertyContent) {
        var html = "";
        var comma = (addComma) ? "<span class='Comma'>,</span> " : "";
        var type = typeof obj;
        var clpsHtml = "";
        if (this.isArray(obj)) {
            if (obj.length == 0) {
                html += this.getRow(indent, "<span class='ArrayBrace'>[ ]</span>" + comma, isPropertyContent);
            }
            else {
                clpsHtml = this.isCollapsible ? "<span class='Minus' onClick=\"" + this.thisObj + ".expandClicked(this)\"></span><span class='collapsible'>" : "";
                html += this.getRow(indent, "<span class='ArrayBrace'>[</span>" + clpsHtml, isPropertyContent);
                for (var i = 0; i < obj.length; i++) {
                    html += this.processObject(obj[i], indent + 1, i < (obj.length - 1), true, false);
                }
                clpsHtml = this.isCollapsible ? "</span>" : "";
                html += this.getRow(indent, clpsHtml + "<span class='ArrayBrace'>]</span>" + comma);
            }
        }
        else if (type == 'object') {
            if (obj == null) {
                html += this.formatLiteral("null", "", comma, indent, isArray, "Null");
            }
            else if (obj.constructor == this.dateObj.constructor) {
                html += this.formatLiteral("new Date(" + obj.getTime() + ") /*" + obj.toLocaleString() + "*/", "", comma, indent, isArray, "Date");
            }
            else if (obj.constructor == this.regexpObj.constructor) {
                html += this.formatLiteral("new RegExp(" + obj + ")", "", comma, indent, isArray, "RegExp");
            }
            else {
                var numProps = 0;
                for (var prop in obj) numProps++;
                if (numProps == 0) {
                    html += this.getRow(indent, "<span class='ObjectBrace'>{ }</span>" + comma, isPropertyContent);
                }
                else {
                    clpsHtml = this.isCollapsible ? "<span class='Minus' onClick=\"" + this.thisObj + ".expandClicked(this)\"></span><span class='collapsible'>" : "";
                    html += this.getRow(indent, "<span class='ObjectBrace'>{</span>" + clpsHtml, isPropertyContent);
                    var j = 0;
                    for (var prop in obj) {
                        var quote = this.quoteKeys ? "\"" : "";
                        html += this.getRow(indent + 1, "<span class='PropertyName'>" + quote + prop + quote + "</span>: " + this.processObject(obj[prop], indent + 1, ++j < numProps, false, true));
                    }
                    clpsHtml = this.isCollapsible ? "</span>" : "";
                    html += this.getRow(indent, clpsHtml + "<span class='ObjectBrace'>}</span>" + comma);
                }
            }
        }
        else if (type == 'number') {
            html += this.formatLiteral(obj, "", comma, indent, isArray, "Number");
        }
        else if (type == 'boolean') {
            html += this.formatLiteral(obj, "", comma, indent, isArray, "Boolean");
        }
        else if (type == 'function') {
            if (obj.constructor == this.regexpObj.constructor) {
                html += this.formatLiteral("new RegExp(" + obj + ")", "", comma, indent, isArray, "RegExp");
            }
            else {
                obj = this.formatFunction(indent, obj);
                html += this.formatLiteral(obj, "", comma, indent, isArray, "Function");
            }
        }
        else if (type == 'undefined') {
            html += this.formatLiteral("undefined", "", comma, indent, isArray, "Null");
        }
        else {
            html += this.formatLiteral(obj.toString().split("\\").join("\\\\").split('"').join('\\"'), "\"", comma, indent, isArray, "String");
        }
        return html;
    },

    formatLiteral: function (literal, quote, comma, indent, isArray, style) {
        if (typeof literal == 'string')
            literal = literal.split("<").join("&lt;").split(">").join("&gt;");
        var str = "<span class='" + style + "'>" + quote + literal + quote + comma + "</span>";
        if (isArray) str = this.getRow(indent, str);
        return str;
    },

    formatFunction: function (indent, obj) {
        var tabs = "";
        for (var i = 0; i < indent; i++) tabs += this.tab;
        var funcStrArray = obj.toString().split("\n");
        var str = "";
        for (var i = 0; i < funcStrArray.length; i++) {
            str += ((i == 0) ? "" : tabs) + funcStrArray[i] + "\n";
        }
        return str;
    },

    getRow: function (indent, data, isPropertyContent) {
        var tabs = "";
        for (var i = 0; i < indent && !isPropertyContent; i++) tabs += this.tab;
        if (data != null && data.length > 0 && data.charAt(data.length - 1) != "\n")
            data = data + "\n";
        return tabs + data;
    },

    collapsibleViewClicked: function () {
        this.isCollapsible = document.getElementById(this.id + "CollapsibleView").checked;
        document.getElementById(this.id + "CollapsibleViewDetail").style.visibility = this.isCollapsible ? "visible" : "hidden";
        this.process();
    },

    quoteKeysClicked: function () {
        this.quoteKeys = document.getElementById(this.id + "quoteKeys").checked;
        this.process();
    },

    collapseAllClicked: function () {
        var $this = this;
        this.traverseChildren(this.content,
        function (element) {
            if (element.className == 'collapsible') {
                $this.makeContentVisible(element, false);
            }
        },
        0);
    },

    expandAllClicked: function () {
        var $this = this;
        this.traverseChildren(this.content,
        function (element) {
            if (element.className == 'collapsible') {
                $this.makeContentVisible(element, true);
            }
        },
        0);
    },

    makeContentVisible: function (element, visible) {
        element.style.display = visible ? 'inline' : 'none';
        element.previousSibling.className = visible ? "Minus" : "Plus";
    },

    traverseChildren: function (element, func, depth) {
        for (var i = 0; i < element.childNodes.length; i++) {
            this.traverseChildren(element.childNodes[i], func, depth + 1);
        }
        func(element, depth);
    },

    expandClicked: function (element) {
        var container = element.nextSibling;
        if (!container) return;
        if (container.style.display == "none") {
            container.style.display = "inline";
            element.className = "Minus";
        }
        else {
            container.style.display = "none";
            element.className = "Plus";
        }
    },

    collapseLevel: function (level) {
        var $this = this;
        this.traverseChildren(this.content,
        function (element, depth) {
            if (element.className == 'collapsible') {
                if (depth >= level + 1) {
                    $this.makeContentVisible(element, false);
                }
                else {
                    $this.makeContentVisible(element, true);
                }
            }
        },
        0);
    },

    tabSizeChanged: function () {
        var selectTab = document.getElementById(this.id + "TabSize");
        this.tab = this.multiplyString(parseInt(selectTab.options[selectTab.selectedIndex].value), "  ");
        this.process();
    },

    multiplyString: function (num, str) {
        var sb = [];
        for (var i = 0; i < num; i++) {
            sb.push(str);
        }
        return sb.join("");
    },

    selectAllClicked: function () {
        if (!!document.selection && !!document.selection.empty) {
            document.selection.empty();
        } else if (getSelection) {
            var sel = getSelection();
            if (sel.removeAllRanges) {
                getSelection().removeAllRanges();
            }
        }
        var range = (!!document.body && !!document.body.createTextRange) ? document.body.createTextRange() : document.createRange();
        if (!!range.selectNode)
            range.selectNode(this.content);
        else if (range.moveToElementText)
            range.moveToElementText(this.content);
        if (!!range.select)
            range.select(this.content);
        else
            getSelection().addRange(range);
    }
}