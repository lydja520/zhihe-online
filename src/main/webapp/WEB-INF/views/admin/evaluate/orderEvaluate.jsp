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
                href:'${basePath}/admin/orderEvaluateContent',
                title:'订单评价',
                fit: true,
                footer: '#pagination'
            });
        });
    </script>
    <style>
        body {
            margin: 0px;
            padding: 0px;
            font-family: "Microsoft Yahei";
        }

        ul {
            list-style-type: none;
            width: 90%;
            border-radius: 5px;
            padding: 0;
            margin: 10px auto 10px auto;
            border: solid thin #95b8e7
        }

        ul li {
            margin: 10px;
        }

        img {
            width: 50px;
            height: 50px;
            display: block;
            float: left;
        }

        .order-goods {

            margin: 5px 50px 5px 50px;
        }

        .order-goods-info {
            display: block;
            float: left;
            padding-top: 15px;
            margin-left: 10px;
        }

        .stress {
        }

        .stars {
            width: 17px;
            height: 17px;
            margin: 0px;
            padding: 0px
        }

        .clear {
            clear: both;
        }
    </style>
</head>
<body>
<div id="content"></div>
<div id="pagination" class="easyui-pagination" data-options="
    pageSize: 20,
    onSelectPage: function(pageNumber, pageSize){
    $('#content').panel('refresh', '/admin/orderEvaluateContent?page='+pageNumber+'&rows='+pageSize);
    }">
</div>
</body>
</html>
