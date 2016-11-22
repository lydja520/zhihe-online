<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-8-15
  Time: 上午9:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商品首页推荐</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <script type="text/javascript">
        $(function () {

            /*========上传封面图=======*/
            initUploader({
                pickbutton: 'select',
                onFileAdd: function (file) {

                },
                onError: function (file, err, errTip) {

                },
                onBeforeUpload: function (up, file) {
                    $('#preview_icon').attr('src', "/static/images/loading.gif");
                    $('#progress').html("<br>正在上传……");
                },
                onUploaded: function (up, file, info) {
                    $('#img-id').val(info.imgInfoId);
                    $('#preview_icon').attr('src', info.url);
                    $('#img-name').val(file.name);
                    $('#progress').html("<br>上传完成");
                },
                onComplete: function () {

                }
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

            /*========选择商品========*/
            $('#select-goods').textbox({
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
                        }
                    });
                }
            });

            $('#select-goods-btn-confirm').click(function () {
                var rows = $('#goods-dg').datagrid('getSelections');
                if (rows.length <= 0) {
                    $.messager.alert('提示', '请先选中一个商品', 'info');
                    return;
                }
                var goods = rows[0];
                $('#select-goods').textbox('setValue', goods.goodsId);
                $('#select-goods').textbox('setText', goods.goodsName);
                $('#rec-name').textbox('setValue', goods.goodsName);
                $('#goods-win').dialog('close');
            });

            /*========商品推荐数据网格========*/
            $('#dg').datagrid({
                url: '${basePath}/admin/api/recGoodsForAdmin/list',
                fit: true,
                fitColumns: true,
                singleSelect: true,
                rownumbers: true,
                pagination: true,
                columns: [[
                    {field: 'recId', checkbox: true},
                    {
                        field: 'coverImg', title: '推荐封面图', align: 'center', width: 100,
                        formatter: function (data, row, index) {
                            return "<div style='text-align: center;margin: 1px auto'><img style='width: 40px;height: 40px' src='" + data.url + "'><br>" + row.recName + "<div>";
                        }
                    },
                    {
                        field: 'goods', title: '商品', width: 100, align: 'center',
                        formatter: function (data) {
                            return "<div style='text-align: center;margin: 1px auto'><img style='width: 40px;height: 40px' src='" + data.coverImg.url + "'><br>" + data.goodsName + "<div>";
                        }
                    },
                    {
                        field: 'merchant', title: '商家', width: 100, align: 'center',
                        formatter: function (data, row, index) {
                            return row.goods.merchant.merchName;
                        }
                    },
                    {
                        field: 'desc', title: '描述', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return data;
                            } else {
                                return "无";
                            }
                        }
                    },
                    {field: 'recOrder', title: '顺序', width: 100, align: 'center'}
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        addRecommend();
                    }
                }, '-', {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                        editRecommend();
                    }
                }, '-', {
                    text: '删除',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        delRecommend();
                    }
                }]
            });
        });

        /*========添加推荐========*/
        function addRecommend() {
            $('#add-edit-win').dialog({
                width: 400,
                height: 320,
                title: '添加',
                iconCls: 'icon-add',
                modal: true,
                buttons: [{
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var imgName = $('#img-name').val();
                        if ($.trim(imgName) == '') {
                            $.messager.alert('提示', '请上传一张封面图', 'info');
                            return;
                        }
                        $('#add-edit-form').form('submit', {
                            url: '${basePath}/admin/api/recGoodsForAdmin/add',
                            onSubmit: function () {
                                return $('#add-edit-form').form('validate');
                            },
                            success: function (info) {
                                info = $.parseJSON(info);
                                if (info.success) {
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                    $('#add-edit-win').dialog('close');
                                    $('#dg').datagrid('reload');
                                } else {
                                    $.messager.alert('错误提示', info.msg, 'error');
                                }
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
                    $('#preview_icon').attr('src', "${basePath}/static/images/preview.jpg");
                    $('#progress').html("");
                }
            });
        }

        /*========修改推荐========*/
        function editRecommend() {
            var rows = $('#dg').datagrid('getSelections');
            if (rows.length <= 0) {
                $.messager.alert('提示', '请选中一条数据进行删除', 'info');
                return;
            }
            var recGoods = rows[0];
            $('#add-edit-win').dialog({
                width: 400,
                height: 320,
                title: '修改',
                iconCls: 'icon-add',
                modal: true,
                buttons: [{
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#add-edit-form').form('submit', {
                            url: '${basePath}/admin/api/recGoodsForAdmin/edit',
                            onSubmit: function () {
                                return $('#add-edit-form').form('validate');
                            },
                            success: function (info) {
                                info = $.parseJSON(info);
                                if (info.success) {
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                    $('#add-edit-win').dialog('close');
                                    $('#dg').datagrid('reload');
                                } else {
                                    $.messager.alert('错误提示', info.msg, 'error');
                                }
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
                    $('#add-edit-form').form('load', recGoods);
                    $('#select-goods').textbox('setValue', recGoods.goods.goodsId);
                    $('#select-goods').textbox('setText', recGoods.goods.goodsName);
                    $('#preview_icon').attr('src', recGoods.coverImg.url);
                    $('#img-id').val(recGoods.coverImg.imgInfoId);
                    $('#img-name').val(recGoods.coverImg.imgInfoId);
                }
            });
        }

        /*========删除推荐========*/
        function delRecommend() {
            var rows = $('#dg').datagrid('getSelections');
            if (rows.length <= 0) {
                $.messager.alert('提示', '请选中一条数据进行删除', 'info');
                return;
            }
            $.messager.confirm('提示', '你确认要删除选中的数据吗?', function (r) {
                if (r) {
                    $.ajax({
                        url: '${basePath}/admin/api/recGoodsForAdmin/' + rows[0].recId + '/delete',
                        type: 'get',
                        dataType: 'json',
                        success: function (info) {
                            if (info.success) {
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg
                                });
                                $('#dg').datagrid('reload');
                            } else {
                                $.messager.alert('错误提示', info.msg, 'error');
                            }
                        }, error: function (request, status, error) {
                            $.messager.alert('错误提示', request.responseText, 'error');
                        }
                    })
                }
            });
        }
    </script>
    <style type="text/css">
        * {
            font-size: 12px;
        }
    </style>
</head>
<body>

<div id="dg"></div>

<div id="add-edit-win">
    <form id="add-edit-form" method="post">
        <table align="center" style="margin-top: 15px">
            <input name="recId" type="hidden">
            <input id="img-name" type="hidden">
            <input id="img-id" name="coverImg.imgInfoId" type="hidden">
            <tr>
                <td>
                    上传图片:<br>(建议比例1:1)
                </td>
                <td>
                    <img src="${basePath}/static/images/preview.jpg"
                         style="width: 70px;height:70px;float: left;border: dashed thin #4c4c4c" id="preview_icon">
                    <button style="margin: 20px auto auto 5px;float: left" id="select">选择图片</button>
                    <span id="progress"></span>
                </td>
            </tr>
            <tr>
                <td>
                    选择商品：
                </td>
                <td>
                    <input id="select-goods" required name="goods.goodsId" data-options="editable:false,required:true"
                           type="text">
                </td>
            </tr>
            <tr>
                <td>推荐名：</td>
                <td>
                    <input class="easyui-textbox" data-options="required:true" name="recName" id="rec-name" type="text">
                </td>
            </tr>
            <tr>
                <td>顺序：</td>
                <td>
                    <input class="easyui-numberspinner" data-options="min:1,max:5,editable:true,required:true"
                           name="recOrder" type="number">
                </td>
            </tr>
            <tr>
                <td>描述：</td>
                <td>
                    <input class="easyui-textbox" data-options="multiline:true" style="height: 50px" name="desc"
                           type="text">
                </td>
            </tr>
        </table>
    </form>


</div>

<div id="goods-win">
    <div id="gods-select-toolbar">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:false" id="select-goods-btn-confirm">选择</a>
        <input id="ss">
    </div>
    <div id="mm" style="width:120px">
        <div data-options="name:'goodsName',iconCls:'icon-bricks'">商品名</div>
        <div data-options="name:'merchant.merchName',iconCls:'icon-home'">商家名</div>
    </div>
    <div id="goods-dg"></div>
</div>


</body>
</html>
