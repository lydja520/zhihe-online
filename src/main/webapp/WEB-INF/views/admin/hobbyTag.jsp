<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>喜好标签维护</title>
    <%@ include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        function formatParentTag(value) {
            if (value) {
                return value.tagName;
            }
        }
        function onSuccessAdd(data) {
            onAfterEdit(data, 'hobbytag-grid', function () {
                $('#add-dialog').dialog('close');
            });
        }
    </script>
</head>
<e:body>
    <e:datagrid id="hobbytag-grid" fit="true" fitColumns="true" singleSelect="true" striped="true"
                url="${root}/admin/api/hobbyTags" pagination="true">
        <e:columns>
            <e:column field="tagId" checkbox="true"/>
            <e:column field="tagName" title="标签名称" align="center" width="15"/>
            <e:column field="parentTag" title="父标签" align="center" width="15" formatter="formatParentTag"/>
            <e:column field="tagDesc" title="描述" align="center" width="20"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-btn" iconCls="icon-add" text="添加标签">
                <e:event event="click" target="add-dialog" action="open"/>
            </e:button>
            <%--<e:button id="add-edit" iconCls="icon-edit" text="标签信息修改"></e:button>--%>
        </e:facet>
    </e:datagrid>
    <e:dialog id="add-dialog" iconCls="icon-add" title="添加喜好标签" closable="true" closed="true" modal="true"
              style="width:450px;height:260px;">
        <e:event event="onBeforeClose" target="add-form" action="reset"/>
        <e:event event="onBeforeOpen" target="add-parent-tag" action="reload"/>
        <e:form id="add-form" method="post" url="${root}/admin/api/hobbyTag/add">
            <e:eventListener event="success" listener="onSuccessAdd"/>
            <table class="form-table">
                <tr>
                    <td><label>标签名称：</label></td>
                    <td>
                        <e:inputText id="add-tag-name" name="tagName" style="width:150px;" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td><label>父标签：</label></td>
                    <td>
                        <e:comboBox id="add-parent-tag" name="parentTag.tagId" url="${root}/admin/api/hobbyTag/rootTags" style="width:150px"
                                    editable="false" textField="tagName" valueField="tagId"/>
                    </td>
                </tr>
                <tr>
                    <td><label>标签描述：</label></td>
                    <td>
                        <textarea cols="40" rows="6" name="tagDesc"></textarea>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="add-submit" iconCls="icon-ok" text="保存">
                <e:event event="click" target="add-form" action="submit"/>
            </e:button>
            <e:button id="add-cancel" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="add-dialog" action="close"/>
            </e:button>
        </e:facet>
    </e:dialog>
</e:body>
</html>
