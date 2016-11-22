<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>挚合电商后台管理系统</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,Chrome=1">
    <meta name="renderer" content="webkit">
    <!--[if lt ie 9]>
    <script src="/static/js/html5shiv.min.js"></script>
    <![endif]-->
    <script src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <style type="text/css">
        html, body {
            overflow-x: hidden;
        }

        body {
            margin: 0px;
            padding: 0px;
            font-family: Microsoft Yahei;
            background-image: url('${basePath}/static/images/bg.jpg');
            background-size: cover;
            background-repeat: no-repeat;
        }

        @media screen and (max-width: 992px) {
            body {
                background-image: url('${basePath}/static/images/bg2.jpg');
                background-size: cover;
                background-repeat: no-repeat;
            }
        }

        .login-container {
            border: solid thin rgba(255, 255, 255, 0.7);
            background-color: rgba(51, 54, 71, 0.3);
            border-radius: 10px;
            box-shadow: 0px 0px 10px #ffffff;
            padding: 10px;
        }

        .login-left-contanier {

        }

        .login-right-container {
            border-left: solid thin white;
        }

        @media screen and (max-width: 992px) {
            .login-right-container {
                border-left: 0px;
                border-top: solid thin white;
            }
        }

        .web-title {
            text-align: center;
            height: 30px;
            line-height: 30px;
            font-size: 18px;
            color: white;
            margin: 15px 0px;
        }

        input::-webkit-input-placeholder {
            color: #ffffff;
        }

        input:-moz-placeholder {
            color: #ffffff;
        }

        ul {
            width: 90%;
            padding: 0px;
            margin: auto;
            list-style-type: none;
        }

        ul li {
            margin-top: 30px;
        }

        .user-code-li, .user-pwd-li {
            border-bottom: solid thin white;
        }

        #user-code, #user-pwd {
            border: none;
            padding-left: 40px;
            font-size: 15px;
            background-position: left;
            height: 24px;
            line-height: 24px;
            outline: none;
            color: white;
        }

        #user-code {
            background: url('${basePath}/static/images/user.png') no-repeat;
        }

        #user-pwd {
            background: url('${basePath}/static/images/pwd.png') no-repeat;
            background-position: left;
        }

        input:-webkit-autofill,
        input:-webkit-autofill:hover,
        input:-webkit-autofill:focus,
        input:-webkit-autofill:active {
            -webkit-transition-delay: 9999s;
            -webkit-transition: color 9999s ease-out, background-color 9999s ease-out;
        }

        #login, #register {
            display: block;
            width: 55%;
            margin: 0px auto 0px auto;
        }

    </style>
    <script type="text/javascript">
        $(function () {
            /*
             if (navigator.userAgent.indexOf('AppleWebKit') == -1) {
             window.location.href = '/notSupport.html';
             }
             */
            var clientHeight = document.body.clientHeight;
            var clientWidth = document.body.clientWidth;
            var rightContainerHeight = $('.login-right-container').css('height');
            if (clientWidth > 768) {
                $('.login-container').css('position', 'absolute');
                $('.login-container').css('top', clientHeight * 0.33 + 'px');
            } else {
                $('.login-container').css('position', 'relative');
                $('.login-container').css('margin-top', 30 + 'px');
                $('.login-container').css('margin-bottom', 30 + 'px');
            }
            $('.login-left-contanier').css('margin-top', parseInt(rightContainerHeight) * 0.3 + 'px');
            /*=====点击登录=====*/
            $('#login').click(function () {
                var adminCode = $('#user-code').val();
                var adminPwd = $('#user-pwd').val();
                console.log(adminCode)
                if ($.trim(adminCode) == "") {
                    $('#alert-msg').html('登录账号不能为空');
                    $('#alert-modal').modal({});
                    $('#user-code').focus();
                    return;
                }
                if ($.trim(adminPwd) == "") {
                    $('#alert-msg').html('登录密码不能为空');
                    $('#alert-modal').modal({});
                    $('#user-pwd').focus();
                    return;
                }
                $.ajax({
                    url: '/admin/login',
                    type: 'post',
                    data: {
                        adminCode: adminCode,
                        adminPwd: adminPwd
                    },
                    dataType: 'text',
                    success: function (data) {
                        data = $.parseJSON(data);
                        if (data.success) {
                            location.href = "/admin/index";
                        }
                        else {
                            $('#alert-msg').html(data.msg);
                            $('#alert-modal').modal({});
                        }
                    }
                });
            });
            /*=======响应回车键=======*/
            $(window).keyup(function (event) {
                if (event.keyCode == "13") {
                    document.getElementById("login").click();
                    return false;
                }
            });
            $('#user-code').focus();
        });
    </script>
</head>
<body>
<%--<div class="col-md-6 col-md-offset-3 col-sm-10 col-sm-offset-1" style="color: white">
    <h2 class="text-center">实淘《紧急公告》</h2>

    <p class="text-left">亲爱的实淘平台商户：</p>

    <p class="text-left">您好！</p>

    <p style="text-indent: 2em">
        实淘上线在即，为了广大商户及用户有更加流畅的平台体验，实淘将于2016年15日（0时—15时）进行系统维护，该时段内平台商户无法正常登陆管理系统，为您带来的不便，实淘深表抱歉！</p>

    <p class="text-right">挚合实淘</p>

    <p class="text-right">2016年6月14日</p>
</div>--%>

<div class=" login-container col-md-4 col-md-offset-4 col-xs-10 col-xs-offset-1">
    <div class="col-md-6 login-left-contanier">
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4 col-xs-6 col-xs-offset-3">
                <img class="img-responsive" src="${basePath}/static/images/logo.png"/>
            </div>
        </div>
        <div class="row web-title">挚合实淘后台管理系统</div>
    </div>
    <div class="col-md-6 login-right-container">
        <ul>
            <li class="user-code-li">
                <input id="user-code" placeholder="用户账号" autocomplete="off"/>
            </li>
            <li class="user-pwd-li">
                <input id="user-pwd" type="password" placeholder="密码" autocomplete="off"/>
            </li>
            <li style="margin: 0px auto 0px auto">
                <input type="image" src="${basePath}/static/images/login.png" id="login"/>
            </li>
            <li style="margin:0px auto 5px auto">
                <input type="image" src="${basePath}/static/images/rigster.png" id="register"
                       onclick="location.href='${basePath}/merchant/registAccount'"/>
            </li>
        </ul>
    </div>
    <div class="clearfix"></div>
</div>

<div id="alert-modal" class="modal fade" data-title-target="#alert-title" data-msg-target="#alert-msg">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title text-center" id="alert-title">错误提示</h4>
            </div>
            <div class="modal-body">
                <p id="alert-msg" style="text-align: center"></p>
            </div>
        </div>
    </div>
</div>

</body>
</html>
