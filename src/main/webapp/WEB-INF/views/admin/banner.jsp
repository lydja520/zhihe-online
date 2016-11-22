<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>轮播图管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>

    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>

    <script>
        $(function () {
            /*=========初始化数据网格========*/
            $("#banner_gird").datagrid({
                url: '/admin/api/banner/list',
                singleSelect: true,
                pageSize: 20,
                pagination: true,
                fit: true,
                fitColumns: true,
                border: false,
                rownumbers: true,
                columns: [[
                    {field: 'bannerId', checkbox: true, align: 'center'},
                    {field: 'displayLocation', title: '轮播图所在的位置', width: 100, align: 'center', sortable: true},
                    {field: 'displayTarget', title: '跳转类型', width: 100, align: 'center'},
                    {field: 'viewTargetTitle', title: '标题', width: 100, align: 'center'},
                    {field: 'bannerOrder', title: '轮播图顺序', sortable: true, width: 100, align: 'center'},
                    {field: 'createDate', title: '创建时间', width: 100, align: 'center'},
                    {
                        field: 'imgInfo', title: '操作', width: 100, align: 'center',
                        formatter: function (value) {
                            return "<button class='preview-btn' data-img='" + value.url + "'>预览</button>";
                        }
                    }
                ]],
                onLoadSuccess: function (data) {
                    $('.preview-btn').each(function (index, btn) {
                        $(btn).click(function () {
                            preview($(btn).data('img'));
                        });
                    });
                },
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        add();
                    }
                }, '-', {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                        edit();
                    }
                }, '-', {
                    text: '删除',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        deletes();
                    }
                }],
            });

            /*=========上传轮播图图片========*/
            initUploader({
                pickbutton: 'select',
                /*uploadbutton: 'upload',*/
                onFileAdd: function (file) {
                    $('#pic_name').textbox('setText', file.name);
                },
                onError: function (file, err, errTip) {

                },
                onBeforeUpload: function (up, file) {
                    total = up.total.size;
                    loaded = up.total.loaded;
                    progress = parseInt(loaded / total * 100);
                    $('#progress')[0].innerHTML = ' 正在上传...';
                },
                onUploaded: function (up, file, info) {
                    console.log(info);
                    $('#pic_id').val(info.imgInfoId);
                    $('#preview_icon').attr('src', info.url);
                    $('#progress')[0].innerHTML = ' 上传完成';
                },
                onComplete: function () {

                }
            });

            /*========增加轮播图========*/
            function add() {
                $("#add-form").form('reset');
                $("#add-dialog").dialog({
                    title: '添加',
                    width: 450,
                    iconCls: 'icon-add',
                    modal: true,
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-save',
                        handler: function () {
                            if ($('#view_type').combobox('getValue') == 5) {
                                var value = $('#view_target').textbox('getText');
                                $('#view_target_field').val(value);
                            }
                            $("#add_form").form('submit', {
                                url: '${basePath}/admin/api/banner/add',
                                success: function (data) {
                                    data = $.parseJSON(data);
                                    if (data.success) {
                                        $.messager.show({
                                            title: '提示',
                                            msg: '操作成功'
                                        });
                                        $('#add-dialog').dialog('close');
                                        $('#banner_gird').datagrid('load');
                                    } else {
                                        $.messager.alert('提示', data.msg + ",稍后重试!");
                                    }
                                }
                            });
                        }

                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#add-dialog").dialog('close');
                        }
                    }],
                    onBeforeOpen: function () {
                        resetFormData();
                    }
                });
            }

            /*========删除轮播图========*/
            function deletes() {
                var rows = $("#banner_gird").datagrid('getChecked');
                console.log(rows[0]);
                if (rows.length < 1) {
                    $.messager.alert("提示", '请先选择要删除的行?');
                } else {
                    $.messager.confirm('删除数据', '确定要删除改行?',
                            function (flag) {
                                if (flag) {
                                    $.ajax({
                                        url: '${basePath}/admin/api/banner/delete',
                                        type: 'post',
                                        dataType: 'text',
                                        data: {
                                            bannerId: rows[0].bannerId,
                                            imgInfoId: rows[0].imgInfo.imgInfoId
                                        },
                                        beforeSend: function () {
                                            $("#add-viewP").numberbox('fix');
                                        },
                                        success: function (info) {
                                            info = $.parseJSON(info);
                                            console.log(info);
                                            if (info.success) {
                                                $.messager.show({
                                                    title: '提示',
                                                    msg: info.msg
                                                });
                                                $('#banner_gird').datagrid('load');
                                            } else {
                                                $.messager.alert('提示', '删除失败');
                                            }
                                        },
                                        error: function () {
                                            $.messager.alert('提示', '删除失败', 'error');
                                        }
                                    });
                                }
                            }
                    );
                }
            }

            /*========修改轮播图========*/
            function edit() {
                var datas = $("#banner_gird").datagrid('getChecked');
                if (datas.length < 1) {
                    $.messager.alert("提示", '请先选择要修改的行');
                    return;
                }
                $("#add-dialog").dialog({
                    title: '编辑',
                    width: 450,
                    iconCls: 'icon-edit',
                    modal: true,
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-save',
                        handler: function () {
                            if ($('#view_type').combobox('getValue') == 5) {
                                var value = $('#view_target').textbox('getText');
                                $('#view_target_field').val(value);
                            }
                            $("#add-dialog").dialog('close');
                            $("#add_form").form('submit', {
                                url: '${basePath}/admin/api/banner/update',
                                onSubmit: function () {
                                    $("#add-viewP").numberbox('fix');
                                    var isValid = $(this).form('validate');
                                    if (!isValid) {
                                        return isValid;
                                    }
                                },
                                success: function (data) {
                                    data = $.parseJSON(data);
                                    if (data.success) {
                                        $("#banner_grid").datagrid('reload');
                                        $.messager.show({
                                            title: '提示',
                                            msg: '修改成功'
                                        });
                                        $('#add-dialog').dialog('close');
                                        $('#banner_gird').datagrid('load');
                                    } else {
                                        $.messager.alert('提示', data.msg + ",稍后重试!");
                                    }
                                }
                            });
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#add-dialog").dialog('close');
                        }
                    }],
                    onBeforeOpen: function () {
                        var info = {
                            bannerId: datas[0].bannerId,
                            bannerOrder: datas[0].bannerOrder,
                            'imgInfo.imgInfoId': datas[0].imgInfo.imgInfoId,
                            imgName: datas[0].imgInfo.key,
                            imgURL: datas[0].imgInfo.url,
                            bannerType: datas[0].bannerType,
                            viewType: datas[0].viewType,
                            viewTargert: datas[0].viewTargert,
                            viewTargetTitle: datas[0].viewTargetTitle,
                            createDate: datas[0].createDate,
                        };
                        resetFormData(info);
                    }
                });
            }
        });

        function resetFormData(data) {
            $('#preview_icon').attr('src', '${basePath}/static/images/preview.jpg');
            $('#progress')[0].innerHTML = '';
            $('#pic_name').textbox('setText', '');
            $('#view_type').combobox('select', -1);

            $('#view_target_field').val('');

            $('#view_target_title').attr('readonly', true);
            $('#view_target_title').validatebox({
                editable: false,
                required: false
            });
            $('#view_target').textbox({
                editable: false,
                buttonText: '请选择',
                required: false
            });

            $('#bannerType').combobox('select', 1);

            if (!data) {
                return;
            }
            $("#add_form").form('load', data);
            $('#view_target_field').val(data.viewTargert);
            $('#pic_name').textbox('setText', data.imgName);
            $('#preview_icon').attr('src', data.imgURL);
            $('#view_target').textbox('setValue', data.viewTargetTitle);

            if (data.viewType == -1) {
                $('#view_target_title').validatebox({
                    required: false
                });
                $('#view_target').textbox({
                    required: false
                });
            } else if (data.viewType == 5) {
                $('#view_target_title').validatebox({
                    required: true
                });
                $('#view_target').textbox({
                    required: true
                }).textbox('setText', data.viewTargert);
            }
        }

        /*========预览轮播图========*/
        function preview(url) {
            $('#preview').dialog({
                title: '预览',
                iconCls: 'icon-preview',
                modal: 'true',
                width: 500,
                height: 250,
            });
            $('#previes_img').attr('src', url);
        }

        function openPickDialog() {
            $("#upload-dialog").dialog('open');
        }
    </script>
</head>
<body>

<!--数据网格开始-->
<table id="banner_gird"></table>
<!--数据网格开始-->

<!--增加轮播图开始-->
<div id="add-dialog" align="center">
    <form action="${basePath}/admin/api/navigation/add" method="POST" id="add_form">
        <input type="hidden" id="banner_id" name="bannerId">
        <input type="hidden" name="imgInfo.imgInfoId" id="pic_id">
        <table style="margin: 20px auto 20px auto;border-collapse: separate;border-spacing: 7px">
            <tr>
                <td valign="top">上传轮播图图片:(建议比例：1080*486)</td>
                <td>
                    <div style="margin-bottom: 5px">
                        <input type="text" class="easyui-textbox" id="pic_name" data-options="editable:false">
                        <button type="button" class="easyui-linkbutton" iconCls="icon-preview" id="select">选择图片</button>
                    </div>
                    <div>
                        <div style="border: dotted;border-width: 1px;float: left">
                            <img src="${basePath}/static/images/preview.jpg" id="preview_icon"
                                 style="width: 170px;height:80px">
                        </div>
                        <div id="progress" style="float: left;margin-top: 40px"></div>
                    </div>
                </td>
            </tr>
            <tr>
                <td valign="top"><label>轮播图所在位置:</label></td>
                <td>
                    <select id="bannerType" class="easyui-combobox" name="bannerType" style="width:180px;"
                            data-options="panelHeight: 100,required: true,editable:false">
                        <option value="1" selected>首页</option>
                        <option value="2">购物中心</option>
                        <option value="3">特色街区</option>
                        <option value="4">优"+"店</option>
                    </select>
                </td>
            </tr>
            <tr id="annotation_vie_type">
                <td>
                    <label>跳转类型:</label>
                </td>
                <td>
                    <select id="view_type" name="viewType" style="width:180px;">
                        <option value="-1" selected>不进行跳转</option>
                        <option value="2">商家页面</option>
                        <option value="3">商品页面</option>
                        <option value="4">活动页面</option>
                        <option value="5">指定页面</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>目标标题:</td>
                <td>
                    <input name="viewTargetTitle" style="width: 180px;" class="easyui-validatebox"
                           data-options="required:true,editable:false" id="view_target_title">
                </td>
            </tr>
            <tr id="annotation_view_target">
                <td>跳转目标:</td>
                <td>
                    <input type="hidden" name="viewTargert" id="view_target_field">
                    <input type="text" id="view_target" style="width: 180px;" class="easyui-textbox"
                           data-options="buttonText:'前选择',editable:false">
                </td>
            </tr>
            <tr>
                <td>轮播图顺序:</td>
                <td><input type="text" class="easyui-numberspinner" data-options="prompt:'导航顺序'" name="bannerOrder"
                           style="width: 180px" value="1"></td>
            </tr>
        </table>
    </form>
    <script type="text/javascript">
        /*=======viewType与ViewTarget=======*/
        $('#view_type').combobox({
            rerequired: true,
            editable: false,
            panelHeight: 120,
            onChange: function (newVal, oldVal) {
                $('#view_target_title').val('');
                $('#view_target').textbox('clear');
                $('#view_target_title').attr('readonly', true);
                $('#view_target_title').validatebox({
                    editable: false,
                    required: true
                });
                $('#view_target').textbox({
                    editable: false,
                    buttonText: '请选择',
                    required: true
                });
                $('#view_target_field').val('');

                if (newVal == -1) {
                    $('#view_target_title').validatebox({
                        required: false
                    });
                    $('#view_target').textbox({
                        required: false
                    });
                }
                if (newVal == 5) {
                    $('#view_target_title').validatebox({
                        editable: true
                    });
                    $('#view_target_title').attr('readonly', false);
                    $('#view_target').textbox({
                        editable: true,
                        buttonText: ''
                    });
                }
            }
        });

        $('#view_target').textbox({
            onClickButton: function () {
                var viewType = $('#view_type').combobox('getValue');
                if (viewType == 2) {
                    $('#merchant_gird_dialog').dialog({
                        title: '商家选择',
                        iconCls: 'icon-ok',
                        width: 700,
                        height: 400,
                        madol: true
                    });
                } else if (viewType == 3) {
                    $('#goods_grid_dialog').dialog({
                        title: '商品选择',
                        iconCls: 'icon-ok',
                        width: 700,
                        height: 400,
                        madol: true
                    });
                } else if (viewType == 4) {
                    $('#activity_gird_dialog').dialog({
                        title: '活动选择',
                        iconCls: 'icon-ok',
                        width: 700,
                        height: 400,
                        madol: true
                    });
                }
            }
        });
    </script>
</div>
<!--增加轮播图结束-->

<!--预览轮播图开始-->
<div id="preview">
    <img id="previes_img" style="width: 100%;height: 100%">
</div>
<!--预览轮播图结束-->

<!--商品选择对话框-->
<div id="goods_grid_dialog">
    <table id="goods-grid"></table>
    <div id="goods-grid-toolbar">
        <button class="easyui-linkbutton" iconCls="icon-ok" id="check-goods-btn">选择</button>
        <input class="easyui-searchbox" style="width: 240px;" menu="#goods-searchbox-menu"
               searcher="searchGoods" id="goods-searchbox"/>
        <div id="goods-searchbox-menu">
            <div name="goodsName">商品名称</div>
            <div name="merchant.merchName">商家名称</div>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            /**
             * 初始化商家数据列表
             */
            $('#goods-grid').datagrid({
                url: '${basePath}/admin/api/onsale/goodses',
                singleSelect: true,
                pageSize: 10,
                pagination: true,
                fit: true,
                fitColumns: true,
                border: false,
                rownumbers: true,
                toolbar: '#goods-grid-toolbar',
                columns: [[
                    {field: 'goodsId', checkbox: true, align: 'center'},
                    {field: 'goodsName', title: '商品名称名称', width: 50, align: 'center'},
                    {
                        field: 'merchant',
                        title: '商家',
                        width: 40,
                        align: 'center',
                        formatter:function(value){
                            return value.merchName;
                        }
                    },
                    {field: 'price', title: '价格(元)', width: 30, align: 'center'},
                    {field: 'volume', title: '销量', width: 30, align: 'center'},
                    {field: 'currentStock', title: '库存', width: 30, align: 'center'},
                    {field: 'goodsDesc', title: '商品描述', width: 100, align: 'center'}
                ]]
            });

            /**
             * 选择商家按钮
             */
            $('#check-goods-btn').click(function () {
                var rows = $('#goods-grid').datagrid('getChecked');
                if (rows.length < 1) {
                    $.messager.alert('提示', '请选择数据！', 'info');
                    return;
                }
                var goods = rows[0];
                $('#view_target_title').val(goods.goodsName);
                $('#view_target').textbox('setText', goods.goodsName);
                $('#view_target_field').val(goods.goodsId);
                $('#goods_grid_dialog').dialog('close');
            });
        });
        function searchGoods(searchValue, searchName) {
            var param = {};
            if (searchName && $.trim(searchValue)) {
                param.searchName = searchName;
                param.searchValue = $.trim(searchValue);
            }
            $('#goods-grid').datagrid('load', param);
        }
    </script>
</div>
<!--商品选择对话框结束-->

<!--商家选择对话框-->
<div id="merchant_gird_dialog">
    <table id="merchant-gird"></table>
    <div id="merchant-grid-toolbar">
        <button class="easyui-linkbutton" iconCls="icon-ok" id="check-merchant-btn">选择</button>
        <input class="easyui-searchbox" style="width: 240px;" menu="#merchant-searchbox-menu"
               searcher="searchMerchant" id="merchant-searchbox"/>
        <div id="merchant-searchbox-menu">
            <div name="merchName">商家名称</div>
            <div name="contactName">联系人</div>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            /**
             * 初始化商家数据列表
             */
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

            /**
             * 选择商家按钮
             */
            $('#check-merchant-btn').click(function () {
                var rows = $('#merchant-gird').datagrid('getChecked');
                if (rows.length < 1) {
                    $.messager.alert('提示', '请选择数据！', 'info');
                    return;
                }
                var merch = rows[0];
                $('#view_target_title').val(merch.merchName);
                $('#view_target_field').val(merch.merchantId);
                $('#view_target').textbox('setText', merch.merchName);
                $('#merchant_gird_dialog').dialog('close');
            });
        });
        function searchMerchant(searchValue, searchName) {
            var param = {};
            if (searchName && $.trim(searchValue)) {
                param.searchName = searchName;
                param.searchValue = $.trim(searchValue);
            }
            $('#merchant-gird').datagrid('load', param);
        }
    </script>
</div>
<!--商家选择对话框结束-->

<!--活动选择对话框-->
<div id="activity_gird_dialog">
    <table id="activity-gird"></table>
    <div id="activity-grid-toolbar">
        <button class="easyui-linkbutton" iconCls="icon-ok" id="check-activity-btn">选择</button>
        <input class="easyui-searchbox" style="width: 240px;" menu="#activity-searchbox-menu"
               searcher="searchActivity" id="activity-searchbox"/>
        <div id="activity-searchbox-menu">
            <div name="activitName">活动名称</div>
            <div name="activityPromoter.merchName">发起商家</div>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            /**
             * 初始化活动数据列表
             */
            $('#activity-gird').datagrid({
                url: '${basePath}/admin/activitya/banner/able',
                singleSelect: true,
                pageSize: 10,
                pagination: true,
                fit: true,
                fitColumns: true,
                border: false,
                rownumbers: true,
                toolbar: '#activity-grid-toolbar',
                columns: [[
                    {field: 'activitId', checkbox: true, align: 'center'},
                    {field: 'activitName', title: '活动名称', width: 50, align: 'center'},
                    {
                        field: 'activityPromoter',
                        title: '活动发起商',
                        width: 40,
                        align: 'center',
                        formatter:function(value){
                            return value.merchName;
                        }
                    },
                    {field: 'contacterName', title: '联系人', width: 30, align: 'center'},
                    {field: 'contactTell', title: '联系电话', width: 30, align: 'center'},
                    {field: 'displayState', title: '当前状态', width: 30, align: 'center'},
                    {field: 'activitDetail', title: '活动描述', width: 100, align: 'center'}
                ]]
            });

            /**
             * 选择商家按钮
             */
            $('#check-activity-btn').click(function () {
                var rows = $('#activity-gird').datagrid('getChecked');
                if (rows.length < 1) {
                    $.messager.alert('提示', '请选择数据！', 'info');
                    return;
                }
                var activity = rows[0];
                $('#view_target_title').val(activity.activitName);
                $('#view_target_field').val(activity.activitId);
                $('#view_target').textbox('setText', activity.activitName);
                $('#activity_gird_dialog').dialog('close');
            });
        });
        function searchActivity(searchValue, searchName) {
            var param = {};
            if (searchName && $.trim(searchValue)) {
                param.searchName = searchName;
                param.searchValue = $.trim(searchValue);
            }
            $('#activity-gird').datagrid('load', param);
        }
    </script>
</div>
<!--活动选择对话框结束-->

</body>
</html>
