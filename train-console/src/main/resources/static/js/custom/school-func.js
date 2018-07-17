function form_validator(){
	$('#form1').bootstrapValidator({
        message: '这个值是无效的！',
        live: 'enabled',
        feedbackIcons: {
//	            valid: 'glyphicon glyphicon-ok',
//	            invalid: 'glyphicon glyphicon-remove',
//	            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	name: {
                message: '此学校名称是无效的！',
                validators: {
                    notEmpty: {
                        message: '请填写学校名称！'
                    },
                    stringLength: {
                        max: 50,
                        message: '学校名称最长不得多于50个字符'
                    } 
                }
            },
            address: {
                validators: {
                    notEmpty: {
                        message: '请填写学校地址！'
                    },
                    stringLength: {
                        max: 50,
                        message: '学校地址最长不得多于50个字符'
                    } 
                }
            },
            intro: {
                validators: {
                	notEmpty: {
                        message: '学校简介不能为空！'
                    },
                    stringLength: {
                        min: 5,
                        max: 200,
                        message: '学校简介最短不得低于5个字符，最长不得多于200个字符'
                    } 
                }
            },
//	            cooperation_start: {
//	                validators: {
//	                    notEmpty: {
//	                        message: '请选择与学校的合作开始时间！'
//	                    }
//	                }
//	            },
//	            cooperation_end: {
//	                validators: {
//	                	notEmpty: {
//	                        message: '请选择与学校的合作结束时间！'
//	                    }
//	                }
//	            },
            linkman: {
                validators: {
                	notEmpty: {
                        message: '请填写联系人的名称！'
                    },
                    stringLength: {
                        max: 10,
                        message: '联系人最长不得多于10个字符'
                    } 
                }
            },
            contact_number: {
                validators: {
                	notEmpty: {
                        message: '请填写联系电话！'
                    },
                    regexp: {
                        regexp: /^1(3|4|5|7|8|9)\d{9}$/,
                        message: '请输入一个有效的联系电话！'
                    }
                }
            }
        }
    });
}

//跳转到学校信息编辑页面
function school_edit(school_id,pageNo){
	var page_current_page = pageNo;
	window.location.href = "/schoolmanager/school_edit?school_id="+school_id+"&page_current_page="+page_current_page;
}

//异步删除学校信息
function school_del(school_id,pageNo){
	var page_current_page = pageNo;
	reset();
	alertify.confirm("若要删除该条学校信息，其下所属的教室信息也会被删除，确定删除这条信息吗？", function (e) {
	    if (e) {
	    	$.ajax({
				url:"/schoolmanager/school_delete/"+school_id,
				type:'GET',
				success:function(data){
					reset();
					alertify.success(data.msg);
					setTimeout(function (){
						window.location.href = "/schoolmanager/goto_school_list?p="+page_current_page;
					}, 1500);
				}
			});
	    } else {
	    	reset();
			alertify.log("已取消操作。");
	    }
	});
}