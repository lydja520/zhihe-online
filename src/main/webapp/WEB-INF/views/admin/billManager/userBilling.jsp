<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/3/14
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>等待提现的用户</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var user = null;
            /*========数据网格========*/
            $('#dg').datagrid({
                url: '${basePath}/admin/api/user/waitWithdraw/list',
                fit: true,
                title: '用户提现管理',
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                rownumbers: true,
                columns: [[
                    {field: 'userWithdrawId', checkbox: true, width: 100, align: 'center'},
                    {
                        field: 'user', title: '用户头像', width: 100, align: 'center',
                        formatter: function (value) {
                            user = value;
                            if (user.portrait != null) {
                                return "<img style='width:45px;height:45px;border-radius:22px;margin:1px auto 1px auto' src='" + user.portrait.url + "'/><br><span>" + user.userName + "</span>";
                            } else {
                                return "<img style='width:45px;height:45px;margin:1px auto 1px auto' src='${basePath}/static/images/unknow.jpg'/>";
                            }
                        }
                    },
                    {
                        field: 'aaa', title: '用户账号', width: 100, align: 'center',
                        formatter: function (value) {
                            return user.userPhone;
                        }
                    },
                    {
                        field: 'money', title: '申请提现', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + "元";
                        }
                    },
                    {
                        field: 'poundage', title: '手续费', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + "元";
                        }
                    },
                    {
                        field: 'realMoney', title: '实际应提现', width: 100, align: 'center',
                        formatter: function (value) {
                            return "<span style='color: red'>" + value + "元</span>";
                        }
                    },
                    {field: 'aliCode', title: '支付宝账号', width: 100, align: 'center'},
                    {
                        field: 'withdrawState', title: '当前状态', width: 100, align: 'center',
                        formatter: function (value) {
                            switch (value) {
                                case <%=Constant.APPLY_WITHDRAW%>:
                                    return "<span>买家提交申请</span><a><img src='${basePath}/static/css/icons/tip.png'></a>";
                                    break;
                                case <%=Constant.WITHDRAW_OK%>:
                                    return "<span>提现成功</span>";
                                    break;
                                case <%=Constant.WITHDRAW_ERR%>:
                                    return "<span>提现失败<span><a><img src='${basePath}/static/css/icons/tip.png'></a>";
                                    break;
                            }
                        }
                    },
                    {field: 'applyDate', title: '申请提现日期', width: 100, align: 'center'}
                ]],
                toolbar: '#tb',
            });

            /*提现成功*/
            $('#withdraw-success-btn').click(function () {
                var rows = $('#dg').datagrid('getSelections');
                if (rows.length == 0) {
                    $.messager.alert('提示', '请选中一条数据进行操作', 'info');
                    return;
                }
                var userWithDraw = rows[0];
                $('#examin_win').dialog({
                    title: '审核用户提现',
                    modal: true,
                    width: 350,
                    buttons: [{
                        text: '确定',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#withdraw_form').form({
                                url: '${basePath}/admin/api/userWithDrawOk',
                                success: function (info) {
                                    info = $.parseJSON(info);
                                    if (info.success) {
                                        $.messager.show({
                                            title: '提示',
                                            msg: info.msg
                                        });
                                        $('#examin_win').dialog('close');
                                        $('#dg').datagrid('reload');
                                    } else {
                                        $.messager.alert('提示', info.msg, 'info');
                                    }
                                }
                            });
                            $('#withdraw_form').submit();
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#examin_win').dialog('close');
                        }
                    }
                    ],
                    onBeforeOpen: function () {
                        $('#withdraw-id').val(userWithDraw.userWithdrawId);
                        $('#user-name').html('用户名：' + userWithDraw.user.userName);
                        $('#alipay-code').html('用户支付宝账号：' + userWithDraw.aliCode);
                        $('#mount').html('申请提金额：' + userWithDraw.money + ' 元');
                        $('#poundage').html('手续费：' + userWithDraw.poundage + ' 元');
                        $('#real-mount').html('实际转账金额：' + userWithDraw.realMoney + ' 元');
                        $('#transaction-code').show();
                        $('#ali-transaction-code').textbox('enable');
                        $('#ali-transaction-code').textbox('clear');
                        $('#transaction-err').hide();
                        $('#transaction-err-reason').textbox('disable');
                        $('#transaction-err-reason').textbox('clear');
                    }

                });
            });

            /*提现失败*/
            $('#withdraw-fial-btn').click(function () {
                var rows = $('#dg').datagrid('getSelections');
                if (rows.length == 0) {
                    $.messager.alert('提示', '请选中一条数据进行操作', 'info');
                    return;
                }
                var userWithDraw = rows[0];
                $('#examin_win').dialog({
                    title: '审核用户提现',
                    modal: true,
                    width: 350,
                    buttons: [{
                        text: '确定',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#withdraw_form').form({
                                url: '${basePath}/admin/api/userWithDrawFailure',
                                success: function (info) {
                                    info = $.parseJSON(info);
                                    if (info.success) {
                                        $.messager.show({
                                            title: '提示',
                                            msg: info.msg
                                        });
                                        $('#examin_win').dialog('close');
                                        $('#dg').datagrid('reload');
                                    } else {
                                        $.messager.alert('提示', info.msg, 'info');
                                    }
                                }
                            });
                            $('#withdraw_form').submit();
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#examin_win').dialog('close');
                        }
                    }
                    ],
                    onBeforeOpen: function () {
                        $('#withdraw-id').val(userWithDraw.userWithdrawId);
                        $('#user-name').html('用户名：' + userWithDraw.user.userName);
                        $('#alipay-code').html('用户支付宝账号：' + userWithDraw.aliCode);
                        $('#mount').html('申请提金额：' + userWithDraw.money + ' 元');
                        $('#poundage').html('手续费：' + userWithDraw.poundage + ' 元');
                        $('#real-mount').html('实际转账金额：' + userWithDraw.realMoney + ' 元');
                        $('#transaction-code').hide();
                        $('#ali-transaction-code').textbox('disable');
                        $('#ali-transaction-code').textbox('clear');
                        $('#transaction-err').show();
                        $('#transaction-err-reason').textbox('enable');
                        $('#transaction-err-reason').textbox('clear');
                    }
                });
            });

            /*导出查询的数据*/
            $('#export-data-btn').click(function () {
                $.messager.confirm('确认导出', '确认要导出满足查询条件的用户提现数据吗？', function (r) {
                    if (r) {
                        var paramAndValue = "?billCode=" + $('#billCode').textbox('getValue') + "&merchant.merchName=" + $('#merchantName').textbox('getValue')
                                + "&startTime=" + $('#start-time').textbox('getValue') + "&endTime=" + $('#end-time').textbox('getValue');
                        window.location.href = "${basePath}/admin/api/merchantBill/export" + paramAndValue;
                    }
                });
            });
        });
    </script>
    <style type="text/css">
        ul, li {
            margin: 0px;
            padding: 0px;
            list-style: none;
        }

        ul {
            width: 80%;
            margin: auto;
        }

        li {
            margin-top: 10px;
        }


    </style>
</head>
<body>

<!--工具栏和搜索栏开始-->
<div id="tb">
    <a id="withdraw-success-btn" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">提现成功</a>
    <a id="withdraw-fial-btn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">提现失败</a>
    <form id="search-form" accept-charset="UTF-8" style="display: inline">
        用户账号:<input name="billCode" class="easyui-textbox" id="billCode" prompt="请输入用户账号"/>&nbsp;
        提现状态:
        <select id="handleState" class="easyui-combobox" name="handleState"
                data-options="editable:false,panelHeight:100" style="width: 100px">
            <option value="0">等待提现</option>
            <option value="1">已处理</option>
            <option value="2">未处理</option>
        </select>&nbsp;
        申请时间：
        从<input name="startTime" id="start-time" type="text" class="easyui-datebox"
                     data-options="editable:false" prompt="开始时间"/>
        至<input id="end-time" name="endTime" type="text" class="easyui-datebox"
                data-options="editable:false" prompt="结束时间"/>
        <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索</a>
        <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清除搜索</a>
        <a id="export-data-btn" class="easyui-linkbutton" data-options="iconCls:'icon-save'">导出</a>
    </form>
</div>
<!--工具栏和搜索栏结束-->

<!--数据网格开始-->
<div id="dg"></div>
<!--数据网格结束-->

<!--提现操作-->
<div id="examin_win">
    <ul>
        <form id="withdraw_form" method="post">
            <input type="hidden" id="withdraw-id" name="withDrawId">
            <li id="user-name"></li>
            <li id="alipay-code"></li>
            <li id="mount"></li>
            <li id="poundage"></li>
            <li id="real-mount"></li>
            <hr>
            <li id="transaction-code" style="margin-bottom: 10px">支付宝交易号：
                <input class="easyui-textbox" id="ali-transaction-code" required name="aliTransactionCode"/>
            </li>
            <li id="transaction-err" style="margin-bottom: 10px">
                提现失败原因：
                <input class="easyui-textbox" id="transaction-err-reason" data-options="multiline:true" required
                       style="height: 50px" name="withDrawFailureReason"/>
            </li>
        </form>
    </ul>
</div>
<!--提现操作-->

</body>
</html>
