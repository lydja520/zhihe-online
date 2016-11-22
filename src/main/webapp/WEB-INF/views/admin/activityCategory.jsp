<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>活动类别维护</title>
    <%@ include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        function getCategoryFlag(value) {
            if (value) {
                return '官方活动';
            }
            return '商家活动';
        }
        function getType(value) {
            switch (value) {
                case 1 :
                    return '促销';
                    break;
                default :
                    break;
            }
        }
        function editData() {
            var row = $('#category-grid').datagrid('getChecked');
            if (!row || row.length < 1) {
                $.messager.alert('提示', '请选择需要修改的数据', 'error');
                return;
            }
            $('#data-form').form('load', row[0]);
            $('#data-dialog').dialog('open');
        }

        function onSubmitSuccess(data) {
            onAfterEdit(data, 'category-grid', function () {
                $('#data-dialog').dialog('close');
            })
        }
    </script>
</head>
<e:body>
    <e:datagrid id="category-grid" fit="true" fitColumns="true" url="${root}/admin/api/activityCategory/list"
                pagination="true" singleSelect="true">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="categId" checkbox="true"/>
            <e:column field="categName" title="活动类别名称" align="center" width="20"/>
            <e:column field="official" title="类别标示" align="center" width="20" formatter="getCategoryFlag"/>
            <e:column field="categType" title="活动类别" align="center" width="20" formatter="getType"/>
            <e:column field="createDate" title="创建时间" align="center" width="25" sortable="true"/>
            <e:column field="categDesc" title="活动描述" align="center" width="40"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-btn" iconCls="icon-add" text="添加类别" plain="true">
                <e:event event="click" target="data-dialog" action="open"/>
            </e:button>
            <e:button id="edit-btn" iconCls="icon-edit" text="修改信息" plain="true">
                <e:eventListener event="click" listener="editData"/>
            </e:button>
            <%--<e:inputSearch id="grid-searchbox" style="width:260px;" prompt="输入关键字搜索">
                <e:eventListener event="searcher" listener="doSearcher"/>
                <e:facet name="menu">
                    <div name="categName">类别名称</div>
                </e:facet>
            </e:inputSearch>--%>
        </e:facet>
    </e:datagrid>

    <e:dialog id="data-dialog" title="数据编辑" iconCls="icon-tip" closed="true" modal="true"
              style="width:450px;height:260px;">
        <e:event event="onBeforeClose" target="data-form" action="reset"/>
        <e:form id="data-form" method="post" url="${root}/admin/api/activityCategory/saveOrUpdate">
            <e:eventListener event="success" listener="onSubmitSuccess"/>
            <input type="hidden" name="categId">
            <table class="form-table">
                <tr>
                    <td><label>类别名称：</label></td>
                    <td>
                        <e:validateInput id="categName" name="categName" required="true"/>
                    </td>
                    <td><label>是否官方活动：</label></td>
                    <td>
                        <input name="official" type="checkbox" value="true">
                    </td>
                </tr>
                <tr>
                    <td><label>活动类别：</label></td>
                    <td>
                        <select class="easyui-combobox" name="categType" data-options="editable:false"
                                style="width:140px;">
                            <option value="1">促销活动</option>
                            <option value="2">清仓大促</option>
                            <option value="3">抢红包</option>
                            <option value="1">一元夺宝</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label>活动描述</label></td>
                    <td colspan="3">
                        <textarea cols="45" name="categDesc" rows="4"></textarea>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="submit-btn" iconCls="icon-ok" text="提交">
                <e:event event="click" target="data-form" action="submit"/>
            </e:button>
            <e:button id="cancel-btn" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="data-dialog" action="close"/>
            </e:button>
        </e:facet>
    </e:dialog>
</e:body>
</html>
