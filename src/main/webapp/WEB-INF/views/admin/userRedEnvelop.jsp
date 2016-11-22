<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/6/16
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/4/19
  Time: 15:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<title>用户统计</title>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
<script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
<script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
<script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
<style>
    table {
        border: solid #add9c0;
        border-width: 1px;
    }

    td {
        width: auto;
        border-collapse: collapse;
        border: solid #add9c0;
        border-width: 0px 1px 1px 0px
    }

    table tr td {
        text-align: center;
        font-size: 12px;
        height: 26px;
    }

    .title {
        color: #67b168;
        font-size: 14px;
        font-weight: bold;
        background-color: #edf4ff;
    }
</style>
<script type="text/javascript">

</script>
</head>

<body>
<table align="center" cellspacing="0px" cellpadding="0px" width="100%">
    <tr class="title">
        <td colspan="8">当前红包统计</td>
    </tr>
    <tr class="title">
        <td>红包数量</td>
        <td>红包金额</td>
        <td>已领取个数</td>
        <td>已领取金额</td>
        <td>剩余红包个数</td>
        <td>剩余红包金额</td>
    </tr>
    <tr>
        <td>${requestScope.currentRedEnvelop.numbers}个</td>
        <td>${requestScope.currentRedEnvelop.totalMoney}元</td>
        <td>${requestScope.receivedTotal}个</td>
        <td>${requestScope.receivedTotalMoney}元</td>
        <td>${requestScope.currentRedEnvelop.numbers-requestScope.receivedTotal}个</td>
        <td>${requestScope.surplusMoney}元</td>
    </tr>
</table>
<table align="center" cellspacing="0px" cellpadding="0px" width="100%">
    <tr class="title">
        <td colspan="8">抢红包详情</td>
    </tr>
    <tr class="title">
        <td class="phoneNumber">用户电话</td>
        <td>用户名</td>
        <td>抢到红包金额</td>
        <td>红包领取时间</td>
        <td>是否提现</td>
    </tr>
    <c:forEach items="${requestScope.rows}" var="rows">
        <tr>
            <td>
                    ${fn:substring(rows.user.userPhone, 0, 3)}****${fn:substring(rows.user.userPhone, 7,-1 )}<br>
            </td>
            <td>${rows.user.userName}</td>
            <td>${rows.amountOfMoney}</td>
            <td>${rows.receivedDate}</td>
            <td>
                <c:choose>
                    <c:when test="${rows.extractState==true}">
                        已提现
                    </c:when>
                    <c:when test="${rows.extractState==false}">
                        未提现
                    </c:when>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

