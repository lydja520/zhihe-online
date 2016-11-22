<%@ page import="cn.com.zhihetech.online.commons.Constant" %>
<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/21
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>APP导航管理</title>
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
        $(document).ready(function () {
            /*========初始化数据网格=======*/
            $('#datagrid_win').datagrid({
                url: '${basePath}/admin/api/navigation/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                rownumbers: true,
                columns: [[
                    {field: 'navigationId', checkbox: true, align: 'center'},
                    {
                        field: 'img', title: '导航图标', width: 70, align: 'center',
                        formatter: function (value) {
                            console.log(value);
                            return "<img src='" + value.url + "' style='width:40px;height:40px;margin:1px auto 1px auto'/>";
                        }
                    },
                    {field: 'navigationName', title: '导航名', width: 100, align: 'center'},
                    {field: 'desc', title: '描述', width: 100, align: 'center'},
                    {
                        field: 'viewTargert', title: '跳转到的模块', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value == <%=Constant.NAV_ONE%>) {
                                return '投诉维权';
                            } else if (value == <%=Constant.NAV_TWO%>) {
                                return '购物中心';
                            } else if (value == <%=Constant.NAV_THREE%>) {
                                return '活动专区';
                            } else if (value == <%=Constant.NAV_FOUR%>) {
                                return '特色街区';
                            } else if (value == <%=Constant.NAV_FIVE%>) {
                                return '优+店';
                            } else if (value == <%=Constant.NAV_SIX%>) {
                                return '分类';
                            } else {
                                return "<span style='color: red'>无</span>"
                            }
                        }
                    },
                    {field: 'order', title: '导航顺序', width: 100, align: 'center', sortable: true},
                    {
                        field: 'permit', title: '是否启用', width: 100, align: 'center',
                        formatter: function (value) {
                            if (value) {
                                return "<span style='color: green'>是</sapn>"
                            }
                            return "<span style='color:red;'>否</sapn>"
                        }
                    },
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        add();
                    }
                }, '-', {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                        edit();
                    }
                }, '-', {
                    text: '禁用',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        disable();
                    }
                }]

            });

            /*=========上传导航图标========*/
            initUploader({
                pickbutton: 'select',
                uploadbutton: 'upload',
                onFileAdd: function (file) {
                    $('#pic_name').textbox('setText', file.name);
                },
                onError: function (file, err, errTip) {

                },
                onBeforeUpload: function (up, file) {
                    total = up.total.size;
                    loaded = up.total.loaded;
                    progress = parseInt(loaded / total * 100);
                    $('#progress')[0].innerHTML = ' 正在上传...';
                },
                onUploaded: function (up, file, info) {
                    console.log(info);
                    $('#pic_id').val(info.imgInfoId);
                    $('#preview_icon').attr('src', info.url);
                    $('#progress')[0].innerHTML = ' 上传完成';
                },
                onComplete: function () {

                }
            });

            $('#select').click(function () {
                $('#preview_icon').attr('src', '${basePath}/static/images/preview.jpg');
                $('#progress')[0].innerHTML = '';
            });

            /*=========增加导航========*/
            function add() {
                $('#add_win').dialog({
                    title: '添加',
                    iconCls: 'icon-add',
                    width: 450,
                    modal: true,
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#add_form').form('submit', {
                                success: function (data) {
                                    data = $.parseJSON(data);
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    });
                                    if (data.success) {
                                        $('#add_win').dialog('close');
                                        $('#datagrid_win').datagrid('load');
                                    }
                                }
                            })
                        }
                    }, {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#add_win').dialog('close');
                        }
                    }],
                    onBeforeOpen: function () {
                        $('#add_form').form('reset');
                    }
                });
            }

            /*========修改导航========*/
            function edit() {
                var rows = $('#datagrid_win').datagrid('getSelections');
                if (!rows && rows.length < 0) {
                    $.messager.alert('提示', '请选择需要操作的数据', 'info');
                    return;
                }
                $('#add_win').dialog({
                    title: '修改',
                    iconCls: 'icon-add',
                    width: 450,
                    modal: true,
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#add_form').form('submit', {
                                url: '${basePath}/admin/api/navigation/edit',
                                success: function (data) {
                                    data = $.parseJSON(data);
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.msg
                                    });
                                    if (data.success) {
                                        $('#add_win').dialog('close');
                                        $('#datagrid_win').datagrid('load');
                                    }
                                }
                            })
                        }
                    }, {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#add_win').dialog('close');
                        }
                    }],
                    onBeforeOpen: function () {
                        var nav = rows[0];
                        console.log(nav);
                        $('#add_form').form('load', nav);
                        $('#pic_id').attr('value', nav.img.imgInfoId);
                        $('#pic_name').textbox('setValue', nav.img.key);
                        $('#preview_icon').attr('src', nav.img.url);
                    }
                });
            }

            /*========禁用导航========*/
            function disable() {
                alert(213);
            }

        });


    </script>
</head>
<body>
<!--数据网格开始-->
<div id="datagrid_win"></div>
<!--数据网格结束-->

<!--添加导航数据开始-->
<div id="add_win" align="center">
    <form action="${basePath}/admin/api/navigation/add" method="POST" id="add_form">
        <input type="hidden" name="navigationId" id="nav_id">
        <input type="hidden" name="img.imgInfoId" id="pic_id">
        <table style="margin: 10px 0px 10px 0px;border-collapse: separate;border-spacing: 7px">
            <tr>
                <td>导航名:</td>
                <td>
                    <input type="text" class="easyui-textbox" data-options="prompt:'请输入导航名',width:280"
                           name="navigationName" required="required">
                </td>
            </tr>
            <tr>
                <td valign="top">上传图标:<br>(建议比例40*40)</td>
                <td>
                    <div style="margin:0px auto 5px auto">
                        <input type="text" class="easyui-textbox" id="pic_name" editable="false">
                        <button type="button" id="select">选择图片</button>
                    </div>
                    <div>
                        <span>
                            <img src="${basePath}/static/images/preview.jpg" id="preview_icon"
                                 style="width: 100px;height: 100px">
                         </span>
                        <span id="progress"></span>
                    </div>
                </td>
            </tr>
            <tr>
                <td valign="top">描述:</td>
                <td>
                    <input type="" class="easyui-textbox"
                           data-options="prompt:'对该导航的描述',width:280,multiline:true,height:70"
                           name="desc">
                </td>
            </tr>
            <tr>
                <td>跳转到的模块:</td>
                <td>
                    <select id="add-viewTo" class="easyui-combobox" name="viewTargert" style="width:100px;">
                        <option value="1">投诉维权</option>
                        <option value="2">购物中心</option>
                        <option value="3">活动专区</option>
                        <option value="4">特色街区</option>
                        <option value="5">优“+”店</option>
                        <option value="6">分类</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>导航顺序:</td>
                <td><input type="text" class="easyui-textbox" data-options="prompt:'导航顺序',width:280" name="order"
                           required="required"></td>
            </tr>
            <tr>
                <td>是否启用:</td>
                <td>
                    <input type="radio" name="permit" value=true checked>是
                    <input type="radio" name="permit" value=false>否
                </td>
            </tr>
        </table>
    </form>
</div>
<!--添加导航数据结束-->

</body>
</html>
