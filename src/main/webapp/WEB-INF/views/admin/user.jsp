<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/4/19
  Time: 15:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<title>用户统计</title>
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

        sumaryUserInfo();

        /*===========数据网格========*/
        $('#dg').datagrid({
            url: '${basePath}/admin/api/user/list',
//            title: '用户列表',
            fit: true,
            border: false,
            fitColumns: true,
            pageSize: 20,
            pagination: true,
            singleSelect: true,
            striped: true,
            rownumbers: true,
            columns: [[
                {
                    field: 'portrait', title: '用户头像', width: 100, align: 'center',
                    formatter: function (value) {
                        if (value) {
                            return "<img src='" + value.url + "' style='width: 40px;height: 40px;display: block;margin: 1px auto;border-radius: 20px' />";
                        }
                        return "<img src='${basePath}/static/images/unknow.jpg' style='width: 40px;height: 40px;display: block;margin: 1px auto;border-radius: 20px' />";
                    }
                },
                {
                    field: 'userName', title: '用户名称', width: 100, align: 'center'
                },
                {
                    field: 'userPhone', title: '用户电话', width: 100, align: 'center'
                },
                {
                    field: 'invitCode', title: '邀请码', width: 100, align: 'center',
                    formatter: function (data) {
                        if (data) {
                            return data;
                        }
                        return "无";
                    }
                },
                {
                    field: 'age', title: '用户年龄', width: 100, align: 'center'
                },
                {
                    field: 'sex', title: '性别', width: 100, align: 'center',
                    formatter: function (value, row, index) {
                        if (value) {
                            return "男";
                        } else {
                            return "女";
                        }
                    }
                },
                {
                    field: 'area', title: '区域', width: 100, align: 'center',
                    formatter: function (data) {
                        if (data) {
                            return data.fullName;
                        }
                    }
                },
                {
                    field: 'occupation', title: '职业', width: 100, align: 'center'
                },
                {
                    field: 'income', title: '收入', width: 100, align: 'center'
                },
                {
                    field: 'createDate', title: '注册时间', width: 100, align: 'center', sortable: true
                }

            ]],
            toolbar: "#grid_toolbar"

        });

        /*================用户搜索================*/
        $('#search').click(function () {
            $('#dg').datagrid('load', {
                searchPhone: $('#user-phone').textbox('getValue'),
                searchInvitCode: $('#invit-code').textbox('getValue'),
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
    function sumaryUserInfo() {
        $.ajax({
            url: '${basePath}/admin/api/user/statistic',
            dataType: 'text',
            type: 'get',
            data: {},
            success: function (info) {
                info = $.parseJSON(info);
                $('#userTotal').html(info.userTotal);
                $('#newUserTotal').html(info.newUserTotal);
                $('#activeUser').html(info.activeUser);
                setTimeout(sumaryUserInfo, 1000 * 60);
            }
        });
    }
</script>
</head>

<body class="easyui-layout">

<div title="用户统计" data-options="region:'north',collapsible:false" style="height: 100px">
    <div style="margin:7px auto 5px 10px">
        <div style="width:130px;height:50px;border:1px solid #95b8e7;margin-left:10px;text-align:center;border-radius:5px;float:left;">
            <div style="height: 25px;line-height: 25px;color: #ff5500">用户数量</div>
            <div id="userTotal" style="height: 25px;line-height: 25px"></div>
        </div>
        <div style="width:130px;height:50px;border:1px solid #95b8e7;margin-left:10px;text-align:center;border-radius:5px;float: left">
            <div style="height: 25px;line-height: 25px;color: #ff5500">今日新增用户</div>
            <div id="newUserTotal" style="height: 25px;line-height: 25px"></div>
        </div>
        <div style="width:130px;height:50px;border:1px solid#95b8e7;margin-left:10px;text-align:center;border-radius:5px;float: left">
            <div style="height: 25px;line-height: 25px;color: #ff5500">活跃用户</div>
            <div id="activeUser" style="height: 25px;line-height: 25px"></div>
        </div>
        <div style="clear: both"></div>
    </div>
</div>

<div data-options="region:'center'">
    <div id="grid_toolbar" style="height:30px">
        <form id="search-form">
            用户手机号:<input class="easyui-textbox" id="user-phone" prompt="输入手机号"/>&nbsp;
            邀请码:<input class="easyui-textbox" id="invit-code" prompt="输入邀请码">&nbsp;
            注册时间：从<input id="start-time" type="text" class="easyui-datebox"
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
