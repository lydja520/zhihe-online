<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/6/29
  Time: 13:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>订单价格审核</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/style.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            /**
             * 初始化对账单数据网格
             */
            $('#order_examine_grid').datagrid({
                url: '${basePath}/admin/api/orderExamine/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                pageSize: 20,
                toolbar: '#grid_tool_bar',
                columns: [[
                    {field: 'examineId', checkbox: true},
                    {field: 'batchCode', title: '账单批次', align: 'center', width: 60},
                    {field: 'payOrderCode', title: '交易订单号', align: 'center', width: 100},
                    {field: 'orderCode', title: '平台订单号', align: 'center', width: 200},
                    {field: 'payAmount', title: '支付金额(元)', align: 'center', width: 60},
                    {field: 'orderAmount', title: '订单金额(元)', align: 'center', width: 60},
                    {
                        field: 'batchOrder', title: '是否是批量订单', align: 'center', width: 60,
                        formatter: function (value) {
                            if (value) {
                                return '<span style="color:red;">是</span>';
                            }
                            return '不是';
                        }
                    },
                    {
                        field: 'examineOk', title: '是否相等', align: 'center', width: 80,
                        formatter: function (value) {
                            if (value) {
                                return '相等';
                            }
                            return '<span style="color:red;">不相等</span>';
                        }
                    },
                    {field: 'examineMsg', title: '审核信息', align: 'left', width: 120}
                ]]
            });
            /**
             * 初始化账单批次下拉列表
             */
            $('#batch-code').combobox({
                url: '${basePath}/admin/api/merchantBill/batchCode/list',
                editable: false,
                width: 100,
                require: true,
                valueField: 'value',
                textField: 'text',
                onLoadSuccess: function () {
                    var data = $('#batch-code').combobox('getData');
                    $('#batch-code').combobox('setValue', data[0].value);
                }
            });
            /**
             * 初始化查询按钮点击事件
             */
            $('#search_btn').click(function () {
                var param = {
                    batchCode: $('#batch-code').combobox('getValue'),
                    payType: $('#pay-type').combobox('getValue')
                };
                var payOrderCode = $('#pay-order-code').val();
                if (payOrderCode) {
                    param.searchValue = payOrderCode;
                    param.searchName = 'payOrderCode';
                }
                $('#order_examine_grid').datagrid('load', param);
            });

            /**
             * 初始化上传账单按钮点击事件
             */
            $('#upload_bill_data_btn').click(function () {
                var batchCode = $('#batch-code').combobox('getValue');
                var payType = $('#pay-type').combobox('getValue');
                $('#form-batch-code').val(batchCode);
                $('#form-pay-type').val(payType);
                $('#upload-dialog').dialog('open');
            });

            /**
             * 初始化上传账单弹出框
             */
            $('#upload-dialog').dialog({
                onBeforeOpen: function () {
                    $('#bill-file').filebox('clear');
                    $('#tip-container').html('');
                    var batchCode = $('#form-batch-code').val();
                    var payType = $('#form-pay-type').val();
                    $.messager.progress();
                    $.ajax({
                        url: '${basePath}/admin/api/orderExamine/minAndMaxDate',
                        type: 'get',
                        dataType: 'text',
                        data: {
                            batchCode: batchCode,
                            payType: payType
                        },
                        success: function (data) {
                            data = $.parseJSON(data);
                            if (data.code != 200) {
                                return;
                            }
                            var payName = payType == 'wx' ? '微信' : '支付宝';
                            var tipMsg = '请上传 ' + data.attribute.min +
                                    ' 至 ' + data.attribute.max + ' 时间段内的\"' + payName + '\"账单';
                            $('#tip-container').html(tipMsg);
                        },
                        complete: function () {
                            $.messager.progress('close');
                        }
                    })
                },
                buttons: [{
                    text: '上传',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var batchCode = $('#form-batch-code').val();
                        var payType = $('#form-pay-type').val();
                        uploadBill(batchCode, payType);
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#upload-dialog').dialog('close');
                    }
                }]
            });
        });
        /**
         * 上传账单
         */
        function uploadBill(batchCode, payType) {
            $('#upload-form').form('submit', {
                onSubmit: function () {
                    $.messager.progress();
                    return true;
                },
                success: function (data) {
                    $.messager.progress('close');
                    data = $.parseJSON(data);
                    if (data.code != 200) {
                        $.messager.alert('提示', data.msg, 'error');
                        return;
                    }
                    $('#upload-dialog').dialog('close');
                    $.messager.show({
                        title: '提示',
                        msg: '对账完成'
                    });
                    $('#order_examine_grid').datagrid('load', {
                        batchCode: batchCode,
                        payType: payType
                    })
                }
            });
        }
    </script>
</head>
<body>
<table id="order_examine_grid"></table>
<div id="grid_tool_bar">

    账单批次：<input id="batch-code" name="batchCode">
    <span style="width: 10px;display: inline-block;"></span>
    支付方式：
    <select id="pay-type" name="payType" class="easyui-combobox" style="width: 100px;" panelHeight="100"
            editable="false">
        <option value="wx">微信</option>
        <option value="alipay">支付宝</option>
    </select>
    <span style="width: 10px;display: inline-block;"></span>
    交易订单号：<input id="pay-order-code" class="easyui-validatebox" data-options="required:false">
    <span style="width: 10px;display: inline-block;"></span>
    <button class="easyui-linkbutton" id="search_btn" iconCls="icon-search">查询</button>
    <span style="width: 30px;display: inline-block;"></span>
    <button class="easyui-linkbutton" id="upload_bill_data_btn">上传账单</button>
</div>
<div id="upload-dialog" class="easyui-dialog" title="上传账单" style="width:360px;height:220px;"
     data-options="iconCls:'icon-info',resizable:false,modal:true,closed:true">
    <form id="upload-form" method="post" action="${basePath}/admin/api/payBill/upload"
          enctype="multipart/form-data">
        <table class="form-table">
            <tr>
                <td colspan="2">
                    <p id="tip-container"></p>
                </td>
            </tr>
            <tr>
                <td>选择账单:</td>
                <td>
                    <input id="form-batch-code" type="hidden" name="batchCode">
                    <input id="form-pay-type" type="hidden" name="payType">
                    <input id="bill-file" class="easyui-filebox" name="billFile" style="width:200px" buttonText="选择文件"
                           buttonAlign="right"
                           required="true">
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
