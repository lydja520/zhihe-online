<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/5/20
  Time: 11:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>实淘APP下载</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="render" content="webkit">
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <style>
        html, body {
            overflow: hidden;
        }

        #banner {
            position: absolute;
            width: 1920px;
            height: 1080px;
            left: 50%;
            margin-left: -960px;
            z-index: -10;
            background: url("/static/images/download_bg.jpg");
            background-repeat: no-repeat;
            background-size: 100%;
        }

        @media screen and (max-width: 992px) {
            #banner {
                position: absolute;
                width: 100%;
                left: 0px;
                margin-left: 0px;
                z-index: -10;
                background: url("/static/images/download_bg-sub.jpg");
                background-repeat: no-repeat;
                background-size: 100%;
            }
        }

        .code-img{
            padding-bottom: 30px;
        }

    </style>
    <script type="text/javascript">
        $(function () {
            var screenHeight = $(window).height();
            $('#download-container').css('margin-top', screenHeight * 0.2);
            var h = document.documentElement ? document.documentElement.clientHeight : document.body.clientHeight;
            var w = document.documentElement ? document.documentElement.clientHeight : document.body.clientWidth;
            if (w < 992) {
                $('#banner').css('height', h + 'px');
            }
        });
    </script>
</head>
<body>
<div id="banner"></div>
<div class="container-fluid" id="download-container">
    <div class="row">
        <div class="col-md-6 code-img">
            <div class="col-md-4 col-md-offset-7 col-xs-6 col-xs-offset-3">
                <img src="${basePath}/static/images/download.png" class="img-responsive">
            </div>
        </div>
        <div class="col-md-6 code-img">
            <div class="col-md-4 col-md-offset-1 col-xs-6 col-xs-offset-3">
                <img src="${basePath}/static/images/download.png" class="img-responsive">
            </div>
        </div>
        <div></div>
    </div>
</div>
</body>
</html>
