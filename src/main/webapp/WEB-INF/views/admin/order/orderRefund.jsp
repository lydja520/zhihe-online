<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/2/3
  Time: 9:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(document).ready(function () {
            /*========数据网格========*/
            $('#order_dg').datagrid({
                url: '${basePath}/admin/api/order/list',
                queryParams: {
                    searchOrderState: '101',
                },
                title: '所有订单',
                fitColumns: true,
                fit: true,
                pagination: true,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#tb',
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
                        field: 'orderState', title: '订单状态', width: 100, align: 'center', sortable: true,
                        formatter: function (value) {
                            console.log(value);
                            switch (value) {
                                case <%=Constant.ORDER_STATE_NO_PAYMENT%>:
                                    return "<span style='color: red'>已提交，等待买家付款</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_NO_DISPATCHER%>:
                                    return "<span style='color: firebrick'>买家已付款，等待卖家发货</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_DISPATCHER%>:
                                    return "<span style='color: #0000FF'>卖家已经发货</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_DELIVER%>:
                                    return "<span style='color:green'>买家已经确认收货</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_CANCEL%>:
                                    return "<span style='color: red'>订单已取消</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_WAIT_REFUND%>:
                                    return "<span style='color: red'>订单等待退款</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_REFUNDING%>:
                                    return "<span style='color: blue'>退款中……</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_REFUND%>:
                                    return "<span style='color: green'>订单已退款</span>";
                                    break;
                            }
                        }
                    },
                    {
                        field: 'activityGoods', title: '是否是秒杀商品', width: 100, align: 'center',
                        formatter:function (value) {
                            if(value){
                                return "<span style='color: red;'>是</span>"
                            }
                            return "<span style='color: green'>否</span>";
                        }
                    },
                    {
                        field: 'payType', title: '支付方式', width: 60, align: 'center',
                        formatter: function (value) {
                            if (value == 'wx') {
                                return '微信支付';
                            }
                            return '支付宝支付';
                        }
                    },
                    {
                        field: 'user', title: '买家账号', width: 80, align: 'center',
                        formatter: function (value) {
                            return value.userPhone;
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
                    {
                        field: 'orderId', title: '订单详情', width: 80, align: 'center',
                        formatter: function (data) {
                            return "<button value='" + data + "' onclick='preview(this.value)'>查看详情</button>"
                        }
                    },
                ]]
            });


            /*========订单处理(修改运费、修改发货地址、修改订单金额，发货)========*/
            $('#order-handle').click(function () {
                var rows = $('#order_dg').datagrid('getSelections');
                if (rows.length == 0) {
                    $.messager.alert('未选中行', '请选中一行进行修改');
                } else {
                    var orderId = rows[0].orderId;
                    $('#order-handle-win').dialog({
                        title: '修改订单',
                        icon: 'icon-edit2',
                        width: 800,
                        height: 480,
                        modal: true,
                        toolbar: [{
                            text: '确认退款',
                            iconCls: 'icon-edit2',
                            handler: function () {
                                if (rows[0].orderState == <%=Constant.ORDER_STATE_WAIT_REFUND%>) {
                                    $.messager.confirm('提示', '确认要退款吗？', function (r) {
                                        if (r) {
                                            refund(rows[0]);
                                        }
                                    });
                                } else {
                                    $.messager.alert('提示', '当前订单状态不支持确认退款操作', 'warning');
                                }
                            }
                        }],
                        onBeforeOpen: function () {
                            $('#order-handle-win').attr('src', "/admin/order/" + orderId + "/orderDetailsInfo");
                        }
                    });
                }
            });

            $('#search').click(function () {
                $('#order_dg').datagrid('load', {
                    searchOrderCode: $('#order-code').textbox('getValue'),
                    searchOrderState: '101',
                    searchUserPhone: $('#user-phone').textbox('getValue'),
                    searchStartTime: $('#start-time').datebox('getValue'),
                    searchEndTime: $('#end-time').datebox('getValue')
                });
            });

            $('#clear-search').click(function () {
                $('#order_dg').datagrid('load', {
                    searchOrderState: '101'
                });
                $('#search-form').form('reset');
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
                    $('#preview_orderDetails').attr('src', "" + data + "/orderDetailsInfo");
                }
            });
        }

        /*========退款========*/
        function refund(order) {
            $.ajax({
                url: '${basePath}/admin/api/order/refund',
                dataType: 'text',
                type: 'post',
                data: {
                    orderId: order.orderId
                },
                beforeSend: function () {
                    $.messager.progress();
                },
                success: function (info) {
                    info = $.parseJSON(info);
                    if (info.code == 200) {
                        $.messager.show({
                            title: '提示',
                            msg: info.msg
                        });
                        $('#order_dg').datagrid('load');
                    } else {
                        $.messager.alert('提示', '退款操作失败，请重试！', 'error');
                    }
                },
                error: function () {
                    $.messager.alert('提示', '退款操作失败，请重试！', 'error');
                },
                complete: function () {
                    $.messager.progress('close');
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

        .edit-order {
            list-style-type: none;
            margin-top: 30px;
            display: block;
            padding: 0px 0px 0px;
        }

        .edit-order li {
            width: 80%;
            margin: 15px auto 15px auto;
        }

        .edit-order li input {
            width: 200px;
        }

        .info-title {
            display: block;
            width: 30%;
            float: left;
        }

        .info-content {
            display: block;
            width: 65%;
            float: left;
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
        <a class="easyui-linkbutton" data-options="iconCls:'icon-edit2',plain:true" id="order-handle">订单处理</a>&nbsp;&nbsp;
        订单号：<input class="easyui-textbox" id="order-code" name="orderCode"/> &nbsp;&nbsp;&nbsp;&nbsp;
        买家账号：<input class="easyui-textbox" id="user-phone" name="userName"/>&nbsp;&nbsp;&nbsp;&nbsp;
        订单生成时间：
        从&nbsp;
        <input id="start-time" type="text" class="easyui-datebox" data-options="editable:false" name="startDate"/>
        &nbsp;至&nbsp;
        <input id="end-time" type="text" class="easyui-datebox" data-options="editable:false" name="endDate"/>&nbsp;&nbsp;
        <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">
            &nbsp;搜索&nbsp;
        </a>
        <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">
            &nbsp;清除搜索&nbsp;
        </a>&nbsp;&nbsp;
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

<!--修改订单开始-->
<iframe id="order-handle-win" style="padding: 0px;margin: 0px;border: none"></iframe>
<!--修改订单结束-->

<!--退款窗口开始-->
<div id="refund-win">
    <ur class="edit-order">
        <li>
            <div class="info-title">
                退款金额：
            </div>
            <div class="info-content">
                <input type="text" class="easyui-numberbox"
                       data-options="min:0,precision:2,required:true" style="width: 180px" id="refund-textbox"> 元
            </div>
        </li>
    </ur>
</div>
<!--退款窗口结束-->


</body>
</html>
