<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>VIP专区</title>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link href="${basePath}/static/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/utils.js"></script>
    <script type="text/javascript" src="${basePath}/static/js/handlebars.js"></script>
    <script type="text/javascript">
        var userCode, token, userId;
        function onBackUserCode(_userCode) {
            userCode = _userCode;
        }
        function onBackUserToken(_token) {
            token = _token;
        }
        function onBackUserId(_userId) {
            userId = _userId;
        }
        $(function () {
            var screenWidth = $(window).width();
            var itemHeight = 0.45 * screenWidth;
            $('.merchant-item').each(function (index, element) {
                $(element).css('height', itemHeight)
                        .css('line-height', itemHeight + 'px')
                        .click(function () {
                            var merchantId = $(this).data('merchant-id');
                            var merchantName = $(this).data('merchant-name');
                            if (isAndroid()) {
                                JSInterface.navigationMerchant(merchantId, merchantName);
                            } else if (isIOS()) {
                                window.webkit.messageHandlers.navigationMerchant.postMessage(merchantId, merchantName);
                            }
                        });
            });
            $('#next-pager').click(function () {
                if (!$(this).data('loading')) {
                    loadMoreData();
                }
            })
            startLoad();
        });

        function startLoad() {
            if (userCode && token) {
                loadMoreData();
            } else {
                setTimeout(function () {
                    startLoad();
                }, 100)
            }
        }

        /**
         *导航到商家
         */
        function navigationMerchant(obj) {
            var merchantId = $(obj).data('merchant-id');
            var merchantName = $(obj).data('merchant-name');
            if (isAndroid()) {
                JSInterface.navigationMerchant(merchantId, merchantName);
            } else if (isIOS()) {
                window.webkit.messageHandlers.navigationMerchant.postMessage(merchantId, merchantName);
            }
        }

        /**
         * 分页加载数据
         */
        function loadMoreData() {
            if ($('#next-pager').data('loading')) {
                return;
            }
            $('#next-pager').html('加载中...').data('loading', true);
            var page = $('#next-pager').data('page') + 1;
            var rows = $('#next-pager').data('page-size');
            $.ajax({
                url: '${basePath}/api/vipzone/vipmerchants',
                type: 'post',
                dataType: 'text',
                data: {
                    page: page,
                    rows: rows
                },
                headers: {
                    token: token,
                    userCode: userCode
                },
                success: function (data) {
                    data = $.parseJSON(data);
                    onLoadSuccess(data);
                },
                complete: function () {
                    $('#next-pager').html('下一页').data('loading', false);
                }
            })
        }

        /**
         * 远程加载数据成功回调
         * @param data
         */
        function onLoadSuccess(data) {
            $('#next-pager').data('page', data.page)
                    .data('page-size', data.pageSize)
                    .data('total-page', data.totalPage)
                    .css('display', data.page >= data.totalPage ? 'none' : 'block');
            var template = Handlebars.compile($('#merchants-template').html());
            $('#merchants-container').append(template(data));
        }
    </script>
    <style type="text/css">
        * {
            font-family: 微软雅黑;
        }

        .merchant-item {
            text-align: center;
            border-radius: 5px;
            box-shadow: 4px 4px 3px #888888;
            cursor: pointer;
            overflow: hidden;
            margin: 10px 0;
            background-size: cover;
            background-repeat: no-repeat;
            height: 160px;
            line-height: 160px;
        }

        .merchant-item .merchant-name {
            background-color: rgba(40, 40, 40, 0.4);
            text-align: center;
            border-radius: 2px;
            color: white;
            padding: 6px 10px;
            font-size: 16px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div id="merchants-container"></div>
            <button class="btn btn-primary btn-block btn-lg" id="next-pager" data-page="0"
                    data-page-size="20" data-total-page="1" data-loading="false"
                    style="display: none">下一页
            </button>
        </div>
    </div>
</div>
<script id="merchants-template" type="text/x-handlebars-template">
    {{#each rows}}
    <div data-merchant-id="{{merchantId}}" data-merchant-name="{{merchName}}"
         class="merchant-item" onclick="navigationMerchant(this)"
         style="background-image: url('{{headerImg.url}}')">
        <span class="merchant-name">{{merchName}}</span>
    </div>
    {{/each}}
</script>
</body>
</html>
