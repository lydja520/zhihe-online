<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-4-28
  Time: 下午1:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>抽奖测试</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript">
        var i = 1;
        $(function () {
            req();

            function req() {
                $.ajax({
                    url: '${basePath}/user/078ef163-33fc-4b9f-ab05-1d7948ba77f5/luckDraw',
                    type: 'get',
                    dataType: 'text',
                    success: function (info) {
                        info = $.parseJSON(info);
                        console.log("====================第" + i + "个人抽奖====================")
                        console.log(info.msg)
                        if (info.code == 200) {
                            console.log("你中了" + info.attribute.luckyDraw.grade + "等奖");
                        }
                    },
                    complete: function () {
                        req();
                    }
                });
                i++;
            }
        });
    </script>
</head>
<body>

</body>
</html>
