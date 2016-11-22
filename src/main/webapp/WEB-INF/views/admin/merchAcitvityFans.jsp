<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>参加活动的会员</title>
    <%@ include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        function getUserName(value, row) {
            return row.fans.userName;
        }
        function getUserInfo(value) {
            if (value) {
                return value.userName;
            }
        }
        function getUserAge(value, row) {
            return row.fans.age;
        }
        function getUserOccName(value, row) {
            return row.fans.occupation;
        }
        function getUserIncome(value, row) {
            return row.fans.income;
        }
        function getUserSex(value, row) {
            if (row.fans) {
                return row.fans.sex ? '男' : '女';
            }
            return value ? '男' : '女';
        }
        function getUserArea(value, row) {
            if (row.fans) {
                return row.fans.area ? row.fans.area.fullName : "";
            }
            return value ? value.fullName : "";
        }
        function getUserOccupation(value, row) {
            if (row.fans) {
                return row.fans.occName;
            }
        }
        function applyUser() {
            var row = getGridChecked('#user-grid');
            if (!row) {
                return;
            }
            var param = {};
            param['fans.userId'] = row.userId;
            param['invitationMerch.merchantId'] = '${merchant.merchantId}';
            param['activity.activitId'] = '${activityId}';
            $.messager.progress();
            request({
                url: '${root}/admin/merchActivity/fans/add',
                data: param,
                success: function (result) {
                    $.messager.progress('close');
                    if (result.success) {
                        $.messager.show({
                            title: '提示',
                            msg: result.msg
                        });
                        $('#user-grid').datagrid('load');
                    }
                    else {
                        $.messager.alert('提示', result.msg, 'info');
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert('提示', '系统错误请刷新后重试或联系管理员！', 'error');
                }
            })
        }
        function removeMember() {
            var row = getGridChecked('#fans-grid');
            if (!row) {
                return;
            }
            $.messager.confirm('警告', '确定要进行此操作吗，此操作不可恢复？', function (r) {
                if (r) {
                    $.messager.progress();
                    request({
                        url: '${root}/admin/merchActivity/fans/delete',
                        data: {
                            fansId: row.fansId
                        },
                        success: function (result) {
                            $.messager.progress('close');
                            if (result.success) {
                                $.messager.show({
                                    title: '提示',
                                    msg: result.msg
                                });
                                $('#fans-grid').datagrid('load');
                            }
                            else {
                                $.messager.alert('提示', result.msg, 'info');
                            }
                        },
                        error: function () {
                            $.messager.progress('close');
                            $.messager.alert('提示', '系统错误请刷新后重试或联系管理员！', 'error');
                        }
                    })
                }
            });
        }
        function getGridChecked(data_grid) {
            var rows = $(data_grid).datagrid('getChecked');
            if (!rows || rows.length != 1) {
                $.messager.alert('提示', '请选择需要操作的数据！', 'info');
                return undefined;
            }
            return rows[0];
        }
        function reloadUserGrid() {
            $('#user-searchbox').searchbox('clear');
            searchUser(undefined, undefined);
        }
        function searchUser(value, name) {
            var param = {};
            if (value) {
                param.searchName = name;
                param.searchValue = value;
            }
            $('#user-grid').datagrid('load', param);
        }
    </script>
</head>
<e:body>
    <e:datagrid id="fans-grid" url="${root}/admin/merchActivity/${activity.activitId}/fans/list" singleSelect="true"
                fit="true" pagination="true" fitColumns="true" rownumbers="true" border="false" striped="true">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="fansId" checkbox="true"/>
            <e:column field="user" title="会员名称" align="center" width="20" formatter="getUserName"/>
            <e:column field="sex" title="会员性别" align="center" width="20" formatter="getUserSex"/>
            <e:column field="age" title="会员年龄" align="center" width="20" formatter="getUserAge"/>
            <e:column field="occupation" title="职业" align="center" width="20" formatter="getUserOccName"/>
            <e:column field="income" title="收入" align="center" width="20" formatter="getUserIncome"/>
            <e:column field="area" title="区域" align="center" width="40" formatter="getUserArea"/>
            <e:column field="joinDate" title="邀请时间" align="center" width="20" sortable="true"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-member" iconCls="icon-add" text="邀请会员" plain="true">
                <e:event event="click" target="data-dialog" action="open"/>
            </e:button>
            <e:button id="remove-member" iconCls="icon-remove" text="删除邀请" plain="true">
                <e:eventListener event="click" listener="removeMember"/>
            </e:button>
        </e:facet>
    </e:datagrid>

    <!--邀请会员开始-->
    <e:dialog id="data-dialog" iconCls="icon-add" title="邀请会员" style="width:700px;height:450px;" closable="true"
              modal="true" closed="true">
        <e:event event="onBeforeClose" target="fans-grid" action="load"/>
        <e:eventListener event="onBeforeOpen" listener="reloadUserGrid"/>
        <e:datagrid id="user-grid" url="${root}/admin/merchActivity/${activity.activitId}/ableUsers" singleSelect="true"
                    fit="true" pagination="true" fitColumns="true" rownumbers="true" border="false" striped="true"
                    pageSize="20">
            <e:columns>
                <e:column field="userId" checkbox="true"/>
                <e:column field="userName" title="会员名称" align="center" width="20"/>
                <e:column field="sex" title="会员性别" align="center" width="20" formatter="getUserSex"/>
                <e:column field="age" title="会员年龄" align="center" width="20"/>
                <e:column field="area" title="区域" align="center" width="40" formatter="getUserArea"/>
                <e:column field="occupation" title="职业" align="center" width="20"/>
                <e:column field="income" title="收入" align="center" width="20"/>
            </e:columns>
            <e:facet name="toolbar">
                <e:button id="add-user-btn" iconCls="icon-add" text="邀请" plain="true">
                    <e:eventListener event="click" listener="applyUser"/>
                </e:button>
                <%--<e:button id="remove-user-btn" iconCls="icon-remove" text="取消邀请" plain="true">
                    <e:eventListener event="click" listener="removeUser"/>
                </e:button>--%>
                <e:inputSearch id="user-searchbox" style="width:240px" prompt="输入关键字搜索">
                    <e:eventListener listener="searchUser" event="searcher"/>
                    <e:facet name="menu">
                        <div name="userName">会员名称</div>
                    </e:facet>
                </e:inputSearch>
            </e:facet>
        </e:datagrid>
    </e:dialog>
    <!--邀请会员结束-->

</e:body>
</html>
