<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>活动红包</title>
    <%@include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        var currentState =${requestScope.activity.currentState};
        var merchId = '${requestScope.merchant.merchantId}';
        /**
         *添加红包的表单提交成功之后毁掉方法
         * @param data
         */
        function onFormSubmitSuccess(data) {
            onAfterEdit(data, 'redenvelop-grid', function () {
                $('#redenvelop-dialog').dialog('close');
            })
        }

        /**
         *修改红包信息成功之后回调
         *@param data
         */
        function onUpdateSuccess(data) {
            onAfterEdit(data, 'redenvelop-grid', function () {
                $('#update-dialog').dialog('close');
            })
        }

        /*添加活动红包*/
        function addRedEnvelop() {
            if (currentState == 1 || currentState == 4) {
                $('#redenvelop-dialog').dialog('open');
            } else {
                $.messager.alert('提示', '当前活动状态不支持添加活动红包操作！', 'info');
            }
        }

        /**
         * 点击修改按钮，弹出修改对话框
         */
        function loadInfo() {
            if (currentState == 1 || currentState == 4) {
                var rows = $('#redenvelop-grid').datagrid('getChecked');
                if (!rows || rows.length != 1) {
                    $.messager.alert('提示', '请选择需要操作的数据', 'info');
                    return;
                }
                $('#update-dialog').dialog('open');
                $('#update-form').form('load', rows[0]);
            } else {
                $.messager.alert('提示', '当前活动状态不支持修改活动红包操作！', 'info');
            }
        }

        /**
         *   删除红包
         */
        function removeRedEnvelop() {
            if (currentState == 1 || currentState == 4) {
                var rows = $('#redenvelop-grid').datagrid('getChecked');
                if (!rows || rows.length != 1) {
                    $.messager.alert('提示', '请选择需要操作的数据', 'info');
                    return;
                }
                $.messager.confirm('提示', '确定要删除此红包吗，删除后不可恢复？', function (r) {
                    if (r) {
                        request({
                            url: '${root}/admin/api/redEnvelop/delete',
                            data: {
                                envelopId: rows[0].envelopId
                            },
                            success: function (data) {
                                if (data.success) {
                                    $('#redenvelop-grid').datagrid('load');
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    })
                                }
                                else {
                                    $.messager.alert('提示', data.msg, 'info');
                                }
                            },
                            error: function () {
                                $.messager.alert('提示', '系统出错，请联系管理员！', 'error');
                            }
                        })
                    }
                })
            } else {
                $.messager.alert('提示', '当前活动状态不支持删除活动红包操作！', 'info');
            }
        }

        function payStateFormatter(data) {
            if (data) {
                return "<span style='color: green'>已支付</span>";
            }
            return "<span style='color: red'>未支付</span>";
        }

        function userRedEnvelopPage(envelopId) {
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

        function userRedEnvelop(value, rows, index) {
            return "<button value=" + rows.envelopId + " onclick='userRedEnvelopPage(this.value)'>查看详情</button>";
        }
    </script>
    <style>
        body {
            margin: 0px;
            padding: 0px;
            font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
            font-size: 12px;
        }

        ul {
            width: 80%;
            margin: 10px auto 10px auto;
            padding: 0px;
            list-style-type: none;
        }

        ul li {
            float: left;
            width: 49%;
            margin-top: 10px;
        }

        .clear {
            clear: both;
        }

    </style>
</head>
<e:body>
    <e:datagrid id="redenvelop-grid" url="${root}/admin/api/activity/${requestScope.activity.activitId}/redEnvelop/list"
                singleSelect="true"
                fit="true" pagination="true" fitColumns="true" rownumbers="true" border="false" striped="true">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="envelopId" checkbox="true"/>
            <e:column field="totalMoney" title="红包金额" align="center" width="20" sortable="true"/>
            <e:column field="numbers" title="红包个数" align="center" width="20" sortable="true"/>
            <e:column field="createDate" title="添加时间" align="center" width="20" sortable="true"/>
            <e:column field="payState" title="是否支付" align="center" width="20" sortable="true"
                      formatter="payStateFormatter"/>
            <e:column field="envelopMsg" title="留言" align="center" width="40"/>
            <e:column field="userRedEnvelop" title="抢到红包用户" align="center" width="20" formatter="userRedEnvelop"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-redmoney" iconCls="icon-add" text="添加红包" plain="true">
                <e:eventListener event="click" listener="addRedEnvelop"/>
            </e:button>
            <e:button id="add-coupon" iconCls="icon-edit" text="编辑红包" plain="true">
                <e:eventListener event="click" listener="loadInfo"/>
            </e:button>
            <e:button id="remove-btn" iconCls="icon-cancel" text="删除红包" plain="true">
                <e:eventListener event="click" listener="removeRedEnvelop"/>
            </e:button>
        </e:facet>
    </e:datagrid>

    <!--添加活动红包-->
    <e:dialog id="redenvelop-dialog" title="活动红包" iconCls="icon-tip" style="width:500px;" closed="true"
              modal="true">
        <e:event event="onBeforeClose" target="redenvelop-form" action="reset"/>
        <e:form id="redenvelop-form" url="${root}/admin/api/redEnvelop/add" method="post">
            <e:eventListener event="success" listener="onFormSubmitSuccess"/>
            <input type="hidden" name="merchant.merchantId" value="${merchant.merchantId}">
            <input type="hidden" name="activity.activitId" value="${activity.activitId}">
            <ul>
                <li>
                    金额： <input type="text" class="easyui-numberbox" name="totalMoney"
                               data-options="min:1,precision:2,required:true">
                </li>
                <li>
                    数量：<input type="text" class="easyui-numberbox" name="numbers"
                              data-options="min:1,precision:0,required:true">
                </li>
                <li class="clear" style="width: 100%;margin-bottom: 10px">
                    留言：<input class="easyui-textbox" type="text" data-options="multiline:true,height:50"
                              name="envelopMsg" style="width: 93%"/>
                </li>
            </ul>
        </e:form>
        <e:facet name="buttons">
            <e:button id="redenvelop-cancel-btn" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="redenvelop-dialog" action="close"/>
            </e:button>
            <e:button id="redenvelop-submit-btn" iconCls="icon-ok" text="保存">
                <e:event event="click" target="redenvelop-form" action="submit"/>
            </e:button>
        </e:facet>
    </e:dialog>

    <!--修改活动红包-->
    <e:dialog id="update-dialog" title="活动红包" iconCls="icon-edit" style="width:500px" closed="true"
              modal="true">
        <e:event event="onBeforeClose" target="update-form" action="clear"/>
        <e:form id="update-form" url="${root}/admin/api/redEnvelop/updateInfo" method="post">
            <e:eventListener event="success" listener="onUpdateSuccess"/>
            <input type="hidden" name="envelopId">
            <ul>
                <li>
                    金额： <input type="text" class="easyui-numberbox" name="totalMoney"
                               data-options="min:1,precision:2,required:true">
                </li>
                <li>
                    数量：<input type="text" class="easyui-numberbox" name="numbers"
                              data-options="min:1,precision:0,required:true">
                </li>
                <li class="clear" style="width: 100%;margin-bottom: 10px">
                    留言：<input class="easyui-textbox" type="text" data-options="multiline:true,height:50"
                              name="envelopMsg" style="width: 93%"/>
                </li>
            </ul>
        </e:form>
        <e:facet name="buttons">
            <e:button id="update-cancel-btn" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="update-dialog" action="close"/>
            </e:button>
            <e:button id="update-submit-btn" iconCls="icon-ok" text="保存">
                <e:event event="click" target="update-form" action="submit"/>
            </e:button>
        </e:facet>
    </e:dialog>
    <%--user红包--%>
    <iframe id="preview"></iframe>
</e:body>
</html>
