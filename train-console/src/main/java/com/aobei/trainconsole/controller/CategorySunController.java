package com.aobei.trainconsole.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.aobei.trainconsole.util.PathUtil.PathType;

@Controller
@RequestMapping("/categorySun")
public class CategorySunController {

	private static final Logger logger= LoggerFactory.getLogger(CategorySunController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ServiceitemService serviceitemService;
	
	@Autowired
	private MyFileHandleUtil myfileHandleUtil;
	
	@Autowired
	private OssImgService ossImgService;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CacheReloadHandler cacheReloadHandler;
	//递归
	/*public void ndata(Long pid,Map<String,Object> returnData,List<Category> allData,List<Object> pattr,boolean root){
		List<Map<String,Object>> dMap = new ArrayList<>();
		for (Category category: allData) {
			if(root && pid.equals(category.getPid())){
				List<Object> pattrList = new ArrayList<>();
				pattrList.addAll(pattr);
				pattrList.add(pid);
				pattrList.add(category.getCategory_id());
				Map<String,Object> mapone=new HashMap<>();
				mapone.put("id",category.getCategory_id().toString());
				mapone.put("name",category.getName());
				//mapone.put("upTime",category.getErcategory_update());
				//mapone.put("downTime",category.getErcategory_downdate());
				mapone.put("status",category.getState());
				mapone.put("arr",pattrList);
				mapone.put("pid",category.getCategory_id()+"");
				
				ndata(category.getCategory_id(), mapone, allData,pattrList ,true);
				ServiceitemExample serviceitemExample = new ServiceitemExample();
				serviceitemExample.or().andCategory_idEqualTo(category.getCategory_id());
				List<Serviceitem> serList = this.serviceitemService.selectByExample(serviceitemExample);
				if(serList.size()!=0){
					//根据每个分类找到对应的服务项目
					List<Map<String,Object>> cMap = new ArrayList<>();
					for (Serviceitem serviceitem : serList) {
						List<Object> childList = new ArrayList<>();
						for (Object object : pattrList) {
							childList.add(object);
						}
						childList.add(serviceitem.getServiceitem_id());
						Map<String,Object> map=new HashMap<>();
						map.put("id",serviceitem.getServiceitem_id().toString());
						map.put("name",serviceitem.getName());
						//mapone.put("upTime",category.getErcategory_update());
						//mapone.put("downTime",category.getErcategory_downdate());
						map.put("status",serviceitem.getState());
						map.put("arr",childList);
						map.put("pid",serviceitem.getCategory_id()+"");
						cMap.add(map);
					}
					mapone.put("child", cMap);
				}else{
					mapone.put("child", pattr);
				}
				dMap.add(mapone);
			}
		}
		returnData.put("child", dMap);
	}*/
	
	/**
	 * 显示分类
	 * @param model
	 * @return
	 */
	@RequestMapping("/showCategorySun")
	public String showCategorySun(Model model){
		List<Map<String,Object>> list=new ArrayList<>();
		
		CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andDeletedEqualTo(0);
		List<Category> categoryList = this.categoryService.selectByExample(categoryExample);
		
		ServiceitemExample serviceitemExample = new ServiceitemExample();
		serviceitemExample.or().andDeletedEqualTo(0);
		List<Serviceitem> serviceItemList = this.serviceitemService.selectByExample(serviceitemExample);
		
		buildTree(list, categoryList, serviceItemList, null, new ArrayList<>());
		model.addAttribute("list", list);
		return "category/category_list";
	}
	
	public void buildTree(List<Map<String,Object>> list,List<Category> categoryList,List<Serviceitem> serviceItemList,Long pid,List<Object> pattr){
		boolean root = pid == null;
		categoryList.stream()
					.filter(category -> pid == null ? category.getPid() == null : pid.equals(category.getPid()))
					.forEach(category -> {
						List<Object> pattrList = new ArrayList<>();
						pattrList.addAll(pattr);
						pattrList.add(category.getCategory_id()+"");
						Map<String,Object> mapone = new HashMap<>();
						mapone.put("id",category.getCategory_id().toString());
						mapone.put("name",category.getName());
						mapone.put("pid",root ? "a" : category.getPid());
						mapone.put("status",category.getState());
						mapone.put("actived",category.getActived());
						mapone.put("arr",root ? new ArrayList<>():pattrList);
						//ndata(category.getCategory_id(), mapone, Clist,listNull,true);
						List<Map<String,Object>> tempList = new ArrayList<>();
						buildTree(tempList, categoryList, serviceItemList, category.getCategory_id(), pattrList);
						serviceItemList.stream()
									   .filter(serviceItem -> serviceItem.getCategory_id().equals(category.getCategory_id()))
									   .forEach(serviceItem-> {
											List<Object> siPattrList = new ArrayList<>();
											siPattrList.addAll(pattrList);
											siPattrList.add(serviceItem.getServiceitem_id()+"");
											Map<String, Object> serviceItemMap = new HashMap<>();
											serviceItemMap.put("id", serviceItem.getServiceitem_id() + "");
											serviceItemMap.put("name", serviceItem.getName());
											serviceItemMap.put("pid", serviceItem.getCategory_id() + "");
											serviceItemMap.put("status", serviceItem.getState());
											serviceItemMap.put("arr", siPattrList);
											serviceItemMap.put("serviceItem", 1);
											tempList.add(serviceItemMap);
									   });
						
						mapone.put("child", tempList);
						list.add(mapone);
					});
	}
	
	
	/**
	 * 新增分类
	 * @return
	 */
	@RequestMapping("/addCategorySun")
	@Transactional
	//@ResponseBody
	public Object addCategorySun(String name,String descr,String pid,MultipartFile logo,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[addCategorySun] U[{}] ,params name:{},descr:{}, pid:{}, logo:{}",
				users.getUser_id(),name,descr,pid,logo);
		Map<String, String> logoimg = myfileHandleUtil.file_upload(logo,PathType.image_category_logo);
		//封装 （新增分类）
		int num=this.categoryService.xAddCategorySun(logoimg, name, descr, pid);
		logger.info("M[categorySun] F[addCategorySun] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");

		return "redirect:/categorySun/showCategorySun";
	}
	
	
	/**
	 * 分类删除
	 * @param category_id
	 * @return
	 */
	@RequestMapping("/delCategory/{category_id}")
	@ResponseBody
	@Transactional
	public Object delCategory(@PathVariable(value="category_id") String category_id,Authentication authentication){
		long cl_id = Long.parseLong(category_id);
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[delCategory] U[{}] ,params category_id:{}",
				users.getUser_id(),category_id);
		//封装（根据分类id进行删除）
		int num = this.categoryService.xDelCategory(cl_id);
		logger.info("M[categorySun] F[delCategory] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("分类信息删除%s!", num> 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 服务项目下线
	 * @return
	 */
	@RequestMapping("/downServiceItem/{service_id}")
	@ResponseBody
	@Transactional
	public Object downServiceItem(@PathVariable(value="service_id") String service_id,Authentication authentication){
		long se_id = Long.parseLong(service_id);
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[downServiceItem] U[{}] ,params service_id:{}",
				users.getUser_id(),service_id);
		//封装（根据服务项目id进行下线）
		int num =this.categoryService.xDownServiceItem(se_id);
		logger.info("M[categorySun] F[downServiceItem] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("服务项目信息下线%s!", num> 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 服务项目上线
	 * @param service_id
	 * @return
	 */


	@RequestMapping("/upServiceItem/{service_id}")
	@ResponseBody
	public Object upServiceItem(@PathVariable(value="service_id") String service_id,Authentication authentication){
		long se_id = Long.parseLong(service_id);
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[upServiceItem] U[{}] ,params service_id:{}",
				users.getUser_id(),service_id);
		//封装（根据服务项目id进行上线）
		int num = this.categoryService.xUpServiceItem(se_id);
		logger.info("M[categorySun] F[upServiceItem] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("服务项目信息上线%s!", num> 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 父级分类跳转编辑页面
	 * @param category_id
	 * @param model
	 * @return
	 */
	@RequestMapping("/showCategoryEdit/{category_id}")
	public String showCategoryEdit(@PathVariable(value="category_id") String category_id,Model model){
		//根据category_id查找对象
		long cl_id = Long.parseLong(category_id);
		Category category = this.categoryService.selectByPrimaryKey(cl_id);
		model.addAttribute("category", category);
		//返回对象，进行模态框数据回显
		return "category/category_cateEdit";
	}
	
	/**
	 * 分类修改
	 * @return
	 */
	@RequestMapping("/editCategory")
	@Transactional
	public String editCategory(Category category,MultipartFile category_logo,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[editCategory] U[{}] ,params category:{},category_logo:{}",
				users.getUser_id(),category,category_logo);
		//根据category_id查找对象
		Category Oldcategory = this.categoryService.selectByPrimaryKey(category.getCategory_id());
		String logo_name = category_logo.getOriginalFilename();
		if(logo_name.equals("") || category_logo==null){
			category.setLogo(Oldcategory.getLogo());
			category.setLogo_id(Oldcategory.getLogo_id());
		}else{
			//显示切换了图片，先将原来的图片删除
			if(Oldcategory.getLogo()!=null){
				OssImg LogoossImg = this.ossImgService.selectByPrimaryKey(Oldcategory.getLogo_id());
				this.ossImgService.deleteByPrimaryKey(LogoossImg.getOss_img_id());
			}
			//设置要上传的bucket
			Map<String, String> logoimg = myfileHandleUtil.file_upload(category_logo,PathType.image_category_logo);
			//添加分类logo
			String effect="分类logo";
			String privileges="public";
			OssImg logoImg = this.ossImgService.xInsertOssImg(logoimg,effect,privileges);
			category.setLogo(logoImg.getUrl());
			category.setLogo_id(logoImg.getOss_img_id());
		}
		int num = this.categoryService.updateByPrimaryKeySelective(category);
		logger.info("M[categorySun] F[editCategory] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");

		//去除掉之前的缓存
		this.cacheReloadHandler.homeCategoryListReload();
		return "redirect:/categorySun/showCategorySun";
	}
	
	
	/**
	 * 服务项目编辑页面跳转
	 * @param model
	 * @return
	 */
	@RequestMapping("/editServiceItemShow/{serviceitem_id}")
	public String editServiceItemShow(@PathVariable(value="serviceitem_id") Long serviceitem_id,Model model){
		//根据id找到对象
		Serviceitem serviceitem = this.serviceitemService.selectByPrimaryKey(serviceitem_id);
		model.addAttribute("serviceitem", serviceitem);
		return "category/category_serEdit";
	}
	
	
	/**
	 * 服务项目编辑功能
	 * @return
	 */
	@RequestMapping("/editServiceItem")
	@ResponseBody
	@Transactional
	public Object editServiceItem(Serviceitem serviceitem,String onlineDate,String offlineDate,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[editServiceItem] U[{}] ,params serviceitem:{}",
				users.getUser_id(),serviceitem);
		serviceitem.setOnline_date(null);
		serviceitem.setOffline_date(null);
		int num = this.serviceitemService.updateByPrimaryKeySelective(serviceitem);
		logger.info("M[categorySun] F[editServiceItem] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("服务项目信息修改%s!", num> 0 ? "成功" : "失败"));
		return map;
	}
	
	
	/**
	 * 服务项目跳转添加页面
	 * @param category_id
	 * @param model
	 * @return
	 */
	@RequestMapping("/addServiceitemShow/{category_id}")
	public String addServiceitemShow(@PathVariable(value="category_id") Long category_id,Model model){
		model.addAttribute("category_id", category_id);
		return "category/category_serAdd";
	}
	
	/**
	 * 服务项目添加功能
	 */
	@RequestMapping("/addServiceItem")
	@ResponseBody
	public Object addServiceItem(Serviceitem serviceitem,String onlineDate,String offlineDate,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[addServiceItem] U[{}] ,params serviceitem:{}",
				users.getUser_id(),serviceitem);
		//暂时不需要上下线时间
		//封装（添加服务项）
		int num = this.categoryService.xAddServiceItem(serviceitem);
		logger.info("M[categorySun] F[addServiceItem] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("服务项目添加%s!", num> 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 分类下线
	 * @param category_id
	 * @return
	 */
	@RequestMapping("/downCategory/{category_id}")
	@ResponseBody
	@Transactional
	public Object downCategory(@PathVariable(value="category_id") Long category_id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[downCategory] U[{}] ,params category_id:{}",
				users.getUser_id(),category_id);
		Category category = this.categoryService.selectByPrimaryKey(category_id);
		category.setState(0);//下线
		//封装（根据分类id 将其关联的所有子分类，服务项，产品全部下线）
		int num = this.categoryService.xDownCategory(category);
		logger.info("M[categorySun] F[downCategory] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("分类信息下线%s!", num> 0 ? "成功" : "失败"));
		//去除掉之前的缓存
		this.cacheReloadHandler.homeCategoryListReload();
		this.cacheReloadHandler.productListByCategoryLevelCode();
		return map;
	}
	
	
	/**
	 * 分类上线
	 * @param category_id
	 * @return
	 */
	@RequestMapping("/upCategory/{category_id}")
	@ResponseBody
	@Transactional
	public Object upCategory(@PathVariable(value="category_id") Long category_id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[categorySun] F[upCategory] U[{}] ,params category_id:{}",
				users.getUser_id(),category_id);
		Category category = this.categoryService.selectByPrimaryKey(category_id);
		//封装（根据分类id 其关联的子分类，服务项，产品全部上线）
		int num = this.categoryService.xUpCategory(category);
		logger.info("M[categorySun] F[upCategory] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("分类信息上线%s!", num> 0 ? "成功" : "失败"));
		//去除掉之前的缓存
		this.cacheReloadHandler.homeCategoryListReload();
		this.cacheReloadHandler.productListByCategoryLevelCode();
		return map;
	}
	
	
	
}
