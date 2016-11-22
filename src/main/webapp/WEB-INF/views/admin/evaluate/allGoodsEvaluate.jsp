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
                url: '${basePath}/admin/api/allGoodsEvaluate/list',
                title: '商品评价',
                fit: true,
                fitColumns: true,
                pagination: true,
                columns: [[
                    {
                        field: 'goods', title: '商品图片', align: 'center', width: 100,
                        formatter: function (data) {
                            return "<img src='" + data.coverImg.url + "' style='width:45px;height:45px;'>"
                        }
                    },
                    {
                        field: 'goodsName', title: '商品名字', align: 'center', width: 100,
                        formatter: function (value, row, index) {
                            console.log(row);
                            return row.goods.goodsName;
                        }
                    },
                    {
                        field: 'user', title: '买家名字', align: 'center', width: 100,
                        formatter: function (data) {
                            return data.userName;
                        }
                    },
                    {
                      field:'userName',title:'买家电话',align:'center',width:100,
                        formatter:function(value,row,index){
                            return row.user.userPhone;
                        }
                    },
                    {
                        field: 'createDate', title: '评论时间', align: 'center',width:100
                    },
                    {field: 'evaluate', title: '商品评价', width: 100, align: 'center'},


                    {
                        field: 'score', title: '商品评分', width: 100, align: 'center',
                        formatter: function (data) {
                            var score = parseFloat(data);
                            var star = "";
                            var halfstar;
                            var darkstar = "";
                            var x = 0;
                            for (var i = 1; i < score+0.5; i++) {
                                star = star + "<img style='width: 16px;height: 16px;margin: 0px;padding: 0px' src='${basePath}/static/images/stars.png'>";
                                x = x+1;
                            }
                            if (score % 1 != 0) {
                                halfstar = "<img style='width: 16px;height: 16px;margin: 0px;padding: 0px' src='${basePath}/static/images/stars-half.png'>";
                                x =x + 1;
                            } else {
                                halfstar = "";
                            }
                            for (var i = 0; i < 5 - x; i++) {
                                darkstar = darkstar+"<img style='width: 16px;height: 16px;margin: 0px;padding: 0px' src='${basePath}/static/images/stars-dark.png'>";
                            }
                            return star + halfstar + darkstar + data + "分";
                        }
                    }
                ]]
            });
        });
    </script>
</head>

<body>

<div id="dg"></div>

</body>
</html>
