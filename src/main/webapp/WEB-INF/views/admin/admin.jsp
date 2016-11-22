<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/11/23
  Time: 17:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>后台维护人员管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>

</head>
<body>
<!--显示数据网格数据开始-->
<table id="admin_grid"></table>
<!--显示数据网格数据结束-->

<!--增加数据-->
<div id="add_window" class="form-container">
    <form id="add_form" action="${pageContext.request.contextPath}/admin/api/admin/add" method="post">
        <table align="center">
            <tr>
                <td>
                    <label for="add_adminCode">登录账号：</label>
                </td>
                <td>
                    <input id="add_adminCode" name="adminCode" class="easyui-validatebox" data-options="required:true">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="add_adminPwd">登录密码(默认123456)：</label>
                </td>
                <td>
                    <input type="password" id="add_adminPwd" name="adminPwd" class="easyui-textbox"
                           data-options="required:true" value="123456">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="add_adminPermit">是否启用：</label>
                </td>
                <td>
                    <input id="add_adminPermit" name="permit" type="checkbox" value="true" checked="checked">
                </td>
            </tr>
            <tr>
                <td>
                    <label>角色(可多选)：</label>
                </td>
                <td>
                    <c:forEach items="${roleList}" var="role">
                        <input type="checkbox" name="roleIds" value="${role.roleId}">${role.roleName}
                    </c:forEach>
                </td>
            </tr>
            <%--<tr>
                <td>
                    <label for="add_adminIsAdmin">是否是管理员用户：</label>
                </td>
                <td>
                    <input id="add_adminIsAdmin" name="superAdmin" type="checkbox" value="true" checked="checked">
                </td>
            </tr>--%>
        </table>
    </form>
</div>
<!--增加数据-->

<!--编辑数据-->
<div id="edit_window" class="form-container">
    <form id="edit_form" action="${pageContext.request.contextPath}/admin/api/admin/edit" method="post">
        <table align="center">
            <tr>
                <td>
                </td>
                <td>
                    <input id="eidt_adminId" name="adminId" type="hidden">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="eidt_adminCode">登录账号：</label>
                </td>
                <td>
                    <input id="eidt_adminCode" name="adminCode" class="easyui-validatebox" data-options="required:true">
                </td>
            </tr>
            <input type="hidden" name="adminPwd">
            <tr>
                <td>
                    <label for="eidt_adminPermit">是否启用：</label>
                </td>
                <td>
                    <input id="eidt_adminPermit" name="permit" type="checkbox" value="true" checked="checked">
                </td>
            </tr>
            <tr>
                <td>
                    <label>角色(可多选)：</label>
                </td>
                <td>
                    <c:forEach items="${roleList}" var="role">
                        <input type="checkbox" name="roleIds" value="${role.roleId}">${role.roleName}
                    </c:forEach>
                </td>
            </tr>
            <tr>
               <%-- <td>
                    <label for="eidt_adminIsAdmin">是否是管理员用户：</label>
                </td>
                <td>
                    <input id="eidt_adminIsAdmin" name="superAdmin" type="checkbox" value="true">
                </td>--%>
            </tr>
        </table>
    </form>
</div>
<!--编辑数据-->

<!--禁用管理员-->
<div id="disable_window" align="center"><div style="margin-top: 30px; font-size: 16px; color: red">确定要禁用掉该用户吗!</div></div>

<!--重置管理员密码-->
<div id="reset_window" align="center"><div style="margin-top: 30px; font-size: 16px; color: red">确定要把该用户的密码重置为123456吗!</div></div>
<script type="text/javascript">
    /*============初始化数据网格==============*/
    $(function () {
        $('#admin_grid').datagrid({
            url: '/admin/api/admin/list',
            title:'管理员维护',
            singleSelect: true,
            columns: [[
                {field: 'adminId', checkbox: true},
                {field: 'adminCode', title: '账号', width: 100, align: 'center'},
                {
                    field: 'roles', title: '角色', width: 100, align: 'center', formatter: function (value) {
                    if (value && value.length > 0) {
                        var retVal = '';
                        for (var i = 0; i < value.length; i++) {
                            retVal += value[i].roleName + '、';
                        }
                        return retVal;
                    }
                    return value;
                }
                },
                {
                    field: 'permit',
                    title: '是否启用',
                    width: 100,
                    align: 'center',
                    sortable: true,
                    formatter: function (value) {
                        if (value) {
                            return '已启用';
                        }
                        else {
                            return '<span style="color:red">已禁用</span>';
                        }
                    }
                }, {
                    field: 'merchant',
                    title: '用户类型',
                    width: 100,
                    align: 'center',
                    formatter: function (value) {
                        if (value) {
                            return '商家';
                        }
                        else {
                            return '系统管理人员';
                        }
                    }
                }, {
                    field: 'superAdmin',
                    title: '是否是管理员',
                    width: 100,
                    align: 'center',
                    formatter: function (value) {
                        if (value) {
                            return '是';
                        }
                        {
                            return '不是';
                        }
                    }
                }
            ]],
            toolbar: [{
                text: '添加',
                iconCls: 'icon-add',
                handler: function () {
                    add();
                }
            }, '-', {
                text: '修改',
                iconCls: 'icon-edit',
                handler: function () {
                    edit();
                }
            }, '-', {
                text: '禁用',
                iconCls: 'icon-remove',
                handler: function () {
                    disable();
                }
            }, '-', {
                text: '重置密码',
                iconCls: 'icon-undo',
                handler: function () {
                    reset();
                }
            }],
            pageSize: 20,
            pagination: true,
            fit: true,
            fitColumns: true,
            onLoadError: function () {
                $.messager.alert('出错了', '数据加载失败，请重试', 'error');
            }
        });
    });

    /*============信息添加==============*/
    function add() {
        $('#add_window').dialog({
            width: 460,
            title: '添加管理员',
            iconCls: 'icon-add',
            modal: true,
            buttons: [{
                text: '添加',
                iconCls: 'icon-ok',
                handler: function () {
                    $('#add_form').form('submit', {
                        success: function (data) {
                            data = $.parseJSON(data);
                            $.messager.show({
                                title: '提示',
                                msg: data.msg
                            });
                            if (data.success) {
                                $('#add_window').dialog('close');
                                $('#admin_grid').datagrid('load');
                            }
                        }
                    });
                }
            }]
        });
        $('#add_form').form('reset');

    }

    /*============信息编辑==============*/
    function edit() {
        var rows = $('#admin_grid').datagrid('getSelections');
        if (rows.length < 1) {
            $.messager.alert('提示', '请选择需要操作的数据', 'error');
            return;
        }

        $('#edit_window').dialog({
            modal: true,
            title: '修改选中的用户',
            width: 400,
            buttons: [{
                text: '修改',
                iconCls: 'icon-ok',
                handler: function () {
                    $('#edit_form').form('submit', {
                        success: function (data) {
                            data = $.parseJSON(data);
                            $.messager.show({
                                title: '提示',
                                msg: data.msg
                            });
                            if (data.success) {
                                $('#edit_window').dialog('close');
                                $('#admin_grid').datagrid('load');
                            }
                        }
                    })
                }
            }],
            onBeforeOpen: function () {
                $('#edit_form').form('reset');
                $('#edit_form').form('load', rows[0]);
                var roles = $("#edit_form input[name=roleIds]");
                $.each(roles, function (index, item) {
                    var Id = item.value;
                    item.checked = '';
                    $.each(rows[0].roles, function (indexs, data) {
                        if (data.roleId == Id) {
                            item.checked = 'checked';
                        }
                    });
                })
            }
        });
    }

    /*============禁用选中的管理员==============*/
    function disable() {
        var rows = $('#admin_grid').datagrid('getSelections');
        console.log(rows[0]);
        if (rows.length == 0) {
            $.messager.alert('未选中行', '请选中一个账号进行禁用');
        } else if (rows.length == 1) {
            if (rows[0].permit) {
                $('#disable_window').dialog({
                    modal: true,
                    title: '禁用选中的账号',
                    iconCls: 'icon-remove',
                    width: 300,
                    height: 150,
                    buttons: [{
                        iconCls: 'icon-ok',
                        text: '确定禁用',
                        handler: function () {
                            $.ajax({
                                url: '/admin/api/admin/disableAdmin',
                                dataType: 'text',
                                type: 'post',
                                data: {
                                    adminId: rows[0].adminId,
                                    permit: false
                                },
                                success: function (data) {
                                    data = $.parseJSON(data);
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    });
                                    if (data.success) {
                                        $('#disable_window').dialog('close');
                                        $('#admin_grid').datagrid('load');
                                    }
                                }
                            });
                        }
                    }]
                });
            } else {
                $.messager.alert('提示', '你选中的用户已经被禁用！！');
            }
        } else {
            $.messager.alert('选中了多行', '你选中了多行，请选中一行进行修改');
        }
    }

    /*============密码重置为123456==============*/
    function reset() {
        var rows = $('#admin_grid').datagrid('getSelections');
        if (rows.length == 0) {
            $.messager.alert('未选中行', '请选中一个账号进行密码重置');
        } else if (rows.length == 1) {
            $('#reset_window').dialog({
                modal: true,
                title: '重置选中的密码',
                iconCls: 'icon-undo',
                width: 400,
                height: 150,
                buttons: [{
                    iconCls: 'icon-ok',
                    text: '确定重置',
                    handler: function () {
                        $.ajax({
                            url:'/admin/api/admin/resetPwd',
                            type:'post',
                            dataType:'text',
                            data:{
                                adminId: rows[0].adminId,
                                adminPwd:123456
                            },
                            success:function(data){
                                data = $.parseJSON(data);
                                $.messager.show({
                                    title: '提示',
                                    msg: data.msg
                                });
                                if(data.success){
                                    $('#reset_window').dialog('close');
                                    $('#admin_grid').datagrid('load');
                                }
                            }
                        });
                    }
                }]
            });
        }
    }
</script>
</body>
</html>
