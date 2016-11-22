<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>商家优惠券</title>
    <%@include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript">
        $(function () {
            $('#discount-value').numberspinner('enable');
            $('#discount-total').numberbox('enable');
            $('#discount-templat').show();
            $('#coupon-value').numberbox('disable');
            $('#coupon-total').numberbox('disable');
            $('#coupon-templat').hide();
        });
        var currentState =${requestScope.activity.currentState};
        var merchId = '${requestScope.merchant.merchantId}';
        function getFaceValue(value, row) {
            if (row.couponType == 1) {
                return value + '折';
            }
            return value + '元';
        }
        function getCouponType(value) {
            if (value == 1) {
                return '打折券';
            }
            return '代金券';
        }
        function onSubmitSeccess(data) {
            onAfterEdit(data, 'coupon-grid', function () {
                $('#add-dialog').dialog('close');
            })
        }
        function onEditSeccess(data) {
            onAfterEdit(data, 'coupon-grid', function () {
                $('#edit-dialog').dialog('close');
            })
        }

        /*=========添加活动优惠券========*/
        function addCoupon() {
            if (currentState == 1 || currentState == 4) {
                $('#add-dialog').window('open');
            } else {
                $.messager.alert('提示', '当前活动状态不支持添加活动优惠券操作！', 'info');
            }
        }

        /*========修改选中的活动优惠券========*/
        function loadCouponInfo() {
            if (currentState == 1 || currentState == 4) {
                var rows = $('#coupon-grid').datagrid('getChecked');
                if (rows.length != 1) {
                    $.messager.alert('提示', '请选择需要操作的数据', 'info');
                    return;
                }
                var coupon = rows[0];
                $('#edit-form').form('load', coupon);
                if (coupon.couponType == '1') {
                    $('#select-coupon-type1').removeAttr('disabled');
                    $('#select-coupon-type1').attr('checked', 'true');
                    $('#select-coupon-type2').attr('disabled', 'true');
                    $('#select-coupon-type2').removeAttr('checked');
                    $('#discount-value2').numberspinner('enable');
                    $('#discount-total2').numberbox('enable');
                    $('#discount-templat2').show();
                    $('#coupon-value2').numberbox('disable');
                    $('#coupon-total2').numberbox('disable');
                    $('#coupon-templat2').hide();
                } else {
                    $('#select-coupon-type2').removeAttr('disabled');
                    $('#select-coupon-type2').attr('checked', 'true');
                    $('#select-coupon-type1').attr('disabled', 'true');
                    $('#select-coupon-type1').removeAttr('checked');
                    $('#discount-value2').numberspinner('disable');
                    $('#discount-total2').numberbox('disable');
                    $('#discount-templat2').hide();
                    $('#coupon-value2').numberbox('enable');
                    $('#coupon-total2').numberbox('enable');
                    $('#coupon-templat2').show();
                }
                $('#edit-dialog').dialog('open');
                $('#activity_activitId').val(coupon.activity.activitId);
                $('#edit_start_validity').datetimebox('setValue', coupon.startValidity);
                $('#edit_end_validity').datetimebox('setValue', coupon.endValidity);
            } else {
                $.messager.alert('提示', '当前活动状态不支持修改活动优惠券操作！', 'info');
            }

        }

        /*========删除选中的活动优惠券========*/
        function removeCoupon() {
            if (currentState == 1 || currentState == 4) {
                var rows = $('#coupon-grid').datagrid('getChecked');
                if (rows.length != 1) {
                    $.messager.alert('提示', '请选择需要操作的数据', 'info');
                    return;
                }
                $.messager.confirm('提示', '确定要删除所选则优惠券吗，删除后不可恢复？', function (r) {
                    if (r) {
                        request({
                            url: '${root}/admin/api/coupon/delete',
                            data: {
                                couponId: rows[0].couponId,
                                'activity.activitId': rows[0].activity.activitId
                            },
                            success: function (data) {
                                if (data.success) {
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    })
                                    $('#coupon-grid').datagrid('load');
                                }
                                else {
                                    $.messager.alert('提示', data.msg, 'info');
                                }
                            },
                            error: function () {
                                $.messager.alert('提示', '系统错误，刷新后重试或联系管理员', 'error');
                            }
                        })
                    }
                });
            } else {
                $.messager.alert('提示', '当前活动状态不支持删除活动优惠券操作操作！', 'info');
            }
        }

        function getValidity(value, row) {
            if (row.startValidity && row.endValidity) {
                return row.startValidity + '至' + row.endValidity;
            }
        }

        /**
         *判断是哪一种优惠券
         * @param value
         */
        function whatCoupon(value) {
            if (value == '1') {
                $('#discount-value').numberspinner('enable');
                $('#discount-total').numberbox('enable');
                $('#discount-templat').show();
                $('#coupon-value').numberbox('disable');
                $('#coupon-total').numberbox('disable');
                $('#coupon-templat').hide();
            } else if (value == '2') {
                $('#discount-value').numberspinner('disable');
                $('#discount-total').numberbox('disable');
                $('#discount-templat').hide();
                $('#coupon-value').numberbox('enable');
                $('#coupon-total').numberbox('enable');
                $('#coupon-templat').show();
            } else {
                alert("请选中一种优惠券类型")
            }
        }

    </script>
</head>
<e:body>
    <e:datagrid id="coupon-grid" url="${root}/admin/api/activity/${activity.activitId}/coupon/list"
                singleSelect="true" fit="true" pagination="true" fitColumns="true" rownumbers="true" border="false"
                striped="true">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="couponId" checkbox="true"/>
            <e:column field="faceValue" title="面值" align="center" width="10" sortable="true" formatter="getFaceValue"/>
            <e:column field="total" title="优惠券数量" align="center" width="10" sortable="true"/>
            <e:column field="totalReceived" title="已领取" align="center" width="10" sortable="true"/>
            <e:column field="couponType" title="卷类型" align="center" width="10" formatter="getCouponType"
                      sortable="true"/>
            <e:column field="validity" title="有效期" align="center" width="30" formatter="getValidity"/>
            <e:column field="couponMsg" title="使用规则" align="center" width="40"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-coupon" iconCls="icon-add" text="添加优惠券" plain="true">
                <e:eventListener event="click" listener="addCoupon"/>
            </e:button>
            <e:button id="edit-coupon" iconCls="icon-edit" text="修改优惠券" plain="true">
                <e:eventListener event="click" listener="loadCouponInfo"/>
            </e:button>
            <e:button id="remove-coupon" iconCls="icon-remove" text="删除优惠券" plain="true">
                <e:eventListener event="click" listener="removeCoupon"/>
            </e:button>
        </e:facet>
    </e:datagrid>

    <!--添加活动优惠券开始-->
    <e:dialog id="add-dialog" style="width:530px;height:300px;" iconCls="icon-tip" title="优惠券信息" closed="true"
              closable="true" modal="true">
        <e:event event="onBeforeClose" target="add-form" action="reset"/>
        <e:form id="add-form" url="${root}/admin/api/coupon/add" method="post">
            <e:eventListener event="success" listener="onSubmitSeccess"/>
            <input type="hidden" name="merchant.merchantId" value="${merchant.merchantId}">
            <input type="hidden" name="activity.activitId" value="${activity.activitId}">
            <table class="form-table">
                <tr>
                    <td><label>券类型：</label></td>
                    <td colspan="3">
                        <input type="radio" name="couponType" value="1" onclick="whatCoupon(this.value)" checked/>打折券&nbsp;
                        <input type="radio" name="couponType" value="2" onclick="whatCoupon(this.value)"/>代金券
                    </td>
                </tr>
                <tr id="discount-templat">
                    <td><label>折扣：</label></td>
                    <td>
                        <input id="discount-value" class="easyui-numberspinner" name="faceValue"
                               required data-options="min:0,max:9.9,precision:1,increment:0.1" value="9.9"/>折
                    </td>
                    <td><label>份数：</label></td>
                    <td>
                        <input id="discount-total" type="text" class="easyui-numberbox" name="total"
                               required data-options="min:1,precision:0,required:true" value="1"/>份
                    </td>
                </tr>
                <tr id="coupon-templat">
                    <td><label>抵现：</label></td>
                    <td>
                        <input id="coupon-value" type="text" class="easyui-numberbox" name="faceValue"
                               data-options="min:0.01,precision:2,required:true" value="0.01"/>元
                    </td>
                    <td><label>份数：</label></td>
                    <td>
                        <input id="coupon-total" type="text" class="easyui-numberbox" name="total"
                               data-options="min:1,precision:0,required:true" value="1"/>份
                    </td>
                </tr>
                <tr>
                    <td><label>有效期：</label></td>
                    <td colspan="3">
                        <input class="easyui-datetimebox" name="beginValidity" style="width:140px"
                               data-options="required:true,editable:false">
                        至
                        <input class="easyui-datetimebox" name="validity" style="width:140px"
                               data-options="required:true,editable:false">
                    </td>
                </tr>
                <tr>
                    <td><label>使用规则：</label></td>
                    <td colspan="3">
                        <textarea cols="60" rows="4" name="couponMsg"></textarea>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="cancel-btn" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="add-dialog" action="close"/>
            </e:button>
            <e:button id="add-btn" iconCls="icon-ok" text="添加">
                <e:event event="click" target="add-form" action="submit"/>
            </e:button>
        </e:facet>
    </e:dialog>
    <!--添加活动优惠券结束-->

    <!--修改活动优惠券开始-->
    <e:dialog id="edit-dialog" style="width:530px;height:300px;" iconCls="icon-tip" title="优惠券信息" closed="true"
              closable="true" modal="true">
        <e:event event="onBeforeClose" target="edit-form" action="clear"/>
        <e:form id="edit-form" url="${root}/admin/api/coupon/editInfo" method="post">
            <e:eventListener event="success" listener="onEditSeccess"/>
            <input type="hidden" name="couponId">
            <input type="hidden" name="activity.activitId" id="activity_activitId">
            <table class="form-table">

                <tr>
                    <td><label>券类型：</label></td>
                    <td colspan="3">
                        <input id="select-coupon-type1" type="radio" name="couponType" value="1">打折券&nbsp;
                        <input id="select-coupon-type2" type="radio" name="couponType" value="2">代金券
                    </td>
                </tr>

                <tr id="discount-templat2">
                    <td><label>折扣：</label></td>
                    <td>
                        <input id="discount-value2" class="easyui-numberspinner" name="faceValue"
                               required="required" data-options="min:0,max:9.9,precision:1,increment:0.1" value="9.9">折
                    </td>
                    <td><label>份数：</label></td>
                    <td>
                        <input id="discount-total2" type="text" class="easyui-numberbox" name="total"
                               data-options="min:1,precision:0,required:true" value="1">份
                    </td>
                </tr>

                <tr id="coupon-templat2">
                    <td><label>抵现：</label></td>
                    <td>
                        <input id="coupon-value2" type="text" class="easyui-numberbox" name="faceValue"
                               data-options="min:0.01,precision:2,required:true" value="0.01">元
                    </td>
                    <td><label>份数：</label></td>
                    <td>
                        <input id="coupon-total2" type="text" class="easyui-numberbox" name="total"
                               data-options="min:1,precision:0,required:true" value="1">份
                    </td>
                </tr>

                <tr>
                    <td><label>有效期：</label></td>
                    <td colspan="3">
                        <input class="easyui-datetimebox" name="beginValidity" style="width:140px"
                               id="edit_start_validity"
                               data-options="required:true,editable:false">
                        至
                        <input class="easyui-datetimebox" name="validity" style="width:140px" id="edit_end_validity"
                               data-options="required:true,editable:false">
                    </td>
                </tr>

                <tr>
                    <td><label>使用规则：</label></td>
                    <td colspan="3">
                        <textarea cols="60" rows="4" name="couponMsg"></textarea>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="cancel-edit" iconCls="icon-cancel" text="取消">
                <e:event event="click" target="edit-dialog" action="close"/>
            </e:button>
            <e:button id="submit-edit" iconCls="icon-ok" text="修改">
                <e:event event="click" target="edit-form" action="submit"/>
            </e:button>
        </e:facet>
    </e:dialog>
    <!--修改活动优惠券开始-->

</e:body>
</html>
