<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/3/17
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>投诉维权</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var u = navigator.userAgent;
            var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
            var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
            $('.button-phone').each(function (index, element) {
                $(element).click(function () {
                    var phoneNumber = $(this).data('value');
                    if (isAndroid) {
                        JSInterface.callPhone(phoneNumber);
                    }
                    if (isiOS) {
                        window.webkit.messageHandlers.callPhone.postMessage(phoneNumber);
                    }
                });
            });
        });
    </script>
    <style>
        body {
            margin: 0px;
            padding: 0px;
            font-size: 12px;
            background-color: #f4f8fb;
            font-family: "Microsoft Yahei";
        }

        .header img {
            width: 100%;
        }

        ul {
            margin: 0px;
            padding: 0px;
            list-style-type: none;
        }

        ul li {
            width: 92%;
            margin: auto;
            height: 75px;
            border-bottom: solid thin #dfe0e1;
        }

        .list-left {
            width: 65%;
            height: 75px;
            float: left;
            font-size: 16px;
        }

        .left-content {
            margin: 20px auto auto 0px;
        }

        .call-title {
            font-size: 16px;
            font-weight: bolder;
        }

        .call-number {
            font-size: 14px;
            color: #636363;
            margin-top: 5px;
            font-family: Arial;
        }

        .list-right {
            width: 35%;
            height: 75px;
            float: right;
        }

        .right-content {
            width: 90%;
            margin: auto;
            margin-top: 22px;
            text-align: right;
        }

        .right-content img {
            width: 90%;
            margin: 0px;
            padding: 0px;
        }

        .clear {
            clear: both;
        }
    </style>
<body>
<div class="header">
    <img src="${basePath}/static/images/rightprotect_heade.png"/>
</div>
<div class="main-body">
    <ul>


        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">挚合实淘</div>
                    <div class="call-number">67170168</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="67170168" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>


        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">消费者投诉</div>
                    <div class="call-number">12315</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12315" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">质量监督投诉</div>
                    <div class="call-number">12365</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12365" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">价格举报</div>
                    <div class="call-number">12358</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12358" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">国家旅游服务</div>
                    <div class="call-number">12301</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12301" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">邮政行业消费者投诉</div>
                    <div class="call-number">12305</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12305" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">文化市场举报</div>
                    <div class="call-number">12318</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12318" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">保监会投诉</div>
                    <div class="call-number">12378</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12378" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">食品药品安全投诉</div>
                    <div class="call-number">12331</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12331" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">工信部投诉</div>
                    <div class="call-number">12300</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12300" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
        <li>
            <div class="list-left">
                <div class="left-content">
                    <div class="call-title">网络不良与垃圾信息举报</div>
                    <div class="call-number">12321</div>
                </div>
            </div>
            <div class="list-right">
                <div class="right-content">
                    <img class="button-phone" data-value="12321" src="${basePath}/static/images/calphone.png">
                </div>
            </div>
            <div class="clear"></div>
        </li>
    </ul>
</div>
</body>
</html>
