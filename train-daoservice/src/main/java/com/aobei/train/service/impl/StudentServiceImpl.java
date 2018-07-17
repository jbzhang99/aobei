package com.aobei.train.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.service.ProductService;
import com.aobei.train.service.StudentServiceitemService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.PartnerMapper;
import com.aobei.train.mapper.ServiceitemMapper;
import com.aobei.train.mapper.StudentMapper;
import com.aobei.train.model.Insurance;
import com.aobei.train.model.InsuranceExample;
import com.aobei.train.model.InsuranceRegister;
import com.aobei.train.model.InsuranceRegisterExample;
import com.aobei.train.model.Partner;
import com.aobei.train.model.PartnerExample;
import com.aobei.train.model.ProSku;
import com.aobei.train.model.Product;
import com.aobei.train.model.Serviceitem;
import com.aobei.train.model.ServiceitemExample;
import com.aobei.train.model.Station;
import com.aobei.train.model.Student;
import com.aobei.train.model.StudentExample;
import com.aobei.train.model.StudentServiceitem;
import com.aobei.train.model.StudentServiceitemExample;
import com.aobei.train.service.InsuranceRegisterService;
import com.aobei.train.service.InsuranceService;
import com.aobei.train.service.StudentService;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
public class StudentServiceImpl extends MbgServiceSupport<StudentMapper, Long, Student, Student, StudentExample> implements StudentService {

	
	@Autowired
	private ServiceitemMapper serviceitemMapper;
	
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private PartnerMapper partnerMapper;
    
    @Autowired
    private StudentServiceitemService studentServiceitemService;

	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private InsuranceRegisterService insuranceRegisterService;

	@Autowired
	private CacheReloadHandler cacheReloadHandler;

	@Autowired
	private ProductService productService;

    @Autowired
    private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory) {
        super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(studentMapper);
    }

	public List<Student> getExcelInfo(MultipartFile mfile) throws IOException {
		//获取文件完整名称
		String fileName = mfile.getOriginalFilename();
		List<Student> list = new ArrayList<Student>();

			/*if(!ValidateExcel(fileName)){
				return null;
			}*/
		boolean isExcel2003 = true;
		if (isExcel2007(fileName)) {
			isExcel2003 = false;
		}
		list = createExcel(mfile.getInputStream(), isExcel2003);

		return list;
	}

	/**
	 * 根据excel表里的内容读取数据域
	 *
	 * @param is          输入流
	 * @param isExcel2003 判断excel的版本信息
	 * @return
	 * @throws IOException
	 */
	public List<Student> createExcel(InputStream is, boolean isExcel2003) throws IOException {
		List<Student> list = null;

		Workbook wb = null;
		//读取excel表
		if (isExcel2003) {
			wb = new HSSFWorkbook(is);
		} else {
			//当excel是2007时，创建excel2007
			wb = new XSSFWorkbook(is);
		}
		list = readExcelValue(wb);

		return list;
	}

	/**
	 * 读取excel表内容，把数据对应添加到集合中，拿到数据集合
	 *
	 * @param wb
	 * @return
	 */
	public List<Student> readExcelValue(Workbook wb) {
		//得到第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		//得到excel的行数
		int totalRows = sheet.getPhysicalNumberOfRows();
		//得到excel的列数,先要判断行数不为空
		int totalCells = 0;
		if (totalRows > 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		//创建表对应的对象，循环excel的数据设置到对象里面插入数据库即可。
		List<Student> list = new ArrayList<Student>();
		//开始循环行数
		for (int r = 2; r < totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			Student student = new Student();
			student.setStudent_id(IdGenerator.generateId());
			//循环excel列
			for (int c = 0; c < totalCells; c++) {
				Cell cell = row.getCell(c);
				if (cell != null) {
					//开始设置对象属性
					if (c == 0) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							//第一列的数据
							student.setName(cell.getStringCellValue());
						}
					} else if (c == 1) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							if (cell.getStringCellValue().equals("女")) {
								student.setSex(0);
							} else {
								student.setSex(1);
							}
						}
					} else if (c == 2) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							//身份证号
							student.setIdentity_card(cell.getStringCellValue());
						}
					} else if (c == 3) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							//电话
							Long phone = (long) cell.getNumericCellValue();
							student.setPhone(phone.toString());
						}
					} else if (c == 4) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							//年龄
							String age = String.valueOf(cell.getNumericCellValue());
							String ages = age.substring(0, age.indexOf("."));
							student.setAge(Integer.parseInt(ages));
						}
					} else if (c == 5) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							//状态
							if (cell.getStringCellValue().equals("在职")) {
								student.setState(1);
							} else if (cell.getStringCellValue().equals("离职")) {
								student.setState(0);
							}
						}
					} else if (c == 6) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							//隶属合伙人
							//把导入的数据合伙人类型从字符串转化为long类型存储。要和相对应的合伙人信息表匹配
							PartnerExample partnerExample = new PartnerExample();
							partnerExample.or().andStateEqualTo(1);
							List<Partner> partner_list = partnerMapper.selectByExample(partnerExample);
							for (Partner partner : partner_list) {
								if (cell.getStringCellValue().equals(partner.getName())) {
									Long partner_id = partner.getPartner_id();
									student.setPartner_id(partner_id);
								}
							}
						}
					} else if (c == 7) {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							//籍贯
							student.setNative_place(cell.getStringCellValue());
						}
					}
				}
			}
			list.add(student);
		}
		return list;
	}

	/**
	 * 验证excel格式
	 *
	 * @param fileName
	 * @return
	 */
	public boolean ValidateExcel(String fileName) {
		if (fileName == null || isExcel2003(fileName) || isExcel2007(fileName)) {
			String errorMsg = "文件不是正确的excel名称";
			return false;
		}
		return true;
	}

	//
	//判断是否为2003的excel，返回true为是
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	//判断是否为2007的excel，返回true为是
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	/**
	 * 获得和station相关联的学员列表
	 * @param station
	 * @return
	 */
	private  List<Student> getStudentsByStation(Station station) {
		StudentExample example = new StudentExample();
		example.or().andPartner_idEqualTo(station.getPartner_id())
				.andStation_idEqualTo(station.getStation_id())
				.andStateEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		return selectByExample(example);
	}

	//@Cacheable(value = "getStudentByStationAndProduct",key = "'station:'+#station.station_id+':product:'+#product.product_id")
	@Override
	public List<Student> getStudentByStationAndProduct(Station station, Product product) {
		return  getStudentsByStation(station).stream().filter(t->{
			StudentServiceitemExample example = new StudentServiceitemExample();
			example.or().andServiceitem_idEqualTo(product.getServiceitem_id())
					.andStudent_idEqualTo(t.getStudent_id())
					.andStatusEqualTo(1);
			if(studentServiceitemService.countByExample(example)>0){
				return  true;
			}
			return false;
		}).collect(Collectors.toList());


	}

	@Override
	public Integer[] getStudentTaskSchedule(Student student, String dateTime) {
		// TODO: 2018/2/26 获取一个工作人员的具体工作调度缓存信息
		return new Integer[0];
	}

	@Override
	public List<Map<String, Integer>> countSku(Student student, ProSku sku, String dateTime) {
		// TODO: 2018/2/26 进行一个工作人员的工作时间是否可用的详细计算
		return null;
	}

	/**
	 * 学员列表
	 */
	@Override
	public Page<Student> xPackPage(Integer p, Integer ps, Integer sex_selected, Integer state_selected,
								   Integer grade_selected, Long partner_name_selected, Long serItem_selected,Long insurance_selected, String conditions) {
		// TODO Auto-generated method stub
		StudentExample studentExample = new StudentExample();
		studentExample.setOrderByClause(StudentExample.C.cdate+" desc");
		StudentExample.Criteria citeria=studentExample.or();
		//citeria.andDeletedEqualTo(Status.DeleteStatus.no.value);
		//筛选条件拼接
		if(sex_selected != 2){
			citeria.andSexEqualTo(sex_selected);
		}
		if(state_selected != 2){
			citeria.andStateEqualTo(state_selected);
		}
		if(grade_selected!=0){
			citeria.andGradeEqualTo(grade_selected);
		}
		if(partner_name_selected!=2){
			citeria.andPartner_idEqualTo(partner_name_selected);
		}
		if(serItem_selected !=0){
			StudentServiceitemExample studentServiceitemExample = new StudentServiceitemExample();
			studentServiceitemExample.or().andServiceitem_idEqualTo(serItem_selected).andStatusEqualTo(1);
			List<StudentServiceitem> sslist = studentServiceitemService.selectByExample(studentServiceitemExample);
			List<Long> list = new ArrayList<Long>();
			for (StudentServiceitem studentServiceitem : sslist) {
				list.add(studentServiceitem.getStudent_id());
			}
			if(list.size() != 0) {
				//说明有筛选出的服务项目
				citeria.andStudent_idIn(list);
			} else {
				//没有筛选出的服务项目
				citeria.andStudent_idIsNull();
			}
		}
		if(insurance_selected != 0){
            InsuranceRegisterExample insuranceRegisterExample = new InsuranceRegisterExample();
            insuranceRegisterExample.or().andInsurance_idEqualTo(insurance_selected);
            List<InsuranceRegister> insuranceRegisters = this.insuranceRegisterService.selectByExample(insuranceRegisterExample);
            List<Long> list = new ArrayList<Long>();
            insuranceRegisters.stream().forEach(insuranceRegister ->{
                list.add(insuranceRegister.getUid());
            });
            if(list.size() != 0){
                citeria.andStudent_idIn(list);
            }else{
                citeria.andStudent_idIsNull();
            }
        }
		if(conditions!=null){
			conditions = "%" + conditions + "%";
			citeria.andInnerOrLike(conditions,
					StudentExample.C.name,
					StudentExample.C.identity_card,
					StudentExample.C.phone,
					StudentExample.C.age,
					StudentExample.C.cdate);
		}

		Page<Student> page = this.selectByExample(studentExample,p,ps);
		return page;
	}


	/**
	 *  导入
	 */
	@Override
	@Transactional
	public HashMap<String, Object> xAddStudent(MultipartFile file) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		List<Student> excelInfo = new ArrayList<>();
		//学员集合
		StudentExample studentExample = new StudentExample();
		//studentExample.or().andStateEqualTo(1);
		List<Student> stuList = this.selectByExample(studentExample);
		//服务项目集合
		if(file != null && !file.isEmpty()){
			try {
				excelInfo=this.getExcelInfo(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			StringBuilder builder=new StringBuilder();
			StringBuilder cardSb=new StringBuilder();
			List<Student> longList=new ArrayList<>();

			for (Student student : excelInfo) {
				//验证学员是否存在
				for (Student stu : stuList) {
					if(student.getName().equals(stu.getName()) & student.getIdentity_card().equals(stu.getIdentity_card())){
						//builder.append(student.getName()+"、");
						if(stu.getState()==0){//判断是否是离职状态
							stu.setName(student.getName());
							stu.setPartner_id(student.getPartner_id());
							stu.setIdentity_card(student.getIdentity_card());
							stu.setPhone(student.getPhone());
							stu.setAge(student.getAge());
							stu.setState(student.getState());
							stu.setSex(student.getSex());
							stu.setNative_place(student.getNative_place());
							this.updateByPrimaryKeySelective(stu);
							this.cacheReloadHandler.my_employeeManagementReload(stu.getPartner_id());
							this.cacheReloadHandler.studentInfoByUserIdReload(stu.getUser_id());
							this.cacheReloadHandler.selectStuUndoneOrderReload(stu.getStudent_id());
							longList.add(student);//记录被修改的学员对象
						}
					}
				}
				//验证隶属合伙人信息
				if(student.getPartner_id()==null){
					map.put("result","excel导入失败,请核对隶属合伙人信息是否存在!");
					return map;
				}
				//验证身份证号码
				String cardCheck = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]";
				boolean flag = Pattern.matches(cardCheck,student.getIdentity_card());

				if(student.getIdentity_card().length()!=18 || flag==false){
					cardSb.append(student.getIdentity_card()+"、");
				}

			}
			if(builder.length() !=0){
				map.put("result","excel导入失败,学员信息("+builder.substring(0, builder.length()-1)+")已存在!");
				return map;
			}
			if(cardSb.length() !=0){
				map.put("result","excel导入失败,身份证号码信息("+cardSb.substring(0,cardSb.length()-1)+")存在错误，必须填写完整的身份证号!");
				return map;
			}
			if(cardSb.length() ==0){
				//从集合中去除已经进行修改过的学员
				if(longList.size()!=0){
					for (Student student : longList) {
						excelInfo.remove(student);
					}
				}
				//如果excle表里面的集合不为空，则进行新增
				if(excelInfo.size()!=0){
					for (Student student : excelInfo) {
						student.setCard_just(null);
						student.setCard_against(null);
						student.setHealth(null);
						//student.setState(1);
						student.setDeleted(Status.DeleteStatus.no.value);
						student.setCdate(new Date());
						int num = this.insert(student);
						//变更  清除合伙人缓存
						this.cacheReloadHandler.my_employeeManagementReload(student.getPartner_id());
					}
				}
				map.put("result", "excel导入成功");
			}else{
				map.put("result", "excel导入失败");
			}
		}
		return map;
	}

	/**
	 * 导出
	 */
	@Override
	public void xExportStudent(Sheet sheet, Row row, Cell cell) {
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到
		StudentExample studentExample = new StudentExample();
		studentExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		studentExample.setOrderByClause(StudentExample.C.cdate+" desc");
		List<Student> list = this.selectByExample(studentExample);
		//找到所有的合伙人
		List<Partner> pList = this.partnerMapper.selectByExample(new PartnerExample());
		//找到所有的学员和二级分类对应表
		List<StudentServiceitem> pfList = studentServiceitemService.selectByExample(new StudentServiceitemExample());
		//找到二级分类
		List<Serviceitem> perList = this.serviceitemMapper.selectByExample(new ServiceitemExample());
		for (int i = 0; i < list.size(); i++)
		{
			row = sheet.createRow((int) i + 1);
			Student stu = (Student) list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(stu.getName());
			for (Partner partner : pList) {
				if(stu.getPartner_id().toString().equals(partner.getPartner_id().toString())){
					row.createCell((short) 1).setCellValue(partner.getName());
				}
			}
			row.createCell((short) 2).setCellValue(stu.getIdentity_card());
			row.createCell((short) 3).setCellValue(stu.getPhone());
			if(stu.getSex()==1){
				row.createCell((short) 4).setCellValue("男");
			}else{
				row.createCell((short) 4).setCellValue("女");
			}

			row.createCell((short) 5).setCellValue(stu.getAge());

			cell = row.createCell((short) 6);
			cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(stu.getCdate()));

			if(stu.getState()==1){
				row.createCell((short) 7).setCellValue("在职");
			}else{
				row.createCell((short) 7).setCellValue("离职");
			}

			StringBuilder sb=new StringBuilder();

			for (StudentServiceitem serviceitem : pfList) {
				if(stu.getStudent_id().toString().equals(serviceitem.getStudent_id().toString())){
					for (Serviceitem ser : perList) {
						if(serviceitem.getServiceitem_id().toString().equals(ser.getServiceitem_id().toString())){
							sb.append(ser.getName()+",");
						}
					}
				}
			}
			if(sb.length()!=0){
				StringBuilder ss=sb.deleteCharAt(sb.length()-1);
				row.createCell((short) 8).setCellValue(ss.toString());
			}else{
				row.createCell((short) 8).setCellValue("");
			}

			if(stu.getGrade()== null){
				row.createCell((short) 9).setCellValue("");
			}else{
				if(stu.getGrade()==1){
					row.createCell((short) 9).setCellValue("高级");
				}else if(stu.getGrade()==2){
					row.createCell((short) 9).setCellValue("中级");
				}else{
					row.createCell((short) 9).setCellValue("低级");
				}
			}
		}
	}

	/**
	 * 查询没添加过的服务项目
	 */
	@Override
	public List<Serviceitem> xScreenServiceItem(String str) {
		List<Long> longs = JSONObject.parseArray(str, Long.class);
		if(longs.size() ==0){
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
			List<Serviceitem> list = this.serviceitemMapper.selectByExample(serviceitemExample);
			return list;
		}else{
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andServiceitem_idNotIn(longs).andDeletedEqualTo(Status.DeleteStatus.no.value);
			List<Serviceitem> list = this.serviceitemMapper.selectByExample(serviceitemExample);
			return list;
		}
	}

	
	/**
	 * 修改学员
	 */
	@Override
	@Transactional(timeout = 5)
	public void xEditStudent(Long student_id,Map<String, Object> map) {
		//先删除给学生对应的未具备的服务项目
		StudentServiceitemExample studentServiceitemExample = new StudentServiceitemExample();
		studentServiceitemExample.or().andTrain_plan_idIsNull().andStudent_idEqualTo(student_id);
		List<StudentServiceitem> list = studentServiceitemService.selectByExample(studentServiceitemExample);
		for (StudentServiceitem studentServiceitem : list) {
			studentServiceitemService.deleteByPrimaryKey(studentServiceitem);
		}
		//然后再进行新增
		List<Long> longs = JSONObject.parseArray(map.get("categoryAllList").toString(), Long.class);
		//如果没数据，就删除该学生对应的未具备的服务项目
		if(longs.size() ==0){
			//根据学员编号，在中间表找出所有的服务项目
			StudentServiceitemExample example=new StudentServiceitemExample();
			example.or().andStudent_idEqualTo(student_id);
			studentServiceitemService.deleteByExample(example);
		}
		//不为空
		for (Long i : longs) {
			StudentServiceitemExample example=new StudentServiceitemExample();
			example.or().andStudent_idEqualTo(student_id).andServiceitem_idEqualTo(i);
			studentServiceitemService.deleteByExample(example);
			//创建中间表对象
			StudentServiceitem serviceitem=new StudentServiceitem();
			serviceitem.setServiceitem_id(i);
			serviceitem.setStudent_id(student_id);
			serviceitem.setStatus(1);
			studentServiceitemService.insert(serviceitem);
		}

	}

	@Override
	@Transactional(timeout = 5)
	public void xAddStudent(Student student,Map<String, Object> map) {
		//然后再进行新增
		List<Long> longs = JSONObject.parseArray(map.get("categoryAllList").toString(), Long.class);
		//不为空
		for (Long i : longs) {
			//创建中间表对象
			StudentServiceitem serviceitem=new StudentServiceitem();
			serviceitem.setServiceitem_id(i);
			serviceitem.setStudent_id(student.getStudent_id());
			serviceitem.setStatus(1);
			studentServiceitemService.insert(serviceitem);
		}
		this.studentMapper.insert(student);
	}

	@Override
	@Transactional
	public List<Insurance> xScreenInsurance(String str, Long student_id) {
	    //已经存在的保险id集合
		List<Long> longs = JSONObject.parseArray(str, Long.class);
        Student student = this.studentMapper.selectByPrimaryKey(student_id);
        InsuranceExample insuranceExample = new InsuranceExample();
        InsuranceExample.Criteria criteria = insuranceExample.or();
        List<Insurance> insuranceList = new ArrayList<>();
        //如果没有，就查询所有的
		if(longs.size() ==0){
            insuranceList= this.insuranceService.selectByExample(insuranceExample);
		}else{//有，就查询出除此之外的
            criteria.andInsurance_idNotIn(longs);
            insuranceList = this.insuranceService.selectByExample(insuranceExample);
		}
		return insuranceList;
	}


    @Override
    @Transactional(timeout = 5)
    public int xAddInsurance(String str, Long student_id) {
	    //先删除之前的数据
        InsuranceRegisterExample insuranceRegisterExample = new InsuranceRegisterExample();
        insuranceRegisterExample.or().andUidEqualTo(student_id);
        this.insuranceRegisterService.deleteByExample(insuranceRegisterExample);

        //在进行新增
        List<Long> list = JSONArray.parseArray(str,Long.class);
        int num=0;
        if(list.size()!=0){
            list.stream().forEach(id ->{
                //根据保险的id找到对象
                Insurance insurance = this.insuranceService.selectByPrimaryKey(id);
                InsuranceRegister insuranceRegister=new InsuranceRegister();
                insuranceRegister.setInsurance_register_id(IdGenerator.generateId());
                insuranceRegister.setInsurance_id(id);
                insuranceRegister.setInsurance_name(insurance.getInsurance_name());
                insuranceRegister.setUid(student_id);
                insuranceRegister.setType(1);
                insuranceRegister.setCreate_datetime(new Date());
                insuranceRegister.setStart_datetime(insurance.getStart_datetime());
                insuranceRegister.setEnd_datetime(insurance.getEnd_datetime());
                this.insuranceRegisterService.insert(insuranceRegister);
            });
			num++;
        }
        return num;
    }
}