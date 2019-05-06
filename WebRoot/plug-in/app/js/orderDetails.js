$(function () {
    $.ajax({
        type: 'get',
        url: '/app/getOrderDetails',
        dataType:'json',
        success: function (data) {
            if(data.code == 200) {
                var data = data.data
                $('#commodityNum').text(data.count)
                $('#commodityPrice').text(data.totalMoney)
                var orderDetailsHtml = template('orderDetailsTemp', data)
                $('#orderDetails').html(orderDetailsHtml)
            }else{
                $('#orderList').html('<p style="text-align: center;font-size: .32rem;padding: .5rem;">暂无订单</p>')
            }
        }
    })
})

