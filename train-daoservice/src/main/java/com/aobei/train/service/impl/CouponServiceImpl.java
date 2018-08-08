package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSON;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.CouponMapper;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Service
public class CouponServiceImpl extends MbgServiceSupport<CouponMapper, Long, Coupon, Coupon, CouponExample> implements CouponService {

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    CouponReceiveService couponReceiveService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CouponService couponService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CacheReloadHandler cacheReloadHandler;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    private UsersService usersService;
    @Autowired
    private SysUsersService sysUsersService;

    @Autowired
    private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory) {
        super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(couponMapper);
    }

    @Override
    public List<CouponResponse> selectByUid(Map<String, Object> map) {
        return couponMapper.selectByUid(map);
    }


    public List<CouponResponse> filterByProductId(List<CouponResponse> list, Long product_id, int price) {
        return list.stream().map(t -> {
            int type = t.getCondition_type();
            Condition_type condition = null;
            try {
                condition = JSON.parseObject(t.getCondition(), Condition_type.class);
            } catch (Exception e) {
                return null;
            }
            switch (type) {
                case 1:
                    if (condition.getValue() <= price) {
                        return t;
                    }
                    break;
                case 2:
                    if (condition.getList_product().contains(product_id)
                            && condition.getValue() <= price) {
                        return t;
                    }
                    break;
                case  3:
                    return t;
                case 4:
                    if (condition.getList_product().contains(product_id)){
                        return  t;
                    }
                    break;

            }
            return null;
        }).filter(t -> t != null).collect(Collectors.toList());

    }

    public List<CouponResponse> mapCoupon(List<CouponResponse> list) {

        return list.stream().map(t -> {
            if (new Date().after(t.getUseEndTime())) {
                t.setExpire(true);
            } else {
                t.setExpire(false);
            }
            Condition_type condition = null;
            Programme_type programme = null;
            try {
                condition = JSON.parseObject(t.getCondition(), Condition_type.class);
                programme = JSON.parseObject(t.getProgramme(), Programme_type.class);
            } catch (Exception e) {
                return null;
            }

            Integer program_type = t.getProgramme_type();
            t.setCondition(condition.getTitle());
            switch (program_type) {

                case 1://已固定折扣
                    t.setValue(programme.getValue() * 10);
                    t.setUnit("折");

                    break;
                case 2://减固定价格
                    t.setValue(programme.getValue());
                    t.setUnit("元");
                    break;
            }
            return t;
        }).filter(t -> t != null).collect(Collectors.toList());
    }


    public int recalculatePrice(Coupon coupon, ProSku sku, int num) {
        if(coupon==null){
            return  -1;
        }
        Map<String, Integer> result = new HashMap<>();
        int discountPrice = 0;
        int totalPrice = sku.getPrice() * num;
        int payPrice = sku.getPrice() * num;
        Condition_type condition = null;
        Programme_type programme = null;
        try {
            condition = JSON.parseObject(coupon.getCondition(), Condition_type.class);
            programme = JSON.parseObject(coupon.getProgramme(), Programme_type.class);
        } catch (Exception e) {
            payPrice = -1;
        }
        //1 订单总价满X时，对所有商品优惠  2 对指定商品，3,所有订单优惠，4，用户注册优惠。
        int condition_type = coupon.getCondition_type();
        //优惠方案类型  1 固定折扣，2以固定价格，3减固定价格
        int programme_type = coupon.getProgramme_type();
        List<Long> list = condition.getList_product();
        switch (condition_type) {
            case 1: //满减, 对所有商品
                if (totalPrice >= condition.getValue()) {
                    payPrice = priceByCouponProgram(programme, programme_type, totalPrice);
                }
                break;
            case 2://满减，对指定商品
                if (totalPrice >= condition.getValue() && list.contains(sku.getProduct_id())) {
                    payPrice = priceByCouponProgram(programme, programme_type, totalPrice);
                }
                break;
            case 3: //下单立减
                payPrice = priceByCouponProgram(programme, programme_type, totalPrice);
                break;
            case 4://对指定商品西单李建
                if (list.contains(sku.getProduct_id())) {
                    payPrice = priceByCouponProgram(programme, programme_type, totalPrice);
                }
                break;
        }

        return payPrice;
    }

    @Override
    public DiscountData recalculatePrice(Customer customer, Long coupon_receive_id, ProSku proSku, int num) {

        DiscountData discountData = new DiscountData();
        CouponReceiveExample couponReceiveExample = new CouponReceiveExample();
        couponReceiveExample.or()
                .andCoupon_receive_idEqualTo(coupon_receive_id)
                .andUidEqualTo(customer.getCustomer_id())
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andStatusEqualTo(2);
        CouponReceive couponReceive = singleResult(couponReceiveService.selectByExample(couponReceiveExample));
        if (couponReceive == null) {
            discountData.setPayPrice(proSku.getPrice() * num);
            discountData.setDiscount(false);
            return discountData;
        }
        Coupon coupon = selectByPrimaryKey(couponReceive.getCoupon_id());
        if (coupon == null) {
            discountData.setPayPrice(proSku.getPrice() * num);
            discountData.setDiscount(false);
            return discountData;
        }
        discountData.setPayPrice(recalculatePrice(coupon, proSku, num));
        discountData.setDiscount(true);
        discountData.setCoupon(coupon);
        discountData.setCouponReceive(couponReceive);
        Map<String, Object> map = new HashMap<>();
        map.put("coupon", coupon);
        map.put("couponReceive", couponReceive);
        discountData.setDiscountData(JSON.toJSONString(map));
        return discountData;
    }

    /**
     * 优惠模版，满足条件后直接计算优惠后金额
     *
     * @param programme
     * @param programme_type
     * @param totalprice
     * @return
     */
    private int priceByCouponProgram(Programme_type programme, int programme_type, int totalprice) {
        int payPrice = totalprice;
        switch (programme_type) {
            case 1://固定折扣 取整数。非精确值
                payPrice = totalprice * programme.getValue() / 100;
                break;
            case 2://减固定价格
                payPrice = totalprice - programme.getValue();
                payPrice = payPrice > 0 ? payPrice : 0;
                break;
        }
        return payPrice;
    }

    @Override
    public Page<CouponList> xSelectCouponList(CouponExample example, int pageNo, int pageSize,int type) {
        Page<Coupon> page = this.selectByExample(example, pageNo, pageSize);
        List<CouponList> list = page.getList().stream().map(n -> {
            //优惠券已领用数量
            CouponReceiveExample couponReceiveExample_received = new CouponReceiveExample();
            com.aobei.train.model.CouponReceiveExample.Criteria or1 = couponReceiveExample_received.or();
            or1.andCoupon_idEqualTo(n.getCoupon_id())
                    .andUidIsNotNull();
            List<CouponReceive> list_received = couponReceiveService.selectByExample(couponReceiveExample_received);
            //查询优惠券已下单数量
            CouponReceiveExample couponReceiveExample_order = new CouponReceiveExample();
            com.aobei.train.model.CouponReceiveExample.Criteria or2 = couponReceiveExample_order.or();
            or2.andCoupon_idEqualTo(n.getCoupon_id())
                    .andPay_order_idIsNotNull()
                    .andUidIsNotNull();
            List<CouponReceive> list_order = couponReceiveService.selectByExample(couponReceiveExample_order);
            //单条优惠券
            CouponList couponList = new CouponList();
            couponList.setCoupon_id(n.getCoupon_id());
            //解析json数据
            String condition = n.getCondition();
            try {
                Condition_type condition_obj = JSON.parseObject(condition, Condition_type.class);
                if (2==n.getCondition_type() || 4==n.getCondition_type()) {
                    String replace = condition_obj.getTitle().replace("指定商品", "<a  href=javascript:toProducts('" + n.getCoupon_id() + "') >指定商品</a>");
                    couponList.setCondition(replace);
                } else {
                    couponList.setCondition(condition_obj.getTitle());//优惠条件模板
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (n.getUse_end_datetime().before(new Date())) {
                couponList.setValid(2);//检查是否过期,过期修改状态未 已过期
            } else {
                couponList.setValid(n.getValid());
            }
            if(1==type){
                couponList.setName(n.getName());
            }else{
                String replace = n.getName().replace(n.getName(), "<a  href=javascript:goto_detail('" + n.getCoupon_id() + "') >" + n.getName() + "</a>");
                couponList.setName(replace);
            }
            couponList.setType(n.getType());
            couponList.setPlan_money(n.getPlan_money());

            couponList.setDescript(n.getDescript());
            //两个需要判断的字段，是否排他，是否有限
            couponList.setNum_limit(n.getNum_limit());
            couponList.setExclusive(n.getExclusive());
            couponList.setNum_total(n.getNum_total());
            couponList.setNum_able(n.getNum_able());//可发放数量
            couponList.setPriority(n.getPriority());//优先级,99最大
            couponList.setCoupon_receive(list_received.size());//已领用数量
            couponList.setCoupon_order(list_order.size());//下单数量
            couponList.setPlan_money(n.getPlan_money());//预算金额
            couponList.setCondition_type(n.getCondition_type());//优惠条件类型
            couponList.setProgramme(n.getProgramme());//优惠方案模板
            couponList.setProgramme_type(n.getProgramme_type());//优惠方案类型
            couponList.setUse_start_datetime(n.getUse_start_datetime());//优惠券有效开始
            couponList.setUse_end_datetime(n.getUse_end_datetime());//优惠券有效结束
            couponList.setReceive_start_datetime(n.getReceive_start_datetime());//优惠券设定的有效领取开始日期
            couponList.setReceive_end_datetime(n.getReceive_end_datetime());//优惠券设定的有效领取结束日期
            /*-----------------------0.6 version 6/27---------------------------*/
            couponList.setVerify(n.getVerify());
            couponList.setVerify_user(n.getVerify_user());
            couponList.setCreate_user(n.getCreate_user());
            couponList.setIs_valid(n.getIs_valid());
            couponList.setVerifier_name(n.getVerify_user()==null?"":getUser(n.getVerify_user())==null?"":getUser(n.getVerify_user()).getName());
            couponList.setCreater_name(n.getCreate_user()==null?"":getUser(n.getCreate_user())==null?"":getUser(n.getCreate_user()).getName());
            return couponList;
        }).collect(Collectors.toList());
        return new Page<CouponList>(list, page.getTotal(), pageNo, pageSize);
    }

    public List<Object[]> xSelectCouponList(CouponExample example) {
        List<Coupon> list = this.selectByExample(example);
        List<Object[]> datas = list.stream().map(n -> {
            //优惠券已领用数量
            CouponReceiveExample couponReceiveExample_received = new CouponReceiveExample();
            com.aobei.train.model.CouponReceiveExample.Criteria or1 = couponReceiveExample_received.or();
            or1.andCoupon_idEqualTo(n.getCoupon_id())
                    .andUidIsNotNull();
            List<CouponReceive> list_received = couponReceiveService.selectByExample(couponReceiveExample_received);
            //查询优惠券已下单数量
            CouponReceiveExample couponReceiveExample_order = new CouponReceiveExample();
            com.aobei.train.model.CouponReceiveExample.Criteria or2 = couponReceiveExample_order.or();
            or2.andCoupon_idEqualTo(n.getCoupon_id())
                    .andPay_order_idIsNotNull()
                    .andUidIsNotNull();
            List<CouponReceive> list_order = couponReceiveService.selectByExample(couponReceiveExample_order);
            //解析json数据
            String condition = n.getCondition();
            Condition_type condition_obj = JSON.parseObject(condition, Condition_type.class);
            String programme = n.getProgramme();
            Programme_type programme_type = JSON.parseObject(programme, Programme_type.class);
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            Object[] obj = {
                n.getName(),
                n.getType()!=null?(n.getType()==1?"派发券":(n.getType()==2?"赔偿券":n.getType()==3?"领取券":(n.getType()==4?"兑换券":"无"))):"无",
                n.getNum_limit()==1?n.getNum_total():"无限制",
                list_received.size(),
                list_order.size(),
                condition_obj.getTitle()+"/"+programme_type.getTitle(),
                sd.format(n.getUse_start_datetime()).toString(),
                sd.format(n.getUse_end_datetime()).toString(),
                n.getValid()!=null?(n.getValid()==0?"无效":(n.getValid()==1?"有效":(n.getValid()==2?"已过期":"无"))):"无",
                n.getCreate_user()==null?"":getUser(n.getCreate_user()).getName(),
                n.getVerify_user()==null?"":getUser(n.getVerify_user()).getName()
            };
            return obj;
        }).collect(Collectors.toList());
        return datas;
    }

    @Override
    public List<Object[]> xSelectCouponList(CouponExample example, int type) {
        List<Coupon> list = this.selectByExample(example);
        List<Object[]> datas = list.stream().map(n -> {
            //优惠券已领用数量
            CouponReceiveExample couponReceiveExample_received = new CouponReceiveExample();
            com.aobei.train.model.CouponReceiveExample.Criteria or1 = couponReceiveExample_received.or();
            or1.andCoupon_idEqualTo(n.getCoupon_id())
                    .andUidIsNotNull();
            List<CouponReceive> list_received = couponReceiveService.selectByExample(couponReceiveExample_received);
            //查询优惠券已下单数量
            CouponReceiveExample couponReceiveExample_order = new CouponReceiveExample();
            com.aobei.train.model.CouponReceiveExample.Criteria or2 = couponReceiveExample_order.or();
            or2.andCoupon_idEqualTo(n.getCoupon_id())
                    .andPay_order_idIsNotNull()
                    .andUidIsNotNull();
            List<CouponReceive> list_order = couponReceiveService.selectByExample(couponReceiveExample_order);
            //解析json数据
            String condition = n.getCondition();
            Condition_type condition_obj = JSON.parseObject(condition, Condition_type.class);
            String programme = n.getProgramme();
            Programme_type programme_type = JSON.parseObject(programme, Programme_type.class);
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            Object[] obj = {
                    n.getName(),
                    n.getType()!=null?(n.getType()==1?"派发券":(n.getType()==2?"赔偿券":n.getType()==3?"领取券":(n.getType()==4?"兑换券":"无"))):"无",
                    n.getNum_limit()==1?n.getNum_total():"无限制",
                    list_received.size(),
                    list_order.size(),
                    condition_obj.getTitle()+"/"+programme_type.getTitle(),
                    sd.format(n.getUse_start_datetime()).toString(),
                    sd.format(n.getUse_end_datetime()).toString(),
                    n.getValid()!=null?(n.getValid()==0?"无效":(n.getValid()==1?"有效":(n.getValid()==2?"已过期":"无"))):"无",
                    n.getVerify()!=null?(n.getVerify()==0?"未提审":(n.getVerify()==1?"待提审":(n.getVerify()==2?"审核通过":(n.getVerify()==3?"审核未通过":"无")))):"无",
                    n.getVerify_user()==null?"":getUser(n.getVerify_user()).getName()
            };
            return obj;
        }).collect(Collectors.toList());
        return datas;
    }

    @SuppressWarnings("unused")
    @Override
    @Transactional
    public Map<String, String> xDistribute_coupon(Long coupon_id, String ids) {
        HashMap<String, String> map = new HashMap<String, String>();
        Coupon cou = this.couponMapper.selectByPrimaryKey(coupon_id);
        if(cou.getType()==Status.CouponType.get_type.value ){
            map.put("message", "该优惠券为领取券，不能派发！");
            return map;
        }
        if(cou.getType()==Status.CouponType.exchange_type.value ){
            map.put("message", "该优惠券为兑换券，不能派发！");
            return map;
        }
        String key = Constant.getCouponKey(cou.getCoupon_id());
        if (!redisTemplate.hasKey(key)) {
            if(cou.getNum_able()>0){
               redisTemplate.opsForValue().set(key,cou.getNum_able()+"");
            }
        }else  if(Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get(key)))==0){
            map.put("message", "派发失败！该优惠券可用数量已用完");
            redisTemplate.delete(key);
            return map;
        }
        String[] split = ids.split(",");
        if(split.length>cou.getNum_able()){
            map.put("message", "派发失败！该优惠券可用数量只能派发"+cou.getNum_able()+" 位用户");
            return map;
        }
        for (int i = 0; i < split.length; i++) {
            if (split[i].length() == 11) {
                //判断用户是否存在
                CustomerExample customerExample = new CustomerExample();
                customerExample.or().andPhoneEqualTo(split[i]);
                List<Customer> customers = customerService.selectByExample(customerExample);
                if(customers.size()>1){
                    map.put("message", "派发失败！用户手机号错误，存在两个用户！！");
                    return map;
                }
                Customer resultSingle = DataAccessUtils.singleResult(customers);
                if (resultSingle == null) {
                    map.put("message", "派发失败！用户手机号错误！请重新派发");
                    return map;
                }
            }else{
                map.put("message", "派发失败！用户手机号格式错误！请重新派发");
                return map;
            }
        }
        //循环为正确用户派发优惠券
        for (int i = 0; i < split.length; i++) {
                //判断用户是否存在
                CustomerExample customerExample = new CustomerExample();
                customerExample.or().andPhoneEqualTo(split[i]);
                Customer resultSingle = DataAccessUtils.singleResult(customerService.selectByExample(customerExample));
                    //减少当前优惠券可用数量
                    if (cou.getNum_limit() == 1) {
                        if(cou.getNum_able()<=0){
                            map.put("message", "派发失败！该优惠券可用数量已用完");
                            return map;
                        }

                        if(Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get(key)))==0){
                            map.put("message", "派发失败！该优惠券可用数量已用完");
                            return map;
                        }
                        cou.setNum_able(Integer.parseInt(redisTemplate.opsForValue().increment(key,-1L)+""));
                        couponService.updateByPrimaryKeySelective(cou);
                    }
                    //为用户添加使用记录
                    CouponReceive couponReceive = new CouponReceive();//没有订单id和使用时间
                    couponReceive.setCoupon_receive_id(IdGenerator.generateId());
                    couponReceive.setUid(resultSingle.getCustomer_id());//顾客id不是user_id
                    couponReceive.setCoupon_id(coupon_id);
                    couponReceive.setReceive_datetime(new Date());
                    couponReceive.setStatus(2);//待使用状态
                    couponReceive.setVerification(0);//未核销
                    //couponReceive.setUse_datetime(new Date());
                    couponReceive.setDeleted(Status.DeleteStatus.no.value);//未删除
                    couponReceive.setCreate_datetime(new Date());
                    couponReceiveService.insert(couponReceive);
                    //每次清除对应顾客优惠券缓存信息
                    cacheReloadHandler.couponListReload(resultSingle.getCustomer_id());
                    cacheReloadHandler.userCouponListReload(resultSingle.getCustomer_id());
                    map.put("message", "派发成功" + (i + 1) + "条！,请到优惠券使用记录查看");
            }
                    return map;
    }

    @Override
    public HashMap<String, List<Product>> getProductsList(Long coupon_id) {
        HashMap<String, List<Product>> map = new HashMap<String, List<Product>>();
        Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
        if (coupon != null) {
            String condition = coupon.getCondition();
            Condition_type condition_type = null;
            try {
                condition_type = JSON.parseObject(condition, Condition_type.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //向前台传递商品集合数据
            if (condition_type.getList_product() != null) {
                List<Product> productList = new ArrayList<Product>();
                List<Long> list_product = condition_type.getList_product();
                for (Long product_id : list_product) {
                    ProductExample productExample = new ProductExample();
                    productExample.or().andProduct_idEqualTo(product_id);
                    Product product = productService.selectByPrimaryKey(product_id);
                    productList.add(product);
                }
                map.put("productList_data", productList);
            }
        }//需要异常处理
        return map;
    }


    @Override
    public Page<CouponReceiveList> xSelectRecordDetail(CouponReceiveExample example, int pageNo, int pageSize) {
        Page<CouponReceive> page = couponReceiveService.selectByExample(example, pageNo, pageSize);
        List<CouponReceiveList> list = page.getList().stream().map(n -> {
            Customer customer = customerService.selectByPrimaryKey(n.getUid());

            //此处需要异常处理
            CouponReceiveList receiveList = new CouponReceiveList();
            receiveList.setCoupon_id(n.getCoupon_id());
            receiveList.setCoupon_receive_id(n.getCoupon_receive_id());
            receiveList.setDeleted(n.getDeleted());
            receiveList.setPay_order_id(n.getPay_order_id()==null?"无":n.getPay_order_id());
            if (customer != null) {
                receiveList.setPhone(customer.getPhone());
            }else {
                receiveList.setPhone("无");
            }
            receiveList.setCreate_datetime(n.getCreate_datetime());
            receiveList.setReceive_datetime(n.getReceive_datetime());
            receiveList.setStatus(n.getStatus());
            receiveList.setUid(n.getUid());
            receiveList.setVerification(n.getVerification());
            return receiveList;

        }).collect(Collectors.toList());
        return new Page<CouponReceiveList>(list, page.getTotal(), pageNo, pageSize);

    }

    @Transactional(timeout = 5)
    @Override
    public Integer xVerify(Long coupon_id,Integer verify,String verify_comments) {
        Coupon coupon = new Coupon();
        coupon.setCoupon_id(coupon_id);
        coupon.setVerify(verify);
        coupon.setVerify_comments(verify_comments);
        if(verify==2){
            coupon.setIs_valid(1);
        }
        return this.couponMapper.updateByPrimaryKeySelective(coupon);
    }

    @Override
    public HashMap<String, String> getEndTime(Long coupon_id) {
        Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
        HashMap<String, String> map = new HashMap<String, String>();
        Date end_datetime = coupon.getUse_end_datetime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String end_time = format.format(end_datetime);
        map.put("end_time", end_time);
        return map;
    }

    @Override
    public HashMap<String, Object> getCategoryProduct() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<Category> list = new ArrayList<Category>();
        //只加载有商品的分类
        List<Category> list_category = categoryService.selectByExample(new CategoryExample());
        for (Category category : list_category) {
            ProductExample productExample = new ProductExample();
            productExample.or().andCategory_idEqualTo(category.getCategory_id());
            List<Product> list_product = productService.selectByExample(productExample);
            if (list_product.size() > 0) {
                list.add(category);
            }
        }
        //查询所有分类对应的商品
        List<Product> list_product = productService.selectByExample(new ProductExample());
        map.put("list_product", list_product);
        map.put("list_category", list);
        return map;
    }

    private SysUsers getUser(Long user_id){
        SysUsersExample sysUsersExample = new SysUsersExample();
        sysUsersExample.or()
                .andUser_idEqualTo(user_id);
        return DataAccessUtils.singleResult(sysUsersService.selectByExample(sysUsersExample));
    }
    @Override
    public List<CouponResponse> xCouponResponse(List<CouponReceive> receives) {

        Set<Long> couponIdSet = receives.stream().map(t -> t.getCoupon_id()).collect(Collectors.toSet());
        List<Long> couponIds = new ArrayList<>(couponIdSet);
        if(couponIds.size()==0){
            return new ArrayList<>();
        }
        CouponExample example = new CouponExample();
        example.or().andCoupon_idIn(couponIds);
        List<Coupon> coupons = selectByExample(example);
        Map<Long, Coupon> map = coupons.stream().collect(Collectors.toMap(Coupon::getCoupon_id, Function.identity()));
        List<CouponResponse> responses = receives.stream().map(t -> {
            CouponResponse response = new CouponResponse();
            Coupon c = map.get(t.getCoupon_id());
            response.setStatus(t.getStatus());
            response.setName(c.getName());
            response.setCoupon_receive_id(t.getCoupon_receive_id());
            response.setCoupon_id(c.getCoupon_id());
            response.setUseEndTime(c.getUse_end_datetime());
            response.setUseStartTime(c.getUse_start_datetime());
            response.setCondition(c.getCondition());
            response.setCondition_type(c.getCondition_type());
            response.setCreate_time(c.getCreate_time());
            response.setDeleted(t.getDeleted());
            response.setUid(t.getUid());
            response.setVerification(t.getVerification());
            response.setUse_datetime(t.getUse_datetime());
            response.setDescript(c.getDescript());
            response.setExclusive(c.getExclusive());
            response.setProgramme(c.getProgramme());
            response.setProgramme_type(c.getProgramme_type());
            response.setNum_able(c.getNum_able());
            response.setNum_limit(c.getNum_limit());
            response.setNum_total(c.getNum_total());
            response.setPriority(c.getPriority());



            return response;
        }).collect(Collectors.toList());


        return mapCoupon(responses);
    }
}