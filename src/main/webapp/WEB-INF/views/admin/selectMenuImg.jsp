<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/1/20
  Time: 10:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script>
        $(function () {
            $('img').each(function (index, element) {
                $(element).click(function () {
                    var src = $(this).attr('src');
                    $('img').each(function (i, e) {
                        if ($(e).attr('src') != src) {
                            $(e).css('width','16px');
                            $(e).css('height','16px');
                            $(e).css('border', 'none');
                        } else {
                            $(e).css('width','14px');
                            $(e).css('height','14px');
                            $(e).css('border', '1px solid red');
                            $(window.parent.document).find("#imgValue").attr('value', $(e).attr('data-icon'));
                            $(window.parent.document).find("#imgPreview").attr('src', $(e).attr('src'));
                        }
                    });
                });
            });
        });
    </script>
    <style>
        img {
            width: 16px;
            height: 16px;
        }
    </style>
</head>
<body>
<div>
    <img src="${basePath}/static/css/icons/alliance.png" data-icon="icon-alliance">
    <img src="${basePath}/static/css/icons/application_cascade.png" data-icon="icon-application-cascade">
    <img src="${basePath}/static/css/icons/application_form_edit.png"  data-icon="icon-application-form-edit">
    <img src="${basePath}/static/css/icons/bricks.png" data-icon="icon-bricks">
    <img src="${basePath}/static/css/icons/cart.png"data-icon="icon-cart">
    <img src="${basePath}/static/css/icons/cart_delete.png" data-icon="icon-cart-del">
    <img src="${basePath}/static/css/icons/cart_remove.png" data-icon="icon-cart-remove">
    <img src="${basePath}/static/css/icons/chart_curve.png" data-icon="icon-chart-curve">
    <img src="${basePath}/static/css/icons/creditcards.png" data-icon="icon-creditcards">
    <img src="${basePath}/static/css/icons/discount.png" data-icon="icon-discount">
    <img src="${basePath}/static/css/icons/folder.png" data-icon="icon-folder">
    <img src="${basePath}/static/css/icons/notonsal.png" data-icon="icon-notonsal">
    <img src="${basePath}/static/css/icons/onsal.png" data-icon="icon-onsal">
    <img src="${basePath}/static/css/icons/preview.png" data-icon="icon-preview">
    <img src="${basePath}/static/css/icons/redpack.png" data-icon="redpack">
    <img src="${basePath}/static/css/icons/table_edit.png" data-icon="icon-edit2">
    <img src="${basePath}/static/css/icons/user_home.png" data-icon="icon-home">
    <img src="${basePath}/static/css/icons/wrench.png" data-icon="icon-wrench">
    <img src="${basePath}/static/css/icons/users.png" data-icon="icon-users">
    <img src="${basePath}/static/css/icons/user_group.png"data-icon="icon-users-group">
    <img src="${basePath}/static/css/icons/book.png" data-icon="icon-book">
    <img src="${basePath}/static/css/icons/menu.png" data-icon="icon-menu">
    <img src="${basePath}/static/css/icons/user.png" data-icon="icon-user">
    <img src="${basePath}/static/css/icons/calendar.png" data-icon="icon-calender">
    <img src="${basePath}/static/css/icons/calendar_edit.png" data-icon="icon-calender-edit">
    <img src="${basePath}/static/css/icons/chart_bar.png" data-icon="icon-chart-bar">
    <img src="${basePath}/static/css/icons/messg.png" data-icon="icon-messager">
    <img src="${basePath}/static/css/icons/photo.png" data-icon="icon-photo">
    <img src="${basePath}/static/css/icons/key.png" data-icon="icon-key">
    <img src="${basePath}/static/css/icons/lock.png" data-icon="icon-lock">
    <img src="${basePath}/static/css/icons/plugin_edit.png" data-icon="icon-plugin">
    <img src="${basePath}/static/css/icons/report.png" data-icon="icon-report">
    <img src="${basePath}/static/css/icons/note_add.png" data-icon="icon-note">
    <img src="${basePath}/static/css/icons/categ.png" data-icon="icon-categ">
</div>
</body>
</html>
