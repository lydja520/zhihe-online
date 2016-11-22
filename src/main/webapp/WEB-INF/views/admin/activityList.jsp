<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>活动列表</title>
    <%@include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        function onSubmitSuccess(data) {
            onAfterEdit(data, 'activity-grid', function () {
                $('#data-dialog').dialog('close');
            })
        }
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
        function getState(state) {
            switch (state) {
                case 1:
                    return '未提交';
                    break;
                case 2:
                    return '审核中';
                    break;
                case 3:
                    return '已审核通过';
                    break;
                case 4:
                    return '<span style="color:red">未审核通过</span>';
                    break;
                case 5:
                    return '活动已开始';
                    break;
                case 6:
                    return '活动已结束';
                    break
            }
        }
        function getButtons() {
            return '<a class="easyui-linkbutton">预览</a>';
        }
        /**
         * 搜索活动
         * @param value
         * @param name
         */
        function searchActivity(value, name) {
            var param = {};
            if (name && $.trim(value)) {
                param.searchName = name;
                param.searchValue = value;
            }
            $('#activity-grid').datagrid('load', param);
        }

        function activityPreview(value, row, index) {
            return "<button value='" + row.activitId + "' onclick='preview(this.value)'>查看详情</button>";
        }

        function preview(activitId) {
            $("#activityInfoPanel").dialog({
                onBeforeOpen: function () {
                    $("#showInfo").attr('src', '${root}/admin/activity/' + activitId + '/info');
                }
            });
            $("#activityInfoPanel").dialog('open');
        }

        function getCoverImg(data) {
            if (data) {
                var url = data.coverImg.url;
                return "<div style='margin: 1px auto 1px auto'><img style='width: 70px;height: 35px;' src='" + url + "'></div>";
            }
        }
    </script>
</head>
<e:body>
    <e:datagrid id="activity-grid" url="${root}/admin/api/activity/list" singleSelect="true" fit="true"
                pagination="true" fitColumns="true" rownumbers="true" border="true">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="activitId" checkbox="true"/>
            <e:column field="receptionRoom" title="活动封面图" align="center" width="20" formatter="getCoverImg"/>
            <e:column field="activitName" title="活动名称" align="center" width="20"/>
            <e:column field="activityPromoter" title="发起商" align="center" width="20" formatter="getMerchName"/>
            <e:column field="category" title="活动类别" align="center" width="20" formatter="getCategName"/>
            <e:column field="attributeSets" title="商品类别" align="center" width="20" formatter="getGoodsCategName"/>
            <e:column field="currentState" title="当前状态" align="center" width="10" formatter="getState"/>
            <e:column field="createDate" title="创建时间" align="center" width="20" sortable="true"/>
            <e:column field="beginDate" title="开始时间" align="center" width="20" sortable="true"/>
            <e:column field="endDate" title="结束时间" align="center" width="20" sortable="true"/>
            <e:column field="contacterName" title="活动联系人" align="center" width="20"/>
            <e:column field="contactTell" title="联系电话" align="center" width="20"/>
            <e:column field="aaa" title="活动详情" align="center" width="20" formatter="activityPreview"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:inputSearch id="gird-searchbox" prompt="输入关键字搜索" style="width:260px;">
                <e:eventListener event="searcher" listener="searchActivity"></e:eventListener>
                <e:facet name="menu">
                    <div name="activitName">活动名称</div>
                </e:facet>
            </e:inputSearch>
        </e:facet>
    </e:datagrid>

    <e:dialog id="activityInfoPanel" closed="true" style="width:800px;height:600px;overflow-y:hidden;" title="活动详情"
              modal="true" maximizable="true" iconCls="icon-tip">
        <iframe id="showInfo" frameborder="0" width="100%" height="100%"></iframe>
    </e:dialog>
</e:body>
</html>
