function end_range(){
	var begin_date = $("#begin").val();
	if(begin_date == ''){
		reset();
		alertify.alert("请选择起始查询时间！");
		return;
	}
	query_by_condition();
}

function begin_range(){
	var end_date = $("#end").val();
	if(end_date == ''){
		reset();
		alertify.alert("请选择结束查询时间！");
		return;
	}
	query_by_condition();
}

//按条件筛选查询条件
function query_by_condition(){
	var begin_date = $("#begin").val();
	var end_date = $("#end").val();
	var school_id = $("#school").val();
	var teacher_id = $("#teacher").val();
	var partner_id = $("#partner").val();
	var classroom_id = $("#classroom_select").val();

	window.location.href = "/planmanager/goto_plan_list?begin_date="+begin_date+
														"&end_date="+end_date+
														"&school_id="+school_id+
														"&teacher_id="+teacher_id+
														"&partner_id="+partner_id+
														"&classroom_id="+classroom_id;
}

//日期格式化
function FormatDate (strTime) {
    var date = new Date(strTime);
    month = date.getMonth()+1;
    day = date.getDate();
    if((date.getMonth()+1) < 10){
    	month = "0" + (date.getMonth()+1);
    }
    if((date.getDate() < 10)){
    	day = "0" + date.getDate();
    }
    return date.getFullYear()+"-"+month+"-"+day;
}

function plan_edit(train_plan_id,pageNo){
	var page_current_page = pageNo;
	window.location.href = "/planmanager/plan_edit?train_plan_id="+train_plan_id+"&page_current_page="+page_current_page;
}

function plan_del(train_plan_id,pageNo){
	var page_current_page = pageNo;
	reset();
	alertify.confirm("确定删除该条培训计划信息吗？", function (e) {
	    if (e) {
	    	$.ajax({
				url:"/planmanager/plan_delete/"+train_plan_id,
				type:'GET',
				success:function(data){
					reset();
					alertify.success(data.msg);
					setTimeout(function (){
						window.location.href = "/planmanager/goto_plan_list?p="+page_current_page;
					}, 1500);
				}
			});
	    } else {
	    	reset();
			alertify.log("已取消操作。");
	    }
	});
}

//编辑页面改变课程，教室重新加载
function change_team(course_id){
//	var school_id = $("#school_sel").val();
//	select_school(school_id,'');
//	reset();
//	alertify.alert("培训课程已更改，请重新选择教室");
	
	$("#end").val('');
	var train_begin = $("#begin").val();
	if(train_begin == ''){
		reset();
		alertify.log("还未选择培训开始日期！");
	}else{
		if(course_id != ''){
			var	param = {'course_id':course_id};
			$.post({
				type : 'post',
				data : param,
				url  : '/planmanager/get_optional_school_classroom',
				dataType:'json',
				success : function(result){
					var html_s = [];
					html_s.push("<option value=''>培训学校</option>");
					$.each(result.schools,function(i,ele){
						html_s.push("<option value="+ele.school_id+">"+ele.name+"</option>");
					});
					$("#school_sel").html(html_s.join(''));
					
					var html_c = [];
					html_c.push("<option value=''>教室</option>");
					$.each(result.classrooms,function(i,ele){
						html_c.push("<option value="+ele.classroom_id+">"+ele.block_number+"</option>");
					});
					$("#classroom_select").html(html_c.join(''));
					
					var html_t = [];
					html_t.push("<option value=''>培训教师</option>");
					$.each(result.teachers,function(i,ele){
						html_t.push("<option value="+ele.teacher_id+">"+ele.name+"</option>");
					});
					$("#teacher_sel").html(html_t.join(''));
					
					var html_a = [];
					html_a.push("<option value=''>培训助教</option>");
					$.each(result.assistants,function(i,ele){
						html_a.push("<option value="+ele.teacher_id+">"+ele.name+"</option>");
					});
					$("#assistant_sel").html(html_a.join(''));
				}
			});
		}
	}
}

//选完学校异步获取教室信息，为教室下拉选框添加值
function select_school(school_id,classroom_id){
	var course_id = $("#select_course").val();
	if(course_id == ''){
		reset();
		alertify.alert("还未选择课程信息");
		$("#add_sel_school").val('');
		return;
	}else{
	    var	param = {'school_id':school_id,'course_id':course_id};
		$.post({
			type : 'post',
			data : param,
			url  : '/planmanager/get_optional_classroom',
			dataType:'json',
			success : function(result){
				var html = [];
				html.push("<option value=''>教室</option>");
				$.each(result,function(i,ele){
					if((classroom_id != '') && (ele.classroom_id == classroom_id)){
	                   html.push("<option value="+ele.classroom_id+" selected>"+ele.block_number+"</option>");
	                }else{
	                   html.push("<option value="+ele.classroom_id+">"+ele.block_number+"</option>");
	                }
				});
				$("#classroom_select").html(html.join(''));
			}
		});
	}
}

//改变合伙人或者培训时间，对应的培训计划的学员列表更新，需要重新选学员
function change_time_or_partner(flag){
	if(flag == 1){//修改时间
		var original_begin = $("#original_begin").val();
		var original_end = $("#original_end").val();
		var train_begin = $("#begin").val();
		var train_end = $("#end").val();
		if(original_begin != train_begin){
			//1.修改了时间，则需要用新的时间和教室信息来进行排课
			var course_id = $("#select_course").val();
			var classroom_id = $("#classroom_select").val();
			if(classroom_id == ''){
				reset();
				alertify.alert("教室信息已变更，请先选择教室信息！");
			}else{
				get_train_time(train_begin,course_id,classroom_id);
				//3.清空编辑页面回显的已有的学员名单
				$("#stu_list").html('');
				reset();
				alertify.log("培训时间已变更，请重新选择要参加培训的学员名单！");
			}
		}else{
			$("#end").val(original_end);
			var students = $("#original_student_list").val();
			var student_list =  $.parseJSON(students);
			$.each(student_list,function(i,stu){
				var a = "<tr>";
				var b = "<td><input type='hidden' name='students' value="+stu.student_id+"><span>"+stu.name+"</span></td>";
				var c = "<td><input name='rm' type='checkbox'></td>";
				var d = "</tr>";
				$("#stu_list").append(a+b+c+d);
			});
		}
	}else if(flag == 2){//修改合伙人
		var original_begin = $("#original_begin").val();
		var original_end = $("#original_end").val();
		var train_begin = $("#begin").val();
		var train_end = $("#end").val();
		if(original_begin == train_begin & original_end == train_end){
			var planPartners =  $("#original_planPartner_list").val();
			var planPartner_list = $.parseJSON(planPartners);
			var planPartner_arr = [];
			$.each(planPartner_list,function(i,ele){
				planPartner_arr[i] = ele.partner_id;
			});
			var new_planPartners = $("#peopleSelect").val();
			if(isContained(new_planPartners,planPartner_arr) == false){
				$("#stu_list").html('');
				reset();
				alertify.log("合伙人已变更，请重新选择要参加培训的学员名单！");
			}else{
				$("#stu_list").html('');
				var students = $("#original_student_list").val();
				var student_list =  $.parseJSON(students);
				$.each(student_list,function(i,stu){
					var a = "<tr>";
					var b = "<td><input type='hidden' name='students' value="+stu.student_id+"><span>"+stu.name+"</span></td>";
					var c = "<td><input name='rm' type='checkbox'></td>";
					var d = "</tr>";
					$("#stu_list").append(a+b+c+d);
				});
			}
		}else{
			$("#stu_list").html('');
		}
	}
}

//添加页改变培训开始时间
function add_train_begin_change(train_begin){
	$("#select_course").removeAttr("disabled");
	$("#add_sel_school").removeAttr("disabled");
	$("#classroom_select").removeAttr("disabled");
	$("#teacher_sel").removeAttr("disabled");
	$("#assistant_sel").removeAttr("disabled");
	var course_id = $("#select_course").val();
	var classroom_id = $("#classroom_select").val();
	if(course_id == ''){
		reset();
		alertify.log("请选择培训课程信息！");
	}else{
		if(classroom_id == ''){
			reset();
			alertify.log("请选择学校教室信息！");
		}else{
			var param = {"train_begin":train_begin,"course_id":course_id,"classroom_id":classroom_id};
			get_train_time(train_begin,course_id,classroom_id);
		}
	}
}

//添加页培训课程改变
function add_team_change(course_id){
	$("#end").val('');
	var train_begin = $("#begin").val();
	if(train_begin == ''){
		reset();
		alertify.log("还未选择培训开始日期！");
	}else{
		if(course_id != ''){
			var	param = {'course_id':course_id};
			$.post({
				type : 'post',
				data : param,
				url  : '/planmanager/get_optional_school_classroom',
				dataType:'json',
				success : function(result){
					var html_s = [];
					html_s.push("<option value=''>培训学校</option>");
					$.each(result.schools,function(i,ele){
						html_s.push("<option value="+ele.school_id+">"+ele.name+"</option>");
					});
					$("#add_sel_school").html(html_s.join(''));
					
					var html_c = [];
					html_c.push("<option value=''>教室</option>");
					$.each(result.classrooms,function(i,ele){
						html_c.push("<option value="+ele.classroom_id+">"+ele.block_number+"</option>");
					});
					$("#classroom_select").html(html_c.join(''));
					
					var html_t = [];
					html_t.push("<option value=''>培训教师</option>");
					$.each(result.teachers,function(i,ele){
						html_t.push("<option value="+ele.teacher_id+">"+ele.name+"</option>");
					});
					$("#teacher_sel").html(html_t.join(''));
					
					var html_a = [];
					html_a.push("<option value=''>培训助教</option>");
					$.each(result.assistants,function(i,ele){
						html_a.push("<option value="+ele.teacher_id+">"+ele.name+"</option>");
					});
					$("#assistant_sel").html(html_a.join(''));
				}
			});
		}
	}
}

function isContained(a, b){
	if(!(a instanceof Array) || !(b instanceof Array)) return false;
    if(a.length < b.length) return false;
    var aStr = a.toString();
    for(var i = 0, len = b.length; i < len; i++){
      if(aStr.indexOf(b[i]) == -1) return false;
    }
    return true;
}

function m_select(){
	var html = [];
	$("#peopleSelect option:selected").each(function(i){
		var id = $(this).val();
		var text = $(this).text();
		html.push("<option value="+id+">"+text+"</option>");
	});
	$("#sel_partner").html(html.join(''));
	//调用公用的查询方法
	search();
}

function search(){
	var train_begin = $("#begin").val();
	var train_end = $("#end").val();
	var partner = $("#peopleSelect").val();
	if(partner == null){
		reset();
		alertify.alert("还未选择合伙人！");
		return false;
	}else{
		if(train_begin == '' || train_end == ''){
			reset();
			alertify.alert("请先确定培训起止时间！");
			return false;
		}else{
			get_student_list(train_begin ,train_end);
		}
	}
}

function edit_select_classroom(classroom_id){
	var train_begin = $("#begin").val();
	var course_id = $("#select_course").val();
	if(train_begin == ''){
		reset();
		alertify.alert("请选择培训开始时间，以方便培训计划排期。");
		$("#classroom_select").val('');
		return false;
	}else{
		if(classroom_id == ''){
			reset();
			alertify.alert("请选择教室！");
		}else{
			var param = {"train_begin":train_begin,"course_id":course_id,"classroom_id":classroom_id};
			$.post({
				type : 'post',
				data : param,
				url  : '/planmanager/get_train_time',
				dataType:'json',
				success : function(result){
					$("#begin").val(result.train_begin);
					$("#end").val(result.train_end);
					//清空编辑页面回显的已有的学员名单
					$("#stu_list").html('');
					reset();
					alertify.log("培训时间已变更，请重新选择要参加培训的学员名单！");
				}
			});
		}
	}
}

//根据培训开始时间，培训课程，教室信息获取培训开始结束时间
function get_train_time(train_begin,course_id,classroom_id){
	var param = {"train_begin":train_begin,"course_id":course_id,"classroom_id":classroom_id};
	$.post({
		type : 'post',
		data : param,
		url  : '/planmanager/get_train_time',
		dataType:'json',
		success : function(result){
			$("#begin").val(result.train_begin);
			$("#end").val(result.train_end);
		}
	});
}

function select_classroom(classroom_id){
	var train_begin = $("#begin").val();
	var course_id = $("#select_course").val();
	if(train_begin == ''){
		reset();
		alertify.alert("请选择培训开始时间，以方便培训计划排期。");
		$("#classroom_select").val('');
		return false;
	}else{
		if(classroom_id == ''){
			reset();
			alertify.alert("请选择教室！");
		}else{
			get_train_time(train_begin,course_id,classroom_id);
		}
	}
}

function get_student_list(train_begin ,train_end){
	var condition = $("#search").val();
	var partner_id = $("#sel_partner").val();
	var	param = {'partner_id':partner_id,'train_begin':train_begin,'train_end':train_end};
	if(condition != ''){
		param = {'partner_id':partner_id,'train_begin':train_begin,'train_end':train_end,'condition':condition};
	}
	$.post({
		type : 'post',
		data : param,
		url  : '/planmanager/get_student_list',
		dataType:'json',
		success : function(result){
			var stu_list = []
			$("input[name='students']").each(function(i){
				stu_list[i] = $(this).val();
			});
			//添加学生表表数据
			var html = [];
			$.each(result,function(i,ele){
				var i = $.inArray(ele.student_id, stu_list);
				if(i == -1){
					html.push("<tr>");
					if(ele.name != '' && ele.name != null){
						html.push("<td>" + ele.name + "</td>");
					}else{
						html.push("<td>未定义 </td>");
					}
					if(ele.sex == 1){
						html.push("<td>男</td>");
					}else if(ele.sex == 0){
						html.push("<td>女</td>");
					}else{
						html.push("<td>未定义 </td>");
					}
					if(ele.identity_card != '' && ele.identity_card != null){
						html.push("<td>" + ele.identity_card + "</td>");
					}else{
						html.push("<td>未定义 </td>");
					}
					if(ele.phone != '' && ele.phone != null){
						html.push("<td>" + ele.phone + "</td>");
					}else{
						html.push("<td>未定义 </td>");
					}
					if(ele.state == 1){
						html.push("<td>在职</td>");
					}else if(ele.state == 0){
						html.push("<td>离职</td>");
					}else{
						html.push("<td>未定义 </td>");
					}
					html.push("<td><input name='ck' type='checkbox' value="+ele.student_id+"></td>");
					html.push("</tr>");
				}
			});
			$("#tbody_students").html(html.join(''));
			
			$('#selectList').modal('show'); 
		}
	});
}

function form_validator(){
	//表单校验
	$('#form1').bootstrapValidator({
        message: '这个值是无效的！',
        live: 'enabled',
        feedbackIcons: {
//	            valid: 'glyphicon glyphicon-ok',
//	            invalid: 'glyphicon glyphicon-remove',
//	            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	
			train_begin: {
                validators: {
                    notEmpty: {
                        message: '选择培训开始时间！'
                    }
                }
            },
//            train_end: {
//                validators: {
//                    notEmpty: {
//                        message: '选择培训结束时间！'
//                    }
//                }
//            },
            course_id: {
                validators: {
                	notEmpty: {
                        message: '请选择培训的课程！'
                    }
                }
            },
            school_id: {
                validators: {
                    notEmpty: {
                        message: '请选择培训学校！'
                    }
                }
            },
            classroom_id: {
                validators: {
                	notEmpty: {
                        message: '请选择教室！'
                    }
                }
            },
            teacher_id: {
                validators: {
                	notEmpty: {
                        message: '请选择培训教师！'
                    }
                }
            },
//            assistant: {
//                validators: {
//                	notEmpty: {
//                        message: '请选择助教老师！'
//                    }
//                }
//            },
            partnerids: {
                validators: {
                	notEmpty: {
                        message: '请选择合伙人！'
                    }
                }
            },
            train_way: {
                validators: {
                	notEmpty: {
                        message: '请选择培训方式！'
                    }
                }
            }
        }
    });
}

function ref(p){
	$(".back_butn").click(function(){
		window.location.href = "/planmanager/goto_plan_list?p="+p;
	});
	
	$("#search_btn").click(function(){
		search();
	});
	
	$("#sel_partner").change(function(){
		search();
	});
	
	$("#add_stu").click(function(){
		//设置学员数量
		var i = $("input[name='students']").length;
		$("input[name='trainers_number']").val(i);
		//隐藏模态框
		$('#selectList').modal('hide');
		$("#save_btn").attr("disabled",false);
	});
	
	form_validator();
	
	$("#to_left_btn").click(function(){
		$("input[name='ck']:checked").each(function(){
			var stu_name = $(this).parent('td').parent('tr').find('td:first-child').text();
			var stu_id = $(this).val();
			var a = "<tr>";
			var b = "<td><input type='hidden' name='students' value="+stu_id+"><span>"+stu_name+"</span></td>";
			var c = "<td><input name='rm' type='checkbox'></td>";
			var d = "</tr>";
			$("#stu_list").append(a+b+c+d);
			$(this).parent('td').parent('tr').remove();
		});
	});
	
	$("#rm_check_btn").click(function(){
		$("input[name='rm']:checked").each(function(){
			$(this).parent('td').parent('tr').remove();
		});
	});
}