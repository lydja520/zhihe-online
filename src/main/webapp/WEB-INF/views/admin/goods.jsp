<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/14
  Time: 9:39
  To change this template use File | Settings | File Templates...
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商品管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        /*========初始化商品数据网格========*/
        $(document).ready(function () {
            $('#goods_win').datagrid({
                url: '${basePath}/admin/api/goods/list',
                fit: true,
                title: '所有商品',
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                pageSize: 20,
                rownumbers: true,
                columns: [[
                    {field: '', checkbox: true, width: 100},
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
                    {field: 'goodsDesc', title: '商品描述', width: 100, align: 'center'},
                    {
                        field: 'goodsAttributeSet', title: '商品所属分类', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return data.goodsAttSetName;
                            }
                        }
                    },
                    {field: 'displayState', title: '状态', width: 100, align: 'center'},
                    {
                        field: 'examinMsg', title: '审核结果', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return '<a href="javascript:void(0)" title=' + value + '>' + value + '</a>';
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
                    }/*,
                     {
                     field: 'isActivityGoods', title: '是否参加活动', width: 100, align: 'center',
                     formatter: function (data) {
                     if (data) {
                     return "<span style='color: green'>是</span>";
                     }
                     return "<span style='color: red;'>否</span>";
                     }
                     },
                     {
                     field: 'isPick', title: '是否可以自取', width: 100, align: 'center', sortable: true,
                     formatter: function (value) {
                     if (value) {
                     return "<span style='color: green'>可以</span>"
                     }
                     return "<span style='color: red'>不可以</span>"
                     }
                     }*/,
                    {
                        field: 'carriageMethod', title: '快递方式', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return "<span style='color: green;'>" + value + "</span>";
                            }
                            return "<span style='color: red'>无</span>";
                        }
                    },
                    {
                        field: 'carriage', title: '运费', width: 40, align: 'center',
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {
                        field: 'price', title: '价格', width: 50, align: 'center', sortable: true,
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {field: 'stock', title: '库存量', width: 50, align: 'center'},
                    {field: 'volume', title: '销量', width: 50, align: 'center', sortable: true},
                    /*                    {field: 'currentStock', title: '现有库存量', width: 50, align: 'center', sortable: true},*/
                    {field: 'createDate', title: '添加时间', width: 100, align: 'center', sortable: true,},
                    {
                        field: 'goodsId', title: '商品详细信息', width: 100, align: 'center',
                        formatter: function (value) {
                            return "<button onclick=\'previewDetails(this.value)\' value=" + value + ">点击查看</button>";
                        }
                    }
                ]],
                toolbar: '#toobar_searchbar'
            });
            /*========searchbar========*/
            $('#grid_searchbox').searchbox({
                width: 300,
                menu: '#searchbox_menu',
                prompt: '输入关键字搜索',
                searcher: function (value, name) {
                    var param = {};
                    if (value) {
                        param.searchName = name;
                        param.searchValue = value;
                    }
                    $('#goods_win').datagrid('load', param);
                }
            });

            /*=========添加========*/
            $('#add_btn').click(function () {
                $('#add_goods').dialog({
                    title: '添加新商品',
                    maximizable: true,
                    maximized: true,
                    iconCls: 'icon-add2',
                    modal: true
                });
            });

            /*=========修改========*/
            $('#edit_btn').click(function () {
                var rows = $('#goods_win').datagrid('getSelections');
                if (rows.length == 1) {
                    if (rows[0].onsale) {
                        $.messager.alert('提示', '该商品为上架状态，无法进行编辑！要进行编辑操作，请先下架商品！', 'info');
                    } else {
                        $('#edit_goods').attr('src', "/admin/api/goods/edit/" + rows[0].goodsId);
                        $('#edit_goods').dialog({
                            title: '编辑商品信息',
                            maximizable: true,
                            maximized: true,
                            iconCls: 'icon-edit2',
                            modal: true
                        });
                    }
                } else {
                    $.messager.alert('提示', '请选中一条数据进行修改!', 'info');
                }
            });

            /*===========提交审核============*/
            $('#submit_examine_btn').click(function () {
                var rows = $('#goods_win').datagrid('getChecked');
                if (rows.length != 1) {
                    $.messager.alert('提示', '请选择需要操作的数据!', 'info');
                    return;
                }
                var goods = rows[0];
                console.log(goods);
                if (goods.onsale) {
                    $.messager.alert('提示', '该商品为上架状态，无须提交审核或刷新重试！', 'info');
                    return;
                }
                if (goods.examinState != 1 && goods.examinState != 4) {
                    $.messager.alert('提示', '只有未提交审核或审核未通过的商品才支持此操作或刷新后重试！', 'info');
                    return;
                }
                $.messager.progress();
                $.ajax({
                    url: '${basePath}/admin/api/goods/examine/submit',
                    type: 'POST',
                    dataType: 'text',
                    data: {
                        goodsId: goods.goodsId
                    },
                    success: function (data) {
                        data = $.parseJSON(data);
                        if (data.code == 200) {
                            $.messager.show({
                                title: '提示',
                                msg: '已提交审核'
                            });
                            $('#goods_win').datagrid('load');
                        } else {
                            $.messager.alert('提示', data.msg, 'info');
                        }
                    },
                    error: function () {
                        $.messager.alert('提示', '操作失败请重试！', 'error');
                    },
                    complete: function () {
                        $.messager.progress('close');
                    }
                })
            });

            /*========上架========*/
            $('#onsal_btn').click(function () {
                var rows = $('#goods_win').datagrid('getSelections');
                if (rows.length == 1) {
                    if (rows[0].examinState == <%=Constant.EXAMINE_STATE_EXAMINED_OK %>) {
                        if (rows[0].onsale) {
                            $.messager.alert('提示', '该商品已经上架，请勿重复上架', 'info');
                        } else {
                            $.messager.confirm('上架商品', '你确定要上架该商品吗？', function (r) {
                                if (r) {
                                    $.ajax({
                                        url: '${basePath}/admin/api/Goods/updateOnsalState',
                                        type: 'post',
                                        data: {
                                            goodsId: rows[0].goodsId,
                                            onsale: true
                                        },
                                        success: function (info) {
                                            $.messager.show({
                                                title: '提示',
                                                msg: info.msg
                                            });
                                            if (info.success) {
                                                $('#goods_win').datagrid('load');
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    } else {
                        $.messager.alert('提示', '未通过审核的商品不支持上架操作,请先提交审核！', 'info');
                    }
                } else {
                    $.messager.alert('提示', '请选中一条数据!', 'info');
                }
            });

            /*========下架========*/
            $('#notonsal_btn').click(function () {
                var rows = $('#goods_win').datagrid('getSelections');
                if (rows.length == 1) {
                    if (rows[0].examinState == <%=Constant.EXAMINE_STATE_EXAMINED_OK %>) {
                        if (!rows[0].onsale) {
                            $.messager.alert('提示', '该商品已经下架，请勿重复下架', 'info');
                        } else {
                            $.messager.confirm('下架商品', '您确定要下架商品吗？', function (r) {
                                if (r) {
                                    $.ajax({
                                        url: '${basePath}/admin/api/Goods/updateOnsalState',
                                        type: 'post',
                                        data: {
                                            goodsId: rows[0].goodsId,
                                            onsale: false
                                        },
                                        success: function (info) {
                                            $.messager.show({
                                                title: '提示',
                                                msg: info.msg
                                            });
                                            if (info.success) {
                                                $('#goods_win').datagrid('load');
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    } else {
                        $.messager.alert('提示', '未通过审核的商品不支持下架操作', 'info');
                    }
                } else {
                    $.messager.alert('提示', '请选中一条数据!', 'info');
                }
            });

            /*=========删除========*/
            $('#delete_btn').click(function () {
                var rows = $('#goods_win').datagrid('getSelections');
                if (rows.length < 1) {
                    $.messager.alert('提示', '请先选中选中需要删除的商品进行删除操作！', "info");
                    return;
                }
                var goods = rows[0];
                if (goods.onsale) {
                    $.messager.alert('错误提示', '商品为上架状态，不支持删除操作！', 'warning');
                    return;
                }
                $.messager.confirm('删除确认', '您确认要删除该商品吗？', function (r) {
                    if (r) {
                        $.ajax({
                            url: '${basePath}/admin/api/goods/updateDeleteState',
                            type: 'post',
                            data: {
                                goodsId: rows[0].goodsId
                            },
                            success: function (info) {
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg
                                });
                                if (info.success) {
                                    $('#goods_win').datagrid('load');
                                }
                            }
                        });
                    }
                });
            });

        });

        /*========点击预览封面图========*/
        function preview(url) {
            $('#pic_preview').attr('src', url);
            $('#disp_pic').dialog({
                title: '封面图',
                iconCls: 'icon-edit',
                width: 400,
                height: 400
            });
        }

        /*========点击预览商品详细信息========*/
        function previewDetails(value) {
            $('#detail_info').dialog({
                title: '商品详细信息',
                iconCls: 'icon-preview',
                width: '800',
                height: 480,
                modal: true,
                maximizable: true,
                onBeforeOpen: function () {
                    $('#detail_info').attr('src', "/admin/api/goods/goodsDetail/" + value);
                }
            });
        }
    </script>
</head>
<body>
<!--toolbar&searchbar开始-->
<div id="toobar_searchbar">
    <a class="easyui-linkbutton" data-options="iconCls:'icon-add2',plain:true" id="add_btn">添加</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-edit2',plain:true" id="edit_btn">修改</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-submit',plain:true" id="submit_examine_btn">提交审核</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-onsal',plain:true" id="onsal_btn">上架</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-notonsal',plain:true" id="notonsal_btn">下架</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel2',plain:true" id="delete_btn">删除</a>
    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <input id="grid_searchbox">
</div>
<div id="searchbox_menu">
    <div data-options="name:'goodsName'">商品名</div>
</div>
<!--toolbar&searchbar结束-->

<!--显示商品数据网格开始-->
<div id="goods_win"></div>
<!--显示商品数据网格结束-->

<!--添加商品开始-->
<iframe id="add_goods" src="${basePath}/admin/goods/addView"></iframe>
<!--添加商品结束-->

<!--修改商品开始-->
<iframe id="edit_goods"></iframe>
<!--修改商品结束-->

<!--删除商品开始-->
<div id="delete_goods">
    <div style="width: 50%;margin-left: auto;margin-right: auto;margin-top: 10%;color: red;">
        确定要删除选中的商品吗?
    </div>
</div>
<!--删除商品结束-->

<!--预览图片开始-->
<div id="disp_pic">
    <img id="pic_preview" style="width: 100%">
</div>
<!--预览图片结束-->

<!--商品详细信息开始-->
<iframe src="" id="detail_info"></iframe>
<!--商品详细信息结束-->

<iframe id="add-price-win">

</iframe>

</body>
</html>
