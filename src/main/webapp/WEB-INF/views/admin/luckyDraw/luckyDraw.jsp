<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-4-22
  Time: 下午4:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <c:set var="luckDrawActId" value="${requestScope.lkDrawActId}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${basePath}/static/js/calculate.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/luckDraw/${luckDrawActId}/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                border: false,
                columns: [[
                    {field: 'luckyDrawId', checkbox: true, align: 'centrer'},
                    {
                        field: 'title', title: '奖项名', width: 50, align: 'center',
                        formatter: function (data) {
                            if (data == 0) {
                                return "未中奖";
                            }
                            return data;
                        }
                    },
                    {field: 'desc', title: '奖品描述', width: 200, align: 'center'},
                    {
                        field: 'amount', title: '奖品数量', width: 80, align: 'center',
                        formatter: function (data, row, index) {
                            if (row.ldOrder == 0) {
                                return "";
                            }
                            return data;
                        }
                    },
                    {
                        field: 'percentage', title: '所占百分比', width: 80, align: 'center',
                        formatter: function (data, row, index) {
                            return accMul(data, 100) + "%";
                        }
                    },
                    {
                        field: 'merchant', title: '赞助商家', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data == null) {
                                return "无";
                            }
                            return data.merchName;
                        }
                    },
                    {
                        field: 'ldOrder', title: '顺序', width: 100, align: 'center'
                    },
                    {
                        field: 'surplus', title: '还剩', width: 100, align: 'center',
                        formatter: function (data, row, index) {
                            if (row.ldOrder == 0) {
                                return "";
                            }
                            return data;
                        }
                    },
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        var editable;
                        $.ajax({
                            url: '${basePath}/admin/api/luckyDrawAct/${lkDrawActId}/isEditable',
                            type: 'get',
                            dataType: 'text',
                            async: false,
                            success: function (info) {
                                info = $.parseJSON(info);
                                editable = info.isEditable;
                            }
                        });
                        if (!editable) {
                            $.messager.alert('提示', '该抽将活动已经上过线，上过线的活动都不支持添加操作！');
                            return;
                        }
                        $('#add-edit-win').dialog({
                            title: '添加奖项',
                            width: 400,
                            modal: true,
                            buttons: [{
                                text: '保存',
                                iconCls: 'icon-save',
                                handler: function () {
                                    $('#add-edit-form').form({
                                        url: '${basePath}/admin/api/luckyDraw/add',
                                        success: function (info) {
                                            info = $.parseJSON(info);
                                            if (info.success) {
                                                $.messager.show({
                                                    title: '提示',
                                                    msg: info.msg
                                                });
                                                $('#dg').datagrid('load');
                                                $('#add-edit-win').dialog('close');
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
                                $('#add-edit-form').form('clear');
                                console.log('${luckDrawActId}')
                                $('#luckyDrawActivityId').val('${luckDrawActId}');
                                $.ajax({
                                    url: '${basePath}/admin/api/lkDrawAct/${lkDrawActId}/currentPercentage',
                                    dataType: 'text',
                                    type: 'get',
                                    success: function (info) {
                                        info = $.parseJSON(info);
                                        $('#percentage').numberspinner('setValue', accSub(1, info.percentage));
                                    }
                                });
                                $('#merchant').textbox({
                                    buttonText: '选择商家',
                                    iconAlign: 'left',
                                    editable:false,
                                    onClickButton: function () {
                                        $('#merchant-list').dialog({
                                            title: '选择赞助商家',
                                            width: 750,
                                            height: 450,
                                            onBeforeOpen: function () {
                                                /*============初始化商家数据网格==============*/
                                                $('#merchant_grid').datagrid({
                                                    url: '${basePath}/admin/api/luckyDraw/merchant/list',
                                                    columns: [[
                                                        {field: '', checkbox: 'true'},
                                                        {
                                                            field: 'headerImg',
                                                            title: '商家头像',
                                                            width: 100,
                                                            align: 'center',
                                                            formatter: function (value) {
                                                                return "<img src=" + value.url + " style='width:30px;heigh:30px' >";
                                                            }
                                                        },
                                                        {
                                                            field: 'merchName',
                                                            title: '商家名称',
                                                            width: 100,
                                                            align: 'center'
                                                        },
                                                        {
                                                            field: 'merchTell',
                                                            title: '联系电话',
                                                            width: 100,
                                                            align: 'center'
                                                        },
                                                        {
                                                            field: 'examinState',
                                                            title: '商家状态',
                                                            width: 100,
                                                            align: 'center',
                                                            formatter: function (value) {
                                                                switch (value) {
                                                                    case 1:
                                                                        return '未提交审核';
                                                                    case 2:
                                                                        return '待审核';
                                                                    case 3:
                                                                        return '已通过审核';
                                                                    case 4:
                                                                        return '未通过审核';
                                                                }
                                                            }
                                                        },
                                                        {
                                                            field: 'permit',
                                                            title: '是否有效',
                                                            width: 100,
                                                            align: 'center',
                                                            sortable: true,
                                                            formatter: function (value) {
                                                                if (value) {
                                                                    return "已启用";
                                                                }
                                                                else {
                                                                    return "未启用";
                                                                }
                                                            }
                                                        }
                                                    ]],
                                                    toolbar: '#grid_toolbar',
                                                    singleSelect: true,
                                                    pageSize: 20,
                                                    pagination: true,
                                                    fit: true,
                                                    fitColumns: true,
                                                    border: false,
                                                    onLoadError: function () {
                                                        $.messager.alert('出错了', '数据加载失败，请重试', 'error');
                                                    },
                                                    onCheck: function (index, row) {
                                                        rows = row;
                                                    }
                                                });
                                                /*============初始化商家搜索框==============*/
                                                $('#grid_searchbox').searchbox({
                                                    width: 300,
                                                    menu: '#searchbox_menu',
                                                    prompt: '输入关键字搜索',
                                                    searcher: function (value, name) {
                                                        var param = {};
                                                        if (value) {
                                                            param.searchName = name;
                                                            param.searchValue = value;
                                                        }
                                                        $('#merchant_grid').datagrid('load', param);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }, '-', {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var editable;
                        $.ajax({
                            url: '${basePath}/admin/api/luckyDrawAct/${lkDrawActId}/isEditable',
                            type: 'get',
                            dataType: 'text',
                            async: false,
                            success: function (info) {
                                info = $.parseJSON(info);
                                editable = info.isEditable;
                            }
                        });
                        console.log(editable)
                        if (!editable) {
                            $.messager.alert('提示', '该抽将活动已经上过线，上过线的活动都不支持添加操作！');
                            return;
                        }
                        var rows = $('#dg').datagrid('getChecked');
                        if (rows.length == 0) {
                            $.messager.alert('提示', '请选中一行进行操作', 'info');
                            return;
                        }
                        var luckDraw = rows[0];
                        if (luckDraw.ldOrder == 0) {
                            $('#edit-notLuckyDraw').dialog({
                                title: '修改奖项',
                                width: 400,
                                modal: true,
                                buttons: [{
                                    text: '保存',
                                    iconCls: 'icon-save',
                                    handler: function () {
                                        $('#edit-notLuckyDraw-form').form({
                                            url: '${basePath}/admin/api/luckyDraw/edit',
                                            success: function (info) {
                                                info = $.parseJSON(info);
                                                if (info.success) {
                                                    $.messager.show({
                                                        title: '提示',
                                                        msg: info.msg
                                                    });
                                                    $('#dg').datagrid('load');
                                                    $('#edit-notLuckyDraw').dialog('close');
                                                } else {
                                                    $.messager.alert('错误', info.msg, 'error');
                                                }
                                            }
                                        });

                                        $('#edit-notLuckyDraw-form').submit();
                                    }
                                }, {
                                    text: '取消',
                                    iconCls: 'icon-cancel',
                                    handler: function () {
                                        $('#edit-notLuckyDraw').dialog('close');
                                    }
                                }],
                                onBeforeOpen: function () {
                                    $('#edit-notLuckyDraw-form').form('clear');
                                    $('#edit-notLuckyDraw-form').form('load', luckDraw);
                                    $('#luckyDrawActivityId1').val('${luckDrawActId}');
                                    console.log(luckDraw.percentage);
                                    $('#notluck-percentage').numberspinner('setValue', luckDraw.percentage);
                                }
                            });
                            return;
                        }
                        $('#add-edit-win').dialog({
                            title: '修改奖项',
                            width: 400,
                            modal: true,
                            buttons: [{
                                text: '保存',
                                iconCls: 'icon-save',
                                handler: function () {
                                    $('#add-edit-form').form({
                                        url: '${basePath}/admin/api/luckyDraw/edit',
                                        success: function (info) {
                                            info = $.parseJSON(info);
                                            if (info.success) {
                                                $.messager.show({
                                                    title: '提示',
                                                    msg: info.msg
                                                });
                                                $('#dg').datagrid('load');
                                                $('#add-edit-win').dialog('close');
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
                                $('#add-edit-form').form('clear');
                                $('#add-edit-form').form('load', luckDraw);
                                $('#luckyDrawActivityId').val('${luckDrawActId}');
                                $('#merchant').textbox({
                                    buttonText: '选择商家',
                                    iconAlign: 'left',
                                    editable:false,
                                    onClickButton: function () {
                                        $('#merchant-list').dialog({
                                            title: '选择赞助商家',
                                            width: 750,
                                            height: 450,
                                            onBeforeOpen: function () {
                                                /*============初始化商家数据网格==============*/
                                                $('#merchant_grid').datagrid({
                                                    url: '${basePath}/admin/api/luckyDraw/merchant/list',
                                                    columns: [[
                                                        {field: '', checkbox: 'true'},
                                                        {
                                                            field: 'headerImg',
                                                            title: '商家头像',
                                                            width: 100,
                                                            align: 'center',
                                                            formatter: function (value) {
                                                                return "<img src=" + value.url + " style='width:30px;heigh:30px' >";
                                                            }
                                                        },
                                                        {
                                                            field: 'merchName',
                                                            title: '商家名称',
                                                            width: 100,
                                                            align: 'center'
                                                        },
                                                        {
                                                            field: 'merchTell',
                                                            title: '联系电话',
                                                            width: 100,
                                                            align: 'center'
                                                        },
                                                        {
                                                            field: 'examinState',
                                                            title: '商家状态',
                                                            width: 100,
                                                            align: 'center',
                                                            formatter: function (value) {
                                                                switch (value) {
                                                                    case 1:
                                                                        return '未提交审核';
                                                                    case 2:
                                                                        return '待审核';
                                                                    case 3:
                                                                        return '已通过审核';
                                                                    case 4:
                                                                        return '未通过审核';
                                                                }
                                                            }
                                                        },
                                                        {
                                                            field: 'permit',
                                                            title: '是否有效',
                                                            width: 100,
                                                            align: 'center',
                                                            sortable: true,
                                                            formatter: function (value) {
                                                                if (value) {
                                                                    return "已启用";
                                                                }
                                                                else {
                                                                    return "未启用";
                                                                }
                                                            }
                                                        }
                                                    ]],
                                                    toolbar: '#grid_toolbar',
                                                    singleSelect: true,
                                                    pageSize: 20,
                                                    pagination: true,
                                                    fit: true,
                                                    fitColumns: true,
                                                    border: false,
                                                    onLoadError: function () {
                                                        $.messager.alert('出错了', '数据加载失败，请重试', 'error');
                                                    },
                                                    onCheck: function (index, row) {
                                                        rows = row;
                                                    }
                                                });
                                                /*============初始化商家搜索框==============*/
                                                $('#grid_searchbox').searchbox({
                                                    width: 300,
                                                    menu: '#searchbox_menu',
                                                    prompt: '输入关键字搜索',
                                                    searcher: function (value, name) {
                                                        var param = {};
                                                        if (value) {
                                                            param.searchName = name;
                                                            param.searchValue = value;
                                                        }
                                                        $('#merchant_grid').datagrid('load', param);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                                if (luckDraw.hasOwnProperty("merchant")) {
                                    $('#merchant').textbox('setValue', luckDraw.merchant.merchantId);
                                    $('#merchant').textbox('setText', luckDraw.merchant.merchName);
                                }
                                $('#percentage').numberspinner('setValue', luckDraw.percentage);
                            }
                        });
                    }
                }, '-', {
                    text: '删除',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        var editable;
                        $.ajax({
                            url: '${basePath}/admin/api/luckyDrawAct/${lkDrawActId}/isEditable',
                            type: 'get',
                            dataType: 'text',
                            async: false,
                            success: function (info) {
                                info = $.parseJSON(info);
                                editable = info.isEditable;
                            }
                        });
                        if (!editable) {
                            $.messager.alert('提示', '该抽将活动已经上过线，上过线的活动都不支持删除操作！');
                            return;
                        }
                        var rows = $('#dg').datagrid('getChecked');
                        if (rows.length == 0) {
                            $.messager.alert('提示', '请选中一行进行操作', 'info');
                            return;
                        }
                        var luckDraw = rows[0];
                        if (luckDraw.ldOrder == 0) {
                            $.messager.alert('提示', '未中奖项不能删除', 'info');
                            return;
                        }
                        $.messager.confirm('提示', '确认要删除吗?', function (r) {
                            if (r) {
                                $.ajax({
                                    url: '${basePath}/admin/api/luckyDraw/del',
                                    type: 'post',
                                    dataType: 'text',
                                    data: {
                                        luckDrawId: luckDraw.luckyDrawId
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

            $('#select-merchant').click(function () {
                var rows = $('#merchant_grid').datagrid('getChecked');
                if (rows.length == 0) {
                    $.messager.alert('提示', '请选中一条数据', 'info');
                    return;
                }
                $('#merchant').textbox('setValue', rows[0].merchantId);
                $('#merchant').textbox('setText', rows[0].merchName);
                $('#merchant-list').dialog('close');
            });
        });

    </script>
    <style type="text/css">
        * {
            font-size: 12px;
            font-family: 微软雅黑;
        }

        body {
            font-size: 12px;
            font-family: 微软雅黑;
        }

        table input {
            width: 200px;
        }
    </style>
</head>
<body>
<div id="dg"></div>

<div id="add-edit-win">
    <form id="add-edit-form" method="post">
        <input type="hidden" name="luckyDrawId"/>
        <input type="hidden" name="luckyDrawActivity.activityId" id="luckyDrawActivityId"/>
        <table align="center" width="90%" style="border-collapse:collapse ">
            <tr>
                <td>奖项名:</td>
                <td colspan="3">
                    <input name="title" class="easyui-textbox" data-options="required:true">
                </td>
            </tr>
            <tr>
                <td>奖品数量:</td>
                <td>
                    <input name="amount" class="easyui-numberspinner" required="required"
                           data-options="min:0,precision:0">
                </td>
            </tr>
            <tr>
                <td>占百分比:(0~1之间)</td>
                <td>
                    <input class="easyui-numberspinner" name="percentage" id="percentage"
                           data-options="min:0,max:1,precision:2," increment="0.01">
                </td>
            </tr>
            <tr>
                <td>赞助商家:</td>
                <td>
                    <input name="merchant.merchantId" id="merchant">
                </td>
            </tr>
            <tr>
                <td>奖品使用说明:</td>
                <td colspan="3">
                    <input name="desc" class="easyui-textbox" data-options="mutline:true,required:true"
                           style="height: 40px;">
                </td>
            </tr>
            <tr>
                <td>顺序</td>
                <td>
                    <input name="ldOrder" class="easyui-numberspinner" required="required"
                           data-options="min:1,max:12,editable:false">
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="edit-notLuckyDraw">
    <form id="edit-notLuckyDraw-form" method="post">
        <input type="hidden" name="luckyDrawId"/>
        <input type="hidden" name="luckyDrawActivity.activityId" id="luckyDrawActivityId1"/>
        <table>
            <tr>
                <td>占百分比:(0~1之间)</td>
                <td>
                    <input class="easyui-numberspinner" name="percentage" id="notluck-percentage"
                           data-options="min:0,max:1,precision:2," increment="0.01">
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="merchant-list">
    <table id="merchant_grid"></table>
    <div id="grid_toolbar" class="toolbar-container">
        <a class="easyui-linkbutton" id="select-merchant" data-options="iconCls:'icon-ok',plain:true">选择</a>
        <input id="grid_searchbox">
    </div>
    <div id="searchbox_menu" style="width:120px">
        <div data-options="name:'merchName'">商家名称</div>
        <div data-options="name:'merchTell'">商家电话</div>
        <div data-options="name:'district.name'">商圈</div>
    </div>
</div>


</body>
</html>
