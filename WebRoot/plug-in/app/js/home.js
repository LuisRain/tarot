
var page = 0,totalPage = 1,timers = null;
var curid = 1
$(function () {

    //先获取分类数据
    $.ajax({
        url: '/app/getCategory',
        success: function (data) {
            if (data.code == 200) {

                var classHtml = template('classTemp', data)

                $('#class').html(classHtml)
                $($('#category li')[0]).addClass('active')
                curid =$('.active').attr('id');
                newList(page,curid)
                // 点击分类样式变化右侧内容改变
                $('.variety_left li').click(function () {
                    $('.variety_left li').removeClass('active');
                    $(this).addClass('active');
                    curid=$(this).attr('id');//获取分类ID
                    $('#commodity').empty();
                    newList(page,curid)//根据id得到分类下的内容
                });
            }
        }
    })




    //----
    //newList (page)

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
                        newList(page,curid)
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


function newList (page, curid) {
    var search=$('#search>input').val().trim();
    $.ajax({
        type: 'get',
        url: '/app/goods',
        data: {page: page,
        pageSize: 10,
        categoryId: curid,
            search:search},
        success: function (data) {
            if (data.code == 200) {
                // 数据渲染
                totalPage = data.data.totalPage
                var data = data.data
                var commodityHtml = template('commodityTemp', data)
                $('#commodity').append(commodityHtml)
            } else {
                $(document).dialog({
                    type: 'notice',
                    infoText: '未获取到数据',
                    autoClose: 1000,
                    position: 'middle'
                })
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

// 点击购物车
var count=1;
function addShop(id) {
    console.log(id)
    $.ajax({
        type: 'get',
        url: '/app/addCart?id='+id,
        success: function (data) {
            if (data.code == 200) {
                var sum=count++;
                $('.cartNum').show();
                $('.cartNum').text(sum)
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
  /* var sum=count++;
       $('.cartNum').show();
       $('.cartNum').text(sum)*/
}
// 点击搜索
function  search() {
   var search=$('#search>input').val().trim();

   $('#category li').each(function () {
       var value = $(this).text();
       console.log(value)
       if (value.indexOf(search)>-1) {
           console.log('aaa   ' + value)
           $('.variety_left li').removeClass('active');
           $(this).addClass('active');
           curid=$(this).attr('id');//获取分类ID
           $('#commodity').empty();
           newList(page,curid)//根据id得到分类下的内容
           return false
       }
   })
        $.ajax({
            type: 'get',
            url: '/app/search',
            data: {
                page: page,
                pageSize: 10,
                search: search
            },
            success: function (data) {
                // console.log(totalPage)
                if(data.code == 200) {
                    var commodityHtml = template('commodityTemp', data.data)
                    $('#commodity').html(commodityHtml)
                }else{
                    $('#commodity').html('<p style="text-align: center;font-size: .32rem;padding: .5rem;">暂无数据</p>')
                }
            }
        })

}
function openDetails(id) {
    location.href = '/app/toCommodityDetails?id=' + id ;
}