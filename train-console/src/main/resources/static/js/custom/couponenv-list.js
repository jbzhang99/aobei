/**
 * Created by adminL on 2018/5/15.
 */
function couponEnv_edit(couponEnv_id){
    var current = $("#page_current").val()
    window.location.href="/couponEnv/goto_edit_couponEnv?p="+current+"&couponEnv_id="+couponEnv_id
}
function couponEnv_on(id) {
    var mts = $("#mts").val();
    var current = $("#page_current").val()
    reset();
    alertify.confirm("确定要生效此条策略吗",function (e) {
        if(e){
            $.post('/couponEnv/change_status',{couponEnv_id:id,mts:mts},function (data) {
                var mgs = data.message;
                if(mgs.indexOf("change_status")!=-1){
                    reset();
                    alertify.alert("生效失败，注册派券类型已存在！")
                    return false;
                }
                reset();
                alertify.success(data.message);
                setTimeout(function () {
                    window.location.href="/couponEnv/couponEnv_list?p="+current
                },1500)
            })
        }else{
            reset();
            alertify.log("已取消操作！")
        }
    })

}
function couponEnv_off(id) {
    var mts = $("#mts").val();
    var current = $("#page_current").val()
    reset();
    alertify.confirm("确定要失效此条策略吗",function (e) {
        if(e){
            $.post('/couponEnv/change_status',{couponEnv_id:id,mts:mts},function (data) {
                reset();
                alertify.success(data.message);
                setTimeout(function () {
                    window.location.href="/couponEnv/couponEnv_list?p="+current
                },1500)

            })
        }else{
            reset();
            alertify.log("已取消操作！")
        }
    })

}

/* couponEnv-add   函数  */
function formatDate(date){
    var date = new Date(date);
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = date.getDate() + ' ';
    var s = Y+M+D
    return s;
}