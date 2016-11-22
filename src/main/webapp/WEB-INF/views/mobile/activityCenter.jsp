<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>幸运大转盘</title>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link href="${basePath}/static/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePaht}/static/js/awardRotate.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/utils.js"></script>
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
        (function (win, $) {
            var turntableMargin = 30;
            var screenWidth = $(win).width();
            var turntableContainerWidth = screenWidth;
            var turntableWidth = turntableContainerWidth - (turntableMargin * 2);
            var turntableHeight = turntableWidth;
            var pointerX = turntableWidth / 2;
            var pointerY = turntableHeight / 2;
            var radius = turntableWidth / 2;
            var pointerUrl = '${basePath}/static/images/pointer.png' + '?' + new Date().getTime();
            var isRotating = false; //大转盘是否正在旋转
            //var pointerAngle = -0.5 * Math.PI;  //指针所指角度
            var luckItems = undefined;
            var rotateInterval = undefined; //转盘定时器
            var luckyDrawContainerWidth = turntableWidth;
            var luckyDrawContainerHeight = luckyDrawContainerWidth;

            $(function () {
                initMerchantLink();
                startInitData();
            });

            function startInitData() {
                if (userCode && token) {
                    loadDataAndDraw();
                } else {
                    setTimeout(function () {
                        startInitData();
                    }, 100);
                }
            }

            /**
             *  加载奖品数据并绘制大转盘
             */
            function loadDataAndDraw() {
                $('#turntable').attr('width', turntableWidth).attr('height', turntableHeight);
                $('#turntable-container').css('width', turntableContainerWidth)//.css('height', turntableContainerWidth)
                        .css("padding", turntableMargin);
                $('#lucky-draw-container').css('width', luckyDrawContainerWidth).css('height', luckyDrawContainerHeight);
                if (!userCode || !token) {
                    onLoadDataError('请使用实淘客户端登录后重试！');
                    return;
                }
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
                            luckItems = data.attribute;
                            try {
                                drawTurntable(pointerX, pointerY, radius, luckItems);
                                drawPointer(luckItems);
                            } catch (e) {
                                console.log(e);
                                onLoadDataError('初始化转盘失败，请重试！');
                            }
                        } else {
                            onLoadDataError(data.msg);
                        }
                    },
                    error: function (ex) {
                        onLoadDataError('获取数据失败，请刷新重试！');
                    }
                });
            }

            /**
             *初始化数据失败时调用
             */
            function onLoadDataError(msg) {
                alertModal({msg: msg || '初始数据失败,请重试！'});
            }

            /**
             *  绘制指针
             */
            function drawPointer(data) {
                var pointerWidth = 87;
                var pointerHeight = 104;
                var pointer = new Image();
                pointer.src = pointerUrl;
                pointer.onload = function () {
                    $(this).attr('width', pointerWidth).attr('height', pointerHeight);
                    drawPointerImage(this, pointerWidth, pointerHeight, data);
                }
            }

            function drawPointerImage(img, width, height, data) {
                var top = (luckyDrawContainerHeight - height) / 2;
                var left = (luckyDrawContainerWidth - width) / 2;
                $('#pointer').attr('width', width)
                        .attr('height', height).css('position', 'absolute')
                        .css('top', top)
                        .css('left', left)
                        .click(function () {
                            if (isRotating) {
                                return;
                            }
                            doLuckDraw(data);
                        });
                var canvas = document.getElementById('pointer');
                var ctx = canvas.getContext('2d');
                ctx.beginPath();
                ctx.drawImage(img, 0, 0, width, height);
                ctx.closePath();
            }

            /**
             * 绘制大转盘
             * @param ctx 画布上下文
             * @param pointX    转盘圆心X坐标
             * @param pointY    转盘圆心Y坐标
             * @param radius    转盘半径
             * @param data  奖品数据
             */
            function drawTurntable(pointX, pointY, radius, data) {
                if (!data || !$.isArray(data)) {
                    console.error("The data is error data");
                    throw new error("The data is not array or is empty");
                }
                var canvas = document.getElementById('turntable');
                var ctx = canvas.getContext('2d');

                var itemAngle = (2 * Math.PI) / data.length;
                var colors = ['rgb(255,235,140)', 'rgb(255,156,17)', 'rgb(255,213,112)'];
                for (var i = 0; i < data.length; i++) {
                    var startAngle = (i * itemAngle);
                    var endAngle = itemAngle + startAngle;
                    var color = i % 2 == 0 ? colors[0] : colors[1];
                    if (i == (data.length - 1) && (data.length % 2) != 0) {
                        color = colors[2];
                    }
                    drawSector(ctx, pointX, pointY, startAngle, endAngle, radius, color, data[i]);
                }
            }

            /**
             *
             *绘制大转盘中间的小圆
             */
            function drawCenterArc(ctx, pointX, pointY) {
                ctx.save();
                ctx.beginPath();
                ctx.arc(pointX, pointY, 50, 0, 2 * Math.PI, false);
                ctx.fillStyle = 'rgba(0,0,0,0.5)';
                ctx.closePath();
                ctx.fill();
                ctx.restore();
            }

            /**
             *
             * @param ctx   画布上下文
             * @param pointX    扇形圆心X坐标
             * @param pointY    扇形圆心Y坐标
             * @param sarteAngle    扇形开始角度
             * @param endAngle  扇形结束角度
             * @param radius    扇形半径
             * @param fillColor 填充颜色
             */
            function drawSector(ctx, pointX, pointY, startAngle, endAngle, radius, fillColor, item) {
                ctx.save();
                ctx.beginPath();
                // 位移到圆心，方便绘制
                ctx.translate(pointX, pointY);
                // 移动到圆心
                ctx.moveTo(0, 0);
                ctx.arc(0, 0, radius, startAngle, endAngle, false);
                ctx.fillStyle = fillColor;
                ctx.closePath();
                ctx.fill();
                ctx.restore();
                createCirText(ctx, pointX, pointY, startAngle, endAngle, item);
            }

            /**
             *
             * @param ctx
             * @param pointX
             * @param pointY
             * @param startAngle
             * @param endAngle
             * @param item
             */
            function createCirText(ctx, pointX, pointY, startAngle, endAngle, item) {
                ctx.font = "Bold 14px Arial"; // 设置字体
                ctx.textAlign = 'start';//文本水平对齐方式
                ctx.textBaseline = 'middle';//文本垂直方向，基线位置
                ctx.fillStyle = "#ac6110";// 设置填充颜色
                ctx.save();
                ctx.beginPath();
                ctx.translate(pointX, pointY);//平移到圆心
                ctx.rotate(startAngle + (endAngle - startAngle) / 2);
                ctx.fillText(item.title, 65, (endAngle - startAngle) / 2, 50);
                ctx.restore();
            }

            /**
             * 点击开始抽奖
             */
            function doLuckDraw(luckyItems) {
                isRotating = true;
                var angle = 0;
                rotateInterval = setInterval(function () {
                    $('#turntable').rotate(angle);
                    angle += 30;
                }, 30);
                $.ajax({
                    url: '${basePath}/api/user/' + userId + '/luckyDraw',
                    type: 'post',
                    dataType: 'text',
                    headers: {
                        userCode: userCode,
                        token: token
                    },
                    success: function (data) {
                        data = $.parseJSON(data);
                        if (data.code == 200) {
                            onLuckyDrawed(luckyItems, data.attribute);
                        } else {
                            onLoadDataError(data.msg);
                            clearInterval(rotateInterval);
                            isRotating = false;
                        }
                    },
                    error: function (ex) {
                        onLoadDataError('抽奖失败，请重试！');
                        clearInterval(rotateInterval);
                        isRotating = false;
                    }
                });
            }

            /**
             * 抽奖完成回调
             * @param luckyItems    //所有奖项
             * @param currentItem   //所中奖项
             */
            function onLuckyDrawed(luckyItems, currentItem) {
                clearInterval(rotateInterval);
                $('#turntable').rotate(0)
                var _itemAngle = 360 / luckyItems.length;
                for (var i = 0; i < luckyItems.length; i++) {
                    var item = luckyItems[i];
                    if (item.luckyDrawId != currentItem.luckyDraw.luckyDrawId) {
                        continue;
                    }
                    console.log(currentItem);
                    var targetAngle = 270 - (_itemAngle * i) - (_itemAngle / 2) + 360 * 5;
                    $('#turntable').rotate({
                        angle: 0,  //起始角度
                        animateTo: targetAngle,  //结束的角度
                        duration: 360 * 6, //转动时间
                        callback: function () {
                            isRotating = false;
                            var title = currentItem.luckyDraw.luckState ? '恭喜你中了' : 'Sorry';
                            alertModal({
                                title: title,
                                msg: currentItem.luckyDraw.title
                            })
                        }
                    });
                    break;
                }
            }

            function initMerchantLink() {
                $('.merchant-item').each(function (index, element) {
                    $(element).click(function () {
                        var merchantId = $(element).data('merchant-id');
                        var merchantName = $(element).data('merchant-name');
                        if (isAndroid()) {
                            JSInterface.navigationMerchant(merchantId, merchantName);
                        } else if (isIOS()) {
                            window.webkit.messageHandlers.navigationMerchant.postMessage(merchantId, merchantName);
                        }
                    });
                });
            }
        })(window, jQuery);

        $(function () {
            $('#activity-info-btn').click(function () {
                alertModal({
                    title: '活动规则',
                    msg: $(this).data('activity-info')
                });
            });
        });

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
    </script>
    <style type="text/css">
        .main-container {
            background: url("${basePath}/static/images/lucky_turntable_main_bg.png") center center no-repeat;
            background-size: cover;
        }

        .merchant-list-tip {
            margin: 10px auto;
            width: 70%;
        }

        .truntable-container {
            padding: 0;
            margin: 0;
        }

        #turntable-container {
            margin: 0 auto;
            position: relative;
            overflow: hidden;
            background: url("${basePath}/static/images/lucky_turntable_bg.png") center no-repeat;
            background-size: 100%;
        }

        #turntable-container #lucky-draw-container {
            margin: 0 auto;
            position: relative;
            overflow: hidden;
        }

        #turntable {
            display: block;
            margin: 0 auto;
        }

        #pointer {
            display: block;
            margin: 0 auto;
            overflow: hidden;
            cursor: pointer;
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

        .merchant-name {
            font-size: 12px;
            padding: 5px 0;
            color: white;
        }

        #activity-info-btn {
            background: rgb(255, 156, 17);
            font-weight: bold;
            color: white;
            font-size: 16px;
            padding: 10px auto;
            margin: 10px auto;
            min-width: 200px;
        }
    </style>
</head>
<body>
<div class="container-fluid main-container">
    <div class="row">
        <div class="col-xs-12">
            <div class="merchant-list-tip">
                <img src="${basePath}/static/images/lucky_turntable_merchant.png" class="img-responsive">
            </div>
        </div>
        <div class="col-xs-12">
            <div class="sponsor-merchant-group">
                <c:forEach items="${merchantList}" var="merchant">
                    <div class="merchant-item" data-merchant-id="${merchant.merchantId}"
                         data-merchant-name="${merchant.merchName}">
                        <img class="merchant-header" src="${merchant.coverImg.url}"/>
                        <%--<div class="merchant-name">${merchant.merchName}</div>--%>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 truntable-container">
            <div id="turntable-container">
                <div style="margin: 0 auto;">
                    <img width="80%" src="${basePath}/static/images/lucky_turntable_tip.png" class="img-responsive"/>
                </div>
                <div id="lucky-draw-container">
                    <canvas id="turntable">
                        对不起你的设备支持此功能！
                    </canvas>
                    <canvas id="pointer"></canvas>
                </div>
            </div>
            <div id="msg"></div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12" style="color: white;text-align: center">
            <button id="activity-info-btn" class="btn" data-activity-info="${lucyDrawActivity.desc}">
                活动规则
            </button>
        </div>
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
            </div>
        </div>
    </div>
</div>
</body>
</html>
