package com.aobei.trainconsole.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.ProductTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aobei.train.IdGenerator;
import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.aobei.trainconsole.util.PathUtil.PathType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Constant;

@Controller
@RequestMapping("/product")
public class ProductController {

	private static final Logger logger= LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ServiceitemService serviceitemService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired
	private OssImgService ossImgService;
	
	@Autowired
	private ProSkuService proSkuService;
	
	@Autowired
	private PartnerService partnerService;
	
	@Autowired
	private ProductSoleService productSoleService;
	
	@Autowired
	private BespeakService bespeakService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CacheReloadHandler cacheReloadHandler;

	@Autowired
	private CancleStrategyService cancleStrategyService;
	/**
	 * 显示分类
	 * @param model
	 * @return
	 */
	@RequestMapping("/showProductCategory")
	public String showProductCategory(Model model){
		CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andPidIsNull().andDeletedEqualTo(0);
		List<Category> categoryList = this.categoryService.selectByExample(categoryExample);
		model.addAttribute("categoryList", categoryList);
		return "product/product_add";
	}
	
	/**
	 * 显示分类及服务项目
	 * @param cid
	 * @return
	 */
	@RequestMapping("/selectCategory/{cid}")
	@ResponseBody
	public Object selectCategory(@PathVariable(value="cid") Long cid){
		List<Map<String,Object>> mapList=this.productService.xSelectCategory(cid);
		return mapList;
	}
	
	/**
	 * 跳转到添加商品页面
	 * @param serid
	 * @param model
	 * @return
	 */
	@RequestMapping("/showCategoryNext/{serid}")
	public String showCategoryNext(@PathVariable(value="serid") Long serid,Model model){
		//根据服务项id找到服务对象
		Serviceitem serviceitem = this.serviceitemService.selectByPrimaryKey(serid);
		//在找到该服务项对应的分类
		Category category = this.categoryService.selectByPrimaryKey(serviceitem.getCategory_id());
        //取消策略
        CancleStrategyExample cancleStrategyExample = new CancleStrategyExample();
        cancleStrategyExample.or().andDeletedEqualTo(0);
        List<CancleStrategy> cancleStrategies = cancleStrategyService.selectByExample(cancleStrategyExample);
        model.addAttribute("cancleStrategies",cancleStrategies);
		model.addAttribute("category", category);
		model.addAttribute("serviceitem", serviceitem);
		
		return "product/product_next";
	}
	
	/**
	 * 添加商品
	 * @param model
	 * @param product
	 * @param commodity_img
	 * @return
	 */
	@RequestMapping("/addCommodityProduct")
	@Transactional
	public String addCommodityProduct(Model model,Product product,MultipartFile commodity_img,MultipartFile little_image,/**String ueditorContext,**/Authentication authentication,String uploadControllerList){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[addCommodityProduct] U[{}] ,params 产品对象数据product:{}, 产品图片commodity_img:{},uploadControllerList:{}",
				users.getUser_id(),product,commodity_img,uploadControllerList);
		Map<String, String> params=new HashMap<>();
		if(!("".equals(commodity_img.getOriginalFilename())) ){
			 params =this.myFileHandleUtil.file_upload(commodity_img, PathType.image_product_logo);
		}

		Map<String, String> littleParams=new HashMap<>();
		if(!("".equals(little_image.getOriginalFilename())) ){
			littleParams =this.myFileHandleUtil.file_upload(little_image, PathType.image_productLite_logo);
		}
		//封装   新增商品  (商品对象，图片，富文本内容)
		int num = this.productService.xAddCommodityProduct(product, params,littleParams,uploadControllerList);
		logger.info("M[product] F[addCommodityProduct] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		List<Bespeak> bespeakList= this.bespeakService.selectByExample(new BespeakExample());
		model.addAttribute("bespeakList", bespeakList);
		model.addAttribute("product", product);
		//根据服务项id找到服务对象
		return "product/psku_add";
	}
	
	/**
	 * 添加sku
	 * @param product_id
	 * @param request
	 * @return
	 */
	@RequestMapping("/addSku/{product_id}")
	@ResponseBody
	@Transactional
	public Object addSku(@PathVariable(value="product_id") Long product_id,HttpServletRequest request,Authentication authentication){
		//获取前台传过来的json
		String str = request.getParameter("result");
		String unsetStr = request.getParameter("unsetSkuList");
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[addSku] U[{}] ,params request参数包含：固定时长Sku集合 str:{},不固定时长Sku集合 unsetStr:{}",
				users.getUser_id(),str,unsetStr);
		//封装（添加sku）
		int num=this.productService.xAddSku(product_id,str,unsetStr);
		logger.info("M[product] F[addSku] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		//清除掉之前的缓存
		this.cacheReloadHandler.homeProductListReload();
		this.cacheReloadHandler.productDetailReload(product_id);
		this.cacheReloadHandler.proSkuListReload(product_id);
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("产品信息添加%s!", num> 0 ? "成功" : "失败"));
		return map;
		
	}
	
	/**
	 * 列表展示页
	 * @param model
	 * @param p
	 * @param ps
	 * @return
	 */
	@RequestMapping("/showProductList")
	public String showProductList(Model model,@RequestParam(value="p",defaultValue="1") Integer p,@RequestParam(value="ps",defaultValue="10") Integer ps,
			@RequestParam(defaultValue="0") Long category_name_selected,
			@RequestParam(defaultValue="0") Long serviceItem_name_selected,
			@RequestParam(defaultValue="2") Integer online_selected,
			String name){
		//所有商品集合+筛选条件
		ProductExample productExample = new ProductExample();
		productExample.setOrderByClause(ProductExample.C.create_datetime+" desc");
		ProductExample.Criteria citeria=productExample.or();
		citeria.andDeletedEqualTo(0);
		if(category_name_selected !=0){
			citeria.andCategory_idEqualTo(category_name_selected);
		}
		if(serviceItem_name_selected !=0){
			citeria.andServiceitem_idEqualTo(serviceItem_name_selected);
		}
		if(online_selected !=2){
			citeria.andOnlineEqualTo(online_selected);
		}
		if(name !=null){
			name = "%" + name + "%";
			citeria.andInnerOrLike(name,
					ProductExample.C.name);
		}
		Page<Product> page = this.productService.selectByExample(productExample,p,ps);
		List<Product> productList = page.getList();
		//所有图片集合
		List<OssImg> ossImgList = this.ossImgService.selectByExample(new OssImgExample());
		model.addAttribute("productList", productList);
		model.addAttribute("page", page);
		model.addAttribute("ossImgList", ossImgList);
		CategoryExample categoryExample = new CategoryExample();
		categoryExample.or().andDeletedEqualTo(0);
		List<Category> categoryList = this.categoryService.selectByExample(categoryExample);
		model.addAttribute("categoryList", categoryList);
		
		//返回筛选条件的内容
		model.addAttribute("category_name_selected", category_name_selected);
		model.addAttribute("serviceItem_name_selected", serviceItem_name_selected);
		model.addAttribute("online_selected", online_selected);
		if(name !=null){
			model.addAttribute("name", name.replace("%",""));
		}
		
		return "product/product_list";
	}
	
	/**
	 * 下架商品
	 * @return
	 */
	@RequestMapping("/downProduct/{product_id}")
	@ResponseBody
	@Transactional
	public Object downProduct(@PathVariable(value="product_id") Long product_id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[downProduct] U[{}] ,params product_id:{}",
				users.getUser_id(),product_id);
		//封装（添加sku）

		//封装 （根据产品id进行产品下线）
		int num = this.productService.xDownProduct(product_id);
		logger.info("M[product] F[downProduct] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("产品信息下架%s!", num> 0 ? "成功" : "失败"));
		//清除掉之前的缓存
		this.cacheReloadHandler.homeProductListReload();
		this.cacheReloadHandler.productListByCategoryLevelCode();
		this.cacheReloadHandler.productDetailReload(product_id);
		return map;
	}
	
	/**
	 * 上架商品
	 * @return
	 */
	@RequestMapping("/upProduct/{product_id}")
	@ResponseBody
	@Transactional
	public Object upProduct(@PathVariable(value="product_id") Long product_id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[upProduct] U[{}] ,params product_id:{}",
				users.getUser_id(),product_id);
		//先根据id找出其包含的sku
		ProSkuExample proSkuExample = new ProSkuExample();
		proSkuExample.or().andProduct_idEqualTo(product_id);
		List<ProSku> skuList = this.proSkuService.selectByExample(proSkuExample);
		Map<String, String> map = new HashMap<>();
		if(skuList.size()!=0){
			//封装  （根据产品id进行产品上线）
			int num = this.productService.xUpProduct(product_id);
			logger.info("M[product] F[upProduct] U[{}] ,execute result:{}",
					users.getUser_id(),num>0?"成功":"失败");
			map.put("msg", String.format("产品信息上架%s!", num> 0 ? "成功" : "失败"));
		}
		//清除掉之前的缓存
		this.cacheReloadHandler.homeProductListReload();
		this.cacheReloadHandler.productListByCategoryLevelCode();
		this.cacheReloadHandler.productDetailReload(product_id);
		return map;
	}
	
	/**
	 * 删除产品
	 * @param product_id
	 * @return
	 */
	@RequestMapping("/delProduct/{product_id}")
	@ResponseBody
	@Transactional
	public Object delProduct(@PathVariable(value="product_id") Long product_id,Authentication authentication){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[delProduct] U[{}] ,params product_id:{}",
				users.getUser_id(),product_id);
		//封装   （根据产品id进项产品删除）
		int num = this.productService.xDelProduct(product_id);
		logger.info("M[product] F[delProduct] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("产品信息删除%s!", num> 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 商品编辑页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/editProductShow/{product_id}/{pageNo}")
	public String editProductShow(@PathVariable(value="product_id") Long product_id,@PathVariable(value="pageNo") Long pageNo,Model model){
		Product product = this.productService.selectByPrimaryKey(product_id);
		//找到服务项
		Serviceitem serviceitem = this.serviceitemService.selectByPrimaryKey(product.getServiceitem_id());
		//在找到分类
		Category category = this.categoryService.selectByPrimaryKey(serviceitem.getCategory_id());
		if(product.getLite_image()!=null){
			Map<String,String> parseArray = JSON.parseObject(product.getLite_image(), new TypeReference<Map<String, String>>() {});
			model.addAttribute("lite_image",parseArray);
		}else{
			model.addAttribute("lite_image","");
		}
        String tags = product.getTag_images();
        List<ProductTag> parseArray = JSON.parseArray(tags,ProductTag.class);

        //取消策略
        CancleStrategyExample cancleStrategyExample = new CancleStrategyExample();
        cancleStrategyExample.or().andDeletedEqualTo(0);
        List<CancleStrategy> cancleStrategies = cancleStrategyService.selectByExample(cancleStrategyExample);
        model.addAttribute("tags", parseArray);
		model.addAttribute("product", product);
		model.addAttribute("serviceitem", serviceitem);
		model.addAttribute("category", category);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("cancleStrategies",cancleStrategies);
		return "product/product_edit";
	}
	
	/**
	 * 商品编辑完成
	 * @param model
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/editProduct/{pageNo}")
	@Transactional
	public String editProduct(Product product,@PathVariable(value="pageNo") Long pageNo,Model model,
							  MultipartFile commodity_img,MultipartFile little_image,Authentication authentication,String uploadControllerList){
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[editProduct] U[{}] ,params product:{}, pageNo:{},commodity_img:{}, ueditorContext:{}，uploadControllerList:{}",
				users.getUser_id(),product,pageNo,commodity_img,uploadControllerList);
		//判断图片是否修改
		Map<String, String> params=new HashMap<>();
		if(!("".equals(commodity_img.getOriginalFilename()))){
			 params =this.myFileHandleUtil.file_upload(commodity_img, PathType.image_product_logo);
		}
		Map<String, String> littleParams=new HashMap<>();
		if(!("".equals(little_image.getOriginalFilename()))){
			littleParams =this.myFileHandleUtil.file_upload(little_image, PathType.image_product_logo);
		}
		//封装 （编辑产品）
		int num=this.productService.xEditProduct(product,params,littleParams,uploadControllerList);
		logger.info("M[product] F[editProduct] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		//根据商品找到所有的sku
		/*ProSkuExample proSkuExample = new ProSkuExample();
		proSkuExample.or().andProduct_idEqualTo(product.getProduct_id()).andDeletedEqualTo(0);
		List<ProSku> proSkuList = this.proSkuService.selectByExample(proSkuExample);
		//自定义时间刻度
		Map<String, Integer> timeUnis = Constant.timeUnisMap;
		
		//封装 （根据数据库存储的每个Sku服务时间段json格式，解析成字符串显示，格式：[{"s":18,"e":22}] 对应显示  08:00-10:00）
		List<Map<Long,String>> returnList=this.productService.xEditProductAnalysisTime(timeUnis,proSkuList);
		
		List<Bespeak> bespeakList= this.bespeakService.selectByExample(new BespeakExample());
		model.addAttribute("proSkuList", proSkuList);
		model.addAttribute("product", product);
		model.addAttribute("timeUnis", timeUnis);
		model.addAttribute("returnList", returnList);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("bespeakList", bespeakList);
		return "product/psku_edit";*/
		//清除掉之前的缓存
		this.cacheReloadHandler.homeProductListReload();
		this.cacheReloadHandler.productDetailReload(product.getProduct_id());
		this.cacheReloadHandler.productListByCategoryLevelCode();
		return "redirect:/product/showProductList?p="+pageNo;
	}
	
	/**
	 * 编辑sku
	 * @param product_id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/editSku/{product_id}")
	@ResponseBody
	@Transactional
	public Object editSku(@PathVariable(value="product_id") Long product_id,Model model,HttpServletRequest request,Authentication authentication){

		//删除用户在页面上操作删除的sku
		String str = request.getParameter("result");
		String delStr = request.getParameter("delPskIdList");
		String unsetStr = request.getParameter("unsetSkuList");

		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[editSku] U[{}] ,params product_id:{},request参数包含：固定时长Sku集合 str：{}, 删除Sku集合 delStr:{}, 不固定时长Sku集合 unsetStr:{}",
				users.getUser_id(),product_id,str,delStr,unsetStr);
		//封装  （编辑sku）
		int num=this.productService.xEditSku(product_id,str,delStr,unsetStr);
		logger.info("M[product] F[editSku] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("Sku信息修改%s!", num> 0 ? "成功" : "失败"));
		//清除掉之前的缓存
		this.cacheReloadHandler.homeProductListReload();
		this.cacheReloadHandler.productDetailReload(product_id);
		this.cacheReloadHandler.proSkuListReload(product_id);
		return map;
	}
	
	/**
	 * 获取服务时间段开始时间+时长找到其对应的结束时间
	 */
	@RequestMapping("/selectTime/{time}/{timeLength}")
	@ResponseBody
	public Map<String,Object> selectTime(@PathVariable(value="time")int time,@PathVariable(value="timeLength") int timeLength){
		Map<String,Object> returnMap=new HashMap<>();
		if(timeLength!=0){
			Map<String, Integer> map = Constant.timeUnisMap;
			for (String key : map.keySet()) {
				if(map.get(key)==(time+timeLength*2)){
					returnMap.put("time", key);
					returnMap.put("timeValue",map.get(key));
				}
			}
		}
		return returnMap;
	}
	
	/**
	 * 点击Sku编辑按钮的时候，根据服务时间段，找到时间对应的数值度
	 * @param p_service_times
	 * @return
	 */
	@RequestMapping("/selectEditShowTime/{p_service_times}")
	@ResponseBody
	public Object selectEditShowTime(@PathVariable(value="p_service_times")String p_service_times){
		//封装  （根据sku的服务时间段   找到其对应的刻度值）
		List<Map<String,Object>> returnList=this.productService.xSelectEditShowTime(p_service_times);
		
		return returnList;
	}
	
	/**
	 * 根据筛选条件的分类id查找对应的所有服务项目
	 * @param category_id
	 * @return
	 */
	@RequestMapping("/categorySelectSerList/{category_id}")
	@ResponseBody
	public Object categorySelectSerList(@PathVariable(value="category_id") Long category_id){
		ServiceitemExample serviceitemExample = new ServiceitemExample();
		serviceitemExample.or().andCategory_idEqualTo(category_id).andDeletedEqualTo(0);
		List<Serviceitem> serviceItemList = this.serviceitemService.selectByExample(serviceitemExample);
		return serviceItemList;
	}
	
	/**
	 * 添加Sku页面。点击取消按钮，并且删除之前添加对应的商品
	 * @param product_id
	 * @return
	 */
	@RequestMapping("/cancleDelProduct/{product_id}")
	@ResponseBody
	@Transactional
	public Object cancleDelProduct(@PathVariable(value="product_id") Long product_id){
		//删除商品
		int num = this.productService.deleteByPrimaryKey(product_id);
		//删除商品对应的图片
		//Product product = this.productService.selectByPrimaryKey(product_id);
		//String images = product.getImages();
		//this.ossImgService.deleteByPrimaryKey(product.get)
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("取消%s!", num> 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 查询没添加过的合伙人
	 * @param request
	 * @return
	 */
	@RequestMapping("/screenPartner/{proid}")
	@ResponseBody
	public List<Partner> screenPartner(HttpServletRequest request,@PathVariable(value = "proid") Long proid){
		String str = request.getParameter("result");
		List<Partner> list=this.productService.xScreenPartner(str,proid);
		return list;
	}
	
	/**
	 * 点击派单设置按钮查询该商品 对应添加过的合伙人
	 * @return
	 */
	@RequestMapping("/sendOrderPartner/{product_id}/{pageNo}")
	@ResponseBody
	public List<Partner> sendOrderPartner(@PathVariable(value="product_id") Long product_id,@PathVariable(value="pageNo") Integer pageNo){
		List<Partner> partnerList=new ArrayList<>();
		ProductSoleExample productSoleExample = new ProductSoleExample();
		productSoleExample.or().andProduct_idEqualTo(product_id);
		List<ProductSole> list = this.productSoleService.selectByExample(productSoleExample);
		list.stream().forEach(productSole ->{
			Partner partner = this.partnerService.selectByPrimaryKey(productSole.getPartner_id());
			partnerList.add(partner);
		});
		return partnerList;
	}
	
	/**
	 * 派单设置添加合伙人
	 * @param request
	 * @return
	 */
	@RequestMapping("/addOrderPartner/{product_id}")
	@ResponseBody
	@Transactional
	public Object addOrderPartner(@PathVariable(value="product_id") Long product_id,HttpServletRequest request,Authentication authentication){

		String str = request.getParameter("result");

		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[addOrderPartner] U[{}] ,params product_id:{}, 选择的派单合伙人str:{}",
				users.getUser_id(),product_id,str);
		int num=this.productService.xAddOrderPartner(str,product_id);
		logger.info("M[product] F[addOrderPartner] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("设置%s!", num> 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 查询所有的预约策略
	 * @return
	 */
	@RequestMapping("/selBespeakStrategyList")
	@ResponseBody
	@Transactional
	public List<Bespeak> selBespeakStrategyList(){
		List<Bespeak> bespeakList= this.bespeakService.selectByExample(new BespeakExample());
		return bespeakList;
	}
	
	/**
	 * 产品详情
	 * @return
	 */
	@RequestMapping("/productDetails/{product_id}")
	public String productDetails(@PathVariable(value="product_id") Long product_id,Model model){
		Product product = this.productService.selectByPrimaryKey(product_id);
		ProSkuExample proSkuExample = new ProSkuExample();
		proSkuExample.or().andProduct_idEqualTo(product_id).andDeletedEqualTo(0);
		List<ProSku> proSkuList = this.proSkuService.selectByExample(proSkuExample);
		//找到服务项
		Serviceitem serviceitem = this.serviceitemService.selectByPrimaryKey(product.getServiceitem_id());
		//在找到分类
		Category category = this.categoryService.selectByPrimaryKey(serviceitem.getCategory_id());
		//预约策略
		List<Bespeak> bespeakList= this.bespeakService.selectByExample(new BespeakExample());
		//自定义时间刻度
		Map<String, Integer> timeUnis = Constant.timeUnisMap;
		//封装 （根据数据库存储的每个Sku服务时间段json格式，解析成字符串显示，格式：[{"s":18,"e":22}] 对应显示  08:00-10:00）
		List<Map<Long,String>> returnList=this.productService.xEditProductAnalysisTime(timeUnis,proSkuList);
		model.addAttribute("product", product);
		model.addAttribute("proSkuList", proSkuList);
		model.addAttribute("serviceitem", serviceitem);
		model.addAttribute("category", category);
		model.addAttribute("bespeakList", bespeakList);
		model.addAttribute("returnList", returnList);
		return "product/product_detail";
	}
	
	
	/**
	 * 跳转编辑Sku页面显示
	 * @return
	 */
	@RequestMapping("/editPSkuShow/{product_id}/{pageNo}")
	@Transactional
	public String editPSkuShow(Model model,@PathVariable(value="product_id") Long product_id,@PathVariable(value="pageNo") int pageNo){
		//根据商品找到所有的sku
		Product product = this.productService.selectByPrimaryKey(product_id);
		ProSkuExample proSkuExample = new ProSkuExample();
		proSkuExample.or().andProduct_idEqualTo(product.getProduct_id()).andDeletedEqualTo(0);
		List<ProSku> proSkuList = this.proSkuService.selectByExample(proSkuExample);
		//自定义时间刻度
		Map<String, Integer> timeUnis = Constant.timeUnisMap;
		//封装 （根据数据库存储的每个Sku服务时间段json格式，解析成字符串显示，格式：[{"s":18,"e":22}] 对应显示  08:00-10:00）
		List<Map<Long,String>> returnList=this.productService.xEditProductAnalysisTime(timeUnis,proSkuList);
		//预约策略
		List<Bespeak> bespeakList= this.bespeakService.selectByExample(new BespeakExample());
		model.addAttribute("proSkuList", proSkuList);
		model.addAttribute("product", product);
		model.addAttribute("timeUnis", timeUnis);
		model.addAttribute("returnList", returnList);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("bespeakList", bespeakList);
		return "product/psku_edit";
	}


	/**
	 * tag标签
	 * @param file
	 * @param authentication
	 * @return
	 */
	@RequestMapping("/uploadTagImages")
	@ResponseBody
	public Object uploadTagImages(MultipartFile file,Authentication authentication){
        Map<String, String> params=this.myFileHandleUtil.file_upload(file, PathType.image_productTag_logo);
		//获取登录的用户id
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[product] F[uploadTagImages] U[{}] ,params file:{},",
				users.getUser_id(),params);
        //保存图片信息
        OssImg ossImg=new OssImg();
        ossImg.setOss_img_id(IdGenerator.generateId());
        ossImg.setFormat(params.get("file_format"));
        ossImg.setName(params.get("file_name"));
        ossImg.setUrl(params.get("file_url"));
        ossImg.setBucket(params.get("bucketName"));
        ossImg.setEffect("商品Tag图片");
        ossImg.setCreate_time(new Date());
        ossImg.setAccess_permissions("public");
        int num = this.ossImgService.insert(ossImg);

        Map<String,String> map=new HashMap<>();
        map.put("url",ossImg.getUrl());
        map.put("img_id",ossImg.getOss_img_id()+"");
        map.put("width",params.get("width"));
        map.put("height",params.get("height"));

        logger.info("M[product] F[uploadTagImages] U[{}] ,execute result:{}",
				users.getUser_id(),num>0?"成功":"失败");
        return map;
	}
	
}
