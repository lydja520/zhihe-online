<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/1/27
  Time: 17:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>订单详情</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"></c:set>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <style>
        body {
            margin: 0px;
            padding: 0px;
            font-size: 12px;
        }

        .list {
            width: 90%;
            margin: 0px auto 5px auto;
        }

        .list-title {
            color: #ff5500;
            font-size: 14px;
            font-weight: bold;
            padding: 0px;
            margin: 5px auto 5px 0px;
        }

        .order-plate {
            width: 100%;
            height: auto;
            margin: 5px auto 5px auto;
            padding: 2px;
            border: 1px solid #95b8e7;
            border-radius: 6px;
        }

        .order-basic-info {
            margin: 0px;
            padding: 0px;
            list-style-type: none;
            clear: both;
        }

        .order-basic-info li {
            padding: 0px 0px 0px 10px;
            margin: 5px auto 5px auto;
            display: block;
            width: 310px;
            float: left;
        }

        .order-plate-title {
            width: 100%;
            height: 25px;
            line-height: 25px;
            background-color: #e8f2fe;
        }

        #order-total-price {
            clear: both;
        }

        #order-total-price div, .edit-order-price {
            display: block;
            float: right;
            width: 140px;
            height: 20px;
            line-height: 20px;
            margin: 3px;
            background-color: #ff5500;
            color: white;
            text-align: center;
            border-radius: 6px;
        }

        .order-plate-detail {
            padding: 0px;
            margin: 0px;
            list-style-type: none;
        }

        .order-plate-detail li {
            display: block;
            float: left;
            width: 115px;
            height: 80px;
            margin: 3px;
            text-align: center;
        }

        .goods-coverImg {
            width: 80px;
            height: 80px;
        }

        .goods-total-price {
            display: block;
            width: 70px;
            height: 20px;
            line-height: 20px;
            color: white;
            border-radius: 6px;
            margin: 0px auto 0px auto;
            background-color: #ff5500;
        }

        #order-progress-state {
            list-style-type: none;
            width: 55%;
            margin: 0px auto 0px auto;
            padding: 0px;
        }

        #order-progress-state li {
            width: 20%;
            float: left;
            margin: 0px;
            padding: 0px;
            text-align: center;
        }

        #order-progress-state li div {
            width: 90%;
            margin: auto;
        }

        li {
            word-wrap: break-word;
            overflow: hidden;
        }

        .clear {
            clear: both;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            var orderState = "${order.orderState}";
            if (orderState == '2') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_progress_waitpay.png");
                $('#order-subDate').html("${order.createDate}");
            } else if (orderState == '3') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_progress_waitDispatcher.png");
                $('#order-subDate').html("${order.createDate}");
                $('#order-payDate').html("${order.payDate}");
            } else if (orderState == '4') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_progress_waitDeliver.png");
                $('#order-subDate').html("${order.createDate}");
                $('#order-payDate').html("${order.payDate}");
                $('#order-dispatcherDate').html("${order.dispatcherDate}");
            } else if (orderState == '5') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_progress_evaluate.png");
                $('#order-subDate').html("${order.createDate}");
                $('#order-payDate').html("${order.payDate}");
                $('#order-dispatcherDate').html("${order.dispatcherDate}");
                $('#order-deliverDate').html("${order.deliverDate}");
            } else if (orderState == '6') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_progress_waitpay.png");
                $('#order-subDate').html("${order.createDate}");
            } else if (orderState == '7') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_refund_apply.png");
                $('#order-subDate').html("${order.applyRefundDate}");
            } else if (orderState == '8') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_refund_ok.png");
                $('#order-subDate').html("${order.applyRefundDate}");
                $('#order-payDate').html("<span style='color:white'>${order.applyRefundDate}<span>");
                $('#order-dispatcherDate').html("${order.confirmRefundDate}");
                $('#order-deliverDate').html("<span style='color:white;'>${order.confirmRefundDate}<span>");
                $('#order-evaluateDate').html("${order.refundOkDate}");
            } else if (orderState == '9' || orderState == '11' || orderState == '12') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_progress_success.png");
                $('#order-subDate').html("${order.createDate}");
                $('#order-payDate').html("${order.payDate}");
                $('#order-dispatcherDate').html("${order.dispatcherDate}");
                $('#order-deliverDate').html("${order.deliverDate}");
                $('#order-evaluateDate').html("${order.evaluateDate}");
            } else if (orderState == '10') {
                $('#order-progress').attr('src', "${basePath}" + "/static/images/order_refund_confirm.png");
                $('#order-subDate').html("${order.applyRefundDate}");
                $('#order-payDate').html("<span style='color:white'>${order.applyRefundDate}<span>");
                $('#order-dispatcherDate').html("${order.confirmRefundDate}");
            }
        });
    </script>
</head>
<body>
<div id="containner">
    <div class="list" style="margin-top: 10px">
        <div style="width: 100%;">
            <img style="display: block;width: 55%;margin: auto;" id="order-progress" src="${basePath}/static/images"/>
            <ul style="" id="order-progress-state">
                <li>
                    <div id="order-subDate"></div>
                </li>
                <li>
                    <div id="order-payDate"></div>
                </li>
                <li>
                    <div id="order-dispatcherDate"></div>
                </li>
                <li>
                    <div id="order-deliverDate"></div>
                </li>
                <li>
                    <div id="order-evaluateDate"></div>
                </li>
                <div style="clear: both"></div>
            </ul>
        </div>
        <div class="clear"></div>
        <p class="list-title">订单基本信息</p>

        <div class="order-plate">
            <div class="order-plate-title">
                <span>订单号:${order.pingPPorderNo}</span>&nbsp;
                <span>提交时间:${order.createDate}</span>
                <span style="display: block;float: right;color: #ff5500;font-weight: bold">
                    <c:if test="${order.orderState == 2}">
                        <span>（已提交订单，等待买家支付）</span>
                    </c:if>
                    <c:if test="${order.orderState == 20}">
                        <span>（支付中……）</span>
                    </c:if>
                    <c:if test="${order.orderState == 3}">
                        <span>（买家已付款，等待发货）</span>
                    </c:if>
                    <c:if test="${order.orderState == 4}">
                        <span>（已发货，等待买家收货）</span>
                    </c:if>
                    <c:if test="${order.orderState == 5}">
                        <span>（订单已确认收货）</span>
                    </c:if>
                    <c:if test="${order.orderState == 6}">
                        <span>（订单已取消）</span>
                    </c:if>
                    <c:if test="${order.orderState == 7}">
                        <span>（订单等待退款）</span>
                    </c:if>
                    <c:if test="${order.orderState == 8}">
                        <span>（订单退款成功）</span>
                    </c:if>
                     <c:if test="${order.orderState == 10}">
                         <span>（退款中……）</span>
                     </c:if>
                    <c:if test="${order.orderState == 11}">
                        <span>已生成账单，待结算</span>
                    </c:if>
                    <c:if test="${order.orderState == 12}">
                        <span>已结算</span>
                    </c:if>
                </span>
            </div>
            <ul class="order-basic-info">
                <li>订单名:${order.orderName}</li>
                <li>买家号码:${order.user.userPhone}</li>
                <li>
                    支付方式:
                    <c:if test="${order.payType == 'alipay'}">
                        支付宝
                    </c:if>
                    <c:if test="${order.payType == 'wx'}">
                        微信钱包
                    </c:if>
                </li>
                <li>支付日期:
                    <c:if test="${order.payDate == null}">
                        订单未支付
                    </c:if>
                    <c:if test="${order.payDate != null}">
                        ${order.payDate}
                    </c:if>
                </li>
                <li>收件人姓名:${order.receiverName}</li>
                <li>买家留言:${order.userMsg}</li>
                <li>收件人电话号码:${order.receiverPhone}</li>
                <li>快递单号:${order.carriageNum}</li>
            </ul>
            <div style="clear: both;margin:5px auto 5px 10px">收件人详细地址:${order.receiverAdd}</div>
            <c:if test="${order.activityGoods == null}">
                <div id="order-total-price">
                    <div style="margin-right: 15px">订单金额:${order.originalOrderTotal}元</div>
                    <div style="color: black;width: 10px;margin: 0px;padding: 0px;background: none">=</div>
                    <div style="width: 110px">快递运费:${order.carriage}元</div>
                    <div style="color: black;width: 10px;margin: 0px;padding: 0px;background: none">+</div>
                    <div style="width: 110px">商品总价:
                        <c:set var="goodsTotalPrice" value="${0}"></c:set>
                        <c:forEach items="${requestScope.orderDetails}" var="orderDetail">
                            <c:set var="goodsTotalPrice"
                                   value="${goodsTotalPrice+orderDetail.price*orderDetail.goodsCount}"></c:set>
                        </c:forEach>
                            ${goodsTotalPrice}元
                    </div>
                </div>
            </c:if>
            <c:if test="${order.activityGoods != null}">
                <div id="order-total-price">
                    <div style="margin-right: 15px">订单金额:${order.originalOrderTotal}元</div>
                    <div style="color: black;width: 10px;margin: 0px;padding: 0px;background: none">=</div>
                    <div style="width: 110px">快递运费:${order.carriage}元</div>
                    <div style="color: black;width: 10px;margin: 0px;padding: 0px;background: none">+</div>
                    <div style="width: 110px"><s>商品总价:
                        <c:set var="goodsTotalPrice" value="${0}"></c:set>
                        <c:forEach items="${requestScope.orderDetails}" var="orderDetail">
                            <c:set var="goodsTotalPrice"
                                   value="${goodsTotalPrice+orderDetail.price*orderDetail.goodsCount}"></c:set>
                        </c:forEach>
                            ${goodsTotalPrice}元</s>
                    </div>
                </div>
                <div class="clear"></div>
                <div id="order-total-price">
                    <div style="margin-right: 15px">订单金额:${order.originalOrderTotal}元</div>
                    <div style="color: black;width: 10px;margin: 0px;padding: 0px;background: none">=</div>
                    <div style="width: 110px">快递运费:${order.carriage}元</div>
                    <div style="color: black;width: 10px;margin: 0px;padding: 0px;background: none">+</div>
                    <div style="width: 110px">商品总价:
                        <c:set var="goodsTotalPrice" value="${0}"></c:set>
                        <c:forEach items="${requestScope.orderDetails}" var="orderDetail">
                            <c:set var="goodsTotalPrice"
                                   value="${goodsTotalPrice+order.activityGoods.activityPrice*orderDetail.goodsCount}"></c:set>
                        </c:forEach>
                            ${goodsTotalPrice}元
                    </div>
                    <div>该订单为秒杀订单</div>
                </div>
            </c:if>
            <div class="clear"></div>
            <c:if test="${order.orderTotal != order.originalOrderTotal}">
                <div class="edit-order-price" style="float: right;margin-right: 15px">修改后的价格：${order.orderTotal}</div>
            </c:if>
            <div class="clear"></div>
        </div>
    </div>

    <div class="list">
        <p class="list-title">订单中的商品</p>
        <c:forEach items="${requestScope.orderDetails}" var="orderDetail">
            <div class="order-plate">
                <div class="order-plate-title" style="overflow-x: hidden">
                    <div>商品名：${orderDetail.goods.goodsName}&nbsp;${orderDetail.skuName}</div>
                </div>
                <ul class="order-plate-detail">
                    <li style="width: 80px">
                        <img src="${orderDetail.goods.coverImg.url}" class="goods-coverImg" style="border-radius: 8px">
                    </li>
                    <li>
                        <p>${orderDetail.goods.goodsName}</p>
                    </li>
                    <li>
                        <p>${orderDetail.skuName}</p>
                    </li>
                    <li>
                        <p>商品单价</p>

                        <p>
                            <span style="color: #ff5500;font-weight: bold">${orderDetail.price}</span> 元
                        </p>
                    </li>
                    <li>
                        <p>买家购买了</p>

                        <p>
                            <span style="color: #ff5500;font-weight: bold">${orderDetail.goodsCount}</span> 件
                        </p>
                    </li>
                    <li>
                        <p>总价</p>

                        <p class="goods-total-price">${order.orderTotal}元</p>
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
        </c:forEach>
    </div>
</div>

</div>
</body>
</html>