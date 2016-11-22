<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/6/7
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<title>浏览统计</title>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
<script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript">
    $(function () {
        /*===========数据网格========*/
        $('#dg').datagrid({
            url: '${basePath}/admin/api/merchantbrowse/list',
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
                    field: 'merchant', title: '商家名', width: 100, align: 'center',
                    formatter: function (data) {
                        return data.merchName;
                    }
                },
                {
                    field: 'merchTell', title: '商家联系电话', width: 100, align: 'center',
                    formatter: function (value,row,index) {
                        return row.merchant.merchTell;
                    }
                },
                {
                    field: 'user', title: '用户名称', width: 100, align: 'center',
                    formatter: function (data) {
                        return data.userName;
                    }
                },
                {
                    field: 'userPhone', title: '用户电话', width: 100, align: 'center',
                    formatter: function (value,row,index) {
                        var phone = row.user.userPhone;
                        var mphone = phone.substr(0, 3) + '****' + phone.substr(7);
                        return mphone;
                    }
                },
                {
                    field: 'userage', title: '用户年龄', width: 100, align: 'center',
                    formatter: function (value,row,index) {
                        return row.user.age;
                    }
                },
                {
                    field: 'usersex', title: '性别', width: 100, align: 'center',
                    formatter: function (value, row, index) {
                        var usersex = row.user.sex;
                        if (usersex) {
                            return "男";
                        } else {
                            return "女";
                        }
                    }
                },
                {
                    field: 'userarea', title: '区域', width: 100, align: 'center',
                    formatter: function (value, row, index) {
                        if(row.user.area==null){
                            return "";
                        }
                            return  row.user.area.fullName;
                    }
                },
                {
                    field: 'browseDate', title: '浏览时间', width: 100, align: 'center',sortable: true,
//                    formatter: function (value, row, index) {
//                        var date  = new Date(parseInt(value));
//                        var y = date.getFullYear();
//                        var m = date.getMonth() + 1;
//                        m = m < 10 ? ('0' + m) : m;
//                        var d = date.getDate();
//                        d = d < 10 ? ('0' + d) : d;
//                        var h = date.getHours();
//                        var minute = date.getMinutes();
//                        minute = minute < 10 ? ('0' + minute) : minute;
//                        return y + '-' + m + '-' + d+' '+h+':'+minute;
//                    }
                }
            ]],
            toolbar: "#grid_toolbar"

        });

        /*================用户搜索================*/
        $('#search').click(function () {
            $('#dg').datagrid('load', {
                searchName: "merchant.merchName",
                searchValue: $('#merchant-name').textbox('getValue'),
                userName:$('#user-name').textbox('getValue'),
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
    <div id="grid_toolbar" style="height:30px">
        <form id="search-form">
            商家名:<input class="easyui-textbox" id="merchant-name" prompt="输入名称"/>&nbsp;
            用户名:<input class="easyui-textbox" id="user-name" prompt="输入名称"/>&nbsp;
            浏览时间：从<input id="start-time" type="text" class="easyui-datebox"
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
