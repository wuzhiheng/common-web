$(function () {
    //初始化表格数据
    $('#tableGrid').bootstrapTable({
        url: context_path + "/sysJob/list", // 获取表格数据的url
        cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
        striped: true,  //表格显示条纹，默认为false
        pagination: true, // 在表格底部显示分页组件，默认false
        pageList: [10, 20, 30, 40, 50], // 设置页面可以显示的数据条数
        pageSize: 10, // 页面数据条数
        pageNumber: 1, // 首页页码
        sidePagination: 'server', // 设置为服务器端分页
        queryParams: function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit,   //页面大小
                offset: params.offset,  //页码
                status: $('#queryForm [name=status]').val()
            };
            return temp;
        },
        singleSelect: true,
        columns: [
            {
                field: 'id',
                title: 'jobId',
            },
            {
                field: 'beanName',
                title: 'bean名称',
            },
            {
                field: 'methodName',
                title: '方法名称',
            },
            {
                field: 'methodParams',
                title: '方法参数',
            },
            {
                field: 'cronExpression',
                title: 'cron表达式',
            },
            {
                field: 'remark',
                title: '备注',
            }
            ,
            {
                field: 'status',
                title: '状态',
                formatter: function (v, r, i) {
                    var html =
                        '<label class="">' +
                        // '<small class="muted">Dark:</small>' +
                        '<input type="checkbox" class="ace ace-switch ace-switch-5" ' + (v == '1' ? 'checked' : '') + ' onclick="switchStatus(this,\'' + r.id + '\')">' +
                        '<span class="lbl middle"></span>' +
                        '</label>';

                    return html;
                }
            },
            {
                field: '',
                title: '操作',
                formatter: function (v, r, i) {
                    var html =
                        '<div class="btn-group btn-group-minier">' +
                        '<button type="button" class="btn btn-success btn-minier btn-round" onclick="doEdit(\'' + r.id + '\')"><i class="glyphicon glyphicon-edit"></i> 修改</button>' +
                        '<button type="button" class="btn btn-danger btn-minier btn-round" onclick="doDelete(\'' + r.id + '\')"><i class="glyphicon glyphicon-trash"></i> 删除</button>' +
                        '</div>';

                    return html;
                }
            }

        ]
    })
});

function doQuery() {
    $('#tableGrid').bootstrapTable('refresh');
}

function doEdit(id) {
    $.json({
        url: context_path + '/sysJob/getOne?id=' + id,
        success: function (data) {
            autoFillData($('#saveForm'), data.data);

            $('#myModalLabel').text('修改');
            $('#myModal').modal('show');

            setTimeout(function () {
                $('#saveForm').valid()
            },200);

        }
    });
}

function doDelete(id) {

    if (!confirm('是否确定删除？'))
        return;

    $.json({
        url: context_path + '/sysJob/delete?id=' + id,
        success: function (data) {
            weui.toast(data.msg, {
                duration: 500,
                callback: function () {
                    $('#tableGrid').bootstrapTable('refresh');
                }
            });
        }
    });
}

function doAdd() {
    $('#saveForm')[0].reset();
    $('#saveForm [name=id]').val('');
    $('#myModalLabel').text('新增');
    $('#myModal').modal('show');
}

function save() {

    var id = $('#saveForm [name=id]').val();
    var url = id ? context_path + '/sysJob/update' : context_path + '/sysJob/add';

    var data = $('#saveForm').serializeObject();
    data.status = $('#saveForm [name=status]').is(':checked') ? '1' : '0';

    $.json({
        type: 'post',
        url: url,
        data: data,
        success: function (res) {
            if (res.code == '200') {
                weui.toast(res.msg, {
                    duration: 500,
                    callback: function () {
                        $('#myModal').modal('hide');
                        $('#tableGrid').bootstrapTable('refresh');
                    }
                });

            } else {
                alert(res.msg);
            }
        }
    })
}

function switchStatus(obj, id) {

    var status = $(obj).is(':checked') ? '1' : '0';

    $.json({
        type: 'post',
        url: context_path + '/sysJob/status',
        data: {id: id, status: status},
        success: function (res) {
            if (res.code == '200') {
                weui.toast(res.msg, {
                    duration: 500,
                    callback: function () {
                        $('#tableGrid').bootstrapTable('refresh');
                    }
                });

            } else {
                alert(res.msg);
            }
        }
    })
}

//校验form
$('#saveForm').validate({
    //校验表单成功后的回调
    submitHandler: save,
    rules: {
        beanName: {
            required: true,
        },
        methodName: {
            required: true,
        },
        cronExpression: {
            required: true
        },
    },
    messages: {
        beanName: {
            required: "bean名称不能为空",
        },
        methodName:"方法名称不能为空",
        cronExpression: "cron表达式不能为空"
    },
    //显示错误的包裹元素
    errorElement: 'span',
    //存放显示错误的位置
    errorPlacement: function (error, element) {

        error.addClass('text-danger').insertAfter(element.parent());
    },
    //显示错误时输入框的样式
    highlight: function (element, errorClass, validClass) {
        $(element).parents('.form-group').addClass('has-error').removeClass('has-success');
        $(element).after('<i class="ace-icon fa fa-times-circle"></i>');
    },
    //成功
    unhighlight: function (element, errorClass, validClass) {
        $(element).parents('.form-group').removeClass('has-error');
        $(element).siblings('i').remove();
    }
});
