var id = 'aa'
$(function () {
    //获取当前商品页id
    id = $("#commodify_id").val();
    // 数据渲染
    $.ajax({
        type: 'get',
        url: '/app/findById',
        data : {
            id : id
        },
        dataType : "json",
        success: function (data) {
            // 数据列表
            var commodifyHtml = template('commodifyTem', data)
            $('#commodify').append(commodifyHtml)
            // 列表详情
            var detailsHtml = template('detailsTem', data)
            $('#details').append(detailsHtml)
            $(":input").css("border", "0px");
        },
        error: function (data) {
            $(document).dialog({
                type: 'notice',
                style: 'ios',
                infoText: '网络异常，请稍后再试',
                autoClose: 1000
            })
        }
    })
})


function addShop1() {
    $.ajax({
        type: 'get',
        url: '/app/addCart?id='+id,
        success: function (data) {
            if (data.code == 200) {

                $(document).dialog({
                    type: 'notice',
                    infoText: data.message,
                    autoClose: 1000,
                    position: 'middle'
                })
            }else {
                $(document).dialog({
                    type: 'notice',
                    infoText: data.message,
                    autoClose: 1000,
                    position: 'middle'
                })
            }
        }
    })

}

