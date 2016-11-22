<%--
  Created by IntelliJ IDEA.
  User: ShenYunjie
  Date: 2016/4/5
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>挚合会客厅</title>
    <c:set var="basePath" value="${pageContext.request.contextPath}"/>
    <%@ include file="/WEB-INF/views/commons/header.jsp" %>
    <script type="text/javascript" src="${basePath}/static/core/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/qiniu.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/core/uploader.js"></script>
    <script type="text/javascript">
        $(function () {
            $.each($('.select_img_btn'), function (index, element) {
                /*var keyName = $(element).data('key');*/
                var pickbutton = $(element).attr('id');
                var fileName = $(element).prev();
                fileName.textbox({
                    required: true,
                    editable: false,
                    invalidMessage: '请先上传封面图！'
                });
                initUploader({
                    pickbutton: pickbutton,
                    onFileAdd: function (file) {
                        console.log(file);
                        fileName.textbox('setText', file.name);
                    },
                    onError: function (file, err, errTip) {
                        $.messager.alert('提示', '上传图片失败，请重试！', 'error');
                        $(element).next().next()[0].innerHTML = '';
                    },
                    onBeforeUpload: function (up, file) {
                        total = up.total.size;
                        loaded = up.total.loaded;
                        progress = parseInt(loaded / total * 100);
                        $(element).next().next()[0].innerHTML = ' 正在上传...';
                    },
                    onUploaded: function (up, file, info) {
                        console.log(info);
                        $(element).next().children().attr('src', info.url);
                        $(element).next().next()[0].innerHTML = ' 上传完成';
                        fileName.textbox('setValue', info.imgInfoId);
                    },
                    onComplete: function () {

                    }
                });
            });
            $('#add_room_form').form({
                onSubmit: function (params) {
                    var val = $(this).form('validate');
                    if (!val && !$('#add_file_name').textbox('getValue')) {
                        $.messager.alert('提示', '请先上传封面图！', 'info');
                    }
                    return val;
                }
            });
            $('#edit_room_form').form({
                onSubmit: function (params) {
                    var val = $(this).form('validate');
                    if (!val && !$('#edit_file_name').textbox('getValue')) {
                        $.messager.alert('提示', '请先上传封面图！', 'info');
                    }
                    return val;
                }
            })
        });

        function resetAddDialog() {
            $('#add_room_form').form('reset');
            $('#add_cover_preview').attr('src', '${basePath}/static/images/preview.jpg');
            $('#progress').val('');
        }

        function addSuccessCallback(data) {
            onAfterEdit(data, 'room-grid', function () {
                $('#add_room_dialog').dialog('close');
            })
        }


        function resetEditDialog(room) {
            $('#edit_room_form').form('reset');
            $('#edit_room_form').form('load', room);
            $('#edit_cover_preview').attr('src', room.coverImg.url);
            $('#edit_progress').val('');
            if (room.attributeSet) {
                $('#edit_attributeSet').combobox('setValue', room.attributeSet.goodsAttSetId);
            }
            $('#edit_file_name').textbox('setText', room.coverImg.key);
            $('#edit_file_name').textbox('setValue', room.coverImg.imgInfoId);
        }

        function modifyCheckData() {
            var rows = $('#room-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要修改的数据！', 'info');
                return;
            }
            $('#edit_room_dialog').dialog('open');
            resetEditDialog(rows[0]);
        }

        function modifySuccessCallback(data) {
            onAfterEdit(data, 'room-grid', function () {
                $('#edit_room_dialog').dialog('close');
            })
        }

        function deleteCheckedRoom() {
            var rows = $('#room-grid').datagrid('getChecked');
            if (!rows || rows.length < 1) {
                $.messager.alert('提示', '请选择需要删除的数据！', 'info');
                return;
            }
            $.messager.confirm('警告', '确定要删除所选的数据吗？', function (r) {
                if (r) {
                    $.messager.progress();
                    request({
                        url: '${basePath}/admin/api/receptionRoom/delete',
                        data: {roomId: rows[0].roomId},
                        success: function (data) {
                            if (data.success) {
                                $.messager.show({
                                    title: '提示',
                                    msg: '删除成功！'
                                });
                                $('#room-grid').datagrid('load');
                            } else {
                                $.messager.alert('提示', data.msg, 'info');
                            }
                        },
                        error: function () {
                            $.messager.alert('提示', '删除失败，请重试！', 'error');
                        },
                        complete: function () {
                            $.messager.progress('close');
                        }
                    });
                }
            })
        }

        function searchRoom(value, name) {
            var param = {};
            if (name && $.trim(value)) {
                param.searchName = name;
                param.searchValue = $.trim(value);
            }
            $('#room-grid').datagrid('load', param);
        }

        function getCoverImg(value) {
            if (value) {
                return '<img src="' + value.url + '" style="width:100px;height:60px;"/>'
            }
        }

        function getGoodsAttrName(value) {
            if (value) {
                return value.goodsAttSetName;
            }
        }
    </script>
</head>
<e:body>
    <e:datagrid id="room-grid" url="${root}/admin/api/receptionRooms" singleSelect="true"
                fit="true" pagination="true" fitColumns="true" rownumbers="true" border="false" striped="true">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="roomId" checkbox="true"/>
            <e:column field="coverImg" title="封面图" align="center" width="20" formatter="getCoverImg"/>
            <e:column field="roomName" title="会客厅名称" align="center" width="20"/>
            <e:column field="templatePath" title="模板" align="center" width="20"/>
            <e:column field="attributeSet" title="所属类别" align="center" width="20" formatter="getGoodsAttrName"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-room" iconCls="icon-add" text="添加" plain="true">
                <e:event event="click" target="add_room_dialog" action="open"/>
            </e:button>
            <e:button id="edti_room" iconCls="icon-edit" text="修改" plain="true">
                <e:eventListener event="click" listener="modifyCheckData"/>
            </e:button>
            <e:button id="remove-room" iconCls="icon-remove" text="删除" plain="true">
                <e:eventListener event="click" listener="deleteCheckedRoom"/>
            </e:button>

            <e:inputSearch id="activit-grid-searchbox" prompt="输入关键字搜索" style="width:260px">
                <e:eventListener event="searcher" listener="searchRoom"/>
                <e:facet name="menu">
                    <div name="roomName">会客厅名称</div>
                    <div name="attributeSet.goodsAttSetName">所属类别名称</div>
                </e:facet>
            </e:inputSearch>
        </e:facet>
    </e:datagrid>
    <e:dialog id="add_room_dialog" closed="true" style="width:400px;height:360px;" title="添加会客厅"
              modal="true" resizable="false" iconCls="icon-add">
        <e:eventListener event="onBeforeOpen" listener="resetAddDialog"/>
        <e:form id="add_room_form" method="post" url="${root}/admin/api/receptionRoom/add">
            <e:eventListener event="success" listener="addSuccessCallback"/>
            <table class="form-table">
                <tr>
                    <td><label>会客厅名称</label></td>
                    <td>
                        <e:inputText id="add_roomName" name="roomName" required="true" style="width:200px;"
                                     missingMessage="名称不能为空"/>
                    </td>
                </tr>
                <tr>
                    <td><label>模板名称</label></td>
                    <td>
                        <%--<e:inputText id="add_template_path" name="templatePath" required="true"
                                     style="width:200px;"></e:inputText>--%>
                        <select class="easyui-combobox" style="width: 180px;" required="true" editable="false"
                                name="templatePath">
                            <option value="default" selected="true">默认模板</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label>所属类别</label></td>
                    <td>
                        <e:comboBox id="add_attributeSet" name="attributeSet.goodsAttSetId" style="width:180px;"
                                    url="${basePath}/admin/api/goodsAttSet/allAtts" required="true" editable="false"
                                    valueField="goodsAttSetId" textField="goodsAttSetName"/>
                    </td>
                </tr>
                <tr>
                    <td><label>封面图<br>(建议比例1080*540)</label></td>
                    <td>
                        <input id="add_file_name" style="width:180px" name="coverImg.imgInfoId">
                        <button class="easyui-linkbutton select_img_btn" id="add_room_picker"
                                data-options="iconCls:'icon-ok'">
                            选择图片
                        </button>

                        <div style="border: dotted;border-width: 1px;width: 160px;height: 100px;margin-top:10px">
                            <img id="add_cover_preview" src="${basePath}/static/images/preview.jpg"
                                 style="width: 100%;height: 100%">
                        </div>
                        <div id="add_progress" class="upload-progress"></div>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="add_room_submit" text="添加" iconCls="icon-ok">
                <e:event event="click" target="add_room_form" action="submit"/>
            </e:button>
        </e:facet>
    </e:dialog>

    <e:dialog id="edit_room_dialog" closed="true" style="width:400px;height:360px;" title="添加会客厅"
              modal="true" resizable="false" iconCls="icon-edit">
        <e:form id="edit_room_form" method="post" url="${root}/admin/api/receptionRoom/update">
            <e:eventListener event="success" listener="modifySuccessCallback"/>
            <table class="form-table">
                <tr>
                    <td><label>会客厅名称</label></td>
                    <td>
                        <input type="hidden" name="roomId" id="edit_roomId"/>
                        <e:inputText id="edit_roomName" name="roomName" required="true" style="width:200px;"
                                     missingMessage="名称不能为空"/>
                    </td>
                </tr>
                <tr>
                    <td><label>模板名称</label></td>
                    <td>
                        <%--<e:inputText id="edit_template_path" name="templatePath" required="true"
                                     style="width:200px;"></e:inputText>--%>
                        <select class="easyui-combobox" style="width: 180px;" required="true" editable="false"
                                name="templatePath" id="edit_template_path">
                            <option value="default" selected="true">默认模板</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label>所属类别</label></td>
                    <td>
                        <e:comboBox id="edit_attributeSet" name="attributeSet.goodsAttSetId" style="width:110px;"
                                    url="${basePath}/admin/api/goodsAttSet/allAtts" required="true" editable="false"
                                    valueField="goodsAttSetId" textField="goodsAttSetName"/>
                    </td>
                </tr>
                <tr>
                    <td><label>封面图</label></td>
                    <td>
                        <input id="edit_file_name" style="width:180px" name="coverImg.imgInfoId">
                        <button class="easyui-linkbutton select_img_btn" id="edit_room_picker"
                                data-options="iconCls:'icon-ok'">
                            选择图片
                        </button>

                        <div style="border: dotted;border-width: 1px;width: 160px;height: 100px;margin-top:10px">
                            <img id="edit_cover_preview" src="${basePath}/static/images/preview.jpg"
                                 style="width: 100%;height: 100%">
                        </div>
                        <div id="edit_progress" class="upload-progress"></div>
                    </td>
                </tr>
            </table>
        </e:form>
        <e:facet name="buttons">
            <e:button id="edit_room_submit" text="修改" iconCls="icon-ok">
                <e:event event="click" target="edit_room_form" action="submit"/>
            </e:button>
        </e:facet>
    </e:dialog>
</e:body>
</html>
