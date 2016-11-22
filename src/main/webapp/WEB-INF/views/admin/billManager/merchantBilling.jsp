<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/4/8
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家账单结算</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/utils.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/merchantBill/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                pageSize: 20,
                columns: [[
                    {field: 'billId', checkbox: true, width: 100, align: 'center'},
                    {
                        field: 'aaa', title: '商家头像', width: 100, align: 'center',
                        formatter: function (value, row) {
                            return "<img src='" + row.merchant.coverImg.url + "' style='width:40px;height:40px;margin:1px auto;border-radius:25px'><div style='height: 20px;line-height: 20px;'>" + row.merchant.merchName + "</div>";
                        }
                    },
                    {field: 'batchCode', title: '账单批次', width: 60, align: 'center', sortable: true},
                    {field: 'createDate', title: '账单生成日期', width: 100, align: 'center', sortable: true},
                    {
                        field: 'ccc', title: '账单所属时间', width: 100, align: 'center', width: 200,
                        formatter: function (index, row, value) {
                            return row.startDate + "(周一) - " + row.endDate + "(周日)";
                        }
                    },
                    {field: 'billCode', title: '账单号', width: 100, align: 'center'},
                    {
                        field: 'amount', title: '产生营业额', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + "元";
                        }
                    },
                    {
                        field: 'poundage', title: '手续费', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + '元';
                        }
                    },
                    {
                        field: 'handlePoundageRate', title: '手续费利率', width: 100, align: 'center'
                    },
                    {
                        field: 'realAmount', title: '实际应转账', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + "元";
                        },
                        styler: function (value, row, index) {
                            return 'background-color:#ffee00;color:red;';
                        }
                    },
                    {field: 'aliPayAccount', title: '商家支付宝账号', width: 100, align: 'center'},
                    {
                        field: 'handleState', title: '是否已经处理', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return "<span style='color: green'>已处理</span>";
                            }
                            return "<span style='color: red'>未处理</span>";
                        }
                    },
                    {
                        field: 'handleDate', title: '账单处理日期', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value == null) {
                                return "未处理";
                            }
                            return value;
                        }
                    },
                    {
                        field: 'aliPayTransactionCode', title: '支付宝交易号', width: 100, align: 'center'
                    },
                    {
                        field: 'bbb', title: '查看详细信息', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            return "<button onclick='previewInfo(this.value)' value='" + row.billId + "'>详细信息</button>";
                        }
                    },
                    {
                        field: 'ddd', title: '导出账单详细', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            return "<button onclick='exportOrder(this.value)' value='" + row.billId + "'>导出订单详细</button>";
                        }
                    },
                ]],
                toolbar: [{
                    text: '账单处理',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var rows = $('#dg').datagrid('getChecked');
                        if (rows.length > 0) {
                            var bill = rows[0];
                            console.log(bill);
                            $('#edit-merchant-bill').dialog({
                                title: '处理账单',
                                width: '450',
                                height: '200',
                                modal: true,
                                buttons: [{
                                    text: '确认操作',
                                    iconCls: 'icon-ok',
                                    handler: function () {
                                        $('#edit-form').form({
                                            url: '${basePath}/admin/api/merchanBill/confirmCheckOut',
                                            success: function (info) {
                                                info = $.parseJSON(info);
                                                $.messager.show({
                                                    title: '提示',
                                                    msg: info.msg
                                                });
                                                if (info.success) {
                                                    $('#edit-merchant-bill').dialog('close');
                                                    $('#dg').datagrid('load');
                                                }
                                            }
                                        });
                                        $('#edit-form').submit();
                                    }
                                }, {
                                    text: '取消',
                                    iconCls: 'icon-cancel',
                                    handler: function () {
                                        $('#edit-merchant-bill').dialog('close');
                                    }
                                }],
                                onBeforeOpen: function () {
                                    $('#merchant-name').html(bill.merchant.merchName);
                                    $('#bill-code').html(bill.billCode);
                                    $('#alipay-code').html(bill.merchant.alipayCode);
                                    $('#billId').val(bill.billId);
                                }
                            });
                        } else {
                            $.messager.alert('提示', '请选中一条数据进行处理！', 'info');
                        }
                    }
                }],
                onClickRow: function (index, row) {
                    $('#amount').html(row.realAmount + '元');
                    $('#order-amount').html(row.orderAmount + '件');
                }
            });

            $('#order_dg').datagrid('load', {});

            /**
             *  执行搜素
             */
            $('#search').click(function () {
                $('#dg').datagrid('load', {
                    billCode: $('#billCode').textbox('getValue'),
                    'merchant.merchName': $('#merchantName').textbox('getValue'),
                    handleState: $('#handleState').combobox('getValue'),
                    startTime: $('#start-time').textbox('getValue'),
                    endTime: $('#end-time').textbox('getValue'),
                });
            });

            /**
             * 清除搜索
             */
            $('#clear-search').click(function () {
                $('#search-form').form('clear');
                $('#handleState').combobox('reset');
                $('#dg').datagrid('load', {});
            });

            $('#select').click(function () {
                $('#preview_icon').attr('src', '${basePath}/static/images/preview.jpg');
                $('#progress')[0].innerHTML = '';
            });

            $('#export').click(function () {
                $.messager.confirm('确认导出', '确认要导出满足查询条件的账单吗？', function (r) {
                    if (r) {
                        var paramAndValue = "?billCode=" + $('#billCode').textbox('getValue') + "&merchant.merchName=" + $('#merchantName').textbox('getValue')
                                + "&startTime=" + $('#start-time').textbox('getValue') + "&endTime=" + $('#end-time').textbox('getValue');
                        window.location.href = "${basePath}/admin/api/merchantBill/export" + paramAndValue;
                    }
                });
            });
        });

        function previewInfo(billId) {
            $('#previewInfo-win').dialog({
                title: '账单中包含的订单',
                width: 900,
                height: 480,
                modal: true,
                onBeforeOpen: function () {
                    $('#previewInfo-win').attr('src', '${basePath}/admin/merchantBill/' + billId + '/merchantBillDetails');
                }
            });
        }

        function exportOrder(billId) {
            $.messager.confirm('确认导出', '确认导出该商家的账单详细数据吗？', function (r) {
                if (r) {
                    window.location.href = "${basePath}/admin/api/merchantBillOrderDetails/export?billId=" + billId;
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

        ul {
            margin: 0px;
            padding: 0px;
            list-style-type: none;
        }

        ul li {
            margin: 5px 0px 0px 0px;
        }

        #bill-info {
            list-style-type: none;
            width: 80%;
            margin: 0px auto;
            padding: 0px;
        }

        #bill-info li {
            height: 20px;
            line-height: 20px;
            padding: 0px;
            float: left;
        }

        .form-title {
            width: 30%;
        }

        .form-content {
            width: 70%;
            text-align: center;
        }

        .clear {
            clear: both;
        }

        .easyui-textbox, .easyui-datebox {
            width: 150px;
        }
    </style>
</head>
<body class="easyui-layout">
<div data-options="region:'north',split:false,border:true,title:'商家账单结算',collapsible:false" style="height:100px;">
    <div style="margin:7px auto 5px 10px">
        <div style="width: 130px;height: 50px;border: solid 1px #95b8e7;float: left;border-radius: 5px">
            <div style="height: 25px;line-height: 25px;text-align: center;color: #ff5500">
                订单数
            </div>
            <div style="height: 25px;line-height: 25px;text-align: center" id="order-amount">
                未选择
            </div>
            <div class="clear"></div>
        </div>
        <div style="width: 130px;height: 50px;border: solid thin #95b8e7;float: left;margin-left: 20px;border-radius: 5px">
            <div style="height: 25px;line-height: 25px;text-align: center;color:#ff5500;">
                商家应得
            </div>
            <div style="height: 25px;line-height: 25px;text-align: center" id="amount">
                未选择
            </div>
            <div class="clear"></div>
        </div>
        <div id="tb" style="float: left;margin: 15px auto auto 10px">
            <form id="search-form" accept-charset="UTF-8">
                账单号:<input name="billCode" class="easyui-textbox" id="billCode" prompt="请输入账单单号"/>&nbsp;
                商家名:<input name="merchant.merchName" class="easyui-textbox" id="merchantName" prompt="请输入商家名"/>&nbsp;
                是否转账:
                <select id="handleState" class="easyui-combobox" name="handleState"
                        data-options="editable:false,panelHeight:100" style="width: 80px">
                    <option value="0">全部</option>
                    <option value="1">已处理</option>
                    <option value="2">未处理</option>
                </select>&nbsp;
                账单生成时间：从<input name="startTime" id="start-time" type="text" class="easyui-datebox"
                               data-options="editable:false" prompt="开始时间"/>
                至<input id="end-time" name="endTime" type="text" class="easyui-datebox"
                        data-options="editable:false" prompt="结束时间"/>
                <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索</a>
                <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清除搜索</a>
                <a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-save'">导出</a>
                <div class="clear"></div>
            </form>
        </div>
        <div style="clear: both"></div>
    </div>
</div>

<div data-options="region:'center',split:false,border:false">
    <div id="dg"></div>

    <div id="edit-merchant-bill">
        <form id="edit-form" accept-charset="UTF-8">
            <input type="hidden" name="billId" id="billId">
            <input type="hidden" name="proofImg.imgInfoId" id="pic_id"/>
            <ul id="bill-info">
                <li class="form-title">商家名：</li>
                <li id="merchant-name" class="form-content"></li>
                <li class="form-title">账单号：</li>
                <li id="bill-code" class="form-content"></li>
                <li class="form-title">商家支付宝账号：</li>
                <li id="alipay-code" class="form-content"></li>
                <hr>
                <li class="form-title">支付宝交易号:</li>
                <li id="aliPay-transaction-code" style="width: 70%">
                    <input class="easyui-textbox" name="aliPayTransactionCode" style="width: 90%"
                           data-options="required:true">
                </li>
            </ul>
        </form>
    </div>

    <iframe id="previewInfo-win"></iframe>
</div>

</body>
</html>

