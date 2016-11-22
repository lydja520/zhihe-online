<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>商家参与的活动</title>
    <%@include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        var merchId = '${merchant.merchantId}';
        function getMerchName(merchant) {
            if (merchant) {
                return merchant.merchName;
            }
        }
        function getCategName(category) {
            if (category) {
                return category.categName;
            }
        }
        function getGoodsCategName(values) {
            if (values && $.isArray(values) && values.length > 0) {
                var retVal = "";
                $.each(values, function (index, item) {
                    if (index == 0) {
                        retVal = item.goodsAttSetName;
                    } else {
                        retVal += "," + item.goodsAttSetName;
                    }
                });
                return retVal;
            }

        }
        function getCoverImg(data) {
            if (data) {
                var url = data.coverImg.url;
                return "<div style='margin: 1px auto 1px auto'><img style='width: 70px;height: 35px;' src='" + url + "'></div>";
            }
        }
        function getActivityInfo(value, row, index) {
            return "<button class='easyui-linkbutton' onclick=\"activityInfoPanel('" + row.activitId + "')\">查看详情</button>";
        }
        function activityInfoPanel(activitId) {
            $("#activityInfoPanel").dialog({
                onBeforeOpen: function () {
                    $("#showInfo").attr('src', '${root}/admin/activity/' + activitId + '/info');
                }
            });
            $("#activityInfoPanel").dialog('open');
        }
        /*搜索实现*/
        function activitySearch(value, name) {
            var param = {};
            if (value) {
                param.searchName = name;
                param.searchValue = value;
            }
            $('#activity-grid').datagrid('load', param);
        }

        /*打开红包信息窗口*/
        function loadMyRedEnvelops() {
            var rows = $('#activity-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            var url = '${root}/admin/activity/' + rows[0].activitId + '/redEnvelop';
            var iframe = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
            $('#redenvelop-win').window({
                content: iframe
            });
            $('#redenvelop-win').window('open');
        }
        $(function () {
            $('#redenvelop-win').window({
                onBeforeClose: function () {
                    $('#redenvelop-win').window({
                        content: ''
                    });
                }
            });
        });
        /**
         *打开活动优惠券窗口
         */
        function loadCoupons() {
            var rows = $('#activity-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            var url = '${root}/admin/activity/' + rows[0].activitId + '/coupon';
            var iframe = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
            $('#coupon-win').window({
                content: iframe
            });
            $('#coupon-win').window('open');
        }
        /**
         *打开参加活动的会员窗口
         */
        function loadActivityFans() {
            var rows = $('#activity-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }

            var url = '${root}/admin/merchActivity/' + rows[0].activitId + '/fans';
            var iframe = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
            $('#fans-win').window({
                content: iframe
            });
            $('#fans-win').window('open');
        }

        /**
         * 活动会员提交活动吧状态更新为已提交（已准备就绪）
         */
        function commitActivity() {
            var rows = $('#activity-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            var activity = rows[0];
            $('#paySubmitDialog').dialog({
                width: 500,
                height: 300,
                title: '提交支付',
                modal: true,
                onBeforeOpen: function () {
                    $('#paySubmitDialog').attr('src', '/admin/api/activity/' + activity.activitId + '/payOrder')
                }
            });

            /*            $.messager.confirm('警告', '确认要提交吗？', function (r) {
             if (r) {
             $.ajax({
             url: '/admin/api/activity/' + rows[0].activitId + '/alliance/commit',
             type: 'post',
             dataType: 'text',
             data: {},
             success: function (data) {
             data = $.parseJSON(data);
             if (data.success) {
             $.messager.show({
             title: '提示',
             msg: data.msg
             });
             $('#activity-grid').datagrid('load');
             } else {
             $.messager.alert('提示', data.msg, 'error');
             }
             },
             error: function () {
             $.messager.alert('提示', '操作失败，请重试！', 'error');
             }
             });
             }
             });*/
        }

        /*========打开活动商品页面========*/
        function loadActivityGoods() {
            var rows = $('#activity-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要操作的数据', 'info');
                return;
            }
            var url = '${root}/admin/merchActivity/' + rows[0].activitId + '/activityGoods';
            var iframe = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
            $('#activityGoods-win').window({
                content: iframe
            });
            $('#activityGoods-win').window('open');
        }
    </script>
</head>
<e:body>

    <e:datagrid id="activity-grid" url="${root}/admin/api/merchActivity/list" singleSelect="true" fit="true"
                pagination="true" fitColumns="true" rownumbers="true" border="true" striped="true" title="我参加的活动">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="activitId" checkbox="true"/>
            <e:column field="receptionRoom" title="活动封面图" align="center" width="20" formatter="getCoverImg"/>
            <e:column field="activitName" title="活动名称" align="center" width="20"/>
            <e:column field="activityPromoter" title="发起商" align="center" width="20" formatter="getMerchName"/>
            <e:column field="category" title="活动类别" align="center" width="20" formatter="getCategName"/>
            <e:column field="attributeSets" title="商品类别" align="center" width="20" formatter="getGoodsCategName"/>
            <e:column field="displayState" title="当前状态" align="center" width="10"/>
            <e:column field="contacterName" title="活动联系人" align="center" width="15"/>
            <e:column field="contactTell" title="联系电话" align="center" width="20"/>
            <e:column field="createDate" title="创建时间" align="center" width="20" sortable="true"/>
            <e:column field="beginDate" title="开始时间" align="center" width="20" sortable="true"/>
            <e:column field="endDate" title="结束时间" align="center" width="20" sortable="true"/>
            <e:column field="activitDetail" title="活动介绍" align="center" width="25"/>
            <e:column field="af" title="活动详情" align="center" width="25" formatter="getActivityInfo"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="redenvelop-btn" iconCls="icon-redpack" text="活动红包" plain="true">
                <e:eventListener event="click" listener="loadMyRedEnvelops"/>
            </e:button>
            <e:button id="add-coupon" iconCls="icon-discount" text="活动优惠券" plain="true">
                <e:eventListener event="click" listener="loadCoupons"/>
            </e:button>
            <e:button id="add-goods" iconCls="icon-goods" text="活动商品" plain="true">
                <e:eventListener event="click" listener="loadActivityGoods"/>
            </e:button>
            <e:button id="add-member" iconCls="icon-person" text="我的会员" plain="true">
                <e:eventListener event="click" listener="loadActivityFans"/>
            </e:button>
            <e:button id="commit_activity" iconCls="icon-ok" text="提交" plain="true">
                <e:eventListener event="click" listener="commitActivity"/>
            </e:button>

            <e:inputSearch id="activit-grid-searchbox" prompt="输入关键字搜索" style="width:260px">
                <e:eventListener event="searcher" listener="activitySearch"/>
                <e:facet name="menu">
                    <div name="activitName">活动名称</div>
                </e:facet>
            </e:inputSearch>
        </e:facet>
    </e:datagrid>
    <!--红包窗口-->
    <div id="redenvelop-win" class="easyui-window"
         data-options="closed:true,collapsible:false,minimizable:false,modal:true,maximized:true,maximizable:false,
         title:'活动红包',draggable:false,fit:true"></div>
    <!--优惠券窗口-->
    <div id="coupon-win" class="easyui-window"
         data-options="closed:true,collapsible:false,minimizable:false,modal:true,maximized:true,maximizable:false,
         title:'活动优惠券',draggable:false,fit:true"></div>
    <!--优惠券窗口-->
    <div id="fans-win" class="easyui-window"
         data-options="closed:true,collapsible:false,minimizable:false,modal:true,maximized:true,maximizable:false,
         title:'我的会员',draggable:false,fit:true"></div>
    <!--优惠券窗口-->
    <div id="activityGoods-win" class="easyui-window"
         data-options="closed:true,collapsible:false,minimizable:false,modal:true,maximized:true,maximizable:false,
         title:'活动商品',draggable:false,fit:true"></div>
    <e:dialog id="activityInfoPanel" closed="true" style="width:800px;height:500px;overflow-y:hidden;" title="活动详情"
              modal="true" iconCls="icon-tip">
        <iframe id="showInfo" frameborder="0" width="100%" height="100%"></iframe>
    </e:dialog>

    <iframe id="paySubmitDialog">

    </iframe>
</e:body>
</html>
