<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/4/5
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
    <link rel="stylesheet" href="${basePath}/static/easyui/themes/default/easyui.css">
    <link rel="stylesheet" href="${basePath}/static/easyui/themes/icon.css">
    <script type="text/javascript">
        $(document).ready(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/goodsEvaluate/list',
                title: '商品评价',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect:true,
                rownumbers:true,
                pageSize:20,
                columns: [[
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
                        field: 'price', title: '价格', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + " 元";
                        }
                    },
                    {
                        field: 'volume', title: '销量', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + " 件";
                        }
                    },
                    {
                        field: 'currentStock', title: '现有库存量', width: 100, align: 'center',
                        formatter: function (value) {
                            return value + " 件";
                        }
                    },
                    {
                        field: 'goodsId', title: '商品评价', width: 100, align: 'center',
                        formatter: function (value) {
                            return "<button onclick='previewDetails(this.value)' value=" + value + ">查看评价</button>";
                        }
                    }
                ]],
            });
        });

        /*========点击预览商品详细信息========*/
        function previewDetails(value) {
            $('#detail_info').dialog({
                title: '商品评价',
                iconCls: 'icon-preview',
                width: 800,
                height: 480,
                modal: true,
                onBeforeOpen: function () {
                    $('#detail_info').attr('src', "/admin/goods/"+value+"/evaluateDetail");
                }
            });
        }
    </script>
</head>
<body>

<div id="dg"></div>

<!--商品详细信息开始-->
<iframe src="" id="detail_info"></iframe>
<!--商品详细信息结束-->

</body>
</html>
