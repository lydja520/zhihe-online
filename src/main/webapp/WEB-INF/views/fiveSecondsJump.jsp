<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/3/21
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>跳到登录页</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript">
        function countDown(secs, surl) {
            var jumpTo = document.getElementById('jumpTo');
            jumpTo.innerHTML = secs;
            if (--secs > 0) {
                setTimeout("countDown(" + secs + ",'" + surl + "')", 1000);
            }
            else {
                location.href = surl;
            }
        }
    </script>
</head>
<body>
<a href="${basePath}/admin/login" style="display:block;margin: 30px">恭喜你！注册成功！你可以使用该账号进行登录，页面将在 <span id="jumpTo">5</span> 秒后跳转，或者点击进行跳转……</a>
<script type="text/javascript">countDown(10, '${basePath}/admin/login');</script>
</body>
</html>
