<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/14
  Time: 12:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="cn.com.zhihetech.online.bean.Goods" %>
<%@ page import="cn.com.zhihetech.online.bean.GoodsAttributeSet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>添加新商品</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>


    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <style>
        * {
            font-family: "Microsoft Yahei";
            font-size: 12px;
        }

        html,body{
            width: 100%;
            height: 100%;
            overflow-y: scroll;
        }

        body {
            margin: 0px;
            padding: 0px;
        }

        fieldset {
            border: solid;
            border-width: 1px;
            border-color: green;
            padding-bottom: 5px;
            margin: 10px auto;
        }

        legend {
            width: auto;
            height: 20px;
            padding-left: 10px;
            padding-right: 10px;
            margin-left: 30px;
            line-height: 20px;
            border: none;
        }

        legend span {
            font-size: 14px;
            color: #ff4500;
            font-weight: bold;
        }

        .row {
            display: block;
            width: 90%;
            margin: 5px auto;
        }

        .info-title {
            width: 120px;
            float: left;
        }

        .info-content {
            width: 500px;
            float: left;
        }

        .info-content .easyui-textbox, .info-content .easyui-numberbox {
            width: 450px;
        }

        a {
            color: blue;
        }

        a:hover {
            color: red;
            cursor: pointer;
        }

        .clear {
            clear: both;
        }

        .info-list div {
            margin-bottom: 3px;
        }

        ul, li {
            list-style: none;
            margin: 0px;
            padding: 0px;
        }

        li {
            display: inline;
            margin: 0px 4px;
        }

        li a {
            border: solid thin darkgrey;
            padding: 0px 5px;
        }

        table td {
            text-align: center;
        }

        table .easyui-textbox, .easyui-numberbox, .easyui-numberspinner {
            width: 100px;
        }

        h4 {
            text-align: center;
        }

        .order-num {
            width: 450px;
            margin: 5px auto;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $.ajax({
                url: '/admin/api/goods/${goods.goodsId}/addGoodsSkuPage',
                type: 'get',
                dataType: 'text',
                success: function (data) {
                    $('#add-goods-sku-win').html(data);
                }
            });
            /*========验证并提交表单=======*/
            $('#add-goodsinfo-btn').click(function () {
                $('#banners_pic .pic_preview').each(function (index, element) {
                    var imgId = $($(element).children().get(0)).next().val();
                    var order = $($(element).children().get(0)).next().next().val();
                    $($(element).children().get(0)).next().next().next().attr('value', imgId + "#" + order);
                });
                $('#details_pic .pic_preview').each(function (index, element) {
                    var imgId = $($(element).children().get(0)).next().val();
                    var order = $($(element).children().get(0)).next().next().val();
                    $($(element).children().get(0)).next().next().next().attr('value', imgId + "#" + order);
                });

                $('#add_goods_form').form('submit', {
                    url: '${basePath}/admin/api/goods/addOrUpdate',
                    type: 'post',
                    dataType: 'text',
                    onSubmit: function () {
                        if ($('#add_goods_form').form('validate')) {
                            $.messager.progress();
                            return true;
                        }
                        return false;
                    },
                    success: function (info) {
                        info = $.parseJSON(info);
                        if (info.code == 200) {
                            $('#add-goodsinfo-btn').addClass('disabled');
                            $('#add-goodsinfo-btn').prop('disabled', true);
                            $.messager.show({
                                title: '提示',
                                msg: info.msg
                            });
                            $.messager.progress('close');
                        } else {
                            $.messager.alert('错误', info.msg, 'error');
                        }
                    },
                    error: function (info) {
                        $.messager.progress('close');
                        info = $.parseJSON(info);
                        $.messager.alert('错误', info.msg, 'error');
                    }
                });
            });

            var flag = $('input[name="carriageState"]:checked').val();
            carriageOpacity(flag);

            /*=========上传图片========*/
            $('.select_btn').each(function (index, element) {
                initUploader({
                    pickbutton: $(element).attr('id'),
                    onFileAdd: function () {
                        if ($(element).next().val() == "coverImg") {
                        } else if ($(element).next().val() == "banner") {
                            $(element).before("<div class='pic_preview' style='border: solid;border-width: 1px;border-color:#d98b4b;width: 100px;height:150px;float:left;margin:0px 10px 10px 0px;text-align: center'></div>");
                            $(element).prev().append("<img src='/static/images/loading.gif' style='width: 100px;height: 100px'>");
                            $(element).prev().append("<input type='hidden'/><input type='number' value='1' style='width: 90px;margin: 5px auto;text-align: center' placeholder='请输入序号'/>");
                            $(element).prev().append("<input type='hidden' name='goodsBanners'/>");
                            $(element).prev().append("<a style='display: block;width: 40px;margin: 0px auto 0px auto;text-align: center' onclick='deleteImg(this)'>删 除</a>");
                        } else {
                            $(element).before("<div  class='pic_preview' style='border: solid;border-width: 1px;border-color:#d98b4b;width: 100px;height:150px;float:left;margin:0px 10px 10px 0px;text-align: center'></div>");
                            $(element).prev().append("<img src='/static/images/loading.gif' style='width:100px;height: 100px;'>");
                            $(element).prev().append("<input type='hidden'/><input  type='number' value='1' style='width: 90px;margin: 5px auto;text-align: center' placeholder='请输入序号'/>");
                            $(element).prev().append("<input type='hidden' name='goodsDetails'/>");
                            $(element).prev().append("<a style='display: block;width: 40px;margin: 0px auto 0px auto;text-align: center' onclick='deleteImg(this)'>删 除</a>");
                        }
                    },
                    onError: function (file, err, errTip) {

                    },
                    onBeforeUpload: function (up, file) {
                        total = up.total.size;
                        loaded = up.total.loaded;
                        progress = parseInt(loaded / total * 100);
                        if ($(element).next().val() == "coverImg") {
                            $(element).next().next().next().html(' 正在上传...');
                        } else if ($(element).next().val() == "banner") {

                        } else {

                        }
                    },
                    onUploaded: function (up, file, info) {
                        if ($(element).next().val() == "coverImg") {
                            $(element).prev().attr('value', info.imgInfoId);
                            $(element).next().next().children().attr('src', info.url);
                            $(element).next().next().next().html(' 上传完成');
                        } else if ($(element).next().val() == "banner") {
                            $($(element).prev().children().get(0)).attr('src', info.url);
                            $($(element).prev().children().get(0)).next().attr('value', info.imgInfoId);
                        } else {
                            $($(element).prev().children().get(0)).attr('src', info.url);
                            $($(element).prev().children().get(0)).next().attr('value', info.imgInfoId);
                        }
                    },
                    onComplete: function () {

                    }
                });
            });

            $('#selectbutton1').click(function () {
                $('#preview_img').attr('src', '${basePath}/static/images/preview.jpg');
                $('#cov_pregress').html("");
            });

        });

        /*========设置邮寄方式是否可编辑========*/
        function carriageOpacity(flag) {
            if (flag == 'true') {
                $('.no_edit').hide();
                $('#disp_carriagerMethod').animate({"opacity": 1});
                $('#disp_carriage').animate({"opacity": 1});
                $('#carriageMethod').textbox({disabled: false});
                $('#carriage').numberbox({disabled: false});
            } else {
                $('.no_edit').show();
                $('#disp_carriagerMethod').animate({"opacity": 0.5});
                $('#disp_carriage').animate({"opacity": 0.5});
                $('#carriageMethod').textbox({disabled: true});
                $('#carriage').numberbox({disabled: true});
            }
        }

        function deleteImg(element) {
            $(element).parent().remove();
        }
    </script>
</head>
<body>
<div style="width: 70%;margin: auto">
    <fieldset class="info_forum">
        <legend class="info_forum_title"><span>1、商品基本信息</span></legend>
        <form id="add_goods_form">
            <input type="hidden" name="goodsId" value="${requestScope.goods.goodsId}">
            <input type="hidden" name="merchant.merchantId" value="${requestScope.goods.merchant.merchantId}">
            <input type="hidden" name="createDate" value="${requestScope.goods.createDate}">
            <input type="hidden" name="examinState" value=${requestScope.goods.examinState}>
            <input type="hidden" name="examinMsg" value="${requestScope.goods.examinMsg}">
            <input type="hidden" name="deleteState" value=${requestScope.goods.deleteState}>
            <input type="hidden" name="isActivityGoods" value=${requestScope.goods.isActivityGoods}>
            <input type="hidden" name="onsale" value=${requestScope.goods.onsale}>

            <div class="row">
                <div class="info-title"><span style="color: red">*</span> 商品标题：</div>
                <div class="info-content">
                    <input type="text" class="easyui-textbox" data-options="required:true" id="goodsName"
                           name="goodsName" prompt="请输入商品的标题" value="${requestScope.goods.goodsName}">
                </div>
                <div class="clear"></div>
            </div>

            <div class="row">
                <div class="info-title"><span style="color: red">*</span> 商品分类：</div>
                <div class="info-content">
                    <%
                        Goods goods = (Goods) request.getAttribute("goods");
                        Iterator<GoodsAttributeSet> iterator = ((List<GoodsAttributeSet>) request.getAttribute("goodsAttributeSets")).iterator();
                        while (iterator.hasNext()) {
                            GoodsAttributeSet goodsAttributeSet = iterator.next();
                            if (goodsAttributeSet.getGoodsAttSetId().equals(goods.getGoodsAttributeSet().getGoodsAttSetId())) {
                    %>
                    <input type="radio" name="goodsAttributeSet.goodsAttSetId" checked="checked"
                           value="<%=goodsAttributeSet.getGoodsAttSetId()%>"><%=goodsAttributeSet.getGoodsAttSetName()%>
                    <%
                            continue;
                        }
                    %>
                    <input type="radio" name="goodsAttributeSet.goodsAttSetId" disabled
                           value="<%=goodsAttributeSet.getGoodsAttSetId()%>"><%=goodsAttributeSet.getGoodsAttSetName()%>
                    <%
                        }
                    %>
                </div>
                <div class="clear"></div>
            </div>

            <div class="row">
                <div class="info-title"><span style="color: red">*</span> 商品描述：</div>
                <div class="info-content">
                    <input id="goodsDesc" class="easyui-textbox" data-options="multiline:true,required:true"
                           style="height: 50px"
                           name="goodsDesc" prompt="请输入商品描述" value="${requestScope.goods.goodsDesc}"/>
                </div>
                <div class="clear"></div>
            </div>

            <div class="row">
                <div class="info-title"><span style="color: red">*</span> 商品封面图：<br><span
                        style="font-size: 10px;color: #6a6a6a">(建议比例540*540)</span></div>
                <div class="info-content">
                    <input type="hidden" name="coverImg.imgInfoId" value="${requestScope.goods.coverImg.imgInfoId}">
                    <button type="button" class="select_btn" style="margin: 35px auto auto 10px" id="selectbutton1">
                        选择图片
                    </button>
                    <input type="hidden" value="coverImg">
                    <div style="border: solid thin #c0c0c0;border-width: 1px;width: 100px;height: 100px;float:left;margin-top: 5px;">
                        <img src="${requestScope.goods.coverImg.url}" style="width: 100%;height: 100%"
                             id="preview_img">
                    </div>
                    <div style="margin: 15px auto auto 110px;" id="cov_pregress"></div>
                    <div class="clear"></div>
                </div>
                <div class="clear"></div>
            </div>

            <div class="row">
                <div class="info-title">
                    <span style="color: red">*</span>商品轮播图：<br>
                    <span style="font-size: 10px;color: #6a6a6a">(建议比例1080*1080)</span>
                </div>
                <div class="info-content" id="banners_pic">
                    <c:forEach items="${requestScope.goodsBanners}" var="goodsBanner">
                        <div class='pic_preview'
                             style='border: solid;border-width: 1px;border-color:#d98b4b;width: 100px;height:150px;float:left;margin:0px 10px 10px 0px;text-align: center'>
                            <img src='${goodsBanner.imgInfo.url}' style='width: 100px;height: 100px;'>
                            <input type='hidden' value="${goodsBanner.imgInfo.imgInfoId}"/>
                            <input  type='number'
                                   style='width: 90px;margin: 5px auto;text-align: center'
                                   value='${goodsBanner.bannerOrder}'
                                   required placeholder='请输入序号'/>
                            <input type='hidden' name='goodsBanners'/>
                            <a style='display: block;width: 40px;margin: 0px auto 0px auto;text-align: center'
                               onclick='deleteImg(this)'>删 除</a>
                        </div>
                    </c:forEach>
                    <input type="image" src="${basePath}/static/images/upload.jpg" class="select_btn"
                           id="selectbutton2"
                           style="float:left;border: solid thin #c0c0c0;border-radius: 5px;width: 100px;height: 100px"/>
                    <input type="hidden" value="banner">
                </div>
                <div class="clear"></div>
            </div>

            <div class="row">
                <div class="info-title"><span style="color: red">*</span>商品详情图：</div>
                <div class="info-content" id="details_pic">
                    <c:forEach items="${requestScope.goodsDetails}" var="goodsDetail">
                        <div class='pic_preview'
                             style='border: solid;border-width: 1px;border-color:#d98b4b;width: 100px;float:left;margin:0px 10px 10px 0px;text-align: center'>
                            <img src='${goodsDetail.imgInfo.url}' style='width:100px;height: 100px;'>
                            <input type='hidden' value="${goodsDetail.imgInfo.imgInfoId}"/>
                            <input type='number'
                                   style='width: 90px;margin: 5px auto;text-align: center'
                                   value='${goodsDetail.detailOrder}'
                                   required placeholder='请输入序号'/>
                            <input type='hidden' name='goodsDetails'/>

                            <a style='display: block;width: 40px;margin: 0px auto 0px auto;text-align: center'
                               onclick='deleteImg(this)'>删 除</a>
                        </div>
                    </c:forEach>
                    <input type="image" src="${basePath}/static/images/upload.jpg" class="select_btn"
                           id="selectbutton3"
                           style="float:left;border: solid thin #c0c0c0;border-radius: 5px;width: 100px;height: 100px"/>
                    <input type="hidden" value="detail">
                </div>
                <div class="clear"></div>
            </div>

            <div class="row">
                <div class="info-title"><span style="color: red">*</span> 是否可以邮寄：</div>
                <div class="info-content">
                    <c:choose>
                        <c:when test="${requestScope.goods.carriageMethod != null}">
                            <input type="radio" class="carriageState" name="carriageState" value="true"
                                   onclick="carriageOpacity('true')" checked>是&nbsp;&nbsp;
                            <input type="radio" class="carriageState" name="carriageState" value="false"
                                   onclick="carriageOpacity('false')">否
                        </c:when>
                        <c:otherwise>
                            <input type="radio" class="carriageState" name="carriageState" value="true"
                                   onclick="carriageOpacity('true')">是&nbsp;&nbsp;
                            <input type="radio" class="carriageState" name="carriageState" value="false"
                                   onclick="carriageOpacity('false')" checked>否
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="clear"></div>
            </div>

            <div class="row">
                <div class="info-title"><span style="color: red">*</span> 快递方式：</div>
                <div class="info-content">
                    <input type="text" class="easyui-textbox" data-options="required:true" id="carriageMethod"
                           name="carriageMethod"
                           prompt="快递方式"
                           value="${requestScope.goods.carriageMethod}">
                </div>
                <span class="no_edit" style="color: red">不可编辑</span>
                <div class="clear"></div>
            </div>

            <div class="row">
                <div class="info-title"><span style="color: red">*</span> 快递运费：</div>
                <div class="info-content">
                    <input type="text" class="easyui-numberbox"
                           data-options="min:0,precision:2,required:true"
                           id="carriage" name="carriage"
                           prompt="快递运费"
                           value="${requestScope.goods.carriage}">元
                </div>
                <span class="no_edit" style="color: red">不可编辑</span>

                <input type="hidden" name="price" value="0">
                <input type="hidden" name="stock" value="0">
                <div class="clear"></div>
            </div>

        </form>

        <div class="row" style="width: 80px;margin: 5px auto">
            <button id="add-goodsinfo-btn" class="btn btn-primary">修改</button>
        </div>
    </fieldset>


    <fieldset class="info_forum">
        <legend class="info_forum_title"><span>2、商品库存和价格</span></legend>
        <div id="add-goods-sku-win" style="padding: 10px 0px">
            <div style="text-align: center;font-size: 14px;font-weight: bolder;height: 20px;line-height: 20px;margin-bottom: 10px;color: #ff4500;">
                请先添加商品的基本信息
            </div>
        </div>
    </fieldset>

</div>
</body>
</html>
