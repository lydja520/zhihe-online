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
            color: green;
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
<div id="content">
    <c:forEach items="${orderAndEvaluatePgData.rows}" var="orderAndEvaluate">
        <ul>
            <div style="background-color: #e8f2fe;height: 20px;line-height: 20px;padding-left:10px;margin: 1px 1px 0px 1px;border-radius: 5px">
                <span>订单号：${orderAndEvaluate.order.orderCode}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                <span>订单名：${orderAndEvaluate.order.orderName}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                <span>购买的用户：${orderAndEvaluate.order.user.userName}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                <span>用户电话号码：${orderAndEvaluate.order.user.userPhone}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                <span>订单金额：${orderAndEvaluate.order.orderTotal}元</span>&nbsp;&nbsp;&nbsp;&nbsp;
                <span>下单时间：${orderAndEvaluate.order.createDate}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                <span>订单评价时间：${orderAndEvaluate.order.evaluateDate}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="clear"></div>
            </div>
            <c:forEach items="${orderAndEvaluate.goodsScores}" var="goodsScore">
                <li class="order-goods">
                    <img src="${goodsScore.goods.coverImg.url}" style="float: left">

                    <div style="float: left;width: 85%">
                        <span class="order-goods-info">商品名:${goodsScore.goods.goodsName}</span>
                        <span class="order-goods-info stress"
                              style="color: #ff4500;font-weight: bold">用户"${orderAndEvaluate.order.user.userName}"评价：${goodsScore.evaluate}</span>
                        <span class="order-goods-info stress">
                            <span style="float: left;color: #ff4500;font-weight: bold">用户评分: </span>
                            <c:set var="integer" value="${0}"></c:set>
                            <c:forEach var="x" begin="1" end="${goodsScore.score}" step="1">
                                <img class="stars" src="${basePath}/static/images/stars.png">
                                <c:set var="integer" value="${x}"></c:set>
                            </c:forEach>
                            <c:if test="${goodsScore.score - integer == 0.5 }">
                                <img class="stars" src="${basePath}/static/images/stars-half.png">
                                <c:set var="integer" value="${integer + 1}"></c:set>
                            </c:if>
                            <c:forEach var="y" begin="1" end="${5 - integer}" step="1">
                                <img class="stars" src="${basePath}/static/images/stars-dark.png">
                            </c:forEach>
                            <span style="float: left">（${goodsScore.score}分）</span>
                        </span>
                    </div>

                    <div class="clear"></div>
                </li>
            </c:forEach>
        </ul>
    </c:forEach>
</div>
</body>
</html>
