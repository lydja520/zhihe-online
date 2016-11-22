<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/6/15
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<title>浏览统计</title>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
<script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript">
  $(function () {
    /*===========数据网格========*/
    $('#dg').datagrid({
      url: '${basePath}/admin/api/merchantbrowse/list',
//            title: '用户列表',
      fit: true,
      border: false,
      fitColumns: true,
      pageSize: 20,
      pagination: true,
      singleSelect: true,
      striped: true,
      rownumbers: true,
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
          field: 'merchant.merchName', title: '商品所属商家', width: 100, align: 'center',
          formatter: function (value,row,index) {
            return "";
          }
        },
        {
          field: 'merchant.merchTell', title: '商家联系电话', width: 100, align: 'center',
          formatter: function (value, row, index) {
            return "";
          }
        },
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
          field: 'examinState', title: '是否通过审核', width: 100, align: 'center',
          formatter: function (data) {
            if (data == <%=Constant.EXAMINE_STATE_SUBMITED%>) {
              return "<span style='color: blue'>待审核中</span>";
            } else if (data == <%=Constant.EXAMINE_STATE_EXAMINED_OK%>) {
              return "<span style='color: green'>审核通过</span>";
            }
            return "<span style='color: red'>审核未通过</span>";
          }
        },
        {field: 'examinMsg', title: '审核信息', width: 100, align: 'center'},
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
        {field: 'stock', title: '原始库存量', width: 100, align: 'center'},
        {field: 'volume', title: '销量', width: 100, align: 'center', sortable: true},
        {field: 'currentStock', title: '现有库存量', width: 100, align: 'center', sortable: true},
        {field: 'createDate', title: '添加时间', width: 100, align: 'center', sortable: true,},
        {
          field: 'goodsId', title: '商品详细信息', width: 100, align: 'center',
          formatter: function (value) {
            return "<button onclick=\'previewDetails(this.value)\' value=" + value + ">点击查看</button>";
          }
        }
      ]],
      toolbar: "#grid_toolbar"

    });

    /*================用户搜索================*/
    $('#search').click(function () {
      $('#dg').datagrid('load', {
        merchName: "",
        goodsName: "",
        initTime: $('#start-time').datebox('getValue'),
        endTime: $('#end-time').datebox('getValue')
      });
    });
    /*===========清楚搜索数据===========*/
    $('#clear-search').click(function () {
      $('#dg').datagrid('load', {});
      $('#search-form').form('reset');
    });
  })
</script>
</head>

<body class="easyui-layout">
<%--<div data-options="region:'center'">--%>
  <%--<div id="grid_toolbar" style="height:30px">--%>
    <%--<form id="search-form">--%>
      <%--商品名:<input class="easyui-textbox" id="goods-name" prompt="输入名称"/>&nbsp;--%>
      <%--商家名:<input class="easyui-textbox" id="merchant-name" prompt="输入名称"/>&nbsp;--%>
      <%--浏览时间：从<input id="start-time" type="text" class="easyui-datebox"--%>
                   <%--data-options="editable:false" prompt="请选择开始时间"/>--%>
      <%--至<input id="end-time" type="text" class="easyui-datebox"--%>
              <%--data-options="editable:false" prompt="请输入结束时间"/>--%>
      <%--<a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">--%>
        <%--搜索--%>
      <%--</a>--%>
      <%--<a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">--%>
        <%--清除搜索--%>
      <%--</a>--%>
    <%--</form>--%>
  <%--</div>--%>
  <%--<div id="dg"></div>--%>
<%--</div>--%>
</body>
</html>

