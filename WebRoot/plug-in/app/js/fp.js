$(function () {
    $('.confirm').click(function () {
        var userName = $('#userName').val()
        var oldPassword = $('#oldPassword').val()
        var password = $('#password').val()
        if (!userName) {
            $(document).dialog({
                type: 'notice',
                style: 'ios',
                infoText: '用户名不能为空',
                autoClose: 800
            })
            return false
        }

        if (!userName) {
            $(document).dialog({
                type: 'notice',
                style: 'ios',
                infoText: '用户名不能为空',
                autoClose: 800
            })
            return false
        }
        if (!oldPassword) {
            $(document).dialog({
                type: 'notice',
                style: 'ios',
                infoText: '旧密码不能为空',
                autoClose: 800
            })
            return false
        }

        if (password.length<6 || password.length>18) {
            $(document).dialog({
                type: 'notice',
                style: 'ios',
                infoText: '新密码长度为6-18位',
                autoClose: 800
            })
        }

        $.ajax({
            url: '/app/modifyPword',
            data: {
              userName: userName,
              oldPassword: oldPassword,
              newPasswprd: password  
            },
            success: function (data) {
                console.log(data)
                if (data.code == 200) {
                    $(document).dialog({
                        type: 'notice',
                        style: 'ios',
                        infoText: '修改密码成功',
                        autoClose: 800
                    })
                    window.location.href='/app/index'
                } else {
                    $(document).dialog({
                        type: 'notice',
                        style: 'ios',
                        infoText: data.message,
                        autoClose: 800
                    })
                }
            },
            error: function () {
                $(document).dialog({
                    type: 'notice',
                    style: 'ios',
                    infoText: '网络异常，请稍后再试',
                    autoClose: 800
                })
            }
        })

    })

    $('.a').click(function () {
        window.location.href='/app/index'
    })

})
