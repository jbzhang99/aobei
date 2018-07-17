package com.aobei.train.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.aobei.train.mapper.*;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ProductServiceImpl extends MbgServiceSupport<ProductMapper, Long, Product, Product, ProductExample> implements ProductService{

	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private OssImgMapper ossImgMapper; 
	
	@Autowired
	private ProSkuMapper proSkuMapper;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private ServiceitemMapper serviceitemMapper;
	
	@Autowired
	private PartnerMapper partnerMapper;
	
	@Autowired
	private ProductSoleMapper productSoleMapper;

    @Autowired
	private PartnerServiceitemMapper partnerServiceitemMapper;
    @Autowired
	OrderItemService orderItemService;
    @Autowired
	CouponService couponService;
    @Autowired
	CouponReceiveService couponReceiveService;
    @Autowired
	ProSkuService proSkuService;
	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(productMapper);
	}

	
	/**
	 * 封装   添加商品(商品对象，图片，富文本内容)
	 */
	@Override
	@Transactional(timeout = 5)
	public int xAddCommodityProduct(Product product, Map<String, String> params,Map<String, String> littleParams,String uploadControllerList) {
		if(params.get("file_format")!= null){
			//保存图片信息
			OssImg ossImg=new OssImg();
			ossImg.setOss_img_id(IdGenerator.generateId());
			ossImg.setFormat(params.get("file_format"));
			ossImg.setName(params.get("file_name"));
			ossImg.setUrl(params.get("file_url"));
			ossImg.setBucket(params.get("bucketName"));
			ossImg.setEffect("服务图片");
			ossImg.setCreate_time(new Date());
			ossImg.setAccess_permissions("public");
			this.ossImgMapper.insert(ossImg);
			product.setImage_first(ossImg.getUrl());
			List<Map<String,Object>> images=new ArrayList<>();
			Map<String,Object> map=new HashMap<>();
			map.put("id",ossImg.getOss_img_id());
			map.put("url",ossImg.getUrl());
			map.put("first", 1);
			images.add(map);
			try {
				product.setImages(JSONArray.toJSONString(images));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(littleParams.get("file_format")!= null){
			//保存图片信息
			OssImg ossImg=new OssImg();
			ossImg.setOss_img_id(IdGenerator.generateId());
			ossImg.setFormat(littleParams.get("file_format"));
			ossImg.setName(littleParams.get("file_name"));
			ossImg.setUrl(littleParams.get("file_url"));
			ossImg.setBucket(littleParams.get("bucketName"));
			ossImg.setEffect("商品详情图片");
			ossImg.setCreate_time(new Date());
			ossImg.setAccess_permissions("public");
			this.ossImgMapper.insert(ossImg);
			List<Map<String,Object>> images=new ArrayList<>();
			Map<String,Object> map=new HashMap<>();
			map.put("id",ossImg.getOss_img_id());
			map.put("url",ossImg.getUrl());
			images.add(map);
			try {
				product.setLite_image(JSONObject.toJSONString(map));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//设置商品信息
		product.setActived(0);
		product.setProduct_id(IdGenerator.generateId());
		product.setUpdate_datetime(new Date());
		product.setCreate_datetime(new Date());
		product.setDeleted(Status.DeleteStatus.no.value);
		product.setOnline(0);	
		product.setPricev_first(0);
		product.setPrice_first(0);
		product.setTag_images(uploadControllerList);
		int num = this.insertSelective(product);
		return num;
	}


	/**
	 * 封装     添加sku
	 */
	@Override
	@Transactional
	public int xAddSku(Long product_id, String str,String unsetStr) {
		JSONArray jsonArry = JSONArray.parseArray(str);
		JSONArray unsetJsonArry = JSONArray.parseArray(unsetStr);
		//转换成集合对象
		List<ProSku> proSkuList = jsonArry.toJavaList(ProSku.class);
		//只有开始时间的sku集合
		//不固定的sku集合
		List<ProSku> unsetSkuList = unsetJsonArry.toJavaList(ProSku.class);
		for (ProSku ps : unsetSkuList) {
			ps.setPsku_id(IdGenerator.generateId());
			ps.setDeleted(Status.DeleteStatus.no.value);
			String service_times = ps.getService_times();
			SkuTime skuTime=new SkuTime();
			List<SkuTime> skuTimeList=new ArrayList<>();
			skuTime.setE(48);
			//获取定义好的时间段
			Map<String, Integer> map = Constant.timeUnisMap;
			for (String key : map.keySet()) {
				if(service_times.equals(key)){
					skuTime.setS(map.get(key));
					break;
				}
			}
			skuTimeList.add(skuTime);
			ps.setService_times(jsonArry.toJSONString(skuTimeList));
			ps.setService_time_length(0);
			this.proSkuMapper.insert(ps);
		}
		//固定的sku集合
		for (ProSku proSku : proSkuList) {
			proSku.setPsku_id(IdGenerator.generateId());
			proSku.setDeleted(Status.DeleteStatus.no.value);
			//给服务时间段重新封装数据
			String service_times = proSku.getService_times();
			//定义一个集合
			List<Map<String,Integer>> timesList=new ArrayList<>();
			//获取定义好的时间段
			Map<String, Integer> map = Constant.timeUnisMap;
			//判断是否有多个服务时间段
			if(service_times.indexOf(",")!=-1){
				String[] pluralSplit = service_times.split(",");
				for (String s : pluralSplit) {
					String[] split = s.split("-");
					List<Object> list=new ArrayList<>();
					//进行匹配
					Map<String,Integer> m=new HashMap<>();
					for (String key : map.keySet()) {
						for (String string : split) {
							if(string.equals(key)){
								list.add(map.get(key));
							}
						}
					}
					if(list.size()!=0){
						for (int i = 0; i < list.size(); i++) {
							if(i==0){
								if(Integer.parseInt(list.get(0).toString())>Integer.parseInt(list.get(1).toString())){
									m.put("s",Integer.parseInt(list.get(1).toString()));
									m.put("e",Integer.parseInt(list.get(0).toString()));
								}else{
									m.put("s",Integer.parseInt(list.get(0).toString()));
									m.put("e",Integer.parseInt(list.get(1).toString()));
								}
							}
						}
					}
					timesList.add(m);
				}
			}else{
				String[] split = service_times.split("-");
				List<Object> list=new ArrayList<>();
				for (String key : map.keySet()) {
					for (String string : split) {
						if(string.equals(key)){
							list.add(map.get(key));
						}
					}
				}
				Map<String,Integer> m=new HashMap<>();
				if(list.size()!=0){
					for (int i = 0; i < list.size(); i++) {
						if(i==0){
							if(Integer.parseInt(list.get(0).toString())>Integer.parseInt(list.get(1).toString())){
								m.put("s",Integer.parseInt(list.get(1).toString()));
								m.put("e",Integer.parseInt(list.get(0).toString()));
							}else{
								m.put("s",Integer.parseInt(list.get(0).toString()));
								m.put("e",Integer.parseInt(list.get(1).toString()));
							}
						}
					}
				}
				timesList.add(m);
			}
			try {
				//proSku.setService_times(JacksonUtil.object_to_json(timesList));
				proSku.setService_times(jsonArry.toJSONString(timesList));
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.proSkuMapper.insert(proSku);
		}
		//给商品赋值(sku排序第一个)
		/*ProSkuExample proSkuExample = new ProSkuExample();
		proSkuExample.or().andProduct_idEqualTo(product_id);
		proSkuExample.setOrderByClause(ProSkuExample.C.sort_num +" asc");
		List<ProSku> psList = this.proSkuService.selectByExample(proSkuExample);
		ProSku proSku = psList.get(0);*/
		
		//给商品赋值(sku浦尔家价格最低的)
		/*Product product = this.selectByPrimaryKey(product_id);
		ProSkuExample proSkuExample2 = new ProSkuExample();
		proSkuExample2.or().andProduct_idEqualTo(product_id).andDispalyEqualTo(0);
		proSkuExample2.setOrderByClause(ProSkuExample.C.price+" asc");
		List<ProSku> psku = this.proSkuMapper.selectByExample(proSkuExample2);
		if(psku.size()!=0){
			product.setPrice_first(psku.get(0).getPrice());
			product.setPricev_first(psku.get(0).getPricev());
			product.setPsku_id_first(psku.get(0).getPsku_id());
		}else{
			product.setPrice_first(0);
			product.setPricev_first(0);
			product.setPsku_id_first(null);
		}*/
        ProSkuExample proSkuExample = new ProSkuExample();
        proSkuExample.or().andProduct_idEqualTo(product_id).andDefault_skuEqualTo(1);
        ProSku proSku = DataAccessUtils.singleResult(this.proSkuMapper.selectByExample(proSkuExample));
        //更新商品的价格
        Product product = this.selectByPrimaryKey(product_id);


        if(proSku!=null){
            product.setPrice_first(proSku.getPrice());
            product.setPricev_first(proSku.getPricev());
            product.setPsku_id_first(proSku.getPsku_id());
        }else{
            product.setPrice_first(0);
            product.setPricev_first(0);
            product.setPsku_id_first(null);
        }
		int num = this.updateByPrimaryKey(product);
		return num;
	}


	/**
	 * 产品下线
	 */
	@Override
	@Transactional(timeout = 5)
	public int xDownProduct(Long product_id) {
		Product product = this.selectByPrimaryKey(product_id);
		product.setOnline(0);//下架
		int num = this.updateByPrimaryKey(product);
		return num;
	}


	/**
	 * 产品上线
	 */
	@Override
	@Transactional(timeout = 5)
	public int xUpProduct(Long product_id) {
		Product product = this.selectByPrimaryKey(product_id);
		product.setOnline(1);//上架
		product.setActived(1);//已上架
		int num = this.updateByPrimaryKey(product);
		return num;
	}


	/**
	 * 删除产品
	 */
	@Override
	@Transactional(timeout = 5)
	public int xDelProduct(Long product_id) {
		Product product = this.selectByPrimaryKey(product_id);
		product.setDeleted(Status.DeleteStatus.yes.value);
		int num = this.updateByPrimaryKey(product);
		return num;
	}


	/**
	 * 编辑产品
	 * @param product
	 * @param params
	 * @return
	 */
	@Override
	@Transactional(timeout = 5)
	public int xEditProduct(Product product, Map<String, String> params,Map<String, String> littleParams,String uploadControllerList) {
		if(params.get("file_format")!= null){
			//先判断商品之前是否有图片
			Product pd = this.selectByPrimaryKey(product.getProduct_id());
			if(!StringUtils.isEmpty(pd.getImage_first())){
				//如果不为空，则进行删除
				OssImgExample ossImgExample = new OssImgExample();
				ossImgExample.or().andUrlEqualTo(pd.getImage_first());
				this.ossImgMapper.deleteByExample(ossImgExample);
			}
			//保存图片信息
			OssImg ossImg=new OssImg();
			ossImg.setOss_img_id(IdGenerator.generateId());
			ossImg.setFormat(params.get("file_format"));
			ossImg.setName(params.get("file_name"));
			ossImg.setUrl(params.get("file_url"));
			ossImg.setBucket(params.get("bucketName"));
			ossImg.setEffect("服务图片");
			ossImg.setCreate_time(new Date());
			ossImg.setAccess_permissions("public");
			this.ossImgMapper.insert(ossImg);
			product.setImage_first(ossImg.getUrl());
			List<Map<String,Object>> images=new ArrayList<>();
			Map<String,Object> map=new HashMap<>();
			map.put("id",ossImg.getOss_img_id());
			map.put("url",ossImg.getUrl());
			map.put("first", 1);
			images.add(map);
			//ObjectMapper objectMapper=new ObjectMapper();
			try {
				//product.setImages(objectMapper.writeValueAsString(images));
				product.setImages(JSONObject.toJSONString(map));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(littleParams.get("file_format")!= null){
			//保存图片信息
			OssImg ossImg=new OssImg();
			ossImg.setOss_img_id(IdGenerator.generateId());
			ossImg.setFormat(littleParams.get("file_format"));
			ossImg.setName(littleParams.get("file_name"));
			ossImg.setUrl(littleParams.get("file_url"));
			ossImg.setBucket(littleParams.get("bucketName"));
			ossImg.setEffect("详情图片");
			ossImg.setCreate_time(new Date());
			ossImg.setAccess_permissions("public");
			this.ossImgMapper.insert(ossImg);
			Map<String,Object> map=new HashMap<>();
			map.put("id",ossImg.getOss_img_id());
			map.put("url",ossImg.getUrl());
			product.setLite_image(JSONObject.toJSONString(map));
		}
		product.setUpdate_datetime(new Date());
		product.setTag_images(uploadControllerList);
		//product.setCreate_datetime(new Date());
		//product.setContent(ueditorContext);
		int num = this.updateByPrimaryKeySelective(product);
		return num;
	}


	/**
	 * 编辑商品完，在跳转到编辑sku页面时，进行时间的解析
	 */
	@Override
	public List<Map<Long, String>> xEditProductAnalysisTime(Map<String, Integer> timeUnis,List<ProSku> proSkuList) {
		//sku服务时间段
		List<Map<Long,List<SkuTime>>> lists=new ArrayList<>();
		
		List<SkuTime> list_skuTime=new ArrayList<>();
		//根据数据库存储的每个Sku服务时间段json格式，解析成字符串显示，格式：[{"s":18,"e":22}] 对应显示  08:00-10:00
		for (ProSku proSku : proSkuList) {
			Map<Long,List<SkuTime>> map=new HashMap<>();
			String serviceTimes = proSku.getService_times();
			if(serviceTimes==null){
				map.put(proSku.getPsku_id(),new ArrayList<>());
			}else{
				JSONArray parseArray = JSONObject.parseArray(serviceTimes);
				list_skuTime = parseArray.toJavaList(SkuTime.class);
				map.put(proSku.getPsku_id(),list_skuTime);
			}
			lists.add(map);
		}
		
		//进行解析
		List<Map<Long,String>> returnList=new ArrayList<>();
		
		for (Map<Long, List<SkuTime>> map : lists) {
			for (Map.Entry<Long, List<SkuTime>> entrys : map.entrySet()) {
				Map<Long,String> returnMap=new HashMap<>();
				String times="";
				for (SkuTime skuTime : entrys.getValue()) {
					String start="";
					String end="";
					for (Map.Entry<String, Integer> entry : timeUnis.entrySet()) { 
						if(skuTime.getE()==48){
							if(skuTime.getS()==entry.getValue()){
								 start=entry.getKey();
								 end=".";
							}
						}else{
							if(skuTime.getS()==entry.getValue()){
								 start=entry.getKey()+"-";
							}
							if(skuTime.getE()==entry.getValue()){
								end=entry.getKey()+",";
							}
						}
						
					}
					times+=start+end;
				}
				if(times.length()!=0){
					returnMap.put(entrys.getKey(),times.substring(0,times.length()-1));
				}else{
					returnMap.put(entrys.getKey(),"");
				}
				returnList.add(returnMap);
			}
		}
		return returnList;
	}

	/**
	 * 编辑Sku
	 */
	@Override
	@Transactional
	public int xEditSku(Long product_id, String str, String delStr,String unsetStr) {
		JSONArray jsonArry = JSONArray.parseArray(str);
		JSONArray unsetJsonArry = JSONArray.parseArray(unsetStr);
        List<Long> delPskuId = JSONArray.parseArray(delStr, Long.class);
        for (Long l : delPskuId) {
            //this.proSkuMapper.deleteByPrimaryKey(l);
            ProSku proSku = this.proSkuMapper.selectByPrimaryKey(l);
            proSku.setDeleted(1);
            this.proSkuMapper.updateByPrimaryKey(proSku);
        }

		//不固定的sku
		List<ProSku> unsetSkuList = unsetJsonArry.toJavaList(ProSku.class);
		for (ProSku ps : unsetSkuList) {
			ps.setDeleted(0);
			String service_times = ps.getService_times();
			SkuTime skuTime=new SkuTime();
			List<SkuTime> skuTimeList=new ArrayList<>();
			skuTime.setE(48);
			//获取定义好的时间段
			Map<String, Integer> map = Constant.timeUnisMap;
			for (String key : map.keySet()) {
				if(service_times.equals(key)){
					skuTime.setS(map.get(key));
					break;
				}
			}
			skuTimeList.add(skuTime);
			ps.setService_times(jsonArry.toJSONString(skuTimeList));
			int length = ps.getPsku_id().toString().length();
			if(length==19){//要修改
				this.proSkuMapper.updateByPrimaryKey(ps);
			}else{//要新增
				ps.setPsku_id(IdGenerator.generateId());
				ps.setDeleted(Status.DeleteStatus.no.value);
				ps.setProduct_id(product_id);
				this.proSkuMapper.insert(ps);
			}
		}
		//固定sku
		List<ProSku> proSkuList = jsonArry.toJavaList(ProSku.class);
		for (ProSku proSku : proSkuList) {
			proSku.setDeleted(0);
			//给服务时间段重新封装数据  格式：[{"s":18,"e":22}]
			String service_times = proSku.getService_times();
			
			//定义一个集合
			List<Map<String,Integer>> timesList=new ArrayList<>();
			//获取定义好的时间段
			Map<String, Integer> map = Constant.timeUnisMap;
			//判断是否有多个服务时间段
			if(service_times.indexOf(",")!=-1){
				String[] pluralSplit = service_times.split(",");
				for (String s : pluralSplit) {
					String[] split = s.split("-");
					List<Object> list=new ArrayList<>();
					//进行匹配
					Map<String,Integer> m=new HashMap<>();
					for (String key : map.keySet()) {
						for (String string : split) {
							if(string.equals(key)){
								list.add(map.get(key));
							}
						}
					}
					if(list.size()!=0){
						for (int i = 0; i < list.size(); i++) {
							if(i==0){
								if(Integer.parseInt(list.get(0).toString())>Integer.parseInt(list.get(1).toString())){
									m.put("s",Integer.parseInt(list.get(1).toString()));
									m.put("e",Integer.parseInt(list.get(0).toString()));
								}else{
									m.put("s",Integer.parseInt(list.get(0).toString()));
									m.put("e",Integer.parseInt(list.get(1).toString()));
								}
							}
						}
					}
					timesList.add(m);
				}
			}else{//否则只有一个服务时间段
				String[] split = service_times.split("-");
				List<Object> list=new ArrayList<>();
				for (String key : map.keySet()) {
					for (String string : split) {
						if(string.equals(key)){
							list.add(map.get(key));
						}
					}
				}
				Map<String,Integer> m=new HashMap<>();
				if(list.size()!=0){
					for (int i = 0; i < list.size(); i++) {
						if(i==0){
							if(Integer.parseInt(list.get(0).toString())>Integer.parseInt(list.get(1).toString())){
								m.put("s",Integer.parseInt(list.get(1).toString()));
								m.put("e",Integer.parseInt(list.get(0).toString()));
							}else{
								m.put("s",Integer.parseInt(list.get(0).toString()));
								m.put("e",Integer.parseInt(list.get(1).toString()));
							}
						}
					}
				}
				timesList.add(m);
			}
			try {
				//proSku.setService_times(JacksonUtil.object_to_json(timesList));
				proSku.setService_times(jsonArry.toJSONString(timesList));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			//判断该SKU是要修改的，还是新增的。
			int length=proSku.getPsku_id().toString().length();
			if(length==19){
				this.proSkuMapper.updateByPrimaryKey(proSku);
			}else{
				proSku.setPsku_id(IdGenerator.generateId());
				proSku.setDeleted(Status.DeleteStatus.no.value);
				proSku.setProduct_id(product_id);
				this.proSkuMapper.insert(proSku);
			}
			
		}
		//给商品赋值(sku浦尔家价格最低排序)
		/*ProSkuExample proSkuExample2 = new ProSkuExample();
		proSkuExample2.or().andProduct_idEqualTo(product_id).andDispalyEqualTo(0);
		proSkuExample2.setOrderByClause(ProSkuExample.C.price+" asc");
		List<ProSku> psku = this.proSkuMapper.selectByExample(proSkuExample2);*/
        //找出Sku价格最低的sku的id
		/*if(psku.size()!=0){
			product.setPrice_first(psku.get(0).getPrice());
			product.setPricev_first(psku.get(0).getPricev());
			product.setPsku_id_first(psku.get(0).getPsku_id());
		}else{
			product.setPrice_first(0);
			product.setPricev_first(0);
			product.setPsku_id_first(null);
		}*/
        ProSkuExample proSkuExample = new ProSkuExample();
        proSkuExample.or().andProduct_idEqualTo(product_id).andDefault_skuEqualTo(1);
        ProSku proSku = DataAccessUtils.singleResult(this.proSkuMapper.selectByExample(proSkuExample));
        //更新商品的价格
		Product product = this.selectByPrimaryKey(product_id);
		

        if(proSku!=null){
            product.setPrice_first(proSku.getPrice());
            product.setPricev_first(proSku.getPricev());
            product.setPsku_id_first(proSku.getPsku_id());
        }else{
            product.setPrice_first(0);
            product.setPricev_first(0);
            product.setPsku_id_first(null);
        }
		int num = this.updateByPrimaryKey(product);
		return num;
	}


	/**
	 * 点击Sku编辑按钮的时候，根据服务时间段，找到时间对应的数值度
	 */
	@Override
	public List<Map<String, Object>> xSelectEditShowTime(String p_service_times) {
		List<Map<String,Object>> returnList=new ArrayList<>();
		Map<String,Object> returnMap=new LinkedHashMap<>();
		Map<String, Integer> map = Constant.timeUnisMap;
		//有多个时间段
		if(p_service_times.indexOf(",")!=-1){
			String[] split = p_service_times.split(",");
			for (String s : split) {
				String[] sp = s.split("-");
				for (String string : sp) {
					for (String key : map.keySet()) {
						if(string.equals(key)){
							returnMap.put(string, map.get(key));
						}
					}
				}
				returnList.add(returnMap);
			}
		}else{//只有一个时间段
			String[] split = p_service_times.split("-");
			for (String string : split) {
				for (String key : map.keySet()) {
					if(string.equals(key)){
						returnMap.put(string, map.get(key));
					}
				}
			}
			returnList.add(returnMap);
		}
		return returnList;
	}


	/**
	 * 显示分类及服务项目
	 * @param cid
	 * @return
	 */
	@Override
	public List<Map<String, Object>> xSelectCategory(Long cid) {
		//先去查是否有分类
		CategoryExample categoryExample = new CategoryExample();
		//上线，未删除
		categoryExample.or().andPidEqualTo(cid).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Category> categoryList = this.categoryMapper.selectByExample(categoryExample);
		List<Map<String,Object>> mapList=new ArrayList<>();
		if(categoryList.size() !=0){
			categoryList.stream().forEach(category ->{
				Map<String,Object> map =new HashMap<>();
				map.put("id", category.getCategory_id().toString());
				map.put("name", category.getName());
				map.put("identify",1);
				mapList.add(map);
			});
		}
		//再查是否有服务项目
		ServiceitemExample serviceitemExample = new ServiceitemExample();
		//上线，未删除
		serviceitemExample.or().andCategory_idEqualTo(cid).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Serviceitem> serList = this.serviceitemMapper.selectByExample(serviceitemExample);
		if(serList.size() !=0 ){
			serList.stream().forEach(serviceitem ->{
				Map<String,Object> map =new HashMap<>();
				map.put("id",serviceitem.getServiceitem_id().toString());
				map.put("name", serviceitem.getName());
				map.put("identify",0);
				mapList.add(map);
			});
		}
		return mapList;
	}


	/**
	 * 点击派单设置按钮查询该商品 对应添加过的合伙人
	 * @param str
	 * @return
	 */
	@Override
	public List<Partner> xScreenPartner(String str,Long proid) {
		List<Long> longs = JSONObject.parseArray(str, Long.class);
		Product product = this.productMapper.selectByPrimaryKey(proid);
        PartnerServiceitemExample partnerServiceitemExample = new PartnerServiceitemExample();
        partnerServiceitemExample.or().andServiceitem_idEqualTo(product.getServiceitem_id());
        List<PartnerServiceitem> partnerServiceitems = this.partnerServiceitemMapper.selectByExample(partnerServiceitemExample);
        List<Long> partner_ids = partnerServiceitems.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
        List<Partner> list=new ArrayList<>();
        PartnerExample partnerExample = new PartnerExample();
        PartnerExample.Criteria criteria = partnerExample.or();
        criteria.andDeletedEqualTo(0);

        //如果没有，就查询所有的
		if(longs.size() ==0){
            if(partner_ids.size()!=0){
                criteria.andPartner_idIn(partner_ids);
                list= this.partnerMapper.selectByExample(partnerExample);
            }
		}else{//有，就查询出除此之外的
            partner_ids.removeAll(longs);
            if(partner_ids.size()!=0){
                criteria.andPartner_idIn(partner_ids);
                list = this.partnerMapper.selectByExample(partnerExample);
            }
		}
        return list;
	}

	/**
	 * 新增派单设置里面的合伙人
	 * @param str
	 * @param product_id
	 * @return
	 */
	@Override
	@Transactional(timeout = 5)
	public int xAddOrderPartner(String str,Long product_id) {
		//先删除之前的数据
		ProductSoleExample productSoleExample = new ProductSoleExample();
		productSoleExample.or().andProduct_idEqualTo(product_id);
		this.productSoleMapper.deleteByExample(productSoleExample);
		//重新添加数据
		List<Long> list = JSONArray.parseArray(str,Long.class);
		Product product = this.selectByPrimaryKey(product_id);
		int num=0;
		if(list.size()==0){
			product.setSole(0);
			num = this.updateByPrimaryKey(product);
		}else{
			list.stream().forEach(id ->{
				ProductSole productSole=new ProductSole();
				productSole.setProduct_id(product_id);
				productSole.setPartner_id(id);
				this.productSoleMapper.insert(productSole);
			});
			product.setSole(1);
			num = this.updateByPrimaryKey(product);
		}
		return num;
	}

	@Override
	public List<ProductWithCoupon> xStreamProduct(List<Product> list,Long customer_id) {

		Date now  = new Date();
		CouponExample couponExample  = new CouponExample();
		couponExample.or()
				.andUse_start_datetimeLessThan(now)
				.andUse_end_datetimeGreaterThan(now)
				.andReceive_start_datetimeLessThan(now)
				.andReceive_end_datetimeGreaterThan(now)
				.andTypeEqualTo(3)
                .andValidEqualTo(1);

		List<Coupon> coupons = couponService.selectByExample(couponExample);
		Map<Long,Coupon> map  = new HashMap<>();
		try {
			for (Coupon coupon : coupons) {
				switch (coupon.getCondition_type()) {
					case 1:
					case 3:
						map.put(0L, coupon);
						break;
					case 2:
					case 4:
						String condition = coupon.getCondition();
						Condition_type condition_type = JSON.parseObject(condition, Condition_type.class);
						for (Long product_id : condition_type.getList_product()) {
							map.put(product_id, coupon);
						}
						break;
				}

			}
		} catch (Exception e) {
			//noting to do
		}

		return list.stream().map(t -> {

			OrderItemExample orderItemExample = new OrderItemExample();
			orderItemExample.or().andProduct_idEqualTo(t.getProduct_id());
			int num = orderItemService.xSumNum(orderItemExample);
			t.setBase_buyed(t.getBase_buyed() + num);
			//筛选
			ProductWithCoupon productWithCoupon = new ProductWithCoupon();
			BeanUtils.copyProperties(t,productWithCoupon);
			Coupon coupon =map.get(t.getProduct_id());
			coupon = coupon==null?map.get(0L):coupon;
			if(coupon!=null){
				productWithCoupon.setCoupon_id(coupon.getCoupon_id());
				productWithCoupon.setCoupon_name(coupon.getName());
				//customer_id 目前还不需要进行
				productWithCoupon.setProgramme_type(coupon.getProgramme_type());
				productWithCoupon.setHave(true);
			}

			if(productWithCoupon.getUnit()==null && productWithCoupon.getPsku_id_first()!=null){
				ProSku  proSku  = proSkuService.selectByPrimaryKey(productWithCoupon.getPsku_id_first());
				if(proSku!=null) {
					productWithCoupon.setUnit(proSku.getUnit());
					productWithCoupon.setPrice_first(proSku.getPrice());
					productWithCoupon.setPricev_first(proSku.getPricev());
				}
			}
			//Img img
			String imgstring  =  productWithCoupon.getLite_image();
			try {
				Img img  = JSON.parseObject(imgstring,Img.class);
				productWithCoupon.setLite_image(img.getUrl());
			}catch (Exception e){
				//notingtodo
			}

			return productWithCoupon;
		}).collect(Collectors.toList());
	}
}