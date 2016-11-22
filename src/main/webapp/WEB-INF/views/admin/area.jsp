<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/11/23
  Time: 17:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>区域管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(function () {
            /*=========地域信息网格信息初始化化========*/
            $('#area_grid').datagrid({
                url: '/admin/api/area/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                columns: [[
                    {field: 'areaId', hidden:true, width: 100, align: 'center'},
                    {field: 'areaName', title: '地区名', width: 100, align: 'center'},
                    {field: 'fullName', title: '地区全名', width: 100, align: 'center'},
                    {
                        field: 'isRoot', title: '是否是根地域', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return '是';
                            } else {
                                return '否';
                            }
                        }
                    },
                    {
                        field: 'parentArea', title: '上一级地区', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return value.areaName;
                            }
                        }
                    }
                ]],
                toolbar: '#grid_toolbar'
            });
            /*=========上一级地域信息网格信息初始化化========*/
            $('#parent_areas_datagrid').datagrid({
                url: '/admin/api/area/parentList',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                columns: [[
                    {field: 'areaId', hidden: true, width: 100, align: 'center'},
                    {field: 'areaName', title: '地区名', width: 100, align: 'center'},
                    {
                        field: 'isRoot', title: '是否是根地域', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return '是';
                            } else {
                                return '否';
                            }
                        }
                    }
                ]],
            });
            /*=========工具栏========*/
            $('#add_button').click(function () {
                $('#add_win').dialog({
                    title: '添加记录',
                    width: 400,
                    modal: true,
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#add_form').form('submit', {
                                success: function (info) {
                                    info = $.parseJSON(info);
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                    if (info.success) {
                                        $('#add_win').dialog('close');
                                        $('#area_grid').datagrid('load');
                                    }
                                }
                            })
                        }
                    }, {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#add_win').dialog('close');
                        }
                    }]
                });
                $('#add_form').form('reset');
                var flag = $('#add_isRoot input[name="isRoot"]:checked ').val();
                is_disp(flag);
                $('#add_parent_area').combo({
                    hasDownArrow: false,
                    editable: false,
                    onShowPanel: function () {
                        $(this).combo('hidePanel');
                        $('#parent_areas').dialog({
                            width: 600,
                            height: 350,
                            title: '区域列表',
                            modal: true
                        });
                        $('#parent_areas_datagrid').datagrid({
                            onClickRow: function (index, row) {
                                $('#parent_areas').dialog('close');
                                $('#add_parent_area').combo('setValue', row.areaId);
                                $('#add_parent_area').combo('setText', row.areaName);
                            }
                        });
                    }
                });
            });
            /*=========搜索栏========*/
            $('#area_search').searchbox({
                menu: '#area_search_select',
                prompt: '请输入值',
                searcher: function (value, name) {
                    var param = {};
                    if (value) {
                        param.searchName = name;
                        param.searchValue = value;
                    }
                    $('#area_grid').datagrid('load', param);
                }
            });
        });

        /*=========是否显示上一级datagrid========*/
        function is_disp(flag) {
            if (flag == true) {
                $('#add_parent_area').combo('clear');
                $('#add_parent').hide();
            } else {
                $('#add_parent').show();
            }
        }
    </script>
</head>
<body>
<!--工具栏和搜索栏开始-->
<div id="grid_toolbar">
    <a id="add_button" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>

    <input id="area_search" style="width:300px">
    <div id="area_search_select" style="width:200px">
        <div data-options="name:'areaName',iconCls:'icon-ok'">地区名</div>
    </div>
</div>
<!--工具栏和搜索栏结束-->

<!--显示地域信息开始-->
<table id="area_grid"></table>
<!--显示地域信息结束-->

<!--增加地域信息-->
<div id="add_win">
    <form id="add_form" action="${basePath}/admin/api/area/add" method="post">
        <table align="center">
            <tr>
                <td>
                    地区名：
                </td>
                <td>
                    <input class="easyui-textbox" name="areaName" data-options="required:true">
                </td>
            </tr>
            <tr id="add_isRoot">
                <td>
                    是否是根区域：
                </td>
                <td>
                    <input type="radio" name="isRoot" value="true" onclick="is_disp(true)">是
                    <input type="radio" name="isRoot" value="false" checked onclick="is_disp(false)">否
                </td>
            </tr>
            <tr id="add_parent">
                <td>
                    上一级区域名：
                </td>
                <td>
                    <input id="add_parent_area" name="parentArea.areaId">
                </td>
            </tr>
        </table>
    </form>
</div>
<!--增加地域信息-->

<!--上一级地域信息开始-->
<div id="parent_areas">
    <div id="parent_areas_datagrid"></div>
</div>
<!--上一级地域信息结束-->
</body>
</html>
