<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/11/26
  Time: 10:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>职业管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(function () {
            /*========初始化职业信息数据网格========*/
            $('#occupation_datagrid').datagrid({
                url: '/admin/api/occupation/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect:true,
                columns: [[
                    {field: 'occupationId', checkbox:true,width: 100, align: 'center'},
                    {field: 'occName', title: '职业名', width: 100, align: 'center'},
                    {field: 'occDesc', title: '描述', width: 100, align: 'center'},
                    {field: 'createDate', title: '创建时间', width: 100, align: 'center'}
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        add();
                    }
                }, '-', {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        edit();
                    }
                }],

            });

        });

        /*========增加职业信息========*/
        function add() {
            $('#add_win').dialog({
                title: '添加记录',
                width: 400,
                modal: true,
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#add_form').form('submit', {
                            success: function (info) {
                                info = $.parseJSON(info);
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg
                                });
                                if (info.success) {
                                    $('#add_win').dialog('close');
                                    $('#occupation_datagrid').datagrid('load');
                                }
                            }
                        })
                    }
                }, {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#add_win').dialog('close');
                    }
                }]
            });
        }
        /*========修改选中的职业信息========*/
        function edit() {
            var rows = $('#occupation_datagrid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示','请选择一条数据');
                return;
            }
            $('#edit_win').dialog({
                modal: true,
                title: '修改',
                width: 600,
                buttons: [{
                    text: '修改',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#edit_form').form('submit', {
                            success: function (info) {
                                info = $.parseJSON(info);
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg
                                });
                                if (info.success) {
                                    $('#edit_win').dialog('close');
                                    $('#occupation_datagrid').datagrid('load');
                                }
                            }
                        })
                    }
                }, {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#edit_win').dialog('close');
                    }
                }]
            });
            $('#edit_form').form('reset');
            $('#edit_form').form('load', rows[0]);
        }
    </script>
</head>
<body>
<!--显示职业数据网格开始-->
<table id="occupation_datagrid"></table>
<!--显示职业数据网格开始-->

<!--增加职业信息开始-->
<div id="add_win">
    <form id="add_form" action="${basePath}/admin/api/occupation/add" method="post">
        <table align="center">
            <tr>
                <td>职业名称:</td>
                <td>
                    <input name="occName" class="easyui-textbox" data-options="required:true">
                </td>
            </tr>
            <tr>
                <td>职业描述:</td>
                <td><input name="occDesc" class="easyui-textbox" data-options="required:true"></td>
            </tr>
        </table>
    </form>
</div>
<!--增加职业信息结束-->

<!--修改职业信息开始-->
<div id="edit_win">
    <form id="edit_form" action="${basePath}/admin/api/occupation/edit" method="post">
        <table align="center">
            <tr>
                <td>职业名称:</td>
                <td>
                    <input name="occName" class="easyui-textbox" data-options="required:true">
                </td>
            </tr>
            <tr>
                <td>职业描述:</td>
                <td><input name="occDesc" class="easyui-textbox" data-options="required:true"></td>
            </tr>
        </table>
        <input name="occupationId" type="hidden"  data-options="required:true">
        <input name="createDate" type="hidden" data-options="required:true">
    </form>
</div>
<!--修改职业信息结束-->
</body>
</html>
