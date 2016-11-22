<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/1/5
  Time: 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<html>
<head>
    <title>待发货</title>
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
                queryParams: {
                    searchOrderState: '3',
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
                                case <%=Constant.ORDER_STATE_ALREADY_REFUND%>:
                                    return "<span style='color: red'>订单已退款</span>";
                                    break;
                                case <%=Constant.ORDER_STATE_ALREADY_EVALUATE%>:
                                    return "<span style='color: green'>买家已评价</span>";
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
                            text: '修改收货信息',
                            iconCls: 'icon-edit2',
                            handler: function () {
                                if (rows[0].orderState == <%=Constant.ORDER_STATE_NO_PAYMENT%> || rows[0].orderState == <%=Constant.ORDER_STATE_NO_DISPATCHER%>) {
                                    editReceiveInfo(rows[0]);
                                } else {
                                    $.messager.alert('提示', '当前订单状态不支持修改收货信息', 'warning');
                                }
                            }
                        }, '-', {
                            text: '发货',
                            iconCls: 'icon-edit2',
                            handler: function () {
                                if (rows[0].orderState == <%=Constant.ORDER_STATE_NO_DISPATCHER %>) {
                                    deliverGoods(rows[0]);
                                } else {
                                    $.messager.alert('提示', '该订单还未支付，不支持发货操作', 'warning');
                                }
                            }
                        },],
                        onBeforeOpen: function () {
                            $('#order-handle-win').attr('src', "/admin/order/" + orderId + "/orderDetailsInfo");
                        }
                    });
                }
            });

            $('#search').click(function () {
                $('#order_dg').datagrid('load', {
                    searchOrderCode: $('#order-code').textbox('getValue'),
                    searchOrderState: '3',
                    searchUserPhone: $('#user-phone').textbox('getValue'),
                    searchStartTime: $('#start-time').datebox('getValue'),
                    searchEndTime: $('#end-time').datebox('getValue')
                });
            });

            $('#clear-search').click(function () {
                $('#order_dg').datagrid('load', {
                    searchOrderState: '3'
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
                    $('#preview_orderDetails').attr('src', "/admin/order/" + data + "/orderDetailsInfo");
                }
            });
        }

        /*========修改收货信息========*/
        function editReceiveInfo(order) {
            $('#edit-receive-info').dialog({
                title: '修改收货信息',
                iconCls: 'icon-edit2',
                width: 400,
                height: 260,
                modal: true,
                buttons: [{
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $.ajax({
                            url: '${basePath}/admin/order/updateReceiverInfo',
                            dataType: 'text',
                            type: 'post',
                            data: {
                                orderId: order.orderId,
                                receiverName: $('#receiverName').textbox('getValue'),
                                receiverPhone: $('#receiverPhone').textbox('getValue'),
                                receiverAdd: $('#receiverAdd').textbox('getValue'),
                            },
                            success: function (info) {
                                info = $.parseJSON(info);
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg,
                                });
                                if (info.success) {
                                    $('#edit-receive-info').dialog('close');
                                    $('#order-handle-win').attr('src', $('#order-handle-win').attr('src'));
                                    $('#order_dg').datagrid('load');
                                }
                            }
                        });
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#edit-receive-info').dialog('close');
                    }
                }],
                onBeforeOpen: function () {
                    $('#receiverName').textbox('setValue', order.receiverName);
                    $('#receiverPhone').textbox('setValue', order.receiverPhone);
                    $('#receiverAdd').textbox('setValue', order.receiverAdd);
                }
            });
        }

        /*========发货========*/
        function deliverGoods(order) {
            $('#deliver-goods-win').dialog({
                title: '发货',
                icon: 'icon-edit2',
                width: 400,
                height: 200,
                modal: true,
                buttons: [{
                    text: '发货',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $.ajax({
                            url: '${basePath}/admin/order/updateDispatcherState',
                            dataType: 'text',
                            type: 'post',
                            data: {
                                orderId: order.orderId,
                                carriageNum: $('#carriageMethod').textbox('getValue') + ": " + $('#carriageNum').textbox('getValue')
                            },
                            success: function (info) {
                                info = $.parseJSON(info);
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg
                                });
                                if (info.success) {
                                    $('#deliver-goods-win').dialog('close');
                                    $('#order-handle-win').attr('src', $('#order-handle-win').attr('src'));
                                    $('#order_dg').datagrid('load');
                                }
                            }
                        });
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#deliver-goods-win').dialog('close');
                    }
                }],
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
        订单号：<input class="easyui-textbox" id="order-code"/> &nbsp;&nbsp;&nbsp;&nbsp;
        买家账号：<input class="easyui-textbox" id="user-phone"/>&nbsp;&nbsp;&nbsp;&nbsp;
        订单生成时间：
        从&nbsp;
        <input id="start-time" type="text" class="easyui-datebox" data-options="editable:false"/>
        &nbsp;至&nbsp;
        <input id="end-time" type="text" class="easyui-datebox" data-options="editable:false"/>&nbsp;&nbsp;
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

<!--修改收货信息开始-->
<div id="edit-receive-info">
    <ur class="edit-order">
        <li>
            <div class="info-title">
                收件人姓名：
            </div>
            <div class="info-content">
                <input type="text" class="easyui-textbox" data-options="required:true" id="receiverName">
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="info-title">
                收件人电话：
            </div>
            <div class="info-content">
                <input type="text" class="easyui-textbox" data-options="required:true" id="receiverPhone">
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="info-title">
                详细收货地址：
            </div>
            <div class="info-content">
                <input type="text" class="easyui-textbox" data-options="multiline:true,required:true"
                       style="height: 60px" id="receiverAdd">
            </div>
            <div class="clear"></div>
        </li>
    </ur>
</div>
<!--修改收货信息结束-->

<!--发货-->
<div id="deliver-goods-win">
    <ur class="edit-order">
        <li>
            <div class="info-title">
                快递公司：
            </div>
            <div class="info-content">
                <input type="text" class="easyui-textbox" data-options="required:true" id="carriageMethod">
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="info-title">
                快递单号：
            </div>
            <div class="info-content">
                <input type="text" class="easyui-textbox" data-options="required:true" id="carriageNum">
            </div>
            <div class="clear"></div>
        </li>
    </ur>
</div>
<!--发货-->

</body>
</html>
