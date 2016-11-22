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
                url: '${basePath}/admin/currentMerchant/merchantBill/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                columns: [[
                    {field: 'createDate', title: '账单生成日期', width: 100, align: 'center'},
                    {
                        field: 'ccc', title: '账单所属时间', width: 200, align: 'center', width: 200,
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
                        field: 'handlePoundageRate', title: '转账手续费利率', width: 100, align: 'center'
                    },
                    {
                        field: 'realAmount', title: '实际应转账', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + "元";
                        },
                        styler: function (value, row, index) {
                        if (value < 20) {
                            return 'background-color:#ffee00;color:red;';
                        }
                    }
                    },
                    {
                        field: 'handleState', title: '是否已经处理', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return "已经处理";
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
                ]],
                onClickRow: function (index, row) {
                    $('#amount').html(row.realAmount + '元');
                    $('#order-amount').html(row.orderAmount + '件');
                }
            });

            $('#order_dg').datagrid('load', {
//                searchOrderCode: $('#order-code').textbox('getValue'),
//                searchOrderState: $('#order-state').combobox('getValue'),
//                searchCarriageNum: $('#carriage-num').textbox('getValue'),
//                searchUserName: $('#user-name').textbox('getValue'),
//                searchStartTime: $('#start-time').datebox('getValue'),
//                searchEndTime: $('#end-time').datebox('getValue')
            });

            /**
             *  执行搜素
             */
            $('#search').click(function () {
                $('#dg').datagrid('load', parseFormJSON('search-form'));
            });

            /**
             * 清除搜索
             */
            $('#clear-search').click(function () {
                $('#search-form').form('clear');
                $('#dg').datagrid('load', {});
            });

            $('#select').click(function () {
                $('#preview_icon').attr('src', '${basePath}/static/images/preview.jpg');
                $('#progress')[0].innerHTML = '';
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
        </div>
        <div style="width: 130px;height: 50px;border: solid thin #95b8e7;float: left;margin-left: 20px;border-radius: 5px">
            <div style="height: 25px;line-height: 25px;text-align: center;color:#ff5500;">
                商家应得
            </div>
            <div style="height: 25px;line-height: 25px;text-align: center" id="amount">
                未选择
            </div>
        </div>
        <div id="tb" style="float: left;margin: 15px auto auto 10px">
            <form id="search-form">
                账单号:<input name="billCode" class="easyui-textbox" id="order-code" prompt="请输入账单单号"/>
                账单生成时间：从<input name="startTime" id="start-time" type="text" class="easyui-datebox"
                               data-options="editable:false" prompt="开始时间"/>
                至<input id="end-time" name="endTime" type="text" class="easyui-datebox"
                        data-options="editable:false" prompt="结束时间"/>
                <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索</a>
                <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清除搜索</a>

                <div class="clear"></div>
            </form>
        </div>
        <div style="clear: both"></div>
    </div>
</div>

<div data-options="region:'center',split:false,border:false">
    <div id="dg"></div>

    <iframe id="previewInfo-win"></iframe>
</div>

</body>
</html>

