<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>通知</title>
</head>
<body>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
    $(function () {
        var shopShow = '${requestScope.totalShopShow}';
        $('#invitation-grid').datagrid({
            url: '${basePath}/admin/api/merchant/invitations',
            columns: [[
                {field: 'invitationId', title: 'ID', checkbox: true},
                {
                    field: 'activitName',
                    title: '活动名称',
                    width: 40,
                    align: 'center',
                    formatter: function (value, row) {
                        return row.activity.activitName;
                    }
                }, {
                    field: 'invitationMerchant',
                    title: '邀请商家',
                    width: 40,
                    align: 'center',
                    formatter: function (value, row) {
                        return row.activity.activityPromoter.merchName;
                    }
                },
                {field: 'createDate', title: '邀请时间', width: 50, align: 'center'},
                {field: 'invitedMsg', title: '活动简介', width: 100, align: 'center'}
            ]],
            singleSelect: true,
            method: 'get',
            fit: true,
            fitColumns: true,
            pagination: true,
            border: false,
            toolbar: '#invitation-grid-toolbar'
        });

        /**
         * 活动邀请同意按钮触发
         */
        $('#agree_invitation').click(function () {
            var rows = $('#invitation-grid').datagrid('getChecked');
            if (rows.lenght < 1) {
                $.messager.alert('提示', '请选择需要操作的数据！', 'info');
                return;
            }
            agreeInvitation(rows[0]);
        });

        /**
         * 活动邀请拒绝按钮触发
         */
        $('#un_agree_invitation').click(function () {
            var rows = $('#invitation-grid').datagrid('getChecked');
            if (rows.lenght < 1) {
                $.messager.alert('提示', '请选择需要操作的数据！', 'info');
                return;
            }
            refuseInvitation(rows[0]);
        });

        /**
         * 活动邀请同意按钮触发
         */
        $('#preview_invitation').click(function () {
            var rows = $('#invitation-grid').datagrid('getChecked');
            if (rows.lenght < 1) {
                $.messager.alert('提示', '请选择需要操作的数据！', 'info');
                return;
            }
            previewInvitation(rows[0]);
        });

        var focusMerchant = null;
        $('#push-grid').datagrid({
            url: '${basePath}/admin/pushMessage/brithdateList',
            columns: [[
                {
                    field: 'focusMerchant', title: '用户头像', width: 100, align: 'center',
                    formatter: function (value) {
                        focusMerchant = value;
                        if (value.user.portrait != null) {
                            return "<img style='width:45px;height:45px;margin:1px auto 1px auto;border-radius:22.5px' src='" + value.user.portrait.url + "'/>";
                        } else {
                            return "<img style='width:45px;height:45px;margin:1px auto 1px auto' src='${basePath}/static/images/unknow.jpg'/>";
                        }
                    }
                },
                {
                    field: 'name', title: '用户名', width: 100, align: 'center',
                    formatter: function (value) {
                        return focusMerchant.user.userName;
                    }
                },
                {field: 'nowBrithDay', title: '用户生日', width: 100, align: 'center'},
                {
                    field: 'op', title: '操作', width: 100, align: 'center',
                    formatter: function (value, row) {
                        var pushBrithdateId = row.pushBrithdateId;
                        var userId = row.focusMerchant.user.userId;
                        var userName = row.focusMerchant.user.userName;
                        var userCoverImg = row.focusMerchant.user.portrait.url;
                        return '<a class="grid-options-push" data-id="' + pushBrithdateId + '" data-userid="' + userId + '" data-username="' + userName + '" data-userimg="' + userCoverImg + '">推送信息</a> ';
                    }
                }
            ]],
            onLoadSuccess: function (data) {
                $('.grid-options-push').linkbutton({
                    iconCls: 'icon-ok'
                }).click(function () {
                    pushuBirthdateInfo(this);
                });
            },
            method: 'get',
            fit: true,
            fitColumns: true,
            pagination: true,
            border: false
        });

        $('#push-dialog').dialog({
            title: '生日祝福推送',
            icon: 'icon-priview2',
            width: 400,
            modal: true,
            buttons: [{
                text: '推送',
                iconCls: 'icon-ok',
                handler: function () {
                    $('#push-brithday-form').form({
                        url: '${basePath}/admin/pushMessage/brithday',
                        onSubmit: function () {

                        },
                        success: function (data) {
                            alert(423423)
                        }
                    });
                    $('#push-brithday-form').submit();
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#push-dialog').dialog('close');
                }
            }]
        });
        $('#push-dialog').dialog('close');

        if (shopShow == '0') {
            $.messager.show({
                title: '我的消息',
                width:300,
                height:100,
                msg: '您的还没有上传门店照，你可以到“信息维护>商家图片修改”中进行门店照上传！',
                timeout: 4000,
                showType:'slide',
                style:{
                    right:'',
                    top:document.body.clientHeight/2,
                    bottom:''
                }
            });
        }
    });

    /**
     *同意邀请
     * @param obj
     */
    function agreeInvitation(data) {
        var invitationId = data.invitationId;
        $.messager.progress();
        $.ajax({
            type: 'POST',
            url: '${basePath}/admin/api/activityInvitation/agree',
            dataType: 'text',
            data: {
                invitationId: invitationId
            },
            error: function (XHR, textStatus, errorThrown) {
                $.messager.progress('close');
                $.messager.alert('提示', '操作失败,请重试！', 'error');
            },
            success: function (data, textStatus) {
                $.messager.progress('close');
                data = $.parseJSON(data);
                if (data.success) {
                    $.messager.show({
                        title: '提示',
                        msg: '操作成功',
                    });
                    $('#invitation-grid').datagrid('load');
                } else {
                    $.messager.alert('提示', '操作失败,请重试！', 'warning');
                }
            }
        });
    }
    /**
     *拒绝邀请
     * @param obj
     */
    function refuseInvitation(data) {
        var invitationId = data.invitationId;
        $.messager.progress();
        $.ajax({
            type: 'POST',
            url: '${basePath}/admin/api/activityInvitation/refuse',
            dataType: 'text',
            data: {
                invitationId: invitationId
            },
            error: function (XHR, textStatus, errorThrown) {
                $.messager.progress('close');
                $.messager.alert('提示', '操作失败,请重试！', 'error');
            },
            success: function (data, textStatus) {
                $.messager.progress('close');
                data = $.parseJSON(data);
                if (data.success) {
                    $.messager.show({
                        title: '提示',
                        msg: '操作成功',
                    });
                    $('#invitation-grid').datagrid('load');
                } else {
                    $.messager.alert('提示', '操作失败,请重试！', 'warning');
                }
            }
        });
    }
    /**
     *查看详情
     * @param obj
     */
    function previewInvitation(data) {
        var url = '${basePath}/admin/invitation/' + data.invitationId + '/baseInfo';
        $('#activity_baseinfo_dialog').dialog({
            content: "<iframe scrolling='auto' frameborder='0'  src='" + url + "' style='width:100%;height:100%;' ></iframe>"
        });
        $('#activity_baseinfo_dialog').dialog('open');
    }

    /**
     *推送生日信息
     * @param obj
     */
    function pushuBirthdateInfo(obj) {
        var pushBrithdateId = $(obj).data('id');
        var userid = $(obj).data('userid');
        var username = $(obj).data('username');
        var userimg = $(obj).data('userimg');
        console.log(pushBrithdateId);
        console.log(userid);
        console.log(username);
        console.log(userimg);
        $('#push-brith-id').val(pushBrithdateId);
        $('#push-user-id').val(userid);
        $('#user-portriat').attr('src', userimg);
        $('#user-name').html(username);
        $('#push-content').val('亲爱的 "' + username + '" 用户,你的生日快到了哦！祝你生日快乐^_^!');
        $('#push-dialog').dialog('open');
    }

</script>
<table style="width: 100%;height: 380px;padding: 5px;">
    <tr>
        <td width="50%">
            <div class="easyui-panel" style="float: left;"
                 data-options="title:'活动邀请通知',width:'100%',height:300,fit:true">
                <table id="invitation-grid"></table>
                <div id="invitation-grid-toolbar">
                    <button class="easyui-linkbutton" id="agree_invitation" data-options="iconCls:'icon-ok'">同意</button>
                    <button class="easyui-linkbutton" id="un_agree_invitation" data-options="iconCls:'icon-cancel'">拒绝
                    </button>
                    <button class="easyui-linkbutton" id="preview_invitation" data-options="iconCls:'icon-preview'">预览
                    </button>
                </div>
            </div>
        </td>
        <td width="50%">
            <div class="easyui-panel" style="float: left;"
                 data-options="title:'推送提醒',width:'100%',height:300,fit:true">
                <table id="push-grid"></table>
            </div>
        </td>
    </tr>
</table>

<div class="easyui-dialog" id="activity_baseinfo_dialog" style="width: 350px;height: 260px;"
     data-options="title:'详情',closable:true,closed:true,modal:true">
</div>

<div id="push-dialog">
    <div style="text-align: center;font-size: 14px;margin: 10px auto 10px auto;color: green">推送生日祝福</div>

    <form id="push-brithday-form">
        <input type="hidden" id="push-brith-id" name="pushBrithdateId"/>
        <input type="hidden" id="push-user-id" name="userId"/>

        <div id="push-user-info">
            <div style="width:60px;height:60px;margin: auto;border-radius: 40px;overflow-y: hidden">
                <img id="user-portriat" style="width: 100%;"/>
            </div>
            <div id="user-name" style="text-align: center;"></div>
        </div>
        <ul style="list-style-type: none;width: 80%;margin: auto">
            <li>推送内容:</li>
            <li>
                <textarea name="pushInfo" id="push-content"
                          style="width: 100%;height: 70px;margin:10px auto 10px auto;border-radius: 5px"></textarea>
            </li>
        </ul>
    </form>
</div>

</body>
</html>
