<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <title>新闻发布会</title>
    <link rel="stylesheet" href="${basePath}/static/css/swiper-3.3.1.min.css">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script src="${basePath}/static/js/swiper-3.3.1.jquery.min.js"></script>
    <script src="${basePath}/static/core/utils.js"></script>
    <style>
        .main-container {
            margin: 0 auto;
            max-width: 720px;
        }

        .top-banner {
            width: 100%;
            margin: 0 auto;
            display: block;
        }

        .swiper-container {
            height: 0px;
        }

        .lucky-turntable-link {
            display: block;
            margin-top: 5px;
            padding: 5px;
        }

        .merchant-link-item {
            padding: 6px;
            cursor: pointer;
            border-radius: 5px;
            box-shadow: 4px 4px 3px #888888;
            margin: 5px;
        }

        .link-item {
            padding: 6px;
            cursor: pointer;
            border-radius: 5px;
            box-shadow: 4px 4px 3px #888888;
            margin: 5px;
        }
    </style>
    <script type="text/javascript">
        var userCode, token;
        function onBackUserCode(_userCode) {
            userCode = _userCode;
        }
        function onBackUserToken(_token) {
            token = _token;
        }
        (function (win, $) {
            var bannerRatio = 0.42; //顶部轮播图高与宽的比例
            $(function () {
                initTopBanner();
                initMerchantLink();
                startInitData();
            });

            function startInitData() {
                if (userCode && token) {
                    initLuckyTurnTableLink();
                } else {
                    setTimeout(function () {
                        startInitData();
                    }, 100);
                }
            }

            function initMerchantLink() {
                $('.merchant-link-item').each(function (index, item) {
                    var merchantId = $(item).data('merchantid');
                    var merchantName = $(item).data('merchname');
                    if (merchantId && merchantName) {
                        $(item).click(function () {
                            if (isAndroid()) {
                                JSInterface.navigationMerchant(merchantId, merchantName);
                            } else if (isIOS()) {
                                window.webkit.messageHandlers.navigationMerchant.postMessage(merchantId, merchantName);
                            }
                        })
                    }
                });
            }

            function initTopBanner() {
                new Swiper('.swiper-container', {
                    direction: 'horizontal',
                    loop: true,
                    autoplay: 3000,
                    pagination: '.swiper-pagination'
                });
                var bannerWidth = $(window).width();
                var bannerHeight = bannerWidth * bannerRatio;
                $('.swiper-container').css('height', bannerHeight);
            }

            function initLuckyTurnTableLink() {
                $.ajax({
                    url: '${basePath}/api/luckyDrawDetail/list',
                    type: 'get',
                    dataType: 'text',
                    headers: {
                        userCode: userCode,
                        token: token
                    },
                    success: function (data) {
                        data = $.parseJSON(data);
                        if (data.code == 200) {
                            $('#lucky-turntable-link').attr('href', '${basePath}/web/activityCenter');
                        }
                    },
                    complete: function () {
                        $('#lucky-turntable-link').click(function () {
                            if (!$(this).attr('href')) {
                                alertModal({msg: '暂时没有幸运大转盘活动，敬请期待！'})
                            }
                        });
                    }
                });
            }

            /**
             * 弹出模态对话框
             * @param params params.title:对话框标题；msg:对话框内容
             */
            function alertModal(params) {
                if (params.title) {
                    $($('#alert-modal').data('title-target')).html(params.title);
                }
                if (params.msg) {
                    $($('#alert-modal').data('msg-target')).html(params.msg);
                }
                $('#alert-modal').modal({});
            }
        })(window, jQuery);
    </script>
</head>

<body>

<div class="container main-container">
    <div class="row">
        <header class="top-banner">
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide">
                        <img src="http://o8uwe6adq.bkt.clouddn.com/330455992608826222.jpg" class="img-responsive">
                    </div>
                </div>
                <!-- 如果需要分页器 -->
                <div class="swiper-pagination"></div>
            </div>
        </header>
    </div>
    <div class="row">
        <a class="lucky-turntable-link" id="lucky-turntable-link">
            <img src="${basePath}/static/images/luck_turntable_link.jpg" class="img-responsive">
        </a>
    </div>
    <div class="row">
        <div><img src="${basePath}/static/images/news_realse_separate.png" class="img-responsive"></div>
        <div class="col-xs-7 text-right" style="height: 30px;">
            <img src="${basePath}/static/images/news_realse_title.png"
                 class="img-responsive" style="line-height: 30px;margin-top: 12px">
        </div>
    </div>
    <div class="row merchant-group">
        <c:forEach items="${merchantAdvList}" var="merchantAdv">
            <div class="merchant-link-item" data-merchantId="${merchantAdv.merchant.merchantId}"
                 data-merchName="${merchantAdv.merchant.merchName}">
                <img src="${merchantAdv.advImg.url}" class="img-responsive">
            </div>
        </c:forEach>

        <!--新增网页链接-->
        <div class="link-item">
            <a href="https://view.inews.qq.com/a/20160607A04AP400?cv=0x70000001&dt=6&lang=zh_CN&pass_ticket=fLE5XuIAa4y5gjnvOjYjnit2ZjSbWsvxgC7nsk5SAvvIifzjPwmU7MnKdxFkIcSn">
                <img src="http://o8uwe6adq.bkt.clouddn.com/%E8%85%BE%E8%AE%AF%E7%BD%91.jpg" class="img-responsive">
            </a>
        </div>
        <div class="link-item">
            <a href="http://mt.sohu.com/20160607/n453353484.shtml">
                <img src="http://o8uwe6adq.bkt.clouddn.com/%E6%90%9C%E7%8B%90%E7%BD%91.jpg" class="img-responsive">
            </a>
        </div>
        <div class="link-item">
            <a href="http://yn.news.163.com/16/0607/09/BOUR0FOG03230LFM.html">
                <img src="http://o8uwe6adq.bkt.clouddn.com/%E7%BD%91%E6%98%93.jpg" class="img-responsive">
            </a>
        </div>
        <div class="link-item">
            <a href="http://www.yn.xinhuanet.com/2016news2/20160607/3192112_m.html?from=groupmessage&isappinstalled=0">
                <img src="http://o8uwe6adq.bkt.clouddn.com/%E6%96%B0%E5%8D%8E%E7%BD%91.jpg" class="img-responsive">
            </a>
        </div>
        <div class="link-item">
            <a href="http://www.yunnan.cn/html/2016-06/07/content_4377908.htm">
                <img src="http://o8uwe6adq.bkt.clouddn.com/%E4%BA%91%E5%8D%97%E7%BD%91.jpg" class="img-responsive">
            </a>
        </div>
        <div class="link-item">
            <a href="https://mp.weixin.qq.com/s?__biz=MzA5NjE4NTYyNA==&mid=2650700259&idx=3&sn=46a319604a8463644c484584d6d480c2&scene=1&srcid=0607djri9K26m5XBZxebEQO7&pass_ticket=fLE5XuIAa4y5gjnvOjYjnit2ZjSbWsvxgC7nsk5SAvvIifzjPwmU7MnKdxFkIcSn#rd">
                <img src="http://o8uwe6adq.bkt.clouddn.com/%E6%8B%BE%E4%BA%91%E5%8D%97.jpg" class="img-responsive">
            </a>
        </div>
        <!--网页链接结束-->

    </div>
</div>
<div id="alert-modal" class="modal fade" data-title-target="#alert-title" data-msg-target="#alert-msg">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="alert-title">提示</h4>
            </div>
            <div class="modal-body">
                <p id="alert-msg">One fine body&hellip;</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
                <%--<button type="button" class="btn btn-primary">Save changes</button>--%>
            </div>
        </div>
    </div>
</div>
</body>
</html>
