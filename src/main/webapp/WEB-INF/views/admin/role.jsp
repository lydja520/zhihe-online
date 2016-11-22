<%@ page import="cn.com.zhihetech.online.controller.SupportController" %>
<%@ page import="cn.com.zhihetech.online.bean.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>角色管理</title>
    <%@ include file="/WEB-INF/views/commons/header.jsp"%>
    <script type="text/javascript">
        function crateMenus(menus) {
            if (menus && menus.length > 0) {
                var retVal = '';
                $.each(menus, function (index, menu) {
                    retVal += menu.menuName + ",";
                });
                return retVal;
            }
            return retVal;
        }

        function addRole() {
            $('#data-dialog').dialog('open');
            $('#data-form').form('clear');
        }

        function editRole() {
            var rows = $('#role-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要编辑的数据');
                return;
            }
            $('#data-form').form('reset');
            $('#data-dialog').dialog('open');
            $('#data-form').form('load', rows[0]);
            var row = rows[0];
            var menuIds = $('#menus input[type=checkbox]');
            $.each(menuIds, function (index, item) {
                $(item).prop('checked', false);
                $.each(row.menus, function (i, menu) {
                    if ($(item).val() == menu.menuId) {
                        $(item).prop('checked', true);
                    }
                });
            });
        }

        function onSubmitSuccess(data) {
            onAfterEdit(data, 'role-grid', function () {
                $('#data-dialog').dialog('close');
            })
        }
    </script>
</head>
<e:body>
    <e:datagrid id="role-grid" fit="true" fitColumns="true" border="true" url="${root}/admin/api/role/list"
                pagination="true" rownumbers="true" singleSelect="true" title="角色管理">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="roleId" checkbox="true"/>
            <e:column field="roleName" title="角色名称" width="20" align="center"/>
            <e:column field="roleDesc" title="角色描述" width="30" align="center"/>
            <e:column field="menus" title="拥有权限" width="30" align="center" formatter="crateMenus"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-btn" iconCls="icon-add" text="添加" plain="true">
                <e:eventListener event="click" listener="addRole"/>
            </e:button>
            <e:button id="edit-btn" iconCls="icon-edit" text="编辑" plain="true">
                <e:eventListener event="click" listener="editRole"/>
            </e:button>
        </e:facet>
    </e:datagrid>

    <e:dialog id="data-dialog" closable="true" closed="true" style="width:350px;height:260px;" modal="true"
              iconCls="icon-save" title="编辑数据">
        <e:form id="data-form" method="post" url="${root}/admin/api/role/saveOrUpdate">
            <e:eventListener event="success" listener="onSubmitSuccess"/>
            <input type="hidden" name="roleId"/>

            <div>
                <label>角色名称:</label>
                <e:validateInput id="roleName" name="roleName" required="true"/>
            </div>
            <div style="margin-top: 10px">
                <label>权限分配:</label>

                <div id="menus">
                    <c:forEach items="${menuList}" var="menu" varStatus="status">
                        <c:if test="${status.index % 3 == 0 && status.index != 0}">
                            <e:separator/><br>
                        </c:if>
                        <input type="checkbox" name="menuIds" checked="" value="${menu.menuId}">${menu.menuName}
                    </c:forEach>
                </div
            </div>
            <div style="margin-top: 10px">
                <label>关于:&nbsp;&nbsp;</label>
                <textarea name="roleDesc" rows="3" cols="25"></textarea>
            </div>
        </e:form>
        <e:facet name="buttons">
            <e:button id="cancel-btn" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="data-dialog" action="close"/>
            </e:button>
            <e:button id="submit-btn" iconCls="icon-ok" text="提交">
                <e:event event="click" target="data-form" action="submit"/>
            </e:button>
        </e:facet>
    </e:dialog>
</e:body>
</html>
