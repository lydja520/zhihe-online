<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>待审核商品</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,Chrome=1">
    <meta name="renderer" content="webkit">
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(function () {
            $('#goods-grid').datagrid({
                url: '${basePath}/admin/api/goods/waitExamine/goodses',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                pageSize: 20,
                rownumbers: true,
                columns: [[
                    {field: '', checkbox: true, width: 100},
                    {
                        field: 'merchant', title: '商家', width: 100, align: 'center',
                        formatter: function (value) {
                            console.log(value);
                            if (value) {
                                return value.merchName
                            }
                        }
                    },
                    {field: 'goodsName', title: '商品名', width: 100, align: 'center'},
                    {field: 'goodsDesc', title: '商品描述', width: 100, align: 'center'},
                    {
                        field: 'goodsAttributeSet', title: '商品所属分类', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return data.goodsAttSetName;
                            }
                        }
                    },
                    {field: 'displayState', title: '状态', width: 100, align: 'center'},
                    {
                        field: 'carriageMethod', title: '快递方式', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return "<span style='color: green;'>" + value + "</span>";
                            }
                            return "<span style='color: red'>无</span>";
                        }
                    },
                    {
                        field: 'carriage', title: '运费', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {
                        field: 'price', title: '价格', width: 100, align: 'center', sortable: true,
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {field: 'stock', title: '原始库存量', width: 50, align: 'center'},
                    {field: 'volume', title: '销量', width: 50, align: 'center', sortable: true},
                    {field: 'currentStock', title: '现有库存量', width: 50, align: 'center', sortable: true},
                    {field: 'createDate', title: '添加时间', width: 100, align: 'center', sortable: true}
                ]],
                toolbar: '#grid-toolbar'
            });
            /**
             *审核按钮点击事件
             */
            $('#examine-btn').click(function () {
                var rows = $('#goods-grid').datagrid('getChecked');
                if (rows.length != 1) {
                    $.messager.alert('提示', '请选择需要操作的数据！', 'info');
                    return;
                }
                previewDetails(rows[0].goodsId);
            });
            $('#grid-searcher-box').searchbox({
                searcher: function (value, name) {
                    var param = {};
                    if (name && value && $.trim(value) != "") {
                        param.searchName = name;
                        param.searchValue = value;
                    }
                    $('#goods-grid').datagrid('load', param);
                },
                menu: '#search-box-menu',
                prompt: '输入关键字搜素'
            })
        });
        /*========点击预览商品详细信息========*/
        function previewDetails(goodsId) {
            $('#goods-info-dialog').dialog({
                title: '商品详细信息',
                iconCls: 'icon-preview',
                width: 800,
                height: 480,
                modal: true,
                buttons: [{
                    text: '通过',
                    iconCls: 'icon-ok',
                    handler: function () {
                        if (goodsId) {
                            examineGoods(goodsId);
                        }
                    }
                }, {
                    text: '不通过',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        if (goodsId) {
                            showExamineMsgDialog(goodsId);
                        }
                    }
                }],
                onBeforeOpen: function () {
                    $('#goods-info-dialog').attr('src', "/admin/api/goods/goodsDetail/" + goodsId);
                }
            });
        }

        /**
         * 开始审核商品
         * @param goodsId
         */
        function examineGoods(goodsId) {
            $.messager.progress();
            $.ajax({
                url: '${basePath}/admin/api/goods/examine',
                type: 'POST',
                dataType: 'text',
                data: {
                    goodsId: goodsId
                },
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.code == 200) {
                        $.messager.show({
                            title: '提示',
                            msg: '操作成功！'
                        });
                        $('#goods-info-dialog').dialog('close');
                        $('#goods-grid').datagrid('load');
                    } else {
                        $.messager.alert('提示', data.msg, 'info');
                    }
                },
                error: function () {
                    $.messager.alert('提示', '操作失败，请重试！', 'error');
                },
                complete: function () {
                    $.messager.progress('close');
                }
            })
        }

        /**
         * 审核不通过时显示填写不通过原因
         */
        function showExamineMsgDialog(goodsId) {
            $('#examine-msg-dialog').dialog({
                title: '不通过原因',
                width: 320,
                height: 200,
                modal: true,
                buttons: [{
                    text: '确认',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var msg = $('#examine-msg').val();
                        if (!msg || $.trim(msg) == "") {
                            $.messager.alert('提示', '请填写不通过原因', 'info');
                            return;
                        }
                        $('#examine-msg-dialog').dialog('close');
                        unExamineGoods(goodsId, msg);
                    }
                }],
                onBeforeOpen: function () {
                    $('#examine-msg').val('');
                }
            })
        }

        /**
         * 审核未通过
         *
         * @param goodsId
         */
        function unExamineGoods(goodsId, msg) {
            $.messager.progress();
            $.ajax({
                url: '${basePath}/admin/api/goods/unExamine',
                type: 'POST',
                dataType: 'text',
                data: {
                    goodsId: goodsId,
                    examineMsg: msg
                },
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.code == 200) {
                        $.messager.show({
                            title: '提示',
                            msg: '操作成功！'
                        });
                        $('#goods-info-dialog').dialog('close');
                        $('#goods-grid').datagrid('load');
                    } else {
                        $.messager.alert('提示', data.msg, 'info');
                    }
                },
                error: function () {
                    $.messager.alert('提示', '操作失败，请重试！', 'error');
                },
                complete: function () {
                    $.messager.progress('close');
                }
            })
        }
    </script>
</head>
<body>
<table id="goods-grid"></table>
<div id="grid-toolbar">
    <button class="easyui-linkbutton" id="examine-btn" iconCls="icon-preview">审核</button>
    <input id="grid-searcher-box" style="width: 300px">
</div>
<div id="search-box-menu">
    <div data-options="name:'goodsName'">商品名称</div>
    <div data-options="name:'merchant.merchName'">商家名称</div>
</div>
<!--商品详细信息开始-->
<iframe src="" id="goods-info-dialog"></iframe>
<!--商品详细信息结束-->

<!--审核未通过原因-->
<div id="examine-msg-dialog">
    <table class="form-table">
        <tr>
            <td>原因：</td>
            <td>
                <textarea id="examine-msg" rows="6" cols="30"></textarea>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
