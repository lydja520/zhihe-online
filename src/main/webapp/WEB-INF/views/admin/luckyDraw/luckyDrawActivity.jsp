<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-4-21
  Time: 下午4:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>抽奖活动</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/luckyDrawActivity/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                columns: [[
                    {field: 'activityId', checkbox: true, width: 100, align: 'center'},
                    {field: 'activityName', title: '抽奖活动名称', width: 100, align: 'center'},
                    {field: 'desc', title: '活动简介', width: 100, align: 'center'},
                    {field: 'createDate', title: '创建时间', width: 100, align: 'center'},
                    {
                        field: 'permit', title: '是否上线', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return "<span style='color: green'>上线</span>";
                            }
                            return "<span style='color: red'>未上线</span>";
                        }
                    },
                    {
                        field: 'submitState', title: '是否能进行奖项编辑', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return "不能编辑";
                            }
                            return "能编辑";
                        }
                    }
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        $('#add-edit-win').dialog({
                            title: '添加一个抽奖活动',
                            width: 460,
                            buttons: [{
                                text: '添加',
                                iconCls: 'icon-add',
                                handler: function () {
                                    $('#add-edit-form').form({
                                        url: '${basePath}/admin/api/luckyDrawActivity/add',
                                        success: function (info) {
                                            info = $.parseJSON(info);
                                            $.messager.show({
                                                title: '提示',
                                                msg: info.msg
                                            });
                                            if (info.success) {
                                                $('#add-edit-win').dialog('close');
                                                $('#dg').datagrid('load');
                                            }
                                        }
                                    });
                                    $('#add-edit-form').submit();
                                }
                            }, {
                                text: '取消',
                                iconCls: 'icon-cancel',
                                handler: function () {
                                    $('#add-edit-win').dialog('close');
                                }
                            }],
                            onBeforeOpen: function () {
                                $('#add-edit-form').form('clear');
                            }
                        });
                    }
                }, '-', {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var rows = $('#dg').datagrid('getChecked');
                        if (rows.length == 0) {
                            $.messager.alert('提示', '请选中一条数据进行操作', 'info');
                            return;
                        }
                        var luckDrawActivity = rows[0];
                        if (luckDrawActivity.permit) {
                            $.messager.alert('提示', '该活动已经上线，无法进行修改，如要修改，请先下线!', 'warning');
                            return;
                        }
                        $('#add-edit-win').dialog({
                            title: '修改一个抽奖活动',
                            width: 400,
                            buttons: [{
                                text: '修改',
                                iconCls: 'icon-add',
                                handler: function () {
                                    $('#add-edit-form').form({
                                        url: '${basePath}/admin/api/ludkyDrawActivity/update',
                                        success: function (info) {
                                            info = $.parseJSON(info);
                                            if (info.success) {
                                                $.messager.show({
                                                    title: '提示',
                                                    msg: info.msg
                                                });
                                                $('#add-edit-win').dialog('close');
                                                $('#dg').datagrid('load');
                                            } else {
                                                $.messager.alert('错误', info.msg, 'error');
                                                $('#add-edit-win').dialog('close');
                                            }
                                        }
                                    });
                                    $('#add-edit-form').submit();
                                }
                            }, {
                                text: '取消',
                                iconCls: 'icon-cancel',
                                handler: function () {
                                    $('#add-edit-win').dialog('close');
                                }
                            }],
                            onBeforeOpen: function () {
                                $('#add-edit-form').form('load', luckDrawActivity);
                            }
                        });
                    }
                }, '-', {
                    text: '奖项查看',
                    iconCls: 'icon-add2',
                    handler: function () {
                        var rows = $('#dg').datagrid('getChecked');
                        if (rows.length == 0) {
                            $.messager.alert('提示', '请选中一条数据进行操作', 'info');
                            return;
                        }
                        var luckDraw = rows[0];
                        $('#lucky-op-win').dialog({
                            title: '奖项设置操作',
                            width: 850,
                            height: 500,
                            modal: true,
                            onBeforeOpen: function () {
                                $('#lucky-op-win').attr('src', '${basePath}/admin/lkDrawAct/' + luckDraw.activityId + '/luckDraw');
                            }
                        });
                    }
                }, '-', {
                    text: '上线',
                    iconCls: 'icon-onsal',
                    handler: function () {
                        var rows = $('#dg').datagrid('getChecked');
                        if (rows.length == 0) {
                            $.messager.alert('提示', '请选中一条数据进行操作', 'info');
                            return;
                        }
                        var luckDrawActivity = rows[0];
                        if (luckDrawActivity.permit) {
                            $.messager.alert('提示', '该活动已经为上线状态！', 'info');
                            return;
                        }
                        $.messager.confirm('确认操作', '确定要上线该抽奖活动吗？', function (r) {
                            if (r) {
                                $.ajax({
                                    url: '${basePath}/admin/api/luckyDrawActivity/onlineOrOffline',
                                    type: 'post',
                                    dataType: 'text',
                                    data: {
                                        activityId: luckDrawActivity.activityId,
                                        permit: true
                                    },
                                    beforeSend: function () {
                                        $.messager.progress();
                                    },
                                    success: function (info) {
                                        info = $.parseJSON(info);
                                        $.messager.progress('close');
                                        if (info.success) {
                                            $.messager.show({
                                                title: '提示',
                                                msg: info.msg
                                            });
                                            $('#dg').datagrid('load');
                                        } else {
                                            $.messager.alert('错误', info.msg, 'error');
                                        }
                                    }
                                });
                            }
                        });
                    }
                }, '-', {
                    text: '下线',
                    iconCls: 'icon-notonsal',
                    handler: function () {
                        var rows = $('#dg').datagrid('getChecked');
                        if (rows.length == 0) {
                            $.messager.alert('提示', '请选中一条数据进行操作', 'info');
                            return;
                        }
                        var luckDrawActivity = rows[0];
                        if (!luckDrawActivity.permit) {
                            $.messager.alert('提示', '该活动已经为下线状态！', 'info');
                            return;
                        }
                        $.messager.confirm('确认操作', '确定要下线该抽奖活动吗？', function (r) {
                            if (r) {
                                $.ajax({
                                    url: '${basePath}/admin/api/luckyDrawActivity/onlineOrOffline',
                                    type: 'post',
                                    dataType: 'text',
                                    data: {
                                        activityId: luckDrawActivity.activityId,
                                        permit: false
                                    },
                                    success: function (info) {
                                        info = $.parseJSON(info);
                                        if (info.success) {
                                            $.messager.show({
                                                title: '提示',
                                                msg: info.msg
                                            });
                                            $('#dg').datagrid('load');
                                        } else {
                                            $.messager.alert('错误', info.msg, 'error');
                                        }
                                    }
                                });
                            }
                        });
                    }
                }]
            });
        });
    </script>
    <style type="text/css">
        ul, li {
            margin: 0px;
            padding: 0px;
            list-style: none;
        }

        .li-title {
            width: 30%;
            float: left;
            margin: 2px auto;
        }

        .li-content {
            width: 70%;
            float: left;
            margin: 2px auto;
        }

        #luck-draw-content li {
            width: 33%;
            float: left;
            margin: 2px auto;
        }

        .clear {
            clear: both;
        }
    </style>
</head>

<body>

<div id="dg"></div>

<div id="add-edit-win">
    <form id="add-edit-form" method="post">
        <input type="hidden" name="activityId">
        <p style="color: #FF4500;font-weight: bolder;text-align: center">抽奖基本信息</p>
        <ul style="border-top:solid thin #008000;width: 80%;margin: auto">
            <li class="li-title" style="margin-top: 7px">抽奖活动名:</li>
            <li class="li-content" style="margin-top: 7px">
                <input class="easyui-textbox" style="width: 100%" name="activityName">
            </li>
            <li class="li-title">抽奖描述:</li>
            <li class="li-content">
                <input class="easyui-textbox" data-options="multiline:true" style="height: 50px;width: 100%"
                       name="desc">
            </li>
            <div class="clear"></div>
        </ul>
    </form>
</div>

<iframe id="lucky-op-win"></iframe>

</body>
</html>
