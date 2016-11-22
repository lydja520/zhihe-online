<%@ page import="cn.com.zhihetech.online.bean.ActivityOrderDetail" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.com.zhihetech.online.vo.ActivityOrderAndActivityOrderDeatils" %>
<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-5-30
  Time: 上午11:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>提交活动商品订单</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#confirm-pay-btn').hide();
            $('#pay-btn').click(function () {
                $('#confirm-pay-btn').show();
            });
        })
    </script>
</head>
<style>
    a {
        text-decoration: none;
    }
</style>
<body>
<table style="width: 80%;margin: 5px auto 40px auto">
    <div>
        <c:if test="${info != null}">
            <div style="text-align: center">${info}</div>
        </c:if>
    </div>
    <tr>
        <td colspan="2" style="color:red;font-weight: bold">
            订单号：${activityOrderAndActivityOrderDeatils.activityOrder.orderCode}
        </td>
    </tr>
    <tr>
        <td colspan="2" style="color:red;font-weight: bold">
            订单名：${activityOrderAndActivityOrderDeatils.activityOrder.orderName}
        </td>
    </tr>
    <tr>
        <td colspan="2" style="color:red;font-weight: bold">
            该支付的总金额：${activityOrderAndActivityOrderDeatils.activityOrder.totalMoney}
        </td>
    </tr>
    <tr>
        <td colspan="2" style="text-align: center">详细信息</td>
    </tr>
    <%
        ActivityOrderAndActivityOrderDeatils activityOrderAndActivityOrderDeatils = (ActivityOrderAndActivityOrderDeatils) request.getAttribute("activityOrderAndActivityOrderDeatils");
        List<ActivityOrderDetail> activityOrderDetails = activityOrderAndActivityOrderDeatils.getActivityOrderDetails();
        int i = 1;
        for (ActivityOrderDetail activityOrderDetail : activityOrderDetails) {
            if (activityOrderDetail.getOrderType() == ActivityOrderDetail.OrderType.activityCost) {
    %>
    <tr style="color: green">
        <td>活动经费:</td>
        <td>
            <%=activityOrderDetail.getMoney()%>元
        </td>
    </tr>
    <%
            continue;
        }
    %>
    <tr>
        <td>红包<%=i%>金额:</td>
        <td>
            <%=activityOrderDetail.getMoney()%>元
        </td>
    </tr>
    <%
            i++;
        }
    %>

</table>
<div id="pay-btn"
     style="position: fixed;bottom: 0px;left: 0px;right: 0px;height: 30px;background-color: #f4f4f4;border-top: solid thin #dddddd">
    <a style="display: block;width: 70px;height: 25px;line-height:25px;text-align:center;margin:auto;
    border: solid thin grey;border-radius: 5px;background-color: white;margin-top: 2px"
       href="${basePath}/admin/api/activityOrder/${activityOrderAndActivityOrderDeatils.activityOrder.activityOrderId}/pay"
       target="_blank" onclick="confirmDialog">
        去支付
    </a>
</div>

<div id="confirm-pay-btn"
     style="position: fixed;bottom: 0px;left: 0px;right: 0px;height: 30px;background-color: #f4f4f4;border-top: solid thin #dddddd;text-align: center;padding-top: 10px">
    <a style="width: 70px;height: 25px;line-height:25px;text-align:center;margin:auto;
    border: solid thin grey;border-radius: 5px;background-color: white;"
       href="${basePath}/admin/api/activityOrder/${activityOrderAndActivityOrderDeatils.activityOrder.activityOrderId}/payOk"
       onclick="confirmDialog">
        支付成功
    </a>
    <a style="width: 70px;height: 25px;line-height:25px;text-align:center;margin:auto;
    border: solid thin grey;border-radius: 5px;background-color: white;margin-left: 20px"
       href="${basePath}/admin/api/activityOrder/${activityOrderAndActivityOrderDeatils.activityOrder.activityOrderId}/payErr"
       onclick="confirmDialog">
        支付出现问题？
    </a>
</div>

</body>
</html>
