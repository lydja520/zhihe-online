<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>${activity.activitName}</title>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <link href="${basePath}/static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript">
        var token = null;
        var userId = null;
        var userCode = null;
        var u = navigator.userAgent;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

        $(document).ready(function () {
            $('.merchant-list li').css('width', parseInt($('.container').css('width')) * 0.15);
            $('.merchant-list li').css('height', $('.merchant-list li').css('width'));
            $('.merchant-list-div').css('height', parseInt($('.merchant-list li').css('height')) + parseInt($('.merchant-list li').css('margin')) * 2);
            $('.merchant-list').css('width', parseInt($('.merchant-list li').length) * (parseInt($('.merchant-list li').css('width')) + parseInt($('.merchant-list li').css('margin-right'))));
            $(".coupon-list-content").css('height', parseInt($(".coupon-list-content").css('width')) * 0.6);
            $('.coupon-list-content-total').css('line-height', $('.coupon-list-content-total').css('height'));
            $('.coupon-info-goods-list ').css('width', parseInt($('.coupon-info-goods-list li').length) * (parseInt($('.coupon-info-goods-list li').css('width')) + parseInt($('.coupon-info-goods-list li').css('margin-right'))));
            $('.activity-promoter-header-img').css('height', parseInt($('.activity-promoter-header-img').css('width')) * 0.4);

            /*========获取userId和token和userCode========*/
            getUserAndToken();

            /*========活动优惠券========*/
            $('.coupon-get-button').each(function (index, element) {
                $(this).click(function () {
                    $.ajax({
                        url: '${basePath}/api/coupon/grab',
                        type: 'post',
                        dataType: 'text',
                        data: {
                            couponId: $(this).val(),
                            userId: userId
                        },
                        headers: {
                            userCode: userCode,
                            token: token
                        },
                        beforeSend: function () {
                            /*加载动画*/
                            if (isAndroid) {
                                JSInterface.toggleProgressDialog(true, "努力中……");
                            }
                            if (isiOS) {
                                window.webkit.messageHandlers.toggleProgressDialog.postMessage(true);
                            }
                        },
                        success: function (info) {
                            if (isAndroid) {
                                JSInterface.toggleProgressDialog(false, "");
                            }
                            if (isiOS) {
                                window.webkit.messageHandlers.toggleProgressDialog.postMessage(false);
                            }
                            info = $.parseJSON(info);
                            if (info.success) {
                                $('.couponItemId').attr('value', info.attribute.couponItemId);
                                $('#successModal').modal('show');  //成功领取
                            } else {
                                if (info.code == "861") {
                                    $('.couponItemId').attr('value', info.attribute);
                                    $('#alreadyGrabModal').modal('show');  //已经领取过
                                    return;
                                }
                                if (info.code == "862") {
                                    $('#alreadyOverModal').modal('show');   //已被领取完
                                    return
                                }
                                $('#failureModal').modal('show');  //领取失败
                            }
                        },
                        error: function () {
                            JSInterface.toggleProgressDialog(false, "");
                            alert('系统出错，请与管理员联系');   //系统出错
                        }
                    });
                });
            });

            /*========商家跳转=========*/
            $('.merchant-header').each(function (index, element) {
                $(element).click(function () {
                    var merchantId = $(this).prev().attr('value');
                    var merchantName = $(this).prev().prev().attr('value');
                    if (isAndroid) {
                        JSInterface.navigationMerchant(merchantId, merchantName);
                    } else if (isiOS) {
                        window.webkit.messageHandlers.navigationMerchant.postMessage(merchantId, merchantName);
                    }
                });
            });

            /*========活动商品跳转========*/
            $('.activity-good-img').each(function (index, element) {
                $(element).click(function () {
                    /*var goodsId = $(this).prev().attr('value');
                     var goodsName = $(this).prev().prev().attr('value');
                     if (isAndroid) {
                     JSInterface.navigationActivityGoods(goodsId, goodsName);
                     } else if (isiOS) {
                     window.webkit.messageHandlers.navigationActivityGoods.postMessage(goodsId, goodsName);
                     }*/
                    showSeckillGoodsInfo(element);
                })
            });

            /*========点击查看优惠券========*/
            $('.coupon-get-modal-button').each(function (index, element) {
                $(element).click(function () {
                    var couponItemId = $(this).prev().val();
                    if (isAndroid) {
                        JSInterface.navigationCouponItemInfo(couponItemId);
                    } else if (isiOS) {
                        window.webkit.messageHandlers.navigationCouponItemInfo.postMessage(couponItemId);
                    }
                });
            })

            /**
             * 活动规则按钮点击事件
             */
            $('#activity-desc-btn').click(function () {
                var desc = $(this).data('activity-desc') || '暂无简介！';
                $('#activity-desc-content').html(desc);
                $('#activity-desc-modal').modal({});
            })
        });

        function getUserAndToken() {
            if (isAndroid) {
                JSInterface.getUserToken();
                JSInterface.getUserId();
                JSInterface.getUserCode();
            }
        }

        function onBackUserCode(_userCode) {
            userCode = _userCode;
        }

        function onBackUserId(_userId) {
            userId = _userId;
        }

        function onBackUserToken(_token) {
            token = _token;
        }

        function showSeckillGoodsInfo(target) {
            $('#seckill-goods-cover-img').attr('src', $(target).attr('src'));
            $('#seckill-goods-name').html($(target).data('goods-name'));
            $('#seckill-goods-count').html('数量:' + $(target).data('count'));
            $('#seckill-price').html('秒杀价:' + $(target).data('seckill-price'));
            $('#seckill-old-price').html('原价:' + $(target).data('old-price'));
            $('#seckill-goods-desc').html($(target).data('desc'));
            $('#seckill-goods-info-modal').modal({});
        }
    </script>
    <style>
        body {
            margin: 0px;
            padding: 0px;
            background-color: #3a4d5c;
            font-size: 12px;
            font-family: "微软雅黑";
        }

        .container {
            width: 100%;
            height: auto;
        }

        .top-container {
            width: 100%;
            height: 30%;
            background-image: url("${basePath}/static/css/theme/nomal/imgs/top_img.png");
            background-repeat: no-repeat;
            background-size: 100% auto;
            position: relative;
        }

        .main-container1 {
            width: 100%;
            background-image: url("${basePath}/static/css/theme/nomal/imgs/dot_background.png");
            background-repeat: repeat-y;
            background-size: 100% auto;
        }

        .merchant-list-title {
            margin-top: 5px;
            margin-bottom: 5px;
        }

        .merchant-list-title img {
            width: 50%;
            display: block;
            margin: 0px auto 0px auto;
        }

        .merchant-list-div {
            width: 90%;
            padding: 0px;
            margin: 0px auto 0px auto;
            overflow-x: auto;
            overflow-y: hidden;
        }

        .merchant-list {
            padding: 0px;
            margin: 0 auto 0px auto;
            list-style-type: none;
            text-align: center;
            align-content: center;
        }

        .merchant-list li {
            width: 90px;
            float: left;
            margin-right: 8px;
            padding: 0px;
        }

        .merchant-list li img {
            width: 100%;
            height: 100%;
            border-radius: 10px;
        }

        .main-container2 {
            width: 100%;
            padding-top: 20px;
            background-image: url("${basePath}/static/css/theme/nomal/imgs/dot_background.png");
            background-repeat: repeat-y;
            background-size: 100% auto;
        }

        .coupon-list-title {
            margin: 5px 0px 5px 0px;
        }

        .coupon-list-title img {
            width: 30%;
            display: block;
            margin: 0px auto 0px auto;
        }

        .coupon-list {
            width: 92%;
            padding: 1%;
            margin: 0px auto 0px auto;
            list-style-type: none;
            border: solid thin white;
            border-radius: 10px;
            overflow-x: auto;
            overflow-x: hidden;
        }

        .coupon-list li {
            width: 50%;
            margin-bottom: 10px;
            float: left;
        }

        .coupon-list-content {
            width: 95%;
            margin: 0px auto 0px auto;
            border: solid thin #445442;
            border-radius: 10px;
        }

        .coupon-list-content1 {
            background-color: #cb6ca6;
        }

        .coupon-list-content2 {
            background-color: #4d5ed0;
        }

        .coupon-list-content-total {
            width: 45%;
            height: 65%;
            font-size: 20px;
            font-weight: bolder;
            font-family: Helvetica, Arial, sans-serif;
            text-align: center;
            color: white;
            float: left;
        }

        .coupon-list-content-msg {
            width: 55%;
            height: 60%;
            float: right;
            color: white;
            overflow-y: hidden;
            text-align: center;
        }

        .coupon-list-content-msg p {
            width: 92%;
            font-weight: lighter;
            font-size: 11px;
        }

        .coupon-get {
            width: 100%;
            height: 35%;
            clear: both;
        }

        .coupon-get-button {
            width: 80%;
            color: #666666;
            -moz-opacity: 0.8;
            opacity: 0.6;
            display: block;
            height: 75%;
            margin: 0px auto;
            border: none;
            border-radius: 5px;
            text-align: center;
            background-color: white;
        }

        .main-container3 {
            width: 100%;
            padding-top: 20px;
            background-image: url("${basePath}/static/css/theme/nomal/imgs/dot_background.png");
            background-repeat: repeat-y;
            background-size: 100% auto;
        }

        .coupon-info {
            width: 92%;
            padding: 1%;
            margin: 0px auto 0px auto;
            list-style-type: none;
            border: solid thin white;
            border-radius: 10px;
        }

        .coupon-info-title, .coupon-info-notice, .activity-promoter-title {
            color: white;
            font-size: 14px;
            font-weight: bolder;
            margin: 10px;
        }

        .coupon-info-notice {

        }

        .coupon-info-goods {
            width: 90%;
            margin: 0px auto 0px auto;
            overflow-x: auto;
            overflow-y: hidden;
        }

        .coupon-info-goods-list {
            margin: 0px auto 0px auto;
            padding: 0px;
            list-style-type: none;
        }

        .coupon-info-goods-list li {
            width: 55px;
            height: 55px;
            background-color: white;
            float: left;
            margin-right: 5px;
            border-radius: 10px;
            border-color: white;
        }

        .activity-promoter-header-img {
            width: 90%;
            margin: 0px auto 10px auto;
            background-image: url("${requestScope.activity.activityPromoter.headerImg.url}");
            background-repeat: no-repeat;
            background-size: 100% auto;
            border-radius: 10px;
            border: solid medium mintcream;
        }

        .main-container4 {
            width: 100%;
            padding: 0px;
            margin: 20px auto 5px auto;
        }

        .bottom-container {
            width: 100%;
            height: 100px;
            background-image: url("${basePath}/static/css/theme/nomal/imgs/bottom_img.png");
            background-size: 100% auto;
            background-repeat: no-repeat;
        }

        .activity-desc {
            width: 33%;
            display: block;
            margin: 0px auto 0px auto;
        }

        .activity-good-img {
            width: 97%;
            height: 97%;
            border-radius: 10px;
            display: block;
            margin: 1% auto 1% auto;
        }

        #successModalBg {
            height: 170px;
            background-image: url("${basePath}/static/images/successCoupon.png");
            background-size: 100% 100%;
        }

        #failureModalBg {
            height: 170px;
            background-image: url("${basePath}/static/images/failuredCoupon.png");
            background-size: 100% 100%;
        }

        #alreadyGrabModalBg {
            height: 170px;
            background-image: url("${basePath}/static/images/alreadyGrabCoupon.png");
            background-size: 100% 100%;
        }

        #alreadyOverModalBg {
            height: 170px;
            background-image: url("${basePath}/static/images/alreadyOverCoupon.png");
            background-size: 100% 100%;
        }

        .coupon-get-modal {
            width: 40%;
            margin: 100px auto auto auto;
        }

        .coupon-get-modal-button {
            width: 100%;
        }

        .clear {
            clear: both;
        }

        .sponsor-merchant-group {
            height: auto;
            text-align: center;
            margin: 5px auto;
            overflow-y: hidden;
            overflow-x: auto;
            white-space: nowrap;
        }

        .merchant-item {
            display: inline-block;
            margin: 2px;
        }

        .merchant-header {
            width: 50px;
            height: 50px;
            border-radius: 5px;
        }
    </style>
</head>
<body>
<div class="container" style="width: 100%;padding: 0px;margin: 0px">
    <div class="top-container"></div>
    <div class="main-container1">
        <div class="merchant-list-title">
            <img src="${basePath}/static/css/theme/nomal/imgs/merchant_list_title.png">
        </div>

        <div class="merchant-list-div" style="display: none">
            <ul class="merchant-list">
                <!--参与活动的商家列表开始-->
                <c:forEach items="${requestScope.merchantAlliances}" var="merchantAlliance">
                    <li>
                        <input type="hidden" value="${merchantAlliance.merchant.merchName}"/>
                        <input type="hidden" value="${merchantAlliance.merchant.merchantId}"/>
                        <img src="${merchantAlliance.merchant.coverImg.url}" class="merchant-cover-img">
                    </li>
                </c:forEach>
                <!--参与活动的商家列表结束-->
                <div class="clear"></div>
            </ul>
        </div>

        <div class="sponsor-merchant-group">
            <!--参与活动的商家列表开始-->
            <c:forEach items="${requestScope.merchantAlliances}" var="merchantAlliance">
                <div class="merchant-item">
                    <input type="hidden" value="${merchantAlliance.merchant.merchName}"/>
                    <input type="hidden" value="${merchantAlliance.merchant.merchantId}"/>
                    <img src="${merchantAlliance.merchant.coverImg.url}" class="merchant-header">
                </div>
            </c:forEach>
            <!--参与活动的商家列表结束-->
        </div>

        <div class="clear"></div>
    </div>
    <div class="main-container2">
        <ul class="coupon-list">
            <div class="coupon-list-title">
                <img src="${basePath}/static/css/theme/nomal/imgs/coupon_title.png">
            </div>
            <!--活动券开始-->
            <c:forEach items="${requestScope.coupons}" var="coupon">
                <li>
                    <!--活动打折券开始-->
                    <c:if test="${coupon.couponType == 1}">
                        <div class="coupon-list-content coupon-list-content1">
                            <div class="coupon-list-content-total">${coupon.faceValue}折</div>
                            <div class="coupon-list-content-msg"><p>${coupon.couponMsg}</p></div>
                            <div class="coupon-get">
                                <button class="coupon-get-button" value=${coupon.couponId}>立即领取
                                </button>
                            </div>
                        </div>
                    </c:if>
                    <!--活动打折券结束-->
                    <!--活动优惠券开始-->
                    <c:if test="${coupon.couponType == 2}">
                        <div class="coupon-list-content coupon-list-content2">
                            <div class="coupon-list-content-total">￥${coupon.faceValue}</div>
                            <div class="coupon-list-content-msg"><p>${coupon.couponMsg}</p></div>
                            <div class="coupon-get">
                                <button class="coupon-get-button" value=${coupon.couponId}>立即领取</button>
                            </div>
                        </div>
                    </c:if>
                    <!--活动优惠券结束-->
                </li>
            </c:forEach>
            <!--活动券结束-->
            <div class="clear"></div>
        </ul>
        <div class="clear"></div>
    </div>
    <div class="main-container3">
        <div class="coupon-info">
            <p class="coupon-info-title">
                活动时间：<fmt:formatDate value="${activity.beginDate}" pattern="MM-dd HH:mm"></fmt:formatDate> 至
                <fmt:formatDate value="${activity.endDate}" pattern="MM-dd HH:mm"></fmt:formatDate>
            </p>

            <p class="coupon-info-notice">秒杀预告:</p>

            <div class="coupon-info-goods">
                <ul class="coupon-info-goods-list">
                    <!--即将参加活动的商品开始-->
                    <c:forEach items="${requestScope.activityGoodses}" var="activityGoods">
                        <li>
                            <input type="hidden" value="${activityGoods.goods.goodsName}"/>
                            <input type="hidden" value="${activityGoods.agId}"/>
                            <img class="activity-good-img" data-old-price="${activityGoods.goods.price}"
                                 data-seckill-price="${activityGoods.activityPrice}"
                                 data-count="${activityGoods.agCount}"
                                 data-goods-name="${activityGoods.goods.goodsName}"
                                 data-desc="${acitivityGoodsDesc}"
                                 src="${activityGoods.sku.coverImg.url}">
                        </li>
                    </c:forEach>
                    <!--即将参加活动的商品结束-->
                    <div class="clear"></div>
                </ul>
            </div>

            <%--<div class="activity-promoter">
                <p class="activity-promoter-title">赞助商家：</p>

                <div class="activity-promoter-header-img"></div>
            </div>--%>
        </div>
        <div class="clear"></div>
    </div>
    <div class="main-container4">
        <button class="btn btn-info activity-desc" id="activity-desc-btn"
                data-activity-desc="${activity.activitDetail}">
            查看活动规则
        </button>
    </div>
    <div class="bottom-container"></div>
    <div class="clear"></div>
</div>

<!-- 模态框（Modal） 优惠券获取成功-->
<div class="modal fade" id="successModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="successModalBg">
            <button type="button" class="close"
                    data-dismiss="modal" aria-hidden="true" style="margin:10px 20px auto auto">
                &times;
            </button>
            <div class="coupon-get-modal">
                <input type="hidden" class="couponItemId"/>
                <img src="${basePath}/static/images/coupon_button.png" class="coupon-get-modal-button">
            </div>
        </div>
    </div>
</div>

<!-- 模态框（Modal） 优惠券获取失败-->
<div class="modal fade" id="failureModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="failureModalBg">
            <button type="button" class="close"
                    data-dismiss="modal" aria-hidden="true" style="margin:10px 20px auto auto">
                &times;
            </button>
        </div>
    </div>
</div>

<!-- 模态框（Modal） 优惠券已经领取-->
<div class="modal fade" id="alreadyGrabModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="alreadyGrabModalBg">
            <button type="button" class="close"
                    data-dismiss="modal" aria-hidden="true" style="margin:10px 20px auto auto">
                &times;
            </button>
            <div class="coupon-get-modal">
                <input type="hidden" class="couponItemId"/>
                <img src="${basePath}/static/images/coupon_button.png" class="coupon-get-modal-button">
            </div>
        </div>
    </div>
</div>

<!-- 模态框（Modal） 优惠券已领光-->
<div class="modal fade" id="alreadyOverModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="alreadyOverModalBg">
            <button type="button" class="close"
                    data-dismiss="modal" aria-hidden="true" style="margin:10px 20px auto auto">
                &times;
            </button>
        </div>
    </div>
</div>

<!-- 模态框（Modal） 秒杀商品详情-->
<div id="seckill-goods-info-modal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="alert-title">秒杀详情</h4>
            </div>
            <div class="modal-body">
                <div>
                    <img class="img-responsive" id="seckill-goods-cover-img">
                </div>
                <h5 id="seckill-goods-name"></h5>
                <p>
    <span id="seckill-price"
          style="color: red; width: 30%;font-weight: bold;display: inline-block;font-size: 14px;"></span>
                    <span id="seckill-old-price" style="width: 30%;display: inline-block;font-size: 14px;"></span>
                    <span id="seckill-goods-count" style="width: 30%;display: inline-block;font-size: 14px;"></span>
                </p>
                <p id="seckill-goods-desc"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
            </div>
        </div>
    </div>
</div>

<!-- 模态框（Modal）活动规则-->
<div id="activity-desc-modal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">活动规则</h4>
            </div>
            <div class="modal-body">
                <p id="activity-desc-content"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
