<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-8-12
  Time: 上午9:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>系统参数设置</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

    <style type="text/css">
        span {
            word-wrap: break-word;
        }
    </style>

    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/systemConfig/list',
                title: '系统参数设置',
                fit: true,
                fitColumns: true,
                singleSelect: true,
                columns: [[
                    {field: 'code', checkbox: 'true', align: 'center'},
                    {field: 'configName', title: '属性名', width: 100, align: 'center'},
                    {field: 'configValue', title: '属性值', width: 100, align: 'center'},
                    {field: 'configType', title: '设置类型', width: 100, align: 'center'},
                    {field: 'configDesc', title: '描述', width: 100, align: 'center'},
                ]],
                toolbar: [{
                    iconCls: 'icon-edit',
                    text: '修改',
                    handler: function () {
                        editChatRoomPersonCount();
                    }
                }]
            });
        });


        function editChatRoomPersonCount() {
            var rows = $('#dg').datagrid('getSelections');
            if (rows.length <= 0) {
                $.messager.alert('提示', '请选中一条数据进行修改', 'warning');
                return;
            }
            var row = rows[0];
            var type = row.configType;
            switch (type) {
                case 2:
                    type = "app活动最小人数设置";
                    break;
            }
            $('#configId').val(row.configId);
            $('#type').html(type);
            $('#name').html(row.configName);
            $('#value').textbox('setValue', row.configValue);
            $('#desc').textbox('setValue', row.configDesc);
            $('#edit-win').dialog({
                width: 330,
                height: 250,
                title: '修改',
                buttons: [{
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#edit-form').form('submit', {
                            url: '${basePath}/admin/api/systemConfig/edit',
                            type: 'post',
                            dataType: 'text',
                            onSubmit: function () {
                                return $('#edit-form').form('validate');
                            },
                            success: function (data) {
                                data = $.parseJSON(data);
                                if (data.success) {
                                    $('#edit-win').dialog('close');
                                    $('#dg').datagrid('reload');
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    });
                                    return;
                                }
                                $.messager.alert('提示', data.msg, 'error');
                            }
                        });
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#edit-win').dialog('close');
                    }
                }]
            });
        }
    </script>

</head>
<body>

<table id="dg"></table>

<div id="edit-win">
    <form id="edit-form">
        <input type="hidden" name="systemConfigId" id="configId">
        <div style="width: 80%;margin: auto">
            <div style="margin-top: 10px">类型：<span id="type"></span></div>
            <div style="margin-top: 10px">属性名：<span id="name"></span></div>
            <div style="margin-top: 10px">
                属性值：<input class="easyui-textbox" id="value" name="configValue" required>
            </div>
            <div style="margin-top: 10px">
                描 述：<input class="easyui-textbox"
                           data-options="multiline:true" name="configDesc"
                           style="height: 50px" id="desc" required>
            </div>
        </div>
    </form>
</div>

</body>
</html>
