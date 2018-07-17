function content_edit(cms_banner_id){
	var app = $("#app").val();
	window.location.href = "/bannermanager/banner_edit?cms_banner_id="+cms_banner_id+"&app="+app;
}

function change_state(sign,cms_banner_id){

	var app = $("#app").val();
	var param = {'sign':sign,'cms_banner_id':cms_banner_id,'app':app,"mts":$("#mts").val()};
	$.ajax({
		type:'post',
		data:param,
		url:'/bannermanager/change_state',
		dataType:'json',
		success:function(data){
	        //请求成功时
			reset();
			alertify.success(data.msg);
			ref(data);
	    }
	});
}

function move_up(obj,serial_number,cms_banner_id){
	var pre_cms_id = $(obj).parent().parent().prev().data('id');
	var app = $("#app").val();
	var param = {'serial_number':serial_number,'cms_banner_id':cms_banner_id,'pre_cms_id':pre_cms_id,'app':app,"mts":$("#mts").val()};
	$.ajax({
		type:'post',
		data:param,
		url:'/bannermanager/move_up',
		dataType:'json',
		success:function(data){
	        //请求成功时
			reset();
			alertify.success(data.msg);
			ref(data);
	    }
	});
}

function move_down(obj,serial_number,cms_banner_id){
	var next_cms_id = $(obj).parent().parent().next().data('id');
	var app = $("#app").val();
	var param = {'serial_number':serial_number,'cms_banner_id':cms_banner_id,'next_cms_id':next_cms_id,'app':app,"mts":$("#mts").val()};
	$.ajax({
		type:'post',
		data:param,
		url:'/bannermanager/move_down',
		dataType:'json',
		success:function(data){
	        //请求成功时
			reset();
			alertify.success(data.msg);
			ref(data);
	    }
	});
}

//日期格式化
function FormatDate (strTime) {
    var date = new Date(strTime);
    month = date.getMonth()+1;
    day = date.getDate();
    hour = date.getHours();
    minute = date.getMinutes();
    if((date.getMonth()+1) < 10){
    	month = "0" + (date.getMonth()+1);
    }
    if((date.getDate() < 10)){
    	day = "0" + date.getDate();
    }
    if((date.getHours() < 10)){
    	hour = "0" + date.getHours();
    }
    if((date.getMinutes() < 10)){
    	minute = "0" + date.getMinutes();
    }
    return date.getFullYear()+"-"+month+"-"+day+" "+hour+":"+minute;
}

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
        	cover_img: {
                validators: {
                    notEmpty: {
                        message: '请选择要上传的图片！'
                    }
                }
            },
            title: {
                validators: {
//                    notEmpty: {
//                        message: '请输入标题！'
//                    },
                    stringLength: {
                        max: 15,
                        message: '标题最长不得多于15个字符'
                    } 
                }
            },
            intro: {
                validators: {
//                	notEmpty: {
//                        message: '请输入简介！'
//                    },
                    stringLength: {
                        max: 99,
                        message: '简介最长不得多于99个字符'
                    } 
                }
            },
            app: {
                validators: {
                	notEmpty: {
                        message: '请选择图片的使用端！'
                    }
                }
            },
            online_datetime: {
                validators: {
                    notEmpty: {
                        message: '请选择上线时间！'
                    },
                    regexp: {
                        regexp: /^[1-2][0-9][0-9][0-9]-([1][0-2]|0?[1-9])-([12][0-9]|3[01]|0?[1-9]) ([01][0-9]|[2][0-3]):[0-5][0-9]$/,
                        message: '请输入正确的日期格式！如（2018-03-22 11:47）'
                    }
                }
            },
            offline_datetime: {
                validators: {
                	notEmpty: {
                        message: '请选择下线时间！'
                    },
                    regexp: {
                        regexp: /^[1-2][0-9][0-9][0-9]-([1][0-2]|0?[1-9])-([12][0-9]|3[01]|0?[1-9]) ([01][0-9]|[2][0-3]):[0-5][0-9]$/,
                        message: '请输入正确的日期格式！如（2018-03-22 11:47）'
                    }
                }
            }
        }
    });
}

function fileinput_set(){
	$("#cover_img").fileinput({
        language: 'zh', //设置语言
        //uploadUrl:"http://127.0.0.1/testDemo/fileupload/upload.do", //上传的地址
        allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
        //uploadExtraData:{"id": 1, "fileName":'123.mp3'},
        //uploadAsync: true, //默认异步上传
        showUpload:false, //是否显示上传按钮
        showRemove :true, //显示移除按钮
        showPreview :true, //是否显示预览
        showCaption:false,//是否显示标题
        //browseClass:"btn btn-primary", //按钮样式    
        dropZoneEnabled: false,//是否显示拖拽区域
        /*minImageWidth: 50, //图片的最小宽度
        minImageHeight: 50,//图片的最小高度
        maxImageWidth: 1080,//图片的最大宽度
        maxImageHeight: 343,//图片的最大高度*/
        maxFileSize:1000,//单位为kb，如果为0表示不限制文件大小
        //minFileCount: 0,
        //maxFileCount:10, //表示允许同时上传的最大文件个数
        enctype:'multipart/form-data',
        validateInitialCount:true,
        //previewFileIcon: "<iclass='glyphicon glyphicon-king'></i>",
        //msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
    });
}

function ref(data){
	var html = [];
	$.each(data.banner_list,function(i,ele){
		html.push("<tr data-id="+ele.cms_banner_id+">");
		html.push("<td style='vertical-align: middle'>00"+(i+1)+"</td>");
		html.push("<td><img alt='封面图' src="+ele.ossImg.url+" width='100px' height='75px'></td>");
		html.push("<td style='vertical-align: middle'>"+ele.title+"</td>");
		html.push("<td style='vertical-align: middle'>"+ele.intro+"</td>");
		//$.each(s,function(i,k){
        //if(ele.app==e.group_name){
        html.push("<td style='vertical-align: middle'>"+ele.group_name+"</td>");
        //}
        //})
		/*if(ele.app == 'wx_m_custom'){
			html.push("<td style='vertical-align: middle'>顾客端</td>");
		}
		if(ele.app == 'wx_m_teacher'){
			html.push("<td style='vertical-align: middle'>教师端</td>");
		}
		if(ele.app == 'wx_m_student'){
			html.push("<td style='vertical-align: middle'>学员端</td>");
		}
		if(ele.app == 'wx_m_partner'){
			html.push("<td style='vertical-align: middle'>合伙人端</td>");
		}*/

		var online_time = ele.online_datetime;
		var offline_time = ele.offline_datetime;
		var	online = FormatDate(new Date(online_time));
		var	offline = FormatDate(new Date(offline_time));
		html.push("<td style='vertical-align: middle'>"+online+"</td>");
		html.push("<td style='vertical-align: middle'>"+offline+"</td>");
		list = eval(data.banner_list);
		var len = list.length;
		html.push("<td style='vertical-align: middle'>");
		if(ele.sign == 0){
			//上线按钮
			html.push("<a class='btn btn-link btn-xs' href='javascript:;' style='outline:none' onclick=change_state("+ele.sign+",'"+ele.cms_banner_id+"');><i class='fas fa-arrow-circle-up'></i>上线</a>");
		}else if(ele.sign == 1){
			//下线按钮
			html.push("<a class='btn btn-link btn-xs' href='javascript:;' style='outline:none' onclick=change_state("+ele.sign+",'"+ele.cms_banner_id+"');><i class='fas fa-arrow-circle-down'></i>下线</a>");
		}
		html.push("<a class='btn btn-link btn-xs' href='javascript:;' style='outline:none' onclick=content_edit('"+ele.cms_banner_id+"');><i class='fas fa-edit'></i>编辑</a>")
		if(i != 0){
			html.push("<a class='btn btn-link btn-xs' href='javascript:;' style='outline:none' onclick=move_up(this,"+ele.serial_number+",'"+ele.cms_banner_id+"');><i class='fas fa-level-up-alt'></i>上移</a>");
		}
		if(i != (len - 1)){
			html.push("<a class='btn btn-link btn-xs' href='javascript:;' style='outline:none' onclick=move_down(this,"+ele.serial_number+",'"+ele.cms_banner_id+"');><i class='fas fa-level-down-alt'></i>下移</a>");
		}
		html.push("</td>");
		html.push("</tr>");
	});
	$("#tbody_contents").html(html.join(''));
    setTimeout(function (){
        window.location.reload();
    }, 1000);

}
function goto_page(o) {
	var s = $(o).val();

	if(s==''){
		reset();
		alertify.alert("请选择跳转类型！");
		return false;
	}
	if(s==1){
		$("#modal_product").modal('show');
	}
    if(s==2){
        $("#modal_native").modal('show');
    }
}
function goto_page_data(o) {
    if(o==''){
        reset();
        alertify.alert("请选择跳转类型！");
        return false;
    }
    if(o==1){
        $("#modal_product").modal('show');
    }
    if(o==2){
        $("#modal_native").modal('show');
    }
}
function get_product_list(condition) {
        $.post({
            type : 'post',
            data : {condition:condition},
            url  : '/bannermanager/get_product_list',
            dataType:'json',
            success : function(result){

                var html = [];
                $.each(result,function(i,ele){

                    html.push("<tr>");
                    html.push("<td style='width:70px;'>" + "<input name='banner_href' type='radio'  id="+ele.cms_url_id+" value="+ele.title+">" + "</td>");
                    if(ele.title != '' && ele.title != null){
                        html.push("<td>" + ele.title + "</td>");
                    }else{
                        html.push("<td>未定义 </td>");
                    }
                    html.push("</tr>");

                });
                $("#tbody_product").html(html.join(''));

            }
        });
}
function get_product_list1(condition) {
    $.post({
        type : 'post',
        data : {condition:condition},
        url  : '/bannermanager/get_product_list1',
        dataType:'json',
        success : function(result){

            var html = [];
            $.each(result,function(i,ele){

                html.push("<tr>");
                html.push("<td style='width:70px;'>" + "<input name='product_href' type='radio'  id="+ele.product_id+">" + "</td>");
                html.push("<td>"+ele.name+"</td>")
                html.push("</tr>");

            });
            $("#tbody_product1").html(html.join(''));

        }
    });
}
function get_native_list(condition) {
    $.post({
        type : 'post',
        data : {condition:condition},
        url  : '/bannermanager/get_native_list',
        dataType:'json',
        success : function(result){

            var html = [];
            $.each(result,function(i,ele){

                html.push("<tr>");
                html.push("<td style='width:70px;'>" + "<input name='banner_href' type='radio'  id="+ele.cms_url_id+" value="+ele.title+">" + "</td>");
                if(ele.title != '' && ele.title != null){
                    html.push("<td>" + ele.title + "</td>");
                }else{
                    html.push("<td>未定义 </td>");
                }
                html.push("</tr>");

            });
            $("#tbody_native").html(html.join(''));

        }
    });
}
function load() {
    var pro = $("#select_type option:selected").val();
    var rad = $("input:radio[name='banner_href']:checked").attr("id")

    var pro_name = $("#select_type option:selected").text();
    var rad_name = $("input:radio[name='banner_href']:checked").parent().next().text();


    $.post('/bannermanager/getUrl',{cms_url_id:rad},function (data) {

        var html = [];
        html.push("<p id='data' style='color:red;'>");
        html.push(pro_name+" | "+rad_name);
        html.push("<input hidden name='href' value="+data.url+" />");
        html.push("<a style='margin-left:40px;' href='#' onclick='edit_pro("+pro+")'>编辑</a>");
        html.push("<a style='margin-left:40px;' href='#' onclick='del_pro(this)'>删除</a>")
        html.push("</p>");

        $("#last_div").html(html.join(''));
    });
}
function load1() {
    var pro = $("#input[name='product_href']").val();
    var rad = $("input:radio[name='product_href']:checked").attr("id")

    var pro_name = $("#input_type_text").val();
    var rad_name = $("input:radio[name='product_href']:checked").parent().next().text();

    var product_id = $("input:radio[name='product_href']:checked").attr("id");

        var html = [];
        html.push("<p id='data' style='color:red;'>");
        html.push(pro_name+" | "+rad_name);
        html.push("<input hidden name='href' value="+product_id+" />");
        html.push("<a style='margin-left:40px;' href='#' onclick='edit_pro1()'>编辑</a>");
        html.push("<a style='margin-left:40px;' href='#' onclick='del_pro(this)'>删除</a>")
        html.push("</p>");

        $("#last_div").html(html.join(''));

}
function edit_pro1() {
    var product_id = $("#val_href").val()
    if(product_id.length==19){
        $("input[name='product_href']").each(function (i,k) {
            if(k.id==product_id){
                k.setAttribute("checked",true);
            }
        })
    }
    $("#modal_product_1").modal('show');
}
/*--------------------------------0.5 version----------------------------------- */
//改变事件，动态加载使用端数据
function change_port(v) {
    $.get('/bannermanager/getPort',{group_name:v.value},function (data) {
        $("#app").html('');
        var html = [];

        if(data.list.length==0){
            html.push("<option value="+0+" >暂无此端</option>");
        }else{
            $.each(data.list,function(index,t){
                html.push("<option value="+t.app_pack_id+" >"+t.app_pack_name+"</option>");
                html.join(' ');
            })
        }
        $("#app").html(html);
    })
}
//初始化加载使用端数据
function inint(v){
    $.get('/bannermanager/getPort',{group_name:v},function (data) {
        $("#app").html('');
        var html = [];

        if(data.list.length==0){
            html.push("<option value="+0+" >暂无此端</option>");
        }else{
            $.each(data.list,function(index,t){
                html.push("<option value="+t.app_pack_id+" >"+t.app_pack_name+"</option>");
                html.join(' ');
            })
        }

        $("#app").html(html);
        var back_app = $("#back_app").val();
        $("#app").val(back_app);
    })
}
function goto_product() {
    if($("input[name='product_href']")){
        $("#modal_product_1").modal('show');
    }
}