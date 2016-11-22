<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2016/1/18
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <title>待审核的活动</title>
    <script type="text/javascript">
        $(function () {
            $('#dg').datagrid({
                url: '${basePath}/admin/api/waitExaminActivity/list',
                title: '待审核的活动',
                fit: true,
                fitColumns: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                pageSize: 20,
                columns: [[
                    {field: '', checkbox: true, width: 100, align: 'center'},
                    {
                        field: 'receptionRoom', title: '活动封面图', width: 100, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                var url = data.coverImg.url;
                                return "<div style='margin: 1px auto 1px auto'><img style='width: 70px;height: 35px;' src='" + url + "'></div>";
                            }
                        }
                    },
                    {field: 'activitName', title: '活动名称', width: 100, align: 'center'},
                    {
                        field: 'activityPromoter', title: '发起商', width: 100, align: 'center',
                        formatter: function (value) {
                            return value.merchName;
                        }
                    },
                    {
                        field: 'category', title: '活动类别', width: 100, align: 'center',
                        formatter: function (value) {
                            return value.categName;
                        }
                    },
                    {
                        field: 'attributeSets', title: '商品类别', width: 100, align: 'center',
                        formatter: function (values) {
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
                    },
                    {field: 'contacterName', title: '活动联系人', width: 100, align: 'center'},
                    {field: 'contactTell', title: '联系电话', width: 100, align: 'center'},
                    {field: 'activitDetail', title: '活动介绍', width: 100, align: 'center'},
                    {
                        field: 'activitId', title: '查看详细信息', width: 100, align: 'center',
                        formatter: function (value) {
                            return "<button value=" + value + " onclick='previewInfo(this.value)'>点击查看</button>";
                        }
                    },
                ]]
            });
        });

        function previewInfo(activityId) {
            $('#preview-info-win').dialog({
                title: '查看详细信息',
                iconCls: 'preview',
                maximizable: true,
                width: 700,
                height: 500,
                modal: true,
                buttons: [{
                    text: '通过审核',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $.messager.confirm('确认', '您确认让该活动通过审核吗？', function (r) {
                            if (r) {
                                $.ajax({
                                    url: '${basePath}/admin/activity/updateActivityExaminState',
                                    dataType: 'json',
                                    type: 'post',
                                    data: {
                                        activityId: activityId,
                                        currentState:<%=Constant.ACTIVITY_STATE_EXAMINED_OK%>,
                                        examinMsg: '该活动已经通过官方审核'
                                    },
                                    beforeSend: function () {
                                        $.messager.progress();
                                    },
                                    success: function (info) {
                                        $.messager.progress('close');
                                        $.messager.show({
                                            title: '提示',
                                            iconCls: 'icon-tip',
                                            msg: info.msg,
                                        });
                                        if (info.success) {
                                            $('#dg').datagrid('load');
                                            $('#preview-info-win').dialog('close');
                                        }
                                    },
                                    error: function () {
                                        $.messager.progress('close');
                                        $.messager.alert("错误", "系统出错，请与管理员联系！", 'error');
                                    }
                                });
                            }
                        });
                    }
                }, {
                    text: '审核不通过',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#examinNotOk-win').dialog({
                            title: '审核未通过原因',
                            width: 300,
                            height: 210,
                            modal: true,
                            buttons: [{
                                text: '确定',
                                iconCls: 'icon-ok',
                                handler: function () {
                                    $.ajax({
                                        url: '${basePath}/admin/activity/updateActivityExaminState',
                                        dataType: 'json',
                                        type: 'post',
                                        data: {
                                            activityId: activityId,
                                            currentState:<%=Constant.ACTIVITY_STATE_EXAMINED_NOT%>,
                                            examinMsg: $('#examinNotOk-desc').textbox('getText')
                                        },
                                        beforeSend: function () {
                                            $.messager.progress();
                                        },
                                        success: function (info) {
                                            $.messager.show({
                                                title: '提示',
                                                iconCls: 'icon-tip',
                                                msg: info.msg,
                                            });
                                            if (info.success) {
                                                $.messager.progress('close');
                                                $('#dg').datagrid('load');
                                                $('#examinNotOk-win').dialog('close');
                                                $('#preview-info-win').dialog('close');
                                            }
                                        },
                                        error: function () {
                                            $.messager.progress('close');
                                            $.messager.alert("错误", "系统出错，请与管理员联系！", 'error');
                                        }
                                    });
                                }
                            }, {
                                text: '取消',
                                iconCls: 'icon-cancel',
                                handler: function () {
                                    $('#examinNotOk-win').dialog('close');
                                }
                            }],
                            onBeforeOpen: function () {
                                $('#examinNotOk-desc').textbox({
                                    multiline: true,
                                    width: 250,
                                    height: 80,
                                    prompt: '请输入为什么不能通过审核的原因，以便把不能通过的原因返回给该商家。',
                                    required: true
                                });
                            }
                        });
                    }
                }],
                onBeforeOpen: function () {
                    $('#preview-info-win').attr('src', "/admin/activity/" + activityId + "/info")
                }
            });
        }
    </script>
</head>
<body>
<!--数据网格开始-->
<div id="dg"></div>
<!--数据网格结束-->

<!--打开审核窗口开始-->
<iframe id="preview-info-win"></iframe>
<!--打开审核窗口结束-->

<!--审核未通过的原因对话框开始-->
<div id="examinNotOk-win" style="padding-left: 20px">
    <p><span style="color:red">*</span>审核未通过原因：</p>
    <input id="examinNotOk-desc">
</div>
<!--审核未通过的原意对话框结束-->

<div id="p"></div>
</body>
</html>
