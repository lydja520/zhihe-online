<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/2
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商品属性管理</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
</head>
<body>
<!--商品属性数据网格开始-->
<table id="sku_attr_grid"></table>
<div id="attr-grid-tbar">
    <button id="add-attr-btn" class="easyui-linkbutton" iconCls="icon-add">添加</button>
    <button id="edit-attr-btn" class="easyui-linkbutton" iconCls="icon-edit">修改</button>
<%--    <button id="disable-attr-btn" class="easyui-linkbutton" iconCls="icon-cancel">禁用</button>
    <button id="enable-attr-btn" class="easyui-linkbutton" iconCls="icon-ok">启用</button>--%>
    <span style="display: inline-block;width: 10px;"></span>
    <input id="attr-grid-searchbox" prompt="输入关键字搜索" class="easyui-searchbox" style="width: 300px;"
           menu="#attr-grid-searchbox-menu" searcher="searchAttrGrid">
</div>
<div id="attr-grid-searchbox-menu">
    <div name="skuAttName">属性名称</div>
    <div name="goodsAttributeSet.goodsAttSetName">商品类别名称</div>
</div>
<script type="text/javascript">
    $(function () {
        $('#sku_attr_grid').datagrid({
            url: '${basePath}/admin/api/skuAttribute/list',
            fitColumns: true,
            fit: true,
            pagination: true,
            pageSize: 20,
            toolbar: '#attr-grid-tbar',
            singleSelect: true,
            columns: [[
                {field: 'skuAttId', checkbox: true, width: 100, align: 'center'},
                {field: 'skuAttName', title: '属性名', width: 100, align: 'center'},
                {
                    field: 'goodsAttributeSet', title: '对应商品类别', width: 100, align: 'center',
                    formatter: function (value) {
                        if (value) {
                            return value.goodsAttSetName;
                        }
                        return '<span style="color: red">空</span>';
                    }
                },
                {
                    field: 'permit', title: '是否启用', width: 50, align: 'center',
                    formatter(value){
                        if (!value) {
                            return '<span style="color: red;">已禁用</span>'
                        }
                        return "已启用";
                    }
                },
                {field: 'skuAttDesc', title: '属性描述', width: 200, align: 'center'}
            ]]
        });

        /**
         * 添加属性按钮点击事件
         */
        $('#add-attr-btn').click(function () {
            $('#add-dialog').dialog({
                width: 400,
                height: 260,
                title: '添加商品属性',
                modal: true,
                buttons: [{
                    text: '添加',
                    iconCls: 'icon-ok',
                    handler: function () {
                        doAddSkuAttr();
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#add-dialog').dialog('close');
                    }
                }],
                onBeforeOpen: function () {
                    $('#add_form').form('reset');
                }
            })
        });

        /**
         * 编辑属性按钮点击事件
         */
        $('#edit-attr-btn').click(function () {
            var rows = $('#sku_attr_grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要修改的数据！', 'info');
                return;
            }
            var goodsAttr = rows[0];
            $('#edit-dialog').dialog({
                width: 400,
                height: 260,
                title: '修改商品属性',
                modal: true,
                buttons: [{
                    text: '修改',
                    iconCls: 'icon-ok',
                    handler: function () {
                        doEditSkuAttr();
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#edit-dialog').dialog('close');
                    }
                }],
                onBeforeClose: function () {
                    $('#edit_form').form('clear');
                },
                onBeforeOpen: function () {
                    $('#edit_form').form('load', goodsAttr);
                    $('#edit-goods-attr-set-id').val(goodsAttr.goodsAttributeSet.goodsAttSetId);
                    $('#edit-goods-attr-set-name').textbox('setText', goodsAttr.goodsAttributeSet.goodsAttSetName);
                }
            })
        });

        /**
         * 禁用按钮点击事件处理
         */
        $('#disable-attr-btn').click(function () {
            updateAttrPermit(false);
        });

        /**
         * 禁用按钮点击事件处理
         */
        $('#enable-attr-btn').click(function () {
            updateAttrPermit(true);
        });
    });

    /**
     * 搜索商品属性
     * @param value
     * @param name
     */
    function searchAttrGrid(value, name) {
        var param = {};
        if (name && $.trim(value)) {
            param.searchName = name;
            param.searchValue = value;
        }
        $('#sku_attr_grid').datagrid('load', param);
    }

    /**
     *启用或禁用商品属性
     */
    function updateAttrPermit(permit) {
        var rows = $('#sku_attr_grid').datagrid('getChecked');
        if (rows.length < 1) {
            $.messager.alert('提示', '请选择需要操作的数据！', 'info');
            return;
        }
        var attrId = rows[0].skuAttId;
        var url = permit ? '${basePath}/admin/api/skuAttribute/' + attrId + '/enable' : '${basePath}/admin/api/skuAttribute/' + attrId + '/disable';
        $.messager.confirm('提示', '确定要进行此操作吗？', function (val) {
            if (val) {
                $.messager.progress();
                $.ajax({
                    url: url,
                    type: 'post',
                    dataType: 'text',
                    success: function (data) {
                        data = $.parseJSON(data);
                        if (data.code = 200) {
                            $.messager.show({
                                title: '提示',
                                msg: '操作成功！',
                            });
                            $('#sku_attr_grid').datagrid('load');
                        } else {
                            $.messager.alert('提示', data.msg, 'info');
                        }
                    },
                    error: function () {
                        $.messager.alert('提示', '操作失败，请重试！', 'error');
                    },
                    complete: function () {
                        $.messager.progress('close');
                    }
                })
            }
        });
    }

    /**
     * 提交添加商品属性
     */
    function doAddSkuAttr() {
        $('#add_form').form('submit', {
            onSubmit: function () {
                var goodsAttrSetId = $('#add-goods-attr-set-id').val();
                if (!goodsAttrSetId) {
                    $.messager.alert('提示', '请选择所属商品类别', 'info');
                    return false;
                }
                var validate = $('#add_form').form('validate');
                if (validate) {
                    $.messager.progress();
                }
                return validate;
            },
            success: function (data) {
                $.messager.progress('close');
                data = $.parseJSON(data);
                if (data.code == 200) {
                    $.messager.show({
                        title: '提示',
                        msg: '添加成功！'
                    });
                    $('#sku_attr_grid').datagrid('load');
                    $('#add-dialog').dialog('close');
                } else {
                    $.messager.alert('提示', data.msg, 'info');
                }
            }
        });
    }

    /**
     * 提交编辑商品属性
     */
    function doEditSkuAttr() {
        $('#edit_form').form('submit', {
            onSubmit: function () {
                var goodsAttrSetId = $('#edit-goods-attr-set-id').val();
                if (!goodsAttrSetId) {
                    $.messager.alert('提示', '请选择所属商品类别', 'info');
                    return false;
                }
                var validate = $('#edit_form').form('validate');
                if (validate) {
                    $.messager.progress();
                }
                return validate;
            },
            success: function (data) {
                $.messager.progress('close');
                data = $.parseJSON(data);
                if (data.code == 200) {
                    $.messager.show({
                        title: '提示',
                        msg: '修改成功！'
                    });
                    $('#sku_attr_grid').datagrid('load');
                    $('#edit-dialog').dialog('close');
                } else {
                    $.messager.alert('提示', data.msg, 'info');
                }
            }
        });
    }
</script>
<!--商品属性数据网格结束-->

<!--添加商品属性开始-->
<div id="add-dialog">
    <form id="add_form" action="${basePath}/admin/api/skuAttribute/add" method="post">
        <table class="form-table">
            <tr>
                <td>所属商品类别:</td>
                <td>
                    <input type="hidden" id="add-goods-attr-set-id" name="goodsAttributeSet.goodsAttSetId">
                    <input class="easyui-textbox attrSetName" id="add-goods-attr-set-name" data-options="required:true"
                           buttonText="商品类别" editable="false"
                           style="width:200px">
                </td>
            </tr>
            <tr>
                <td>
                    商品属性名:
                </td>
                <td>
                    <input class="easyui-textbox" name="skuAttName" data-options="required:true" style="width: 200px;">
                </td>
            </tr>
            <tr>
                <td>描述：</td>
                <td>
                    <textarea cols="30" rows="5" name="skuAttDesc"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<!--添加sku属性结束-->

<!--修改sku属性开始-->
<div id="edit-dialog">
    <form id="edit_form" action="${basePath}/admin/api/skuAttribute/params/modify" method="post">
        <input type="hidden" name="skuAttId">
        <table class="form-table">
            <tr>
                <td>所属商品类别:</td>
                <td>
                    <input type="hidden" id="edit-goods-attr-set-id" name="goodsAttSetId">
                    <input class="easyui-textbox attrSetName" id="edit-goods-attr-set-name" data-options="required:true"
                           buttonText="商品类别" editable="false"
                           style="width:200px">
                </td>
            </tr>
            <tr>
                <td>
                    商品属性名:
                </td>
                <td>
                    <input class="easyui-textbox" name="skuAttName" data-options="required:true" style="width: 200px;">
                </td>
            </tr>
            <tr>
                <td>描述：</td>
                <td>
                    <textarea cols="30" rows="5" name="skuAttDesc"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<script type="text/javascript">
    /**
     * 添加商品属性选择商品类别按钮点击事件
     */
    $('#add-goods-attr-set-name').textbox({
        onClickButton: function () {
            openGoodsAttrSetDialog();
            checkSetVal('#add-goods-attr-set-id', '#add-goods-attr-set-name');
        }
    });

    /**
     * 编辑商品属性选择商品类别按钮点击事件
     */
    $('#edit-goods-attr-set-name').textbox({
        onClickButton: function () {
            openGoodsAttrSetDialog();
            checkSetVal('#edit-goods-attr-set-id', '#edit-goods-attr-set-name');
        }
    });

    /**
     *设置当选择了商品类别之后赋值目标
     * @param valueTarget
     * @param textTarget
     */
    function checkSetVal(valueTarget, textTarget) {
        $('#check-goods-attr-set-btn').click(null);
        $('#check-goods-attr-set-btn').click(function () {
            var rows = $('#goods-attr-set-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择一条数据', 'info');
                return;
            }
            $(valueTarget).val(rows[0].goodsAttSetId);
            $(textTarget).textbox('setText', rows[0].goodsAttSetName);
            $('#goods-attr-set-gird-dialog').dialog('close');
        });
    }
</script>
<!--修改sku属性结束-->

<!--商品类别列表弹出框开始-->
<div id="goods-attr-set-gird-dialog">
    <table id="goods-attr-set-grid"></table>
    <div id="goods-attr-set-grid-tb">
        <button id="check-goods-attr-set-btn" class="easyui-linkbutton" iconCls="icon-ok">选择</button>
        <span style="display: inline-block;width: 10px;"></span>
        <input id="goods-attr-set-grid-searchbox" prompt="输入关键字搜索" class="easyui-searchbox" style="width: 260px;"
               menu="#goods-attr-set-grid-searchbox-menu" searcher="searchGoodsAttrSet">
    </div>
    <div id="goods-attr-set-grid-searchbox-menu">
        <div name="goodsAttSetName">商品类别</div>
    </div>
</div>
</div>
<script type="text/javascript">
    $(function () {
        /**
         *初始化商品类别数据网格
         */
        $('#goods-attr-set-grid').datagrid({
            url: '${basePath}/admin/api/goodsAttSet/list',
            fit: true,
            fitColumns: true,
            pagination: true,
            singleSelect: true,
            border: false,
            toolbar: '#goods-attr-set-grid-tb',
            columns: [[
                {field: 'goodsAttSetId', checkbox: true, width: 100, align: 'center'},
                {field: 'goodsAttSetName', title: '类别名称', width: 80, align: 'center'},
                {field: 'creatDate', title: '创建时间', width: 80, align: 'center'},
                {field: 'goodsAttSetDesc', title: '描述', width: 100, align: 'center'}
            ]]
        });

        /**
         *初始化商品类别数据网格对话框
         */
        $('#goods-attr-set-gird-dialog').dialog({
            title: '选择商品类别',
            width: 600,
            height: 380,
            modal: true,
            closed: true,
            onBeforeOpen: function () {
                $('#goods-attr-set-grid-searchbox').searchbox('clear');
                $('#goods-attr-set-grid').datagrid('load', {});
            }
        })
    });

    /**
     * 搜索商品类别
     * @param value
     * @param name
     */
    function searchGoodsAttrSet(value, name) {
        var param = {};
        if (name && $.trim(value)) {
            param.searchName = name;
            param.searchValue = value;
        }
        $('#goods-attr-set-grid').datagrid('load', param);
    }

    /**
     * 打开商品类别选择窗口
     */
    function openGoodsAttrSetDialog() {
        $('#goods-attr-set-gird-dialog').dialog('open');
    }
</script>
<!--商品类别列表弹出框结束-->

</body>
</html>
