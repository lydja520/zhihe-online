<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/5/18
  Time: 9:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家未自动生成的账单</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/merchantBillErrRd/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                columns: [[
                    {field: 'errId', checkbox: true, width: 100, align: 'center'},
                    {
                        field: 'merchant', title: '对应的商家', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            return value.merchName;
                        }
                    },
                    {field: 'createDate', title: '生成时间', width: 100, align: 'center'},
                    {field: 'startDate', title: '账单开始时间', width: 100, align: 'center'},
                    {field: 'endDate', title: '账单结束时间', width: 100, align: 'center'},
                    {field: 'errMsg', title: '未生成的原因', width: 100, align: 'center'},
                    {
                        field: 'handleState', title: '是否已经处理', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            if (value) {
                                return "<span style='color: green'>是</span>";
                            }
                            return "<span style='color: red'>否</span>";
                        }
                    }
                ]]
            });
        });
    </script>
</head>
<body>
<div id="dg"></div>

</body>
</html>
