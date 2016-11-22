<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>活动详情</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <table class="table table-striped">
            <c:set var="activity" value="${invitation.alliance.activity}"></c:set>
            <tr>
                <td>活动名称</td>
                <td>${activity.activitName}</td>
            </tr>
            <tr>
                <td>活动发起商</td>
                <td>${activity.activityPromoter.merchName}</td>
            </tr>
            <tr>
                <td>活动类型</td>
                <td>${activity.category.categName}</td>
            </tr>
            <tr>
                <td>开始时间</td>
                <td>
                    <f:formatDate value="${activity.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"></f:formatDate>
                </td>
            </tr>
            <tr>
                <td>结束时间</td>
                <td>
                    <f:formatDate value="${activity.endDate}" pattern="yyyy-MM-dd HH:mm:ss"></f:formatDate>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
