<#assign base=request.contextPath />
<html>
<head>
    <base id="base" href="${base}">
    <meta charset="UTF-8">
    <title>商家展示页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${base}/static/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="${base}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/static/bootstrap/js/bootstrap.min.js"></script>
    <style type="text/css">
        .right-template {
            margin: 5px auto;
        }
    </style>
</head>
<body>

<div class="container" style="height: 120px;background-color: #2b542c;margin: 20px auto">

</div>

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">YDC小店</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">首页</a></li>
                <li><a href="#about">关于我们</a></li>
                <li><a href="#contact">店铺精选</a></li>
                <li><a href="#contact">联系我们</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div class="col-md-9" style="height: 1000px;background-color: #8c8c8c">

    </div>
    <div class="col-md-3" style="height: 1000px;background-color: green">
        <div class="right-template" style="height: 200px;background-color: bisque"></div>
        <div class="right-template" style="height: 200px;background-color: bisque"></div>
        <div class="right-template" style="height: 200px;background-color: bisque"></div>
        <div class="right-template" style="height: 200px;background-color: bisque"></div>
    </div>
</div>

<div class="container-fluid" style="margin-top: 20px">
    <address class="text-center">滇ICP备16003241号 电话热线：0871-67170168 公司地址：云南省昆明市官渡区民航路398号顺新时代大厦写字楼1004室</address>
</div>

</body>
</html>