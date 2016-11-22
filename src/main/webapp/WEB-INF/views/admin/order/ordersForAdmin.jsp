<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-6-18
  Time: 下午1:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<html>
<head>
    <title>订单管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(document).ready(function () {

            /*=======数据网格========*/
            $('#order_dg').datagrid({
                url: '${basePath}/admin/api/order/list',
                title: '所有订单',
                fitColumns: true,
                fit: true,
                pagination: true,
                singleSelect: true,
                rownumbers: true,
                pageSize: 30,
                toolbar: '#tb',
                columns: [[
                    {field: '', checkbox: true, align: 'center'},
                    {
                        field: 'coverImg', title: '封面图', align: 'center', width: 60,
                        formatter: function (value) {
                            if (value == null) {
                                return "无封面图";
                            }
                            return "<img style='width: 40px;height: 40px;padding:1px 0px;border-radius: 5px' src='" + value.url + "'/>";
                        }
                    },
                    {field: 'orderCode', title: '平台订单号', width: 100, align: 'center'},
                    {field: 'pingPPorderNo', title: '交易订单号', width: 100, align: 'center'},
                    {field: 'orderName', title: '订单名', width: 100, align: 'center'},
                    {
                        field: 'orderState', title: '订单状态', width: 120, align: 'center', sortable: true,
                        formatter: function (value) {
                            switch (value) {
                                case <%=Constant.ORDER_STATE_NO_PAYMENT%>:
                                    return "<span style='color: green'>已提交，等待买家付款</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_NO_DISPATCHER%>:
                                    return "<span style='color: firebrick'>买家已付款，等待卖家发货</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_DISPATCHER%>:
                                    return "<span style='color: blue'>卖家已经发货</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_DELIVER%>:
                                    return "<span style='color:#080808'>买家已经确认收货</span>"
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_CANCEL%>:
                                    return "<span style='color: red'>订单已取消</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_WAIT_REFUND%>:
                                    return "<span style='color: slategray'>订单等待退款</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_REFUNDING%>:
                                    return "<span style='color: blue'>退款中……</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_REFUND%>:
                                    return "<span style='color: red'>订单已退款</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_EVALUATE%>:
                                    return "<span style='color: green'>买家已评价</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_PAYDING%>:
                                    return "<span>支付中……</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_GENERATE_BILL%>:
                                    return "<span style='color: darkmagenta'>等待官方结算</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_BILL%>:
                                    return "<span style='color: darkgreen'>已结算</span>";
                                    break;
                            }
                        }
                    },
                    {
                        field: 'activityGoods', title: '是否是秒杀商品', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return "<span style='color: red;'>是</span>"
                            }
                            return "<span style='color: green'>否</span>";
                        }
                    },
                    {
                        field: 'merchant', title: '商家名', width: 100, align: 'center',
                        formatter: function (value) {
                            return value.merchName;
                        }
                    },
                    {
                        field: 'merchantPhone', title: '商家电话号码', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            return row.merchant.merchTell;
                        }
                    },
                    {
                        field: 'user', title: '买家账号', width: 80, align: 'center',
                        formatter: function (value) {
                            return value.userPhone;
                        }
                    },
                    {field: 'userMsg', title: '买家留言', width: 100, align: 'center'},
                    {field: 'receiverName', title: '收件人', width: 100, align: 'center'},
                    {field: 'receiverPhone', title: '收件人电话号码', width: 100, align: 'center'},
                    {field: 'receiverAdd', title: '收件人地址', width: 100, align: 'center'},
                    {
                        field: 'orderTotal', title: '原订单金额', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {
                        field: 'originalOrderTotal', title: '修改后订单金额', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            if (row.orderTotal == value) {
                                return "未修改";
                            }
                            return value;
                        }
                    },
                    {field: 'carriageNum', title: '快递单号', width: 100, align: 'center'},
                    {field: 'createDate', title: '下单时间', width: 100, align: 'center', sortable: true},
                    {
                        field: 'orderId', title: '订单详情', width: 80, align: 'center',
                        formatter: function (value) {
                            return "<button value='" + value + "' onclick='preview(this.value)'>查看详情</button>"
                        }
                    }
                ]]
            });

            $('#search').click(function () {
                $('#order_dg').datagrid('load', {
                    searchOrderCode: $('#order-code').textbox('getValue'),
                    searchOrderState: $('#order-state').combobox('getValue'),
                    searchCarriageNum: $('#carriage-num').textbox('getValue'),
                    searchUserPhone: $('#user-phone').textbox('getValue'),
                    searchMerchantName: $('#merchant-name').textbox('getValue'),
                    searchStartTime: $('#start-time').datebox('getValue'),
                    searchEndTime: $('#end-time').datebox('getValue')
                });
            });

            $('#clear-search').click(function () {
                $('#order_dg').datagrid('load', {});
                $('#search-form').form('reset');
            });

            $('#export').click(function () {
                var param = 'searchOrderCode=' + $('#order-code').textbox('getValue') + '&searchOrderState=' + $('#order-state').combobox('getValue') + '&searchCarriageNum='
                        + $('#carriage-num').textbox('getValue') + '&searchUserPhone=' + $('#user-phone').textbox('getValue') + '&searchStartTime=' + $('#start-time').datebox('getValue')
                        + '&searchEndTime=' + $('#end-time').datebox('getValue') + "&searchMerchantName=" + $('#merchant-name').textbox('getValue');
                $.messager.confirm('确认下载', '确认要导出当前查询出来的订单数据吗', function (r) {
                    if (r) {
                        window.location.href = '${basePath}/admin/api/order/export?' + param;
                    }
                });
            });

        });

        /*========预览订单详细信息========*/
        function preview(data) {
            $('#preview_orderDetails').dialog({
                title: '订单详情',
                icon: 'icon-preview',
                width: 800,
                height: 480,
                modal: true,
                onBeforeOpen: function () {
                    $('#preview_orderDetails').attr('src', "${basePath}/admin/order/" + data + "/orderDetailsInfo");
                }
            });
        }

    </script>

    <style>
        body {
            margin: 0px;
            padding: 0px;
            font-size: 12px;
        }

        .edit-order li {
            width: 80%;
            margin: 15px auto 15px auto;
        }

        .edit-order li input {
            width: 200px;
        }

        .clear {
            clear: both;
        }
    </style>
</head>
<body>
<!--toolbar&seacher开始-->
<div id="tb">
    <form id="search-form">
        订单号:<input class="easyui-textbox" id="order-code" prompt="请输入订单号"/> &nbsp;&nbsp;&nbsp;&nbsp;
        订单状态:
        <select id="order-state" class="easyui-combobox" name="orderState"
                data-options="editable:false" style="width: 150px">
            <option value="">全部订单</option>
            <option value="2">待付款</option>
            <option value="3">待发货</option>
            <option value="4">待收货</option>
            <option value="5">待评价</option>
            <option value="9">已完成</option>
            <option value="7">待退款</option>
            <option value="10">退款中</option>
            <option value="8">退款成功</option>
        </select>&nbsp;&nbsp;&nbsp;&nbsp;
        快递单号:<input class="easyui-textbox" id="carriage-num" prompt="请输入快递单号"/>&nbsp;&nbsp;&nbsp;&nbsp;
        买家账号:<input class="easyui-textbox" id="user-phone" prompt="请输入买家账号"/>&nbsp;&nbsp;&nbsp;&nbsp;
        商家名:<input class="easyui-textbox" id="merchant-name" prompt="请输入商家名"/>&nbsp;&nbsp;&nbsp;&nbsp;
        订单生成时间：从&nbsp;<input id="start-time" type="text" class="easyui-datebox"
                             data-options="editable:false" prompt="请选择开始时间"/>
        &nbsp;至&nbsp;<input id="end-time" type="text" class="easyui-datebox"
                            data-options="editable:false" prompt="请输入结束时间"/>
        <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">
            &nbsp;搜索&nbsp;
        </a>&nbsp;&nbsp;
        <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">
            &nbsp;清除搜索&nbsp;
        </a>&nbsp;&nbsp;
        <a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-save'">
            &nbsp;导出&nbsp;
        </a>
        <div class="clear"></div>
    </form>

</div>
<!--toolbar&seacher结束-->

<!--数据网格开始-->
<div id="order_dg"></div>
<!--数据网格结束-->

<!--浏览订单详情开始-->
<iframe id="preview_orderDetails" style="padding: 0px;border: none;margin: 0px"></iframe>
<!--浏览订单详情结束-->

</body>
</html>

