package com.aobei.trainapi.schema;
import com.aobei.trainapi.server.ApiCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.aobei.train.model.Student;
import com.aobei.train.model.Teacher;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.ApiService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;

@Component
public class Mutation implements GraphQLMutationResolver {

	private static final boolean MutationResult = false;


	@Autowired
	private TokenUtil TOKEN;
	
	
	@Autowired
	private ApiService apiService;
	@Autowired
	private ApiCommonService apiCommonService;
	/**
	 * 绑定的学员信息
	 * @return
	 */
	private Student my_student_bindinfo(){
		Student student = apiService.studentInfoByUserId(TOKEN.getUuid());
		if(student == null){
			Errors._40101.throwError();
		}
		return student;
	}
	
	/**
	 * 绑定学员信息
	 * @param phone
	 * @param id_num
	 * @return
	 */
	public MutationResult my_student_binduser(String phone,String id_num){
		Student student = apiService.studentByPhoneAndIdentityCard(phone, id_num);
		if(student == null){
			Errors._40101.throwError();
		}
		if(student.getUser_id() != null){
			Errors._40103.throwError();
		}
		Long student_id = student.getStudent_id();
		//去掉角色互斥条件
		//apiService.teacherInfoByUserId(TOKEN.getUuid()) != null
		if(apiService.studentInfoByUserId(TOKEN.getUuid()) != null){
			Errors._40104.throwError();
		}
		int count = apiService.bindStudent(TOKEN.getUuid(), student_id);
		if(count == 1){
			return new MutationResult();
		}
		Errors._40102.throwError();
		return null;
	}
	
	/**
	 * 学员课程章节添加评价
	 * @param train_schedule_id
	 * @param score
	 * @param comment
	 * @return
	 */
	public MutationResult my_student_insert_course_evaluate(Long train_schedule_id,Integer score,String comment){
		Student student = my_student_bindinfo();
		boolean hasTrainSchedule = apiService.studentAcceptTrainSchedule(train_schedule_id, student.getStudent_id());
		//判断学员是否有参加该培训计划
		if(!hasTrainSchedule){
			Errors._40107.throwError();
		}
		//判断是否已评价过了
		boolean hasEvaluate = apiService.hasCourseEvaluate(train_schedule_id, student.getStudent_id());
		if(hasEvaluate){
			Errors._40108.throwError();
		}
		int count = apiService.addCourseEvaluate(train_schedule_id, student.getStudent_id(), score, comment);
		if(count == 0){
			Errors._40109.throwError();
		}
		return new MutationResult();
	}
	
	
	/**
	 * 绑定的老师信息
	 * @return
	 */
	private Teacher my_teacher_bindinfo(){
		Teacher teacher = apiService.teacherInfoByUserId(TOKEN.getUuid());
		if(teacher == null){
			Errors._40105.throwError();
		}
		return teacher;
	}
	
	/**
	 * 绑定老师信息
	 * @param phone
	 * @param id_num
	 * @return
	 */
	public MutationResult my_teacher_binduser(String phone,String id_num){
		Teacher teacher = apiService.teacherInfoByPhoneAndIdentityCard(phone, id_num);
		if(teacher == null){
			Errors._40105.throwError();
		}
		if(teacher.getUser_id() != null){
			Errors._40106.throwError();
		}
		Long teacher_id = teacher.getTeacher_id();
		teacher = apiService.teacherInfoByUserId(TOKEN.getUuid());
		if(apiService.teacherInfoByUserId(TOKEN.getUuid()) != null){
			Errors._40104.throwError();
		}
		int count = apiService.bindTeacher(TOKEN.getUuid(), teacher_id);
		if(count == 1){
			return new MutationResult();
		}
		Errors._40102.throwError();
		return null;
	}


	/**********
	 * 绑定推送的信息
	 */

	public MutationResult mutation_push_bind(String push_client_id){
		String client = TOKEN.getClientId();
		return  apiCommonService.bindPush(TOKEN.getUuid()+"",push_client_id,client);

	}

	public MutationResult  mutation_push_unbind(String push_client_id){

		String client  = TOKEN.getClientId();
		return  apiCommonService.unbindPush(TOKEN.getUuid()+"",push_client_id,client);
	}

}
