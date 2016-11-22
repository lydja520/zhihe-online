<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/9
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script>
    </script>
    <style>
        table {
            border: solid #add9c0;
            border-width: 1px;
        }

        table img {
            width: 700px;
        }

        td {
            width: 15%;
            border-collapse: collapse;
            border: solid #add9c0;
            border-width: 0px 1px 1px 0px
        }

        table tr td {
            text-align: center;
            font-size: 12px;
            height: 26px;
        }

        .info_title {
            background-color: #67b168;
            color: #FFFFFF;
            font-size: 15px;
            font-weight: bold;
        }

        .title {
            color: #67b168;
            font-size: 14px;
            font-weight: bold;
            background-color: #edf4ff;
        }


    </style>
</head>
<body>
<table align="center" cellspacing="0px" cellpadding="0px">
    <tr>
        <td colspan="8" class="info_title">用户基本信息</td>
    </tr>
    <tr>
        <td>姓名</td>
        <td>性别</td>
        <td>年龄</td>
        <td>生日</td>
        <td>商圈</td>
        <td>职业</td>
        <td>收入</td>
        <td>联系电话</td>
    </tr>
    <c:forEach items="${user}" var="user">
        <td>${user.userName}</td>
        <td><c:choose><c:when test="${user.sex==true}">男</c:when><c:otherwise>女</c:otherwise></c:choose></td>
        <td>${user.age}</td>
        <td>${user.birthday}</td>
        <td>${user.area.areaName}</td>
        <td>${user.occupation.occName}</td>
        <td>${user.income.incomMin}——${user.income.incomMax}</td>
        <td>${user.userPhone}</td>
    </c:forEach>


</table>
</body>
</html>
