<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>活动联盟商家</title>
    <%@include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">

        var currentActivitId = '${currentActivity.activitId}';

        function getActivityInfo(value) {
            if (value) {
                return value.activitName;
            }
        }
        function getMerchantName(value, row) {
            if (row.merchant) {
                return row.merchant.merchName;
            }
        }
        function getMerchantMobileNum(value, row) {
            if (row.merchant) {
                return row.merchant.contactMobileNO;
            }
        }
        function getPromoted(value) {
            if (value) {
                return '<span style="color:red;">活动发起人</span>';
            }
            return '普通成员';
        }
        function getAgreedState(value) {
            if (value == 2) {
                return '确定参加';
            } else if (value == 1) {
                return '已发邀请，待确认';
            } else if (value == 3) {
                return '<span style="color:red;">拒绝参加</span>';
            } else if (value == 4) {
                return '已提交';
            }

        }
        /*从商家联盟中删除某个商家*/
        function removeMerch() {
            var rows = $('#alliance-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            if (rows[0].promoted) {
                $.messager.alert('提示', '不能删除发起活动的商家', 'info');
                return;
            }
            $.messager.confirm('警告', '确定要从次活动中删除此商家吗？', function (flag) {
                if (flag) {
                    removeAllianceMerch(rows[0].allianceId)
                }
            })
        }

        function removeAllianceMerch(allianceId) {

            var _tmp = {};
            _tmp.allianceId = allianceId;
            $.messager.progress();
            request({
                data: _tmp,
                url: '${root}/admin/api/merchAlliance/delete',
                success: function (data) {
                    $.messager.progress('close');
                    if (data.success) {
                        $('#alliance-grid').datagrid('load');
                        $.messager.show({
                            title: '提示',
                            msg: data.msg
                        });
                    }
                    else {
                        $.messager.alert('提示', data.msg, 'error');
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert('提示', '系统错误请重试或联系管理员', 'error');
                }
            });
        }

        /*搜索商家*/
        function searchMerch(value, name) {
            var param = $('#merch-grid').datagrid('options').queryParams;
            if (value) {
                param.searchName = name;
                param.searchValue = value;
            }
            else {
                param.searchName = '';
                param.searchValue = '';
            }
            $('#merch-grid').datagrid('load', param);
        }
        /*将选中的商家加入到指定的活动*/
        function addMerchToAcitivity() {
            var rows = $('#merch-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            var merchId = rows[0].merchantId;
            var _tmp = {};
            _tmp['merchant.merchantId'] = merchId;
            _tmp['activity.activitId'] = currentActivitId;
            $.messager.progress();
            request({
                data: _tmp,
                url: '${root}/admin/api/activityInvitation/add',
                success: function (data) {
                    $.messager.progress('close');
                    if (data.success) {
                        $('#merch-grid').datagrid('load');
                        $.messager.show({
                            title: '提示',
                            msg: data.msg
                        });
                    }
                    else {
                        $.messager.alert('提示', data.msg, 'error');
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert('提示', '系统错误请重试或联系管理员', 'error');
                }
            });
        }
        /*当商家列表对话框打开时，重新加载商家数据网格*/
        function reloadMerchGrid() {
            $('#grid-searchbox').searchbox('clear');
            searchMerch();
        }
    </script>
</head>
<e:body>
    <e:datagrid id="alliance-grid" url="${root}/admin/api/activity/merchAlliance/list" singleSelect="true" fit="true"
                pagination="true" fitColumns="true" rownumbers="true" border="false" striped="true"
                queryParams="${currentActivity}">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="allianceId" checkbox="true"/>
            <e:column field="activity" title="活动名称" align="center" width="15" formatter="getActivityInfo"/>
            <e:column field="merchantName" title="商家名称" align="center" width="20" formatter="getMerchantName"/>
            <e:column field="merchantNO" title="联系电话" align="center" width="20" formatter="getMerchantMobileNum"/>
            <%--<e:column field="agreed" title="是否参加" align="center" width="25"/>--%>
            <e:column field="promoted" title="成员类型" align="center" width="15" formatter="getPromoted"/>
            <e:column field="allianceState" title="状态" align="center" width="25" formatter="getAgreedState"
                      sortable="true"/>
            <e:column field="createDate" title="加入时间" align="center" width="25" sortable="true"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-btn" iconCls="icon-add" text="邀请商家" plain="true">
                <e:event event="click" target="data-dialog" action="open"/>
            </e:button>
            <e:button id="remove-btn" iconCls="icon-remove" text="取消邀请" plain="true">
                <e:eventListener listener="removeMerch" event="click"/>
            </e:button>
        </e:facet>
    </e:datagrid>
    <e:dialog id="data-dialog" style="width:800px;height:400px;" iconCls="icon-tip" title="商家列表" closed="true"
              modal="true">
        <e:event event="onBeforeClose" target="alliance-grid" action="load"/>
        <e:eventListener event="onBeforeOpen" listener="reloadMerchGrid"/>
        <e:datagrid id="merch-grid" url="${root}/admin/api/merchAlliance/ableList" fit="true" fitColumns="true"
                    border="false" pagination="true" rownumbers="true" striped="true" queryParams="${currentActivity}"
                    pageSize="20" singleSelect="true">
            <e:eventListener event="onLoadError" listener="onGridLoadError"/>
            <e:columns>
                <e:column field="merchId" checkbox="true"/>
                <e:column field="merchName" title="商家名称" align="center" width="20"/>
                <e:column field="businScope" title="经营范围" align="center" width="40"/>
                <%--<e:column field="merchTell" title="联系电话" align="center" width="20"/>--%>
                <e:column field="contactMobileNO" title="联系人手机号" align="center" width="20"/>
                <e:column field="createDate" title="入驻时间" align="center" width="20"/>
                <e:column field="address" title="详细地址" align="center" width="40"/>
            </e:columns>
            <e:facet name="toolbar">
                <e:button id="apply-btn" iconCls="icon-ok" text="发送邀请" plain="false">
                    <e:eventListener event="click" listener="addMerchToAcitivity"/>
                </e:button>
                <e:inputSearch id="grid-searchbox" style="width:260px" prompt="输入关键字搜索">
                    <e:eventListener event="searcher" listener="searchMerch"/>
                    <e:facet name="menu">
                        <div name="merchName">商家名称</div>
                    </e:facet>
                </e:inputSearch>
            </e:facet>
        </e:datagrid>
    </e:dialog>
</e:body>
</html>
