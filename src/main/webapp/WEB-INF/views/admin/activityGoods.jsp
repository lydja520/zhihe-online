<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/3/4
  Time: 9:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>活动商品</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
</head>
<style>
    body {
        margin: 0px;
        padding: 0px;
        font-size: 12px;
    }

    ul {
        margin: 0px;
        padding: 0px;
        list-style-type: none;
    }

    ul li {
        width: 30%;
        margin-top: 10px;
        float: left;
        text-align: center;
    }

    .li-content {
        width: 70%;
        text-align: left;
    }

    .li-content input {
        width: 220px;
    }
</style>
<script type="text/javascript">

    $(document).ready(function () {
        /*========数据网格开始=======*/
        $('#dg').datagrid({
            url: '${basePath}/admin/api/activity/${activity.activitId}/activityGoods/list',
            fit: true,
            fitColumns: true,
            rownumbers: true,
            singleSelect: true,
            pagination: true,
            border: false,
            striped: true,
            columns: [[
                {field: 'agId', checkbox: true, align: 'center',},
                {
                    field: 'goodsCoverImg', title: '商品封面', width: 50, align: 'center',
                    formatter: function (value, row) {
                        if (row.sku.coverImg) {
                            var url = row.sku.coverImg.url;
                            return "<div style='text-align: center'><img style='width: 40px;height: 40px;' src='" + url + "'></div>";
                        }
                        return "<div style='text-align: center'><img style='width: 40px;height: 40px;' src='${basePath}/static/images/noPic.jpg'></div>"
                    }
                }, {
                    field: 'goodsName', title: '商品名称', width: 80, align: 'center',
                    formatter: function (value, row) {
                        if (row.goods) {
                            return "<div>" + row.goods.goodsName + "</div>"
                        }
                    }
                }, {
                    field: 'sku', title: '商品组合', width: 100, align: 'center',
                    formatter: function (value) {
                        return value.skuValue;
                    }
                }, {
                    field: 'originalprice', title: '商品原价(元)', width: 50, align: 'center',
                    formatter: function (value, row) {
                        return row.sku.price+" 元";
                    }
                },
                {
                    field: 'activityPrice', title: '活动价格(元)', width: 50, align: 'center',
                    formatter:function (value) {
                        return value +" 元";
                    }
                },
                {field: 'count', title: '数量', width: 50, align: 'center'},
                {field: 'acitivityGoodsDesc', title: '活动商品介绍', width: 50, align: 'center'}
            ]],
            toolbar: [{
                text: '添加',
                iconCls: 'icon-add2',
                handler: function () {
                    addActGoods();
                }
            }, '-', {
                text: '修改',
                iconCls: 'icon-edit2',
                handler: function () {
                    ediActGoods();
                }
            }, '-', {
                text: '删除',
                iconCls: 'icon-remove',
                handler: function () {
                    removeActGoods();
                }
            }]

        });
    });

    /*========添加活动商品========*/
    function addActGoods() {
        if (parseInt(${activity.currentState}) == 1 || parseInt(${activity.currentState}) == 4) {
            $('#add-edit-win').dialog({
                width: 400,
                height: 320,
                title: '添加活动商品',
                modal: true,
                iconCls: 'icon-add2',
                onBeforeOpen: function () {
                    $('#add-edit-form').form('reset');
                    $('#activityGoodsCombo').combo({
                        editable: false,
                        /*                    hasDownArrow: false,*/
                        required: true,
                        multiline: true,
                        height: 50,
                        prompt: '点击选择一个商品',
                        onShowPanel: function () {
                            $(this).combo('hidePanel');
                            $('#activityGoodsSelect').dialog({
                                title: '选择参加活动的商品',
                                width: 800,
                                height: 500,
                                modal: true,
                            });
                            $('#acitivityGoodsSelectDg').datagrid({
                                url: '${basePath}/admin/api/activityGoods/select/list',
                                fit: true,
                                striped: true,
                                fitColumns: true,
                                pagination: true,
                                singleSelect: true,
                                pageSize: 20,
                                border: false,
                                rownumbers: true,
                                columns: [[
                                    {
                                        field: 'coverImg', title: '封面图', width: 100, align: 'center',
                                        formatter: function (data) {
                                            if (data) {
                                                var url = data.domain + data.key;
                                                return "<div style='width: 65px;height: 45px;margin: 1px auto 1px auto'><img style='width: 65px;height: 65px;' src='" + url + "'></div>"
                                            }
                                            return "<span style='color: red' '>无封面图</span>"
                                        }
                                    },
                                    {field: 'goodsName', title: '商品名', width: 100, align: 'center'},
                                    {
                                        field: 'goodsAttributeSet', title: '商品所属分类', width: 100, align: 'center',
                                        formatter: function (data) {
                                            if (data) {
                                                return data.goodsAttSetName;
                                            }
                                        }
                                    },
                                    {
                                        field: 'onsale', title: '在售状态', width: 100, align: 'center', sortable: true,
                                        formatter: function (value) {
                                            if (value) {
                                                return '<span style="color: green">上架</span>';
                                            }
                                            return "<span style='color: red'>下架</span>";
                                        }
                                    },
                                    {
                                        field: 'price', title: '价格', width: 100, align: 'center', sortable: true,
                                        formatter: function (value) {
                                            return value + " 元";
                                        }
                                    },
                                    {
                                        field: 'volume', title: '销量', width: 100, align: 'center', sortable: true,
                                        formatter: function (value) {
                                            return value + " 件";
                                        }
                                    },
                                    {
                                        field: 'currentStock',
                                        title: '库存量',
                                        width: 100,
                                        align: 'center',
                                        sortable: true,
                                        formatter: function (value) {
                                            return value + " 件";
                                        }
                                    },
                                    {field: 'createDate', title: '添加时间', width: 100, align: 'center', sortable: true,},
                                ]],
                                toolbar: '#toobar_searchbar',
                                onClickRow: function (index, row) {
                                    $('#activityGoodsSkuSelect').dialog({
                                        width: 600,
                                        height: 400,
                                        title: '选择商品的sku',
                                        modal: true,
                                        href: '${basePath}/admin/api/activity/${activity.activitId}/goods/' + row.goodsId + '/sku/list',
                                        buttons: [{
                                            text: '确定',
                                            iconCls: 'icon-ok',
                                            handler: function () {
                                                var $selectSku = $('input:radio[name=skuId]:checked');
                                                if ($selectSku.length <= 0) {
                                                    $.messager.alert('提示', '请选中一条数据', 'info');
                                                    return;
                                                }

                                                $('#activityGoodsSkuSelect').dialog('close');
                                                $('#activityGoodsSelect').dialog('close');
                                                $('#activityGoodsCombo').combo('setText', row.goodsName + "-" + $selectSku.data('skuvalue'));
                                                $('#activityGoodsCombo').combo('setValue', row.goodsId);
                                                $('#activityPrice').numberbox('setValue', $selectSku.data('price'));
                                                $('#goodsPrice').numberbox('setValue', $selectSku.data('price'));
                                                $("#sku-skuId").attr('value', $selectSku.val());
                                            }
                                        }, {
                                            text: '取消',
                                            iconCls: 'icon-cancel',
                                            handler: function () {
                                                $('#activityGoodsSkuSelect').dialog('close');
                                                $('#activityGoodsSelect').dialog('close');
                                            }
                                        }]
                                    });
                                }
                            });
                        }
                    });
                },
                buttons: [{
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#add-edit-form').form({
                            url: '${basePath}/admin/api/activityGoodsAdd',
                            success: function (info) {
                                info = $.parseJSON(info);
                                if (info.success) {
                                    $('#add-edit-win').dialog('close');
                                    $('#dg').datagrid('reload');
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                } else {
                                    $.messager.alert('提示', info.msg, 'error');
                                }
                            }
                        });
                        $('#add-edit-form').submit();
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#add-edit-win').dialog('close');
                    }
                }]
            })
        } else {
            $.messager.alert('提示', '活动在该状态下不支持添加活动商品操作', 'info');
        }
    }

    /*========修改活动商品========*/
    function ediActGoods() {
        if (parseInt(${activity.currentState}) == 1 || parseInt(${activity.currentState}) == 4) {
            var rows = $('#dg').datagrid('getSelections');
            if (rows.length > 0) {
                $('#add-edit-win').dialog({
                    width: 450,
                    height: 320,
                    title: '修改活动商品',
                    modal: true,
                    iconCls: 'icon-add2',
                    onBeforeOpen: function () {
                        $('#add-edit-form').form('load', rows[0]);
                        $('#activityGoodsCombo').combo({
                            editable: false,
                            hasDownArrow: false,
                            required: true,
                            onShowPanel: function () {
                                $(this).combo('hidePanel');
                            }
                        });
                        $('#activityGoodsCombo').combo('setText', rows[0].goods.goodsName);
                        $('#activityGoodsCombo').combo('setValue', rows[0].goods.goodsId);
                        $('#activityGoodsId').attr('value', rows[0].agId);
                        $('#goodsPrice').numberbox('setValue', rows[0].goods.price);
                        $('#sku-skuId').attr('value', rows[0].sku.skuId);
                    },
                    buttons: [{
                        text: '确定',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#add-edit-form').form({
                                url: '${basePath}/admin/api/activityGoodsUpdate',
                                success: function (info) {
                                    info = $.parseJSON(info);
                                    if (info.success) {
                                        $.messager.show({
                                            title: '提示',
                                            msg: info.msg
                                        });
                                        $('#add-edit-win').dialog('close');
                                        $('#dg').datagrid('reload');
                                    } else {
                                        $.messager.alert('提示', info.msg, 'error');
                                    }
                                }
                            });
                            $('#add-edit-form').submit();
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#add-edit-win').dialog('close');
                        }
                    }]
                });
            } else {
                $.messager.alert('提示', '请选中一条数据进行操作', 'info');
            }
        } else {
            $.messager.alert('提示', '活动在该状态下不支持修改活动商品操作', 'info');
        }
    }

    /*=======删除活动商品========*/
    function removeActGoods() {
        if (parseInt(${activity.currentState}) == 1 || parseInt(${activity.currentState}) == 4) {
            var rows = $('#dg').datagrid('getSelections');
            if (rows.length > 0) {
                $.messager.confirm('确认', '您确认想要删除选中的活动商品吗？', function (r) {
                    if (r) {
                        $.ajax({
                            url: '${basePath}/admin/api/activityGoods/delete',
                            dataType: 'text',
                            type: 'post',
                            data: {
                                agId: rows[0].agId,
                                activityPrice: rows[0].activityPrice,
                                'activity.activitId': rows[0].activity.activitId,
                                'goods.goodsId': rows[0].goods.goodsId,
                                'merchant.merchantId': rows[0].merchant.merchantId
                            },
                            success: function (info) {
                                info = $.parseJSON(info);
                                if (info.success) {
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                    $('#dg').datagrid('load');
                                } else {
                                    $.messager.alert('提示', info.msg, 'error');
                                }
                            }
                        });
                    }
                });
            } else {
                $.messager.alert('提示', '请选中一条数据进行操作', 'info');
            }
        } else {
            $.messager.alert('提示', '活动在该状态下不支持删除活动商品操作', 'info');
        }
    }


</script>
<body>

<!--数据网格开始-->
<div id="dg"></div>
<!--数据网格结束-->

<!--添加和修改活动商品开始-->
<div id="add-edit-win">
    <form method="post" id="add-edit-form">
        <input type="hidden" id="activityGoodsId" name="agId"/>
        <input type="hidden" id="merchantId" name="merchant.merchantId" value="${merchant.merchantId}">
        <input type="hidden" name="activity.activitId" value="${activity.activitId}"/>
        <input type="hi" name="sku.skuId" id="sku-skuId"/>
        <ul>
            <li>活动商品:</li>
            <li class="li-content"><input id="activityGoodsCombo" name="goods.goodsId"></li>
            <li>商品原价:</li>
            <li class="li-content">
                <input type="text" class="easyui-numberbox" value="0"
                       data-options="editable:false,min:0,precision:2" id="goodsPrice"/>
            </li>
            <li>活动价格:</li>
            <li class="li-content">
                <input type="text" class="easyui-numberbox" value="0"
                       data-options="min:0,precision:2,required:true" id="activityPrice" name="activityPrice"/>
            </li>
            <li>商品数量:</li>
            <li class="li-content">
                <input type="text" class="easyui-numberspinner" min="1" value="1"
                       data-options="min:0" id="activityCount" required="true" name="agCount"/>
            </li>
            <li>描述:</li>
            <li class="li-content">
                <input class="easyui-textbox" data-options="multiline:true,required:false"
                       style="height: 50px" id="acitivityDetail" name="acitivityGoodsDesc"/>
            </li>
            <div class="clear" style="margin-top: 10px"></div>
        </ul>
    </form>
</div>
<!--添加和修改活动商品结束-->

<!--选择活动商品窗口-->
<div id="activityGoodsSelect">
    <div id="acitivityGoodsSelectDg"></div>
</div>
<!--选择活动商品窗口-->

<!--选择商品sku-->
<div id="activityGoodsSkuSelect"></div>
<!--选择商品sku-->

</body>
</html>
