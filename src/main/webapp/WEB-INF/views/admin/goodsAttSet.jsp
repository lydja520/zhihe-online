<%--
  Created by IntelliJ IDEA.
  User: YangDaiChun
  Date: 2015/12/4
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商品分类</title>
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
            /*========商品分类数据网格初始化========*/
            $('#goodsAttSet_grid').datagrid({
                url: '/admin/api/goodsAttSet/list',
                fit: true,
                fitColumns: true,
                pagination: true,
                singleSelect: true,
                columns: [[
                    {field: 'goodsAttSetId', checkbox: true, width: 100, align: 'center'},
                    {
                        field: 'coverImg', title: '封面图', width: 70, align: 'center',
                        formatter: function (value) {
                            return "<img style='width:45px;height:45px;margin:1px auto 1px auto' src='" + value.url + "'/>"
                        }
                    },
                    {field: 'goodsAttSetName', title: '商品分类名', width: 100, align: 'center'},
                    {field: 'goodsAttSetDesc', title: '商品分类描述', width: 100, align: 'center'},
                    {field: 'attSetOrder', title: '顺序', width: 100, align: 'center'},
                    {field: 'creatDate', title: '创建时间', width: 100, align: 'center'},
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        add();
                    },
                }, {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                        edit();
                    }
                }, {
                    text: '删除',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        var rows = $('#goodsAttSet_grid').datagrid('getChecked');
                        if (rows.length != 1) {
                            $.messager.alert('提示', '请选择需要删除的数据！', 'info');
                            return;
                        }
                        $.messager.confirm('警告', '确定要删除选中的数据吗？', function (r) {
                            if (r) {
                                deleteData(rows[0]);
                            }
                        });
                    }
                }],
            });

            /*=========上传封面图片========*/
            $.each($('.select_btn'), function (index, element) {
                var pickbutton = $(element).attr('id');

                initUploader({
                    pickbutton: pickbutton,
                    onFileAdd: function (file) {
                        console.log(file.name);
                        $(element).prev().val(file.name);
                    },
                    onError: function (file, err, errTip) {

                    },
                    onBeforeUpload: function (up, file) {
                        total = up.total.size;
                        loaded = up.total.loaded;
                        progress = parseInt(loaded / total * 100);
                        $(element).next().next().next().next()[0].innerHTML = ' 正在上传...';
                    },
                    onUploaded: function (up, file, info) {
                        console.log(info);
                        $(element).next().next().next().children().attr('src', info.url);
                        $(element).next().next().next().next()[0].innerHTML = ' 上传完成';
                        $(element).prev().prev().attr('value', info.imgInfoId);
                    },
                    onComplete: function () {

                    }
                });
            });
        });

        /*========添加商品分类========*/
        function add() {
            $('#add_win').dialog({
                title: '添加',
                iconCls: 'icon-add',
                width: 450,
                buttons: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        $('#add_form').form('submit', {
                            success: function (info) {
                                info = $.parseJSON(info);
                                $.messager.show({
                                    title: '提示',
                                    msg: info.msg
                                });
                                if (info.success) {
                                    $('#add_win').dialog('close');
                                    $('#goodsAttSet_grid').datagrid('load');
                                }
                            }
                        });
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $('#add_win').dialog('close');
                    }
                }],
                onBeforeOpen: function () {
                    $('#add_form').form('reset');
//                    $('.pic_name').val("");
//                    $('.loadstatus').val("");
                },
            });
        }

        /*========修改商品分类========*/
        function edit() {
            var rows = $('#goodsAttSet_grid').datagrid('getSelections');
            if (rows.length == 0) {
                $.messager.alert('提示', '请选中一行进行修改');
            } else if (rows.length == 1) {
                $('#edit_win').dialog({
                    title: '修改',
                    iconCls: 'icon-edit',
                    width: 450,
                    buttons: [{
                        text: '修改',
                        iconCls: 'icon-add',
                        handler: function () {
                            $('#edit_form').form('submit', {
                                success: function (info) {
                                    info = $.parseJSON(info);
                                    $.messager.show({
                                        title: '提示',
                                        msg: info.msg
                                    });
                                    if (info.success) {
                                        $('#edit_win').dialog('close');
                                        $('#goodsAttSet_grid').datagrid('load');
                                    }
                                }
                            });
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#edit_win').dialog('close');
                        }
                    }],
                    onBeforeOpen: function () {
                        $('#eidt_form').form('reset');
                        $('#pic_name').attr('value', rows[0].coverImg.key);
                        $('#pic').attr('src', rows[0].coverImg.url);
                        var temp = {
                            goodsAttSetId: rows[0].goodsAttSetId,
                            goodsAttSetName: rows[0].goodsAttSetName,
                            goodsAttSetDesc: rows[0].goodsAttSetDesc,
                            permit: rows[0].permit,
                            "coverImg.imgInfoId": rows[0].coverImg.imgInfoId,
                            creatDate: rows[0].creatDate,
                            attSetOrder: rows[0].attSetOrder
                        };
                        $('#edit_form').form('load', temp);
                    }
                });
            } else {
                $.messager.alert('选中了多行', '你选中了多行，请选中一行进行修改');
            }
        }

        /**
         * 删除指定的数据
         * @param data
         */
        function deleteData(data) {
            $.messager.progress();
            $.ajax({
                url: '${basePath}/admin/api/goodsAttSet/' + data.goodsAttSetId + '/delete',
                type: 'post',
                dataType: 'text',
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.success) {
                        $.messager.show({
                            title: '提示',
                            msg: '删除成功！'
                        });
                        $('#goodsAttSet_grid').datagrid('load');
                    } else {
                        $.messager.alert('提示', data.msg, 'info');
                    }
                },
                error: function () {
                    $.messager.alert('提示', '删除数据出错，请重试！', 'error');
                },
                complete: function () {
                    $.messager.progress('close');
                }
            })
        }
    </script>
</head>
<body>
<!--显示商品分类数据网格开始-->
<div id="goodsAttSet_grid"></div>
<!--显示商品分类数据网格结束-->

<!--添加商品分类开始-->
<div id="add_win">
    <form id="add_form" action="${basePath}/admin/api/goodsAttSet/add" method="post">
        <table align="center" style="width: 80%;margin-top: 20px">
            <tr>
                <td>
                    商品分类名:
                </td>
                <td>
                    <input name="goodsAttSetName" class="easyui-textbox" data-options="required:true,width:250">
                </td>
            </tr>
            <tr>
                <td valign="top">商品分类描述:</td>
                <td>
                    <input name="goodsAttSetDesc" class="easyui-textbox"
                           data-options="multiline:true,width:250,height:50">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <span class="item_title">封面图：</span>
                </td>
                <td>
                    <input type="hidden" name="coverImg.imgInfoId">
                    <input type="text"  placeholder="图片名" style="width: 120px"
                           data-options="editable:false">
                    <button type="button" class="btn btn-default select_btn" id="selectbutton1">选择图片</button>
                    <input type="hidden"/>

                    <div style="clear: both"></div>
                    <div style="border: dotted;border-width: 1px;width: 120px;height: 120px;float:left;margin:10px auto 10px auto;">
                        <img src="${basePath}/static/images/preview.jpg" style="width: 100%;height: 100%">
                    </div>
                    <div style="margin-top: 55px;"></div>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="item_title">顺序：</span>
                </td>
                <td>
                    <input class="easyui-numberspinner" required="required" value="1"
                           data-options="min:1" style="width:250px" name="attSetOrder">
                </td>
            </tr>
        </table>
    </form>
</div>
<!--添加商品分类结束-->

<!--修改商品分类开始-->
<div id="edit_win">
    <form id="edit_form" action="${basePath}/admin/api/goodsAttSet/edit" method="post">
        <input type="hidden" name="creatDate">
        <input type="hidden" name="permit">
        <table align="center" style="width: 80%;margin-top: 20px">
            <input type="hidden" name="goodsAttSetId">
            <tr>
                <td>
                    商品分类名:
                </td>
                <td>
                    <input name="goodsAttSetName" class="easyui-textbox" data-options="required:true,width:250">
                </td>
            </tr>
            <tr>
                <td>商品分类描述:</td>
                <td>
                    <input name="goodsAttSetDesc" class="easyui-textbox"
                           data-options="multiline:true,width:250,height:50">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <span class="item_title">封面图：</span>
                </td>
                <td>
                    <input type="hidden" name="coverImg.imgInfoId">
                    <input type="text" id="pic_name" placeholder="图片名" style="width: 120px"
                           data-options="editable:false">
                    <button type="button" class="btn btn-default select_btn" id="selectbutton2">选择图片</button>
                    <input type="hidden"/>

                    <div style="clear: both"></div>
                    <div style="border: dotted;border-width: 1px;width: 100px;height: 120px;float:left;margin:10px auto 10px auto;">
                        <img src="${basePath}/static/images/preview.jpg" style="width: 100%;height: 100%" id="pic">
                    </div>
                    <div style="margin-top: 55px;"></div>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="item_title">顺序：</span>
                </td>
                <td>
                    <input class="easyui-numberspinner" required="required" data-options="min:1"
                           style="width:250px" name="attSetOrder">
                </td>
            </tr>
        </table>
    </form>
</div>
<!--修改商品分类结束-->

</body>
</html>
