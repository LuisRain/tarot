var page = 1,totalPage = 1,timers = null;
$(function () {
    newList(page)
    //获取基本信息
    $.ajax({
        url: '/app/baseUser',
        success: function (data) {
            if (data.code == 200) {
                var data = data.data
                $('.userName').text('站点：'+data.merchantName)
                $('.site').text('用户名：'+data.merchantNum)
                $('.address').text('地址：'+data.address)
            }
        }
    })


    // 懒加载启动逻辑
    var beforeScrollTop = 0
    $('.scrollMain').scroll(function () {
        //    联盟会员滑动
        var afterScrollTop = $(this).scrollTop(),
            delta = afterScrollTop - beforeScrollTop;
        // 判断向上还是向下滚动
        if (delta > 0) {
            // 当时滚动条离底部60px时开始加载下一页的内容
            if (($(this)[0].scrollTop + $(this).height() + 60) > $(this)[0].scrollHeight) {
                clearTimeout(timers)
                // 延时执行来控制是否加载
                timers = setTimeout(function () {
                    page++
                    if (page < totalPage) {
                        var toast = $(document).dialog({
                            type: 'notice',
                            style: 'ios',
                            infoIcon: '../images/public/loading.gif',
                            infoText: '正在加载中'
                        })
                        newList(page)
                        setTimeout(function () {
                            toast.close()
                        }, 1000)
                    }else {
                        $(document).dialog({
                            type: 'notice',
                            style: 'ios',
                            infoText: '已加载完数据',
                            autoClose: 800
                        })
                        return false
                    }
                    // 调用执行上面的加载方法
                }, 50)
            }
        }
        beforeScrollTop = afterScrollTop;
    })
})


function newList (page) {
    $.ajax({
        type: 'post',
        url: '/app/getMyOrderList',
        data: {page: page},
        success: function (data) {
            if (data.code == 200) {
                // 数据渲染
                totalPage =1
                var myOrderHtml = template('myOrderTem', data)
                $('#myOrder').append(myOrderHtml)
                if(('#myOrder ul li').length==0){
                    $('#myOrder').html('<p style="text-align: center;padding: .2rem;font-size: .2rem">暂无订单</p>')
                }
            } else {

            }
        }, error: function (data) {
            $(document).dialog({
                type: 'notice',
                infoText: '网络异常，请稍后再试',
                autoClose: 1000,
                position: 'middle'
            })
        }
    })
    return totalPage
}



function logout() {
    /*localStorage.clear();
    sessionStorage.clear();
    var toast = $(document).dialog({type: 'notice', infoText: '已退出登录'});
    setTimeout(function () {
        toast.close();
        location.href = "app.do?index";
    }, 1000);*/
    $.ajax({
        url: '/app/logout',
        success: function (data) {
            location.href = "/app/index";
        }
    })
}