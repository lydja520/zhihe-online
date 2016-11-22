<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/28
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家封面图和顶部图</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrapValidator.css">
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <style>
        body {
            font: 400 12px/14px Geneva, "宋体", Tahoma, sans-serif;
        }

        .container {
            width: 60%;
            height: auto;
            margin: 20px auto;
            padding: 0px 0px 10px 0px;
            border-left: solid;
            border-bottom: solid;
            border-right: solid;
            border-width: 1px;
            border-color: #95b8e7;
        }

        h2 {
            width: 100%;
            height: 40px;
            margin-left: auto;
            margin-right: auto;
            margin-top: 0px;
            font-size: 18px;
            background-color: #95b8e7;
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
            border-color: #95b8e7;
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

        #submit_coverImg, #submit_headerImg, #submit_shop_show {
            display: block;
            clear: left;
            padding-top: 5px;
            padding-bottom: 5px;
        }

        .input_width {
            padding: 0px 2px 0px 5px;
            display: inline;
            width: 40%;
        }

        .del-btn:hover {
            cursor: pointer;
        }

        .btn-default {
            margin-top: 10px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $.each($('.select_btn'), function (index, element) {
                var keyName = $(element).data('key');
                var pickbutton = $(element).attr('id');
                var filename = $(element).prev();

                initUploader({
                    pickbutton: pickbutton,
                    onFileAdd: function (file) {
                        if ($(element).next().val() == "coverImg_headerImg") {
                            filename.val(file.name);
                            $(element).next().next().children().attr('src', '${basePath}/static/images/loading.gif');
                        } else {
                            $(element).before("<div  class='pic_preview' style='border: solid;border-width: 1px;border-color: #d98b4b;width: 150px;height: auto;float:left;margin:0px 10px 10px 0px;'></div>");
                            $(element).prev().append("<img src='/static/images/loading.gif' style='width: 100%;height: 118px;'>");
                            $(element).prev().append("<input type='hidden'/><input type='hidden' name='shopShows'/>");
                            $(element).prev().append("<div style='margin-left: 5px'>简介:</div>");
                            $(element).prev().append("<input type='text' value='暂无简介' class='form-control' style='width: 140px;margin: 5px auto 5px auto;padding: 0px;text-align: center'/>");
                            $(element).prev().append("<input type='hidden' value=''>");
                            $(element).prev().append("<a style='display: block;width: 40px;margin: 0px auto 5px auto;text-align: center' onclick='deleteImg(this)' class='del-btn'>删 除</a>");
                        }
                    },
                    onError: function (file, err, errTip) {

                    },
                    onBeforeUpload: function (up, file) {
                        total = up.total.size;
                        loaded = up.total.loaded;
                        progress = parseInt(loaded / total * 100);
                    },
                    onUploaded: function (up, file, info) {
                        console.log("=====================")
                        console.log(info)
                        if ($(element).next().val() == "coverImg_headerImg") {
                            $(element).next().next().children().attr('src', info.url);
                            $(element).next().next().next().attr('value', info.imgInfoId);
                        } else if ($(element).next().val() == "shopShow") {
                            $($(element).prev().children().get(0)).attr('src', info.url);
                            $($(element).prev().children().get(0)).next().attr('value', info.imgInfoId);
                        }
                    },
                    onComplete: function () {

                    }
                });
            });

            $('#submit_coverImg').click(function () {
                $('#coverImg_form').form('submit', {
                    url: '${basePath}/admin/api/merchant/updateCoverImg',
                    success: function (data) {
                        data = $.parseJSON(data);
                        $.messager.show({
                            title: '提示',
                            msg: data.msg
                        });
                    }
                });
            });

            $('#submit_headerImg').click(function () {
                $('#headerImg_form').form('submit', {
                    url: '${basePath}/admin/api/merchant/updateHeaderImg',
                    success: function (data) {
                        data = $.parseJSON(data);
                        $.messager.show({
                            title: '提示',
                            msg: data.msg
                        });
                    }
                });
            });

            $('#submit_shop_show').click(function () {
                $('.pic_preview').each(function (index, element) {
                    var imgId = $($(element).children().get(0)).next().val();
                    var imgDetail = $($(element).children().get(0)).next().next().next().next().val();
                    var showId = $($(element).children().get(0)).next().next().next().next().next().val();
                    $($(element).children().get(0)).next().next().attr('value', imgId + "*" + imgDetail + "#" + showId);
                });
                $('#shop_show_form').form('submit', {
                    url: '${basePath}/admin/api/shopShow/add',
                    success: function (data) {
                        data = $.parseJSON(data);
                        console.log(data);
                        $.messager.show({
                            title: '提示',
                            msg: data.msg
                        });
//                        if(data.success){
//                            window.location.reload();
//                        }
                    }
                });
            });
        });

        function deleteImg(element) {
            $(element).parent().remove();
        }
    </script>
</head>
<body>
<div class="container">
    <h2>商家相关图片</h2>

    <fieldset>
        <legend>
            <span style="font-size:14px;color: #337ab7">修改商家头像和商店顶部图</span>
        </legend>
        <div class="col-md-6">
            <form id="coverImg_form" method="post">
                <input type="hidden" name="merchantId" value="${requestScope.merchant.merchantId}">
                <label>
                    <span style="color: red;font-weight: normal;">* </span>商家头像:(建议比例540*540)
                </label>

                <input class="fileName form-control input_width" type="text" placeholder="图片名" readonly
                       value=${requestScope.merchant.coverImg.key}>
                <button id="select_cover_img" type="button"
                        class="select_btn btn btn-primary random"
                        data-key="coverImg.imgInfoId">
                    选择图片
                </button>
                <input type="hidden" value="coverImg_headerImg">

                <div style="width: 120px;height: 120px;margin-top:5px;">
                    <img src="${requestScope.merchant.coverImg.url}"
                         style="display:block;width: 100%;height: 100%;border-radius: 60px;-moz-border-radius: 60px; -webkit-border-radius: 60px;border: solid;border-color:dimgray;border-width: 1px">
                </div>
                <input type="hidden" name="coverImg.imgInfoId"
                       value="${requestScope.merchant.coverImg.imgInfoId}">

            </form>
            <button id="submit_coverImg" class="btn btn-default">确认修改</button>
        </div>

        <div class="col-md-6">
            <form id="headerImg_form" method="post">
                <input type="hidden" name="merchantId" value="${requestScope.merchant.merchantId}">

                <label>
                    <span style="color: red;font-weight: normal">* </span>店铺顶部图:(建议比例1080*486)
                </label>

                <input class="fileName  form-control input_width" type="text" placeholder="图片名" readonly
                       value=${requestScope.merchant.headerImg.key}>
                <button id="select_headerImg" type="button"
                        class="select_btn btn btn-primary random"
                        data-key="headerImg.imgInfoId">
                    选择图片
                </button>
                <input type="hidden" value="coverImg_headerImg">

                <div style="border: dotted;border-width: 1px;width: 240px;height: 120px;margin-top:5px;">
                    <img src="${requestScope.merchant.headerImg.url}"
                         style="width: 100%;height: 100%">
                </div>
                <input type="hidden" name="headerImg.imgInfoId"
                       value="${requestScope.merchant.headerImg.imgInfoId}">

            </form>
            <button id="submit_headerImg" class="btn btn-default">确认修改</button>
        </div>
    </fieldset>


    <fieldset style="margin-top: 20px">
        <legend>
            <span style="font-size:14px;color: #337ab7">修改商家店面图</span>
        </legend>
        <div class="form-group" id="banners_pic"
             style="height:auto;margin-left: 15px;display: block;margin-bottom: 10px;">
            <form method="post" id="shop_show_form">
                <input type="hidden" name="merchantId" value="${requestScope.merchant.merchantId}">

                <div style="margin: 10px auto 10px 0px">
                    <span style="color: red">*</span><b>商家店面图：</b>
                </div>
                <c:forEach items="${shopShows}" varStatus="i" var="shopShow">
                    <div class='pic_preview'
                         style='border: solid;border-width: 1px;border-color: #d98b4b;width: 150px;height: auto;float:left;margin:0px 10px 10px 0px;'>
                        <img src='${shopShow.imgInfo.url}' style='width: 100%;height: 118px;'>
                        <input type='hidden' value="${shopShow.imgInfo.imgInfoId}"/>
                        <input type='hidden' name='shopShows'/>

                        <div style='margin-left: 5px'>简介:</div>
                        <input type='text' class='form-control' value="${shopShow.showDesc}"
                               style='width: 140px;margin: 5px auto 5px auto;padding: 0px;text-align: center'/>
                        <input type="hidden" value="${shopShow.showId}">
                        <a style='display: block;width: 40px;margin: 0px auto 5px auto;text-align: center'
                           class="del-btn"
                           onclick='deleteImg(this)'>删 除</a>
                    </div>
                </c:forEach>
                <input type="image" src="${basePath}/static/images/upload.jpg" class="btn btn-default select_btn"
                       id="selectbutton2"/>
                <input type="hidden" value="shopShow">
            </form>
        </div>
        <button style="margin-left: 15px;" id="submit_shop_show" class="btn btn-default">确认修改</button>
    </fieldset>

</div>
</body>
</html>
