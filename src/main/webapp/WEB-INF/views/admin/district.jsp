<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/11/23
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商圈管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
</head>
<body>
<!--商圈数据网格信息显示-->
<table id="district_grid"></table>
<!--商圈数据网格toolbar-->
<div id="grid_toolbar" class="toolbar-container">
    <a id="add_btn" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
    <a id="edit_btn" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
</div>
<!--商圈数据添加-->
<div id="add_win">
    <form id="add_form" action="${basePath}/admin/api/district/add" method="post">
        <table align="center">
            <tr>
                <td>商圈名：</td>
                <td><input class="easyui-textbox" data-options="required:true,width:250" name="districtName"></td>
            </tr>
            <tr>
                <td>商圈所属区域：</td>
                <td><input class="area_list" name="districtArea.areaId"></td>
            </tr>
            <tr>
                <td>商圈描述:</td>
                <td><input class="easyui-textbox" data-options="width:250,height:80,mutiline:true" name="districtDesc">
                </td>
            </tr>
        </table>
    </form>
</div>

<!--商圈数据修改-->
<div id="edit_win">
    <form id="edit_form" action="${basePath}/admin/api/district/edit" method="post">
        <table align="center">
            <input type="hidden" name="districtId">
            <tr>
                <td>商圈名：</td>
                <td><input class="easyui-textbox" data-options="required:true,width:250" name="districtName"></td>
            </tr>
            <tr>
                <td>商圈所属区域：</td>
                <td><input class="area_list" name="districtArea.areaId" data-options="required:true,width:250"></td>
            </tr>
            <tr>
                <td>商圈描述:</td>
                <td><input class="easyui-textbox" data-options="width:250,height:80,mutiline:true" name="districtDesc">
                </td>
            </tr>
        </table>
    </form>
</div>

<!--商圈地址显示-->
<div id="disp_area_list">
    <div id="area_datagrid"></div>
</div>


<script type="text/javascript">

    $(function () {
        /*========商圈信息显示========*/
        $('#district_grid').datagrid({
            url: '/admin/api/district/list',
            fit: true,
            fitColumns: true,
            pagination: true,
            pageSize: 20,
            singleSelect:true,
            columns: [[
                {field: 'districtId', title: '商圈ID', checkbox: true, chidden: true, width: 100},
                {field: 'districtName', title: '商圈名', width: 100},
                {
                    field: 'districtArea', title: '商圈所属区域', width: 100,
                    formatter: function (value) {
                        if (value) {
                            return value.areaName;
                        }
                    }

                },
                {field: 'districtDesc', title: '商圈描述', width: 100},
            ]],
            toolbar: '#grid_toolbar'
        });

        /*=========商圈信息网格toolbar按钮========*/
        $('#add_btn').click(function () {
            add();
        });

        $('#edit_btn').click(function () {
            edit();
        });
        /*========商圈所属区域========*/
        $('.area_list').combo({
            hasDownArrow: false,
            editable: false,
            required: true,
            width: 250,
            onShowPanel: function () {
                $(this).combo('hidePanel');
                $('#disp_area_list').dialog({
                    width: 600,
                    height: 350,
                    title: '商圈列表',
                    modal: true
                });
                $('#area_datagrid').datagrid({
                    url: '/admin/api/area/list',
                    columns: [[
                        {field: 'areaName', title: '区域名', width: 100, align: 'center'},
                        {field: 'fullName', title: '地区全名', width: 100, align: 'center'},
                        {
                            field: 'isRoot', title: '是否是根区域', width: 120, align: 'center', formatter: function (value) {
                            if (value) {
                                return '是根区域';
                            }
                            return '不是根区域';
                        }
                        }
                    ]],
                    fit: true,
                    fitColumns: true,
                    onClickRow: function (index, row) {
                        $('#disp_area_list').dialog('close');
                        $('.area_list').combo('setValue', row.areaId);
                        $('.area_list').combo('setText', row.areaName);
                    }
                })
            }
        });


    });

    /*========商圈添加========*/
    function add() {
        $('#add_win').dialog({
            title: '添加商圈',
            width: 500,
            iconCls: 'icon-add',
            modal: true,
            buttons: [{
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    $('#add_form').form('submit', {
                        success: function (data) {
                            data = $.parseJSON(data);
                            $.messager.show({
                                title: '提示',
                                msg: data.msg
                            });
                            if (data.success) {
                                $('#add_win').dialog('close');
                                $('#district_grid').datagrid('load');
                            }
                        }
                    })
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add_win').dialog('close');
                }
            }]
        });
        $('#add_form').form('reset');
    }

    /*========商圈信息修改========*/
    function edit() {
        var row = $('#district_grid').datagrid('getChecked');
        if (row.length < 1) {
            $.messager.alert('提示', '请选择需要操作的数据', 'error');
            return;
        }
        $('#edit_win').dialog({
            title: '修改选中的商圈信息',
            width: 500,
            iconCls: 'icon-add',
            modal: true,
            buttons: [{
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    $('#edit_form').form('submit', {
                        success: function (data) {
                            data = $.parseJSON(data);
                            $.messager.show({
                                title: '提示',
                                msg: data.msg
                            });
                            if (data.success) {
                                $('#edit_win').dialog('close');
                                $('#district_grid').datagrid('load');
                            }
                        }
                    })
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit_win').dialog('close');
                }
            }]
        });
        $('#edit_form').form('reset');
        $('#edit_form').form('load',row[0]);
        $('.area_list').combo('setValue',row[0].districtArea.areaId);
        $('.area_list').combo('setText', row[0].districtArea.areaName);
    }


</script>

</body>
</html>
