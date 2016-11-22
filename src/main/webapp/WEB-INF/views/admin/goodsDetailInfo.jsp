<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/1/13
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商品相信信息</title>
    <style>
        #basic_info_sub div {
            height: 20px;
            line-height: 20px;
            margin-bottom: 10px;
            width: 100%;
        }
    </style>
</head>
<body>
<div id="container" style="width: 85%;margin: 10px auto 10px auto">
    <div style="height: 30px;width:40%;color:white;margin:15px auto 15px auto;background-color: #269abc;margin-top: 20px;line-height: 30px;text-align: center;border-radius: 15px">基本信息</div>
    <div style="width: 250px;height: 250px;float: left">
        <img src="${requestScope.goods.coverImg.url}" style="width: 100%;height: 100%">
    </div>
    <div style="float: left;width: 250px;height: 200px;margin-left: 20px" id="basic_info">
        <div style="font-weight: bold;font-size: 18px;margin-bottom: 15px">${requestScope.goods.goodsName}</div>
        <div style="" id="basic_info_sub">
            <div>
                <span>价格:</span>
                <span>${requestScope.goods.price} 元</span>
            </div>
            <div>
                <span>销量:</span>
                <span>${requestScope.goods.volume} 件</span>
                <span style="padding-left: 30px">库存量:</span><span>${requestScope.goods.currentStock} 件</span></div>
            <div>
                <span>是否上架:</span>
                <c:if test="${requestScope.goods.onsale == true}">
                    <span>是</span>
                </c:if>
                <c:if test="${requestScope.goods.onsale == false}">
                    <span>否</span>
                </c:if>
            </div>
            <div>
                <span>是否可自取:</span>
                <c:if test="${requestScope.goods.isPick == true}">
                    <span> 是 </span>
                </c:if>
                <c:if test="${requestScope.goods.isPick == false}">
                    <span> 否 </span>
                </c:if>
            </div>
            <c:if test="${not empty requestScope.goods.carriageMethod}">
                <div>
                    <span>快递方式:</span>
                    <span>${requestScope.goods.carriageMethod}</span>
                    <span style="padding-left: 30px">运费:</span>
                    <span>${requestScope.goods.carriage}</span>
                </div>
            </c:if>
            <div>
                <span>商品分类:</span>
                <span>${requestScope.goods.goodsAttributeSet.goodsAttSetName}</span>
            </div>
        </div>
    </div>
    <div style="clear: both"></div>
    <div style="height: 30px;width:40%;background-color: green;color:white;margin: 15px auto 15px auto;;line-height: 30px;text-align: center;border-radius: 15px">轮播图</div>
    <c:forEach items="${goodsBanners}" var="goodsBanner">
        <div style="width: 100%;margin-bottom: 2px"><img src="${goodsBanner.imgInfo.url}" style="width: 100%;height: auto"></div>
    </c:forEach>
    <div style="height: 30px;width:40%;background-color: #761c19;color:white;margin: 15px auto 15px auto;;line-height: 30px;text-align: center;border-radius: 15px">详情图</div>
    <c:forEach items="${goodsDetails}" var="goodsDetail">
        <div style="width: 100%;margin-bottom: 2px"><img src="${goodsDetail.imgInfo.url}" style="width: 100%;height: auto"></div>
    </c:forEach>
</div>
</body>
</html>
