$.json = function (option) {
    var config = {
        dataType: 'json',
        type: 'get',
        data: {}
    };
    $.extend(config, option);

    config.success = function (data) {
        if (typeof option.success == 'function')
            option.success(data);
    };
    config.complete = function (data) {
        loading.hide()
        if (typeof option.complete == 'function')
            option.complete(data);
    };
    config.error = function (data) {
        weui.topTips('服务正忙,请稍后再试');
        if (typeof option.error == 'function')
            option.error(data);
    };

    var loading = weui.loading('loading', {
        content: '请稍后...'
    });

    //真正发起请求
    $.ajax(config);

}

function autoFillData(container, data) {
    if (!data)
        return;

    $(':input:not(:radio,:checkbox)', $(container)).each(function (i, o) {
        var name = this.name;
        if (name && data[name] + '') {
            $(this).val(data[name]);
        }
    });

    $(':radio', $(container)).each(function (i, o) {
        var name = this.name;
        this.checked = false;
        if (name && (data[name] + '') && this.value == data[name]) {
            this.checked = true;
        }
    });
    $(':checkbox', $(container)).each(function (i, o) {
        var name = this.name;
        this.checked = false;
        if (name && data[name] && (data[name]+'').split(',').indexOf(this.value) > -1) {
            this.checked = true;
        }
    });


}

function initSelectOption(option) {
    if (!option.data) {
        $(option.container).html('');
        return;
    }
    option = $.extend({"value": "value", "name": "name", "nullDesc": "--请选择--"}, option);
    var html = '';
    if (option.needNull) html += '<option value="">' + option.nullDesc + '</option>';
    $.each(option.data, function (i, o) {
        html += '<option value="' + o[option.value] + '" ' + (option.defaultVal && o[option.value] == option.defaultVal ? "selected" : "") + '>' + o[option.name] + '</option>';
    });
    if (option.append) {
        $(option.container).append(html);
    } else {
        $(option.container).html(html);
    }
    if (option.callback && typeof option.callback == 'function') {
        option.callback();
    }
}


//给指定的select容器通过url返回的数组初始化数据
function initSelect(option) {
    option = $.extend({"needNull": true, "queryParam": {}, "append": false}, option);
    $.json({
        url: option.url,
        data: option.queryParam,
        success: function (data) {
            if (data && data.code == '200') {
                option.data = data.data;
                initSelectOption(option);
            } else {
                weui.topTips(data.msg);
            }
        }
    });
}

function getContextPath(fullUrl) {
    if (fullUrl == null || fullUrl == '') {
        fullUrl = window.location.href + '';
    }
    var arrUrl = fullUrl.split('/');
    return arrUrl[0] + '//' + arrUrl[2] + '/' + arrUrl[3];
}

$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};