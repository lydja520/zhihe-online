<%@ page import="cn.com.zhihetech.util.common.StringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/3/21
  Time: 11:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>注册商家账号</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="render" content="webkit">
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/jquery.form.js"></script>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrapValidator.min.js"></script>

    <style>
        body {
            font-family: "Microsoft Yahei";
            font-size: 12px;
            margin: 0px;
            padding: 0px;
        }

        .container {
            width: 60%;
            height: auto;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: 30px;
            padding: 0px;
            border-left: solid;
            border-bottom: solid;
            border-right: solid;
            border-width: 1px;
            border-color: #2d71b8;
            border-radius: 0px 0px 10px 10px;
        }

        #reg-title {
            width: 60%;
            height: 40px;
            margin-top: 15px;
            margin-left: auto;
            margin-right: auto;
            background-color: #2d71b8;
            line-height: 40px;
            color: #FFFFFF;
            font-size: 20px;
            text-align: center;
            border-radius: 10px 10px 0px 0px;
        }

        @media (max-width: 768px) {
            .container {
                width: 95%;
            }

            #reg-title {
                width: 95%;
            }
        }

        fieldset {
            width: 80%;
            margin-left: auto;
            margin-right: auto;
            color: #333;
            border-top: solid;
            border-width: 2px;
            border-color: #2d71b8;
        }

        legend {
            width: auto;
            height: 20px;
            padding-left: 10px;
            padding-right: 10px;
            line-height: 20px;
            text-align: center;
            font-weight: 400;
            border: none;
        }

    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#registerForm").bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    adminCode: {
                        message: '账号无效',
                        validators: {
                            notEmpty: {
                                message: '账号不可以为空'
                            },
                            stringLength: {
                                min: 3,
                                max: 30,
                                message: '长度最少为3个字符，最大为30个字符'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9_]+$/,
                                message: '账号只能由字母，下划线，和数字组成'
                            }
                        }
                    },
                    adminPwd: {
                        message: '密码无效',
                        validators: {
                            notEmpty: {
                                message: '密码不可以为空'
                            },
                            stringLength: {
                                min: 6,
                                max: 25,
                                message: '长度最少为6个字符'
                            },
                        }
                    },
                    adminPwdConfirm: {
                        message: '确认密码密码无效',
                        validators: {
                            notEmpty: {
                                message: '确认密码不可以为空'
                            },
                            stringLength: {
                                min: 6,
                                max: 25,
                                message: '长度最少为6个字符'
                            }
                        }
                    }
                }
            }).on('success.form.bv', function (e) {
                // Prevent form submission
                e.preventDefault();
                // Get the form instance
                var $form = $(e.target);
                // Get the BootstrapValidator instance
                var bv = $form.data('bootstrapValidator');
                // Use Ajax to submit form data
                /*                $.post($form.attr('action'), $form.serialize(), function(result) {
                 console.log(result);
                 }, 'json');*/
                var pwd = $('#adminPwd').val();
                var pwdConfirm = $('#adminPwdConfirm').val();
                if (pwd != pwdConfirm) {
                    $('#tip').show();
                    return;
                }
                $form.ajaxSubmit({
                    url: '${basePath}/merchant/registAccount/submit',
                    type: 'post',
                    dataType: 'text',
                    success: function (info) {
                        info = $.parseJSON(info);
                        if (info.success) {
                            window.location.href = '${basePath}/merchant/registBasicInfo';
                            return;
                        }
                        if (!info.success) {
                            $('#alert-msg').html(info.msg);
                            $('#alert-modal').modal({});
                        }
                    },
                    timeout: 3000
                });
            });

            $('#tip').hide();
            $('.tip-click').click(function () {
                $('#tip').hide();
            });
        });

    </script>
</head>
<body>
<div class="title" id="reg-title">【实淘】商家注册</div>
<div class="container">
    <form role="form" id="registerForm">
        <!--注册账号-->
        <fieldset style="margin-top: 20px">
            <legend><span style="font-size:18px;color: #2d71b8;font-weight: bold">注册账号</span></legend>
            <div class="row">
                <div class="col-md-4 col-md-offset-4 form-group">
                    <label for="adminCode"><span style="color: red;font-weight: normal">* </span>账号</label>
                    <input type="text" class="form-control tip-click" id="adminCode" name="adminCode"
                           placeholder="请输入名称">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 col-md-offset-4 form-group">
                    <label for="adminPwd"><span style="color: red;font-weight: normal">* </span>密码</label>
                    <input type="password" class="form-control tip-click" id="adminPwd" name="adminPwd"
                           placeholder="请输入密码">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 col-md-offset-4 form-group">
                    <label for="adminPwdConfirm"><span style="color: red;font-weight: normal">* </span>确认密码</label>
                    <input type="password" class="form-control tip-click" id="adminPwdConfirm" name="adminPwdConfirm"
                           placeholder="请输入确认密码">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 col-md-offset-4 form-group">
                    <div id="tip" style="color: red">密码与确认密码不一致</div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 col-md-offset-4 ">
                    <button id="next-btn" type="submit" class="btn btn-default">下一步</button>
                </div>
            </div>
        </fieldset>
    </form>
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
