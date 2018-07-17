package com.aobei.trainapi.server.impl;

import com.alibaba.fastjson.JSON;
import com.aobei.train.model.*;
import com.aobei.train.model.ProductExample.Criteria;
import com.aobei.train.service.CustomerService;
import com.aobei.train.service.OrderItemService;
import com.aobei.train.service.ProEvaluateService;
import com.aobei.train.service.ProductService;
import com.aobei.trainapi.schema.TokenUtil;
import com.aobei.trainapi.server.ApiProductService;
import com.aobei.trainapi.server.bean.Img;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.Evaluate;
import custom.bean.EvaluateBase;
import custom.bean.ProductWithCoupon;
import custom.bean.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ApiProductServiceImpl implements ApiProductService {

    Logger logger = LoggerFactory.getLogger(ApiProductServiceImpl.class);
    @Autowired
    ProductService productService;
    @Autowired
    ProEvaluateService proEvaluateService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderItemService orderItemService;
   @Autowired
   TokenUtil TOKEN;

    /**
     * 获取首页需要展示的产品列表
     * 按照sort_num 正序排序
     *
     * @return
     */
    @Override
    public List<ProductWithCoupon> homePageProducts(Integer pageIndex, Integer pages, String city_id, Long customer_id) {
        logger.info("api-method:homePageProducts:params pageIndex:{},pages:{},city_id:{},customer_id:{}", pageIndex, pages,city_id,customer_id);
        List<Integer> recommends = new ArrayList<>();
        recommends.add(0);
        recommends.add(2);
        ProductExample example = new ProductExample();
        Criteria criteria = example.or()
                .andOnlineEqualTo(Status.OnlineStatus.online.value)
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andRecommendIn(recommends);
        String client_id = TOKEN.getClientId();
        productRepeat(criteria, client_id, city_id);
        example.setOrderByClause(ProductExample.C.sort_num + " ASC");
        List<Product> list  = productService.selectByExample(example, pageIndex, pages).getList();
        return productService.xStreamProduct(list,customer_id);
    }

    /**
     * 获取推荐位的产品列表
     * @return
     */
    //取消缓存添加
    //@Cacheable(value = "recommendProducts",key = "#root.methodName",unless = "#result == null")
    @Override
    public List<ProductWithCoupon> recommendProducts(String city_id,Long customer_id) {
        logger.info("api-method:recommendProducts:params  city_id:{},customer_id:{}",city_id,customer_id);
        List<Integer> recommends = new ArrayList<>();
        recommends.add(1);
        recommends.add(2);
        ProductExample example = new ProductExample();
        Criteria criteria = example.or()
        		.andOnlineEqualTo(Status.OnlineStatus.online.value)
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andRecommendIn(recommends);
        String client_id = TOKEN.getClientId();
        productRepeat(criteria, client_id, city_id);
        example.setOrderByClause(ProductExample.C.sort_num + " ASC");
        return productService.xStreamProduct(productService.selectByExample(example),customer_id);
    }

    /**
     * 重复方法
     */
    private void productRepeat(Criteria criteria,String client_id,String city_id){
    	if("wx_m_custom".equals(client_id)){
        	criteria.andShow_in_smallappEqualTo(1);
        }else if("a_custom".equals(client_id)){
        	criteria.andShow_in_appEqualTo(1);
        }else if("i_custom".equals(client_id)){
        	criteria.andShow_in_appEqualTo(1);
        }else if("h5_custom".equals(client_id)){
            criteria.andShow_in_mobileEqualTo(1);
        }else{
        	criteria.andProduct_idEqualTo(0l);
        }
        Integer city = 0;
        try {
           city = Integer.parseInt(city_id);
        }catch (Exception e){
    	    //nothingtodo
        }

    	criteria.andCity_idEqualTo(city);
    }
    /**
     * 获取产品评价的基本统计嘻嘻
     * @param product_id 产品ID
     * @param num 指定条数
     * @return
     */
    @Cacheable(value = "productEvaluateBase",key = "'product_id:'+#product_id+':'+#num",unless = "#result == null")
    @Override
    public EvaluateBase productEvaluateBase(Long product_id, Integer num) {
        logger.info("api-method:productEvaluateBase:params product_id:{},num:{}", product_id, num);
        EvaluateBase base = new EvaluateBase();

        if (num != null && num>0) {
            Page<Evaluate> page = productEvaluates(product_id, 1, num, true);
            base.setTotal(page.getTotal());
            base.setEvaluates(page.getList());
        }else {
            base.setTotal(0L);
            base.setEvaluates(new ArrayList<>());
        }
        ProEvaluateExample example = new ProEvaluateExample();
        example.or()
                .andProduct_idEqualTo(product_id)
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andVerifyEqualTo(1);
        Double avgScore = proEvaluateService.xAvgScore(example);
        avgScore = avgScore == null ? 5 : avgScore;
        base.setAvgScore(avgScore);
        //进行好评率的百分比计算。
        int maxScore = 5;
        double percent = avgScore / maxScore * 100;
        base.setPercent(Integer.parseInt(Math.round(percent) + ""));
        base.setPercentStr(Math.round(percent) + "%");
        return base;
    }

    /**
     * 获取评价信息列表
     * @param product_id 产品
     * @param pageIndex 页码
     * @param pages 展示条数
     * @return
     */
    @Override
    public Page<Evaluate> productEvaluates(Long product_id, Integer pageIndex, Integer pages,boolean top) {
        logger.info("api-method:productEvaluates:params product_id:{},pageIndex:{},pages:{}",product_id,pageIndex,pages);
        ProEvaluateExample proEvaluateExample = new ProEvaluateExample();
        proEvaluateExample.or()
                .andProduct_idEqualTo(product_id)
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andVerifyEqualTo(1);
        if (top){
            StringBuilder builder  = new StringBuilder();
            builder.append(ProEvaluateExample.C.score);
            builder.append(" desc,");
            builder.append(ProEvaluateExample.C.create_datetime);
            builder.append(" desc");
            proEvaluateExample.setOrderByClause(builder.toString());
        }else {
            proEvaluateExample.setOrderByClause(ProEvaluateExample.C.create_datetime + " desc");
        }
        Page<ProEvaluate> pro = proEvaluateService.selectByExample(proEvaluateExample, pageIndex, pages);
        logger.info("api-method:productEvaluates:process List<ProEvaluate>:{}",pro.getList());
        Set<Long> customerIdSet = pro.getList().stream()
                .map(ProEvaluate::getCustomer_id)
                .collect(Collectors.toSet());
        List<Long> customerIdList = new ArrayList<>(customerIdSet);
        Map<Long,Customer>  map = customerMap(customerIdList);
        List<Evaluate> evaluates = pro.getList().stream().map(t -> {
            Evaluate evaluate = new Evaluate();
            BeanUtils.copyProperties(t, evaluate);
            if(map!=null){
                Customer customer = map.get(t.getCustomer_id());
                if(customer!=null) {
                    evaluate.setName(customer.getName());
                    evaluate.setPhone(customer.getPhone());
                    evaluate.setAvatar(customer.getLogo_img());
                }
            }
            return evaluate;

        }).collect(Collectors.toList());
        Page<Evaluate> page = new Page<>(pro.getPageNo(), pro.getPageSize());
        page.setList(evaluates);
        page.setTotal(pro.getTotal());
        page.setCurrent(pro.getCurrent());

        return page;
    }

    @Cacheable(value = "productEvaluatesList",key = "'product_id:'+#product_id+':'+#pageIndex+':'+#pages",unless = "#result == null")
    @Override
    public List<Evaluate> productEvaluatesList(Long product_id, Integer pageIndex, Integer pages) {
        return productEvaluates(product_id,pageIndex,pages,false).getList();
    }
    /*********************************************************************************************
     *              我是英俊潇洒，玉树临风，貌赛潘安， 你们爱谁谁，我就是私有方法的的分割线                *
     **********************************************************************************************/

    private  Map<Long,Customer> customerMap(List<Long> customerIdList){
        Map<Long,Customer>  map = new HashMap<>();
        if(customerIdList!=null && customerIdList.size()>0){
            CustomerExample customerExample  = new CustomerExample();
            customerExample.or().andCustomer_idIn(customerIdList);
            List<Customer>  customers  = customerService.selectByExample(customerExample);
            map = customers.stream().map(t->{
                try {
                    if(t!=null && t.getLogo_img() != null){
                        Img img = JSON.parseObject(t.getLogo_img(),Img.class);
                        t.setLogo_img(img.getUrl());
                    }
                }catch (Exception e){
                    logger.warn("Customer{} customer Logo image is not exits",t.getCustomer_id());
                }
                return t;
            }).collect(Collectors.toMap(Customer::getCustomer_id, Function.identity()));
        }
        return map;
    }
}
