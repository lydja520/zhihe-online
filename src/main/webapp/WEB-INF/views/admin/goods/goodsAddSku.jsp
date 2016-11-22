<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/7/7
  Time: 16:29
  To change this template use File | Settings | File Templates.
  Function: 添加商品的库存和价格（sku）页面,且该商品的分类是有sku属性
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
<script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
    $(function () {
        $('#sku-details').load('${basePath}/admin/api/goods/${goods.goodsId}/sku/list');

        /*========上传封面图=======*/
        initUploader({
            pickbutton: 'select',
            onFileAdd: function (file) {

            },
            onError: function (file, err, errTip) {

            },
            onBeforeUpload: function (up, file) {
                $('#preview_icon').attr('src', "/static/images/loading.gif");
            },
            onUploaded: function (up, file, info) {
                console.log(info);
                $('#coverImgId').val(info.imgInfoId);
                $('#preview_icon').attr('src', info.url);
            },
            onComplete: function () {

            }
        });
    });


    function addSku() {
        $('#add-sku-form').form({
            url: '${basePath}/admin/api/goods/${goods.goodsId}/sku/add',
            onSubmit: function () {
                var flag = $(this).form('validate');
                if (flag) {
                    $.messager.progress();
                }
                return flag;
            },
            success: function (info) {
                info = $.parseJSON(info);
                $.messager.progress('close');
                if (info.success) {
                    $('#sku-details').load('${basePath}/admin/api/goods/${goods.goodsId}/sku/list');
                    $.messager.show({
                        title: '提示',
                        msg: info.msg
                    });
                } else {
                    $.messager.alert('错误提示', info.msg, 'error');
                }
            }
        });
        $('#add-sku-form').submit();
    }

    /**
     * 点击修改时回调此方法
     */
    function editSku(skuId) {
        $.messager.progress();
        var url = '${basePath}/admin/api/sku/' + skuId + '/editPage';
        $('#edit-sku-win').html('').load(url, function (response, status) {
                    $.messager.progress('close');
                    if (status == 'success') {
                        openSkuInfoDialog();
                    } else {
                        $.messager.alert('提示', '数据加载失败,请重试！', 'info');
                    }
                }
        );
    }

    /**
     * 编辑商品sku属性对话框
     */
    function openSkuInfoDialog() {
        var formTarget = $('#edit-sku-form');
        var skuEditDialog = $('#edit-sku-win');
        $(skuEditDialog).dialog({
            width: 400,
            height: 300,
            modal: true,
            title: '修改选中项的库存和价格',
            buttons: [{
                text: '确认',
                iconCls: 'icon-ok',
                handler: function () {
                    $(formTarget).form('submit', {
                        onSubmit: function () {
                            var flag = $(this).form('validate');
                            if (flag) {
                                $.messager.progress();
                            }
                            return flag;
                        },
                        success: function (info) {
                            $.messager.progress('close');
                            info = $.parseJSON(info);
                            if (info.code == 200) {
                                $(skuEditDialog).dialog('close');
                                $('#sku-details').load('${basePath}/admin/api/goods/${goods.goodsId}/sku/list');
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg
                                });
                            } else {
                                $.messager.alert('错误提示', info.msg, 'error');
                            }
                        }
                    });
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $(skuEditDialog).dialog('close');
                }
            }]
        });
    }

    function resetForm() {
        $('#add-sku-form').form('reset');
    }
</script>
<div id="wrap" style="width: 95%;margin: auto">
    <!--商品封面图和基本信息-->
    <form id="add-sku-form" method="post">
        <table id="t1" border="1" bordercolor="#a0c6e5" align="center"
               style="border-collapse:collapse;width: 100%">
            <tr>
                <th>封面图</th>
                <c:forEach items="${skuAtts}" var="skuAtt">
                    <th>${skuAtt.skuAttName}</th>
                </c:forEach>
                <th>库存</th>
                <th>价格(元)</th>
            </tr>
            <tr>
                <td>
                    <img src="${basePath}/static/images/preview.jpg" id="preview_icon"
                         style="display:block;width: 50px;height: 50px;margin:auto;border: solid;border-width: 1px;border-color: #95b8e7">
                    <button type="button" id="select">选择图片</button>
                    <input type="hidden" id="coverImgId" name="imgInfoId">
                </td>
                <c:forEach items="${skuAtts}" var="skuAtt">
                    <td>
                        <input type="hidden" name="skuAttId" value="${skuAtt.skuAttId}">
                        <input class="easyui-textbox" name="skuAttValue" data-options="required:true"/>
                    </td>
                </c:forEach>
                <td><input class="easyui-numberspinner" name="currentStock" required="required" data-options="min:0"/>
                </td>
                <td><input class="easyui-numberbox" name="price" data-options="required:true,min:0,precision:2"/></td>
            </tr>
        </table>
    </form>

    <div style="text-align: center;margin: 5px auto">
        <button onclick="addSku()">添加</button>
        <button onclick="resetForm()">重置</button>
    </div>

    <%--    <div style="margin: 5px auto;text-align: center">已经添加的库存和价格</div>--%>
    <div id="sku-details" style="margin-top: 15px"></div>

</div>
