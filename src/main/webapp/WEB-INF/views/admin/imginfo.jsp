<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="e" uri="org.topteam/easyui" %>
<html>
<head>
    <title>云图片列表</title>
    <e:resource location="static/core" name="jquery.min.js"/>
    <e:resources location="static/easyui"/>
    <e:resource location="static/core" name="jquery.cookie.js"/>
    <e:resource location="static/core" name="utils.js"/>
    <script type="text/javascript">
        function previewBtn(url) {
            return '<button class="easyui-linkbutton preview" type="button" onclick="previewImg(this)" class="easyui-linkbutton" data-link="' + url + '">预览</button>'
        }
        function removeImgs() {
            var rows = $('#img-grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('提示', '请选择需要删除的数据', 'error');
                return;
            }
            var imgIds = '';
            $.each(rows, function (index, row) {
                imgIds += row.imgInfoId + ",";
            });
            $.messager.confirm('警告', '确定要删除所选数据吗？', function (r) {
                if (r) {
                    request({
                        type: 'POST',
                        url: '${root}/admin/api/imginfo/deleteBatch',
                        data: {
                            imgIds: imgIds
                        },
                        success: function (data) {
                            onAfterEdit(data, 'img-grid');
                        }
                    })
                }
            });
        }
        function gridSearch(value, name) {
            var param = {};
            if (value) {
                param.searchName = name;
                param.searchValue = value;
            }
            $('#img-grid').datagrid('load', param);
        }
        function previewImg(obj) {
            var url = $(obj).data('link');
            $('#preview-dialog').dialog('open');
            $('#preview-img').attr('src', url);
        }
    </script>
</head>
<e:body>
    <e:datagrid id="img-grid" fit="true" fitColumns="true" border="false" url="${root}/admin/api/imginfo/list"
                pagination="true" rownumbers="true" striped="true" selectOnCheck="true" checkOnSelect="false">
        <e:eventListener event="onLoadError" listener="onGridLoadError"/>
        <e:columns>
            <e:column field="imgInfoId" checkbox="true"/>
            <e:column field="key" title="图片名称" width="20" align="center"/>
            <e:column field="width" title="图片宽" width="20" align="center"/>
            <e:column field="height" title="图片高" width="20" align="center"/>
            <e:column field="bucket" title="所在云空间" width="20" align="center"/>
            <e:column field="createDate" title="上传时间" width="20" align="center" sortable="true"/>
            <e:column field="url" title="操作" width="20" align="center" formatter="previewBtn"/>
        </e:columns>
        <e:facet name="toolbar">
            <e:button id="add-btn" iconCls="icon-add" text="添加图片" plain="true" disabled="true">
                <%--<e:event event="click" target="add-dialog" action="open"/>--%>
            </e:button>
            <e:button id="remove-btn" iconCls="icon-remove" text="删除" plain="true">
                <e:eventListener event="click" listener="removeImgs"/>
            </e:button>
            <e:inputSearch id="grid-searchbox" style="width:260px;" prompt="输入关键字搜索">
                <e:eventListener event="searcher" listener="gridSearch"/>
                <e:facet name="menu">
                    <div name="key">存储名称</div>
                </e:facet>
            </e:inputSearch>
        </e:facet>
    </e:datagrid>

    <e:dialog id="add-dialog" style="width:400px;height:240px;" iconCls="icon-add" closed="true" closable="true"
              modal="true" title="添加图片">
    </e:dialog>

    <e:dialog id="preview-dialog" style="width:500px;height:380px;" iconCls="icon-ok" closed="true" closable="true"
              modal="true" title="图片预览" resizable="true">
        <img id="preview-img" style="width: 100%;height: auto;vertical-align: middle;text-align: center;" width="100%"
             height="100%">
    </e:dialog>
</e:body>
</html>
