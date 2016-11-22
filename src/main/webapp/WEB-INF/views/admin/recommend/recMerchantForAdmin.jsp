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

            /*========商家搜索========*/
            $('#ss').searchbox({
                searcher: function (value, name) {
                    $('#merchant-dg').datagrid('reload', {
                        searchName: name,
                        searchValue: value,
                    });
                },
                menu: '#mm',
                width: 300,
                prompt: '请输入关键字进行搜索'
            });

            /*========选择商家========*/
            $('#select-merchant').textbox({
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
                        }
                    });
                }
            });

            $('#select-merchant-btn-confirm').click(function () {
                var rows = $('#merchant-dg').datagrid('getSelections');
                if (rows.length <= 0) {
                    $.messager.alert('提示', '请先选中一个商家', 'info');
                    return;
                }
                var merchant = rows[0];
                $('#select-merchant').textbox('setValue', merchant.merchantId);
                $('#select-merchant').textbox('setText', merchant.merchName);
                $('#merchant-win').dialog('close');
            });

            /*========商家推荐数据网格========*/
            $('#dg').datagrid({
                url: '${basePath}/admin/api/recMerchantForAdmin/list',
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
                            return "<div style='text-align: center;margin: 1px auto'><img style='width: 40px;height: 40px' src='" + data.url + "'><div>";
                        }
                    },
                    {
                        field: 'merchant', title: '商家', width: 100, align: 'center',
                        formatter: function (data) {
                            return "<div style='text-align: center;margin: 1px auto'><img style='width: 40px;height: 40px' src='" + data.coverImg.url + "'><br>" + data.merchName + "<div>";
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
                height: 300,
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
                            url: '${basePath}/admin/api/recMerchantForAdmin/add',
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
            var recMerchant = rows[0];
            $('#add-edit-win').dialog({
                width: 400,
                height: 300,
                title: '修改',
                iconCls: 'icon-add',
                modal: true,
                buttons: [{
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#add-edit-form').form('submit', {
                            url: '${basePath}/admin/api/recMerchant/edit',
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
                    $('#add-edit-form').form('load', recMerchant);
                    $('#select-merchant').textbox('setValue', recMerchant.merchant.merchantId);
                    $('#select-merchant').textbox('setText', recMerchant.merchant.merchName);
                    $('#preview_icon').attr('src', recMerchant.coverImg.url);
                    $('#img-id').val(recMerchant.coverImg.imgInfoId);
                    $('#img-name').val(recMerchant.coverImg.imgInfoId);
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
                        url: '${basePath}/admin/api/recMerchantForAdmin/' + rows[0].recId + '/delete',
                        type: 'get',
                        dataType: 'json',
                        success: function (info) {
                            if (info.success) {
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg
                                });
                                $('#dg').datagrid('reload');
                                return;
                            }
                            $.messager.alert('错误提示', info.msg, 'error');
                        },
                        error: function (request, status, error) {
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
                    选择商家：
                </td>
                <td>
                    <input id="select-merchant" required name="merchant.merchantId"
                           data-options="editable:false,required:true"
                           type="text">
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

<div id="merchant-win">
    <div id="merchant-select-toolbar">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:false"
           id="select-merchant-btn-confirm">选择</a>
        <input id="ss">
    </div>
    <div id="mm" style="width:120px">
        <div data-options="name:'merchName',iconCls:'icon-home'">商家名</div>
    </div>
    <div id="merchant-dg"></div>
</div>


</body>
</html>
