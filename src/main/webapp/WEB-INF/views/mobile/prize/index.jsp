<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <title>我的奖品</title>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script src="${basePath}/static/core/utils.js"></script>
    <script type="text/javascript">
        var userId, userCode, token;
        function onBackUserId(_userId) {
            userId = _userId;
        }
        function onBackUserCode(_userCode) {
            userCode = _userCode;
        }
        function onBackUserToken(_userToken) {
            token = _userToken;
        }
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <ul class="list-group">
            <c:forEach items="${prizes}" var="drawDetail">
                <li class="list-group-item list-group-item-success">
                    <h4>
                            ${drawDetail.luckyDraw.title}
                    </h4>
                    <h4>
                        <small>验证码：${drawDetail.lukyDrawCode}</small>
                    </h4>
                    <p>使用说明：${drawDetail.luckyDraw.desc}</p>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
</body>
</html>
