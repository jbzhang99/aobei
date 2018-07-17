/**
 * Created by adminL on 2018/5/15.
 */
$(function(){
    $('#form1').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
           /* valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'*/
        },
        fields: {
            name:{
                validators: {
                    notEmpty: {
                        message: '请填写优惠券名称'
                    }
                }
            },
            plan_money:{
                validators: {
                    digits:{
                        message:'预算金额不能为负数且不能小于0'
                    }
                }
            },
            type:{
                validators: {
                    notEmpty:{
                        message:'请选择优惠券类型'
                    }
                }
            },
            priority:{
                validators: {
                    regexp:{
                        regexp: /^([1-9]\d?|99)$/,
                        message:'优先等级为1-99'
                    }
                }
            },
            num_total:{
                validators: {
                    digits:{
                        message:'请填写数字'
                    }
                }
            },
            start_datetime:{
                validators: {
                    notEmpty: {
                        message: '请填写优惠券有效开始时间'
                    }
                }
            },
            end_datetime:{
                validators: {
                    notEmpty: {
                        message: '请填写优惠券失效结束时间'
                    }
                }
            },
        }
    });

    ymdhm();

    $("#div_status").hide();

    //发放数量的单选按钮
    var limit_name = document.getElementsByName("num_limit");
    $("#num_limit_2").change(function(){
        $.each(limit_name,function(index,ele){
            if(ele.value==0){
                $("#num_total_div").hide()
                reset();
                alertify.log("发放数量没有限制，此优惠券没有总数量！")
            }
        })
    })
    $("#num_limit_1").change(function(){
        $.each(limit_name,function(index,ele){
            if(ele.value==1){
                $("#num_total_div").show()
                reset();
                alertify.log("发放数量有限制，请设置优惠券总数量！")
            }
        })
    })
    var current = $("#page_current").val();
    //取消按钮
    $("#cancel_btn").click(function(){
        //var current = [[${current}]]
        window.location.href="/couponmanager/coupon_list?p="+current
    })
    //默认页面加载是隐藏的
    $("#contition_1").hide();
    $("#contition_2").hide();
    $("#condition_4").hide();
    //隐藏优惠方案隐藏
    $("#programme_1").hide();
    $("#programme_2").hide();
    //$("#programme_3").hide();
    //页面默认隐藏优惠条件列表
    $("#template_hide").hide()
    $("#template_show").show();
    $("#template_content_switch").show()
    //页面默认隐藏优惠方案列表
    $("#programme_hide").hide()
    $("#programme_show").show();
    $("#programme_content_switch").show()
    var checked_template = $("input:radio[name='condition_type']:checked").val();
    if(checked_template==2){
        $("#contition_2").show();
    }else{
        $("#contition_2").hide();
    }
    var checked_template_4 = $("input:radio[name='condition_type']:checked").val();
    if(checked_template_4==4){
        $("#condition_4").show();
    }else{
        $("#condition_4").hide();
    }

    //var condition_type = document.getElementsByName("condition_type");
    //优惠条件列表选择时的显示和隐藏
    $("input:radio[name='condition_type']").change(function(){
        var checked_template = $("input:radio[name='condition_type']:checked").val();
        //页面默认模板隐藏
        if(checked_template==1){
            $("#contition_1").show();
            reset();
            alertify.log("请输入订单金额！")
        }else{
            $("#contition_1").hide();
        }
        if(checked_template==2){
            $("#contition_2").show();
            reset();
            alertify.log("请输入订单金额及指定商品信息！")
        }else{
            $("#contition_2").hide();
        }
        if(checked_template==4){
            $("#condition_4").show();
            reset();
            alertify.log("请选择指定商品信息！")
        }else{
            $("#condition_4").hide();
        }
        if(checked_template==3){
            $("#programme_model_2").hide();
            $("#template_programme_Ddiscount").hide();
            $("#programme_2").hide();
        }else{
            $("#programme_model_2").show();
            $("#template_programme_Ddiscount").show();
            $("#programme_2").show();
        }
    })
    //优惠方案选择
    $("input:radio[name='programme_type']").change(function(){
        var checked_template = $("input:radio[name='programme_type']:checked").val();
        //页面默认模板隐藏
        if(checked_template==1){
            $("#programme_1").show();
            reset();
            alertify.log("请输入订单折扣！")
        }else{
            $("#programme_1").hide();
        }
        if(checked_template==2){
            $("#programme_2").show();
            reset();
            alertify.log("请输入优惠价格！")
        }else{
            $("#programme_2").hide();
        }
        /* if(checked_template==3){
         $("#programme_3").show();
         reset();
         alertify.log("请输入订单金额及指定商品信息！")
         }else{
         $("#programme_3").hide();
         } */
    })
    //加载指定商品的两个下拉框数据方法
    load_select();
    load_select_4();
    //提交按钮
    $("#add_btn").click(function(){

        $("#form1").bootstrapValidator('validate');//提交验证
        /* if ($("#form1").data('bootstrapValidator').isValid()) {
         return false;
         }  */
        var product_ids = [];
        //传递商品id信息
        $("#p_opration p").find("span").each(function(i,v){
            product_ids.push($(v).attr('value'));
        })
        var strJson_productIds = JSON.stringify(product_ids);
        $("#data_hidden_ids").val(strJson_productIds);

        var product_ids_4 = [];
        //传递商品id信息
        $("#p_opration_4 p").find("span").each(function(i,v){
            product_ids_4.push($(v).attr('value'));
        })
        var strJson_productIds = JSON.stringify(product_ids_4);
        $("#data_hidden_ids_4").val(strJson_productIds);



        var input = $("input:radio[name='condition_type']:checked");
        //if(input)

        var limit_name = document.getElementsByName("num_limit");
        var checked_num = $("input:radio[name='num_limit']:checked").val()
        //如果选择有发放数量限制，必须填写总数量
        if(checked_num == 1){
            if($("#num_total").val()==''){
                reset();
                alertify.alert("请输入总数量！不需要总数量时可选择否。 ")
                return false;
            }
        }

        var lose_con1 = $("#template_condition_all").get(0).checked;
        var lose_con2 = $("#template_condition_pro").get(0).checked;

        if(lose_con1==false){
            $("#template_condition_all").nextAll('div').eq(0).find('input').eq(3).val('');
        }
        if(lose_con2==false){
            $("#template_condition_pro").nextAll('div').eq(0).find('input').eq(0).val('');
        }
        var lose_pro1 = $("#template_programme_discount").get(0).checked;
        var lose_pro2 = $("#template_programme_Ddiscount").get(0).checked;
        if(lose_pro1==false){
            $("#template_programme_discount").nextAll('div').eq(0).find('input').eq(0).val('');
        }
        if(lose_pro2==false){
            $("#template_programme_Ddiscount").nextAll('div').eq(0).find('input').eq(0).val('');
        }

        var regex = /^([1-9]\d?|100)$/;
        var regex_price = /^[0-9]+(.[0-9]{1,2})?$/;
        var input_con1= $("#condition_all").val();
        var input_con2= $("#condition_pro").val();
        var input_pro1 = $("#programme_discount").val();
        var input_pro2 = $("#programme_Ddiscount").val();

        if($("#template_condition_all").get(0).checked){
            if(input_con1==''){
                reset();
                alertify.alert("订单金额不能为空！");
                return false;
            }
            if(!regex_price.test(input_con1)){
                reset();
                alertify.alert("优惠条件订单金额只能包含整数或者是小数点后最多保留两位！");
                return false;
            }
        }
        if($("#template_condition_pro").get(0).checked){
            if(input_con2==''){
                reset();
                alertify.alert("订单金额不能为空！");
                return false;
            }
            if(!regex_price.test(input_con2)){
                reset();
                alertify.alert("优惠条件订单金额只能包含整数或者是小数点后最多保留两位！");
                return false;
            }
            var p_size = $("#template_condition_pro").nextAll('div').eq(0).find('div').eq(0).find('div').find('p').find('button').length

            if(p_size=='' || p_size==0){
                reset();
                alertify.alert("优惠条件未选择商品！");
                return false;
            }
        }
        if($("#template_programme_discount").get(0).checked){
            if(input_pro1==''){
                reset();
                alertify.alert("订单折扣不能为空！");
                return false;
            }
            if(!regex.test(input_pro1)){
                reset();
                alertify.alert("优惠方案折扣只能输入1-100的整数");
                return false;
            }
        }
        if($("#template_programme_Ddiscount").get(0).checked){
            if(input_pro2==''){
                reset();
                alertify.alert("订单金额不能为空！");
                return false;
            }
            if(!regex_price.test(input_pro2)){
                reset();
                alertify.alert("优惠方案订单优惠只能包含整数或者是小数点后最多保留两位！");
                return false;
            }
        }
        if($("#template_programme_register").get(0).checked){
            var p_size = $("#template_programme_register").nextAll('div').eq(0).find('div').eq(0).find('p').find('button').length

            if(p_size=='' || p_size==0){
                reset();
                alertify.alert("优惠条件未选择商品！");
                return false;
            }
        }
        if($("#type").val()==4){
            var os = $("#num_total").val();
            var rs = $("input[name='num_limit']:checked").val();
            if(rs==0){
                reset();
                alertify.alert("当类型为兑换券时，必须添加数量");
                return false;
            }
            if(os==''){
                reset();
                alertify.alert("当类型为兑换券时，必须添加数量!");
                return false;
            }
        }
        if($("#type").val()==""){
            reset();
            alertify.alert("请选择优惠券类型！")
            return false;
        }
        //判断表单填入数据，生效时间不能大于失效时间
        var start_time = $("#use_start_datetime").val();
        var end_time = $("#use_end_datetime").val();
        if(start_time=='' ){
            reset();
            alertify.alert("请选择优惠券生效时间！")
            return ;
        }else if(end_time==''){
            reset();
            alertify.alert("请选择优惠券失效时间！")
            return ;
        }else if(start_time>=end_time){
            reset();
            alertify.alert("优惠券生效时间不能在失效时间之后或相等！")
            return ;
        }else if($("input:radio[name='condition_type']:checked").size()==0){
            //打开优惠条件列表
            $("#template_hide").show()
            $("#template_show").hide();
            $("#template_content_switch").show();
            //$("#form1").data("bootstrapValidator").resetForm();
            reset();
            alertify.alert("请至少选择一项优惠条件！");
            return ;
        }else if($("input:radio[name='programme_type']:checked").size()==0){
            reset();
            alertify.alert("请至少选择一项优惠方案！")
            //打开优惠方案列表
            $("#programme_hide").show()
            $("#programme_show").hide();
            $("#programme_content_switch").show();
            //重置表单所有验证
            $("#form1").data("bootstrapValidator").resetForm();
            return ;
        }else{
            $("#form1").data("bootstrapValidator").resetForm();

            //序列化表单数据
            //执行添加请求
            $.ajax({
                url:'/couponmanager/coupon_add',
                data:$("#form1").serializeArray(),
                method:'POST',
                cache:false,
                async:true,
                dataType:'json',
                success:function(data){
                    reset();
                    alertify.success(data.message);
                    setTimeout(function(){
                        window.location.href="/couponmanager/coupon_list?p="+current
                    },2000)
                }
            })
        }
    })

    //指定商品时的添加按钮
    $("#select_add").click(function(){
        $('#select_all').show();
        $("#select_close").show();
    })
    $("#select_close").click(function(){
        $('#select_all').hide();
        $("#select_close").hide();
    })

    //4类型 指定商品
    $("#select_add_4").click(function(){
        $('#select_all_4').show();
        $("#select_close_4").show();
    })
    $("#select_close_4").click(function(){
        $('#select_all_4').hide();
        $("#select_close_4").hide();
    })
    //添加商品
    var num=0
    $("#add_btn_one").click(function(){
        var checked_se2 = $("#select_2 option:checked").val()
        var checked_se2_text = $("#select_2 option:checked").text();
        if(checked_se2 !=null){
            if($("#p_opration").find("span").size() == 0){
                //获取到已选中的商品
                var html_pro = "<p><span id=product"+num+" value="+checked_se2+">"+checked_se2_text+"</span><button id=del_pro_"+num+" type='button' class='btn btn-link text-warning form-group' style='margin-left:40px' onclick='del_pro(this)' value='product"+num+"'><i class='fas fa-minus-circle fa-lg text-danger'></i></button></p>"
                $("#p_opration").append(html_pro);
                num++
                //product_ids.push(checked_se2)
            }else{
                var flag = true;
                $.each($("#p_opration").find("span"),function(index,ele){
                    if(checked_se2 == $(this).attr("value")){
                        reset();
                        alertify.alert("不能重复添加！")
                        flag=false
                        return ;
                    }
                })
                if(flag==true){
                    //获取到已选中的商品
                    var html_pro = "<p><span id=product"+num+" value="+checked_se2+" class='form-inline'>"+checked_se2_text+"</span><button id=del_pro_"+num+" type='button' class='btn btn-link text-warning form-group' style='margin-left:40px' onclick='del_pro(this)' value='product"+num+"'><i class='fas fa-minus-circle fa-lg text-danger'></i></button></p>"
                    $("#p_opration").append(html_pro);
                    num++
                    //product_ids.push(checked_se2)
                }
                /* var strJson_productIds = JSON.stringify(product_ids);
                 $("#data_hidden_ids").val(strJson_productIds); */
            }
        }else{
            reset();
            alertify.alert("请选择分类下存在的商品，请重新选择分类！");
            return;
        }

    })

    //4  添加商品
    var num=0
    $("#add_btn_one_4").click(function(){
        var checked_se2 = $("#select_2_4 option:checked").val()
        var checked_se2_text = $("#select_2_4 option:checked").text();
        if(checked_se2 !=null){
            if($("#p_opration_4").find("span").size() == 0){
                //获取到已选中的商品
                var html_pro = "<p><span id=product"+num+" value="+checked_se2+">"+checked_se2_text+"</span><button id=del_pro_"+num+" type='button' class='btn btn-link text-warning form-group' style='margin-left:40px' onclick='del_pro(this)' value='product"+num+"'><i class='fas fa-minus-circle fa-lg text-danger'></i></button></p>"
                $("#p_opration_4").append(html_pro);
                num++
                //product_ids.push(checked_se2)
            }else{
                var flag = true;
                $.each($("#p_opration_4").find("span"),function(index,ele){
                    if(checked_se2 == $(this).attr("value")){
                        reset();
                        alertify.alert("不能重复添加！")
                        flag=false
                        return ;
                    }
                })
                if(flag==true){
                    //获取到已选中的商品
                    var html_pro = "<p><span id=product"+num+" value="+checked_se2+" class='form-inline'>"+checked_se2_text+"</span><button id=del_pro_"+num+" type='button' class='btn btn-link text-warning form-group' style='margin-left:40px' onclick='del_pro(this)' value='product"+num+"'><i class='fas fa-minus-circle fa-lg text-danger'></i></button></p>"
                    $("#p_opration_4").append(html_pro);
                    num++
                    //product_ids.push(checked_se2)
                }
                /* var strJson_productIds = JSON.stringify(product_ids);
                 $("#data_hidden_ids").val(strJson_productIds); */
            }
        }else{
            reset();
            alertify.alert("请选择分类下存在的商品，请重新选择分类！");
            return;
        }

    })


})
function change_cou_type(v) {
    var type = v.value;
    if(3==type){
        $("#template_programme_order").hide();
        $("input[name='condition_model_3']").hide();
    }else{
        $("#template_programme_order").show();
        $("input[name='condition_model_3']").show();
    }

    if(2==type){
        $("#div_status").show();
    }else{
        $("#div_status").hide();
    }
}