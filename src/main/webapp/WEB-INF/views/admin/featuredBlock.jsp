<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/3/10
  Time: 11:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>特色街区管理</title>
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
            /*===========数据网格========*/
            $('#dg').datagrid({
                url: '${basePath}/admin/api/featuredBlock/list',
                title: '特色街区管理',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                striped: true,
                rownumbers: true,
                columns: [[
                    {field: 'fbId', checkbox: true, width: 100, align: 'center'},
                    {
                        field: 'coverImg', title: '封面图', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return "<div style='width: 65px;height: 45px;margin: 1px auto 1px auto'><img style='width: 65px;height: 65px;' src='" + data.url + "'></div>"
                            }
                            return "<span style='color: red' '>无封面图</span>"
                        }
                    },
                    {field: 'fbName', title: '名称', width: 100, align: 'center'},
                    {field: 'fbOrder', title: '顺序', width: 100, align: 'center'},
                    //{field: 'area', title: '所属区域', width: 100, align: 'center'},
                    {
                        field: 'permit', title: '是否启用', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return "<span style='color: green'>启用</span>";
                            } else {
                                return "<span style='color: red'>未启用</span>"
                            }
                        }
                    }
                ]],
                toolbar: '#tb'
            });

            /*========添加特色街区=======*/
            $('#add-button').click(function () {
                $('#add-edit-win').dialog({
                    iconCls: 'icon-add',
                    title: ' 添加特色街区',
                    width: 400,
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#add-edit-form').form({
                                url: '${basePath}/admin/api/featuredBlock/add',
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

            /*========修改特色街区========*/
            $('#edit-button').click(function () {
                var rows = $('#dg').datagrid('getSelections');
                if (rows.length <= 0) {
                    $.messager.alert('提示', '请选中一条数据进行操作', 'info');
                    return;
                } else {
                    $('#add-edit-win').dialog({
                        title: '修改特色街区',
                        iconCls: 'icon-edit',
                        width: 400,
                        buttons: [{
                            text: '修改',
                            iconCls: 'icon-ok',
                            handler: function () {
                                $('#add-edit-form').form({
                                    url: '${basePath}/admin/api/featuredBlock/edit',
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
                            $('#fbCoverImgName').textbox('setValue', rows[0].coverImg.key);
                            $('#preview_icon').attr('src', rows[0].coverImg.url);
                        }
                    });
                }
            });


            /*========删除=======*/
            $('#forbid-button').click(function () {
                var rows = $('#dg').datagrid('getSelections');
                if (rows.length <= 0) {
                    $.messager.alert('提示', '请选中一条数据进行操作', 'info');
                    return;
                } else {
                    if (!rows[0].permit) {
                        $.messager.alert('提示', '当前状态已删除，勿重复启用!','info');
                        return;
                    }
                    $.messager.confirm('确认', '您确认要删除吗？', function (r) {
                        if (r) {
                            $.ajax({
                                url: '${basePath}/admin/api/featuredBlock/updateDelState',
                                type: 'post',
                                dataType: 'text',
                                data: {
                                    fbId: rows[0].fbId,
                                    permit: 'false'
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
                }
            });

            /*========上传封面图=======*/
            initUploader({
                pickbutton: 'select',
                onFileAdd: function (file) {
                    $('#fbCoverImgName').textbox('setText', file.name);
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

        #fbCoverImgName {
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
    <a class="easyui-linkbutton" id="add-button" data-options="iconCls:'icon-add',plain:true" >添加</a>
    <a class="easyui-linkbutton" id="edit-button" data-options="iconCls:'icon-edit',plain:true" >修改</a>
    <a class="easyui-linkbutton" id="forbid-button" data-options="iconCls:'icon-remove',plain:true">删除</a>
</div>
<!--toolbar&seacherbar-->

<!--数据网格开始-->
<div id="dg"></div>
<!--数据网格结束-->

<!--添加和修改特色街区开始-->
<div id="add-edit-win">
    <form id="add-edit-form" method="post">
        <input type="hidden" name="fbId">
        <input type="hidden" name="coverImg.imgInfoId" id="coverImgId"/>
        <input type="hidden" name="permit" value="true"/>
        <ul>
            <li class="li-title">特色街区名称：</li>
            <li><input class="easyui-textbox" name="fbName" data-options="required:true"/></li>
            <li class="li-title">封面图：<br>(建议比例：1052*498)</li>
            <li>
                <div>
                    <input class="easyui-textbox" id="fbCoverImgName" data-options="required:true"/>
                    <button type="button" id="select">选择图片</button>
                </div>
                <div>
                    <img src="${basePath}/static/images/preview.jpg" id="preview_icon"
                         style="width: 100px;height: 100px;border: solid;border-width: 1px;border-color: #95b8e7">
                </div>
            </li>
            <li class="li-title">顺序：</li>
            <li><input class="easyui-numberspinner" name="fbOrder" data-options="required:true"/></li>
            <div class="clear" style="margin-bottom: 13px"></div>
        </ul>
    </form>
</div>
<!--添加和修改特色街区结束-->
</body>
</html>
