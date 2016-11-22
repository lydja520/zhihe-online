<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript" src="${basePath}/static/core/utils.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <script type="text/javascript">
        var qiniuData = null;
        $(function () {
            /*============初始化商家数据网格==============*/
            $('#merchant_grid').datagrid({
                url: '/admin/api/merchant/list',
                columns: [[
                    {field: '', checkbox: 'true'},
                    {
                        field: 'coverImg', title: '商家头像', width: 100, align: 'center',
                        formatter: function (value) {
                            return "<img src=" + value.url + " style='width:45px;height:45px;border-radius: 22px' >";
                        }
                    },
                    {field: 'merchName', title: '商家名称', width: 100, align: 'center'},
                    {field: 'merchTell', title: '联系电话', width: 100, align: 'center'},
                    {field: 'createDate', title: '入驻时间', width: 100, sortable: true, align: 'center'},
                    {field: 'address', title: '详细地址', width: 200, align: 'center'},
                    {field: 'alipayCode', title: '支付宝账号', width: 100, align: 'center'},
                    {field: 'merchOrder', title: '排列顺序', width: 100, sortable: true, align: 'center'},
                    {
                        field: 'examinState',
                        title: '商家审核情况',
                        width: 100,
                        align: 'center',
                        formatter: function (value) {
                            switch (value) {
                                case 1:
                                    return '<span style="color: orange">未提交审核</span>';
                                case 2:
                                    return '<span style="color: blue">待审核<span>';
                                case 3:
                                    return '<span style="color: green">已通过审核</span>';
                                case 4:
                                    return '<span style="color: red">未通过审核</span>';
                            }
                        }
                    },
                    {
                        field: 'permit',
                        title: '是否启用',
                        width: 100,
                        align: 'center',
                        sortable: true,
                        formatter: function (value) {
                            if (value) {
                                return "<span style='color: green'>启用</span>";
                            }
                            else {
                                return "<span style='color: red'>禁用</span>";
                            }
                        }
                    },
                    {field: 'permitMsg', title: '禁用原因', width: 100, align: 'center'},
                    {
                        field: 'merchantId', title: '查看详情', width: 100, align: 'center',
                        formatter: function (value) {
                            return "<button value=" + value + " onclick='preview(this.value)'>查看详情</button>";
                        }
                    }
                ]],
                toolbar: '#grid_toolbar',
                singleSelect: true,
                pageSize: 20,
                pagination: true,
                fit: true,
                rownumbers: true,
                fitColumns: true,
                border: false,
                onLoadError: function () {
                    $.messager.alert('出错了', '数据加载失败，请重试', 'error');
                },
                onCheck: function (index, row) {
                    rows = row;
                },
                queryParams: {
                    merchName: '',
                    merchTell: '',
                    examinState: '',
                    permit: ''
                }
            });

            /*========启用商家========*/
            $('#able-merch').click(function () {
                updateMerchPermit(true);
            });

            /*========禁用商家========*/
            $('#disable-merch').click(function () {
                updateMerchPermit(false);
            });

            /*========修改商家营业执照=======*/
            $('#edit-merch').click(function () {
                editMerch();
            });

            /*========修改商家基本信息=======*/
            $('#edit-merch-info').click(function () {
                editMerchantInfo();
            });

            /*========修改商家商家图片=======*/
            $('#edit-merch-img').click(function () {
                editMerchantImg();
            });

            /*========上传图片=======*/
            initUploader({
                pickbutton: 'select',
                onFileAdd: function (file) {

                },
                onError: function (file, err, errTip) {

                },
                onBeforeUpload: function (up, file) {
                    $('#preview_icon').attr('src', "/static/images/loading.gif");
                },
                onUploaded: function (up, file, info) {
                    $('#imgName').textbox('setText', file.name);
                    $('#imgId').val(info.imgInfoId);
                    $('#preview_icon').attr('src', info.url);
                },
                onComplete: function () {

                }
            });

            /*============搜索商家==============*/
            $('#search').click(function () {
                $('#merchant_grid').datagrid('load', {
                    merchName: $('#merchName').textbox('getValue'),
                    merchTell: $('#merchTell').textbox('getValue'),
                    examinState: $('#examinState').combobox('getValue'),
                    permit: $('#permit').combobox('getValue')
                });
            });

            /*============清除搜索==============*/
            $('#clear-search').click(function () {
                $('#merchName').textbox('clear');
                $('#merchTell').textbox('clear');
                $('#examinState').combobox('reset');
                $('#permit').combobox('reset');
                $('#merchant_grid').datagrid('load', {
                    merchName: '',
                    merchTell: '',
                    examinState: '',
                    permit: ''
                });
            });

        });

        /*=========启用和禁用商家===========*/
        function updateMerchPermit(permit) {
            var rows = $('#merchant_grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            var data = rows[0];
            if (data.permit == permit) {
                $.messager.alert('提示', '请勿重复操作', 'info');
                return;
            }
            if (!permit) {
                $('#disable-win').dialog({
                    width: 300,
                    height: 200,
                    title: '禁用商家',
                    buttons: [{
                        text: '确定',
                        iconCls: 'icon-ok',
                        handler: function () {
                            var permitMsg = $('#permit-msg').textbox('getValue');
                            if ($.trim(permitMsg) == '') {
                                $.messager.alert('提示', '如果要禁用该商家必须输入禁用原因', 'info');
                                return;
                            }
                            submitUpdateMerchantPermint(data, permit);
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#disable-win').dialog('close');
                        }
                    }],
                    onBeforeOpen: function () {
                        $('#disable-merchant-name').html(data.merchName);
                    }
                });
                return;
            }
            submitUpdateMerchantPermint(data, permit);
        }

        function submitUpdateMerchantPermint(data, permit) {
            if (permit) {
                var tipMsg = "你确定要 启用 该商家吗？";
            } else {
                var tipMsg = "你确定要 禁用 该商家吗？";
            }
            $.messager.confirm('提示', tipMsg, function (r) {
                if (r) {
                    var json = {};
                    json.merchantId = data.merchantId;
                    json.permit = permit;
                    if (!permit) {
                        json.permitMsg = $('#permit-msg').textbox('getValue');
                    } else {
                        json.permitMsg = "无";
                    }
                    $.messager.progress();
                    $.ajax({
                        url: '${basePath}/admin/api/merchant/updatePermit',
                        type: 'POST',
                        dataType: 'text',
                        data: json,
                        success: function (data) {
                            data = $.parseJSON(data);
                            if (data.code == 200) {
                                $.messager.show({
                                    title: '提示',
                                    msg: data.msg
                                });
                                if (!permit) {
                                    $('#disable-win').dialog('close');
                                }
                                $("#merchant_grid").datagrid('load');
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
                    });
                }
            })
        }

        /*=========修改营业执照===========*/
        function editMerch() {
            var rows = $('#merchant_grid').datagrid('getChecked');
            if (rows.length <= 0) {
                $.messager.alert('提示', '请选中一个商家进行修改', 'info');
                return;
            }
            var merchant = rows[0];
            $('#edit-merch-edit').dialog({
                width: 300,
                title: '修改商家营业执照',
                buttons: [{
                    text: '确认',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $.ajax({
                            url: '${basePath}/admin/api/updateMerchant/busLicePhoto',
                            type: 'post',
                            dataType: 'json',
                            data: {
                                merchantId: merchant.merchantId,
                                imgInfoId: $('#imgId').val()
                            },
                            success: function (info) {
                                if (info.success) {
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                    $('#edit-merch-edit').dialog('close');
                                    $('#merchant_grid').datagrid('reload');
                                } else {
                                    $.messager.alert('错误提示', info.msg, 'error');
                                }
                            },
                            error: function () {
                                $.messager.alert('错误提示', '系统错误，请于管理员联系', 'error');
                            }
                        });
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#edit-merch-edit').dialog('close');
                    }
                }],
                onBeforeOpen: function () {
                    if (!merchant.hasOwnProperty("busLicePhoto")) {
                        $('#imgName').textbox('setText', '');
                        $('#imgId').val('');
                        $('#preview_icon').attr('src', "${basePath}/static/images/preview.jpg");
                        return;
                    }
                    $('#imgName').textbox('setText', merchant.busLicePhoto.key);
                    $('#imgId').val(merchant.busLicePhoto.imgInfoId);
                    $('#preview_icon').attr('src', merchant.busLicePhoto.url);
                }
            });
        }

        /*========修改商家基本信息=======*/
        function editMerchantInfo() {
            var rows = $('#merchant_grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            var data = rows[0];
            $('#edit-merchant-info-win').dialog({
                title: '修改商家基本信息',
                iconCls: 'icon-edit',
                width: 600,
                height: 400,
                onBeforeOpen: function () {
                    $('#edit-merchant-info-win').attr('src', '${basePath}/admin/api/merchant/' + data.merchantId + '/editMerchantInfo');
                }
            });
        }

        /*========修改商家商家图片=======*/
        function editMerchantImg() {
            var rows = $('#merchant_grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            var data = rows[0];
        }

        function changeValue(id) {
            var examinStatedom = $("#examinState")
            var checkenabledom = $("#checkenable")
            if (id == "enable") {
                checkenabledom.menubutton({
                    text: "已启用"
                });
            } else if (id == "unable") {
                checkenabledom.menubutton({
                    text: "未启用"
                });
            } else if (id == "enableandunable") {
                checkenabledom.menubutton({
                    text: "全部"
                });
            } else if (id == "State1") {
                examinStatedom.menubutton({
                    text: "未提交审核"
                });
                examinStatedom.attr("name", "1");
            } else if (id == "State2") {
                examinStatedom.menubutton({
                    text: "待审核"
                });
                examinStatedom.attr("name", "2");
            } else if (id == "State3") {
                examinStatedom.menubutton({
                    text: "已审核通过"
                });
                examinStatedom.attr("name", "3");
            } else if (id == "State4") {
                examinStatedom.menubutton({
                    text: "未通过审核"
                });
                examinStatedom.attr("name", "4");
            } else if (id == "Stateall") {
                examinStatedom.menubutton({
                    text: "全部"
                });
                examinStatedom.attr("name", "");
            }
        }

        function preview(url) {
            $('#preview').dialog({
                title: '预览商家信息',
                width: 750,
                height: 400,
                modal: true,
                resizable: true,
                maximizable: true,
                onBeforeOpen: function () {
                    $('#preview').attr('src', '${basePath}/admin/merchant/info/' + url + '');
                }
            });
        }

    </script>
</head>
<body>
<table id="merchant_grid"></table>
<%--工具栏&搜索栏开始--%>
<div id="grid_toolbar" class="toolbar-container">
    <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" id="able-merch">启用</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" id="disable-merch">禁用</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-edit'" id="edit-merch">修改商家营业执照</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-edit'" id="edit-merch-info">修改商家基本信息</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-photo'" id="edit-merch-img">修改商家图片信息</a>
    商家名:<input class="easyui-textbox" id="merchName" data-options="prompt:'请输入商家名'"/>
    商家电话号码:<input class="easyui-textbox" id="merchTell" data-options="prompt:'请输入商家电话号码'"/>
    是否启用:
    <select class="easyui-combobox" id="permit" data-options="editable:false,panelHeight:80" style="width:70px;">
        <option value="">全部</option>
        <option value="1">启用</option>
        <option value="0">禁用</option>
    </select>
    是否通过审核:
    <select class="easyui-combobox" id="examinState"
            data-options="editable:false,panelHeight:95" style="width:70px;">
        <option value="">全部</option>
        <option value="2">待审核</option>
        <option value="3">通过</option>
        <option value="4">未通过</option>
    </select>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="search">搜索</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" id="clear-search">清除搜索</a>
</div>
<%--工具栏&搜索栏结束--%>

<%--修改商家营业执照--%>
<div id="edit-merch-edit" style="padding: 10px 30px">
    <input type="hidden" id="imgId">
    <div>商家营业执照：</div>
    <div><input class="easyui-textbox" id="imgName" data-options="editable:false" required/>
        <button id="select" style="margin-left: 5px">选择图片</button>
    </div>
    <div style="margin-top: 10px">
        <img src="${basePath}/static/images/preview.jpg" id="preview_icon"
             style="width: 100px;height: 100px;border: solid;border-width: 1px;border-color: #95b8e7">
    </div>
</div>

<%--商家详细信息预览--%>
<iframe id="preview"></iframe>

<%--修改商家基本信息--%>
<iframe id="edit-merchant-info-win"></iframe>

<%--修改商家图片信息--%>
<iframe id="edit-merchant-img-win"></iframe>

<div id="disable-win">
    <table align="center">
        <tr>
            <td>商家名：</td>
            <td><span id="disable-merchant-name"></span></td>
        </tr>
        <tr>
            <td>禁用原因：</td>
            <td><input id="permit-msg" class="easyui-textbox" style="height: 80px"
                       data-options="multiline:true,required:true"></td>
        </tr>
    </table>
</div>

</body>
</html>
