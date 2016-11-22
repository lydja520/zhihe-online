<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/4/25
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<title>登录统计</title>
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
    $(function () {

        /*===========数据网格========*/
        $('#dg').datagrid({
            url: '${basePath}/admin/api/user/journal',
//            title: '用户列表',
            fit: true,
            border: false,
            fitColumns: true,
            pageSize: 30,
            pagination: true,
            singleSelect: true,
            striped: true,
            rownumbers: true,
            columns: [[
                {field: 'userCode', title: '账号', width: 100, align: 'center'},
                {field: 'loginDate', title: '登录时间', width: 150, align: 'center', sortable: true},
                {field: 'loginIP', title: '登录IP', width: 110, align: 'center'},
                {field: 'appVersionName', title: 'APP版本名字', width: 80, align: 'center'},
                {field: 'appVersionCode', title: 'APP版本码', width: 80, align: 'center'},
                /*                {field: 'mobileName', title: '手机型号', width: 150, align: 'center'},*/
                {field: 'osName', title: '手机系统类型', width: 100, align: 'center'},
                /*                {field: 'osVersion', title: '手机系统版本', width: 100, align: 'center'},*/
                {
                    field: 'success', title: '是否登录成功', width: 100, align: 'center',
                    formatter: function (value, rows, index) {
                        if (value == true) {
                            return "成功";
                        }
                        return "失败";
                    }
                },
                {field: 'failReason', title: '登录失败原因', width: 200, align: 'center'}
            ]],
            toolbar: "#grid_toolbar"

        });
        /*================用户搜索================*/
        $('#search').click(function () {
            $('#dg').datagrid('load', {
                searchName: "userCode",
                searchValue: $('#user-code').textbox('getValue'),
                initTime: $('#start-time').datebox('getValue'),
                endTime: $('#end-time').datebox('getValue')
            });
        });
        /*===========清楚搜索数据===========*/
        $('#clear-search').click(function () {
            $('#dg').datagrid('load', {});
            $('#search-form').form('reset');
        });
    })
</script>
</head>

<body class="easyui-layout">
<div data-options="region:'center'">
    <div id="grid_toolbar" style="height: 30px">
        <form id="search-form">
            用户账号:<input class="easyui-textbox" id="user-code" prompt="输入账号"/>&nbsp;
            登录时间：从<input id="start-time" type="text" class="easyui-datebox"
                         data-options="editable:false" prompt="请选择开始时间"/>
            至<input id="end-time" type="text" class="easyui-datebox"
                    data-options="editable:false" prompt="请输入结束时间"/>
            <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">
                搜索
            </a>
            <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">
                清除搜索
            </a>
        </form>
    </div>
    <div id="dg"></div>
</div>
</body>
</html>
