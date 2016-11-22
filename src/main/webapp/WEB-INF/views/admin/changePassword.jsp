<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>个人密码修改</title>
    <%@include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        function onChangeSuccess(data) {
            data = $.parseJSON(data);
            $.messager.alert('提示', data.msg, 'info');
        }
    </script>
</head>
<e:body>
    <e:dialog id="data-panel" title="密码修改" iconCls="icon-tip" style="width:360px;height:220px;" resizable="false"
              draggable="false" modal="true" closable="false">
        <e:form id="data-form" url="${root}/admin/api/admin/changePwd" method="post">
            <e:eventListener event="success" listener="onChangeSuccess"/>
            <table class="form-table">
                <tr>
                    <td><label>原登录密码：</label></td>
                    <td>
                        <e:validateInput type="password" id="oldPwd" name="oldPwd" style="width:200px" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td><label>新密码：</label></td>
                    <td>
                        <e:validateInput type="password" id="newPwd" name="newPwd" style="width:200px" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td><label>重复新密码：</label></td>
                    <td>
                        <e:validateInput type="password" id="repeatPwd" style="width:200px" required="true"/>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="submit-btn" iconCls="icon-ok" text="提交">
                <e:event event="click" target="data-form" action="submit"/>
            </e:button>
        </e:facet>
    </e:dialog>
</e:body>
</html>
