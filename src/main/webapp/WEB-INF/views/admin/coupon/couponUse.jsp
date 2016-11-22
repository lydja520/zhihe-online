<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/5/11
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>所有商家优惠券</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#coupon-use-win').dialog({
                title: '查询和使用优惠券',
                iconCls: 'icon-search',
                width: 400,
                height: 210,
                modal: true,
                closable: false,
                draggable: false,
                buttons: [{
                    text: '使用',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#use-coupon-form').form({
                            url: '${basePath}/admin/api/merchant/coupon/use',
                            success: function (info) {
                                info = $.parseJSON(info);
                                if (info.success) {
                                    $.messager.alert('提示', info.msg, 'warning');
                                } else {
                                    $.messager.alert('提示', info.msg, 'warning');
                                }
                            }
                        });
                        $('#use-coupon-form').submit();
                    }
                }, {
                    text: '清空',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#coupon-use-win').form('clear');
                    }
                }]
            });
        });
    </script>
    <style>
        ul, li {
            list-style-type: none;
            padding: 0px;
            margin: 0px;
        }

        li {
            float: left;
            margin: 10px 0px;
        }

        .win-lable {
            width: 40%;
            text-align: center;
        }

        .win-content {
            width: 60%;
        }

        .clear {
            clear: both;
        }
    </style>
</head>
<body>
<div id="coupon-use-win">
    <form id="use-coupon-form">
        <ul class="use-win" style="margin: auto;width: 80%;padding: 20px 0px">
            <li class="win-lable">
                优惠券码：
            </li>
            <li class="win-content">
                <input class="easyui-textbox" required name="couponCode"/>
            </li>
            <li class="win-lable">
                用户电话号码：
            </li>
            <li class="win-content">
                <input class="easyui-textbox" required name="userPhone"/>
            </li>
            <div class="clear"></div>
        </ul>
    </form>
</div>
</body>
</html>
