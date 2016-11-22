<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-7-5
  Time: 下午4:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <c:set var="root" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${root}/static/core/jquery.min.js"></script>
    <title>商品详细信息</title>
    <script type="text/javascript">

        function getStyle(obj, attr) {
            if (obj.currentStyle) {
                return obj.currentStyle[attr];
            }
            else {
                return getComputedStyle(obj, false)[attr];
            }
        }
        function startMove(obj, attr, iTarget) {
            clearInterval(obj.timer);
            obj.timer = setInterval(function () {
                var iCur = 0;

                if (attr == 'opacity') {
                    iCur = parseInt(parseFloat(getStyle(obj, attr)) * 100);
                }
                else {
                    iCur = parseInt(getStyle(obj, attr));
                }

                var iSpeed = (iTarget - iCur) / 8;
                iSpeed = iSpeed > 0 ? Math.ceil(iSpeed) : Math.floor(iSpeed);

                if (iCur == iTarget) {
                    clearInterval(obj.timer);
                }
                else {
                    if (attr == 'opacity') {
                        obj.style.filter = 'alpha(opacity:' + (iCur + iSpeed) + ')';
                        obj.style.opacity = (iCur + iSpeed) / 100;
                    }
                    else {
                        obj.style[attr] = iCur + iSpeed + 'px';
                    }
                }
            }, 30)
        }
        function getByClass(oParent, sClass) {
            var aEle = document.getElementsByTagName('*');
            var i = 0;
            var aResult = [];

            for (i = 0; i < aEle.length; i++) {
                if (aEle[i].className == sClass) {
                    aResult.push(aEle[i]);
                }
            }

            return aResult;
        }

        window.onload = function () {
            var oDiv = document.getElementById('playeimages');
            var oBtnPrev = getByClass(oDiv, 'prev')[0];
            var oBtnNext = getByClass(oDiv, 'next')[0];
            var oMarkLeft = getByClass(oDiv, 'mark_left')[0];
            var oMarkRight = getByClass(oDiv, 'mark_right')[0];
            var oSmallUl = getByClass(oDiv, 'small_pic')[0].getElementsByTagName('ul')[0];
            var aSmallLi = oSmallUl.getElementsByTagName('li');
            var oBigUl = getByClass(oDiv, 'big_pic')[0];
            var aBigLi = oBigUl.getElementsByTagName('li');
            var iNow = 0;
            var iMinZindex = 2;
            var i = 0;

            oSmallUl.style.width = aSmallLi.length * aSmallLi[0].offsetWidth + 'px';

            //上面的左右按钮
            oBtnPrev.onmouseover = oMarkLeft.onmouseover = function () {
                startMove(oBtnPrev, 'opacity', 100);
            }

            oBtnPrev.onmouseout = oMarkLeft.onmouseout = function () {
                startMove(oBtnPrev, 'opacity', 0);
            }

            oBtnNext.onmouseover = oMarkRight.onmouseover = function () {
                startMove(oBtnNext, 'opacity', 100);
            }

            oBtnNext.onmouseout = oMarkRight.onmouseout = function () {
                startMove(oBtnNext, 'opacity', 0);
            }

            //小图点击，大图显示
            for (i = 0; i < aSmallLi.length; i++) {
                aSmallLi[i].index = i;
                aSmallLi[i].onmouseover = function () {
                    startMove(this, 'opacity', 100);
                }
                aSmallLi[i].onmouseout = function () {
                    if (this.index != iNow) {
                        startMove(this, 'opacity', 60);
                    }
                }

                aSmallLi[i].onclick = function () {
                    if (this.index == iNow)return;
                    iNow = this.index;

                    tab();
                }

                function tab() {
                    for (i = 0; i < aSmallLi.length; i++) {
                        startMove(aSmallLi[i], 'opacity', 60);
                    }
                    startMove(aSmallLi[iNow], 'opacity', 100);
                    aBigLi[iNow].style.zIndex = iMinZindex++;
                    aBigLi[iNow].style.height = 0;

                    startMove(aBigLi[iNow], 'height', oBigUl.offsetHeight);

                    if (iNow == 0) {
                        startMove(oSmallUl, 'left', 0);
                    }
                    else if (iNow == aSmallLi.length - 1) {
                        startMove(oSmallUl, 'left', -(iNow - 2) * aSmallLi[0].offsetWidth);
                    }
                    else {
                        startMove(oSmallUl, 'left', -(iNow - 1) * aSmallLi[0].offsetWidth);
                    }
                }

                oBtnPrev.onclick = function () {
                    iNow--;
                    if (iNow == -1) {
                        iNow = aSmallLi.length - 1;
                    }

                    tab();
                }

                oBtnNext.onclick = function () {
                    iNow++;
                    if (iNow == aSmallLi.length) {
                        iNow = 0;
                    }

                    tab();
                }
            }
        }

        $(function () {
            $('#previcw-goodsSku').load('${root}/admin/api/goods/${goods.goodsId}/sku/list2');
        });
    </script>
    <style type="text/css">
        html, body {
            font-size: 12px;
            font-family: "微软雅黑";
            padding: 0;
            margin: 0;
        }

        .info-list div {
            margin: 3px auto;
            padding: 2px auto;
        }

        .info-title {
            width: 20%;
            font-size: 14px;
            float: left;
        }

        .info-content {
            width: 80%;
            font-size: 14px;
            float: right;
        }

        li {
            display: inline;
            margin: 0px 4px;
        }

        li a {
            border: solid thin darkgrey;
            padding: 0px 5px;
        }

        body ul {
            padding: 0;
            margin: 0;
        }

        li {
            list-style: none;
            margin: 0px;
            padding: 0px
        }

        img {
            border: 0;
        }

        .play {
            width: 400px;
            height: 395px;
            background: #333;
            font: 12px Arial;
            margin: 0 auto;
            overflow-x: hidden
        }

        .big_pic {
            height: 300px;
            overflow: hidden;
            border-bottom: 1px solid #ccc;
            background: white;
            position: relative;
        }

        .big_pic li {
            height: 300px;
            overflow: hidden;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 2;
            background: url(/static/images/playImgs/loading.gif) no-repeat center center;
        }

        .mark_left {
            width: 200px;
            height: 320px;
            position: absolute;
            left: 0;
            top: 0;
            background: #eef4ff;
            filter: alpha(opacity:0);
            opacity: 0;
            z-index: 3000;
        }

        .mark_right {
            width: 200px;
            height: 320px;
            position: absolute;
            left: 200px;
            top: 0;
            background: green;
            filter: alpha(opacity:0);
            opacity: 0;
            z-index: 3000;
        }

        .big_pic .prev {
            width: 60px;
            height: 60px;
            background: url(/static/images/playImgs/btn.gif) no-repeat;
            position: absolute;
            top: 130px;
            left: 10px;
            z-index: 3001;
            cursor: pointer;
            filter: alpha(opacity:0);
            opacity: 0;
        }

        .big_pic .next {
            width: 60px;
            height: 60px;
            background: url(/static/images/playImgs/btn.gif) no-repeat 0 -60px;
            position: absolute;
            top: 130px;
            right: 10px;
            z-index: 3001;
            cursor: pointer;
            filter: alpha(opacity:0);
            opacity: 0;
        }

        .big_pic .text {
            position: absolute;
            left: 10px;
            top: 302px;
            z-index: 3000;
            color: #ccc;
        }

        .big_pic .length {
            position: absolute;
            right: 10px;
            bottom: 4px;
            z-index: 3000;
            color: #ccc;
        }

        .big_pic .bg {
            width: 400px;
            height: 25px;
 /*           background: #000;*/
            filter: alpha(opacity=60);
            opacity: 0.6;
            position: absolute;
            z-index: 2999;
            bottom: 0;
            left: 0;
        }

        .small_pic {
            width: 100%;
            height: 80px;
            position: relative;
            top: 7px;
            left: 10px;
            overflow: hidden;
        }

        .small_pic ul {
            height: 80px;
            position: absolute;
            top: 0;
            left: 0;
        }

        .small_pic li {
            width: 90px;
            height: 80px;
            float: left;
            padding-right: 10px;
            background: url(/static/images/playImgs/loading.gif) no-repeat center center;
            cursor: pointer;
            filter: alpha(opacity=60);
            opacity: 0.6;
        }

        .small_pic img {
            width: 90px;
            height: 80px;
        }

        .info-content img{
            border-radius: 7px;
        }

        .clear {
            clear: both;
        }

    </style>
</head>
<body>

<div id="wrap" style="width:700px;margin: auto">

    <div class="goods-name"
         style="font-size: 16px;color:#ff4500;width:95%;font-weight: bold;margin: 10px auto 5px auto;text-align: center">
        商品的基本信息
    </div>
    <!--商品封面图和基本信息-->
    <div style="width: 100%;border: solid thin #a0c6e5;padding: 2px;">
        <!--商品轮播图-->
        <div id="playimages" class="play" style="width: 50%;background-color:#eef4ff;float: left">
            <ul class="big_pic">
                <div class="prev"></div>
                <div class="next"></div>
                <!--<div class="text">加载图片说明……</div>
                <div class="length">计算图片数量……</div>-->
                <a class="mark_left" href="javascript:;"></a>
                <a class="mark_right" href="javascript:;"></a>
                <!--<div class="bg"></div>	-->
                <c:forEach items="${goodsBanners}" var="goodsBanner">
                    <li><img style="width: 100%" src="${goodsBanner.imgInfo.url}"></li>
                </c:forEach>
            </ul>
            <div class="small_pic">
                <ul>
                    <%-- <li style="filter: 100; opacity: 1;"><img src="images/120617_pic6.jpg"/></li>--%>
                    <li style="filter: 100; opacity: 1;"><img src="${goodsBanners.get(0).imgInfo.url}"></li>
                    <c:set var="count" value="${0}"></c:set>
                    <c:forEach items="${goodsBanners}" var="goodsBanner">
                        <c:if test="${count != 0}">
                            <li><img src="${goodsBanner.imgInfo.url}"></li>
                        </c:if>
                        <c:set var="count" value="${count+1}"></c:set>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <!--商品轮播图End-->
        <div style="float: right;width: 45%;">
            <div style="margin-left: 10px" class="info-list">
                <!--商品名-->
                <div class="goods-name" style="font-size: 16px;font-weight: bold;margin-bottom: 10px;text-align: left">
                    ${goods.goodsName}
                </div>
                <div class="info-title">封面图:</div>
                <div class="info-content">
                    <img src="${goods.coverImg.url}" style="width: 70px;height: 70px">
                </div>
                <div class="clear"></div>
                <div class="info-title">分类:</div>
                <div class="info-content">${requestScope.goods.goodsAttributeSet.goodsAttSetName}</div>
                <div class="clear"></div>
                <div class="info-title">价格:</div>
                <div class="info-content">${goods.minPrice}元~${goods.maxPrice}元</div>
                <div class="clear"></div>
                <div class="info-title">配送:</div>
                <div class="info-content">${requestScope.goods.carriageMethod}</div>
                <div class="clear"></div>
                <div class="info-title">邮费:</div>
                <div class="info-content">${requestScope.goods.carriage}元</div>
                <div class="clear"></div>
                <div class="info-title">可自取:</div>
                <c:if test="${requestScope.goods.isPick == true}">
                    <div class="info-content">是</div>
                </c:if>
                <c:if test="${requestScope.goods.isPick == false}">
                    <div class="info-content">否</div>
                </c:if>
                <div class="clear"></div>
                <div class="info-title">销量:</div>
                <div class="info-content"> ${requestScope.goods.volume} </div>
                <div class="info-title">库存量:</div>
                <div class="info-content">${requestScope.goods.currentStock}</div>
                <div class="clear"></div>
                <div class="info-title">描述:</div>
                <div class="info-content"
                     style="max-height: 85px;overflow-y: auto">${requestScope.goods.goodsDesc}</div>
                <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>

    <div class="goods-name"
         style="font-size: 16px;color:#ff4500;width:95%;font-weight: bold;margin: 5px auto;text-align: center">
        商品的库存和价格
    </div>

    <div id="previcw-goodsSku"></div>

    <div class="goods-name"
         style="font-size: 16px;color:#ff4500;width:95%;font-weight: bold;margin: 5px auto;text-align: center">
        商品的详情信息
    </div>
    <!--商品详情图-->
    <div style="margin-top: 10px">
        <c:forEach items="${goodsDetails}" var="goodsDetail">
            <div style="width: 100%;margin-bottom: 2px"><img
                    src="${goodsDetail.imgInfo.url}"
                    style="width: 100%;height: auto">
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
