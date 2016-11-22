<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 2016/7/3
  Time: 17:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/merchantPageTemplate/list',
                title: '商家自定义页面',
                fit: true,
                border: false,
                fitColumns: true,
                pageSize: 20,
                pagination: true,
                singleSelect: true,
                striped: true,
                rownumbers: true,
                columns: [[
                    {field: 'checkbox', checkbox:true,align: 'center'},
                    {
                        field: 'templateCoverImg', title: '封面图', width: 100, align: 'center',
                        formatter: function (url) {
                            if (url) {
                                return "<img style='width: 50px;height: 50px;' src='" + url + "'>"
                            }
                            return "<span style='color: red' '>无封面图</span>"
                        }
                    },
                    {field: 'templateName', title: '页面名', width: 100, align: 'center'},
                    {field: 'templateDesc', title: '简介', width: 100, align: 'center'},
                    {
                        field: 'defaultState', title: '是否是默认模板', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return "<span style='color:green'>是</span>";
                            }
                            return "<span style='color: red'>否</span>";
                        }
                    },
                    {
                        field: '_templateUrl', title: '页面地址', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            return "<button onclick='prieview(this.value)' value='" + row.templateUrl + "'>预览</button>"
                        }
                    },
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        alert('edit')
                    }
                }, '-', {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                        alert('help')
                    }
                }, '-', {
                    text: '删除',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        alert('delete')
                    }
                },'-',{
                    text: '设为默认',
                    iconCls: 'icon-wrench',
                    handler: function () {
                        alert('default')
                    }
                }]
            });
        });

        function prieview(url) {
            $('#preview').dialog({
                width: 700,
                height: 480,
                title: '预览',
                modal: true,
                onBeforeOpen: function () {
                    $('#preview').attr('src', url);
                }
            });
        }
    </script>
</head>
<body>

<div id="dg"></div>

<iframe id="preview"></iframe>

</body>
</html>
