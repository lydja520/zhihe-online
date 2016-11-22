<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商家关注用户查询</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"></c:set>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script>
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript">
        function initFocusGrid() {
            var user;
            $("#focus_grid").datagrid({
                url: '/admin/api/focusMerchant/list',
                columns: [[
                    {
                        field: 'user', title: '用户头像', width: 100, align: 'center',
                        formatter: function (value) {
                            user = value;
                            if (user.portrait != null) {
                                return "<img style='width:45px;height:45px;margin:1px auto 1px auto' src='" + value.portrait.url + "'/>";
                            } else {
                                return "<img style='width:45px;height:45px;margin:1px auto 1px auto' src='${basePath}/static/images/unknow.jpg'/>";
                            }
                        }
                    },
                    {
                        field: 'aaa', title: '用户名', width: 100, align: 'center',
                        formatter: function () {
                            return user.userName;
                        }
                    },
                    {
                        field: 'bbb', title: '职业', width: 100, align: 'center',
                        formatter: function () {
                            return user.occupation;
                        }
                    },
                    {
                        field: 'income', title: '收入', width: 60, align: 'center',
                        formatter: function () {
                            return user.income;
                        }
                    },
                    {
                        field: 'birthday', title: '生日', width: 60, align: 'center',
                        formatter: function () {
                            return user.birthday;
                        }
                    },
                    {
                        field: 'ccc', title: '所在区域', align: 'center', width: 100,
                        formatter: function () {
                            return user.area ? user.area.fullName : '';
                        }
                    },
                    {field: 'focusDate', title: '关注时间', align: 'center', width: 100, sortable: true}
                ]],
                fitColumns: true,
                toolbar: '#tool-bar',
                pagination: true,
                singleSelect: true,
                sortName: 'focusDate',
                pageSize: 20,
                shadow: true,
                fit: true
            });
        }
        ;
        function initSearchBox() {
            $("#search-box").searchbox({
                width: 300,
                menu: '#searchbox_menu',
                prompt: '输入关键字检索',
                searcher: function (name, value) {
                    var params = {};
                    if (name, $.trim(value)) {
                        params.searchName = $.trim(value);
                        params.searchValue = name;
                    }
                    $("#focus_grid").datagrid('load', params);
                }
            });
        }

        $(function () {
            initFocusGrid();
            initSearchBox();
        });

    </script>
</head>
<body>
<div id="tool-bar" class="toolbar-container">
    <input id="search-box"/>
</div>
<div id="searchbox_menu" style="width:120px">
    <div data-options="name:'user.userName'">会员名称</div>
</div>
<table id="focus_grid"></table>

</div>
</body>
</html>