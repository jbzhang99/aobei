package com.aobei.train.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.Category;
import com.aobei.train.IdGenerator;
import com.aobei.train.LevelId;
import com.aobei.train.mapper.CategoryMapper;
import com.aobei.train.mapper.OssImgMapper;
import com.aobei.train.mapper.ProductMapper;
import com.aobei.train.mapper.ServiceitemMapper;
import com.aobei.train.model.CategoryExample;
import com.aobei.train.model.OssImg;
import com.aobei.train.model.Product;
import com.aobei.train.model.ProductExample;
import com.aobei.train.model.Serviceitem;
import com.aobei.train.model.ServiceitemExample;
import com.aobei.train.service.CategoryService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
public class CategoryServiceImpl extends MbgServiceSupport<CategoryMapper, Long, Category, Category, CategoryExample> implements CategoryService{

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private OssImgMapper ossImgMapper;
	
	@Autowired
	private ServiceitemMapper serviceitemMapper;
	
	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(categoryMapper);
	}

	@Override
	public String selectMaxCategoryId() {
		 return categoryMapper.selectMaxCategoryId();
	}
	
	@Override
	public String selectPidFindMaxSonId(String pid) {
		return categoryMapper.selectPidFindMaxSonId(pid);
	}

	/**
	 * 添加分类
	 */
	@Override
	@Transactional(timeout = 5)
	public int xAddCategorySun(Map<String, String> logoimg, String name, String descr,String pid) {
		// TODO Auto-generated method stub
		//添加分类logo
		OssImg logoImg=new OssImg();
		logoImg.setOss_img_id(IdGenerator.generateId());
		logoImg.setFormat(logoimg.get("file_format"));
		logoImg.setName(logoimg.get("file_name"));
		logoImg.setUrl(logoimg.get("file_url"));
		logoImg.setBucket(logoimg.get("bucketName"));
		logoImg.setEffect("分类logo");
		logoImg.setAccess_permissions("public");
		this.ossImgMapper.insertSelective(logoImg);
		int num=0;
		if("".equals(pid) || pid==null){//一级分类
			//pid为空表示进行一级分类添加
			Category category=new Category();
			category.setCategory_id(IdGenerator.generateId());
			String MaxCategory_id=this.selectMaxCategoryId();
			if(MaxCategory_id==null){
				LevelId levelId = new LevelId(4,"0000");
				category.setCategory_level_code(levelId.next());
			}else{
				LevelId levelId = new LevelId(4,MaxCategory_id);
				category.setCategory_level_code(levelId.next());
			}
			
			category.setName(name);
			category.setDescr(descr);
			category.setCdate(new Date());
			category.setDeleted(Status.DeleteStatus.no.value);
			category.setState(0);//下线
			category.setActived(0);
			category.setLogo(logoImg.getUrl());
			category.setLogo_id(logoImg.getOss_img_id());
			num=this.insert(category);
		}else{
			//pid不为空表示进行多级分类添加
			Category category=new Category();
			category.setCategory_id(IdGenerator.generateId());
			String sonMaxId=this.selectPidFindMaxSonId(pid);
			if(sonMaxId==null){
				Category cg = this.selectByPrimaryKey(Long.parseLong(pid));
				LevelId levelId = new LevelId(4,cg.getCategory_level_code());
				category.setCategory_level_code(levelId.nextChildren());
			}else{
				LevelId levelId = new LevelId(4,sonMaxId);
				category.setCategory_level_code(levelId.next());
			}
			category.setName(name);
			category.setDescr(descr);
			category.setCdate(new Date());
			category.setDeleted(Status.DeleteStatus.no.value);
			category.setState(0);
			category.setActived(0);
			category.setPid(Long.parseLong(pid));
			category.setLogo(logoImg.getUrl());
			category.setLogo_id(logoImg.getOss_img_id());
			num=this.insert(category);
		}
		return num;
	}

	
	/**
	 * 根据分类id进行删除
	 */
	@Override
	@Transactional(timeout = 5)
	public int xDelCategory(long cl_id) {
		
		Category category = this.selectByPrimaryKey(cl_id);
		category.setState(0);//下线
		category.setDeleted(Status.DeleteStatus.yes.value);//已删除
		
		//对应的子分类也删除,并且子类对应的服务项目也删除
		CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andCategory_level_codeLike(category.getCategory_level_code()+"%");
		List<Category> cList = this.selectByExample(categoryExample);
		for (Category cate : cList) {
			cate.setDeleted(Status.DeleteStatus.yes.value);
			cate.setState(0);
			this.updateByPrimaryKey(cate);
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andCategory_idEqualTo(cate.getCategory_id());
			List<Serviceitem> serList = this.serviceitemMapper.selectByExample(serviceitemExample);
			for (Serviceitem serviceitem : serList) {
				serviceitem.setState(0);
				serviceitem.setDeleted(Status.DeleteStatus.yes.value);
				this.serviceitemMapper.updateByPrimaryKey(serviceitem);
			}
		}
		
		//对应的服务项目也删除
		ServiceitemExample serviceitemExample = new ServiceitemExample();
		serviceitemExample.or().andCategory_idEqualTo(cl_id);
		List<Serviceitem> serList = this.serviceitemMapper.selectByExample(serviceitemExample);
		for (Serviceitem serviceitem : serList) {
			serviceitem.setState(0);
			serviceitem.setDeleted(Status.DeleteStatus.yes.value);
			this.serviceitemMapper.updateByPrimaryKey(serviceitem);
		}
		
		//删除分类
		int num = this.updateByPrimaryKey(category);
		return num;
	}

	/**
	 * 服务项目下线
	 */
	@Override
	@Transactional(timeout = 5)
	public int xDownServiceItem(long se_id) {
		Serviceitem serviceitem = this.serviceitemMapper.selectByPrimaryKey(se_id);
		serviceitem.setState(0);//下线
		serviceitem.setOffline_date(new Date());//下线时间
		int num = this.serviceitemMapper.updateByPrimaryKey(serviceitem);
		return num;
	}

	/**
	 * 服务项目上线
	 */
	@Override
	@Transactional(timeout = 5)
	public int xUpServiceItem(long se_id) {
		Serviceitem serviceitem = this.serviceitemMapper.selectByPrimaryKey(se_id);
		serviceitem.setState(1);//上线
		serviceitem.setOnline_date(new Date());;//上线时间
		int num = this.serviceitemMapper.updateByPrimaryKey(serviceitem);
		return num;
	}

	/**
	 * 分类下线
	 * @param category
	 * @return
	 */
	@Override
	@Transactional(timeout = 5)
	public int xDownCategory(Category category) {
		//找到其同一分类下的所有分类
		CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andCategory_level_codeLike(category.getCategory_level_code()+"%");
		List<Category> categoryList = this.selectByExample(categoryExample);
		
		for (Category cg : categoryList) {
			cg.setState(0);
			//找到分类下的服务项目
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andCategory_idEqualTo(cg.getCategory_id());
			List<Serviceitem> serList = this.serviceitemMapper.selectByExample(serviceitemExample);
			for (Serviceitem serviceitem : serList) {
				serviceitem.setState(0);//服务项下线
				this.serviceitemMapper.updateByPrimaryKey(serviceitem);
				
			}
			//找到分类下的产品
			ProductExample productExample = new ProductExample();
			productExample.or().andCategory_level_codeLike(cg.getCategory_level_code()+"%");
			List<Product> productList = this.productMapper.selectByExample(productExample);
			for (Product product : productList) {
				product.setOnline(0);
				this.productMapper.updateByPrimaryKey(product);
			}
			this.updateByPrimaryKey(cg);
		}
		int num = this.updateByPrimaryKey(category);
		return num;
	}

	
	/**
	 * 分类上线
	 * @param category
	 * @return
	 */
	@Override
	@Transactional(timeout = 5)
	public int xUpCategory(Category category) {
		category.setState(1);//上线
		category.setActived(1);//点击过上线按钮
		//找到同一分类下的所有分类
		CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andCategory_level_codeLike(category.getCategory_level_code()+"%");
		List<Category> categoryList = this.selectByExample(categoryExample);
		
		for (Category cg : categoryList) {
			cg.setState(1);
			cg.setActived(1);
			//找到分类下的服务项目
			ServiceitemExample serviceitemExample = new ServiceitemExample();
			serviceitemExample.or().andCategory_idEqualTo(cg.getCategory_id());
			List<Serviceitem> serList = this.serviceitemMapper.selectByExample(serviceitemExample);
			for (Serviceitem serviceitem : serList) {
				serviceitem.setState(1);//服务项下线
				this.serviceitemMapper.updateByPrimaryKey(serviceitem);
			}
			//找到分类下的产品
			ProductExample productExample = new ProductExample();
			productExample.or().andCategory_level_codeLike(cg.getCategory_level_code()+"%");
			List<Product> productList = this.productMapper.selectByExample(productExample);
			for (Product product : productList) {
				product.setOnline(1);
				product.setActived(1);
				this.productMapper.updateByPrimaryKey(product);
			}
			this.updateByPrimaryKey(cg);
			
		}
		int num = this.updateByPrimaryKey(category);
		return num;
	}

	@Override
	@Transactional(timeout = 5)
	public int xAddServiceItem(Serviceitem serviceitem) {
		serviceitem.setServiceitem_id(IdGenerator.generateId());
		serviceitem.setOnline_date(null);
		serviceitem.setOffline_date(null);
		serviceitem.setState(0);
		serviceitem.setDeleted(Status.DeleteStatus.no.value);
		int num = this.serviceitemMapper.insert(serviceitem);
		return num;
	}

}