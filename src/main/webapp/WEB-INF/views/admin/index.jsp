<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="render" content="webkit">
    <title>挚合电商后台管理系统</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/css/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $('.side-tree li').bind("click", function () {
                var item = $(this).find('a')[0];
                var title = $(item).text();
                var url = $(item).data('link');
                var iconCls = $(item).data('icon');
                addTab(title, url, iconCls);
            });
            $('#logout-btn').click(function () {
                $.messager.confirm('提示', '确实要退出当前账号吗？', function (r) {
                    if (r) {
                        href = "${basePth}/admin/logout";
                        window.location.href = href;
                    }
                });
            });
        });

        /**
         * 添加菜单项
         *
         * Param title 名称
         * Param url 链接
         * Param iconCls 图标样式
         */
        function addTab(title, url, iconCls) {
            var tabPanel = $('#tabs');
            if (!tabPanel.tabs('exists', title)) {
                tabPanel.tabs('add', {
                    title: title,
                    iconCls: iconCls,
                    content: "<div id='scroll-wrap'><iframe frameborder='0' scrolling='auto' src='" + url + "' style='width:100%;height:100%;' ></iframe></div>",
                    border: false,
                    closable: true,
                    cls: 'pd3',
                });
            }
            else {
                tabPanel.tabs('select', title);
            }
        }

    </script>
    <style type="text/css">
        #scroll-wrap {
            width: 100%;
            height: 100%;
            -webkit-overflow-scrolling: touch;
            overflow-y: auto;
        }
    </style>
</head>

<body class="easyui-layout">

<!-- begin of header -->
<div class="header" data-options="region:'north',border:false,split:false">
    <div class="header-left">
        <div class="index-web-title">实淘后台管理系统</div>
    </div>
    <div class="header-right">
        <p>
            <strong class="easyui-tooltip">${currentAdmin.adminCode}</strong>，欢迎您！
        </p>

        <p>
            <a href="javascript:void(0)" id="logout-btn">安全退出</a>
        </p>
    </div>
</div>
<!-- end of header -->

<!-- begin of sidebar -->
<div class="sidebar"
     data-options="region:'west',split:false,border:true,title:'导航菜单',collapsible:true,maxWidth:250,minWidth:180">

    <div class="easyui-accordion" data-options="border:false,fit:true">
        <c:forEach var="root" items="${menus}">
            <div title="${root.menuName}" data-options="iconCls:'${root.menuImg}'">
                <ul class="easyui-tree side-tree">
                    <c:forEach var="child" items="${root.childList}">
                        <li iconCls='${child.menuImg}'>
                            <a data-icon="${child.menuImg}"
                               data-link="${basePath}${child.menuUrl}">${child.menuName}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:forEach>
        <div title="信息维护" data-options="iconCls:'icon-application-form-edit'">
            <ul class="easyui-tree side-tree">
                <c:if test="${currentAdmin.merchant != null}">
                    <li iconCls="icon-application-form-edit">
                        <a data-icon="icon-application-form-edit"
                           data-link="${basePath}/admin/merchantInfoPage">商家详细信息</a>
                    </li>
                    <li iconCls="icon-report">
                        <a data-icon="icon-report" data-link="${basePath}/admin/api/merchant/editMerchantRegisterInfo">商家信息修改</a>
                    </li>
                    <li iconCls="icon-photo" data-icon="icon-photo">
                        <a data-icon="icon-photo" data-link="${basePath}/admin/updateImg">商家图片修改</a>
                    </li>
                </c:if>
                <li iconCls="icon-lock" data-icon="icon-lock">
                    <a data-icon="icon-lock" data-link="${basePath}/admin/changePwd">密码修改</a>
                </li>
                <c:if test="${currentAdmin.merchant !=null}">
                    <li iconCls="icon-lock" data-icon="icon-lock">
                        <a data-icon="icon-lock" data-link="${basePath}/admin/adminEditToMerchant">账号修改</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</div>
<!-- end of sidebar -->

<!-- begin of main -->
<div data-options="region:'center',border:false,split:true">
    <div id="tabs" class="easyui-tabs" data-options="border:false,fit:true,tabPosition:'top'">
        <c:if test="${requestScope.admin.merchant.examinState == 3}">
            <div title="首页" href="${basePath}/admin/notify" data-options="closable:false,iconCls:'icon-home'"></div>
        </c:if>
        <c:if test="${requestScope.admin.merchant.examinState == 2}">
            <div title="首页" data-options="closable:false,iconCls:'icon-home'">
                <div style="font-size: 18px;text-align: center;margin-top: 20px">
                    官方正在努力审核中！
                </div>
            </div>
        </c:if>
        <c:if test="${requestScope.admin.merchant.examinState == 4}">
            <div title="首页" data-options="closable:false,iconCls:'icon-home'">
                <div style="font-size: 18px;text-align:center;color: red;margin-top: 20px">
                    审核未通过，请按规定修改好相关信息，在提交审核！
                </div>
                <div style="font-size: 16px;text-align: center;margin-top: 20px;">
                    未通过审核原因：${admin.merchant.examinMsg}
                </div>
            </div>
        </c:if>
    </div>
</div>
<!-- end of main -->

<!-- begin of footer -->
<div class="footer" data-options="region:'south',border:true,split:false">
    &copy; 2015 云南挚合科技有限公司 | 滇ICP备16003241号
</div>
<!-- end of footer -->
</body>
</html>
