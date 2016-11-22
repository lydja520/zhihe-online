<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>新闻发布会管理页面</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <script type="text/javascript">
        (function (win, $) {
            $(function () {
                /**
                 * 图片上传组件初始化
                 */
                $('.img-picker').each(function (index, item) {
                    console.log(index, item);
                    var valueTarget = $(item).data('value-target');
                    var previewTarget = $(item).data('preview-target');
                    var progressTarget = $(item).data('progress-target');
                    var itemId = $(item).attr('id');
                    initUploader({
                        pickbutton: itemId,
                        onFileAdd: function (file) {
                            $('#' + valueTarget).val(file.name);
                        },
                        onError: function (file, err, errTip) {

                        },
                        onBeforeUpload: function (up, file) {
                            $('#' + progressTarget).html('图片已上传：0%');
                        },
                        onProgress: function (progress, loaded, total) {
                            $('#' + progressTarget).html('图片已上传：' + progress + '%');
                        },
                        onUploaded: function (up, file, info) {
                            console.log(info);
                            $('#' + previewTarget).attr('src', info.url);
                            $('#' + valueTarget).val(info.imgInfoId);
                            $('#' + progressTarget).html('图片已上传完成，正在处理...');
                            $.messager.show({
                                title: '提示',
                                msg: '图片上传完成'
                            })
                        }
                        ,
                        onError: function (up, err, errTip) {
                            console.log(err);
                            $.messager.alert('提示', '图片上传失败，请重试！', 'error');
                        }
                        ,
                        onComplete: function () {
                            $('#' + progressTarget).html('');
                        }
                    })
                    ;
                });
            });
        })(window, jQuery);
    </script>
</head>
<body>

<!--数据网格开始-->
<table id="adv-grid"></table>
<div id="adv-grid-toolbar">
    <button id="add-adv-btn" class="easyui-linkbutton" iconCls="icon-add">添加</button>
    <button id="edit-adv-btn" class="easyui-linkbutton" iconCls="icon-edit">修改</button>
    <button id="remove-adv-btn" class="easyui-linkbutton" iconCls="icon-remove">删除</button>
    <span style="margin-left: 40px; display: inline-block">
        <input id="adv-searchbox" style="width: 300px;" class="easyui-searchbox" menu="#adv-searchbox-menu">
        <div id="adv-searchbox-menu">
            <div name="merchant.merchName">商家名称</div>
        </div>
    </span>
</div>
<script type="text/javascript">
    $(function () {
        /**
         * 数据网格初始化
         */
        $('#adv-grid').datagrid({
            url: '/admin/api/merchantAdv/list',
            singleSelect: true,
            pageSize: 30,
            pagination: true,
            fit: true,
            fitColumns: true,
            border: false,
            rownumbers: true,
            columns: [[
                {field: 'advId', checkbox: true, align: 'center'},
                {
                    field: 'merchant', title: '商家', width: 40, align: 'center', formatter: function (value) {
                    if (value) {
                        return value.merchName;
                    }
                }
                },
                {field: 'advOrder', title: '排序位置', width: 30, align: 'center', sortable: true},
                {field: 'createDateTime', title: '添加时间', width: 40, align: 'center', sortable: true},
                {
                    field: 'advImg', title: '预览图', width: 100, align: 'center',
                    formatter: function (value) {
                        if (value) {
                            return '<div style="width: 100px;height: 50px;"><img style="width: 100px;height: 50px;" src="' + value.url + '"></div>';
                        }
                    }
                }
            ]],
            toolbar: '#adv-grid-toolbar'
        });
        /**
         * 搜索框初始化
         */
        $('#adv-searchbox').searchbox({
            searcher: function (value, name) {
                var param = {};
                if ($.trim(value) && name) {
                    param.searchName = name;
                    param.searchValue = value;
                }
                $('#adv-grid').datagrid('load', param);
            }
        });
        /**
         * 添加数据按钮初始化
         */
        $('#add-adv-btn').click(function () {
            $('#add-dialog').dialog('open');
        });
        /**
         * 编辑按钮初始化
         */
        $('#edit-adv-btn').click(function () {
            var rows = $('#adv-grid').datagrid('getChecked');
            if (rows.length != 1) {
                $.messager.alert('提示', '请选择一条你需要操作的数据！', 'info');
                return;
            }
            var adv = rows[0];
            $('#edit-dialog').dialog('open');
            $('#edit-form').form('reset');
            $('#edit-form').form('load', adv);
            if (adv.merchant) {
                $('#edit-merchant-picker').textbox('setText', adv.merchant.merchName);
                $('#edit-merchant-id').val(adv.merchant.merchantId);
            }
            if (adv.advImg) {
                $('#edit-img-id').val(adv.advImg.imgInfoId);
                $('#edit-img-preview').attr('src', adv.advImg.url);
            }
        });
        /**
         * 删除按钮初始化
         */
        $('#remove-adv-btn').click(function () {
            var rows = $('#adv-grid').datagrid('getChecked');
            if (rows.length != 1) {
                $.messager.alert('提示', '请选择一条你需要操作的数据！', 'info');
                return;
            }
            var adv = rows[0];
            $.messager.confirm('提示', '您确定要删除所选择的数据吗？', function (r) {
                if (r) {
                    $.messager.progress();
                    $.ajax({
                        url: '${basePath}/admin/api/merchantAdv/delete',
                        type: 'post',
                        dataType: 'text',
                        data: {advId: adv.advId},
                        success: function (data) {
                            data = $.parseJSON(data);
                            if (data.success) {
                                $.messager.show({
                                    title: '提示',
                                    msg: data.msg
                                });
                                $('#adv-grid').datagrid('load');
                            } else {
                                $.messager.alert('提示', data.msg, 'error');
                            }
                        },
                        error: function () {
                            $.messager.alert('提示', '操作失败，请重试！', 'error');
                        },
                        complete: function () {
                            $.messager.progress('close');
                        }
                    });
                }
            });
        });
    });
</script>
<!--数据网格结束-->

<!--添加数据弹出框开始-->
<div id="add-dialog" class="easyui-dialog" style="width: 460px;height:300px;" title="添加数据" iconCls="icon-add"
     modal="true" closed="true">
    <form id="add-form" method="post" action="${basePath}/admin/api/merchantAdv/add">
        <table class="form-table">
            <tr>
                <td>商家：</td>
                <td>
                    <input type="hidden" id="add-merchant-id" name="merchant.merchantId">
                    <input id="add-merchant-picker" class="easyui-textbox" style="width: 200px;" required="true"
                           editable="false" buttonText="选择商家">
                </td>
            </tr>
            <tr>
                <td>排序：</td>
                <td>
                    <input name="advOrder" class="easyui-numberspinner" style="width:200px;" value="0"
                           required="required" data-options="min:0">
                </td>
            </tr>
            <tr>
                <td>图片选择：(建议比例：1080*486)</td>
                <td>
                    <input type="text" readonly id="add-img-id" name="advImg.imgInfoId" style="width: 160px;">
                    <button id="add-img-picker" class="easyui-linkbutton img-picker" iconCls="icon-preview"
                            data-value-target="add-img-id" data-progress-target="add-img-progress"
                            data-preview-target="add-img-preview">选择图片
                    </button>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <img id="add-img-preview" style="width: 160px;height: 100px;"
                         src="${basePath}/static/images/preview.jpg">
                    <span id="add-img-progress"></span>
                </td>
            </tr>
        </table>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        $('#add-dialog').dialog({
            onBeforeOpen: function () {
                $('#add-img-preview').attr('src', '${basePath}/static/images/preview.jpg');
                $('#add-img-id').val('');
                $('#add-form').form('reset');
            },
            buttons: [{
                text: '提交',
                iconCls: 'icon-ok',
                handler: function () {
                    var merchantId = $('#add-merchant-id').val();
                    if (!merchantId) {
                        $.messager.alert('提示', '请选择商家', 'info');
                        return;
                    }
                    var imgId = $('#add-img-id').val();
                    if (!imgId) {
                        $.messager.alert('提示', '请先上传图片', 'info');
                        return;
                    }
                    $('#add-form').form('submit', {
                        success: function (data) {
                            data = $.parseJSON(data);
                            if (data.success) {
                                $.messager.show({
                                    title: '提示',
                                    msg: data.msg
                                });
                                $('#adv-grid').datagrid('load');
                                $('#add-dialog').dialog('close');
                            } else {
                                $.messager.alert('提示', data.msg, 'error');
                            }
                        }
                    });
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add-dialog').dialog('close');
                }
            }]
        });
        $('#add-merchant-picker').textbox({
            onClickButton: function () {
                $('#merchant-grid-dialog').dialog('open');
                $('#check-merchant-btn').data('text-target', $('#add-merchant-picker').attr('id'));
                $('#check-merchant-btn').data('value-target', 'add-merchant-id');
            }
        });
    });
</script>
<!--添加数据弹出框结束-->

<!--编辑数据弹出框开始-->
<div id="edit-dialog" class="easyui-dialog" style="width: 460px;height:300px;" title="数据修改" iconCls="icon-edit"
     modal="true" closed="true">
    <form id="edit-form" method="post" action="${basePath}/admin/api/merchantAdv/edit">
        <input type="hidden" name="advId">
        <table class="form-table">
            <tr>
                <td>商家：</td>
                <td>
                    <input type="hidden" id="edit-merchant-id" name="merchant.merchantId">
                    <input id="edit-merchant-picker" class="easyui-textbox" style="width: 200px;" required="true"
                           editable="false" buttonText="选择商家">
                </td>
            </tr>
            <tr>
                <td>排序：</td>
                <td>
                    <input name="advOrder" class="easyui-numberspinner" style="width:200px;" value="0"
                           required="required" data-options="min:0">
                </td>
            </tr>
            <tr>
                <td>图片选择：</td>
                <td>
                    <input type="text" readonly id="edit-img-id" name="advImg.imgInfoId" style="width: 160px;">
                    <button id="edit-img-picker" class="easyui-linkbutton img-picker" iconCls="icon-preview"
                            data-value-target="edit-img-id" data-progress-target="edit-img-progress"
                            data-preview-target="edit-img-preview">选择图片
                    </button>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <img id="edit-img-preview" style="width: 160px;height: 100px;"
                         src="${basePath}/static/images/preview.jpg">
                    <span id="edit-img-progress"></span>
                </td>
            </tr>
        </table>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        $('#edit-dialog').dialog({
            buttons: [{
                text: '提交',
                iconCls: 'icon-ok',
                handler: function () {
                    var merchantId = $('#edit-merchant-id').val();
                    if (!merchantId) {
                        $.messager.alert('提示', '请先选择商家', 'info');
                        return;
                    }
                    var imgId = $('#edit-img-id').val();
                    if (!imgId) {
                        $.messager.alert('提示', '请先上传图片', 'info');
                        return;
                    }
                    $('#edit-form').form('submit', {
                        success: function (data) {
                            data = $.parseJSON(data);
                            if (data.success) {
                                $.messager.show({
                                    title: '提示',
                                    msg: '操作成功！'
                                })
                                $('#adv-grid').datagrid('load');
                                $('#edit-dialog').dialog('close');
                            } else {
                                $.messager.alert('提示', data.msg, 'erro');
                            }
                        }
                    });
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-dialog').dialog('close');
                }
            }]
        });
        $('#edit-merchant-picker').textbox({
            onClickButton: function () {
                $('#merchant-grid-dialog').dialog('open');
                $('#check-merchant-btn').data('text-target', $('#edit-merchant-picker').attr('id'));
                $('#check-merchant-btn').data('value-target', 'edit-merchant-id');
            }
        });
    });
</script>
<!--编辑数据弹出框结束-->

<!--商家选择框开始-->
<div id="merchant-grid-dialog" class="easyui-dialog" style="width: 700px;height: 400px;" modal="true"
     title="商家列表" closed="true">
    <table id="merchant-gird"></table>
    <div id="merchant-grid-toolbar">
        <button class="easyui-linkbutton" iconCls="icon-ok" id="check-merchant-btn">选择</button>
        <span style="margin-left: 20px;">
            <input class="easyui-searchbox" style="width: 240px;" menu="#merchant-searchbox-menu"
                   searcher="searchMerchant" id="merchant-searchbox"/>
        </span>
        <div id="merchant-searchbox-menu">
            <div name="merchName">商家名称</div>
            <div name="contactName">联系人</div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $('#merchant-gird').datagrid({
            url: '${basePath}/admin/api/merchant/list',
            singleSelect: true,
            pageSize: 10,
            pagination: true,
            fit: true,
            fitColumns: true,
            border: false,
            rownumbers: true,
            toolbar: '#merchant-grid-toolbar',
            columns: [[
                {field: 'merchantId', checkbox: true, align: 'center'},
                {field: 'merchName', title: '商家名称', width: 40, align: 'center'},
                {field: 'contactName', title: '联系人', width: 40, align: 'center'},
                {field: 'address', title: '地址', width: 50, align: 'center'},
                {field: 'merchantDetails', title: '商家简介', width: 100, align: 'center'}
            ]]
        });
        $('#merchant-grid-dialog').dialog({
            onBeforeOpen: function () {
                $('#merchant-searchbox').searchbox('clear');
                searchMerchant();
            }
        });
        $('#check-merchant-btn').click(function () {
            var textTarget = $(this).data('text-target');
            var valueTarget = $(this).data('value-target');
            var rows = $('#merchant-gird').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择一个商家！', 'info');
                return;
            }
            var merchant = rows[0];
            $('#' + valueTarget).val(merchant.merchantId);
            $('#' + textTarget).textbox('setText', merchant.merchName);
            $('#merchant-grid-dialog').dialog('close');
        });
    });
    function searchMerchant(value, name) {
        var param = {};
        if (name && $.trim(value)) {
            param.searchName = name;
            param.searchValue = $.trim(value);
        }
        $('#merchant-gird').datagrid('load', param);
    }
</script>
<!--商家选择框结束-->

</body>
</html>
