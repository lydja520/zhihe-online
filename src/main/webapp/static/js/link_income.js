function IncomeLoad(){
        $(function () {
            $('#t_income').datagrid({
                url: '/admin/api/income/list',
                columns: [[{
                    field: 'incomeId',
                    checkbox: true
                }, {
                    field: 'incomMin',
                    title: '最低收入',
                    width: 100,
                    align: 'center',
                    sortable:true
                }, {
                    field: 'incomMax',
                    title: '最高收入',
                    width: 100,
                    align: 'center',
                    sortable:true
                },{
                    field:'createDate',
                    title:'创建时间',
                    width:100,
                    align: 'center',
                    sortable:true
                }
                ]],
                toolbar: [{
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        $('#incom_form').form('clear');
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
                                    $('#income_form').form('submit', {
                                        url: '/admin/api/income/add',
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
                                                $('#t_income').datagrid('reload');
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
                        var a = $('#t_income').datagrid('getChecked');
                        if (a.length != 1) {
                            $.messager.alert('提示', '一次只能编辑一条数据请勿多选或少选');
                            return;
                        }
                        else {
                            $('#income_form').form('load', a[0]);
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
                                        $('#income_form').form('submit', {
                                            url: '/admin/api/income/modify',
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
                                                    $('#t_income').datagrid('reload');
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