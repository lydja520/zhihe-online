<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/6/15
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<title>红包统计</title>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/css/style.css" />
<script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript">
    $(function () {
        /*===========数据网格========*/
        $('#dg').datagrid({
            url: '${basePath}/admin/api/redEnvelopStatistics/list',
//            title: '用户列表',
            fit: true,
            border: false,
            fitColumns: true,
            pageSize: 30,
            pagination: true,
            singleSelect: true,
            striped: true,
            rownumbers: true,
            columns: [[
                {
                    field: 'merchant.merchName', title: '商家名', width: 100, align: 'center',
                    formatter: function (value, rows, index) {
                        return rows.merchant.merchName
                    }
                },
                {
                    field: 'activity.activitName', title: '活动名', width: 100, align: 'center',
                    formatter: function (value, rows, index) {
                        return rows.activity.activitName;
                    }
                },
                {
                    field: 'totalMoney', title: '红包金额', width: 100, align: 'center'
                },
                {
                    field: 'numbers', title: '红包个数', width: 100, align: 'center'
                },
                {
                    field:'envelopMsg',title:'红包留言',width:100,align:'center'
                },
                {
                    field: 'createDate', title: '红包创建时间', width: 100, sortable: true,align: 'center'
                },
                {
                    field: 'payState', title: '红包是否充值', width: 100, align: 'center',
                    formatter: function (value, rows, index) {
                        if (value == true) {
                            return "<span style='color:green'>已充值</span>"
                        }
                        if (value == false) {
                            return "<span style='color:red'>未充值</span>";
                        }
                    }
                },
                {
                    field: 'sended', title: '红包是否发出', width: 100, align: 'center',
                    formatter: function (value, rows, index) {
                        if (value == true) {
                            return "<span style='color:green'>已发出</span>";
                        } else {
                            return "<span style='color:red'>未发出</span>";
                        }
                    }
                },
                {
                    field: 'envelopId', title: '抢到红包用户', width: 100, align: 'center',
                    formatter: function (value, rows, index) {
                        return "<button value=" + value + " onclick='userRedEnvelop(this.value)'>红包详情</button>"
                    }
                }
            ]],
            toolbar: "#grid_toolbar"

        });

        /*================用户搜索================*/
        $('#search').click(function () {
            $('#dg').datagrid('load', {
                merchName: $('#merchant-name').textbox('getValue'),
                activitName: $('#activity-name').textbox('getValue'),
                initTime: $('#start-time').datebox('getValue'),
                endTime: $('#end-time').datebox('getValue')
            });
        });
        /*===========清楚搜索数据===========*/
        $('#clear-search').click(function () {
            $('#dg').datagrid('load', {});
            $('#search-form').form('reset');
        });

        /*==========导出excel============*/
        $('#export').click(function () {
            var param = 'activitName=' + $('#activity-name').textbox('getValue')  + '&merchName='
                    + $('#merchant-name').textbox('getValue') + '&initTime=' + $('#start-time').datebox('getValue') + '&endTime=' + $('#end-time').datebox('getValue');
            $.messager.confirm('确认下载', '确认要导出当前查询出来的订单数据吗', function (b) {
                if (b) {
                    window.location.href = '${basePath}/admin/api/redEnvelopStatistics/export?' + param;
                }
            });
        });

    })

    function userRedEnvelop(envelopId) {
        $('#preview').dialog({
            title: '抢到红包用户',
            width: 750,
            height: 400,
            modal: true,
            onBeforeOpen: function () {
                $('#preview').attr('src', '${basePath}/admin/api/userRedEnvelop/' + envelopId + '');
            }
        });
    }

</script>
</head>

<body class="easyui-layout">
<div data-options="region:'center'">
    <div id="grid_toolbar" style="height:30px">
        <form id="search-form">
            活动名称:<input class="easyui-textbox" id="activity-name" prompt="输入活动名称"/>&nbsp;
            商家名:<input class="easyui-textbox" id="merchant-name" prompt="输入商家名称"/>&nbsp;
            红包创建时间：从<input id="start-time" type="text" class="easyui-datebox"
                           data-options="editable:false" prompt="请选择开始时间"/>
            至<input id="end-time" type="text" class="easyui-datebox"
                    data-options="editable:false" prompt="请输入结束时间"/>
            <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">
                搜索
            </a>
            <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">
                清除搜索
            </a>
            <a id="export" class="easyui-linkbutton" data-options="iconCls:'icon-save'">
                &nbsp;导出&nbsp;
            </a>
        </form>
    </div>
    <div id="dg"></div>
</div>
<%--user红包--%>
<iframe id="preview"></iframe>
</body>
</html>


