package com.aobei.trainconsole.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.CheckImagesFormatUtil;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aobei.trainconsole.util.MyFileHandleUtil;
import com.aobei.trainconsole.util.PathUtil.PathType;


@Controller
@RequestMapping("/bannermanager")
public class CmsBannerController {

	private static final Logger logger = LoggerFactory.getLogger(CmsBannerController.class);
	
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	
	@Autowired 
	private CmsBannerService cmsBannerService;
	
	@Autowired
	private OssImgService ossImgService;

	@Autowired
	private UsersService usersService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsUrlService cmsUrlService;

	@Autowired
	private CacheReloadHandler cacheReloadHandler;

	@Autowired
	private AppPackService appPackService;

	/**
	 * 内容推荐信息的列表页
	 * 
	 * @param  app
	 * @return
	 */
	@RequestMapping("/goto_banner_list")
	public String goto_bannar_list(Model map,@RequestParam(defaultValue = "wx_m_custom") String app) {
		List<CmsBannerInfo> banner_list = cmsBannerService.xSelectCmsBannerList(app);

		AppPackExample appPackExample = new AppPackExample();
		appPackExample.setOrderByClause(AppPackExample.C.app_pack_name+",type asc");
		List<AppPack> appPacks = appPackService.selectByExample(appPackExample);

		map.addAttribute("appPacks",appPacks);
		map.addAttribute("banner_list", banner_list);

		if(app != null) {
			map.addAttribute("back_app", app);
		}
		List<AppPack> appPacks1 = appPackService.selectByExample(null);

		return "cmsbanner/banner_list";
	}

	/**
	 * 跳转到内容推荐添加页面
	 *
	 * @return
	 */
	@RequestMapping("/banner_add")
	public String banner_add(String app,Model map) {
		map.addAttribute("app", app);

		CmsUrlExample cmsUrlExampleHttp = new CmsUrlExample();
		cmsUrlExampleHttp.or()
				.andTypeEqualTo(1)//http类型
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//未删除状态 0
		List<CmsUrl> cmsUrls = cmsUrlService.selectByExample(cmsUrlExampleHttp);
		map.addAttribute("cmsUrls",cmsUrls);

        CmsUrlExample cmsUrlExampleNative = new CmsUrlExample();
		cmsUrlExampleNative.or()
                .andTypeEqualTo(2)//native类型
                .andDeletedEqualTo(Status.DeleteStatus.no.value);//未删除状态 0
        List<CmsUrl> natives = cmsUrlService.selectByExample(cmsUrlExampleNative);
        map.addAttribute("natives",natives);

		ProductExample productExample = new ProductExample();
		productExample.or()
				.andOnlineEqualTo(1)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Product> products = productService.selectByExample(productExample);
		map.addAttribute("products",products);
		//appPackService.selectByExample();
        return "cmsbanner/banner_add";
	}

	/**
	 * 异步上传并保存封面图片信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save_banner")
	public Object save_banner(CmsBanner cmsBanner,MultipartFile cover_img,Authentication authentication,String port) {
		Map<String, String> map = new HashMap<>();
		Map<String, String> params = myFileHandleUtil.file_upload(cover_img,PathType.image_cms_banner);
		boolean b = false;
		try {
			b = CheckImagesFormatUtil.checkImageScale(Integer.parseInt(params.get("width")), Integer.parseInt(params.get("height")));
		} catch (IOException e) {
			logger.info(e.getMessage());
		}catch (NumberFormatException e) {
			logger.error("数据转换异常", e);
		}
		if(!b){
			map.put("msg", String.format("banner图信息添加失败,图片宽：高比例必须为2：1"));
			return map;
		}
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[banner] F[save_banner] U[{}] , params cmsBanner:{},cover_img:{} .",
				users.getUser_id(),cmsBanner,cover_img.getOriginalFilename());
		logger.info("M[banner] F[save_banner] U[{}] , insert new image,params params:{},effect:{},privileges:{} .",
				users.getUser_id(),params,"banner图","public");
		OssImg ossImg = ossImgService.xInsertOssImg(params,"banner图","public");
		logger.info("M[banner] F[save_banner] U[{}] , execute insert new image result:{}",
				users.getUser_id(),String.format("添加图片%s!", ossImg != null ? "成功" : "失败"));
		cmsBanner.setImg_cover(ossImg.getOss_img_id().toString());
		int i = cmsBannerService.xInsertCmsBanner(cmsBanner,port);
		logger.info("M[banner] F[save_banner] U[{}] , execute result:{}",
				users.getUser_id(),String.format("banner图信息添加%s!", i > 0 ? "成功" : "失败"));
		cacheReloadHandler.bannerListReload();

		map.put("msg", String.format("banner图信息添加%s!", i > 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 跳转到内容推荐编辑页面
	 * 
	 * @param cms_banner_id
	 * @return
	 */
	@RequestMapping("/banner_edit")
	public String banner_edit(String app,Long cms_banner_id,Model map) {
		CmsBanner cmsBanner = cmsBannerService.selectByPrimaryKey(cms_banner_id);
		OssImgExample OssImgExample = new OssImgExample();
		OssImgExample.or().andOss_img_idEqualTo(Long.valueOf(cmsBanner.getImg_cover()));
		OssImg OssImg = DataAccessUtils.singleResult(ossImgService.selectByExample(OssImgExample));


		CmsUrlExample cmsUrlExampleHttp = new CmsUrlExample();
		cmsUrlExampleHttp.or()
				.andTypeEqualTo(1)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//未删除状态 0
		List<CmsUrl> cmsUrls = cmsUrlService.selectByExample(cmsUrlExampleHttp);
		map.addAttribute("cmsUrls",cmsUrls);

        CmsUrlExample cmsUrlExampleNative = new CmsUrlExample();
		cmsUrlExampleNative.or()
                .andTypeEqualTo(2)//native类型
                .andDeletedEqualTo(Status.DeleteStatus.no.value);//未删除状态 0
        List<CmsUrl> natives = cmsUrlService.selectByExample(cmsUrlExampleNative);
        map.addAttribute("natives",natives);

		AppPackExample appPackExample = new AppPackExample();
		appPackExample.or()
				.andApp_pack_idEqualTo(cmsBanner.getApp());
		AppPack appPack = DataAccessUtils.singleResult(appPackService.selectByExample(appPackExample));
		ProductExample productExample = new ProductExample();
		productExample.or()
				.andOnlineEqualTo(1)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Product> products = productService.selectByExample(productExample);
		String substring = cmsBanner.getHref().substring(cmsBanner.getHref().indexOf("=")+1, cmsBanner.getHref().length());
		map.addAttribute("substring",substring);
		map.addAttribute("products",products);
		map.addAttribute("href",cmsBanner.getHref());
		map.addAttribute("cms_banner_id",cmsBanner.getCms_banner_id());
		map.addAttribute("group_name",appPack.getGroup_name());
		map.addAttribute("back_app", appPack.getApp_pack_id());
		map.addAttribute("cmsBanner", cmsBanner);
		map.addAttribute("img", OssImg);
		return "cmsbanner/banner_edit";
	}
	
	/**
	 * 异步上传并保存封面图片信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit_save_banner")
	public Object edit_save_banner(CmsBanner cmsBanner,MultipartFile cover_img,Authentication authentication,String port) {
		Map<String, String> map = new HashMap<>();
		//设置要上传的bucket

		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[banner] F[edit_save_banner] U[{}] , params cmsBanner:{},cover_img:{} .",
				users.getUser_id(),cmsBanner,cover_img.getOriginalFilename());
		String originalFilename = cover_img.getOriginalFilename();
		CmsBanner db_banner = cmsBannerService.selectByPrimaryKey(cmsBanner.getCms_banner_id());
		//当改变端要重新设置序号
		if(!cmsBanner.getApp().equals(db_banner.getApp())) {
			//查询要上传的端的最大序号并设置要上传的banner的序号
			CmsBannerExample cmsBannerExample = new CmsBannerExample();
			cmsBannerExample.includeColumns(CmsBannerExample.C.serial_number);
			cmsBannerExample.or().andAppEqualTo(cmsBanner.getApp());
			cmsBannerExample.setOrderByClause(CmsBannerExample.C.serial_number + " desc ");
			cmsBannerExample.setLimitStart(0l);
			cmsBannerExample.setLimitEnd(1l);
			CmsBanner banner = DataAccessUtils.singleResult(cmsBannerService.selectByExample(cmsBannerExample));
			if(banner != null) {
				cmsBanner.setSerial_number(banner.getSerial_number()+1);
			}else {
				cmsBanner.setSerial_number(1);
			}
		}


		String app  = cmsBanner.getApp();
		String[] apps  = app.split("_");
		port  = apps[apps.length-1];
		if(cmsBanner.getHref()!=null){
			cmsBanner.setHref("ab"+port+"://productdetail?product_id="+cmsBanner.getHref());
		}else{
			cmsBanner.setHref("0");
		}
		int i = 0;
		if(originalFilename.equals("") || cover_img == null) {//未修改图片
			//保存编辑后的内容推荐
			i = cmsBannerService.xUpdateCmsBannerByPrimaryKey(cmsBanner);
		} else {//上传新图片
			Map<String, String> params = myFileHandleUtil.file_upload(cover_img,PathType.image_cms_banner);
			boolean b = false;
			try {
				b = CheckImagesFormatUtil.checkImageScale(Integer.parseInt(params.get("width")), Integer.parseInt(params.get("height")));
			} catch (IOException e) {
				logger.info(e.getMessage());
			}catch (NumberFormatException e) {
				logger.error("数据转换异常", e);
			}
			if(!b){
				map.put("msg", String.format("banner图信息添加失败,图片宽：高比例必须为2：1"));
				return map;
			}
			ossImgService.deleteByPrimaryKey(Long.valueOf(cmsBanner.getImg_cover()));

			logger.info("M[banner] F[edit_save_banner] U[{}] , edit insert new image,params params:{},effect:{},privileges:{} .",
					users.getUser_id(),params,"banner图","public");
			//保存图片信息
			OssImg ossImg = ossImgService.xInsertOssImg(params,"banner图","public");
			logger.info("M[banner] F[edit_save_banner] U[{}] , execute edit insert new image result:{}",
					users.getUser_id(),String.format("添加图片%s!", ossImg != null ? "成功" : "失败"));
			cmsBanner.setImg_cover(ossImg.getOss_img_id().toString());
			//保存编辑后的内容推荐
			i = cmsBannerService.xUpdateCmsBannerByPrimaryKey(cmsBanner);
		}
		logger.info("M[banner] F[edit_save_banner] U[{}] , execute result:{}",
				users.getUser_id(),String.format("banner图信息编辑%s!", i > 0 ? "成功" : "失败"));
		cacheReloadHandler.bannerListReload();

		map.put("msg", String.format("banner图信息编辑%s!", i > 0 ? "成功" : "失败"));
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/change_state")
	public Object change_state(String sign,Long cms_banner_id,String app,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[banner] F[change_state] U[{}] , params sign:{},cms_banner_id:{},app:{} .",
				users.getUser_id(),sign,cms_banner_id,app);
		Map<String, Object> map = cmsBannerService.changeState(sign, cms_banner_id);
		logger.info("M[banner] F[change_state] U[{}] , execute result:{} .",
				users.getUser_id(),map.get("msg"));
		List<CmsBannerInfo> banner_list = cmsBannerService.xSelectCmsBannerList(app);
		cacheReloadHandler.bannerListReload();
		map.put("banner_list", banner_list);
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/move_up")
	public Object move_up(String serial_number,Long cms_banner_id,Long pre_cms_id,String app,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[banner] F[move_up] U[{}] , params serial_number:{},cms_banner_id:{},pre_cms_id:{},app:{} .",
				users.getUser_id(),serial_number,cms_banner_id,pre_cms_id,app);
		Map<String, Object> map = new HashMap<>();
		boolean b = cmsBannerService.move_up(serial_number, cms_banner_id, pre_cms_id);
		map.put("msg", String.format("上移操作%s!", b == true ? "成功" : "失败"));
		logger.info("M[banner] F[move_up] U[{}] , execute result:{} .",
				users.getUser_id(),map.get("msg"));
		List<CmsBannerInfo> banner_list = cmsBannerService.xSelectCmsBannerList(app);
		cacheReloadHandler.bannerListReload();
		map.put("banner_list", banner_list);

		return map;
	}
	
	@ResponseBody
	@RequestMapping("/move_down")
	public Object move_down(String serial_number,Long cms_banner_id,Long next_cms_id,String app,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[banner] F[move_down] U[{}] , params serial_number:{},cms_banner_id:{},next_cms_id:{},app:{} .",
				users.getUser_id(),serial_number,cms_banner_id,next_cms_id,app);
		Map<String , Object> map = new HashMap<>();
		boolean b = cmsBannerService.move_down(serial_number, cms_banner_id, next_cms_id);
		map.put("msg", String.format("下移操作%s!", b == true ? "成功" : "失败"));
		logger.info("M[banner] F[move_down] U[{}] , execute result:{} .",
				users.getUser_id(),map.get("msg"));
		List<CmsBannerInfo> banner_list = cmsBannerService.xSelectCmsBannerList(app);
		cacheReloadHandler.bannerListReload();
		map.put("banner_list", banner_list);
		return map;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	/*----------------------------------- (0.4.0 Version) ------------------------------------------*/

    /**
     * 获取商品数据
     * @param condition
     * @return
     */
    @ResponseBody
    @RequestMapping("/get_product_list")
    public Object get_product_list(@RequestParam(value="condition",required = false)String condition) {

		CmsUrlExample cmsUrlExample = new CmsUrlExample();
		CmsUrlExample.Criteria or = cmsUrlExample.or();
		or.andTypeEqualTo(1)
			.andDeletedEqualTo(Status.DeleteStatus.no.value);//未删除状态 0
        if(!StringUtils.isEmpty(condition)){
            or.andTitleLike("%"+condition+"%");
        }
        return cmsUrlService.selectByExample(cmsUrlExample);
    }
	@ResponseBody
	@RequestMapping("/get_product_list1")
	public Object get_product_list1(@RequestParam(value="condition",required = false)String condition) {

		ProductExample productExample = new ProductExample();
		productExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value)
				.andOnlineEqualTo(1)
				.andNameLike("%"+condition+"%");

		return productService.selectByExample(productExample);
	}
	/**
	 * 获取商品数据
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_native_list")
	public Object get_native_list(@RequestParam(value="condition",required = false)String condition) {

		CmsUrlExample cmsUrlExample = new CmsUrlExample();
		CmsUrlExample.Criteria or = cmsUrlExample.or();
		or.andTypeEqualTo(2)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//未删除状态 0
		if(!StringUtils.isEmpty(condition)){
			or.andTitleLike("%"+condition+"%");
		}
		return cmsUrlService.selectByExample(cmsUrlExample);
	}

	/**
	 * 跳转banner图-- 路径添加
	 * @param p
	 * @param model
	 * @return
	 */
    @RequestMapping("/goto_add_cmsurl")
    public String goto_add_cmsUrl(
    		@RequestParam(value="current")Integer p,
			Model model){
		model.addAttribute("current",p);
		return "cmsbanner/cmsurl_add";
    }

	/**
	 * 异步上传添加路径
	 * @param cmsUrl
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/save_cmsurl")
    public Object save_cmsUrl(CmsUrl cmsUrl,Authentication authentication){
        HashMap<String,String> map = new HashMap<>();
        int i = cmsUrlService.xAdd_cmsUrl(cmsUrl);
        map.put("message",String.format("添加%s",i>0?"成功":"失败"));
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[banner] F[add_cmsUrl] U[{} ] , [param cmsUrl {}] [message:{}]",
				users.getUser_id(),cmsUrl,String.format("添加%s",i>0?"成功":"失败"));
        return map;
    }

	/**
	 * banner图路径管理列表页
	 * @param model
	 * @param p
	 * @param ps
	 * @return
	 */
    @RequestMapping("/cmsurl_list")
    public String cmsurl_list(Model model,
                                   @RequestParam(defaultValue="1") Integer p,
                                   @RequestParam(defaultValue="10") Integer ps){
        CmsUrlExample cmsUrlExample = new CmsUrlExample();
        cmsUrlExample.or()
                .andDeletedEqualTo(Status.DeleteStatus.no.value);//0 未删除
        Page<CmsUrl> page = cmsUrlService.selectByExample(cmsUrlExample, p, ps);

		model.addAttribute("current", p);
		model.addAttribute("page", page);
        model.addAttribute("list", page.getList());
        return "cmsbanner/cmsurl_list";
    }

	/**
	 * 删除banner图路径
	 * @param cms_url_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del_cmsurl")
	public Object del_cmsUrl(Long cms_url_id,Authentication authentication){
		HashMap<String,String> map = new HashMap<>();
		int i = cmsUrlService.xDel_cmsUrl(cms_url_id);
		map.put("message",String.format("删除%s",i>0?"成功":"失败"));
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[banner] F[add_cmsUrl] U[{} ] , [param cms_url_id {}] [message:{}]",
				users.getUser_id(),cms_url_id,String.format("删除%s",i>0?"成功":"失败"));
		return map;
	}

	/**
	 * banenr图-- 编辑路径
	 * @param cms_url_id
	 * @param model
	 * @param p
	 * @return
	 */
	@RequestMapping("/goto_edit_cmsurl")
	public String goto_edit_cmsUrl(Long cms_url_id,Model model,@RequestParam(value="current")Integer p){
		CmsUrl cmsUrl = cmsUrlService.selectByPrimaryKey(cms_url_id);
		model.addAttribute("cms_url_id", cms_url_id);
		model.addAttribute("title", cmsUrl.getTitle());
		model.addAttribute("url", cmsUrl.getUrl());
		model.addAttribute("type", cmsUrl.getType());
		model.addAttribute("current",p);
		return "cmsbanner/cmsurl_edit";
	}

	/**
	 * 异步上传路径编辑
	 * @param cmsUrl
	 * @param cms_url_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit_cmsurl")
	public Object edit_cmsUrl(CmsUrl cmsUrl,
							  @RequestParam(value="data_cmsurl_id")Long cms_url_id,
							  Authentication authentication){
		HashMap<String,String> map = new HashMap<>();
		int i = cmsUrlService.xEdit_cmsUrl(cmsUrl,cms_url_id);
		map.put("message",String.format("修改%s",i>0?"成功":"失败"));
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[banner] F[add_cmsUrl] U[{} ] , [param cms_url_id {}] [message:{}]",
				users.getUser_id(),cms_url_id,String.format("修改%s",i>0?"成功":"失败"));
		return map;
	}

	/**
	 * 通过id 获取对应路径数据
	 * @param cms_url_id cms_url表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUrl")
	public Object getUrl(Long cms_url_id){
		if(cms_url_id==null){
			CmsUrl cmsUrl = new CmsUrl();
			cmsUrl.setUrl("none");
			return cmsUrl;
		}
		CmsUrl cmsUrl = cmsUrlService.selectByPrimaryKey(cms_url_id);
		return cmsUrl;
	}
	@ResponseBody
	@RequestMapping("/getPort")
	public Object getPort(String group_name){
		List<AppPack> list = appPackService.xGetPortBanner(group_name);
		HashMap map = new HashMap();
		map.put("list",list);
		return map;
	}
	@ResponseBody
	@RequestMapping("/getProductInfo")
	public Object getProductInfo(Long cms_banner_id){
		HashMap map = new HashMap();
		CmsBanner cmsBanner = cmsBannerService.selectByPrimaryKey(cms_banner_id);
		String href = cmsBanner.getHref();

		String substring = null;
		if(href!=null && href!="0"){
			if(href.length()<34){
				map.put("name","");
				map.put("product_id","");
				return map;
			}

			substring = href.substring(href.indexOf("=")+1, href.length());
		}
		if(substring.matches("[0-9]+")){
			long l = Long.parseLong(substring);

			Product product = productService.selectByPrimaryKey(l);
			if(product!=null){
				map.put("name",product.getName());
				map.put("product_id",product.getProduct_id());
			}else{
				map.put("name","");
				map.put("product_id","");
			}
		}else{
			map.put("name","");
			map.put("product_id","");
		}
		return map;
	}
}
