<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/12/1
  Time: 9:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<title>商户注册</title>
<head>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="render" content="webkit">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrapValidator.css">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrapValidator.js"></script>
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

        legend span {
            font-size: 18px;
            color: #2d71b8;
        }

        button {
            clear: both;
        }

        label {
            font-size: 12px;
            display: block;
        }

        .upload-progress {
            height: 12px;
            line-height: 12px;
            font-size: 12px;
        }

    </style>
    <script type="text/javascript">
        var imgInfoIds = {};
        function merchant_img(img_name, img_id) {
            imgInfoIds[img_name] = img_id;
        }
        $(function () {
            $.each($('.select_btn'), function (index, element) {
                var keyName = $(element).data('key');
                var pickbutton = $(element).attr('id');
                var filename = $(element).prev();
                initUploader({
                    pickbutton: pickbutton,
                    onFileAdd: function (file) {
                        filename.val(file.name);
                    },
                    onError: function (file, err, errTip) {

                    },
                    onBeforeUpload: function (up, file) {
                        total = up.total.size;
                        loaded = up.total.loaded;
                        progress = parseInt(loaded / total * 100);
                        $(element).next().next()[0].innerHTML = ' 正在上传...';
                    },
                    onUploaded: function (up, file, info) {
                        if (keyName) {
                            console.log(info);
                            merchant_img(keyName, info.imgInfoId);
                            $(element).next().children().attr('src', info.url);
                            $(element).next().next()[0].innerHTML = ' 上传完成';
                        }
                    },
                    onComplete: function () {

                    }
                });
            });

            $('#submit').click(function () {
                if (Object.keys(imgInfoIds).length >= 2) {
                    $.ajax({
                        url: '${basePath}/merchant/registImgForm/submit',
                        data: imgInfoIds,
                        dataType: 'text',
                        type: 'post',
                        success: function (data) {
                            data = $.parseJSON(data)
                            if (data.success) {
                                window.location.href = "${basePath}/merchant/rigistAccount/fiveSecondsJump";
                            } else {
                                $('#alert-msg').html(data.msg);
                                $('#alert-modal').modal({});
                            }
                        },
                        error: function () {
                            $('#alert-msg').html("系统出现异常，请与管理员联系！");
                            $('#alert-modal').modal({});
                        }
                    });
                } else {
                    $('#alert-msg').html("你有未上传的图片！");
                    $('#alert-modal').modal({});
                }
            });
        });
    </script>
</head>
<body>
<div>
    <div class="title" id="reg-title">【实淘】商家注册</div>
    <div class="container">
        <fieldset style="margin-top: 20px">
            <legend>
                <span style="font-size:20px">提交有关审核图片</span>
            </legend>
            <table cellspacing="10" style="border-collapse: separate;border-spacing: 15px;width: 100%">
                <tr>
                    <td width="50%">
                        <label><span style="color: red;font-weight: normal">* </span>商家头像:(建议比例540*540)</label>
                        <input class="fileName form-control" type="text" placeholder="图片名"
                               style="width: 50%;display: inline">
                        <button id="select_cover_img" type="button" class="select_btn btn btn-default random"
                                data-key="coverImg.imgInfoId">
                            选择图片
                        </button>
                        <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top:10px;">
                            <img src="${basePath}/static/images/preview.jpg" style="width: 100%;height: 100%">
                        </div>
                        <div class="upload-progress"></div>
                    </td>
                    <td width="50%">
                        <label><span style="color: red;font-weight: normal">* </span>店铺顶部图:店铺顶部图:(建议比例1080*486)</label>
                        <input class="fileName form-control" type="text" style="width: 50%;display: inline"
                               placeholder="图片名">
                        <button id="select_headerImg" type="button" class="select_btn btn btn-default random"
                                data-key="headerImg.imgInfoId">
                            选择图片
                        </button>
                        <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top:10px">
                            <img src="${basePath}/static/images/preview.jpg" style="width: 100%;height: 100%">
                        </div>
                        <div class="upload-progress"></div>
                    </td>
                </tr>
                <tr>
                    <%--                    <td>
                                            <label><span style="color: red;font-weight: normal">* </span>加盖公章的申请认证公函(与商家纠纷事件裁定等)照片:</label>
                                            <input class="fileName form-control" type="text" style="width: 50%;display: inline"
                                                   placeholder="图片名">
                                            <button id="select_applyLetterPhoto" type="button" class="select_btn btn btn-default random"
                                                    data-key="applyLetterPhoto.imgInfoId">
                                                选择图片
                                            </button>
                                            <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top:10px">
                                                <img src="${basePath}/static/images/preview.jpg" style="width: 100%;height: 100%">
                                            </div>
                                            <div class="upload-progress"></div>
                                        </td>--%>
                    <td>
                        <label>组织机构代码证原件照片:<span style="color: red;font-weight: normal"> (没有可不上传)</span></label>
                        <input class="fileName form-control" type="text" style="width: 50%;display: inline"
                               placeholder="图片名">
                        <button id="select_orgPhoto" type="button" class="select_btn btn btn-default random"
                                data-key="orgPhoto.imgInfoId">
                            选择图片
                        </button>
                        <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top:10px">
                            <img src="${basePath}/static/images/preview.jpg" style="width: 100%;height: 100%">
                        </div>
                        <div class="upload-progress"></div>
                    </td>
                    <td>
                        <label>工商营业执照原件照片:<span style="color: red;font-weight: normal"> (没有可不上传)</span></label>
                        <input class="fileName form-control" type="text" style="width: 50%;display: inline"
                               placeholder="图片名">
                        <button id="select_busLicePhoto" type="button" class="select_btn btn btn-default random"
                                data-key="busLicePhoto.imgInfoId">
                            选择图片
                        </button>
                        <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top:10px">
                            <img src="${basePath}/static/images/preview.jpg" style="width: 100%;height: 100%">
                        </div>
                        <div class="upload-progress"></div>
                    </td>
                </tr>
                <tr>
                    <%--
                    <td>
                        <label><span style="color: red;font-weight: normal">* </span>运营者手持身份证照片:</label>
                        <input class="fileName form-control" type="text" style="width: 50%;display: inline"
                               placeholder="图片名">
                        <button id="select_opraterIDPhoto" type="button" class="select_btn btn btn-default random"
                                data-key="opraterIDPhoto.imgInfoId">
                            选择图片
                        </button>
                        <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top:10px">
                            <img src="${basePath}/static/images/preview.jpg" style="width: 100%;height: 100%">
                        </div>
                        <div class="upload-progress"></div>
                    </td>
                    --%>
                </tr>
                <tr>
                    <td>
                        <button id="submit" class="btn btn-default">提交进行审核</button>
                    </td>
                </tr>
            </table>
        </fieldset>
    </div>
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
