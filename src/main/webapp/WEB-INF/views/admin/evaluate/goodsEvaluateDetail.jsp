<%@ page import="cn.com.zhihetech.online.vo.OrderAndOrderEvaluate" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/1/5
  Time: 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>已完成</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#content').panel({
                href: '${basePath}/admin/goods/${goodsId}/evaluateDetailContent',
                fit: true,
                border:false,
                footer: '#pagination'
            });
        });
    </script>
</head>
<body>
<div id="content"></div>
<div id="pagination" class="easyui-pagination" data-options="
    pageSize: 20,
    onSelectPage: function(pageNumber, pageSize){
    $('#content').panel('refresh', '/admin/goods/${goodsId}/evaluateDetailContent?page='+pageNumber+'&rows='+pageSize);
    }">
</div>
</body>
</html>
