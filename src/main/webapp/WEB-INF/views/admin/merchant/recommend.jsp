<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<head>
    <title>商品推荐</title>
    <%@ include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        function addRecommendGoods() {
            var rows = $('#goods-grid').datagrid('getChecked');
            if (!rows || rows.length < 1) {
                $.messager.alert('提示', '请选择数据！', 'info');
                return;
            }
            console.log(rows[0]);
            $('#goodsId').textbox('setValue', rows[0].goodsId);
            $('#goodsId').textbox('setText', rows[0].goodsName);
            $('#goods-dialog').dialog('close');
        }

        function formatGoodName(value) {
            return value.goodsName;
        }

        function searchGoods(value, name) {
            var param = {};
            if (name && $.trim(name) != "") {
                param.searchName = name;
                param.searchValue = value;
            }
            loadGoodsGrid(param);
        }

        function loadGoodsGrid(param) {
            if (param) {
                $('#goods-grid').datagrid('load', param);
            }
            else {
                $('#goods-grid').datagrid('load');
            }
        }

        function showGoodsGrid() {
            $('#goods-search').searchbox('clear');
            searchGoods();
            $('#goods-dialog').dialog('open');
        }

        function onSubmitSuccess(data) {
            onAfterEdit(data, 'recommend-grid', function () {
                $('#form-dialog').dialog('close');
            })
        }
        function onEditSuccess(data) {
            onAfterEdit(data, 'recommend-grid', function () {
                $('#edit-dialog').dialog('close');
            })
        }

        function deleteRecommend() {
            var rows = $('#recommend-grid').datagrid('getChecked');
            if (!rows || rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据！', 'info');
                return;
            }
            request({
                url: '${root}/admin/api/recommend/' + rows[0].recommendId + '/delete',
                success: function (data) {
                    if (data.success) {
                        $.messager.show({
                            title: '提示',
                            msg: data.msg
                        })
                        $('#recommend-grid').datagrid('load');
                    }
                    else {
                        $.messager.alert('提示', data.msg, 'error');
                    }
                },
                error: function () {
                    $.messager.alert('提示', '出错了', 'error');
                }
            })
        }

        function showAndInitEditData() {
            var rows = $('#recommend-grid').datagrid('getChecked');
            if (!rows || rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据！', 'info');
                return;
            }
            $('#edit-form').form({
                url: '${root}/admin/api/recommend/' + rows[0].recommendId + '/edit'
            })
            $('#edit-form').form('load', rows[0]);
            $('#edit-dialog').dialog('open');
        }
    </script>
</head>
<e:body>
    <e:datagrid id="recommend-grid" fit="true" fitColumns="true"
                url="${root}/admin/api/merchant/${sessionScope.currentMerchantId}/recommends"
                pagination="true" singleSelect="true">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="recommendId" checkbox="true"/>
            <e:column field="goods" title="商品名称" align="center" width="20" formatter="formatGoodName"/>
            <e:column field="createDate" title="推荐时间" align="center" width="20"/>
            <e:column field="orderIndex" title="显示顺序" sortable="true" align="center" width="10"/>
            <e:column field="reason" title="推荐理由" align="center" width="50"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-recommend" text="添加推荐" iconCls="icon-add">
                <e:event event="click" target="form-dialog" action="open"/>
            </e:button>
            <e:button id="edit-recommend" text="修改推荐信息" iconCls="icon-edit">
                <e:eventListener listener="showAndInitEditData" event="click"/>
            </e:button>
            <e:button id="remove-recommend" text="取消推荐" iconCls="icon-remove">
                <e:eventListener listener="deleteRecommend" event="click"/>
            </e:button>
        </e:facet>
    </e:datagrid>
    <e:dialog id="form-dialog" style="width:450px;height:260px" iconCls="icon-tip" modal="true" closed="true"
              closable="true" title="推荐商品">
        <e:event event="onBeforeClose" target="data-form" action="reset"/>
        <e:form id="data-form" method="post" url="${root}/admin/api/recommend/add">
            <e:eventListener event="success" listener="onSubmitSuccess"/>
            <input type="hidden" value="${sessionScope.currentMerchantId}" name="merchant.merchantId">
            <table class="form-table">
                <tr>
                    <td><label>商品：</label></td>
                    <td>
                        <input class="easyui-textbox" name="goods.goodsId" id="goodsId"
                               data-options="buttonText:'选择商品',required:true,onClickButton:showGoodsGrid,editable:false"
                               style="width:200px;"/>
                    </td>
                </tr>
                <tr>
                    <td><label>显示顺序：</label></td>
                    <td>
                        <input type="text" class="easyui-numberbox" name="orderIndex" value="1"
                               data-options="min:0,precision:0,required:true" style="width: 200px"/>

                    </td>
                </tr>
                <tr>
                    <td><label>推荐理由：</label></td>
                    <td>
                        <textarea rows="5" cols="40" name="reason"></textarea>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="submit-form" iconCls="icon-save" text="保存">
                <e:event event="click" target="data-form" action="submit"/>
            </e:button>
            <e:button id="cancel-btn" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="form-dialog" action="close"/>
            </e:button>
        </e:facet>
    </e:dialog>
    <e:dialog id="goods-dialog" style="width:680px;height:400px;" iconCls="icon-add" modal="true" closable="true"
              closed="true" title="推荐商品">
        <e:datagrid id="goods-grid" fit="true" fitColumns="true" pageSize="10" border="false"
                    url="${root}/admin/api/merchant/${sessionScope.currentMerchantId}/ableRecommendGoodses"
                    pagination="true" singleSelect="true">
            <e:columns>
                <e:column field="goodsId" checkbox="true"/>
                <e:column field="goodsName" title="商品名称" align="center" width="20"/>
                <e:column field="price" title="商品价格" align="center" width="20" sortable="true"/>
                <e:column field="volume" title="销量" sortable="true" align="center" width="10"/>
                <e:column field="goodsDesc" title="商品描述" align="center" width="30"/>
            </e:columns>
            <e:facet name="toolbar">
                <e:button id="check-btn" text="选择" iconCls="icon-ok">
                    <e:eventListener event="click" listener="addRecommendGoods"/>
                </e:button>
                <e:inputSearch id="goods-search" style="width:240px;" prompt="输入关键字搜索">
                    <e:eventListener event="searcher" listener="searchGoods"/>
                    <e:facet name="menu">
                        <div name="goodsName">商品名称</div>
                    </e:facet>
                </e:inputSearch>
            </e:facet>
        </e:datagrid>
    </e:dialog>
    <e:dialog id="edit-dialog" style="width:400px:height:200px" iconCls="icon-edit" modal="true" closable="true"
              closed="true" title="推荐编辑">
        <e:event event="onBeforeClose" target="edit-form" action="clear"/>
        <e:form id="edit-form" method="post">
            <e:eventListener event="success" listener="onEditSuccess"/>
            <input type="hidden" name="recommendId">
            <table class="form-table">
                <tr>
                    <td><label>显示顺序：</label></td>
                    <td>
                        <input type="text" class="easyui-numberbox" name="orderIndex" value="1"
                               data-options="min:0,precision:0,required:true" style="width: 200px"/>
                    </td>
                </tr>
                <tr>
                    <td><label>推荐理由：</label></td>
                    <td>
                        <textarea rows="5" cols="40" name="reason"></textarea>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="submit-edit-btn" iconCls="icon-save" text="保存">
                <e:event event="click" target="edit-form" action="submit"/>
            </e:button>
            <e:button id="cancel-edit-btn" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="edit-dialog" action="close"/>
            </e:button>
        </e:facet>
    </e:dialog>
</e:body>
</html>
