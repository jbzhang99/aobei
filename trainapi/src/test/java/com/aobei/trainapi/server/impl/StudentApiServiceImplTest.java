package com.aobei.trainapi.server.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.aobei.train.model.Student;
import com.aobei.train.service.StudentService;
import com.aobei.trainapi.server.bean.MessageContent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aobei.train.model.Message;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.StudentApiService;
import com.aobei.trainapi.server.bean.ApiResponse;

import custom.bean.OrderInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentApiServiceImplTest {
	
	@Autowired
	StudentApiService studentApiService;
	@Autowired
	StudentService studentService;
	
	@Test
	public void student_undone_orderinfo(){
		List<OrderInfo> StuUndoneOrderList = studentApiService.selectStuUndoneOrder(44L,1,10);
		List<OrderInfo> StuUndoneOrderList2 = studentApiService.selectStuUndoneOrder(4L,1,10);
		//Assert.assertNotNull(selectStuUndoneOrder);
		//Assert.assertNull(selectStuUndoneOrder);
		Assert.assertEquals(StuUndoneOrderList.size(),0);
		Assert.assertEquals(StuUndoneOrderList2.size(),0);
	}
	
	@Test
	public void student_complete_orderinfo() throws Exception{
		String date="2018-03-08 10:31:07";
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date2 = sf.parse(date);
		List<OrderInfo> orderList = studentApiService.selectStuCompleteOrder(4L,date2,1,10);
		//Assert.assertEquals(orderList.size(),0);
		Assert.assertNotNull(orderList);
	}
	
	@Test
	public void student_show_taskdetail(){


		OrderInfo orderInfo = studentApiService.selectStuShowTaskdetail(1072893448249450496L,"1524843181");
		OrderInfo orderInfo2 = studentApiService.selectStuShowTaskdetail(5L,"1062752443352727554L");
		//Assert.assertNull(orderInfo);
		Assert.assertNotNull(orderInfo);
		Assert.assertEquals(orderInfo.getStudent_name(),"赵丽");
		Assert.assertNotNull(orderInfo2);
		Assert.assertEquals(orderInfo2.getStudent_name(),"赵丽");
	}
	
	@Test
	public void student_show_massageInfo() throws ParseException{
		Student student = studentService.selectByPrimaryKey(1067677206111346688l);
		List<MessageContent> messageList = studentApiService.selectStuMessage(student,1,10);
		String dates="2018-03-08 10:31:07";
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date2 = sf.parse(dates);
		List<MessageContent> messageList2 = studentApiService.selectStuMessage(student,1,10);
		Assert.assertNotNull(messageList);
		Assert.assertEquals(messageList.size(),0);
		Assert.assertEquals(messageList2.size(),0);
	}
	
	@Test
	public void student_message_detail(){
		Message message1 = studentApiService.selectMessageDetail(4L,1L);
		Message message2 = studentApiService.selectMessageDetail(4L,3L);
		Assert.assertNotNull(message1);
		Assert.assertNotNull(message2);
	}
	
	@Test
	public void student_update_work_status(){
		ApiResponse response = studentApiService.student_update_work_status(1067677205725470720L,"1069300311276052480L","leave","结束","","");
		MutationResult mutationResult = response.getMutationResult();
		int status = mutationResult.getStatus();
		Assert.assertEquals(status, 0);
	}
	
}
