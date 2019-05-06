$(function () {
    $.ajax({
        type: 'get',
        url: all_url + '/home/get_home_store',
        dataType:'json',
        success: function (data) {
            // data = eval('(' + JSON.parse(JSON.stringify(data)) + ')')
            // console.log(totalPage)
            if(data.code == 200) {
                var orderHtml = template('orderTemp', data.data)
                $('#orderList').html(orderHtml)
            }else{
                $('#orderList').html('<p style="text-align: center;font-size: .32rem;padding: .5rem;">暂无订单</p>')
            }
        }
    })
})

