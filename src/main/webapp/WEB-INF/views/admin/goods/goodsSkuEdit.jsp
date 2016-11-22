<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/7/11
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
    $(function () {
        initUploader({
            pickbutton: 'select2',
            onFileAdd: function (file) {

            },
            onError: function (file, err, errTip) {

            },
            onBeforeUpload: function (up, file) {
                $('#preview_icon2').attr('src', "/static/images/loading.gif");
            },
            onUploaded: function (up, file, info) {
                console.log(info);
                $('#coverImgId2').val(info.imgInfoId);
                $('#preview_icon2').attr('src', info.url);
            },
            onComplete: function () {

            }
        });
    });
</script>
<form id="edit-sku-form" method="post" action="${basePath}/admin/api/goods/${sku.goodsId}/sku/edit">
    <table align="center" style="width: 90%;text-align: left;">
        <tr>
            <td>封面图:</td>
            <c:set var="preivewIcon" value="${basePath}/static/images/preview.jpg"></c:set>
            <td>
                <img src="${empty sku.coverImg ? preivewIcon : sku.coverImg.url }" id="preview_icon2"
                     style="float: left;width: 50px;height: 50px;border: solid;border-width: 1px;border-color: #95b8e7">
                <button type="button" id="select2" style="margin: 10px auto auto 10px;float: left">选择图片</button>
                <input type="hidden" id="coverImgId2" name="coverImgId"
                       value="${empty sku.coverImg ? '' :sku.coverImg.imgInfoId}">
            </td>
        </tr>
        <c:forEach items="${goodsAtts}" var="goodsAtt">
            <tr>
                <td>${goodsAtt.attribute.skuAttName}:</td>
                <td style="text-align: left">
                    <input type="hidden" name="goodsAttrIds" value="${empty goodsAtt or empty goodsAtt.goodsAttrId ? " " : goodsAtt.goodsAttrId}">
                    <input class="easyui-textbox" name="attrValues" style="width: 180px" data-options="required:true"
                           value="${goodsAtt.attrValue}"/>
                    <input type="hidden" name="skuAttIds" value="${goodsAtt.attribute.skuAttId}">
                </td>
            </tr>
        </c:forEach>
        <input type="hidden" name="skuId" value="${sku.skuId}">
        <tr>
            <td>现库存:</td>
            <td style="text-align: left">
                <input class="easyui-numberspinner" name="currentStock" style="width: 180px" required="required"
                       data-options="min:0" value="${sku.currentStock}"/>件
            </td>
        </tr>
        <tr>
            <td>价格:</td>
            <td style="text-align: left">
                <input class="easyui-numberbox" name="price" style="width: 180px"
                       data-options="required:true,min:0,precision:2" value="${sku.price}"/>元
            </td>
        </tr>
    </table>
</form>
