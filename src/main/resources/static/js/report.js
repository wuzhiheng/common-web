
$(function () {

    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    var day = date.getDate();

    var today = [year, month, day];
    $('[name=reportTime]').val(today
        .map(function (v, index) {
            return v > 9 ? v : '0' + v;
        })
        .join('-'));


    $('[name=reportTime]').on('click', function (e) {

        weui.datePicker({
            end: new Date(),
            defaultValue: today,
            onConfirm: function (result) {
                var defaultValue = [1900, 1, 1];
                var day = result
                    .map(function (p, index) {
                        var value = p.value || defaultValue[index];
                        return value > 9 ? value : '0' + value;
                    })
                    .join('-');
                $('[name=reportTime]').val(day);
            },
            id: 'datePicker'
        });
    });

})



/*填报保存方法*/
function doSave(id) {

    var ok = true;
    $('#postForm input').each(function (idx, obj) {
        if(!this.value && this.name!='division' && this.name!='divisionId'){
            weui.topTips('请完善数据');
            ok = false;
            $(this).focus();
            return false;
        }
    })

    if(!ok) return;

    $.json({
        url: "../feverReport/save",
        type: 'POST',
        data: $('#postForm').serialize(),
        success: function (data) {
            if(data.code == '200'){
                weui.toast(data.msg,{
                    duration:500,
                    callback:function () {
                        location.href = 'check';
                    }
                });
            }else{
                weui.topTips(data.msg);
            }
        }
    });
}


