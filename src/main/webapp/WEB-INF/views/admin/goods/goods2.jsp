<%--
  Created by IntelliJ IDEA.
  User: ydc
  Date: 16-7-6
  Time: 下午2:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>全部商品列表</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/easyui/themes/icon.css"/>
    <script type="text/javascript" src="${basePath}/static/core/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#tg').treegrid({
                url: '${basePath}/admin/api/goods/list',
                idField: 'goodsId',
                treeField: 'goodsName',
                rownumbers: true,
                method: 'get',
                fit: true,
                fitColumns: true,
                border: false,
                pagination: true,
                loadFilter: function (data) {
                    if (data.rows) {
                        data = data.rows;
                    };
                    console.log(data);
/*                    pagerFilter(data);*/
                    return TreeGridShow(data);
                },
                frozenColumns: [[
                    {field: '', checkbox: true},
                    {field: 'goodsName', title: '商品名', width: 150}
                ]],
                columns: [[
                    {
                        field: 'imgUrl', title: '封面图', width: 100, align: 'center',
                        formatter: function (url) {
                            if (url) {
                                return "<div style='width: 50px;height: 50px;margin: 1px auto 1px auto'><img style='width: 50px;height: 50px;' src='" + url + "'></div>"
                            }
                            return "<span style='color: red' '>无封面图</span>"
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
                /*                onBeforeLoad: function(row,param){
                 if (!row) { // load top level rows
                 param.id = 0;   // set id=0, indicate to load new page rows
                 }
                 },*/
                /*
                 onBeforeExpand: function (row) {
                 //动态设置展开查询的url
                 var url ='/admin/api/goods/list?goodsId='+row.goodsId;
                 $("#tg").treegrid("options").url = url;
                 return true;
                 },*/
                /*                onExpand: function (row) {
                 var children = $("#tg").treegrid('getChildren', row.Id);
                 if (children.length >= 0) {
                 row.leaf = true;
                 $("#tg").treegrid('refresh', row.id);
                 }
                 },*/
                toolbar: [
                    {
                        text: '全部展开',
                        iconCls: 'icon-redo',
                        handler: function () {
                            var node = $('#tg').treegrid('getSelected');
                            if (node) {
                                $('#tg').treegrid('expandAll', node.cid);
                            } else {
                                $('#tg').treegrid('expandAll');
                            }
                        }
                    }, '-', {
                        text: '全部折叠',
                        iconCls: 'icon-undo',
                        handler: function () {
                            var node = $('#tg').treegrid('getSelected');
                            if (node) {
                                $('#tg').treegrid('collapseAll', node.cid);
                            } else {
                                $('#tg').treegrid('collapseAll');
                            }
                        }
                    }, '-', {
                        text: '刷新',
                        iconCls: 'icon-reload',
                        handler: function () {
                            $('#tg').treegrid('reload');
                        }
                    }],
            });



/*            var appendMethod = $.fn.treegrid.methods.append;
            var loadDataMethod = $.fn.treegrid.methods.loadData;
            $.extend($.fn.treegrid.methods, {
                clientPaging: function (jq) {
                    return jq.each(function () {
                        var state = $(this).data('treegrid');
                        var opts = state.options;
                        opts.loadFilter = pagerFilter;
                        var onBeforeLoad = opts.onBeforeLoad;
                        opts.onBeforeLoad = function (row, param) {
                            state.originalRows = null;
                            onBeforeLoad.call(this, row, param);
                        }
                        $(this).treegrid('loadData', state.data);
                        $(this).treegrid('reload');
                    });
                },
                loadData: function (jq, data) {
                    jq.each(function () {
                        $(this).data('treegrid').originalRows = null;
                    });
                    return loadDataMethod.call($.fn.treegrid.methods, jq, data);
                },
                append: function (jq, param) {
                    return jq.each(function () {
                        var state = $(this).data('treegrid');
                        if (state.options.loadFilter == pagerFilter) {
                            $.map(param.data, function (row) {
                                row._parentId = row._parentId || param.parent;
                                state.originalRows.push(row);
                            });
                            $(this).treegrid('loadData', state.originalRows);
                        } else {
                            appendMethod.call($.fn.treegrid.methods, jq, param);
                        }
                    })
                }
            });*/
        });
/*        function pagerFilter(data) {
            if ($.isArray(data)) {    // is array
                data = {
                    total: data.total,
                    rows: data
                }
            }
            var dg = $('#tg');
            console.log(dg);
            var state = dg.data('treegrid');
            var opts = dg.treegrid('options');
            var pager = dg.treegrid('getPager');
            pager.pagination({
                onSelectPage: function (pageNum, pageSize) {
                    opts.pageNumber = pageNum;
                    opts.pageSize = pageSize;
                    pager.pagination('refresh', {
                        pageNumber: pageNum,
                        pageSize: pageSize
                    });
                    dg.treegrid('loadData', state.originalRows);
                }
            });
            if (!state.originalRows) {
                state.originalRows = data.rows;
            }
            var topRows = [];
            var childRows = [];
            $.map(state.originalRows, function (row) {
                row._parentId ? childRows.push(row) : topRows.push(row);
            });
            data.total = topRows.length;
            var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
            var end = start + parseInt(opts.pageSize);
            data.rows = $.extend(true, [], topRows.slice(start, end).concat(childRows));
            return data;
        }*/

        function TreeGridShow(data) {
            $(data).each(function (index, item) {
                if (!item.children) {
                    /*                    item.state = "open";*/
                }
                else {
                    item.state = "closed";
                    TreeGridShow(item.children);
                }
            });
            return data;
        }
    </script>

</head>
<body>

<table id="tg"></table>

</body>
</html>
