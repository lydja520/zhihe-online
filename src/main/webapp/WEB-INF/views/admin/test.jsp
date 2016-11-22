<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/18
  Time: 11:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
</head>
<script type="text/javascript" src="/jquery/jquery.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $(".btn1").click(function () {
            $("p").animate({left: "100px"});
        });
        $(".btn2").click(function () {
            $("p").animate({left: ""});
        });
    });
</script>

<script type="text/javascript">
    $(document).ready(function(){
        $('#submit').click(function(){
            $('#orderDetail1').attr('value',$('#goods1_id').val()+','+$('#goods1_count').val());
            $('#orderDetail2').attr('value',$('#goods2_id').val()+','+$('#goods2_count').val());
            $('#orderDetail3').attr('value',$('#goods3_id').val()+','+$('#goods3_count').val());
            $('#order_form').submit();
        });
    });
</script>

</head>

<body>
<form action="${basePath}/api/securityCode/get" method="post">
    手机号：<input type="text" name="phoneNumber">
    <br><br>
    状态码：<input type="text" name="securityState">
    <br><br>
    <button type="submit">获取验证码</button>
</form>

<br><br>

<h1>提交订单</h1>

<form action="${basePath}/api/order/add" method="post" id="order_form">
    订单名：<input type="text" name="orderName">
    <br><br>
    用户Id：<input type="text" name="user.userId">
    <br><br>
    订单金额:<input type="text" name="orderTotal">
    <br><br>
    给商家留言：<input type="text" name="userMsg">
    <br><br>
    签收人姓名:<input type="text" name="receiverName">
    <br><br>
    签收人电话号码:<input type="text" name="receiverPhone">
    <br><br>
    签收人地址:<input type="text" name="receiverAdd">
    <br><br>
    商品1：<input type="text" id="goods1_id">
    商品1件数：<input type="text" id="goods1_count">
    <input type="hidden" name="orderDetailInfos" id="orderDetail1">
    <br>
    商品2：<input type="text" id="goods2_id">
    商品2件数：<input type="text" id="goods2_count">
    <input type="hidden" name="orderDetailInfos" id="orderDetail2">
    <br>
    商品3：<input type="text" id="goods3_id">
    商品3件数：<input type="text" id="goods3_count">
    <input type="hidden" name="orderDetailInfos" id="orderDetail3">
    <br><br>
    <input type="submit" id="submit" value="提交">
</form>

<br><br>
<h1>确认付款</h1>
<form action="${basePath}/api/charge/get" method="post">
    金额：<input type="text" name="amount">
    <br><br>
    订单号:<input type="text" name="orderNo">
    <br><br>
    支付平台：<input type="text" name="channel">
    <br><br>
    <button type="submit">确认收货</button>
</form>

</body>
</html>
