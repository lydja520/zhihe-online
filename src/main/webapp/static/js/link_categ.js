function CategLoad(){
    $(function () {
        $('#t_categ').datagrid({
            url: '/admin/api/categ/list',
            columns: [[{
                field: 'categId',
                checkbox: true
            }, {
                field: 'categName',
                title: '商品名称',
                width: 100,
                align: 'center'
            }, {
                field: 'categDesc',
                title: '商品描述',
                width: 100
            },{
                field:'creatDate',
                title:'创建时间',
                width:100
            },{
                field:'permit',
                title:'是否可用',
                width:100,
                formatter:function(value){
                    if(value){
                            return "可用";
                    }
                    return "不可用";
                }
            }
            ]],
            toolbar: [{
                text: '添加',
                iconCls: 'icon-add',
                handler: function () {
                    $('#categ_form').form('clear');
                    $("#win").dialog({
                        title: "录入",
                        iconCls: 'icon-add',
                        width: 335,
                        height: 180,
                        modal: true,
                        inline: true,
                        buttons: [{
                            text: '添加',
                            iconCls: 'icon-save',
                            handler: function () {
                                $.messager.progress();
                                $('#categ_form').form('submit', {
                                    url: '/admin/api/categ/add',
                                    onSubmit: function () {
                                        var isValid = $(this).form('validate');
                                        if (!isValid) {
                                            $.messager.progress('close');
                                        }
                                        return isValid;
                                    },
                                    success: function (data) {
                                        data = $.parseJSON(data);
                                        $.messager.progress('close');
                                        if (data.success) {
                                            $.messager.show({
                                                title:'提示',
                                                msg:'添加成功'
                                            });
                                            $('#t_categ').datagrid('reload');
                                            $('#win').dialog('close');
                                        } else {
                                            $.messager.alert('提示', '系统错误稍后再试!');
                                        }
                                    }
                                });
                            }
                        },
                            {
                                text: '取消',
                                iconCls: 'icon-cancel',
                                handler: function () {
                                    $('#win').dialog('close');
                                }
                            }]
                    });
                }
            }, '-', {
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    var a = $('#t_categ').datagrid('getChecked');
                    console.log(a);
                        if (a.length != 1) {
                        $.messager.alert('提示', '一次只能编辑一条数据请勿多选或少选');
                        return;
                    }
                    else {
                        $('#categ_form').form('load', a[0]);
                           var index= !(a[0].permit)?1:0;
                            $(":radio").eq(index).attr('checked','true');

                        $("#win").dialog({
                            title: "编辑角色",
                            iconCls: 'icon-edit',
                            width: 335,
                            height: 180,
                            modal: true,
                            inline: true,
                            buttons: [{
                                text: '保存', iconCls: 'icon-save',
                                handler: function () {
                                    $('#categ_form').form('submit', {
                                        url: '/admin/api/categ/modify',
                                        onSubmit: function (params) {
                                            $.messager.progress();
                                        },
                                        success: function (data) {
                                            if (data != null) {
                                                $('#win').dialog('close');
                                                $.messager.show({
                                                    title:'提示',
                                                    msg:'修改成功'
                                                });
                                                $('#t_categ').datagrid('reload');
                                            } else {
                                                $('#win').dialog('close');
                                                $.message.alert('提示', '数据库异常,稍后重试！')

                                            }
                                            $.messager.progress('close');
                                        }
                                    });
                                }
                            },
                                {
                                    text: '取消', iconCls: 'icon-cancel', handler: function () {
                                    $('#win').dialog('close');
                                }
                                }
                            ]
                        });
                    }
                }
            }],
            fit: true,
            fitColumns: true,
            pageSize: 20,
            pagination: true
        });
    });


}