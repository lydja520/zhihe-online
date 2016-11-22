<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/11/18
  Time: 9:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>菜单管理</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            /*==========初始化数据网格==========*/
            $('#dg').datagrid({
                url: '/admin/api/menu/list',
                fitColumns: true,
                fit: true,
                pagination: true,
                loadMsg: '数据正在加载……',
                rownumbers: true,
                singleSelect: true,
                pageSize: 20,
                title: '菜单（权限管理）',
                onLoadError: function () {
                    $.messager.alert('错误信息', '数据加载失败', 'error');
                },
                columns: [[
                    {field: 'menuId', checkbox: true, align: 'center'},
                    {field: 'menuName', title: '菜单名', width: 50, align: 'center'},
                    {field: 'menuUrl', title: '菜单地址', width: 50, align: 'center'},
                    {field: 'menuOrder', title: '菜单顺序', width: 30, sortable: true, align: 'center'},
                    {field: 'menuDesc', title: '菜单描述', width: 50, align: 'center'},
                    {
                        field: 'isRoot', title: '是否是根菜单', width: 30, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return '<span style="color: red">是</span>';
                            }
                            return '不是';
                        }
                    },
                    {
                        field: 'parentMenu', title: '父级菜单', width: 50, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return value.menuName;
                            }
                        }
                    },
                    {
                        field: 'permit', title: '是否启用', width: 30, align: 'center', sortable: true,
                        formatter: function (value) {
                            if (value) {
                                return '已启用';
                            }
                            else {
                                return '<span style="color:red">已禁用</span>';
                            }
                        }
                    }
                ]],
                toolbar: '#menu-grid-toolbar'
            });

            /*========添加菜单========*/
            function add() {
                $('#menu_win').dialog({
                    title: '增加新的菜单',
                    iconCls: 'icon-add',
                    width: 400,
                    modal: true,
                    buttons: [{
                        text: '添加',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#menu_form').form('submit', {
                                url: '/admin/api/menu/add',
                                success: function (data) {
                                    data = $.parseJSON(data);
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    });
                                    if (data.success) {
                                        $('#menu_win').dialog('close');
                                        $('#dg').datagrid('load');
                                    }
                                }
                            })
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#menu_win').dialog('close');
                        }
                    }
                    ],
                    onBeforeOpen: function () {
                        $('#menu_form').form('reset');
                        $('#parentMenu').combobox({
                            url: '/admin/api/menu/rootList',
                            width: 200,
                            valueField: 'menuId',
                            textField: 'menuName',
                            onLoadSuccess: function () {
                                var values = $('#parentMenu').combobox('getData');
                                console.log(values);
                                if (values.length > 0) {
                                    $('#parentMenu').combobox('select', values[0].menuId);
                                }
                            }
                        });
                        disable($('#is_root input[type=radio]:checked').attr('value'));
                    }
                });
            }

            /*========修改菜单========*/
            function edit() {
                var rows = $('#dg').datagrid('getSelections');
                if (rows.length == 0) {
                    $.messager.alert('未选中行', '请选中一行进行修改');
                } else {
                    $('#menu_win').dialog({
                        modal: true,
                        title: '修改选中的菜单项',
                        width: 500,
                        iconCls: 'icon-edit',
                        buttons: [{
                            text: '修改',
                            iconCls: 'icon-ok',
                            handler: function () {
                                $('#menu_form').form('submit', {
                                    url: '/admin/api/menu/edit',
                                    success: function (data) {
                                        data = $.parseJSON(data);
                                        $.messager.show({
                                            title: '提示',
                                            msg: data.msg
                                        });
                                        if (data.success) {
                                            $('#menu_win').dialog('close');
                                            $('#dg').datagrid('load');
                                        }
                                    }
                                })
                            }
                        }, {
                            text: '取消',
                            iconCls: 'icon-cancel',
                            handler: function () {
                                $('#menu_win').dialog('close');
                            }
                        }],
                        onBeforeOpen: function () {
                            $('#menu_form').form('load', rows[0]);
                            $('#parentMenu').combobox({
                                url: '/admin/api/menu/rootList',
                                width: 200,
                                valueField: 'menuId',
                                textField: 'menuName',
                                onLoadSuccess: function () {
                                    if (rows[0].parentMenu != null) {
                                        $('#parentMenu').combobox('setValue', rows[0].parentMenu.menuId);
                                        $('#parentMenu').combobox('setText', rows[0].parentMenu.menuName);
                                    }
                                }
                            });
                            disable($('#is_root input[type=radio]:checked').attr('value'));
                        }
                    });
                }
            }

            $('#add-menu-btn').click(function () {
                add();
            });
            $('#edit-menu-btn').click(function () {
                edit();
            });
        });

        function disable(flag) {
            if (flag == 'true') {
                $('#animat').animate({'opacity': 0.7}, 300);
                $('#parentMenu').combobox('disable');
            } else {
                $('#animat').animate({'opacity': 1}, 300);
                $('#parentMenu').combobox('enable');
            }
        }

        function searchMenu(value, name) {
            console.log(value,name);
            var param = {};
            if (name && $.trim(value)) {
                param.searchName = name;
                param.searchValue = value;
            }
            $('#dg').datagrid('load', param);
        }
    </script>
</head>

<body>
<table id="dg"></table>
<div id="menu-grid-toolbar">
    <a id="add-menu-btn" class="easyui-linkbutton" iconCls="icon-add">添加</a>
    <a id="edit-menu-btn" class="easyui-linkbutton" iconCls="icon-edit">修改</a>
    <%--<a id="cancel-menu-btn" class="easyui-linkbutton" iconCls="icon-cancel">禁用</a>--%>
    <input class="easyui-searchbox" style="width: 300px;"
           data-options="prompt:'输入关键字搜索', menu:'#searchbox-menu',searcher:searchMenu">
</div>
<div id="searchbox-menu">
    <div name="menuName">菜单名称</div>
</div>
<div id="menu_win">
    <form id="menu_form" action="${pageContext.request.contextPath}/admin/api/menu/add" method="post"
          style="margin: 10px auto 10px auto">
        <input type="hidden" name="menuId"/>
        <table align="center" style="border-collapse: separate;border-spacing: 5px">
            <tr>
                <td><span>菜单名:</span></td>
                <td>
                    <input name="menuName" class="easyui-textbox" data-options="required:true" style="width:250px"/>
                </td>
            </tr>
            <tr>
                <td><span>菜单地址:</span></td>
                <td>
                    <input name="menuUrl" class="easyui-textbox" data-options="required:true" style="width:250px"/>
                </td>
            </tr>
            <tr>
                <td><span>菜单顺序:</span></td>
                <td>
                    <input name="menuOrder" class="easyui-numberbox easyui-numberspinner" data-options="required:true"
                           style="width:250px"/>
                </td>
            </tr>
            <tr>
                <td valign="top"><span>菜单描述:</span></td>
                <td><input name="menuDesc" class="easyui-textbox" data-options="multiline:true,height:50,width:250"/>
                </td>
            </tr>
            <tr>
                <td valign="top"><span>菜单图标:</span></td>
                <td>
                    <img id="imgPreview" style="width: 16px;height: 16px">
                    <br>
                    <input type="hidden" name="menuImg" id="imgValue">
                    <iframe src="${pageContext.request.contextPath}/admin/api/menu/img" scrolling="auto"
                            style="width: 240px;height: 90px;border: solid;border-width: 1px;border-color: #95b8e7;border-radius: 5px"></iframe>
                </td>
            </tr>
            <tr>
                <td><span>是否是根菜单:</span></td>
                <td id="is_root">
                    <input type="radio" name="isRoot" value="true" onclick="disable(this.value)">是
                    <input type="radio" name="isRoot" value="false" checked="checked" onclick="disable(this.value)">否
                </td>
            </tr>
            <tr id="animat">
                <td><span>选择根菜单:</span></td>
                <td>
                    <input id="parentMenu" name="parentMenu.menuId">
                </td>
            </tr>
            <tr>
                <td><span>是否启用:</span></td>
                <td id="is_permit">
                    <input type="radio" name="permit" value="true" checked>是
                    <input type="radio" name="permit" value="false">否
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
