<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>发起活动</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <script type="text/javascript" src="${basePath}/static/core/app.js"></script>
    <script type="text/javascript">
        app.setBasePath('${basePath}');
        app.setup();
    </script>
    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <script type="text/javascript">
        $(function () {
            /*========初始化数据网格=======*/
            $('#activity_grid').datagrid({
                url: '${basePath}/admin/api/applyActivity/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                pageSize: 30,
                singleSelect: true,
                rownumbers: true,
                title: '我发起的活动',
                columns: [[
                    {field: '', checkbox: true, width: 100},
                    {
                        field: 'receptionRoom', title: '会客厅封面', width: 90, align: 'center',
                        formatter: function (data, row) {
                            if (data) {
                                url = data.coverImg.url;
                                return "<div style='margin: 1px auto 1px auto'><img style='width: 70px;height: 35px;' src='" + url + "'></div>";
                            }
                        }
                    },
                    {
                        field: 'roomName', title: '会客厅名称', width: 80, align: 'center',
                        formatter: function (data, row) {
                            if (row.receptionRoom) {
                                return row.receptionRoom.roomName;
                            }
                        }
                    },
                    {field: 'activitName', title: '活动名称', width: 100, align: 'center'},
                    {
                        field: 'category', title: '活动类别', width: 100, align: 'center',
                        formatter: function (category) {
                            return category.categName;
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
                    {field: 'displayState', title: '当前状态', width: 50, align: 'center'},
                    {
                        field: 'createDate', title: '创建时间', width: 100, align: 'center',sortable:true,
                        formatter: function (data) {
                            return "<span style='color:green'>" + data + "</span>";
                        }
                    },
                    {
                        field: 'beginDate', title: '开始时间', width: 100, align: 'center',sortable:true,
                        formatter: function (data) {
                            return "<span style='color:blue'>" + data + "</span>";
                        }
                    },
                    {
                        field: 'endDate', title: '结束时间', width: 100, align: 'center',sortable:true,
                        formatter: function (data) {
                            return "<span style='color: red'>" + data + "</span>";
                        }
                    },
                    {field: 'contacterName', title: '活动联系人', width: 50, align: 'center'},
                    {field: 'contactTell', title: '联系电话', width: 100, align: 'center'},
                    {
                        field: 'activitId', title: '查看', width: 100, align: 'center',
                        formatter: function (value) {
                            var buttons = "<button value=" + value + " onclick='preview_details(this.value)'>详情</button> ";
                            return buttons;
                        }
                    },
                ]],
                toolbar: '#toobar_searchbar'
            });

            /*========searchbar========*/
            $('#grid_searchbox').searchbox({
                width: 300,
                menu: '#searchbox_menu',
                prompt: '输入关键字搜索',
                searcher: function (value, name) {
                    var param = {};
                    if (value) {
                        param.searchName = name;
                        param.searchValue = value;
                    }
                    $('#activity_grid').datagrid('load', param);
                }
            });

            /**
             * 初始化会客厅datagrid
             * */
            $('#reception_room_grid').datagrid({
                url: '${basePath}/admin/api/receptionRooms',
                fit: true,
                fitColumns: true,
                pagination: true,
                pageSize: 20,
                singleSelect: true,
                rownumbers: true,
                border: false,
                columns: [[
                    {field: 'roomId', checkbox: true, width: 100},
                    {
                        field: 'coverImg', title: '会客厅名称', width: 80, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return '<div style="width: 100px;height: 60px; text-align: center;"><img style="width: 100px;height: 60px" src=' + data.url + '></div>';
                            }
                        }
                    },
                    {field: 'roomName', title: '会客厅名称', width: 80, align: 'center'},
                    {
                        field: 'attributeSet', title: '类别', width: 80, align: 'center',
                        formatter: function (data) {
                            if (data) {
                                return data.goodsAttSetName;
                            }
                        }
                    }
                    /*{field: 'templatePath', title: '模板路径', width: 100, align: 'center'},*/

                ]],
                toolbar: '#room_grid_toolbar'
            });

            /*========上传封面图========*/
            initUploader({
                pickbutton: 'pick-pics',
                onFileAdd: function (file) {
                    $('#file-name').textbox('setText', file.name);
                },
                onBeforeUpload: function () {
                    $('#prgress')[0].innerHTML = ' 正在上传...';
                },
                onUploaded: function (file, up, info) {
                    console.log(info);
                    $('#coverImg').val(info.imgInfoId);
                    $('#preview_img').attr('src', info.url);
                    $('#prgress')[0].innerHTML = ' 上传完成';
                },
                onError: function () {
                    $.messager.alert('提示', '图片上传失败，请重试', 'error');
                },
                onComplete: function () {

                }
            });

            /*========发起新的活动========*/
            $('#add_btn').click(function () {
                $('#data-dialog').dialog({
                    title: '发起活动',
                    iconCls: 'icon-add2',
                    width: 460,
                    height: 460,
                    modal: true,
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#data-form').form('submit', {
                                url: '${root}/admin/api/activity/apply',
                                success: function (data) {
                                    data = $.parseJSON(data);
                                    if (data.success) {
                                        $.messager.show({
                                            title: '提示',
                                            msg: data.msg
                                        });
                                        $('#data-dialog').dialog('close');
                                        $('#activity_grid').datagrid('load');
                                    } else {
                                        $.messager.alert('提示', data.msg, 'error');
                                    }
                                }
                            })
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#data-dialog').dialog('close');
                        }
                    }],
                    onBeforeOpen: function () {
                        $('#data-form').form('reset');
                    }
                });
            });

            /*========活动信息修改========*/
            $('#edit_btn').click(function () {
                var rows = $('#activity_grid').datagrid('getChecked');
                if (rows.length < 1) {
                    $.messager.alert('提示', '请选择需要修改的数据', 'error');
                    return;
                }
                var _data = rows[0];
                if (_data.currentState == 1 || _data.currentState == 4) {
                    $('#data-dialog').dialog({
                        title: '修改活动信息',
                        iconCls: 'icon-edit2',
                        width: 460,
                        height: 460,
                        modal: true,
                        buttons: [{
                            text: '保存',
                            iconCls: 'icon-ok',
                            handler: function () {
                                $('#data-form').form('submit', {
                                    url: '${root}/admin/api/activity/editApply',
                                    success: function (data) {
                                        data = $.parseJSON(data);
                                        if (data.success) {
                                            $.messager.show({
                                                title: '提示',
                                                msg: data.msg
                                            });
                                            if (data.success) {
                                                $('#data-dialog').dialog('close');
                                                $('#activity_grid').datagrid('load');
                                            }
                                        } else {
                                            $.messager.alert('提示', data.msg, 'error');
                                        }
                                    }
                                })
                            }
                        }, {
                            text: '取消',
                            iconCls: 'icon-cancel',
                            handler: function () {
                                $('#data-dialog').dialog('close');
                            }
                        }],
                        onBeforeOpen: function () {
                            console.log(_data);
                            $('#data-form').form('reset');
                            $('#data-form').form('load', rows[0]);
                            $('#activitCategory').combobox('setValue', _data.category.categId);
                            var attributeSets = _data.attributeSets;
                            if (attributeSets && $.isArray(attributeSets) && attributeSets.length > 0) {
                                var values = [];
                                $.each(attributeSets, function (index, item) {
                                    values[index] = item.goodsAttSetId;
                                });
                                $('#goodsCategory').combobox('setValues', values);
                            }
                            //$('#goodsCategory').combobox('setValue', _data.attributeSet.goodsAttSetId);
                            //$('#reception_room').combobox('setValue', _data.receptionRoom.roomId);
                            $('#reception_room').textbox('setValue', _data.receptionRoom.roomId);
                            $('#reception_room').textbox('setText', _data.receptionRoom.roomName);
                        }
                    });
                } else {
                    $.messager.alert('提示', '当前状态不支持编辑操作！', 'warning');
                }
            });

            /*========提交活动申请========*/
            $('#submit_audit').click(function () {
                var rows = $('#activity_grid').datagrid('getChecked');
                if (!rows || rows.length < 1) {
                    $.messager.alert('提示', '请选择需要一条数据', 'info');
                    return;
                }
                var activity = rows[0];
                if (activity.currentState != 1 && activity.currentState != 4) {
                    $.messager.alert('提示', '活动申请已经提交过，无需重复提交！', 'warning');
                } else {
                    $.messager.confirm('提交申请',
                            '确认要提交申请吗，请注意，一旦提交申请后，就不能进行，“商家联盟”,"添加活动红包"，“添加优惠券”，“添加活动商品”等修改活动信息的操作，请确认活动已经完全准备好后，点击确定按钮后，提交活动信息进行审核!',
                            function (r) {
                                if (r) {
                                    $.ajax({
                                        url: '${basePath}/admin/api/activity/submitAudit',
                                        dataType: 'text',
                                        type: 'post',
                                        data: {
                                            activitId: activity.activitId,
                                            currentState: activity.currentState
                                        },
                                        error: function () {
                                            $.messager.alert('提示', '出错了，请重试！', 'error');
                                        },
                                        success: function (info) {
                                            info = $.parseJSON(info);
                                            if (info.success) {
                                                $('#activity_grid').datagrid('load');
                                                $.messager.show({
                                                    title: '提示',
                                                    msg: info.msg
                                                });
                                            } else {
                                                $.messager.alert('提示', info.msg, 'info');
                                            }
                                        }
                                    });
                                }
                            });
                }
            });

            /*========商家联盟========*/
            $('#view_alliance').click(function () {
                var rows = $('#activity_grid').datagrid('getChecked');
                if (!rows || rows.length < 1) {
                    $.messager.alert('提示', '请选择需要一条数据', 'info');
                    return;
                }
                var act = rows[0];
                var content = '<iframe scrolling="auto" frameborder="0"  src="${root}/admin/activity/' + act.activitId + '/merchAlliance" style="width:100%;height:100%;"></iframe>';
                $('#merch-alliance-win').window({
                    content: content
                });

                $('#merch-alliance-win').window('open');
            });

            /*============点击选择会客厅=============*/
            $('#reception_room').textbox({
                onClickButton: function () {
                    $('#room_check_dialog').dialog('open');
                }
            });
        });

        function merchant_alliance(id) {
            $('#merchant_allinance').dialog({
                title: '商家联盟',
                width: 800,
                height: 600,
                onBeforeOpen: function () {
                    $('#merchant_allinance').attr('src', "${basePath}" + "/admin/activity/" + id + "/merchAlliance");
                }
            });
        }

        /*========预览详情========*/
        function preview_details(id) {
            $('#preview_detail').dialog({
                title: '查看详情',
                width: 800,
                height: 480,
                onBeforeOpen: function () {
                    $('#preview_detail').attr('src', "${basePath}" + "/admin/activity/" + id + "/info")
                }
            });
        }

    </script>
    <style>
        .activity_title {
            font-size: 14px;
            text-align: center;
        }

        .activity_title div {
            border-bottom: solid;
            border-color: green;
            border-width: 1px;
            color: #ff4500
        }

        .activity_title p {
            font-weight: bold;
        }
    </style>
</head>

<!--toolbar&searchbar开始-->
<div id="toobar_searchbar">
    <a class="easyui-linkbutton" data-options="iconCls:'icon-add2',plain:true" id="add_btn">发起活动</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-edit2',plain:true" id="edit_btn">活动信息修改</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-alliance',plain:true" id="view_alliance">商家联盟</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-submit',plain:true" id="submit_audit">申请</a>
    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <input id="grid_searchbox">
</div>
<div id="searchbox_menu">
    <div data-options="name:'activitName'">活动名称</div>
</div>
<!--toolbar&searchbar结束-->

<body>
<!--活动数据网格开始-->
<div id="activity_grid"></div>
<!--活动数据网格开始-->

<!--添加,修改开始-->
<div id="data-dialog">
    <form id="data-form" method="post">
        <input type="hidden" name="activitId" id="activitId">
        <%--<input type="hidden" id="coverImg" name="coverImg.imgInfoId">--%>
        <table style="width:90%;margin: 3% auto 3% auto;border-collapse: separate;border-spacing: 7px;font-size: 14px">
            <tr>
                <td colspan="2" class="activity_title">
                    <div>
                        <p>活动基本信息</p>
                    </div>
                </td>
            </tr>
            <tr>
                <td width="20%" style="font-size: 13px">活动名称：</td>
                <td width="80%">
                    <input name="activitName" class="easyui-textbox" data-options="required:true"
                           style="width:280px;" prompt="请输入活动名称"/>
                </td>
            </tr>
            <tr>
                <td style="font-size: 13px"><label>活动类别:</label></td>
                <td>
                    <input class="easyui-combobox" id="activitCategory" name="category.categId"
                           panelWidth="180" valueField="categId"
                           textField="categName" editable="fasle" required="true"
                           data-options="url:'${basePath}/admin/api/activityCategory/myCategList'"
                           style="width: 180px" prompt="请选择活动类别"/>
                </td>
            </tr>
            <tr>
                <td style="font-size: 13px"><label>商品类别：</label></td>
                <td>
                    <input class="easyui-combobox" id="goodsCategory" name="attributs"
                           panelWidth="240" valueField="goodsAttSetId" multiple="true"
                           textField="goodsAttSetName" editable="fasle" required="true"
                           data-options="url:'${basePath}/admin/api/goodsAttSet/allAtts'"
                           style="width: 180px" prompt="请选择商品类别"/>
                </td>
            </tr>
            <tr>
                <td style="font-size: 13px">开始时间：</td>
                <td>
                    <input class="easyui-datetimebox" name="beginDate" id="start_datetime"
                           data-options="required:true,editable:false" style="width:180px" prompt="请选择活动开始时间"/>
                </td>
            </tr>
            <%--<tr>
                <td style="font-size: 13px">结束时间：</td>
                <td>
                    <input class="easyui-validatebox" id="end_datetime" name="endDate"
                           style="width:180px" readonly="readonly"
                           data-options="required:true" prompt="请选择活动结束时间"/>
                </td>
            </tr>--%>
            <tr>
                <td style="font-size: 13px"><label>联系人：</label></td>
                <td>
                    <div id="contacter" required="true" class="easyui-textbox" name="contacterName"
                         style="width:280px;" prompt="请输入联系人姓名"/>
                </td>
            </tr>
            <tr>
                <td style="font-size: 13px">联系电话：</td>
                <td>
                    <input id="contactTell" required="true" class="easyui-textbox" name="contactTell"
                           style="width:280px;" prompt="请输入联系人电话"/>
                </td>
            </tr>
            <tr>
                <td style="font-size: 13px" valign="top">会客厅：</td>
                <td>
                    <input name="receptionRoom.roomId" class="easyui-textbox" editable="false" required="true"
                           prompt="请选择会客厅" style="width: 180px;" id="reception_room" buttonText="请选择">
                    <%--<input name="receptionRoom.roomId" class="easyui-combobox" data-options="url:'${basePath}/admin/api/receptionRoom/list',
                        width:180,valueField:'roomId',textField:'roomName',required:true,editable:false"
                           id="reception_room"/>--%>
                </td>
            </tr>
            <tr>
                <td valign="top">活动规则：</td>
                <td colspan="2">
                        <textarea name="activitDetail" class="easyui-textbox"
                                  data-options="multiline:true,required:true"
                                  style="width:280px;height: 80px" prompt="请输入活动的详情"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<!--添加,修改结束-->

<!--会客厅选择对话框-->
<div id="room_check_dialog" class="easyui-dialog" style="width: 700px;height: 420px;" data-options="closable:true,
    closed:true,title:'请选择会客厅',modal:true">
    <table id="reception_room_grid"></table>
    <div id="room_grid_toolbar">
        <button id="check_reception_room_btn" class="easyui-linkbutton" iconCls="icon-ok">选择</button>
        <input class="easyui-searchbox" style="width: 300px;" id="room_search_box"
               data-options="prompt:'输入关键字搜索',menu:'#room_search_menu',searcher:searchRoom"/>
    </div>
    <div id="room_search_menu">
        <div name="roomName">会客厅名称</div>
        <div name="attributeSet.goodsAttSetName">所属类别</div>
    </div>
    <script>
        $(function () {
            $('#room_check_dialog').dialog({
                onBeforeOpen: function () {
                    $('#room_search_box').searchbox('clear');
                    $('#reception_room_grid').datagrid('load', {});
                }
            });
            $('#check_reception_room_btn').click(function () {
                var rows = $('#reception_room_grid').datagrid('getChecked');
                if (!rows || rows.length < 1) {
                    $.messager.alert('提示', '请选择数据', 'info');
                    return;
                }
                var room = rows[0];
                console.log(room);
                $('#reception_room').textbox('setValue', room.roomId);
                $('#reception_room').textbox('setText', room.roomName);
                $('#room_check_dialog').dialog('close');
            });
        });
        function searchRoom(searchValue, searchName) {
            var param = {};
            if (searchName && $.trim(searchValue)) {
                param.searchName = searchName;
                param.searchValue = $.trim(searchValue);
            }
            $('#reception_room_grid').datagrid('load', param);
        }
    </script>
</div>

<!--会客厅选择对话框结束-->

<div id="merch-alliance-win" class="easyui-window"
     data-options="closed:true,collapsible:false,minimizable:false,modal:true,maximized:true,maximizable:false,
         iconCls:'icon-tip',title:'商家联盟',draggable:false">
</div>

<iframe style="margin: 0px;padding: 0px;border: none" id="preview_detail"></iframe>
<iframe style="margin: 0px;padding: 0px;border: none" id="merchant_allinance"></iframe>

</body>
</html>
