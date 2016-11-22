<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/4/15
  Time: 16:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家账单包含的订单</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <c:set var="billId" value="${merchantBillId}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/merchantBill/${billId}/merchantBillDetail/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                border: false,
                rownumbers: true,
                columns: [[
                    {field: 'createDate', title: '生成时间', width: 100, align: 'center'},
                    {
                        field: 'orderCode', title: '订单号', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            return row.order.pingPPorderNo;
                        }
                    },
                    {
                        field: 'activity', title: '是否是秒杀订单', width: 80, align: 'center',
                        formatter: function (value, row, index) {
                            if (row.order.activityGoods) {
                                return "<span style='color: green'>是</span>"
                            } else {
                                return "<span style='color: red'>否</span>"
                            }
                        }
                    },
                    {
                        field: 'orderState', title: '状态', width: 100, align: 'center', sortable: true,
                        formatter: function (value, row, index) {
                            data = row.order.orderState;
                            switch (data) {
                                case <%=Constant.ORDER_STATE_ALREADY_GENERATE_BILL%>:
                                    return "<span style='color:#080808'>账单已生成，待结算</span>"
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_BILL%>:
                                    return "<span style='color: green'>账单已结算</span>";
                                    break;
                            }
                        }
                    },
                    {
                        field: 'aaa', title: '支付时间', width: 80, align: 'center',
                        formatter: function (value, row, index) {
                            return row.order.payDate;
                        }
                    },
                    {
                        field: 'amount', title: '订单金额', width: 80, align: 'center',
                        formatter: function (value) {
                            return value + "元";
                        }
                    },
                    {
                        field: 'carriage', title: '订单运费', width: 80, align: 'center',
                        formatter: function (value, row, index) {
                            return row.order.carriage + "元";
                        }
                    },
                    {
                        field: 'handlePoundageRate', title: '支付手续费利率', width: 80, align: 'center',
                    },
                    {
                        field: 'poundage', title: '手续费', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {
                        field: 'realAmount', title: '实际应转账金额', width: 100, align: 'center',
                        styler: function (value, row, index) {
                            return 'background-color:#ffee00;color:red;';
                        }
                    },
                    {
                        field: 'orderId', title: '订单详情', width: 80, align: 'center',
                        formatter: function (value, row, index) {
                            var data = row.order.orderId;
                            return "<button value='" + data + "' onclick='preview(this.value)'>查看详情</button>"
                        }
                    },
                ]]
            });
        });

        /*========预览订单详细信息========*/
        function preview(data) {
            $("#preview_orderDetails").attr('src', "${basePath}/admin/order/" + data + "/orderDetailsInfo");
            $("#preview_orderDetails").dialog({
                title: '订单详情',
                icon: 'icon-preview',
                width: 800,
                height: 400,
                modal: true,
            });
        }
    </script>
</head>
<body>

<div id="dg"></div>

<!--浏览订单详情开始-->
<iframe id="preview_orderDetails" style="padding: 0px;border: none;margin: 0px"></iframe>
<!--浏览订单详情结束-->

</body>
</html>
