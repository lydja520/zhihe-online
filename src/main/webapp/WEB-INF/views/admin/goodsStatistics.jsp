<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/6/22
  Time: 15:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>所有商品清单</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        /*========商品数据网格========*/
        $(document).ready(function () {
            $('#goods_win').datagrid({
                url: '${basePath}/admin/api/all/goodses',
                fit: true,
                title: '所有商品',
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                pageSize: 30,
                rownumbers: true,
                columns: [[
                    {field: '', checkbox: true, width: 100},
                    {
                        field: 'merchantName', title: '商家名称', width: 100,
                        formatter: function (value, rows, index) {
                            return rows.merchant.merchName;
                        }
                    },
                    {
                        field: 'merchantPhone', title: '商家电话', width: 100,
                        formatter: function (value, rows, index) {
                            return rows.merchant.merchTell;
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
                toolbar: '#toobar_searchbar',
            });

            /*============搜索商家==============*/
            $('#search').click(function () {
                $('#goods_win').datagrid('load', {
                    goodsName: $('#goodsName').textbox('getValue'),
                    onsale: $('#onsale').combobox('getValue'),
                    examinState: $('#examinState').combobox('getValue'),
                    merchName: $('#merchName').textbox('getValue'),
                    merchTell: $('#merchTell').textbox('getValue')
                });
            });

            /*============清除搜索==============*/
            $('#clear-search').click(function () {
                $('#merchName').textbox('clear');
                $('#merchTell').textbox('clear');
                $('#goodsName').textbox('clear');
                $('#examinState').combobox('reset');
                $('#onsale').combobox('reset');

//                goods.goodsName = '';
//                goods.onsale = '';
//                goods.examinState = '';
//                merchant.merchName = '';
//                merhcant.merchTell = '';

                $('#goods_win').datagrid('load', {

                });
            });

        });

        /*========点击预览商品详细信息========*/
        function previewDetails(value) {
            $('#detail_info').dialog({
                title: '商品详细信息',
                iconCls: 'icon-preview',
                width: '800',
                height: '600',
                modal: true,
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
    &nbsp;&nbsp;商家名称:
    <input id="merchName" class="easyui-textbox" data-options="prompt:'请输入商家名称'">
    &nbsp;&nbsp;商家电话:
    <input id="merchTell" class="easyui-textbox" data-options="prompt:'请输入商家电话'">
    &nbsp;&nbsp;商品名称:
    <input id="goodsName" class="easyui-textbox" data-options="prompt:'请输入商品名'">
    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>

    是否启用:
    <select class="easyui-combobox" id="onsale" data-options="editable:false,panelHeight:80" style="width:70px;">
        <option value="">全部</option>
        <option value="true">上架</option>
        <option value="false">下架</option>
    </select>&nbsp;&nbsp;
    是否通过审核:
    <select class="easyui-combobox" id="examinState"
            data-options="editable:false,panelHeight:95,panelWidth:100" style="width:70px;">
        <option value="">全部</option>
        <option value="3">通过</option>
        <option value="2">待审核</option>
        <option value="4">未通过</option>
        <option value="1">没申请</option>
    </select>
    <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索</a>
    <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清除搜索</a>

</div>
<!--toolbar&searchbar结束-->

<!--显示商品数据网格开始-->
<div id="goods_win"></div>
<!--显示商品数据网格结束-->

<!--商品详细信息开始-->
<iframe src="" id="detail_info"></iframe>
<!--商品详细信息结束-->

</body>
</html>
