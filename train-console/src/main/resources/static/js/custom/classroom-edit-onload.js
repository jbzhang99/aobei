$(document).ready(function() {
    //课程表的回显
	var course_times = $("#aabb").attr("data-courses");
	alert(course_times)
	var a = [];
	a = course_times;
	$.each(course_times,function(i,ele){
	   $("input[name='check_class_schedule[]']").each(function(i){
		   if(ele == $(this).val()){
			   $(this).prop("checked",true);
		   }
	   });
	});
    
    //支持课程的回显
    var courseTeam_list = $("#aabb").attr("data-courseTeam");
    $.each(courseTeam_list,function(i,ele){
    	$("option").each(function(i){
    		if(ele == $(this).text()){
    			$(this).prop("selected",true);
    		}
    	});
    });
    
    $("#back_butn").click(function(){
    	var page_current_page = $("#edit_current_page").val();
		window.location.href = "/classroommanager/goto_classroom_list?p="+page_current_page;
	});
    
    //表单校验
    form_validator();
   
    $("#save_edit_btn").click(function(){
    	$("#form1").bootstrapValidator('validate');//提交验证
 		if ($("#form1").data('bootstrapValidator').isValid()) {
 			//序列化获得表单数据，结果为：user_id=12&user_name=John&user_age=20
	    	var data=$("#form1").serialize();
	    	//submitData是解码后的表单数据，结果同上
	    	var submitData=decodeURIComponent(data,true);
	    	$.ajax({
	    	    url:'/classroommanager/edit_submit_classroom',
	    	    data:submitData,
	    	    cache:false,//false是不缓存，true为缓存
	    	    async:true,//true为异步，false为同步
	    	    dataType:'json',
	    	    success:function(data){
	    	        //请求成功时
	    	    	var page_current_page = $("#edit_current_page").val();
					reset();
					alertify.success(data.msg);
					setTimeout(function (){
						window.location.href = "/classroommanager/goto_classroom_list?p="+page_current_page;
					}, 1500);
	    	    }
	    	});
	    	return false;
 		}
	});
    
    $("#confirm_btn").click(function(){
    	var count = $("input[type='checkbox']:checked").length;
    	if(count == 0){
    		reset();
    		alertify.error("您还未勾选可用的课程时间！");
    	}else{
    		$("#class_schedule").modal('hide');
    	}
	});
});