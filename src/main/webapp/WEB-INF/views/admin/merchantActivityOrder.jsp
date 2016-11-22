<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/6/15
  Time: 14:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>活动订单统计</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/activityOrder/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                pageSize: 20,
                singleSelect: true,
                rownumbers: true,
                columns: [[
                    {field: 'orderCode', title: '活动订单号', width: 100,align: 'center'},
                    {
                        field: 'orderName', title: '活动订单名字', width: 100,align: 'center'
                    },
                    {
                        field: 'createDate', title: '订单创建时间', width: 100,align: 'center'
                    },
                    {
                        field: 'activity', title: '活动名字', width: 100,align: 'center',
                        formatter: function (value, rows, index) {
                            return value.activitName;
                        }
                    },
                    {
                        field: 'activity.activityPromoter.merchName', title: '活动发起商', align: 'center',width: 100,
                        formatter: function (value, rows, index) {
                            return rows.activity.activityPromoter.merchName;
                        }
                    },
                    {
                        field: 'activity.activityPromoter.merchTell', title: '发起商联系电话', align: 'center',width: 100,
                        formatter: function (value, rows, index) {
                            return rows.activity.activityPromoter.merchTell;
                        }
                    },
                    {
                        field: 'buyerAccount', title: '支付用户账号', align: 'center',width: 100,
                    },
                    {
                        field: 'sellerAccount', title: '商家收款支付宝账号', align: 'center',width: 100,
                    },
                    {
                        field: 'totalMoney', title: '订单金额', align: 'center',width: 100,
                        formatter: function (value, rows, index) {
                            return value + "元";
                        }
                    },
                    {
                        field: 'payDate', title: '订单支付时间', align: 'center',width: 100,
                    },
                    {
                        field: 'aliTransactionCode', title: '阿里支付交易号', align: 'center',width: 100,
                    }
                ]],
                toolbar: "#grid_toolbar"
            })

            /*================用户搜索================*/
            $('#search').click(function () {
                $('#dg').datagrid('load', {
                    searchActivityOrderCode: $('#activity-orderCode').textbox('getValue'),
                    searchUserpayCode: $('#user-payCode').textbox('getValue'),
                    initTime: $('#start-time').datebox('getValue'),
                    endTime: $('#end-time').datebox('getValue')
                });
            });
            /*===========清楚搜索数据===========*/
            $('#clear-search').click(function () {
                $('#dg').datagrid('load', {});
                $('#search-form').form('reset');
            });
        })
    </script>
</head>
<body>
<div id="dg"></div>
<div id="grid_toolbar">
    <form id="search-form">
        活动订单号:<input class="easyui-textbox" id="activity-orderCode" prompt="输入订单号"/>&nbsp;
        用户支付账号:<input class="easyui-textbox" id="user-payCode" prompt="输入支付用户账号">&nbsp;
        支付时间：从<input id="start-time" type="text" class="easyui-datebox"
                     data-options="editable:false" prompt="请选择开始时间"/>
        至<input id="end-time" type="text" class="easyui-datebox"
                data-options="editable:false" prompt="请输入结束时间"/>
        <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">
            搜索
        </a>
        <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">
            清除搜索
        </a>
    </form>
</div>

</body>
</html>

