<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/7/1
  Time: 10:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
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
      $('#dg').datagrid({
        url: '${basePath}/admin/api/admin/portraitList',
        title: '管理员信息列表',
        fit: true,
        fitColumns: true,
        singleSelect: true,
        pageSize: 20,
        pagination: true,
        toolbar: '#grid_toolbar',
        checkbox: true,
        columns: [[
          {
            field: 'a', checkbox: true, align: 'center'
          },
          {
            field: 'portrait.url', title: '头像', width: 100, align: 'center',
            formatter: function (value, rows, index) {
              if (rows.portrait == null && rows.portrait == null) {
                return "<div style='display:inline-block;border:none;margin: 18px 0 18px 0;align-content: center;'>无头像</div>";
              } else {
                return "<img width='50px' height='50px' style='border-radius:50%' src='" + rows.portrait.domain + rows.portrait.key + "'>";
              }
            }
          },
          {
            field: 'adminCode', title: '账号', width: 100, align: 'center'
          },
          {
            field: 'merchant', title: '商家名', width: 100, align: 'center',
            formatter: function (value, rows, index) {
              if (value) {
                return value.merchName;
              }
              return "";
            }
          },
          {
            field: 'chatNickName', title: '昵称', width: 100, align: 'center'
          },
          {
            field: 'superAdmin', title: '超级用户', width: 100, align: 'center',
            formatter: function (value, rows, index) {
              if (value == true) {
                return "<span style='color:green'>是</span>";
              } else {
                return "<span style='color:red'>不是</span>";
              }
            }
          },
          {
            field: 'permit', title: '是否启用', width: 100, align: 'center',
            formatter: function (value, rows, inde) {
              if (value == true) {
                return "<span style='color:green'>已启用</span>";
              } else {
                return "<span style='color:red'>未启用</span>";
              }
            }
          },
          {
            field: 'merchant.merchantId', title: '查看商家详情', width: 100, align: 'center',
            formatter: function (value, rows, index) {
              var merchId;
              if (rows.merchant != null) {
                merchId = rows.merchant.merchantId
              } else {
                merchId = "null";
              }
              return "<button value=" + merchId + " onclick='preview(this.value)'>查看详情</button>";
            }
          }
        ]],
      });

      /*=========上传图片========*/
      initUploader({
        pickbutton: 'select',
        onFileAdd: function (file) {

        },
        onError: function (file, err, errTip) {

        },
        onBeforeUpload: function (up, file) {
          total = up.total.size;
          loaded = up.total.loaded;
          progress = parseInt(loaded / total * 100);
          $('#progress').html('正在上传...');
        },
        onUploaded: function (up, file, info) {
          console.log(info);
          $('#pic-name').textbox('setText', file.name);
          $('#imgId').val(info.imgInfoId);
          $('#imgPreview').attr('src', info.url);
          $('#progress').html('上传完成');
        },
        onComplete: function () {

        }
      });

      /*====修改头像和昵称====*/
      function editPortraitName() {
        var rows = $('#dg').datagrid('getChecked');
        if (rows.length < 1) {
          $.messager.alert('提示', '请选择需要操作的数据', 'info');
          return;
        }
        $('#edit-win').dialog({
          title: '修改头像昵称',
          iconCls: 'icon-edit',
          width: 400,
          buttons: [{
            iconCls: 'icon-ok',
            text: '保存',
            handler: function () {
              $('#edit-form').form({
                url: '${basePath}/admin/api/admin/editPortraitName',
                success: function (data) {
                  data = $.parseJSON(data);
                  $.messager.show({
                    title: '提示',
                    msg: data.msg
                  });
                  if (data.success) {
                    $('#edit-win').dialog('close');
                    $('#dg').datagrid('load');
                  }
                }
              });
              $('#edit-form').form('submit');
            }
          }, {
            iconCls: 'icon-cancel',
            text: '关闭',
            handler: function () {
              $('#edit-win').dialog('close');
            }
          }],
          onBeforeOpen: function () {
            if (rows.length > 0) {
              var firstRow = rows[0];
              $('#imgPreview').attr('src', "${basePath}/static/images/preview.jpg");
              $('#edit-form').form('reset');
              $('#edit-form').form('load', firstRow);
              $('#admin').val(firstRow.adminId);
              $('#nickName').textbox('setValue', firstRow.chatNickName);
              if (firstRow.portrait != null) {
                var url = firstRow.portrait.url;
//                                console.log("ssss");
                $('#imgId').val(firstRow.portrait.imgInfoId);
                $('#pic-name').textbox('setValue', firstRow.portrait.key);
                $('#imgPreview').attr('src', url);
              }
            }
          }
        });
      }


      /*====修改账号====*/
      function editadminCode() {
        var rows = $('#dg').datagrid('getChecked');
        if (rows.length < 1) {
          $.messager.alert('提示', '请选择需要操作的数据', 'info');
          return;
        }
        $('#admin-code-win').dialog({
          title: '修改账号',
          iconCls: 'icon-edit',
          width: 400,
          buttons: [{
            iconCls: 'icon-ok',
            text: '保存',
            handler: function () {
              $('#admin-code-form').form({
                url: '${basePath}/admin/api/adminEdit/adminCodeEdit',
                success: function (data) {
                  data = $.parseJSON(data);
                  $.messager.show({
                    title: '提示',
                    msg: data.msg
                  });
                  if (data.success) {
                    $('#admin-code-win').dialog('close');
                    $('#dg').datagrid('load');
                  }
                }
              });
              $('#admin-code-form').form('submit');
            }
          }, {
            iconCls: 'icon-cancel',
            text: '关闭',
            handler: function () {
              $('#admin-code-win').dialog('close');
            }
          }],
          onBeforeOpen: function () {
            if (rows.length > 0) {
              var firstRow = rows[0];
              $('#admin-code-form').form('reset');
              $('#admin-code-form').form('load', firstRow);
              $('#adminid').val(firstRow.adminId);
              $('#adminOldCode').textbox('setValue', firstRow.adminCode);
            }
          }
        });
      }

      /*===========重置账号密码============*/
      function resetAdminPwd() {
        var rows = $('#dg').datagrid('getChecked');
        if (rows.length < 1) {
          $.messager.alert('提示', '请选择需要操作的数据', 'info');
          return;
        }
        var data = rows[0];
        $.messager.confirm('提示', '确定要进行此操作吗？重置后密码为123456', function (r) {
          if (r) {
            var json = {};
            json.adminId = data.adminId;
            request({
              data: json,
              url: '${basePath}/admin/api/adminEdit/resetadminPwd',
              success: function (data) {
                if (data.success) {
                  $.messager.show({
                    title: '提示',
                    msg: data.msg
                  });
                  $("#dg").datagrid('reload');
                } else {
                  $.messager.confirm('提示', '操作异常，稍后重试！')
                }

              },
              error: function () {

              }
            });
          }
        })
      }

      /*================用户搜索================*/
      $('#search').click(function () {
        $('#dg').datagrid('load', {
          adminCode: $('#admin-code').textbox('getValue'),
          chatNickName: $('#admin-name').textbox('getValue'),
          merchantName: $('#merchant-name').textbox('getValue')
        });
      });
      /*===========清楚搜索数据===========*/
      $('#clear-search').click(function () {
        $('#dg').datagrid('load', {});
        $('#admin-code').textbox('clear');
        $('#admin-name').textbox('clear');
        $('#merchant-name').textbox('clear');
      });

      /*==========重置密码按钮==========*/
      $('#edit-admin-pwd').click(function () {
        resetAdminPwd();
      })

      /*==========修改账号按钮============*/
      $('#edit-admin-code').click(function () {
        editadminCode();
      })
      /*===========修改头像和昵称按钮点击=============*/
      $('#edit-portrait-name').click(function () {
        editPortraitName();
      });

    });

    /*======查看商家详情========*/
    function preview(id) {
      if (id == "null") {
        $.messager.alert('提示', '不是商家', 'info');
        return;
      }
      $('#preview').dialog({
        title: '预览商家信息',
        width: 750,
        height: 400,
        modal:true,
        resizable:true,
        maximizable:true,
        onBeforeOpen: function () {
          $('#preview').attr('src', '${basePath}/admin/merchant/info/' + id + '');
        }
      });
    }

  </script>
  <style>
    body {
      padding: 0px;
      margin: 0px;
      font-family: "Microsoft Yahei";
    }

    ul {
      list-style-type: none;
    }

    ul li {
      float: left;
      margin: 5px auto 5px auto;
    }

    .li-title {
      width: 30%;
    }

    .li-content {
      width: 70%;
    }

    .clear {
      clear: both;
    }


  </style>
</head>
<body>
<div id="dg"></div>
<div id="grid_toolbar" class="toolbar-container">
  <a class="easyui-linkbutton" data-options="iconCls:'icon-edit'" id="edit-portrait-name">修改头像和昵称</a>&nbsp;&nbsp;
  <a class="easyui-linkbutton" data-options="iconCls:'icon-edit'" id="edit-admin-code">修改账号</a>&nbsp;&nbsp;
  <a class="easyui-linkbutton" data-options="iconCls:'icon-undo'" id="edit-admin-pwd">重置密码</a>&nbsp;&nbsp;
  管理员账号:<input class="easyui-textbox" id="admin-code" prompt="请输入账号"/>&nbsp;
  商家名称:<input class="easyui-textbox" id="merchant-name" prompt="请输入商家名"/>&nbsp;
  昵称:<input class="easyui-textbox" id="admin-name" prompt="请输入昵称"/>&nbsp;
  <a id="search" class="easyui-linkbutton" data-options="iconCls:'icon-search'">
    搜索
  </a>
  <a id="clear-search" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">
    清除搜索
  </a>
</div>
<div id="edit-win">
  <form id="edit-form">
    <input type="hidden" id="admin" name="adminId">
    <input type="hidden" id="imgId" name="imgInfoId">
    <ul>
      <li class="li-title">昵称:</li>
      <li class="li-content"><input class="easyui-textbox" style="width: 200px" name="chatNickName" id="nickName"
                                    data-options="required:true"></li>
      <li class="li-title">上传图片:<br>(建议长宽比例1:1)</li>
      <li class="li-content"><input class="easyui-textbox" style="width: 120px"
                                    data-options="required:true" id="pic-name">
        <button style="margin-left: 10px" id="select">选择图片</button>
      </li>
      <li class="li-title"></li>
      <li class="li-content">
        <img src="${basePath}/static/images/preview.jpg"
             style="width: 70px;" id="imgPreview">
        <span id="progress"></span>
      </li>
      <div class="clear"></div>
    </ul>
  </form>
</div>
<div id="admin-code-win">
  <form id="admin-code-form">
    <input type="hidden" id="adminid" name="adminId">
    <ul>
      <li class="li-title">旧账号:</li>
      <li class="li-content"><input class="easyui-textbox" style="width: 200px" id="adminOldCode"
                                    data-options="editable:false,required:true">
      </li>
      <li class="li-title">新账号:</li>
      <li class="li-content"><input class="easyui-textbox" style="width: 200px" name="newAdminCode"
                                    data-options="required:true">
      </li>
    </ul>
  </form>
</div>
<%--商家详细信息预览--%>
<iframe id="preview"></iframe>
</body>
</html>

