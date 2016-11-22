<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/15
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>正在出售的的商品</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(document).ready(function () {
            /*========初始化数据网格========*/
            $('#onsal_win').datagrid({
                url: '${basePath}/admin/api/onsalGoods/list',
                title: '正在出售的商品',
                fit: true,
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
//                                return "<button onclick=\'preview(this.value)\' value=" + data.domain + data.key + ">点击预览</button>";
                                var url = data.domain + data.key;
//                                return "<img style='width: 40px;height: 40px;margin:1px' src='" + url + "'>";
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
                    },
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
                        field: 'carriage', title: '运费', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {
                        field: 'price', title: '价格', width: 100, align: 'center', sortable: true,
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {field: 'stock', title: '原始库存量', width: 50, align: 'center'},
                    {field: 'volume', title: '销量', width: 50, align: 'center', sortable: true},
                    {field: 'currentStock', title: '现有库存量', width: 50, align: 'center', sortable: true},
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
                    $('#onsal_win').datagrid('load', param);
                }
            });

            /*========下架========*/
            $('#onsal_btn').click(function () {
                var rows = $('#onsal_win').datagrid('getSelections');
                if (rows.length == 1) {
                    $.messager.confirm('确认下架', '您确定要下架该商品吗？', function (r) {
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
                                        $('#onsal_win').datagrid('load');
                                    }
                                }
                            });
                        }
                    });
                } else {
                    $.messager.alert('提示', '请选中一条数据!');
                }
            });
        });

        function preview(key) {
            $('#pic_preview').attr('src', key);
            $('#disp_pic').dialog({
                title: '封面图',
                iconCls: 'icon-edit',
                width: 400,
                height: 400,
            });
        }

        /*========点击预览商品详细信息========*/
        function previewDetails(value) {
            $('#detail_info').dialog({
                title: '商品详细信息',
                iconCls: 'icon-preview',
                width: 800,
                height: 480,
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
    <a class="easyui-linkbutton" data-options="iconCls:'icon-notonsal',plain:true" id="onsal_btn">下架</a>
    <input id="grid_searchbox">
</div>
<div id="searchbox_menu">
    <div data-options="name:'goodsName'">商品名</div>
</div>
<!--toolbar&searchbar结束-->

<!--已上架商品列表开始-->
<div id="onsal_win"></div>
<!--已上架商品列表开始-->

<!--显示封面图开始-->
<div id="disp_pic">
    <img id="pic_preview" style="width: 100%;height: 100%">
</div>
<!--显示封面图结束-->

<!--商品详细信息开始-->
<iframe src="" id="detail_info"></iframe>
<!--商品详细信息结束-->
</body>
</html>
