/**
 * Created by ShenYunjie on 2015/11/30.
 */

/**
 * 数据datagrid数据加载失败是调用
 */
function onGridLoadError(data) {
    if (data && data.responseJSON) {
        try {
            var response = data.responseJSON;
            if (response.msg) {
                $.messager.alert('提示', response.msg, 'error');
                return;
            }
        } catch (e) {
            console.error(e);
        }
    }
    $.messager.alert('提示', '数据加载失败，请重试！', 'error');
}

/**
 * 操作失败是调用接口
 */
function onExecutError(msg) {
    var _msg = msg || '系统出错了，请重试或联系管理';
    $.messager.alert('提示', _msg, 'error');
}

/**
 * 操作datagrid成功后提示
 * @param data  服务器返回数据
 * @param target 操作的datagrid
 * @param successCallback 如果成功调用的回调函
 */
function onAfterEdit(data, targetGrid, successCallback) {
    data = $.parseJSON(data);
    if (data.success) {
        $.messager.show({
            title: '提示',
            msg: data.msg
        })
        if (targetGrid) {
            $('#' + targetGrid).datagrid('load');
        }
        if (successCallback) {
            successCallback();
        }
    }
    else {
        $.messager.alert('提示', data.msg, 'error');
    }
}

function request(param) {
    var settings = {
        url: param.url,
        type: param.type || "POST",
        dataType: param.dataType || "text",
        data: param.data,
        error: function (jqXHR, textStatus, errorThrown) {
            if (param.error) {
                param.error(jqXHR, textStatus, errorThrown);
            }
        },
        success: function (data, textStatus) {
            if (this.dataType == "text") {
                data = $.parseJSON(data);
            }
            if (param.success) {
                param.success(data, textStatus);
            }
        },
        complete: function (jqXHR, textStatus) {
            if (param.complete) {
                param.complete(jqXHR, textStatus);
            }
        },
        headers: {
            //"token": $.cookie("token")
            token: '111111111',
            userId: '111111111111'
        }
    };
    $.ajax(settings);
};

/**
 * 将表单数据解析为JSON数据,不能有数组
 * @param formId
 * @returns {{}}
 */
function parseFormJSON(formId) {
    var data = {};
    var array = $('#' + formId).serialize().split('&');
    if (!array || array.length < 1) {
        return data;
    }
    $(array).each(function (index, element) {
        var _array = element.split('=');
        data[_array[0]] = _array[1];
    });
    return data;
}

/**
 * 是否是安卓终端
 * @returns {boolean}
 */
function isAndroid() {
    var u = navigator.userAgent;
    return u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
}

/**
 * 是否是IOS终端
 * @returns {boolean}
 */
function isIOS() {
    var u = navigator.userAgent;
    return !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
}
