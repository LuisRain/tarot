var log = localStorage.getItem('log');
var html = '';
if (log) {
  log = JSON.parse(log);
  $(log).each(function (i, v) {
    html += '<li>' + v + '</li>'
    $('.log ul').html(html)
  })
} else {
  log = []
}

$(function () {
  $('form').submit(function (event) {
    event.preventDefault();
    document.activeElement.blur();
    var val = $('input').val()
    search(val, 1)
  })
  $('.log li').click(function () {
    search($(this).text(), 0)
  })

  $('.delete').click(function () {
    localStorage.clear()
    $('.log ul').html('')
  })

  function search (val, type) {
    if (type) {
      val = val.trim()
      if (val) {
        if (check(val, log)) {
          if (log.length == 10) {
            log.pop()
          }
          log.unshift(val);
          log = JSON.stringify(log);
          localStorage.setItem('log', log);
        }
      } else {
        $(document).dialog({
            type:"notice",
            style: 'ios',
            infoText:"请输入要搜索的商品名称",
            autoClose:800
        })
        return false;
      }
    }
    location.href = './searchOut.html?search=' + val
  }

  function check (val, arr) {
    var flag = true
    val = val.trim(val)
    if (arr.length) {
      $(arr).each(function (i, v) {
        if (v == val) {
          flag = false
        }
      })
    }
    return flag
  }
})
