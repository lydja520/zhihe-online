<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/5/9
  Time: 9:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>等待退款的订单</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(function () {
            $('#order-grid').datagrid({
                url: '${basePath}/admin/api/waiplatrefund/orders',
                fitColumns: true,
                fit: true,
                pagination: true,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#grid-tool-bar',
                columns: [[
                    {field: '', checkbox: true, align: 'center'},
                    {
                        field: 'coverImg', title: '分面图', align: 'center', width: 60,
                        formatter: function (value) {
                            if (value == null) {
                                return "无封面图";
                            }
                            return "<img style='width: 40px;height: 40px;padding:1px 0px;border-radius: 5px' src='" + value.url + "'/>";
                        }
                    },
                    {field: 'orderCode', title: '平台订单号', width: 100, align: 'center'},
                    /*                    {field: 'pingPPorderNo', title: '交易订单号', width: 100, align: 'center'},*/
                    {field: 'orderName', title: '订单名', width: 100, align: 'center'},
                    {
                        field: 'orderState', title: '订单状态', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value == <%=Constant.ORDER_STATE_REFUNDING%>) {
                                return "退款中……";
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
                        field: 'user', title: '买家账号', width: 80, align: 'center',
                        formatter: function (value) {
                            return value.userPhone;
                        }
                    },
                    {
                        field: 'merchant', title: '商家', width: 100, align: 'center',
                        formatter: function (value) {
                            return value.merchName;
                        }
                    },
                    {field: 'receiverName', title: '收件人', width: 100, align: 'center'},
                    {field: 'receiverPhone', title: '收件人电话号码', width: 100, align: 'center'},
                    {field: 'receiverAdd', title: '收件人地址', width: 100, align: 'center'},
                    {
                        field: 'orderTotal', title: '订单金额', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {field: 'createDate', title: '下单时间', width: 100, align: 'center', sortable: true},
                    {
                        field: 'orderId', title: '订单详情', width: 80, align: 'center',
                        formatter: function (data) {
                            return "<button value='" + data + "' onclick='preview(this.value)'>查看详情</button>"
                        }
                    },
                ]],
                toolbar: [{
                    text: '退款操作',
                    iconCls: 'icon-edit',
                    handler: function () {
                        handlerRefund();
                    }
                }]
            })
        });

        /**
         *执行退款操作
         */
        function handlerRefund() {
            var rows = $('#order-grid').datagrid('getChecked');
            if (rows.length == 0) {
                $.messager.alert("提示", "请选择一条数据进行操作", "info");
                return;
            }
            var order = rows[0];
            $('#oreder-renfund-win').dialog({
                title: '退款',
                icoCls: 'icon-edit',
                width: 350,
                height: 200,
                buttons: [{
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#order-refund-form').form({
                            url: '${basePath}/admin/api/waitPlatRefund/order/refund',
                            success: function (data) {
                                data = $.parseJSON(data);
                                $.messager.show({
                                    title: '提示',
                                    msg: data.msg
                                });
                                $('#order-grid').datagrid('reload');
                                $('#oreder-renfund-win').dialog('close');
                            }
                        });
                        $('#order-refund-form').submit();
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#oreder-renfund-win').dialog('close');
                    }
                }],
                onBeforeOpen: function () {
                    $('#order-name').html(order.orderName);
                    $('#order-code').html(order.orderCode);
                    $('#merchant-name').html(order.merchant.merchName);
                    $('#refund-url').attr('href', order.alipayRefundUrl);
                    $('#orderId').val(order.orderId);
                }
            });
        }

        /*========预览订单详细信息========*/
        function preview(data) {
            $("#preview_orderDetails").attr('src', "${basePath}/admin/order/" + data + "/orderDetailsInfo");
            $("#preview_orderDetails").dialog({
                title: '订单详情',
                icon: 'icon-preview',
                width: 800,
                height: 480,
                modal: true,
            });
        }
    </script>
</head>
<body>
<table id="order-grid"></table>
<div id="oreder-renfund-win">
    <div style="width: 80%;margin: auto">
        <div>订单名：<span id="order-name"></span></div>
        <div>订单编号：<span id="order-code"></span></div>
        <div>商家：<span id="merchant-name"></span></div>
        <hr>
        <div><a target="_blank" id="refund-url" style="color: #0092DC">点击这里，跳到支付宝页面进行退款</a></div>
        <hr>
        <form id="order-refund-form" method="post">
            <input type="hidden" id="orderId" name="orderId">

            <div>支付宝交易号：<input class="easyui-textbox" name="alipayRefundTransacCode" required/></div>
        </form>
    </div>
</div>

<iframe id="preview_orderDetails"></iframe>
</body>
</html>
