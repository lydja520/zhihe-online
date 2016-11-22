<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/8/19
  Time: 11:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>APP滚动信息设置</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/scroNews/list',
                title: 'APP首页滚动信息',
                fit: true,
                fitColumns: true,
                singleSelect: true,
                pagination: true,
                columns: [[
                    {field: 'newsId', checkbox: true, align: 'center'},
                    {field: 'newsContent', title: '滚动信息', width: 100, align: 'center'},
                    {
                        field: 'jumpType', title: '跳转类型', width: 100, align: 'center',
                        formatter: function (data) {
                            switch (data) {
                                case 'goods':
                                    return "商品";
                                    break;
                                case 'merchant':
                                    return '商家';
                                    break;
                                case 'url':
                                    return '页面地址'
                                    break;
                            }
                        }
                    },
                    {field: 'jumpTarget', title: '跳转目标', width: 100, align: 'center'}
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: addScroNews
                }, '-', {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: editScroNew
                }, '-', {
                    text: '删除',
                    iconCls: 'icon-cancel',
                    handler: deleteScroNews
                }]
            });
        })

        function addScroNews() {
            $('#add-edit-win').dialog({
                width: 350,
                title: '添加滚动信息',
                buttons: [{
                    text: '确认',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#add-edit-form').form('submit', {
                            url: '${basePath}/admin/api/scroNews/add',
                            onSubmit: function () {
                                return $('#add-edit-form').form('validate');
                            },
                            success: function (data) {
                                data = $.parseJSON(data);
                                if (data.success) {
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    });
                                    $('#add-edit-win').dialog('close');
                                    $('#dg').datagrid('reload');
                                } else {
                                    $.messager.alert('错误提示', data.msg, 'error');
                                }
                            },
                            error: function (request, status, error) {
                                $.messager.alert('错误提示', request.responseText, 'error');
                            }
                        });
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#add-edit-win').dialog('close');
                    }
                }],
                onBeforeOpen: function () {
                    $('#add-edit-form').form('reset');
                    $('#select-goods-tr').empty();
                    $('#select-merchnat-tr').empty();
                    $('#select-url-tr').empty();
                    selectGoods();
                }
            });
        }

        function editScroNew() {
            var rows = $('#dg').datagrid('getSelections');
            if (rows.length <= 0) {
                $.messager.alert('提示', '请选择一条数据进行编辑', 'info');
                return;
            }
            var scroNews = rows[0];
            $('#add-edit-win').dialog({
                width: 350,
                title: '修改滚动信息',
                buttons: [{
                    text: '确认',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#add-edit-form').form('submit', {
                            url: '${basePath}/admin/api/scroNews/edit',
                            onSubmit: function () {
                                return $('#add-edit-form').form('validate');
                            },
                            success: function (data) {
                                data = $.parseJSON(data);
                                if (data.success) {
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    });
                                    $('#add-edit-win').dialog('close');
                                    $('#dg').datagrid('reload');
                                } else {
                                    $.messager.alert('错误提示', data.msg, 'error');
                                }
                            },
                            error: function (request, status, error) {
                                $.messager.alert('错误提示', request.responseText, 'error');
                            }
                        });
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#add-edit-win').dialog('close');
                    }
                }],
                onBeforeOpen: function () {
                    $('#add-edit-form').form('load', scroNews);
                    $('#select-goods-tr').empty();
                    $('#select-merchnat-tr').empty();
                    $('#select-url-tr').empty();
                    switch (scroNews.jumpType) {
                        case 'goods':
                            selectGoods();
                            $('#jump-type-goods').textbox('setValue', scroNews.jumpTarget);
                            $('#jump-type-goods').textbox('setText', scroNews.jumpTarget);
                            /*                            $('#rec-name').textbox('setValue', goods.goodsName);*/
                            break;
                        case 'merchant':
                            selectMerchant();
                            $('#jump-type-merchant').textbox('setValue', scroNews.jumpTarget);
                            $('#jump-type-merchant').textbox('setText', scroNews.jumpTarget);
                            break;
                        case 'url':
                            selectUrl();
                            $('#jump-type-url').textbox('setValue', scroNews.jumpTarget);
                            break;
                    }
                }
            });
        }

        function deleteScroNews() {
            var rows = $('#dg').datagrid('getSelections');
            if (rows.length <= 0) {
                $.messager.alert('提示', '请选择一条数据进行编辑', 'info');
                return;
            }
            var scroNews = rows[0];
            $.messager.confirm('删除提示', '确认要删除该条滚动信息吗?', function (r) {
                if (r) {
                    $.ajax({
                        url: '${basePath}/admin/api/scroNews/del',
                        type: 'post',
                        data: {
                            newId: scroNews.newsId
                        },
                        dataType: 'text',
                        success: function (data) {
                            data = $.parseJSON(data);
                            if (data.success) {
                                $.messager.show({
                                    title: '提示',
                                    msg: data.msg
                                });
                                $('#dg').datagrid('reload');
                            } else {
                                $.messager.alert('错误提示', data.msg, 'error');
                            }
                        },
                        error: function (request, status, error) {
                            $.messager.alert('错误提示', request.responseText, 'error');
                        }
                    });
                }
            });
        }

        function selectGoods() {
            $('#select-goods-tr').append("<td>选择商品：</td> <td> <input type='text' id='jump-type-goods' name='jumpTarget' required></td>")
            $('#select-merchnat-tr').empty();
            $('#select-url-tr').empty();
            $('#jump-type-goods').textbox({
                buttonText: '选择商品',
                onClickButton: function () {
                    $('#goods-win').dialog({
                        width: 800,
                        height: 480,
                        title: '选择商品',
                        modal: true,
                        iconCls: 'icon-add',
                        onBeforeOpen: function () {
                            $('#goods-dg').datagrid({
                                url: '${basePath}/admin/api/recGoodsForAdmin/select',
                                fit: true,
                                striped: true,
                                fitColumns: true,
                                pagination: true,
                                singleSelect: true,
                                pageSize: 10,
                                border: false,
                                rownumbers: true,
                                columns: [[
                                    {field: 'goodsId', checkbox: true, align: 'center'},
                                    {
                                        field: 'coverImg', title: '封面图', width: 100, align: 'center',
                                        formatter: function (data) {
                                            if (data) {
                                                var url = data.domain + data.key;
                                                return "<div style='width: 40px;height: 40px;margin: 1px auto 1px auto'><img style='width: 40px;height: 40px;' src='" + url + "'></div>"
                                            }
                                            return "<span style='color: red' '>无封面图</span>"
                                        }
                                    },
                                    {field: 'goodsName', title: '商品名', width: 100, align: 'center'},
                                    {
                                        field: 'merchantName', title: '商家', width: 100, align: 'center',
                                        formatter: function (value, row, index) {
                                            return row.merchant.merchName;
                                        }
                                    },
                                    {
                                        field: 'goodsAttributeSet', title: '商品所属分类', width: 100, align: 'center',
                                        formatter: function (data) {
                                            if (data) {
                                                return data.goodsAttSetName;
                                            }
                                        }
                                    },
                                    {
                                        field: 'onsale', title: '在售状态', width: 100, align: 'center', sortable: true,
                                        formatter: function (value) {
                                            if (value) {
                                                return '<span style="color: green">上架</span>';
                                            }
                                            return "<span style='color: red'>下架</span>";
                                        }
                                    },
                                    {
                                        field: 'price', title: '价格', width: 100, align: 'center', sortable: true,
                                        formatter: function (value) {
                                            return value + " 元";
                                        }
                                    },
                                    {
                                        field: 'volume', title: '销量', width: 100, align: 'center', sortable: true,
                                        formatter: function (value) {
                                            return value + " 件";
                                        }
                                    },
                                    {
                                        field: 'currentStock',
                                        title: '库存量',
                                        width: 100,
                                        align: 'center',
                                        sortable: true,
                                        formatter: function (value) {
                                            return value + " 件";
                                        }
                                    },
                                    {field: 'createDate', title: '添加时间', width: 100, align: 'center', sortable: true,},
                                ]],
                                toolbar: '#gods-select-toolbar'
                            });
                            /*========商品搜索========*/
                            $('#ss').searchbox({
                                searcher: function (value, name) {
                                    $('#goods-dg').datagrid('reload', {
                                        searchName: name,
                                        searchValue: value,
                                    });
                                },
                                menu: '#mm',
                                width: 300,
                                prompt: '请输入关键字进行搜索'
                            });
                        }
                    });
                }
            });
        }

        function selectMerchant() {
            $('#select-goods-tr').empty();
            $('#select-merchnat-tr').append("<td>选择商家：</td><td><input type='text' id='jump-type-merchant' name='jumpTarget' class='easyui-textbox' required></td>");
            $('#select-url-tr').empty();
            $('#jump-type-merchant').textbox({
                buttonText: '选择商家',
                onClickButton: function () {
                    $('#merchant-win').dialog({
                        width: 800,
                        height: 480,
                        title: '选择店铺',
                        modal: true,
                        iconCls: 'icon-add',
                        onBeforeOpen: function () {
                            $('#merchant-dg').datagrid({
                                url: '${basePath}/admin/api/recMerchant/select',
                                fit: true,
                                striped: true,
                                fitColumns: true,
                                pagination: true,
                                singleSelect: true,
                                pageSize: 10,
                                border: false,
                                rownumbers: true,
                                columns: [[
                                    {field: 'merchantId', checkbox: true, align: 'center'},
                                    {
                                        field: 'coverImg', title: '封面图', width: 100, align: 'center',
                                        formatter: function (data) {
                                            if (data) {
                                                var url = data.domain + data.key;
                                                return "<div style='width: 40px;height: 40px;margin: 1px auto 1px auto'><img style='width: 40px;height: 40px;' src='" + url + "'></div>"
                                            }
                                            return "<span style='color: red' '>无封面图</span>"
                                        }
                                    },
                                    {field: 'merchName', title: '商家名', width: 100, align: 'center'},
                                    {field: 'contactName', title: '联系人', width: 100, align: 'center'},
                                    {field: 'contactMobileNO', title: '联系人电话号码', width: 100, align: 'center'},
                                    {field: 'merchTell', title: '企业联系电话', width: 100, align: 'center'},
                                    {field: 'address', title: '住址', width: 100, align: 'center'},
                                    {field: 'createDate', title: '入驻时间', width: 100, align: 'center', sortable: true,},
                                ]],
                                toolbar: '#merchant-select-toolbar'
                            });
                            /*========商家搜索========*/
                            $('#sss').searchbox({
                                searcher: function (value, name) {
                                    $('#merchant-dg').datagrid('reload', {
                                        searchName: name,
                                        searchValue: value,
                                    });
                                },
                                menu: '#mmm',
                                width: 300,
                                prompt: '请输入关键字进行搜索'
                            });
                        }
                    });
                }
            });
        }

        function selectUrl() {
            $('#select-goods-tr').empty();
            $('#select-merchnat-tr').empty();
            $('#select-url-tr').append("<td>跳转地址：</td><td><input type='text' id='jump-type-url' name='jumpTarget' class='easyui-textbox' required></td>");
            $('#jump-type-url').textbox({});
        }

        function changeType(value) {
            if (value == 'goods') {
                selectGoods();
            } else if (value == 'merchant') {
                selectMerchant();
            } else if (value == 'url') {
                selectUrl();
            }
        }

        function selectGoodsConfirm() {
            var rows = $('#goods-dg').datagrid('getSelections');
            if (rows.length <= 0) {
                $.messager.alert('提示', '请先选中一个商品', 'info');
                return;
            }
            var goods = rows[0];
            $('#jump-type-goods').textbox('setValue', goods.goodsId);
            $('#jump-type-goods').textbox('setText', goods.goodsName);
            $('#rec-name').textbox('setValue', goods.goodsName);
            $('#goods-win').dialog('close');
        }

        function selectMerchantConfirm() {
            var rows = $('#merchant-dg').datagrid('getSelections');
            if (rows.length <= 0) {
                $.messager.alert('提示', '请先选中一个商家', 'info');
                return;
            }
            var merchant = rows[0];
            $('#jump-type-merchant').textbox('setValue', merchant.merchantId);
            $('#jump-type-merchant').textbox('setText', merchant.merchName);
            $('#merchant-win').dialog('close');
        }

    </script>
    <style type="text/css">
        * {
            font-size: 12px;
            font-family: 微软雅黑;
        }
    </style>
</head>
<body>

<table id="dg"></table>

<div id="add-edit-win">
    <form id="add-edit-form" style="margin: 20px auto" method="post">
        <input name="newsId" type="hidden">
        <table align="center">
            <tr>
                <td>滚动内容：</td>
                <td><input class="easyui-textbox" name="newsContent" required></td>
            </tr>
            <tr>
                <td>
                    跳转类型：
                </td>
                <td>
                    <input type="radio" name="jumpType" value="goods" checked onclick="changeType(this.value)">商品
                    <input type="radio" name="jumpType" value="merchant" onclick="changeType(this.value)">商家
                    <input type="radio" name="jumpType" value="url" onclick="changeType(this.value)">网页
                </td>
            </tr>

            <tr id="select-goods-tr"></tr>
            <tr id="select-merchnat-tr"></tr>
            <tr id="select-url-tr"></tr>

        </table>
    </form>
</div>

<div id="goods-win">
    <table id="goods-dg"></table>
    <div id="gods-select-toolbar">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:false" id="select-goods-btn-confirm"
           onclick="selectGoodsConfirm()">选择</a>
        <input id="ss">
    </div>
    <div id="mm" style="width:120px">
        <div data-options="name:'goodsName',iconCls:'icon-bricks'">商品名</div>
        <div data-options="name:'merchant.merchName',iconCls:'icon-home'">商家名</div>
    </div>
</div>

<div id="merchant-win">
    <div id="merchant-select-toolbar">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:false"
           id="select-merchant-btn-confirm" onclick="selectMerchantConfirm()">选择</a>
        <input id="sss">
    </div>
    <div id="mmm" style="width:120px">
        <div data-options="name:'merchName',iconCls:'icon-home'">商家名</div>
    </div>
    <table id="merchant-dg"></table>
</div>

</body>
</html>
