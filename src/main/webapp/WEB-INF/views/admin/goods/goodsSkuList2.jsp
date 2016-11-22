<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/7/13
  Time: 10:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
    table{
        table-layout: fixed;
    }

    th, td {
        text-align: center;
        word-wrap: break-word;
    }
</style>

<c:if test="${skuList.size() <= 0}">
    <div style="margin: 5px auto;text-align: center;line-height: 30px;height:30px;border: solid thin #a0c6e5">
        还未添加该商品的库存和价格
    </div>
</c:if>

<c:if test="${skuList.size() > 0}">
    <table border="1" bordercolor="#a0c6e5" align="center" style="border-collapse:collapse;width: 100%;">
        <c:set var="goodsAttributes" value="${skuList.get(0).goodsAttributes}"></c:set>
        <tr>
            <th>&nbsp;</th>
            <th>封面图</th>
            <c:forEach items="${skuList.get(0).goodsAttributes}" var="goodsAttribute">
                <th>${goodsAttribute.attribute.skuAttName}</th>
            </c:forEach>
            <th>库存</th>
            <th>销量</th>
            <th>价格(元)</th>
        </tr>
        <c:set var="count" value="${0}"></c:set>
        <c:forEach items="${skuList}" var="sku">
            <c:set var="count" value="${count+1}"></c:set>
            <tr>
                <td>${count}</td>
                <td>
                    <c:if test="${sku.coverImg == null}">
                        <img style="width: 30px;height: 30px" src="/static/images/noPic.jpg">
                    </c:if>
                    <c:if test="${sku.coverImg != null}">
                        <img style="width: 30px;height: 30px" src="${sku.coverImg.url}">
                    </c:if>
                </td>
                <c:forEach items="${sku.goodsAttributes}" var="goodsAtt">
                    <td>${goodsAtt.attrValue}</td>
                </c:forEach>
                <td>${sku.currentStock}</td>
                <td>${sku.volume}</td>
                <td style="color: green">${sku.price}元</td>
            </tr>
        </c:forEach>
    </table>
</c:if>

