<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%@ page import="com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/18
  Time: 14:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>验证码短息</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        $(function () {
            /*=========初始化数据网格========*/
            $('#messager_win').datagrid({
                url: '${basePath}/admin/api/messager/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                rownumbers: true,
                columns: [[
                    {field: 'phoneNumber', title: '电话号码', width: 100, align: 'center'},
                    {field: 'securityCode', title: '验证码', width: 100, align: 'center'},
                    {field: 'securityMsg', title: '验证消息', width: 100, align: 'center'},
                    {
                        field: 'securityState', title: '验证状态', width: 100, align: 'center',
                        formatter:function(data){
                            if(data == <%=Constant.SECURITY_REGISTER%>){
                                return "注册";
                            }else if(data == <%=Constant.SECURITY_ALTERPWD%>){
                                return "修改密码";
                            }
                        }
                    },
                    {field: 'sendDate', title: '发送时间', width: 100, align: 'center',sortable:true},
                    {
                        field: 'validity', title: '验证码过期时间', width: 100, align: 'center',
                        formatter:function(data){
                            return data;
                        }
                    }
                ]],
                toolbar:'#toolbar'
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
                    $('#messager_win').datagrid('load', param);
                }
            });
        });
    </script>
</head>
<body>

<!--显示商品数据网格开始-->
<div id="messager_win"></div>
<!--显示商品数据网格结束-->

<!--searchbar开始-->
<div id="toolbar">
    <input id="grid_searchbox">
</div>
<div id="searchbox_menu">
    <div data-options="name:'phoneNumber'">电话号码</div>
</div>
<!--searchbar结束-->

</body>
</html>
