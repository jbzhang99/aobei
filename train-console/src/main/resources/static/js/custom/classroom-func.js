//异步加载课程表数据并于模态框中展示
function class_schedule_show(flag,classroom_id){
	$(".tdCell").each(function(i){
		$(this).removeClass('tdCellActive');
	});
	if(flag == "1"){
		$.ajax({
		   type: "POST",
		   url: "/classroommanager/get_class_schedule",
		   data: "classroom_id="+classroom_id,
		   success: function(data){
			   $.each(data,function(i,ele){
				   $("td[data-wd='"+ele.w+"']").each(function(i){
					   var td = $(this);
					   var start = td.data("s");
					   var end = td.data("e");
					   var wd = td.data("wd");
					   $.each(ele.se,function(i,se){
						   if(se.s == start && se.e == end && ele.w == wd){
							   td.data("active",1);
							   td.addClass('tdCellActive');
						   }
					   });
				   });
			   });
	 		   $("#class_schedule").modal('show');
		   }
		});
	}
}

//请求去往教室信息编辑页面
function classroom_edit(classroom_id,pageNo){
	var page_current_page = pageNo;
	window.location.href = "/classroommanager/classroom_edit?classroom_id="+classroom_id+"&page_current_page="+page_current_page;
}

//异步删除教室信息
function classroom_del(classroom_id,pageNo){
	var page_current_page = pageNo;
	reset();
	alertify.confirm("确定要删除该条记录吗？", function (e) {
	    if (e) {
	    	$.ajax({
				url:"/classroommanager/classroom_delete/"+classroom_id,
				type:'GET',
				success:function(data){
					reset();
					alertify.success(data.msg);
					setTimeout(function (){
						window.location.href = "/classroommanager/goto_classroom_list?p="+page_current_page;
					}, 1500);
				}
			});
	    } else {
	    	reset();
			alertify.log("已取消操作。");
	    }
	});
}

//显示课程表的函数
function schedule_show(){
	$("#class_schedule").modal('show');
}

//隐藏课程表（其实是将课程表勾选的信息全部去掉）
function schedule_hide(){
	$('#class_schedule').find("[type='checkbox']").prop('checked',false);
}

//表单校验函数
function form_validator(){
	$('#form1').bootstrapValidator({
		message: '这个值是无效的！',
		live: 'enabled',
		feedbackIcons: {
//         valid: 'glyphicon glyphicon-ok',
//         invalid: 'glyphicon glyphicon-remove',
//         validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			school_id: {
				validators: {
					notEmpty: {
						message: '请选择学校名称！'
					}
				}
			},
			block_number: {
				validators: {
					notEmpty: {
						message: '请填写教学楼号！'
					},
                    stringLength: {
                        max: 10,
                        message: '教学楼号最长不得多于10个字符'
                    } 
				}
			},
			'support_course_ids': {
				validators: {
					notEmpty: {
						message: '请选择支持的课程！'
					}
				}
			},
			capacity: {
				validators: {
					notEmpty: {
						message: '请填写教室可容内人数！'
					},
                    regexp: {
                        regexp: /^[2-9]\d$/,
                        message: '教室可容纳人数应在20到99之间！'
                    }
				}
			},
			useable: {
				validators: {
					notEmpty: {
						message: '请选择是否可用！'
					}
				}
			}
		}
	});
}


