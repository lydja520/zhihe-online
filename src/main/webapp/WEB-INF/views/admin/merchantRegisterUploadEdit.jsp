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
<head>
    <title>商户注册</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrapValidator.css">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrapValidator.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrapValidator.min.js"></script>
    <style>
        body {
            font: 400 12px/14px Geneva, "宋体", Tahoma, sans-serif;
        }

        .container {
            width: 60%;
            height: auto;
            margin-top: 2%;
            margin-left: auto;
            margin-right: auto;
            padding: 0px 0px 10px 0px;
            border-left: solid;
            border-bottom: solid;
            border-right: solid;
            border-width: 1px;
            border-color: #67B168;
        }

        h2 {
            width: 100%;
            height: 40px;
            margin-left: auto;
            margin-right: auto;
            margin-top: 0px;
            font-size: 18px;
            background-color: #67B168;
            line-height: 40px;
            color: #FFFFFF;
            font-weight: bold;
            text-align: center;
        }

        fieldset {
            width: 80%;
            margin-left: auto;
            margin-right: auto;
            color: #333;
            border-top: solid;
            border-width: 2px;
            border-color: #67B168;
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

        label {
            font-size: 12px;
            display: block;
        }

        .form-control {
            padding: 6px 64px;
        }

        .input_width {
            padding: 0px 2px 0px 5px;
            display: inline;
            width: 40%;
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
                var uploadbutton = $(element).next().attr('id');
                var filename = $(element).prev();
                initUploader({
                    pickbutton: pickbutton,
                    uploadbutton: uploadbutton,
                    onFileAdd: function (file) {
                        filename.val(file.name);
                    },
                    onError: function (file, err, errTip) {

                    },
                    onBeforeUpload: function (up, file) {
                        total = up.total.size;
                        loaded = up.total.loaded;
                        progress = parseInt(loaded / total * 100);
                        $(element).next().next().html(' 正在上传...');
                    },
                    onUploaded: function (up, file, info) {
                        if (keyName) {
                            console.log(info);
                            merchant_img(keyName, info.imgInfoId);
                            $(element).next().children().attr('src', info.url);
                            $(element).next().next().html(' 上传完成');
                        }
                    },
                    onComplete: function () {

                    }
                });
            });

            $('#submit').click(function () {
                console.log(Object.keys(imgInfoIds).length);
                if (Object.keys(imgInfoIds).length >= 3 || Object.keys(imgInfoIds).length == 0) {
                    $.ajax({
                        url: '${basePath}/admin/api/merchant/editMerchantRegisterUploadForm',
                        data: imgInfoIds,
                        dataType: 'text',
                        type: 'post',
                        success: function (info) {
                            info = $.parseJSON(info);
                            alert(info.msg)
                        }
                    });
                } else {
                    alert("你有未上传的图片");
                }
            });
        });
    </script>
</head>
<body>
<div>
    <div class="container">
        <h2>商家信息修改</h2>
        <fieldset>
            <legend>
                <span style="font-size:14px;color: green">修改有关图片</span>
            </legend>
            <table cellspacing="10" style="border-collapse: separate;border-spacing: 15px;width: 100%">
                <tr>
                    <%--                    <td>
                                            <label><span style="color: red;font-weight: normal">* </span>加盖公章的申请认证公函(与商家纠纷事件裁定等)照片:</label>
                                            <input class="fileName  form-control input_width" type="text" placeholder="图片名"
                                                   value="${merchant.applyLetterPhoto.key}">
                                            <button id="select_applyLetterPhoto" type="button" class="select_btn btn btn-primary random"
                                                    data-key="applyLetterPhoto.imgInfoId">
                                                选择图片
                                            </button>
                                            <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top: 10px">
                                                <img src="${merchant.applyLetterPhoto.url}" style="width: 100%;height: 100%">
                                            </div>
                                            <div></div>
                                        </td>--%>
                    <td>
                        <label>组织机构代码证原件照片:<span style="color: red;font-weight: normal"> (没有可不上传)</span></label>
                        <input class="fileName  form-control input_width" type="text" placeholder="图片名"
                               value="${merchant.orgPhoto.key}">
                        <button id="select_orgPhoto" type="button" class="select_btn btn btn-primary random"
                                data-key="orgPhoto.imgInfoId">
                            选择图片
                        </button>
                        <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top: 10px">
                            <img src="${merchant.orgPhoto.url}" style="width: 100%;height: 100%">
                        </div>
                        <div></div>
                    </td>
                    <td>
                        <label>工商营业执照原件照片:<span style="color: red;font-weight: normal"> (没有可不上传)</span></label>
                        <input class="fileName  form-control input_width" type="text" placeholder="图片名"
                               value="${merchant.busLicePhoto.key}">
                        <button id="select_busLicePhoto" type="button" class="select_btn btn btn-primary random"
                                data-key="busLicePhoto.imgInfoId">
                            选择图片
                        </button>
                        <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top: 10px">
                            <img src="${merchant.busLicePhoto.url}" style="width: 100%;height: 100%">
                        </div>
                        <div></div>
                    </td>
                </tr>
                <tr>

                    <%--<td>
                        <label><span style="color: red;font-weight: normal">* </span>运营者手持身份证照片:</label>
                        <input class="fileName  form-control input_width" type="text" placeholder="图片名"
                               value="${merchant.opraterIDPhoto.key}">
                        <button id="select_opraterIDPhoto" type="button" class="select_btn btn btn-default random"
                                data-key="opraterIDPhoto.imgInfoId">
                            选择图片
                        </button>
                        <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;margin-top: 10px">
                            <img src="${merchant.opraterIDPhoto.url}" style="width: 100%;height: 100%">
                        </div>
                        <div></div>
                    </td>--%>
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
</body>
</html>
