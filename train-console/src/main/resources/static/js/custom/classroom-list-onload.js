$(function(){
	$("#add_butn").click(function(){
		var page_current_page = $("#list_current_page").val();
		window.location.href = "/classroommanager/classroom_add?page_current_page="+page_current_page;
	});
});