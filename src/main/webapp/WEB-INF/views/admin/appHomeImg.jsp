<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/4/8
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
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
        $(document).ready(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/appHomeImg/list',
                title: 'APP封面图',
                fit: true,
                fitColumns: true,
                columns: [[
                    {
                        field: 'imgInfo', title: '封面图', width: 100, align: 'center',
                        formatter: function (data) {
                            return "<img src='" + data.url + "' style='width:60'>";
                        }
                    },
                    {field: 'homeImgName', title: '封面图名称', width: 100, align: 'center'},
                    {
                        field: 'aaa', title: '图片大小', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            return "宽:" + row.imgInfo.width + "px" + "&nbsp;&nbsp;&nbsp;高:" + row.imgInfo.height + "px";
                        }
                    },
                    {
                        field: 'bbb', title: '最近一次更新时间', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            return row.imgInfo.createDate;
                        }
                    },
                ]],
                toolbar: [{
                    iconCls: 'icon-edit',
                    text: '添加或修改',
                    handler: function () {
                        addOrEdit();
                    }
                }, '-']
            });

            /*=========上传导航图标========*/
            initUploader({
                pickbutton: 'select',
                onFileAdd: function (file) {

                },
                onError: function (file, err, errTip) {

                },
                onBeforeUpload: function (up, file) {
                    total = up.total.size;
                    loaded = up.total.loaded;
                    progress = parseInt(loaded / total * 100);
                    $('#progress').html('正在上传...');
                },
                onUploaded: function (up, file, info) {
                    console.log(info);
                    $('#pic-name').textbox('setText', file.name);
                    $('#imgId').val(info.imgInfoId);
                    $('#imgPreview').attr('src', info.url);
                    $('#progress').html('上传完成');
                },
                onComplete: function () {

                }
            });

        });

        function addOrEdit() {
            $('#add-edit-win').dialog({
                title: '添加或修改',
                iconCls: 'icon-edit',
                width: 400,
                buttons: [{
                    iconCls: 'icon-ok',
                    text: '保存',
                    handler: function () {
                        $('#add-edit-form').form({
                            url: '${basePath}/admin/api/appHomeImg/add',
                            success: function (data) {
                                data = $.parseJSON(data);
                                $.messager.show({
                                    title: '提示',
                                    msg: data.msg
                                });
                                if (data.success) {
                                    $('#add-edit-win').dialog('close');
                                    $('#dg').datagrid('load');
                                }
                            }
                        });
                        $('#add-edit-form').form('submit');
                    }
                }, {
                    iconCls: 'icon-cancel',
                    text: '关闭',
                    handler: function () {
                        $('#add-edit-win').dialog('close');
                    }
                }],
                onBeforeOpen: function () {
                    var rows = $('#dg').datagrid('getRows');
                    if (rows.length > 0) {
                        var firstRow = rows[0];
                        $('#add-edit-form').form('reset');
                        $('#add-edit-form').form('load', firstRow);
                        $('#imgId').val(firstRow.imgInfo.imgInfoId);
                        $('#pic-name').textbox('setValue', firstRow.imgInfo.key);
                        $('#imgPreview').attr('src', firstRow.imgInfo.url);
                    }
                }
            });
        }
    </script>
    <style>
        body {
            padding: 0px;
            margin: 0px;
            font-family: "Microsoft Yahei";
        }

        ul {
            list-style-type: none;
        }

        ul li {
            float: left;
            margin: 5px auto 5px auto;
        }

        .li-title {
            width: 30%;
        }

        .li-content {
            width: 70%;
        }

        .clear {
            clear: both;
        }


    </style>
</head>
<body>
<div id="dg"></div>

<div id="add-edit-win">
    <form id="add-edit-form">
        <input type="hidden" name="homeImgId">
        <input type="hidden" id="imgId" name="imgInfo.imgInfoId">
        <ul>
            <li class="li-title">封面图片名:</li>
            <li class="li-content"><input class="easyui-textbox" style="width: 200px" name="homeImgName"
                                          data-options="required:true"></li>
            <li class="li-title">上传图片:<br>(建议比例1920*1080)</li>
            <li class="li-content"><input class="easyui-textbox" style="width: 120px"
                                          data-options="required:true" id="pic-name">
                <button style="margin-left: 10px" id="select">选择图片</button>
            </li>
            <li class="li-title"></li>
            <li class="li-content">
                <img src="${basePath}/static/images/preview.jpg"
                     style="width: 70px;" id="imgPreview">
                <span id="progress"></span>
            </li>
            <div class="clear"></div>
        </ul>
    </form>
</div>

</body>
</html>
