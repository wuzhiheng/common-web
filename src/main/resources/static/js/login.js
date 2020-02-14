if (self != top) {
    //如果不是顶层页面，将其设为顶层页面
    top.location = self.location;
}

$(function () {

    //绑定enter键登录事件
    $(document).keydown(function (e) {
        if (e.keyCode == 13) {
            doLogin();
        }
    });
    $.ajaxSetup({async: false});
});

function checkLogin() {
    var loginName = $('[name=loginName]').val();
    var passWord = $('[name=password]').val();
    if (!loginName) {
        $('[name=loginName]').focus();
        $('#msg').text('请输入用户名!');
        return false;
    }
    if (!passWord) {
        $('[name=password]').focus();
        $('#msg').text('请输入密码!');
        return false;
    }

    return true;
}

function doLogin() {
    if (checkLogin()) {
        $.jCryption.getKeys(context+'/getPublicKey', function (receivedKeys) { // 异步请求获取用来加密的公钥
            var keys = receivedKeys;
            if (null != keys && "undefined" != keys) {
                var postData = {};
                var loginName = $('[name=loginName]').val();
                var password = $('[name=password]').val();
                //实用公钥进行加密
                $.jCryption.encrypt(password, keys, function (encryptedPasswd) {  ///使用公钥谨慎性加密
                    $.json({
                        type:'post',
                        url: context+'/login',
                        data: {loginName: loginName, password: encryptedPasswd},
                        success: function (data) {
                            if (data && data.code == '200') {
                                $('#msg').hide();
                                window.location.href = getContextPath()+'/indexPage/loginSuccess';
                            } else {
                                $('#msg').show().text(data.msg);
                            }
                        }
                    });
                })
            }
        });


    }
}


function getContextPath(fullUrl) {
    if (fullUrl == null || fullUrl == '') {
        fullUrl = window.location.href + '';
    }
    var arrUrl = fullUrl.split('/');
    return arrUrl[0] + '//' + arrUrl[2] + '/' + arrUrl[3];
}
