package com.aobei.train.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.Category;
import com.aobei.train.model.CategoryExample;
import com.aobei.train.model.Chapter;
import com.aobei.train.model.ChapterExample;
import com.aobei.train.model.Course;
import com.aobei.train.model.CourseExamSubject;
import com.aobei.train.model.CourseExamSubjectExample;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.CategoryMapper;
import com.aobei.train.mapper.ChapterMapper;
import com.aobei.train.mapper.CourseExamSubjectMapper;
import com.aobei.train.mapper.CourseMapper;
import com.aobei.train.mapper.CourseServiceitemMapper;
import com.aobei.train.mapper.ExamSubjectMapper;
import com.aobei.train.mapper.OssImgMapper;
import com.aobei.train.mapper.ServiceitemMapper;
import com.aobei.train.model.CourseExample;
import com.aobei.train.model.CourseServiceitem;
import com.aobei.train.model.CourseServiceitemExample;
import com.aobei.train.model.ExamSubject;
import com.aobei.train.model.OssImg;
import com.aobei.train.model.Serviceitem;
import com.aobei.train.model.ServiceitemExample;
import com.aobei.train.service.CourseService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.CourseToCategory;
import custom.bean.Status;

@Service
public class CourseServiceImpl extends MbgServiceSupport<CourseMapper, Long, Course, Course, CourseExample> implements CourseService{

	@Autowired
	private CourseMapper courseMapper;
	
	@Autowired
	private CourseServiceitemMapper courseServiceitemMapper;
	
	@Autowired
	private ServiceitemMapper serviceitemMapper;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private OssImgMapper ossImgMapper;
	
	@Autowired
	private ChapterMapper chapterMapper;
	
	@Autowired
	private ExamSubjectMapper examSubjectMapper;
	
	@Autowired
	private CourseExamSubjectMapper courseExamSubjectMapper;
	
	
	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(courseMapper);
	}

	/**
	 * 封装 课程列表
	 */
	@Override
	public List xShowCourseTeam(List<Course> cList) {
		List categoryList=new ArrayList<>();
		//遍历课程
		for (Course course : cList) {
			//创建一个集合进行封装
			List<CourseToCategory> ctcList=new ArrayList<>();
			//根据每个课程找到对应的服务项目
			CourseServiceitemExample courseServiceitemExample = new CourseServiceitemExample();
			courseServiceitemExample.or().andCourse_idEqualTo(course.getCourse_id());
			List<CourseServiceitem> csExample = this.courseServiceitemMapper.selectByExample(courseServiceitemExample);
			for (CourseServiceitem courseServiceitem : csExample) {
				//创建对象
				CourseToCategory courseToCategory=new CourseToCategory();
				courseToCategory.setCourse_id(course.getCourse_id());
				courseToCategory.setCourse_name(course.getName());
				//根据每个服务项目找到分类
				Serviceitem serviceitem = this.serviceitemMapper.selectByPrimaryKey(courseServiceitem.getServiceitem_id());
				if(serviceitem !=null){
					courseToCategory.setServiceitem_id(serviceitem.getServiceitem_id());
					courseToCategory.setServiceitem_name(serviceitem.getName());
					//根据服务项目找到分类
					Category category = this.categoryMapper.selectByPrimaryKey(serviceitem.getCategory_id());
					if(category !=null){
						courseToCategory.setCategory_id(category.getCategory_id());
						courseToCategory.setCategory_name(category.getName());
						ctcList.add(courseToCategory);
					}
				}
			}
			categoryList.add(ctcList);
		}
		return categoryList;
	}

	
	/**
	 * 封装  课程添加页面（找到所有有服务项并且未删除的分类）
	 */
	@Override
	public List<Category> xCreateTeamShow() {
		List<Category> cList=new ArrayList<>();
		//所有未删除的分类(有服务项目)
		CategoryExample categoryExample = new CategoryExample();
		//.andStateEqualTo(1)
		categoryExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value).andActivedEqualTo(1);
		List<Category> cteList = this.categoryMapper.selectByExample(categoryExample);
		for (Category category : cteList) {
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andCategory_idEqualTo(category.getCategory_id()).andDeletedEqualTo(Status.DeleteStatus.no.value);
			List<Serviceitem> example = this.serviceitemMapper.selectByExample(serviceitemExample);
			if(example.size() !=0){
				cList.add(category);
			}
		}
		return cList;
	}

	
	/**
	 * 封装  添加课程（并且添加章节，服务项，考试科目）
	 * @param map
	 * @param params
	 */
	@Override
	@Transactional(timeout = 5)
	public int xAddCourseTeam(Map<String, Object> map, Map<String, String> params) {
		//保存图片信息
		OssImg ossImg=new OssImg();
		ossImg.setOss_img_id(IdGenerator.generateId());
		ossImg.setFormat(params.get("file_format"));
		ossImg.setName(params.get("file_name"));
		ossImg.setUrl(params.get("file_url"));
		ossImg.setBucket(params.get("bucketName"));
		ossImg.setEffect("课程图片");
		ossImg.setCreate_time(new Date());
		ossImg.setAccess_permissions("private");
		this.ossImgMapper.insert(ossImg);
		
		//创建课程章节集合
		JSONArray jsonArry = JSONArray.parseArray(map.get("chapterList").toString());
		List<Chapter> chpList = jsonArry.toJavaList(Chapter.class);
		//课程组集合
		int chpterLength=chpList.size();
		JSONArray teamjsonArry = JSONArray.parseArray(map.get("courseTeamList").toString());
		//将前台数据封装成集合
		List<Course> cList = teamjsonArry.toJavaList(Course.class);
		Course course = cList.get(0);
		course.setCourse_id(IdGenerator.generateId());
		course.setChapter_count(chpterLength);
		course.setCdate(new Date());
		course.setImg(ossImg.getOss_img_id().toString());
		course.setState(1);
		course.setDeleted(Status.DeleteStatus.no.value);
		
		//循环所有课程章节，进行存储
		int courseHour=0;
		for (Chapter chapter : chpList) {
			//将每个课程章节对象进行数据库存储
			chapter.setChapter_id(IdGenerator.generateId());
			chapter.setCourse_id(course.getCourse_id());
			chapter.setDeleted(Status.DeleteStatus.no.value);
			this.chapterMapper.insert(chapter);
			courseHour+=chapter.getTime_length();
		}
		//根据每个章节时长算出总课时
		course.setHour(courseHour);
		
		
		//课程组所对应的包含的服务项目
		List<Long> longs = JSONObject.parseArray(map.get("categoryAllList").toString(),Long.class);
		for (Long i : longs) {
			CourseServiceitem courseServiceitem=new CourseServiceitem();
			courseServiceitem.setCourse_id(course.getCourse_id());
			courseServiceitem.setServiceitem_id(i);
			this.courseServiceitemMapper.insert(courseServiceitem);
		}
		//创建考试科目
		ExamSubject examSubject=new ExamSubject();
		examSubject.setExam_subject_id(IdGenerator.generateId());
		examSubject.setName(course.getName()+"(培训考试)");
		examSubject.setType(2);//考试类型
		examSubject.setTime_length(courseHour);//占用时长
		examSubject.setDeleted(Status.DeleteStatus.no.value);//是否删除
		this.examSubjectMapper.insert(examSubject);
		//维护课程和考试科目中间表
		CourseExamSubject courseExamSubject=new CourseExamSubject();
		courseExamSubject.setCourse_id(course.getCourse_id());
		courseExamSubject.setExam_subject_id(examSubject.getExam_subject_id());
		this.courseExamSubjectMapper.insert(courseExamSubject);

		//保存课程
		int num = this.insert(course);
		return num;
	}

	
	/**
	 * 封装  编辑课程
	 * @param map
	 * @param course_id
	 * @return
	 */
	@Override
	@Transactional(timeout = 5)
	public Course xEditCourseTeam(Map<String, Object> map,Long course_id) {
		//根据课程组编号删掉所有课程
		//获取到要删除的章节编号
		List<Long> delCids = JSONObject.parseArray(map.get("deleteClist").toString(), Long.class);
		for (Long cid : delCids) {
			//this.chapterMapper.deleteByPrimaryKey(cid);
            Chapter chapter = this.chapterMapper.selectByPrimaryKey(cid);
            chapter.setDeleted(1);
            this.chapterMapper.updateByPrimaryKey(chapter);
        }
		
		//删除课程组和服务项对应表
		CourseServiceitemExample serviceImpl=new CourseServiceitemExample();
		serviceImpl.or().andCourse_idEqualTo(course_id);
		this.courseServiceitemMapper.deleteByExample(serviceImpl);
		
		//添加 课程组所对应的包含的服务项目
		List<Long> longs = JSONObject.parseArray(map.get("categoryAllList").toString(), Long.class);
		for (Long i : longs) {
			CourseServiceitem courseServiceitem=new CourseServiceitem();
			courseServiceitem.setCourse_id(course_id);
			courseServiceitem.setServiceitem_id(i);
			this.courseServiceitemMapper.insert(courseServiceitem);
		}
		
		/*ChapterExample chapterExample = new ChapterExample();
		chapterExample.or().andCourse_idEqualTo(course_id);
		this.chapterMapper.deleteByExample(chapterExample);*/
		
		//创建课程集合
		JSONArray jsonArry = JSONArray.parseArray(map.get("courseList").toString());
		List<Chapter> newChapterList = jsonArry.toJavaList(Chapter.class);
		int courseHour=0;
		for (Chapter chapter : newChapterList) {
			if(chapter.getChapter_id().toString().length() <19){
				chapter.setChapter_id(IdGenerator.generateId());
				chapter.setCourse_id(course_id);
				chapter.setDeleted(Status.DeleteStatus.no.value);
				this.chapterMapper.insert(chapter);
				courseHour+=chapter.getTime_length();
			}else{
				chapter.setCourse_id(course_id);
				this.chapterMapper.updateByPrimaryKeySelective(chapter);
				courseHour+=chapter.getTime_length();
			}
		}
		//课程组集合
		int courseLength=newChapterList.size();
		JSONArray teamjsonArry = JSONArray.parseArray(map.get("courseTeamList").toString());
		//将前台数据封装成集合
		List<Course> ctList = teamjsonArry.toJavaList(Course.class);
		Course course = ctList.get(0);
		course.setCourse_id(course_id);
		course.setChapter_count(courseLength);
		course.setState(1);
		course.setHour(courseHour);
		
		
		//考试科目
		CourseExamSubjectExample courseExamSubjectExample = new CourseExamSubjectExample();
		courseExamSubjectExample.or().andCourse_idEqualTo(course_id);
		List<CourseExamSubject> example = this.courseExamSubjectMapper.selectByExample(courseExamSubjectExample);
		CourseExamSubject courseExamSubject = example.get(0);
		ExamSubject examSubject = this.examSubjectMapper.selectByPrimaryKey(courseExamSubject.getExam_subject_id());
		examSubject.setName(course.getName()+"(培训考试)");
		examSubject.setTime_length(courseHour);
		this.examSubjectMapper.updateByPrimaryKey(examSubject);
		
		
		return course;
	}

	
	/**
	 * 封装 删除课程
	 */
	@Override
	@Transactional(timeout = 5)
	public Boolean xDelCourseByTeamId(Long course_id) {
		Boolean flag;
		//先删除课程对应的所有章节
		ChapterExample chapterExample = new ChapterExample();
		chapterExample.or().andCourse_idEqualTo(course_id);
		List<Chapter> cList = this.chapterMapper.selectByExample(chapterExample);
		for (Chapter chapter : cList) {
			chapter.setDeleted(Status.DeleteStatus.yes.value);
			this.chapterMapper.updateByPrimaryKeySelective(chapter);
		}
		//删除课程
		Course course = this.selectByPrimaryKey(course_id);
		course.setDeleted(Status.DeleteStatus.yes.value);
		int i = this.updateByPrimaryKeySelective(course);
		//考试科目
		CourseExamSubjectExample courseExamSubjectExample = new CourseExamSubjectExample();
		courseExamSubjectExample.or().andCourse_idEqualTo(course_id);
		List<CourseExamSubject> selectByExample = this.courseExamSubjectMapper.selectByExample(courseExamSubjectExample);
		CourseExamSubject courseExamSubject = selectByExample.get(0);
		ExamSubject examSubject = this.examSubjectMapper.selectByPrimaryKey(courseExamSubject.getExam_subject_id());
		examSubject.setDeleted(Status.DeleteStatus.yes.value);
		int j = this.examSubjectMapper.updateByPrimaryKey(examSubject);
		if(i>0 & j>0){
			flag=true;
		}else{
			flag=false;
		}
		return flag;
	}

	/**
	 * 将每个课程组对应的服务项目，进行封装
	 * @return
	 */
	@Override
	public List<CourseToCategory> xFeng(Long courseid) {
		List<CourseToCategory> vptList=new ArrayList<>();
		// 在根据课程组编号，查出来所有的二级分类
		CourseServiceitemExample example = new CourseServiceitemExample();
		example.or().andCourse_idEqualTo(courseid);
		List<CourseServiceitem> list = this.courseServiceitemMapper.selectByExample(example);
		Course course = this.selectByPrimaryKey(courseid);
		for (CourseServiceitem courseServiceitem : list) {
			CourseToCategory ct=new CourseToCategory();
			ct.setCourse_id(courseid);
			ct.setCategory_name(course.getName());
			//服务项目
			Serviceitem serviceitem = this.serviceitemMapper.selectByPrimaryKey(courseServiceitem.getServiceitem_id());
			if(serviceitem !=null){
				ct.setServiceitem_id(serviceitem.getServiceitem_id());
				ct.setServiceitem_name(serviceitem.getName());
				//分类
				Category category = this.categoryMapper.selectByPrimaryKey(serviceitem.getCategory_id());
				if(category !=null){
					ct.setCategory_id(category.getCategory_id());
					ct.setCategory_name(category.getName());
					vptList.add(ct);
				}
			}
		}
		return vptList;
	}
}