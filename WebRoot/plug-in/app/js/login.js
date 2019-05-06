var commonHost = '/app'


$(function () {

  // 手机号码正则
  var phoneReg = /^1[3|4|5|7|8][0-9]{9}$/
  // 6—12位密码正则
  var pwReg = /[A-Za-z0-9]{6,20}/

  // 设置toast弹层消失时间

  function login () {
    var un = $('#userName').val()
    var pw = $('#password').val()
    if (un == '' || pw == '') {
      $(document).dialog({
        type: 'notice',
        style: 'ios',
        infoText: '请将登录信息填写完整',
        autoClose: 800
      })
    }  else if (!pwReg.test(pw)) {
      $(document).dialog({
        type: 'notice',
        style: 'ios',
        infoText: '密码格式错误',
        autoClose: 800
      })
    } else {
      $.ajax({
        url: commonHost + '/check',
        type: 'post',
        dataType: 'json',
        data: {
          USERNAME: un,
          PASSWORD: pw
        },
        success: function (data) {
          if (data.code===200) {
            //校验成功，调用登录，返回页面与数据
              window.location.href = commonHost + '/login'

          }else {
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
    }
  }

  // 去掉用户输入的空格或者横线
  String.prototype.NoSpace = function () {
    return this.replace(/[\s|-]+/g, '')
  }
  $('#text').change(function () {
    var val = $('#text').val().NoSpace()
    $('#text').val(val)
  })

  //   登录请求
  $('#login input').click(function () {
    login()
  })

  /* 监听回车键*/
  document.onkeydown = function (e) {
    var ev = document.all ? window.event : e
    if (ev.keyCode == 13) {
      login()
    }
  }

  $('#modify').click(function (e) {
      window.location.href='app.do?modifyPassword'
  })
})


