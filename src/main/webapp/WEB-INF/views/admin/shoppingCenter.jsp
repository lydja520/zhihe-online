<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/3/10
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>购物中心管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/shoppingCenter/list',
                title: '购物中心管理',
                fit: true,
                fitColumns: true,
                striped: true,
                rownumbers: true,
                singleSelect: true,
                pagination: true,
                columns: [[
                    {field: 'scId', checkbox: true, width: 100, align: 'center'},
                    {
                        field: 'coverImg', title: '封面图', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return "<div style='width: 65px;height: 45px;margin: 1px auto 1px auto'><img style='width: 65px;height: 65px;' src='" + data.url + "'></div>"
                            }
                            return "<span style='color: red' '>无封面图</span>"
                        }
                    },
                    {field: 'scName', title: '名称', width: 100, align: 'center'},
                    {field: 'scOrder', title: '顺序', width: 100, align: 'center'}
                ]],
                toolbar: '#tb'
            });

            /*========添加购物中心=========*/
            $('#add-button').click(function () {
                $('#add-edit-win').dialog({
                    title: '添加购物中心',
                    iconCls: 'icon-add',
                    width: '400',
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#add-edit-form').form({
                                url: '${basePath}/admin/shoppingCenter/add',
                                success: function (info) {
                                    info = $.parseJSON(info);
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                    if (info.success) {
                                        $('#add-edit-win').dialog('close');
                                        $('#dg').datagrid('reload');
                                    }
                                }
                            });
                            $('#add-edit-form').submit();
                        }
                    }, {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#add-edit-win').dialog('close');
                        }
                    }],
                    onBeforeOpen: function () {
                        $('#preview_icon').attr('src','${basePath}/static/images/preview.jpg');
                        $('#add-edit-form').form('reset');
                    }
                });
            });

            /*========修改购物中心========*/
            $('#edit-button').click(function () {
                var rows = $('#dg').datagrid('getSelections');
                if (rows.length > 0) {
                    $('#add-edit-win').dialog({
                        title: '修改购物中心',
                        iconCls: 'icon-edit',
                        width: 400,
                        buttons: [{
                            text: '保存',
                            iconCls: 'icon-ok',
                            handler: function () {
                                $('#add-edit-form').form({
                                    url: '${basePath}/admin/shoppingCenter/edit',
                                    success: function (info) {
                                        info = $.parseJSON(info);
                                        $.messager.show({
                                            title: '提示',
                                            msg: info.msg
                                        });
                                        if (info.success) {
                                            $('#add-edit-win').dialog('close');
                                            $('#dg').datagrid('reload');
                                        }
                                    }
                                });
                                $('#add-edit-form').submit();
                            }
                        }, {
                            text: '关闭',
                            iconCls: 'icon-cancel',
                            handler: function () {
                                $('#add-edit-win').dialog('close');
                            }
                        }],
                        onBeforeOpen: function () {
                            $('#add-edit-form').form('load', rows[0]);
                            $('#coverImgId').attr('value', rows[0].coverImg.imgInfoId);
                            $('#scCoverImgName').textbox('setValue', rows[0].coverImg.key);
                            $('#preview_icon').attr('src', rows[0].coverImg.url);
                        }
                    });
                } else {
                    $.messager.alert('提示', '请选中一条数据进行修改', 'info');
                }
            });

            /*========禁用========*/
            $('#forbib-button').click(function () {
                var rows = $('#dg').datagrid('getSelections');
                if (rows.length > 0) {
                    console.log(rows[0].permit)
                    if (!rows[0].permit) {
                        $.messager.alert('提示', '该条记录已经删除,不用重复启用', 'info');
                        return;
                    }
                    $.messager.confirm('确认', '您确认要删除吗？', function (r) {
                        if (r) {
                            $.ajax({
                                url: '${basePath}/admin/shoppingCenter/updateDelState',
                                type: 'post',
                                dataType: 'text',
                                data: {
                                    scId:rows[0].scId,
                                    permit:'false'
                                },
                                success: function (info) {
                                    info = $.parseJSON(info);
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                    if (info.success) {
                                        $('#dg').datagrid('reload');
                                    }
                                }
                            });
                        }
                    });
                } else {
                    $.messager.alert('提示', '请选中一条数据进行修改', 'info');
                }
            });

            /*========上传封面图=======*/
            initUploader({
                pickbutton: 'select',
                onFileAdd: function (file) {
                    $('#scCoverImgName').textbox('setText', file.name);
                },
                onError: function (file, err, errTip) {

                },
                onBeforeUpload: function (up, file) {
                    $('#preview_icon').attr('src', "/static/images/loading.gif");
                },
                onUploaded: function (up, file, info) {
                    console.log(info);
                    $('#coverImgId').val(info.imgInfoId);
                    $('#preview_icon').attr('src', info.url);
                },
                onComplete: function () {

                }
            });
        });
    </script>
    <style>
        ul {
            width: 80%;
            margin: 10px auto 10px auto;
            padding: 0px;
            list-style-type: none;
        }

        ul li {
            width: 70%;
            float: left;
            text-align: left;
            margin-top: 13px;
        }

        .li-title {
            width: 30%;
        }

        #scCoverImgName {
            width: 100px;
        }

        #preview_icon {
            margin-top: 5px;
        }

        .clear {
            clear: both;
        }
    </style>
</head>
<body>

<!--toolbar&seacherbar-->
<div id="tb">
    <a class="easyui-linkbutton" id="add-button" data-options="iconCls:'icon-add',plain:true">添加</a>
    <a class="easyui-linkbutton" id="edit-button" data-options="iconCls:'icon-edit',plain:true">修改</a>
    <a class="easyui-linkbutton" id="forbib-button" data-options="iconCls:'icon-remove',plain:true">删除</a>
</div>
<!--toolbar&seacherbar-->


<!--数据网格开始-->
<div id="dg"></div>
<!--数据网格结束-->


<!--添加和修改购物中心开始-->
<div id="add-edit-win">
    <form id="add-edit-form" method="post">
        <input type="hidden" name="scId" id="sc-id"/>
        <input type="hidden" name="coverImg.imgInfoId" id="coverImgId"/>
        <input type="hidden" name="permit" value="true"/>
        <ul>
            <li class="li-title">购物中心名称：</li>
            <li><input class="easyui-textbox" name="scName" data-options="required:true"/></li>
            <li class="li-title">封面图：<br>(建议比例：1052*498)</li>
            <li>
                <div>
                    <input class="easyui-textbox" id="scCoverImgName" data-options="required:true"/>
                    <button type="button" id="select">选择图片</button>
                </div>
                <div>
                    <img src="${basePath}/static/images/preview.jpg" id="preview_icon"
                         style="width: 100px;height: 100px;border: solid;border-width: 1px;border-color: #95b8e7">
                </div>
            </li>
            <li class="li-title" >顺序：</li>
            <li><input class="easyui-numberspinner" name="scOrder" data-options="required:true"/></li>
            <div class="clear" style="margin-bottom: 13px"></div>
        </ul>
    </form>
</div>
<!--添加和修改购物中心结束-->

</body>
</html>
