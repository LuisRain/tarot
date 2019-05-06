var tk = localStorage.getItem('tk') ? localStorage.getItem('tk') : ''


//加法
Number.prototype.add = function(arg){
    var r1,r2,m;
    try{r1=this.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2))
    return (this.mul(m) + arg.mul(m)) / m;
}

//减法
Number.prototype.sub = function (arg){
    return this.add(-arg);
}

//乘法
Number.prototype.mul = function (arg)
{
    var m=0,s1=this.toString(),s2=arg.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}

//除法
Number.prototype.div = function (arg){
    var t1=0,t2=0,r1,r2;
    try{t1=this.toString().split(".")[1].length}catch(e){}
    try{t2=arg.toString().split(".")[1].length}catch(e){}
    with(Math){
        r1=Number(this.toString().replace(".",""))
        r2=Number(arg.toString().replace(".",""))
        return (r1/r2)*pow(10,t2-t1);
    }
}

$(function () {
    function compute(){
        var sum = 0,
            all_num = 0
        $('.product li').each(function (i,v) {
            var price=$(v).children().find('.newPrice').text().split('￥')[1];
            var num=parseInt($(v).children().find('.num').val());
            console.log(price)
            sum = sum.add(parseFloat(price).mul(num) );
            all_num = all_num.add(num)
        })
        $('.sum').text('￥'+sum);
        $('.all_num').text(all_num+ '件')
    }


    // 默认0为未编辑状态
    var flag1 = true
    // // 初始化购物车信息
    getCartList()

    // 点击切换产品编辑状态
    $('.edit').click(function () {
        edit()
    })
    // 更改商品数量
    $('.add').click(function () {
        var type = 1
        var minNum ="{{$value.goodsAmount}}"
        editCartNum($(this), type)
    })
    $('.minus').click(function () {
        var type = 0
        editCartNum($(this), type)
    })

    // 点击全部选中
    $('.checkAll').find('input').change(function () {
        var checked = $(this).prop('checked')
        var lis = $(this).parent().parent().next().find('li')
        var sum = 0,
            all_num = 0
        // // 结算运费
        // var $freight = $(this).parent().parent().parent().find('.freight')
        // 结算金额
        var $sum = $(this).parent().parent().parent().find('.sum')
        // 结算数量
        var $all_num = $(this).parent().parent().parent().find('.all_num')
        if (checked) {
            $(this).parent().find('.shopCart_icon').addClass('check_icon')
            $(this).parent().parent().next().find('.check .shopCart_icon').addClass('check_icon')
            $(this).parent().parent().parent().find('.check').find("input[type='checkbox']").prop('checked', true)
            lis.each(function (i, v) {
                var price = $(v).find('.newPrice').text().split('￥')[1]
                var num = parseInt($(v).find('.num').text())
                if (!$(v).find("input[type='checkbox']").prop('disabled')) {
                    all_num += num
                    sum = sum + parseFloat(price) * num
                }
            })
            sum = returnFloat(sum)
            $freight.text('￥' + returnFloat($freight.prev().val()))
            $sum.text('￥' + sum)
            $all_num.text(all_num + '件')
        } else {
            $(this).parent().find('.shopCart_icon').removeClass('check_icon')
            $(this).parent().parent().parent().find('.check .shopCart_icon').removeClass('check_icon')
            $(this).parent().parent().parent().find('.check').find("input[type='checkbox']").prop('checked', false)
            $freight.text('￥0.00')
            $sum.text('￥0.00')
            $all_num.text('0件')
        }
    })
    // 点击选中某个产品
    $('.check').find('input').change(function () {
        $(this).parent().find('.shopCart_icon').toggleClass('check_icon')
        // 当前店铺结算金额
        var sum = +$(this).parent().parent().parent().next().find('.sum').text().split('￥')[1]
        // 当前店铺数量
        var all_num = parseInt($(this).parent().parent().parent().next().find('.all_num').text())
        // 当前产品单价
        var price = parseFloat($(this).parent().next().find('.newPrice').text().split('￥')[1])

        // 当前产品数量
        var num = +$(this).parent().next().find('.num').text()
        // // 当前产品总运费
        // var curExpFee = $(this).parent().parent().attr('data-expFee') * num
        // 当前产品总价
        var anglePrice = price * num
        // 当前店铺运费
        var delivery = returnFloat($(this).parent().parent().parent().next().find('.delivery').val())
        // console.log(delivery)
        // 全部选中为true
        var allCheck = true
        // 全部未选中为false
        var allUncheck = true
        // 获取商店所有产品选择框
        var e = $(this).parent().parent().parent().find(".check input[type='checkbox']")
        // 遍历商品查看查询状态
        e.each(function (i, v) {
            if (!$(v).prop('checked')) {
                // 有一个未选中则为false
                allCheck = false
            } else {
                // 有一个选中则为false
                allUncheck = false
            }
        })
        // 判断是否全部选中
        if (allCheck) {
            // 显示为全部选中状态
            $(this).parent().parent().parent().parent().find('.checkAll .shopCart_icon').addClass('check_icon')
            $(this).parent().parent().parent().parent().find('.checkAll').find("input[type='checkbox']").prop('checked', true)
        } else {
            $(this).parent().parent().parent().parent().find('.checkAll .shopCart_icon').removeClass('check_icon')
            $(this).parent().parent().parent().parent().find('.checkAll').find("input[type='checkbox']").prop('checked', false)
            if (allUncheck) {
                $(this).parent().parent().parent().next().find('.freight').text('￥0.00')
            } else {
                $(this).parent().parent().parent().next().find('.freight').text('￥' + delivery)
            }
        }
        if ($(this).prop('checked')) {
            sum += anglePrice
            all_num += num
        } else {
            sum -= anglePrice
            all_num -= num
        }
        sum = returnFloat(sum)
        $(this).parent().parent().parent().next().find('.sum').text('￥' + sum)
        $(this).parent().parent().parent().next().find('.all_num').text(all_num + '件')
    })
    // 删除购物车商品
    $('.del').click(function () {
        /*var cartId = 0,num = 0
        var cartNum = $('.cartNum').text()
        e = $(this)
        if (flag1) {
          cartId = $(this).parent().find('.cartId').val()
          num = $(this).prev().val()
        } else {
          cartId = $(this).parent().prev().find('.cartId').val()
          num = $(this).parent().prev().find('.num').text()
        }*/
        var goodsId = $(this).parent().prev().find('.goodsId').val()
        var that = this
        $(document).dialog({
            type: 'confirm',
            style: 'ios',
            titleShow: false,
            closeBtnShow: false,
            content: '是否确认删除',
            onClickConfirmBtn: function () {
                var toast = $(document).dialog({
                    type: 'notice',
                    infoIcon: '../public/images/loading.gif',
                    infoText: '正在删除'
                })
                setTimeout(function () {
                    toast.close()
                    delCartList(goodsId, that)
                }, 1000)
            }
        })
    })

    // 点击编辑 状态切换
    function edit () {
        if (flag1) {
            $('.delete').show()
            $('.operate').hide()
            $('.edit').val('取消')
            $('.clean').attr('disabled', true)
            $('.clean').css('background-color', '#ccc')
            flag1 = false
            // 当前编辑状态
        } else {
            $('.operate').show()
            $('.delete').hide()
            $('.edit').val('编辑')
            $('.clean').attr('disabled', false)
            $('.clean').css('background-color', '#f5445d')
            flag1 = true
            // 当前为标记状态
        }
    }

    // 获取购物车信息
    function getCartList () {
        $.ajax({
            url: '/app/getOrderList',
            type: 'get',
            async: false,

            success: function (data) {
                if (data.code == 200) {
                    if (!data.data) {
                        $('.edit').hide()
                        var html = template('emptyCart', data)
                        $('.main').html(html)
                        $('.cartNum').hide()
                        return
                    } else {
                        var html = template('shopProduct', data)
                        $('.main').html(html)
                    }
                    compute()

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
    }
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 验证类别是否是 酒类，饮料，奶类
     * @params:
     * @return:
     * @Date: 2019/3/19 13:47
     * @Modified By:
    */
    function checkType(productTypeId) {
        return (productTypeId == 609 || productTypeId == 8205 || productTypeId == 8109) ? true : false;
    }
    // 修改购物车数量
    function editCartNum (e, type) {
        // 获取当前操作商品数量
        var num = parseInt(e.parent().find('.num').val())
        var goodsId = e.parent().find('.goodsId').val()
        //当前产品最小起订量
        var minOrderNum = e.parent().find('.minOrderNum').val()
        //当前商品类别id
        var productTypeId = e.parent().find('.productTypeId').val()
        // 获取当前产品单价
        var price = parseFloat(e.parent().prev('.newPrice').text().split('￥')[1])
        // 获取总价钱
        var sum = +e.parent().parent().parent().parent().next().find('.sum').text().split('￥')[1]
        console.log(price,sum)
        // 获取总数量
        var all_num = parseInt(e.parent().parent().parent().parent().next().find('.all_num').text())
        var cartNum = parseInt(localStorage.getItem('cartNum'))
        // 判断加减类型
        if (type == 1) {
            num += Number(minOrderNum);
            all_num += Number(minOrderNum);
            cartNum += Number(minOrderNum);
            sum += price * minOrderNum;
            /*if (checkType(productTypeId)) {
            } else {
                num++;
                all_num++
                cartNum++
                sum += price
            }*/
        } else if (type == 0) {
            if (num <= minOrderNum ) {
                $(document).dialog({
                    type: 'notice',
                    style: 'ios',
                    infoText: '商品最小起订量为：' + minOrderNum,
                    autoClose: 800
                })
                e.parent().find('.num').val(minOrderNum)
                return false
            }
            num -= Number(minOrderNum);
            all_num -= Number(minOrderNum);
            cartNum -= Number(minOrderNum);
            sum -= price * minOrderNum;
            /*if (checkType(productTypeId)) {
            } else {
                if (num == 1 ) {
                    $(document).dialog({
                        type: 'notice',
                        style: 'ios',
                        infoText: '商品数量不能为0',
                        autoClose: 800
                    })
                    return false
                }
                num--;
                all_num--;
                cartNum--;
                sum -= price;
            }*/

        }
        sum = returnFloat(sum)
        e.parent().find('.num').val(num)

        compute()
        $.ajax({
            type: 'get',
            url: '/app/modifyCard?orderGoodsId='+goodsId+'&num='+num,
            success: function (data) {
                // console.log(data)
                if (data.code == 200) {
                    // 修改成功后改变单个产品数量
                    localStorage.setItem('cartNum', cartNum)
                    $('.cartNum').text(cartNum)
                    e.parent().find('.num').text(num)

                    compute()
                } else {
                    $(document).dialog({
                        type: 'notice',
                        style: 'ios',
                        infoText: data.msg,
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

    // 删除购物车产品
    function delCartList (goodsId, e) {
        $.ajax({
            type: 'get',
            url: '/app/deleteOrderGoods?orderGoodsId='+goodsId,

            success: function (data) {
                if (data.code == 200) {
                    $(document).dialog({
                        type: 'notice',
                        style: 'ios',
                        infoText: '删除成功',
                        autoClose: 800
                    })
                    $(e).parent().parent().remove()//移除当前行
                    compute()
                    if ($('.main').find('li').length == 0) {
                        var html = template('emptyCart', data.data)
                        $('.main').html(html)
                        $('.edit').hide()

                        return false
                    }

                    /*// 未编辑状态下
                    if (flag1) {
                      if (e.parent().parent().parent().parent().find('li').length == 1) {
                        e.parent().parent().parent().parent().parent().remove()
                      } else {
                        e.parent().parent().remove()
                      }
                    } else {
                      // 编辑状态下
                      if (e.parent().parent().parent().find('li').length == 1) {
                        e.parent().parent().remove()
                      } else {
                        e.parent().parent().remove()
                      }
                    }
                    localStorage.setItem('cartNum', cartNum)
                    $('.cartNum').text(cartNum)*/
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
    }


    $(".inp").change(function (e) {
        var val = e.delegateTarget.value
        //当前产品最小起订量
        var minOrderNum = $(this).parent().find('.minOrderNum').val()
        //当前商品类别id
        var productTypeId = $(this).parent().find('.productTypeId').val()
        if(isNaN(val)||val<=0||!(/^\d+$/.test(val))) {
            $(document).dialog({
                type: 'notice',
                style: 'ios',
                infoText: '商品数量只能为整数',
                autoClose: 800
            })
            $(this).val(minOrderNum)
            return false
        }
        if (val == 0) {
            $(document).dialog({
                type: 'notice',
                style: 'ios',
                infoText: '商品数量不能为0',
                autoClose: 800
            })
            $(this).val(minOrderNum)
            return false
        }

        if (checkType(productTypeId)) {
            if (val % minOrderNum != 0) {
                $(document).dialog({
                    type: 'notice',
                    style: 'ios',
                    infoText: '该商品仅能整箱订购',
                    autoClose: 800
                })
                $(this).val(minOrderNum*(Math.ceil(val/minOrderNum)))
                return false
            }
        }
            if (Number(val) < Number(minOrderNum) ) {
                $(document).dialog({
                    type: 'notice',
                    style: 'ios',
                    infoText: '商品不能低于最小起订量',
                    autoClose: 800
                })
                $(this).val(minOrderNum)
                return false
            }
        var id = $($(this).siblings().get(1)).val()
        console.log(id)
        $.ajax({
            type: 'get',
            url: '/app/inputModifyCard?orderGoodsId='+id+'&num='+val,
            success: function (data) {
                // console.log(data)
                if (data.code == 200) {
                    compute()
                } else {
                    $(document).dialog({
                        type: 'notice',
                        style: 'ios',
                        infoText: data.msg,
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

})

// 保留两位小数点
function returnFloat (value) {
    var value = Math.round(parseFloat(value) * 100) / 100
    var xsd = value.toString().split('.')
    if (xsd.length == 1) {
        value = value.toString() + '.00'
        return value
    }
    if (xsd.length > 1) {
        if (xsd[1].length < 2) {
            value = value.toString() + '0'
        }
        return value
    }
}

function submit() {

    var week = new Date().getDay();
    if (!(week == 3 || week == 4)) {
        $(document).dialog({
            type: 'notice',
            style: 'ios',
            infoText: '请在规定时间内下单...',
            autoClose: 800
        })
        return
    }
    var num = $('.all_num').text()

    if (parseInt(num) > 0) {
        var orderId = $('.cartId').eq(0).val()
        console.log(orderId)
        $.ajax(
            {
                url: '/app/submit?orderId='+orderId,
                success: function (data) {
                    if (data.code == 200) {
                        window.location.href = '/app/personal'
                        return false
                    }
                }
            }
        )
    }else {
        $(document).dialog({
            type: 'notice',
            style: 'ios',
            infoText: '请添加商品...',
            autoClose: 800
        })

        return false
    }
}

function openDetails(id) {
    location.href = '/app/toCommodityDetails?id=' + id ;
}