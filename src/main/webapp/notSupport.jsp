<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/3/30
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <title>系统不支持该浏览器</title>
    <style>
        body {
            font-size: 12px;
            font-family: "Microsoft Yahei";
            padding: 0px;
            margin: 0px;
        }

        #main {
            width: 20%;
            margin-top: -20%;
            margin-left: -10%;
            position: absolute;
            left: 50%;
            top: 50%;
        }

        #header {
            width: 100%;
            height: 100px;
            text-align: center;
            line-height: 100px;
            color: #f57900;
            font-size: 25px;
        }

        /*button {*/
        /*height: 30px;*/
        /*line-height: 30px;*/
        /*border-radius: 5px;*/
        /*border: solid darkgray thin;*/
        /*text-align: center;*/
        /*background-color: white;*/
        /*}*/

        a:link {
            text-decoration: none;
        }

        a:visited {
            text-decoration: none;
        }

        a:hover {
            text-decoration: none;
        }

        a:active {
            text-decoration: none;
        }

    </style>
</head>
<body>
<div id="main">
    <div id="header">实淘友情提示</div>
    <div style="text-align: center">
        <div>
            <img src="${basePath}/static/images/warning.png" style="width: 15%;display: block;margin: auto">
        </div>
        <p style="text-align: center;font-size: 14px">
            系统检测到你的浏览器并不是谷歌浏览器（chrome）,为了你的使用方便和安全，请使用谷歌浏览器，
            你可以到官网进行下载，或者点击下方图标进行下载安装。对你造成的麻烦,我们深感抱歉！
        </p>

        <div style="width: 20%;margin: auto;">
            <a href="${basePath}/download/ChromeSetup.exe">
                <img src="${basePath}/static/images/chrome.png" style="width: 100%;border: none">
            </a>
        </div>
        <div style="margin: 20px">
            <button onclick="javascript:window.location.href='${basePath}/download/ChromeSetup.exe';">点击下载</button>
        </div>
    </div>
</div>
</body>
</html>
