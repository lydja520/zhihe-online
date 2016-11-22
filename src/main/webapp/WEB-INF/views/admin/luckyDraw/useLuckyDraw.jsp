<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>奖品使用</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath("${basePath}");
        app.setup();
    </script>
    <script type="text/javascript" src="${basePath}/static/core/utils.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#data-dialog').dialog({
                width: 380,
                height: 200,
                title: '奖品验证',
                module: true,
                closable: false,
                closed: false,
                iconCls: 'icon-tip',
                draggable: false,
                border: false,
                buttons: [{
                    text: '清空',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#data-form').form('reset');
                    }
                }, {
                    text: '验证',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var info = parseFormJSON('data-form');
                        if (!$.trim(info.luckyCode) || !$.trim(info.mobileNo)) {
                            $.messager.alert('提示', '奖品号码或手机号不能为空！', 'info');
                            return;
                        }
                        $.messager.confirm('提示', '您确定要使用此奖品吗，请先确认此奖品是否已过期？', function (r) {
                            if (r) {
                                useLuckyDraw();
                            }
                        });
                    }
                }]
            });
        });

        function useLuckyDraw() {
            var info = parseFormJSON('data-form');
            $.messager.progress();
            $.ajax({
                url: '${basePath}/admin/api/luckyDraws/use',
                type: 'POST',
                dataType: 'text',
                data: info,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.code == 200) {
                        $.messager.show({
                            title: '提示',
                            msg: '验证成功！'
                        });
                    } else {
                        $.messager.alert('提示', data.msg, "error");
                    }
                },
                error: function (XHR, error) {
                    $.messager.alert('提示', '验证失败请重试！', "error");
                },
                complete: function () {
                    $.messager.progress('close');
                }
            });
        }
    </script>
</head>
<body>
<div id="data-dialog">
    <form id="data-form">
        <table class="form-table">
            <tr>
                <td>奖品号：</td>
                <td>
                    <input id="lucky-code" class="easyui-validatebox" data-options="required:true" name="luckyCode"
                           style="width: 240px;height: 28px">
                </td>
            </tr>
            <tr>
                <td>中奖号码：</td>
                <td>
                    <input id="mobile-no" class="easyui-validatebox" data-options="required:true" name="mobileNo"
                           style="width: 240px;height: 28px">
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
