<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/4/7
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <style>
        body {
            margin: 0px;
            padding: 0px;
        }

    </style>
</head>
<body>
<div style="width: 90%;margin: auto">
    <div style="width:100%;margin: 10px auto 10px auto">
        <img src="${goods.coverImg.url}" style="width: 80px;height: 80px;display: block;float: left">

        <div style="height: 80px;padding-left: 20px;float:left;">
            <ul style="margin: 0px;padding: 0px">
                <li>商品名：${goods.goodsName}</li>
                <li>商品价格：${goods.price}元</li>
                <li>商品库存量：${goods.currentStock}件</li>
                <li>商品销量：${goods.volume}件</li>
            </ul>

        </div>
        <div style="clear: both"></div>
    </div>
    <c:if test="${goodsScores.size() == 0}">
        <div style="text-align:center;border-top: solid thin #95b8e7;padding-top: 5px">sorry！该商品没有任何评价</div>
    </c:if>
    <c:forEach items="${goodsScores}" var="goodsScore">
        <div style="border-top: solid thin #95b8e7;margin: 5px auto 5px auto">
            <div style="background-color:#e6efff;height:20px;line-height:20px; ">
                <span>用户名:${goodsScore.user.userName}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span>用户电话:${goodsScore.user.userPhone}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span>评价时间:${goodsScore.createDate}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </div>
            <div class="left" style="width: 80px;float: left;border-right: solid thin #e6efff">
                <img src="${goodsScore.user.portrait.url}"
                     style="width: 50px;height: 50px;display: block;margin:5px auto 5px auto;border-radius: 25px ">

                <div style="width:100%;text-align: center">${goodsScore.user.userName}</div>
            </div>
            <div class="right" style="float: left;padding-left: 20px">
                <p>用户评价：${goodsScore.evaluate}</p>

                <div>
                    <span style="float: left">用户评分: </span>
                    <c:set var="integer" value="${0}"></c:set>
                    <c:forEach var="x" begin="1" end="${goodsScore.score}" step="1">
                        <img class="stars" style="width: 16px;height: 16px;float: left"
                             src="${basePath}/static/images/stars.png">
                        <c:set var="integer" value="${x}"></c:set>
                    </c:forEach>
                    <c:if test="${goodsScore.score - integer == 0.5 }">
                        <img class="stars" style="width: 16px;height: 16px;float: left"
                             src="${basePath}/static/images/stars-half.png">
                        <c:set var="integer" value="${integer + 1}"></c:set>
                    </c:if>
                    <c:forEach var="y" begin="1" end="${5 - integer}" step="1">
                        <img class="stars" style="width: 16px;height: 16px;float: left;"
                             src="${basePath}/static/images/stars-dark.png">
                    </c:forEach>
                    <span style="float: left">（${goodsScore.score}分）</span>
                </div>
            </div>
            <div class="clear"></div>
        </div>
        <div style="clear: both"></div>
    </c:forEach>
</div>

</body>
</html>
