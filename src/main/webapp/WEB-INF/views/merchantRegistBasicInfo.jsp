<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<title>商户注册</title>
<head>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="render" content="webkit">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/jquery.form.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrapValidator.min.js"></script>
    <style>
        * {
            font-family: "Microsoft Yahei";
            font-size: 12px;
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

        textarea {
            resize: none;
        }

        .row {
            display: block;
            margin: auto;
        }

        ul, li {
            margin: 0px;
            padding: 0px;
            list-style: none;
            font-size: 14px;
        }

    </style>
    <script type="text/javascript">
        $(function () {
            $('#featuredBlock').removeAttr('disabled');

            $("#registerForm").bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    merchName: {
                        validators: {
                            notEmpty: {
                                message: '商家名不能为空'
                            },
                            stringLength: {
                                min: 2,
                                max: 30,
                                message: '商家名长度不小于2个字符，不大于30个字符'
                            }
                        }
                    },
                    merchTell: {
                        validators: {
                            notEmpty: {
                                message: '企业联系电话不能为空'
                            }
                        }
                    },
                    address: {
                        validators: {
                            notEmpty: {
                                message: '地址不能为空'
                            }
                        }
                    },
                    /*                 alipayCode: {
                     validators: {
                     notEmpty: {
                     message: '支付宝账号不能为空'
                     }
                     }
                     },*/
                    /*                    licenseCode: {
                     validators: {
                     notEmpty: {
                     message: '工商执照注册码不能为空'
                     },
                     regexp: {
                     regexp: /^[0-9a-zA-Z]*$/,
                     message: '输入的不是有效的工商注册码，只能是数字或数字和字母的组合'
                     }
                     }
                     },*/
                    categoriesInfo: {
                        validators: {
                            notEmpty: {
                                message: '请选择一个经营范围'
                            }
                        }
                    },
                    /*                    contactEmail: {
                     message: '邮箱地址无效',
                     validators: {
                     notEmpty: {
                     message: '联系人电子邮箱不能为空'
                     }
                     }
                     },*/
                    /*                    contactID: {
                     message: '联系人身份证号无效',
                     validators: {
                     notEmpty: {
                     message: '联系人身份证号不能为空'
                     },
                     regexp: {
                     regexp: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
                     message: '请输入正确的身份证号'
                     }
                     }
                     },*/
                    contactMobileNO: {
                        message: '电话号码无效',
                        validators: {
                            notEmpty: {
                                message: '联系人手机号码不能为空'
                            },
                            regexp: {
                                regexp: /^[0-9]{11}$/,
                                message: '输入的不是有效的电话号码'
                            }
                        }
                    },
                    contactName: {
                        message: '企业联系人姓名无效',
                        validators: {
                            notEmpty: {
                                message: '企业联系人姓名不能为空'
                            }
                        }
                    },
                    /*                    contactPartAndPositon: {
                     message: '填入的值无效',
                     validators: {
                     notEmpty: {
                     message: '联系人部门与职位不能为空'
                     }
                     }
                     },*/
                    emplyCount: {
                        message: '填入的值无效',
                        validators: {
                            notEmpty: {
                                message: '此项不能为空'
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
                $form.ajaxSubmit({
                    url: '${basePath}/merchant/registBasicInfo/submit',
                    type: 'post',
                    dataType: 'text',
                    success: function (info) {
                        info = $.parseJSON(info);
                        if (info.success) {
                            window.location.href = '${basePath}/merchant/registImgForm';
                        }
                        if (!info.success) {
                            $('#alert-msg').html(info.msg);
                            $('#alert-modal').modal({});
                        }
                    },
                    timeout: 3000
                });
            });
        });

        function chooseOne(value) {
            if (value != 'featuredBlock') {
                $('#featuredBlock').attr('disabled', 'true');
            } else {
                $('#featuredBlock').removeAttr('disabled');
            }
        }
    </script>
</head>
<body>
<div class="title" id="reg-title">【实淘】商家注册</div>
<div class="container">
    <!--基本信息表单-->
    <form role="form" id="registerForm">
        <!--商家基本信息-->
        <fieldset style="margin-top: 20px">
            <legend><span style="font-size:18px;color: #2d71b8;">基本信息</span></legend>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="merchName"><span style="color: red;font-weight: normal">* </span>商家名称</label>
                        <input type="text" class="form-control" id="merchName" name="merchName"
                               placeholder="请输入商家名">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="merchTell"><span
                                style="color: red;font-weight: normal">* </span>企业联系电话（固话请加区号）</label>
                        <input type="text" class="form-control" id="merchTell" name="merchTell"
                               placeholder="请输入企业联系电话">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="address"><span style="color: red;font-weight: normal">* </span>详细地址（实体店）详细地址</label>
                        <input type="text" class="form-control" id="address" name="address"
                               placeholder="请输入地址">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="emplyCount"><span style="color: red;font-weight: normal">* </span>企业规模（人）</label>
                        <input type="number" class="form-control" value="10" id="emplyCount" name="emplyCount"
                               placeholder="请输入企业规模">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="licenseCode">工商执照注册码<span style="color: red;font-weight: normal">(选填)</span></label>
                        <input type="text" class="form-control" id="licenseCode" name="licenseCode"
                               placeholder="请输入工商营业执照编码">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="alipayCode">支付宝账号<span style="color: red;font-weight: normal"> (选填)</span></label>
                        <input type="text" class="form-control" id="alipayCode" name="alipayCode"
                               placeholder="请输入支付宝账号">
                    </div>
                </div>
            </div>
        </fieldset>
        <!--商家经营范围-->
        <fieldset>
            <legend><span style="font-size:18px;color: #2d71b8;">经营范围</span></legend>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>经营范围（可多选）</label>

                        <div style="border: solid thin #CCCCCC;border-radius: 5px;max-height: 200px;overflow-y: auto;">
                            <ul>
                                <c:forEach items="${requestScope.goodsAttSets}" var="goodsAttSet">
                                    <li class="col-md-3 col-sm-6">
                                        <input type="checkbox" value="${goodsAttSet.goodsAttSetId}"
                                               name="goodsAttSet">${goodsAttSet.goodsAttSetName}
                                    </li>
                                </c:forEach>
                                <div class="clearfix"></div>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
        <!--特色街区，购物中心，特色店,vip店-->
        <fieldset style="margin-top: 20px">
            <legend><span style="font-size:18px;color: #2d71b8;">实体店所属类型</span></legend>
            <div class="row">
                <div class="col-md-6">
                    <div class="btn-group" style="margin: 0px auto 15px auto">
                        <%--
                            <label>
                                <input type="radio" name="chooseAdressType" value="shoppingCenter"
                                       onclick="chooseOne(this.value)" checked/>购物中心
                            </label>
                            --%>
                        <label>
                            <input type="radio" name="storeType" value="featuredBlock" checked
                                   onclick="chooseOne(this.value)"/>特色街区
                        </label>
                        &nbsp;&nbsp;
                        <label>
                            <input type="radio" name="storeType" value="featuredStore"
                                   onclick="chooseOne(this.value)"/>优"+"店
                        </label>
                        &nbsp;&nbsp;
                        <%--                        <label>
                                                    <input type="radio" name="storeType" value="vipStore"
                                                           onclick="chooseOne(this.value)"/>VIP专区
                                                </label>--%>
                    </div>
                </div>
            </div>
            <div class="row">
                <%--
                <div class="col-md-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的购物中心</label>
                        <select id="inShoppingMall" class="form-control" style="font-size: 12px"
                                name="shoppingCenter.scId">
                            <c:forEach items="${shoppingCenterList}" var="shoppingCenter">
                                <option value="${shoppingCenter.scId}">${shoppingCenter.scName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                --%>
                <div class="col-md-6">
                    <div class="form-group">
                        <label><span style="color: red;font-weight: normal">* </span>选择所在的街区</label>
                        <select id="featuredBlock" class="form-control" style="font-size: 12px"
                                name="featuredBlock.fbId">
                            <c:forEach items="${featuredBlockList}" var="featuredBlock">
                                <option value="${featuredBlock.fbId}">${featuredBlock.fbName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </fieldset>
        <!--商家详细信息-->
        <fieldset>
            <legend><span style="font-size:18px;color: #2d71b8;">简介</span></legend>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="merchantDetails">商家简介<span
                                style="color: red;font-weight: normal">(选填)</span></label>
                    <textarea placeholder="输入商家简介..." class="form-control" rows="3" id="merchantDetails"
                              name="merchantDetails"></textarea>
                    </div>
                </div>
            </div>
        </fieldset>
        <!--联系人信息-->
        <fieldset>
            <legend><span style="font-size:18px;color: #2d71b8;">联系人信息</span></legend>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="contactName"><span style="color: red;font-weight: normal">* </span>联系人</label>
                        <input type="text" class="form-control" id="contactName" name="contactName"
                               placeholder="请输联系人姓名">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="contactID">身份证号<span style="color: red;font-weight: normal">(选填)</span></label>
                        <input type="text" class="form-control" id="contactID" name="contactID"
                               placeholder="请输入联系人身份证号">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="contactMobileNO"><span style="color: red;font-weight: normal">* </span>手机号码</label>
                        <input type="text" class="form-control" id="contactMobileNO" name="contactMobileNO"
                               placeholder="请输入联系人手机号码">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="contactEmail">邮箱<span style="color: red;font-weight: normal">(选填)</span></label>
                        <input type="email" class="form-control" id="contactEmail" name="contactEmail"
                               placeholder="请输入联系人邮箱">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="contactPartAndPositon">联系人部门与职位<span
                                style="color: red;font-weight: normal">(选填)</span></label>
                        <input type="text" class="form-control" id="contactPartAndPositon"
                               name="contactPartAndPositon"
                               placeholder="请输入具体职称">
                    </div>
                </div>
            </div>
            <button id="next-btn" type="submit" class="btn btn-default" style="margin-left:15px">下一步</button>
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
