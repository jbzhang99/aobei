package com.aobei.trainapi.server.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyun.openservices.ons.api.SendResult;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.RedisIdGenerator;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.model.ProductExample.Criteria;
import com.aobei.train.service.*;
import com.aobei.train.service.impl.StoreServiceImpl;
import com.aobei.trainapi.configuration.CustomProperties;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.schema.TokenUtil;
import com.aobei.trainapi.schema.input.CustomerAddressInput;
import com.aobei.trainapi.schema.input.OrderInput;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.ApiOrderService;
import com.aobei.trainapi.server.ApiUserService;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.PayService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.Img;
import com.aobei.trainapi.server.bean.MessageContent;
import com.aobei.trainapi.server.handler.InStationHandler;
import com.aobei.trainapi.server.handler.OnsHandler;
import com.aobei.trainapi.server.handler.PushHandler;
import com.aobei.trainapi.server.handler.SmsHandler;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.*;
import custom.bean.OrderInfo.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.bean.paymch.MchOrderInfoResult;
import weixin.popular.bean.paymch.UnifiedorderResult;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Service
public class CustomerApiServiceImpl implements CustomerApiService {
    @Autowired
    RedisService redisService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ProductService productService;
    @Autowired
    ProSkuService proSkuService;
    @Autowired
    CustomerAddressService customerAddressService;
    @Autowired
    OrderService orderService;
    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    TrainCityService trainCityService;
    @Autowired
    CouponService couponService;
    @Autowired
    CouponReceiveService couponReceiveService;
    @Autowired
    StationService stationService;
    @Autowired
    StudentService studentService;
    @Autowired
    UsersService usersService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    CustomProperties properties;
    @Autowired
    CmsBannerService cmsBannerService;
    @Autowired
    OssImgService ossImgService;
    @Autowired
    ProEvaluateService proEvaluateService;
    @Autowired
    UsersWxService usersWxService;
    @Autowired
    PayService payService;
    @Autowired
    PayWxNotifyService payWxNotifyService;
    @Autowired
    WxAppService wxAppService;
    @Autowired
    WxMchService wxMchService;
    @Autowired
    CouponEnvService couponEnvService;
    @Autowired
    EventPublisher publisher;
    @Autowired
    MetadataService metadataService;
    @Autowired
    OnsHandler onsHandler;
    @Autowired
    StoreService storeService;
    @Autowired
    ServiceunitPersonService serviceunitPersonService;
    @Autowired
    ProductSoleService productSoleService;
    @Autowired
    RedisIdGenerator redisIdGenerator;
    @Autowired
    SmsHandler smsHandler;
    @Autowired
    ApiOrderService apiOrderService;
    @Autowired
    CacheReloadHandler cacheReloadHandler;
    @Autowired
    Environment env;
    @Autowired
    TokenUtil TOKEN;
    @Autowired
    MessageService messageService;
    @Autowired
    ApiUserService apiUserService;
    @Autowired
    CancleStrategyService cancleStrategyService;
    @Autowired
    PushHandler pushHandler;
    @Autowired
    RobbingService robbingService;
    @Autowired
    InStationHandler inStationHandler;

    Logger logger = LoggerFactory.getLogger(CustomerApiServiceImpl.class);
    //分页固定数值
    Integer pageNum = 10;

    /**
     * 获取用户绑定的Customer信息。
     *
     * @param user_id 用户ID
     * @return Customer 对象
     */
    @Cacheable(value = "customerInfo", key = "'user_id:'+#user_id", unless = "#result == null")
    @Override
    public Customer customerInfo(Long user_id) {
        if (user_id == null) {
            logger.error("api-method:customerInfo:params illegal user_id:{}", user_id);
            return null;
        }
        logger.info("api-method:customerInfo:params user_id:{}", user_id);
        CustomerExample example = new CustomerExample();
        example.or().andUser_idEqualTo(user_id);
        Customer customer = singleResult(customerService.selectByExample(example));
        logger.info("api-method:customerInfo:process customer:{}", customer);
        try {
            if (customer != null && customer.getLogo_img() != null) {
                Img img = JSON.parseObject(customer.getLogo_img(), Img.class);
                customer.setLogo_img(img.getUrl());
            }
        } catch (Exception e) {
            logger.warn("api-method:customerInfo  customer Logo image is not exits");
        }
        return customer;
    }

    /**
     * 验证验证码 ，此处不区分是哪一种操作要求验证验证码
     *
     * @param user_id 顾客的用户id
     * @param code    验证码
     * @param phone   手机号码
     * @return true or false
     */
    @Override
    public boolean checkVerificationCode(Long user_id, String code, String phone) {
        logger.info("api-method:checkVerificationCode:params user_id:{},code:{},phone:{}", user_id, code, phone);
        String redisKey = Constant.getVerificationCodeKey(user_id, phone);
        String codeCatch = redisService.getStringValue(redisKey);
        if (StringUtils.isNotEmpty(codeCatch) && StringUtils.equals(codeCatch, code)) {
            // 如果验证码验证成功，删除该验证码！
            redisService.delete(redisKey);
            return true;
        }
        return false;
    }

    /**
     * 检查短信发送次数 短信验证码 ： 使用同一个签名，对同一个手机号码发送短信验证码，1条/分钟，5条/小时，10条/天。
     * 一个手机号码通过阿里云短信服务平台只能收到40条/天。 （天的计算方式是是当下时间往后推24小时，例如2017年8月24日：11:00发送一条验证码短信，
     * 计算限流方式是2017年8月23日11:00点到8月24日：11:00点，是否满40条）
     * 如您是在发送验证码时提示业务限流，建议您根据以上业务调整接口调用时间 短信通知：
     * 使用同一个签名和同一个短信模板ID，对同一个手机号码发送短信通知，支持50条/日
     * （天的计算方式是是当下时间往后推24小时，例如2017年8月24日：11:00发送一条短信，
     * 计算限流方式是2017年8月23日11:00点到8月24日：11:00点，是否满50条）。
     * 如您是在发送验证码时提示业务限流，建议您根据以上业务调整接口调用时间
     *
     * @param phone 电话号码
     * @param type  code 验证码类 note 通知类
     * @return int 大与0，代表发送过短信
     */
    @Override
    public int smsCount(String phone, String type) {
        logger.info("api-method:smsCount:params phone:{},type:{}", phone, type);
        String key = Constant.getMaxnumSendKey(phone, type);
        String numString = redisService.getStringValue(key);
        if (StringUtils.isNumeric(numString))
            return Integer.parseInt(numString);
        return 0;
    }

    /**
     * 获取首页中需要展示的分类列表
     *
     * @return list
     */
    @Cacheable(value = "homeCategoryList", key = "#root.methodName", unless = "#result == null")
    @Override
    public List<Category> homeCategoryList() {
        CategoryExample example = new CategoryExample();
        example.or().andStateEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value).andPidIsNull();
        List<Category> categoryList = categoryService.selectByExample(example, 1, 4).getList();
        return categoryList;
    }

    /**
     * 根据 分类的层级code 获取分类下的所有产品，客户端不需要知道具体多少页， 所以不需要返回page对象
     *
     * @param category_level_code 层级code
     * @param page_index          页码
     * @param count               条数
     * @return list
     */
    @Cacheable(value = "productListByCategoryLevelCode", key = "#category_level_code+':'+#page_index+':'+#count", unless = "#result == null")
    @Override
    public List<ProductWithCoupon> productListByCategoryLevelCode(String category_level_code, int page_index, int count, String city_id, Long customer_id) {
        logger.info("api-method:productListByCategoryLevelCode:params category_level_code:{},page_index:{},count:{}", category_level_code
                , page_index, count);
        if (city_id == null) {
            city_id = "110100";
        }
        ProductExample example = new ProductExample();
        Criteria criteria = example.or()
                .andCategory_level_codeLike(category_level_code + "%")
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andOnlineEqualTo(1);
        String client_id = TOKEN.getClientId();
        productRepeat(criteria, client_id, city_id);
        example.setOrderByClause(ProSkuExample.C.sort_num + " ASC");
        Page<Product> products = productService.selectByExample(example, page_index, count);
        List<ProductWithCoupon> list = productService.xStreamProduct(products.getList(), customer_id);
        return list;
    }

    /**
     * 获取展示在首页的产品累表
     *
     * @param page_index 页码 从1开始
     * @param count      每页显示条数
     * @return list
     */
    @Deprecated
    @Cacheable(value = "homeProductList", key = "#page_index+':'+#count", unless = "#result == null")
    @Override
    public List<ProductWithCoupon> homeProductList(int page_index, int count, String city_id, Long customer_id) {
        logger.info("api-method:homeProductList:params page_index:{},count:{}", page_index, count);
        if (city_id == null) {
            city_id = "110100";
        }
        ProductExample example = new ProductExample();
        Criteria criteria = example.or().andDeletedEqualTo(Status.DeleteStatus.no.value).andOnlineEqualTo(1);
        String client_id = TOKEN.getClientId();
        productRepeat(criteria, client_id, city_id);
        example.setOrderByClause(ProductExample.C.sort_num + " ASC");
        Page<Product> products = productService.selectByExample(example, page_index, count);
        return productService.xStreamProduct(products.getList(), customer_id);
    }

    /**
     * 重复方法
     */
    private void productRepeat(Criteria criteria, String client_id, String city_id) {
        if ("wx_m_custom".equals(client_id)) {
            criteria.andShow_in_smallappEqualTo(1);
        } else if ("a_custom".equals(client_id)) {
            criteria.andShow_in_appEqualTo(1);
        } else if ("i_custom".equals(client_id)) {
            criteria.andShow_in_appEqualTo(1);
        } else {
            criteria.andProduct_idEqualTo(0l);
        }
        Integer city = 0;
        try {
            city = Integer.parseInt(city_id);
        } catch (Exception e) {
            //nothingtodo
        }
        criteria.andCity_idEqualTo(city);
    }

    /**
     * 获得应用自己的banner图
     *
     * @param appid    应用名称
     * @param position 位置
     * @return
     */
    @Deprecated
    @Cacheable(value = "bannerList", key = "#appid+':'+#position", unless = "#result == null")
    @Override
    public List<OssImg> bannerList(String appid, String position) {
        logger.info("api-method:bannerList:params appid:{},position:{}", appid, position);
        CmsBannerExample example = new CmsBannerExample();
        example.or().andAppEqualTo(appid)
                .andSignEqualTo(1)
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andOnline_datetimeLessThanOrEqualTo(new Date())
                .andOffline_datetimeGreaterThanOrEqualTo(new Date());
        example.setOrderByClause(CmsBannerExample.C.serial_number + " ASC");
        List<CmsBanner> list = cmsBannerService.selectByExample(example);
        List<OssImg> list1 = list.stream().map(t -> {
            OssImg ossImg = ossImgService
                    .selectByPrimaryKey(Long.parseLong(t.getImg_cover() == null ? "0" : t.getImg_cover()));
            return ossImg;
        }).collect(Collectors.toList());
        return list1;
    }

    /**
     * 获取一个产品的详情
     *
     * @param product_id 产品ID
     * @return productInfo 自定义对象
     */
    // @Cacheable(value = "productDetail",key = "'product_id:'+#product_id",unless = "#result == null")
    @Override
    public ApiResponse<ProductInfo> productDetail(Long product_id, Long customer_id) {
        logger.info("api-method:productDetail:params product_id:{}", product_id);
        ApiResponse<ProductInfo> response = new ApiResponse<>();
        if (product_id == null) {
            response.setErrors(Errors._41040);
            return response;
        }
        Product product = productService.selectByPrimaryKey(product_id);
        List<ProSku> proSkus = proSkuList(product.getProduct_id(), 1, 20);
        if (product == null || proSkus.size() == 0) {
            response.setErrors(Errors._41006);
            return response;
        }
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.or().andProduct_idEqualTo(product_id);
        Integer num = orderItemService.xSumNum(orderItemExample);
        product.setBase_buyed(product.getBase_buyed() + num);
        String tag_images = product.getTag_images();
        ProductInfo productInfo = new ProductInfo();
        if (StringUtils.isNotEmpty(tag_images)) {
            List<ProductTag> productTags = JSONArray.parseArray(tag_images, ProductTag.class);
            productInfo.setProductTags(productTags);
        } else {
            productInfo.setProductTags(new ArrayList<>());
        }
        Customer customer = new Customer();
        if (customer_id != null) {
            customer = customerService.selectByPrimaryKey(customer_id);
        } else {
            customer = null;
        }
        List<CouponResponse> couponResponses = apiOrderService.queryConponList(customer, product_id, 1, 100);
        couponResponses = couponResponses.stream()
                .filter(t -> (!t.isExpire() && new Integer(3).equals(t.getType()))).collect(Collectors.toList());
        if (couponResponses == null) {
            couponResponses = new ArrayList<>();
        }
        productInfo.setCouponResponse(couponResponses);
        //Img img
        String imgstring = product.getLite_image();
        try {
            custom.bean.Img img = JSON.parseObject(imgstring, custom.bean.Img.class);
            product.setLite_image(img.getUrl());
        } catch (Exception e) {
            logger.warn("product{} liteImage not exits", product_id);
        }
        productInfo.setProduct(product);
        productInfo.setProSkus(proSkus);
        response.setT(productInfo);
        return response;
    }

    /**
     * 获取产品的sku列表
     *
     * @param product_id 产品ID
     * @param page_index 页码 从1开始
     * @param count      每页显示条数
     * @return list
     */
    @Cacheable(value = "proSkuList", key = "'product_id:'+#product_id+':'+#page_index+':'+#count", unless = "#result == null")
    @Override
    public List<ProSku> proSkuList(Long product_id, int page_index, int count) {
        logger.info("api-method:proSkuList:params product_id:{},page_index:{},count:{}", product_id, page_index
                , count);
        ProSkuExample example = new ProSkuExample();
        example.or().andDeletedEqualTo(Status.DeleteStatus.no.value).andProduct_idEqualTo(product_id).andDispalyEqualTo(0);
        example.setOrderByClause(ProSkuExample.C.sort_num + " ASC");
        Page<ProSku> proSkus = proSkuService.selectByExample(example, page_index, count);
        return proSkus.getList();
    }

    /**
     * 获取用户服务地址列表
     *
     * @param customer_id 顾客ID
     * @return list
     */
    @Cacheable(value = "addressList", key = "'customer_id:'+#customer_id", unless = "#result == null")
    @Override
    public List<CustomerAddress> addressList(Long customer_id) {
        logger.info("api-method:addressList:params customer_id:{}", customer_id);
        CustomerAddressExample example = new CustomerAddressExample();
        example.or().andCustomer_idEqualTo(customer_id).andDeletedEqualTo(Status.DeleteStatus.no.value);
        example.setOrderByClause(CustomerExample.C.create_datetime + " desc");
        return customerAddressService.selectByExample(example);
    }

    /**
     * 获取默认的服务无地址
     *
     * @param customer_id 顾客地址ID
     * @return CustomerAddress
     */
    @Cacheable(value = "getDefaultAddress", key = "'customer_id:'+#customer_id", unless = "#result == null")
    @Override
    public CustomerAddress getDefaultAddress(Long customer_id) {
        logger.info("api-method:getDefaultAddress:params customer_id:{}", customer_id);
        CustomerAddressExample example = new CustomerAddressExample();
        example.or().andCustomer_idEqualTo(customer_id).andDefault_addressEqualTo(1)
                .andDeletedEqualTo(Status.DeleteStatus.no.value);
        CustomerAddress customerAddress = singleResult(customerAddressService.selectByExample(example));
        if (customerAddress == null){
            CustomerAddressExample updateExample = new CustomerAddressExample();
            updateExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value)
                    .andCustomer_idEqualTo(customer_id);
            updateExample.setOrderByClause(CustomerAddressExample.C.update_datetime + " desc");
            updateExample.limit(0L, 1L);
            customerAddress = singleResult(customerAddressService.selectByExample(updateExample));
        }
        return customerAddress;
    }

    /**
     * 获取一个sku可选的服务时间 步骤1 找到所有支持该服务的供应商 步骤2 查看所有的sku提供的时间端 步骤3 计算库存 步骤4 组织返回数据对象
     *
     * @param customer            顾客
     * @param product_id          产品ID
     * @param psku_id             产品skuID
     * @param customer_address_id 顾客服务地址
     * @return list
     */
    @Override
    public List<TimeModel> productAvailableTimes(Customer customer, Long product_id, Long psku_id,
                                                 Long customer_address_id, int num) {
        logger.info("api-method:productAvailableTimes:params product_id:{},pskuid:{},customer_address_id:{},num:{}", product_id, psku_id
                , customer_address_id, num);
        Long starttime = System.currentTimeMillis();
        CustomerAddress customerAddress = customerAddressService.selectByPrimaryKey(customer_address_id);
        Product product = productService.selectByPrimaryKey(product_id);
        ProSku proSku = proSkuService.selectByPrimaryKey(psku_id);
        List<TimeModel> timeModels = new ArrayList();
        // 基础数据不存在，不能计算可用时间
        Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_RESERVE_DAYS_SPAN);
        Integer span = metadata == null ? Constant.MAXNUM_RESERVATION_SPAN : metadata.getMeta_value() == null ? Constant.MAXNUM_RESERVATION_SPAN
                : Integer.parseInt(metadata.getMeta_value());
        if (customerAddress == null || product == null || proSku == null || customerAddress.getDeleted() == 1) {
            logger.info("api-method:productAvailableTimes:process fail customerAddress:{},product:{},prosku:{}", customerAddress
                    , product, proSku);
            return storeService.meargeTimeModel(timeModels, span, proSku);
        }
        metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
        Integer radius = metadata == null ? Constant.SEARCH_RADIUS : metadata.getMeta_value() == null ? Constant.SEARCH_RADIUS
                : Integer.parseInt(metadata.getMeta_value());
        logger.info("api-times:base times:{}", System.currentTimeMillis() - starttime);
        starttime = System.currentTimeMillis();
        //判断产品是否绑定合伙人来获取可选的站点
        List<Station> stations = null;
        ProductSoleExample soleExample = new ProductSoleExample();
        soleExample.or().andProduct_idEqualTo(product_id);
        List<ProductSole> productSoles = productSoleService.selectByExample(soleExample);
        if (productSoles.size() != 0) {//绑定合伙人的产品
            List<Long> partner_ids = productSoles.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
            StationExample stationExample = new StationExample();
            stationExample.or().andPartner_idIn(partner_ids).andCityEqualTo(customerAddress.getCity());
            stations = stationService.selectByExample(stationExample);
            logger.info("api-times:getstations1 times:{}", System.currentTimeMillis() - starttime);
        } else {//未绑定的
            stations = stationService.findNearbyStation(customerAddress, radius);
            stations = stationService.filterByProduct(product, stations);
            logger.info("api-times:getstations2 times:{}", System.currentTimeMillis() - starttime);
        }
        starttime = System.currentTimeMillis();
        timeModels = storeService.stationsTimeModel(stations, span, proSku, proSku.getBuy_multiple_o2o() == 1 ? num : 1);
        logger.info("api-times:getTimeModel times:{}", System.currentTimeMillis() - starttime);
        return timeModels;
    }

    /**
     * 目前，优惠券还没有和产品尽心关联
     *
     * @param customer   顾客
     * @param page_index 页码
     * @param count      条数
     * @return list
     */
    @Cacheable(value = "couponList", key = "'customer_id:'+#customer.customer_id+':'+#page_index+':'+#count", unless = "#result.size() == 0")
    @Override
    public List<CouponResponse> couponList(Customer customer, int page_index, int count) {
        logger.info("api-method:couponList:params customer:{},page_index:{},count:{}", customer, page_index, count);
        Map<String, Object> map = new HashMap<>();
        map.put("uid", customer.getCustomer_id());
        int limitStart = (page_index - 1) * count;
        int limitEnd = count;
        map.put("limitStart", limitStart);
        map.put("limitEnd", limitEnd);
        List<CouponResponse> list = couponService.selectByUid(map);
        return couponService.mapCoupon(list);
    }

    @Override
    public List<CouponResponse> expireCouponList(Customer customer, int pageIndex) {
        CouponReceiveExample example = new CouponReceiveExample();
        example.or().andUidEqualTo(customer.getCustomer_id())
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andStatusEqualTo(5);
        List<CouponReceive> couponReceives = couponReceiveService.selectByExample(example, pageIndex, pageNum).getList();
        return couponService.xCouponResponse(couponReceives);
    }

    @Override
    public List<CouponResponse> usedCouponList(Customer customer, int pageIndex) {
        CouponReceiveExample example = new CouponReceiveExample();
        List<Integer> status = new ArrayList<>();
        status.add(3);
        status.add(4);
        example.or().andUidEqualTo(customer.getCustomer_id())
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andStatusIn(status);
        List<CouponReceive> couponReceives = couponReceiveService.selectByExample(example, pageIndex, pageNum).getList();
        return couponService.xCouponResponse(couponReceives);
    }

    /**
     * 根据产品来过滤优惠券
     *
     * @param customer 顾客
     * @param psku_id  产品skuID
     * @param num      购买数量
     * @return
     */
    @Cacheable(value = "userCouponList", key = "'customer_id:'+#customer.customer_id+':'+'psku_id:'+#psku_id+':'+#page_index+':'+#count", unless = "#result.size() == 0")
    @Override
    public List<CouponResponse> userCouponList(Customer customer, Long psku_id, int num, int page_index, int count) {
        logger.info("api-method:userCouponList:params customer:{},page_index:{},count:{}", customer, page_index, count);
        Map<String, Object> map = new HashMap<>();
        map.put("uid", customer.getCustomer_id());
        int limitStart = (page_index - 1) * count;
        int limitEnd = count;
        map.put("limitStart", limitStart);
        map.put("limitEnd", limitEnd);
        ProSku proSku = proSkuService.selectByPrimaryKey(psku_id);
        int price = proSku.getPrice() * num;
        List<CouponResponse> list = couponService.selectByUid(map);
        list = couponService.filterByProductId(list, proSku.getProduct_id(), price);
        return couponService.mapCoupon(list);
    }

    /**
     * 根据数量。产品。优惠券等计算价格。 不同的优惠券对应不同价格计算方法。现在此方法内进行直接计算。
     * 当优惠券各种类型，各种优惠方案确定后，封装完整的价格计算方法。
     *
     * @param psku_id           产品skuID
     * @param coupon_receive_id 分配给顾客的优惠券ID
     * @param num               数量。
     * @return
     */
    @Override
    public ApiResponse<Integer> recalculatePrice(Customer customer, Long psku_id, Long coupon_receive_id, int num) {
        logger.info("api-method:recalculatePrice:params customer:{},psku_id:{},coupon_receive_id:{},num:{}", customer, psku_id, coupon_receive_id, num);
        ApiResponse<Integer> response = new ApiResponse<>();
        ProSku proSku = proSkuService.selectByPrimaryKey(psku_id);
        if (proSku == null) {
            response.setErrors(Errors._41006);
            return response;
        }
        Integer totalPrice = proSku.getPrice() * num;
        Integer payPrice = totalPrice;
        // 临时处理首单优惠的方案
        DiscountData discountData = couponEnvService.xDiscountPolicy(customer, proSku, num);
        if (discountData.isDiscount()) {
            payPrice = discountData.getPayPrice();
            response.setT(payPrice);
            return response;
        }

        if (coupon_receive_id != null && coupon_receive_id != 0) {
            discountData = couponService.recalculatePrice(customer, coupon_receive_id, proSku, num);
            if (discountData.isDiscount()) {
                payPrice = discountData.getPayPrice();
                response.setT(payPrice);
                return response;
            }
        }
        response.setT(payPrice);
        return response;
    }

    /**
     * 按照状态 查看订单列表。订单转台请查看OrderStatus枚举类型。 不传递status表示查看全部订单
     *
     * @param status      订单状态WAIT_PAY("waitPay"), PAYED("payed"),
     *                    WAIT_SERVICE("waitService"), CANCEL("cancel"), DONE("done"),
     *                    REFUSED("refused"), WAIT_REFUND("waitRefund"),
     *                    PART_REFUND("partRefund"), REFUNDED("refunded");
     * @param customer_id 顾客ID
     * @param page_index  页码
     * @param count       条数
     * @return list
     */
    @Cacheable(value = "orderList", key = "'customer_id:'+#customer_id+':stauts:'+#status+':'+#page_index+':'+#count", unless = "#result.size() == 0")
    @Override
    public List<OrderInfo> orderList(String status, Long customer_id, int page_index, int count) {
        logger.info("api-method:orderList:params status:{},customer_id:{},page_index:{},count:{}", status, customer_id, page_index, count);
        OrderExample orderExample = new OrderExample();
        OrderExample.Criteria criteria = orderExample.or();
        criteria.andUidEqualTo(customer_id);
        orderExample.setOrderByClause(OrderExample.C.create_datetime + " DESC");

        // 组装条件
        OrderStatus orderStatus = OrderStatus.get(status);
        if (orderStatus != null) {
            switch (orderStatus) {
                case WAIT_PAY:
                    criteria.andPay_statusEqualTo(Status.PayStatus.wait_pay.value);
                    criteria.andStatus_activeEqualTo(Status.OrderStatus.wait_pay.value);
                    break;
                case PAYED:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andStatus_activeEqualTo(Status.OrderStatus.wait_confirm.value);
                    break;
                case WAIT_SERVICE:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    List<Integer> list = new ArrayList<>();
                    list.add(Status.OrderStatus.wait_confirm.value);
                    list.add(Status.OrderStatus.wait_service.value);
                    criteria.andStatus_activeIn(list);
                    break;
                case CANCEL:
                    criteria.andStatus_activeEqualTo(Status.OrderStatus.cancel.value);
                    break;
                case DONE:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andStatus_activeEqualTo(Status.OrderStatus.done.value);
                    break;
                case WAIT_REFUND:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andR_statusEqualTo(Status.RefundStatus.wait_refund.value);
                    break;
                case REFUNDED:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andR_statusEqualTo(Status.RefundStatus.refunded.value);
                    break;
                case PART_REFUND:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andR_statusEqualTo(Status.RefundStatus.part_refunded.value);
                    break;
            }
        }
        return orderService.orderInfoList(Roles.CUSTOMER, orderExample, page_index, count).getList();
    }

    /**
     * 查看一条订单的详细情况
     *
     * @param pay_order_id 订单号
     * @return OrderInfo
     */
    //@Cacheable(value = "orderDetail",key = "'customer_id:'+#customer.customer_id+':pay_order_id:'+#pay_order_id",unless = "#result == null")
    @Override
    public OrderInfo orderDetail(Customer customer, String pay_order_id) {
        logger.info("api-method:orderDetail:params customer:{},pay_order_id:{}", customer, pay_order_id);
        OrderExample orderExample = new OrderExample();
        orderExample.or().andPay_order_idEqualTo(pay_order_id).andUidEqualTo(customer.getCustomer_id());
        OrderInfo orderInfo = singleResult(orderService.orderInfoList(Roles.CUSTOMER, orderExample, 1, 1).getList());
        switch (orderInfo.getOrder().getStatus_active()){
            case 1:
                orderInfo.setAllowedToCancel(true);
                break;
            case 2:
            case 3:
                if (orderInfo.getOrder().getChannel().equals("JD-001")){
                    orderInfo.setAllowedToCancel(false);
                }else {
                    CancleStrategyMethod cancleStrategyMethod = cancleStrategyMethod(customer, pay_order_id).getT();
                    if (cancleStrategyMethod == null) {
                        orderInfo.setAllowedToCancel(false);
                    } else {
                        orderInfo.setAllowedToCancel(true);
                    }
                }
                break;
            case 4:
            case 5:
                orderInfo.setAllowedToCancel(false);
                break;
        }
        return orderInfo;
    }

    @Override
    public Order getPayOrder(Customer customer, String pay_order_id) {
        logger.info("api-method:getPayOrder:params customer:{},pay_order_id:{}", customer, pay_order_id);
        OrderExample orderExample = new OrderExample();
        orderExample.or().andPay_order_idEqualTo(pay_order_id).andUidEqualTo(customer.getCustomer_id());
        return singleResult(orderService.selectByExample(orderExample));
    }

    @Override
    public ApiResponse<Order> confirmPayStatus(Customer customer, String pay_order_id, String path) {
        logger.info("api-method:confirmPayStatus params pay_order_id:{},path:{}", pay_order_id, path);
        ApiResponse<Order> response = new ApiResponse<>();
        Order order = getPayOrder(customer, pay_order_id);
        if (order == null) {
            response.setErrors(Errors._41007);
            return response;
        }
        if (!order.getStatus_active().equals(Status.OrderStatus.wait_pay.value)) {
            response.setT(order);
            return response;
        }

        WxAppExample wxAppExample = new WxAppExample();
        wxAppExample.or().andPathEqualTo(path);
        WxApp wxApp = singleResult(wxAppService.selectByExample(wxAppExample));
        WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
        // 验证微信支付中是否已经支付成功,支付宝支付的暂不添加
        MchOrderInfoResult mchOrderInfoResult = payService.wxPayOrderquery(customer, order, wxApp, wxMch);

        String trade_state = mchOrderInfoResult.getTrade_state();
        logger.info("api-method:confirmPayStatus process trade_state:{}", trade_state);
        if (StringUtils.equals(trade_state, "SUCCESS")) {
            response = orderPaysuccess(pay_order_id, Type.PayType.wxpay.value);
            if (response.getErrors() != null) {
                return response;
            }
            PayWxNotify payWxNotify = new PayWxNotify();
            payWxNotify.setAppid(mchOrderInfoResult.getAppid());
            payWxNotify.setAttach(mchOrderInfoResult.getAttach());
            payWxNotify.setBank_type(mchOrderInfoResult.getBank_type());
            // payWxNotify.setBank_billno(null);
            // payWxNotify.setData_issubscribe(null);
            // payWxNotify.setData_openid(null);
            // payWxNotify.setDiscount(null);
            payWxNotify.setFee_type(mchOrderInfoResult.getFee_type());
            // payWxNotify.setInput_charset(null);
            // payWxNotify.setNotify_id(null);
            payWxNotify.setOut_trade_no(mchOrderInfoResult.getOut_trade_no());
            payWxNotify.setPartner(mchOrderInfoResult.getMch_id());
            // payWxNotify.setProduct_fee(null);
            payWxNotify.setSign_type(mchOrderInfoResult.getSign_type());
            payWxNotify.setSign(mchOrderInfoResult.getSign());
            payWxNotify.setTransaction_id(mchOrderInfoResult.getTransaction_id());
            payWxNotify.setTrade_type(mchOrderInfoResult.getTrade_type());
            // payWxNotify.setTrade_mode(null);
            // payWxNotify.setTransport_fee(null);
            payWxNotify.setTrade_state(0);// trade_state,"SUCCESS"
            payWxNotify.setTime_end(mchOrderInfoResult.getTime_end());
            payWxNotify.setTotal_fee(mchOrderInfoResult.getTotal_fee());
            // payWxNotifyService.upsertSelective(payWxNotify);
            // payWxNotifyService.insertSelective(payWxNotify);
            // 插入日志
            String logText = "通过查询微信支付订单支付情况";
            // orderLogService.xInsert("system", 0l, payWxNotify.getOut_trade_no(),
            // logText);

        }
        order = getPayOrder(customer, pay_order_id);
        response.setT(order);
        return response;
    }

    /********************************************
     * 我是分割线，下面是定义变更接口
     *************************************************/
    /**
     * 发送短信验证码
     *
     * @param user_id 顾客的用户ID
     * @param phone   手机号码
     * @return MutationResult
     */
    @Override
    public ApiResponse sendVerificationCode(Long user_id, String phone) {
        logger.info("api-method:sendVerificationCode:params user_id:{},phone:{}", user_id, phone);
        ApiResponse response = new ApiResponse();
        // 需要组装短信体
        Random rd = new Random();
        int verificationCode = 100000 + rd.nextInt(899999);
        // 发送消息到redis pub，
        smsHandler.sendCode(verificationCode + "", phone);

        logger.info("api-method:sendVerificationCode:process phone:{},code:{}", phone, verificationCode);
        // 记录发送短信次数
        String maxnumkey = Constant.getMaxnumSendKey(phone, "code");
        if (redisService.getStringValue(maxnumkey) == null) {
            redisService.setStringValue(maxnumkey, "0", 1, TimeUnit.DAYS);
        } else if (smsCount(phone, "code") > 10) {
            response.setErrors(Errors._41012);
            return response;
        }
        // 验证码短信发送次数
        redisService.increment(maxnumkey, 1);

        // 记录生成的验证码
        Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_VERIFICATION_CODE_TIMEOUT);
        String codeKey = Constant.getVerificationCodeKey(user_id, phone);
        redisService.setStringValue(codeKey, verificationCode + "", Integer.parseInt(metadata.getMeta_value()),
                TimeUnit.MINUTES);


        String envname = env.getActiveProfiles()[0];
        MutationResult mutationResult = new MutationResult();

        if ("dev".equals(envname) || "test".equals(envname)) {
            mutationResult.setExtra(verificationCode + "");
        }
        response.setMutationResult(mutationResult);

        return response;
    }

    /**
     * 将顾客信息绑定到user 上
     *
     * @param user_id   用户ID
     * @param user_name 顾客的姓名
     * @param phone     手机号码
     * @return MutationResult
     */
    @Deprecated
    @Override
    public ApiResponse bindUser(Long user_id, String user_name, String phone, Integer channel) {
        ApiResponse response = new ApiResponse();
        Customer customer = new Customer();
        customer.setCustomer_id(IdGenerator.generateId());
        customer.setUser_id(user_id);
        //customer.setName(user_name);
        customer.setCreate_datetime(new Date());
        customer.setPhone(phone);
        customer.setChannel_id(channel);
        CustomerExample customerExample = new CustomerExample();
        customerExample.or().andPhoneEqualTo(phone);
        Customer customer1 = singleResult(customerService.selectByExample(customerExample));
        if (customer1 != null) {
            response.setErrors(Errors._41017);
            return response;
        }
        int count = customerService.insertSelective(customer);
        if (count > 0) {
            apiUserService.userAddRole(user_id, Roles.CUSTOMER.roleName());
            response.setMutationResult(new MutationResult());
            return response;
        }
        response.setErrors(Errors._41001);
        return response;
    }

    /**
     * @param customer 顾客信息
     * @param phone    电话号码
     * @return ApiResponse
     */
    @Override
    public ApiResponse changePhone(Customer customer, String phone) {
        ApiResponse response = new ApiResponse();

        CustomerExample customerExample = new CustomerExample();
        customerExample.or().andPhoneEqualTo(phone);
        Customer customer1 = singleResult(customerService.selectByExample(customerExample));
        if (customer1 != null) {
            response.setErrors(Errors._41017);
            return response;
        }
        Customer upCustomer = new Customer();
        upCustomer.setCustomer_id(customer.getCustomer_id());
        upCustomer.setPhone(phone);
        int count = customerService.updateByPrimaryKeySelective(upCustomer);
        if (count == 0) {
            response.setErrors(Errors._41001);
            return response;
        }
        //更新缓存
        cacheReloadHandler.customerInfoReload(customer.getUser_id());
        response.setMutationResult(new MutationResult());
        return response;
    }

    /**
     * 设置密码，重置密码
     *
     * @param user_id  用户ID
     * @param password 密码
     * @param repeat   重复验证密码
     * @return MutationResult
     */
    @Override
    public MutationResult setPassword(Long user_id, String password, String repeat) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Users user = new Users();
        user.setUser_id(user_id);
        user.setPassword(encoder.encode(password));
        int count = usersService.updateByPrimaryKeySelective(user);
        if (count == 0) {
            return null;
        }
        return new MutationResult();
    }

    /**
     * 添加顾客服务地址
     *
     * @param customer             顾客对象
     * @param customerAddressInput 顾客地址的输入项目
     * @return CustomerAddress
     */
    @Override
    public CustomerAddress addressAdd(Customer customer, CustomerAddressInput customerAddressInput) {
        CustomerAddress customerAddress = customerAddress(customer, customerAddressInput);
        if(customerAddress.getCity()==null){
            return null;
        }
        int count = customerAddressService.insertSelective(customerAddress);
        if (count > 0) {
            //更新缓存操作
            cacheReloadHandler.addressListReload(customer.getCustomer_id());
            if (customerAddressInput.getDefault_address() == 1)
                setDefaultAddress(customer.getCustomer_id(), customerAddress.getCustomer_address_id());
            return customerAddress;
        }
        return null;
    }

    /**
     * 修改顾客服务地址
     *
     * @param customer             顾客对象
     * @param customerAddressInput 顾客地址的输入项目
     * @return
     */
    @Override
    public CustomerAddress addressUpdate(Customer customer, CustomerAddressInput customerAddressInput) {
        CustomerAddress customerAddress = customerAddress(customer, customerAddressInput);
        customerAddress.setCreate_datetime(null);
        customerAddress.setCustomer_address_id(customerAddressInput.getCustomer_address_id());
        int count = customerAddressService.updateByPrimaryKeySelective(customerAddress);
        if (count > 0) {
            //更新缓存操作
            cacheReloadHandler.addressListReload(customer.getCustomer_id());
            if (customerAddressInput.getDefault_address() == 1)
                setDefaultAddress(customer.getCustomer_id(), customerAddress.getCustomer_address_id());
            return customerAddress;
        }
        return null;
    }

    /**
     * @param customer_address_id 顾客服务地址ID
     * @return MutationResult
     */
    @Override
    public MutationResult addressDelete(Customer customer, Long customer_address_id) {
        CustomerAddress address = new CustomerAddress();
        address.setCustomer_address_id(customer_address_id);
        address.setDeleted(1);
        //更新缓存操作
        CustomerAddress customerAddress = customerAddressService.selectByPrimaryKey(customer_address_id);
        int count = customerAddressService.updateByPrimaryKeySelective(address);
        if (new Integer(1).equals(customerAddress.getDefault_address())) {
            CustomerAddressExample example = new CustomerAddressExample();
            example.or().andDeletedEqualTo(Status.DeleteStatus.no.value)
                    .andCustomer_idEqualTo(customer.getCustomer_id());
            example.setOrderByClause(CustomerAddressExample.C.update_datetime + " desc");
            example.limit(0L, 1L);
            customerAddress = singleResult(customerAddressService.selectByExample(example));
            if (customerAddress != null) {
                setDefaultAddress(customer.getCustomer_id(), customerAddress.getCustomer_address_id());
            }
        }
        cacheReloadHandler.addressListReload(customer.getCustomer_id());
        cacheReloadHandler.getDefaultAddressReload(customer.getCustomer_id());
        if (count > 0)

            return new MutationResult();
        return null;
    }

    /**
     * 设定服务地址为默认服务地址
     *
     * @param customer_id         顾客id
     * @param customer_address_id 顾客地址ID
     * @return MutationResult
     */
    @Transactional(timeout = 2)
    @Override
    public MutationResult setDefaultAddress(Long customer_id, Long customer_address_id) {
        if (customer_address_id == null && customer_id == null)
            return null;
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setDefault_address(0);
        CustomerAddressExample example = new CustomerAddressExample();
        example.or().andCustomer_idEqualTo(customer_id).andDefault_addressEqualTo(1);
        customerAddressService.updateByExampleSelective(customerAddress, example);

        customerAddress.setCustomer_address_id(customer_address_id);
        customerAddress.setDefault_address(1);
        customerAddressService.updateByPrimaryKeySelective(customerAddress);
        //更新缓存操作
        cacheReloadHandler.getDefaultAddressReload(customer_id);
        cacheReloadHandler.addressListReload(customer_id);
        return new MutationResult();
    }

    /**
     * 创建订单。 创建订单步骤： 1：创建一条pay_order记录。 2：同时创建服务单，指定一个合伙人。如果涉及到库存需要预先指定 3：是否使用了优惠券
     * 4：创建订单项目
     *
     * @param input 创建订单输入项
     * @return ApiResponse
     */
    @Transactional(timeout = 5)
    @Override
    public ApiResponse<Order> createOrder(Customer customer, OrderInput input, String channelId, String clientId) {
        logger.info("api-method:createOrder params input{}:", input);
        ApiResponse<Order> response = new ApiResponse<>();
        try {
            // 获取产品的最大最小购买值
            ProSku sku = proSkuService.selectByPrimaryKey(input.getPsku_id());
            // --------------------------------判断参数值对否-------------------------------------
            Integer allow_mutiple = sku.getBuy_multiple();
            boolean flag = true;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date begin = null;
            Date end = null;
            try {
                begin = format.parse(input.getBegin_datetime());

                if (StringUtils.isEmpty(input.getEnd_datatime()) || StringUtils.equals(input.getEnd_datatime(), "null")) {
                    end = null;
                } else {
                    end = format.parse(input.getEnd_datatime());
                }

                Date now = new Date();
                if (begin.before(now)) {
                    flag = false;
                }
            } catch (ParseException e) {
                flag = false;
            }

            Integer num = input.getNum();
            if (allow_mutiple == 1) {// 允许购买多件
                Integer max = sku.getBuy_multiple_max();
                Integer min = sku.getBuy_multiple_min();
                if (max == 0) {// 不限制购买数量
                    if (num < min) {
                        flag = false;
                    }
                } else {
                    if (num < min && num > max) {
                        flag = false;
                    }
                }
            } else if (num > 1) {
                flag = false;
            }

            if (!flag) {
                response.setErrors(Errors._41040);
                return response;
            }
            // 用户下单地址校验
            CustomerAddressExample example = new CustomerAddressExample();
            example.or().andCustomer_idEqualTo(customer.getCustomer_id())
                    .andCustomer_address_idEqualTo(input.getCustomer_address_id());
            CustomerAddress customerAddress = singleResult(customerAddressService.selectByExample(example));
            if (customerAddress == null) {
                response.setErrors(Errors._41004);
                return response;
            }

            // 要购买的产品的验证
            ProSku proSku = proSkuService.selectByPrimaryKey(input.getPsku_id());
            Product product = productService.selectByPrimaryKey(input.getProduct_id());
            if (product == null || proSku == null) {
                response.setErrors(Errors._41006);
                return response;
            }
            if (new Integer(0).equals(product.getOnline())) {
                response.setErrors(Errors._41006);
                return response;
            }

            // 绑定合伙人的从绑定合伙人的站点中看库存，未绑定的从未绑定的看库存
            Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
            Integer integer = metadata.getMeta_value() == null ? Constant.SEARCH_RADIUS
                    : Integer.parseInt(metadata.getMeta_value());
            List<Station> stations = null;
            ProductSoleExample soleExample = new ProductSoleExample();
            soleExample.or().andProduct_idEqualTo(product.getProduct_id());
            List<ProductSole> productSoles = productSoleService.selectByExample(soleExample);
            if (productSoles.size() != 0) {//绑定合伙人的产品
                List<Long> partner_ids = productSoles.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
                StationExample stationExample = new StationExample();
                stationExample.or().andPartner_idIn(partner_ids).andCityEqualTo(customerAddress.getCity());
                stations = stationService.selectByExample(stationExample);
            } else {//未绑定的
                stations = stationService.findNearbyStation(customerAddress, integer);
                stations = stationService.filterByProduct(product, stations);
            }
            Integer buy_multiple_o2o = sku.getBuy_multiple_o2o();

            // 开始生成订单，并计算价格
            int totalPrice = proSku.getPrice() * num;
            int payPrice = totalPrice;
            Order order = orderService.initOrder(product, proSku, customerAddress, num);

            String envname = env.getActiveProfiles()[0];
            String pay_order_id = redisIdGenerator.generatorId("pay_order_id", 1000) + "";
            if ("dev".equals(envname)) {
                pay_order_id = pay_order_id + "_1";
            } else if ("test".equals(envname)) {
                pay_order_id = pay_order_id + "_2";
            }
            order.setPay_order_id(pay_order_id);
            //虚拟预占库存
            if (!storeService.preTakeAvilableTimeUnits(order.getPay_order_id()
                    , stations, input.getBegin_datetime().substring(0, 10)
                    , product, begin, end, buy_multiple_o2o == 1 ? num : 1)) {
                response.setErrors(Errors._41021);
                return response;
            }
            // 临时处理首单优惠的方案
            DiscountData discountData = couponEnvService.xDiscountPolicy(customer, proSku, input.getNum());
            if (discountData.isDiscount()) {
                logger.info("api-method:createOrder process discountPolicy discountData:{}", discountData);
                payPrice = discountData.getPayPrice();
                CouponReceive couponReceive = new CouponReceive();
                couponReceive.setCoupon_receive_id(IdGenerator.generateId());
                couponReceive.setUid(customer.getCustomer_id());
                couponReceive.setCoupon_id(discountData.getCouponEnv().getCoupon_id());
                couponReceive.setPay_order_id(order.getPay_order_id());
                couponReceive.setReceive_datetime(new Date());
                couponReceive.setDeleted(0);
                couponReceive.setVerification(0);
                couponReceive.setStatus(4);// 使用后不可以进行回滚
                couponReceive.setUse_datetime(new Date());
                couponReceive.setCoupon_env_id(discountData.getCouponEnv().getCoupon_env_id());
                //减量。。。。
                Coupon coupon = couponService.selectByPrimaryKey(couponReceive.getCoupon_id());
                if (coupon.getNum_limit() == 1) {
                    Coupon coupon1 = new Coupon();
                    coupon1.setCoupon_id(coupon.getCoupon_id());
                    coupon1.setNum_able(coupon.getNum_able() - 1);
                    couponService.updateByPrimaryKeySelective(coupon1);
                }
                couponReceiveService.insertSelective(couponReceive);
            } else if (input.getCoupon_receive_id() != null && input.getCoupon_receive_id() != 0) {
                discountData = couponService.recalculatePrice(customer, input.getCoupon_receive_id(), proSku, num);
                if (discountData != null && discountData.isDiscount()) {
                    logger.info("api-method:createOrder process coupon discountData:{}", discountData);
                    payPrice = discountData.getPayPrice();
                    CouponReceive couponReceive = new CouponReceive();
                    couponReceive.setCoupon_receive_id(input.getCoupon_receive_id());
                    couponReceive.setStatus(3);// 设置为已使用待回滚/确定
                    couponReceive.setPay_order_id(order.getPay_order_id());
                    couponReceive.setUse_datetime(new Date());
                    // 更新优惠券使用情况
                    couponReceiveService.updateByPrimaryKeySelective(couponReceive);
                }
            }
            int discountPrice = totalPrice - payPrice;

            if (discountData.isDiscount()) {
                Map<String, Object> map = new HashMap<>();
                map.put("coupon_id", discountData.getCoupon().getCoupon_id());
                map.put("description", "优惠策略优惠券优惠");
                map.put("price", discountPrice);
                order.setDiscount_data(JSON.toJSONString(map));
            }

            order.setPrice_discount(discountPrice);
            order.setPrice_pay(payPrice);
            order.setPrice_total(totalPrice);
            order.setChannel(channelId);
            order.setClient_id(clientId);
            order.setRemark(input.getRemark());// 填写备注
            OrderItem orderItem = orderItemService.initOrderItem(order, proSku, num);

            ServiceUnit[] serviceUnits = initServiceUnit(customer, order, input);

            // 进行存储,成功和失败的判断
            orderService.insertSelective(order);
            orderItemService.insertSelective(orderItem);
            for (ServiceUnit serviceUnit : serviceUnits) {
                serviceUnitService.insertSelective(serviceUnit);
            }
            String cname = customer.getName() == null ? customer.getPhone() : customer.getName();
            String logText = "用户【" + cname + "】进行了下单操作";
            orderLogService.xInsert(cname, customer.getCustomer_id(), order.getPay_order_id(), logText);
            response.setT(order);

            // 发送自动取消的命令
            metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_PAY_TIMEOUT);
            Integer timeOut = metadata.getMeta_value() == null ? Constant.ORDER_PAY_TIMEOUT
                    : Integer.parseInt(metadata.getMeta_value());
            Long delayTime = System.currentTimeMillis() + timeOut * 60 * 1000;
            try {
                SendResult sendResult = onsHandler.sendCancelMessage(order.getPay_order_id(), delayTime);
                logger.info("api-method:createOrder:SendResult:{}", sendResult.toString());
            } catch (Exception e) {
                logger.error("ONS ERROR{}  sendCancelMessage orderID{}", e.getMessage(), order.getPay_order_id());
            }


            //更新缓存
            cacheReloadHandler.couponListReload(customer.getCustomer_id());
            cacheReloadHandler.userCouponListReload(customer.getCustomer_id());
            cacheReloadHandler.orderListReload(customer.getCustomer_id());
        } catch (Exception e) {
            logger.error("ERROR create order error", e);
            response.setErrors(Errors._41040);
        }
        return response;
    }

    /**
     * wx统一下单接口调用
     *
     * @param customer
     * @param pay_order_id
     * @return
     */
    @Override
    public ApiResponse<WxPaymentBody> wxPrepay(Customer customer, String pay_order_id, String path, String openid) {
        logger.info("api-method:wxPrepay:params customer:{},pay_order_id:{},path:{},openid:{}", customer, pay_order_id, path, openid);
        ApiResponse<WxPaymentBody> response = new ApiResponse<>();
        try{
            Order order = orderService.selectByPrimaryKey(pay_order_id);
            if (order == null) {
                response.setErrors(Errors._41007);
                return response;
            }
            // 订单取消，不可以再进行支付了
            if (order.getExpire_datetime().before(new Date()) || order.getStatus_active() == 4) {
                response.setErrors(Errors._41018);
                return response;
            }
            if (Status.PayStatus.payed.value.equals(order.getPay_status())) {
                response.setErrors(Errors._41043);
                return response;
            }

            //String prepay_id = redisService.getStringValue(Constant.getWxUnifiedorderKey(pay_order_id));
            WxAppExample wxAppExample = new WxAppExample();
            wxAppExample.or().andPathEqualTo(path);
            WxApp wxApp = singleResult(wxAppService.selectByExample(wxAppExample));
            WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
            logger.info("wxapp:{},wxmch:{}", wxApp, wxMch);
            UnifiedorderResult unifiedorderResult = payService.wxUnifiedorder(customer, order, wxApp, wxMch, path, openid, "JSAPI");
            if (StringUtils.equals(unifiedorderResult.getErr_code(), "FAIL")) {
                logger.error("api-method:wxPrepay:process err_msg:{}", unifiedorderResult.getErr_code_des());
                response.setErrors(Errors._41019);
                return response;
            }
            logger.info("api-method:wxPrepay:process the result to unifiedorder msg1{}, msg2{}", unifiedorderResult.getReturn_msg(), unifiedorderResult.getReturn_msg());
            String prepay_id = unifiedorderResult.getPrepay_id();
            logger.info("api-method:wxPrepay:process prepay_id:{}", prepay_id);
            WxPaymentBody body = payService.wxWxPaymentBody(prepay_id, wxApp, wxMch);
            response.setT(body);

        }catch (Exception e){
            logger.error("ERROR wxPrePay error",e);
            response.setErrors(Errors._41019);
        }
        return response;

    }

    /**
     * 0元下单接口
     *
     * @param customer     顾客信息
     * @param pay_order_id 订单号
     * @return
     */
    @Override
    public ApiResponse<Order> wxPayFree(Customer customer, String pay_order_id) {
        logger.info("api-method:wxPrepay:params customer:{},pay_order_id:{}", customer, pay_order_id);
        ApiResponse<Order> response = new ApiResponse<>();
        // 1.判断订单是否存在
        Order order = getPayOrder(customer, pay_order_id);
        if (order == null) {
            response.setErrors(Errors._41007);
            return response;
        }
        // 2.判断订单是否已经超时取消 订单取消，不可以再进行支付了
        if (order.getExpire_datetime().before(new Date()) || order.getStatus_active() == 4) {
            response.setErrors(Errors._41018);
            return response;
        }

        // 3.判断订单的支付状态，若是已支付则不可以再支付
        if (order.getPay_status().equals(Status.PayStatus.payed.value)) {
            response.setErrors(Errors._41043);
            return response;
        }

        // 3.判断订单是否是0元支付的订单
        if (order.getPrice_pay().equals(0)) {
            // 3.1 是0元支付的订单 -- 修改订单状态已支付，安排服务人员
            ApiResponse apiResponse = orderPaysuccess(pay_order_id, 0);
            if (apiResponse.getErrors() != null) {
                response.setErrors(apiResponse.getErrors());
                return response;
            } else {
                order = getPayOrder(customer, pay_order_id);
                response.setT(order);
                return response;
            }
        } else {
            // 3.2 否--报错
            response.setErrors(Errors._41042);
            return response;
        }
    }

    /**
     * 前提第三方通知订单支付成功 需要完成功能： 1.指派一个合伙人，服务单开启 2.发送短息 实时短信；订单以支付，待确认。延时短信。未及时确认短信
     *
     * @param pay_order_id
     * @return ApiResponse
     */
    @Transactional(timeout = 3)
    @Override
    public ApiResponse orderPaysuccess(String pay_order_id, int paytype) {
        logger.info("api-method:orderPaysuccess:params pay_order_id:{},paytype:{}", pay_order_id, paytype);
        ApiResponse response = new ApiResponse();
        try {
            // 1.先看看订单和服务单是否存在
            Order order = orderService.selectByPrimaryKey(pay_order_id);
            if (order == null) {
                response.setErrors(Errors._41007);
                return response;
            }
            if (Status.PayStatus.payed.value.equals(order.getPay_status())) {
                return response;
            }
            ServiceUnitExample example = new ServiceUnitExample();
            example.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
            ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(example));
            if (serviceUnit == null) {
                response.setErrors(Errors._41008);
                return response;
            }
            apiOrderService.paySuccess(order, paytype);
            Station station = orderService.dispatchOrder(order,serviceUnit);
            // 发送短信
            // 您有一张新的${product_name}订单，订单号：${pay_order_id}}，还有10分钟将被自视为动拒单，请及时确认。
            Partner partner = partnerService.selectByPrimaryKey(station == null ? 0l : station.getPartner_id());
            if (partner != null) {
                // 发送短信
                smsHandler.sendToPattnerWhenCustomerPayed(order.getName(), partner.getPhone());
                //推送消息 新订单 通知到合伙人
                pushHandler.pushOrderMessageToPartner(order, partner.getPartner_id().toString());

                // 发送自动拒单命令，晚上18点至早8:00份的订单 第二日早8点半取消
                Metadata metadata = metadataService.selectByPrimaryKey("max_pay_time");
                Metadata metadata1 = metadataService.selectByPrimaryKey("min_pay_time");
                Metadata metadata2 = metadataService.selectByPrimaryKey("default_reject_time");
                LocalDateTime local = LocalDateTime.now();
                Integer time = local.getHour();

                String max = metadata == null ? "20" : metadata.getMeta_value();
                String min = metadata1 == null ? "8" : metadata1.getMeta_value();
                String rejectTime = metadata2 == null ? "30" : metadata2.getMeta_value();
                Long delayTime = System.currentTimeMillis() + Integer.parseInt(rejectTime) * 60 * 1000;
                if (time < Integer.parseInt(min)) {
                    delayTime = local.withHour(8).withMinute(30).toInstant(ZoneOffset.of("+8")).toEpochMilli();
                } else if (time >= Integer.parseInt(max)) {
                    delayTime = local.plusDays(1).withHour(8).withMinute(30).toInstant(ZoneOffset.of("+8")).toEpochMilli();
                }
                logger.info("api-method:orderPaysuccess onsDelayTime:{}", delayTime);
                // 发mq消息
                try {
                    SendResult sendRejectMessage = onsHandler.sendRejectMessage(order.getPay_order_id(), partner.getPartner_id(),
                            delayTime);
                    //拒单和抢单是同时进行的，防止冲突。延迟3秒进行抢单的发起
                    delayTime = delayTime + 3000;
                    SendResult sendRobbingMessage = onsHandler.sendRobbingMessage(order.getPay_order_id(), 0, delayTime);
                    logger.info("api-method:orderPaysuccess:SendResult:{},SendResult:{}", sendRejectMessage.toString(), sendRobbingMessage.toString());
                } catch (Exception e) {
                    logger.error("ONS ERROR {} sendRejectMessage sendRobbingMessage orderID{}", e.getMessage(), order.getPay_order_id());
                }

                cacheReloadHandler.partner_order_listReload(partner.getPartner_id());
                cacheReloadHandler.partner_order_detailReload(pay_order_id);
                cacheReloadHandler.partner_order_listReload(serviceUnit.getPartner_id());
            }
            //更新缓存
            cacheReloadHandler.orderListReload(order.getUid());
            cacheReloadHandler.orderDetailReload(order.getUid(), pay_order_id);

            if (order.getProxyed() == 1) {
                cacheReloadHandler.selectStuUndoneOrderReload(order.getProxyed_uid());
                cacheReloadHandler.selectStuShowTaskdetailReload(order.getProxyed_uid(), pay_order_id);
                cacheReloadHandler.student_order_listReload(order.getProxyed_uid());

            }

        } catch (Exception e) {
            logger.error("ERROR  orderPaysuccess ", e);
            response.setErrors(Errors._41040);
        }

        return response;
    }


    /**
     * 用户手动取消订单。
     *
     * @param customer      用户对象
     * @param pay_order_id  订单号
     * @param remark_cancel 取消备注
     * @return ApiResponse
     */
    @Transactional(timeout = 3)
    @Override
    public ApiResponse cancelOrder(Customer customer, String pay_order_id, String remark_cancel) {
        logger.info("api-method:cancelOrder:params pay_orderid:{},remark_cancel:{}", pay_order_id, remark_cancel);
        ApiResponse response = new ApiResponse();
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Order order = orderService.selectByPrimaryKey(pay_order_id);
            if (order.getStatus_active().equals(Status.OrderStatus.cancel.value)) {
                response.setMutationResult(new MutationResult());
                return response;
            }
            if (order == null) {
                response.setErrors(Errors._41007);
                return response;
            }
            ServiceUnitExample example = new ServiceUnitExample();
            example.or()
                    .andPay_order_idEqualTo(order.getPay_order_id())
                    .andPidEqualTo(0l);
            ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(example));
            if (serviceUnit == null) {
                response.setErrors(Errors._41013);
                return response;
            }
            Partner partner = partnerService.selectByPrimaryKey(serviceUnit.getPartner_id());
            //更新缓存
            cacheReloadHandler.orderListReload(order.getUid());
            cacheReloadHandler.orderDetailReload(order.getUid(), pay_order_id);
            cacheReloadHandler.userCouponListReload(customer.getCustomer_id());
            cacheReloadHandler.couponListReload(customer.getCustomer_id());
            // 未支付订单直接取消
            if (Status.PayStatus.wait_pay.value.equals(order.getPay_status())) {
                if (orderService.xCancelOrderWithoutPay(pay_order_id, remark_cancel)) {
                    // 添加订单日志
                    String cname = customer.getName() == null ? customer.getPhone() : customer.getName();
                    orderLogService.xInsert(cname, customer.getCustomer_id(), order.getPay_order_id(),
                            "【未支付取消】用户【" + cname + "】取消订单），原因是:" + remark_cancel);
                    // 接单删除虚拟库存
                    Set<String> set = redisService.sMembers(pay_order_id);
                    if (set != null) {
                        redisService.delete(pay_order_id);
                        redisService.delete(set);
                    }
                    response.setMutationResult(new MutationResult());
                } else {
                    response.setErrors(Errors._41013);
                    return response;
                }
            } else if (Status.PayStatus.payed.value.equals(order.getPay_status())) {
                // 取消以支付订单
                //取消策略方法
                ApiResponse<CancleStrategyMethod> cancleStrategyMethodApiResponse = cancleStrategyMethod(customer, pay_order_id);
                CancleStrategyMethod cancleStrategyMethod = cancleStrategyMethodApiResponse.getT();
                if (cancleStrategyMethodApiResponse.getErrors() != null || cancleStrategyMethod == null) {
                    response.setErrors(cancleStrategyMethodApiResponse.getErrors());
                    return response;
                }
                boolean result = orderService.xCancelOrderPayedAndRefund(customer, order, remark_cancel, cancleStrategyMethod);
                if (!result) {
                    orderLogService.xInsert(customer.getName(), customer.getCustomer_id(), order.getPay_order_id(), "取消失败");
                    response.setErrors(Errors._41013);
                    return response;
                }
                //如果用户取消订单，有正在发起的抢单还没有抢完的，进行标记无效
                RobbingExample robbingExample = new RobbingExample();
                robbingExample.or()
                        .andServiceunit_idEqualTo(serviceUnit.getServiceunit_id())
                        .andStatusEqualTo(1);
                Robbing robbing  = new Robbing();
                robbing.setStatus(0);
                robbingService.updateByExampleSelective(robbing,robbingExample);

            Set<String> set = redisService.sMembers(pay_order_id);
            if (set!=null){
                redisService.delete(pay_order_id);
                redisService.delete(set);
            }

                //更新合伙人订单缓存
                cacheReloadHandler.partner_order_detailReload(pay_order_id);
                cacheReloadHandler.partner_order_listReload(serviceUnit.getPartner_id());
                cacheReloadHandler.my_mission_scheduled_informationReload(serviceUnit.getPartner_id());
                // 添加订单日志
                String cname = customer.getName() == null ? customer.getPhone() : customer.getName();
                orderLogService.xInsert(cname, customer.getCustomer_id(), order.getPay_order_id(),
                        "【已支付取消】用户【" + cname + "】取消订单，原因是" + remark_cancel);
                orderLogService.xInsert(cname, customer.getCustomer_id(), order.getPay_order_id(),
                        "【已支付取消】用户【" + cname + "】申请退款，原因是" + remark_cancel);
                // 取消完成发送短信
                // 向顾客发送短信确认已取消
                smsHandler.sendToCustomerWhenOrderCancel(order.getName()
                        , format.format(serviceUnit.getC_begin_datetime())
                        , order.getCus_address()
                        , order.getCus_phone());
                // 您于${c_begin_datetime}去${cus_address}，为${cus_username}，进行${product_name}服务的订单已取消。
                if(!Status.ServiceStatus.reject.value.equals(serviceUnit.getStatus_active())) {
                    smsHandler.sendToPartnerWhenOrderCancel(order.getName()
                            , format.format(serviceUnit.getC_begin_datetime())
                            , order.getCus_username()
                            , order.getCus_address()
                            , partner.getPhone());
                }
                logger.info("api-method:cancelOrder:process name:{},time:{},username:{},address:{},phone:{}",
                        order.getName(), serviceUnit.getC_begin_datetime(), order.getCus_username(),
                        order.getCus_address(), order.getCus_phone());
                logger.info("api-method:cancelOrder:process name:{},time:{},address:{},phone:{}",
                        order.getName(), serviceUnit.getC_begin_datetime(), order.getCus_address(), order.getCus_phone());

                //发送站内短信(已支付发送消息)
            /*Message msg = new Message();
            msg.setId(IdGenerator.generateId());
            msg.setType(2);
            msg.setBis_type(3);
            msg.setUser_id(partner.getUser_id());
            msg.setUid(partner.getPartner_id());
            msg.setMsg_title("订单取消通知");
            //顾客取消订单
            MessageContent.ContentMsg content = new MessageContent.ContentMsg();
            content.setMsgtype("native");
            remark_cancel = remark_cancel == null ? "" : remark_cancel;
            content.setContent("您的订单" + pay_order_id + ",由于顾客" + remark_cancel + ",已被取消。");
            content.setTitle("订单取消通知");
            content.setNoticeTypes(3);
            String object_to_json = null;
            try {
                object_to_json = JacksonUtil.object_to_json(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            msg.setMsg_content(object_to_json);
            msg.setCreate_datetime(new Date());
            msg.setNotify_datetime(new Date());
            msg.setGroup_id(pay_order_id);
            msg.setApp_type(3);
            msg.setSend_type(1);
            msg.setApp_platform(0);
            messageService.insertSelective(msg);*/

                pushHandler.pushCancelOrderMessageToPartner(order, partner.getPartner_id().toString());
                inStationHandler.sentToPartnerCancleOrder(pay_order_id,partner);
            }
            // 取消订单 有服务人员 释放库存
            ServiceunitPersonExample personExample = new ServiceunitPersonExample();
            personExample.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id()).andStatus_activeEqualTo(Status.OrderStatus.wait_service.value);
            List<ServiceunitPerson> personList = serviceunitPersonService.selectByExample(personExample);
            OrderInfo orderInfo = orderDetail(customer, pay_order_id);
            if (personList != null && personList.size() > 0) {
                personList.stream().forEach(t -> {
                    storeService.updateAvilableTimeUnits(t.getStudent_id(), serviceUnit.getC_begin_datetime(),
                            serviceUnit.getC_end_datetime(), StoreServiceImpl.RELEASE);
                    if (Status.PayStatus.payed.value.equals(order.getPay_status())) {
                        // 向服务人员发送短信。确认已取消
                        // 您于${c_begin_datetime}去${cus_address}，为${cus_username}，手机号：${cus_phone}，进行${product_name}服务的订单已取消。
                        //多个服务人员分别发送短信
                        Student student = studentService.selectByPrimaryKey(t.getStudent_id());
                        smsHandler.sendToWorkWhenOrderCancel(order.getName()
                                , order.getCus_username()
                                , order.getCus_phone()
                                , format.format(serviceUnit.getC_begin_datetime())
                                , order.getCus_address()
                                , student.getPhone());
                        logger.info("api-method:cancelOrder:process name:{},time:{},address:{},phone:{}", order.getName(),
                                serviceUnit.getC_begin_datetime(), order.getCus_address(), order.getCus_phone());
                    }
                    //更新服务人员缓存
                    cacheReloadHandler.student_order_listReload(t.getStudent_id());
                    cacheReloadHandler.selectStuShowTaskdetailReload(t.getStudent_id(), pay_order_id);
                    cacheReloadHandler.selectStuUndoneOrderReload(t.getStudent_id());
                    //取消,推送服务人员
                    pushHandler.pushCancelOrderMessageToStudent(orderInfo, t.getStudent_id().toString());
                    //站内消息（取消）
                    inStationHandler.sentToStudentCancleOrder(pay_order_id,studentService.selectByPrimaryKey(t.getStudent_id()));
                });
                //同步serviceunitPerson的状态
                ServiceunitPerson serviceunitPerson = new ServiceunitPerson();
                serviceunitPerson.setStatus_active(Status.OrderStatus.cancel.value);
                serviceunitPersonService.updateByExampleSelective(serviceunitPerson, personExample);
            }
            // 接单删除虚拟库存
            Set<String> set = redisService.sMembers(pay_order_id);
            if (set != null) {
                redisService.delete(pay_order_id);
                redisService.delete(set);
            }
            //推送取消
            if(!Status.ServiceStatus.reject.value.equals(serviceUnit.getStatus_active())) {
                pushHandler.pushCancelOrderMessageToCustomer(order, customer.getCustomer_id().toString());
            }
            response.setMutationResult(new MutationResult());
        }catch (Exception e){
            logger.error("ERROR cancelOrder error",e);
            response.setErrors(Errors._41040);
        }

        return response;
    }

    /**
     * 取消策略方法
     *
     * @param customer
     * @param pay_order_id
     * @return
     */
    @Override
    public ApiResponse<CancleStrategyMethod> cancleStrategyMethod(Customer customer, String pay_order_id) {
        //1.根据服务单,查询商品
        //2.根据商品查询商品的取消策略,
        //3.计算扣除金额,写明扣除原因,退款金额
        ApiResponse<CancleStrategyMethod> response = new ApiResponse<>();
        CancleStrategyMethod csm = new CancleStrategyMethod();
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
        ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        //订单sku
        Product product = productService.selectByPrimaryKey(serviceUnit.getProduct_id());
        //获取到该商品的取消策略
        CancleStrategy cancleStrategy = cancleStrategyService.selectByPrimaryKey(product.getCancle_strategy_id().intValue());
        //订单
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        List<CancleStrategyJson> cancleJsons = null;
        //转换对象
        if (cancleStrategy != null) {
            cancleJsons = JSON.parseArray(cancleStrategy.getCancle_strategy(), CancleStrategyJson.class);
            Collections.sort(cancleJsons);
        }
        if (cancleJsons == null) {
            response.setErrors(Errors._41020);
            return response;
        }
        long unitTime = serviceUnit.getC_begin_datetime().getTime();
        long time = System.currentTimeMillis();

        long between = unitTime - time;
        int cancelType = cancleStrategy.getCancle_type();

        Collections.reverse(cancleJsons);

        int deductionPrice = 0;
        int feePrice = 0;
        try {
            for (CancleStrategyJson csj : cancleJsons) {
                long millis = csj.getBeforeHour() * 60 * 60 * 1000;
                if (csj.getAllow() == false) {
                    response.setErrors(Errors._41020);
                    return response;
                }

                if (between > millis) {
                    switch (cancelType) {
                        case 1://根据订单金额扣除
                            deductionPrice = order.getPrice_total() * Integer.parseInt(csj.getValue()) / 100;
                            feePrice = order.getPrice_pay() - deductionPrice;
                            feePrice = feePrice < 0 ? 0 : feePrice;
                            break;
                        case 2: //根据实付金额扣除
                            deductionPrice = order.getPrice_pay() * Integer.parseInt(csj.getValue()) / 100;
                            feePrice = order.getPrice_pay() - deductionPrice;
                            feePrice = feePrice < 0 ? 0 : feePrice;
                            break;
                        case 3://减固定扣款金额
                            deductionPrice = Integer.parseInt(csj.getValue());
                            feePrice = order.getPrice_pay() - deductionPrice;
                            feePrice = feePrice < 0 ? 0 : feePrice;
                            break;
                    }
                    csm.setFeePrice(feePrice);
                    csm.setDeductionPrice(deductionPrice);
                    csm.setFeeRemark("您本次订单取消可能产生费用扣除，具体请参考产品退改规则，确定要取消订单吗？");
                    break;
                }

            }
        } catch (Exception e) {
             logger.error("ERROR  cancleStrategyMethod   is error",e);
             response.setErrors(Errors._41020);
        }


        response.setT(csm);
        return response;
    }

    /**
     * 进行评价
     *
     * @param customer     顾客对象
     * @param pay_order_id 订单号
     * @param score        评分
     * @param comment      评价内容
     * @return ApiResponse
     */
    @Override
    public ApiResponse serviceEvaluate(Customer customer, String pay_order_id, int score, String comment) {
        logger.info("api-method:serviceEvaluate:params pay_order_id:{},score:{},comment:{}", pay_order_id, score, comment);
        ApiResponse response = new ApiResponse();
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        if (order == null) {
            response.setErrors(Errors._41007);
            return response;
        }
        if (order.getPay_status().equals(0)) {
            response.setErrors(Errors._41014);
            return response;
        }
        ProEvaluateExample evaluateExample = new ProEvaluateExample();
        evaluateExample.or().andPay_order_idEqualTo(pay_order_id);
        if (proEvaluateService.countByExample(evaluateExample) > 0) {
            response.setErrors(Errors._41236);
            return response;
        }

        ServiceUnitExample example = new ServiceUnitExample();
        example.or().andPay_order_idEqualTo(pay_order_id).andActiveEqualTo(1).andPidEqualTo(0l);
        ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(example));
        ProEvaluate proEvaluate = new ProEvaluate();
        proEvaluate.setProduct_evaluate_id(IdGenerator.generateId());
        proEvaluate.setCustomer_id(customer.getCustomer_id());
        proEvaluate.setPay_order_id(pay_order_id);
        proEvaluate.setCreate_datetime(new Date());
        proEvaluate.setScore(score);
        proEvaluate.setComment(comment);
        proEvaluate.setProduct_id(serviceUnit.getProduct_id());
        proEvaluate.setPsku_id(serviceUnit.getPsku_id());
        proEvaluate.setDeleted(Status.DeleteStatus.no.value);
        int count = proEvaluateService.insertSelective(proEvaluate);
        if (count == 0) {
            response.setErrors(Errors._40109);
            return response;
        }
        response.setMutationResult(new MutationResult());
        //更新评论缓存
        cacheReloadHandler.productEvaluateBaseReload(serviceUnit.getProduct_id());
        cacheReloadHandler.productEvaluatesListReload(serviceUnit.getProduct_id());
        cacheReloadHandler.orderListReload(customer.getCustomer_id());
        cacheReloadHandler.orderDetailReload(customer.getCustomer_id(), pay_order_id);
        return response;
    }

    /**
     * 服务完成,顾客确定完成
     */
    @Transactional(timeout = 3)
    @Override
    public ApiResponse serviceComplete(Customer customer, String pay_order_id) {
        logger.info("api-method:serviceComplete:params customer:{},pay_order_id:{}", customer, pay_order_id);
        ApiResponse response = new ApiResponse();
        // 根据订单查询服务单是否完成
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
        ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(serviceUnitExample));

        ServiceUnitExample serviceUnitExample1 = new ServiceUnitExample();
        serviceUnitExample1.or().andGroup_tagEqualTo(serviceUnit.getGroup_tag());
        ServiceUnit unit = new ServiceUnit();
        unit.setGroup_tag(serviceUnit.getGroup_tag());
        unit.setFinish_datetime(new Date());
        unit.setStatus_active(Status.OrderStatus.done.value);
        serviceUnitService.updateByExampleSelective(unit, serviceUnitExample1);

        Order order = orderService.selectByPrimaryKey(pay_order_id);
        order.setStatus_active(Status.OrderStatus.done.value);
        orderService.updateByPrimaryKeySelective(order);

        //serviceunitPerson的状态
        ServiceunitPersonExample personExample = new ServiceunitPersonExample();
        personExample.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());

        ServiceunitPerson serviceunitPerson = new ServiceunitPerson();
        serviceunitPerson.setStatus_active(Status.OrderStatus.done.value);
        serviceunitPersonService.updateByExampleSelective(serviceunitPerson, personExample);


        String oprator = customer.getName() == null ? customer.getPhone() : customer.getName();
        String text = "用户【" + oprator + "】进行了确认完成订单操作";
        orderLogService.xInsert(oprator, customer.getCustomer_id(), pay_order_id, text);
        response.setMutationResult(new MutationResult());

        //更新缓存.
        List<ServiceunitPerson> persons = serviceunitPersonService.selectByExample(personExample);
        persons.stream().forEach(t -> {
            cacheReloadHandler.selectStuShowTaskdetailReload(t.getStudent_id(), pay_order_id);
            cacheReloadHandler.selectStuCompleteOrderReload(t.getStudent_id());
            cacheReloadHandler.student_order_listReload(t.getStudent_id());
            cacheReloadHandler.selectStuUndoneOrderReload(t.getStudent_id());

        });
        cacheReloadHandler.orderListReload(order.getUid());
        cacheReloadHandler.partner_order_listReload(serviceUnit.getPartner_id());
        cacheReloadHandler.orderListReload(order.getUid());
        cacheReloadHandler.my_mission_scheduled_informationReload(serviceUnit.getPartner_id());
        return response;
    }


    /**
     * 组装以下顾客服务地址
     *
     * @param customer 顾客对象
     * @param input    输入项
     * @return CustomerAddress
     */
    private CustomerAddress customerAddress(Customer customer, CustomerAddressInput input) {

        CustomerAddress customerAddress = new CustomerAddress();
        Long customer_address_id = input.getCustomer_address_id();
        customer_address_id = customer_address_id == null ? IdGenerator.generateId() : input.getCustomer_address_id();
        customerAddress.setCustomer_address_id(customer_address_id);
        customerAddress.setCustomer_id(customer.getCustomer_id());
        customerAddress.setUsername(input.getUsername() == null ? customer.getName() : input.getUsername());
        customerAddress.setPhone(input.getPhone() == null ? customer.getPhone() : input.getPhone());
        customerAddress.setAddress(input.getAddress());
        customerAddress.setSub_address(input.getSub_address());
        customerAddress.setLbs_lat(input.getLbs_lat());
        customerAddress.setLbs_lng(input.getLbs_lng());
        customerAddress.setDefault_address(input.getDefault_address());
        customerAddress.setCreate_datetime(new Date());

        /**
         * 有关城市的匹配，此处是一个模糊匹配。并不准确
         */
        try {
            TrainCityExample example = new TrainCityExample();
            example.or().andNameEqualTo(input.getCity());
            TrainCity city = singleResult(trainCityService.selectByExample(example));

            if (city != null) {
                customerAddress.setProvince(Integer.parseInt(city.getP_id()));
                customerAddress.setCity(Integer.parseInt(city.getId()));
                example = new TrainCityExample();
                example.or().andNameEqualTo(input.getDistrict()).andP_idEqualTo(city.getId());
                TrainCity district = singleResult(trainCityService.selectByExample(example));
                if (district != null)
                    customerAddress.setArea(Integer.parseInt(district.getId()));
            }
        } catch (Exception e) {
            // 如果发生错误，就不进行存储
        }

        return customerAddress;
    }

    /**
     * 创建订单需要同时创建两个服务单，初始化两个服务单
     *
     * @param customer 顾客对象
     * @param order    订单对象
     * @param input    订单输入项
     * @return Array
     */
    private ServiceUnit[] initServiceUnit(Customer customer, Order order, OrderInput input) {

        Long customer_id = customer.getCustomer_id();
        ServiceUnit serviceUnit = serviceUnit(customer_id, order, input);
        long groupTag = IdGenerator.generateId();
        serviceUnit.setGroup_tag(groupTag);
        ServiceUnit subServiceUnit = serviceUnit(customer_id, order, input);
        subServiceUnit.setGroup_tag(groupTag);
        subServiceUnit.setPid(serviceUnit.getServiceunit_id());
        List<ServiceUnit> list = new ArrayList<>();
        list.add(serviceUnit);
        list.add(subServiceUnit);
        return list.toArray(new ServiceUnit[list.size()]);

    }

    /**
     * 组装一个服务单
     *
     * @param customer_id 顾客id
     * @param order       订单对象
     * @param orderInput  订单输入项目
     * @return ServiceUnit
     */
    private ServiceUnit serviceUnit(Long customer_id, Order order, OrderInput orderInput) {

        ServiceUnit serviceUnit = new ServiceUnit();
        long serveicUnitId = IdGenerator.generateId();
        serviceUnit.setServiceunit_id(serveicUnitId);
        serviceUnit.setCustomer_id(customer_id);
        serviceUnit.setPay_order_id(order.getPay_order_id());
        serviceUnit.setProduct_id(orderInput.getProduct_id());
        serviceUnit.setPsku_id(orderInput.getPsku_id());
        // serviceUnit.setPartner_id(station.getPartner_id());
        // serviceUnit.setStation_id(station.getStation_id());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            serviceUnit.setC_begin_datetime(simpleDateFormat.parse(orderInput.getBegin_datetime()));
            if (!StringUtils.isEmpty(orderInput.getEnd_datatime()) && !StringUtils.equals(orderInput.getEnd_datatime(), "null")) {
                serviceUnit.setC_end_datetime(simpleDateFormat.parse(orderInput.getEnd_datatime()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        serviceUnit.setActive(0);
        serviceUnit.setPid(0l);
        serviceUnit.setStatus_active(1);
        return serviceUnit;

    }

    /**
     * 看看服务站点中是否有能够提供服务的服务人员
     *
     * @param serviceUnit
     * @param station
     * @param product
     * @return
     */
    @Deprecated
    private boolean isHasProvider(ServiceUnit serviceUnit, Station station, Product product, int num) {
        Date start = serviceUnit.getC_begin_datetime();
        SimpleDateFormat dayformat = new SimpleDateFormat("yyyy-MM-dd");

        return storeService.isStationHasStore(station, dayformat.format(start), product,
                serviceUnit.getC_begin_datetime(), serviceUnit.getC_end_datetime(), num);
    }


    @Transactional
    @Override
    public void transactionTest() {

    }

    /**
     * (前台传什么,修改什么)修改顾客信息
     */
    @Override
    public MutationResult customerInfoUpdate(Customer customer, String name, String logo_img, Integer gender, String idcard) {
        logger.info("api-method:customerInfoUpdate:params:name:{},logo:{},gender:{},idcard:{}", name, logo_img, gender, idcard);
        if (StringUtils.isNotEmpty(name)) {
            customer.setName(name);
        }
        if (gender != null) {
            customer.setGender(gender);
        }
        if (StringUtils.isNotEmpty(idcard)) {
            customer.setIdcard(idcard);
        }
        if (StringUtils.isNotEmpty(logo_img)) {
            OssImg ossImg = new OssImg();
            ossImg.setOss_img_id(IdGenerator.generateId());
            ossImg.setUrl(logo_img);
            ossImg.setEffect("顾客头像");
            ossImg.setCreate_time(new Date());
            ossImg.setName(logo_img.substring(logo_img.lastIndexOf("/") + 1));
            ossImg.setBucket(logo_img.substring(logo_img.indexOf("//") + 2, logo_img.indexOf(".")));
            ossImg.setAccess_permissions("public");
            ossImg.setFormat(logo_img.substring(logo_img.lastIndexOf(".") + 1));
            ossImgService.insertSelective(ossImg);
            Img img = new Img();
            img.setId(ossImg.getOss_img_id());
            img.setUrl(logo_img);
            customer.setLogo_img(JSON.toJSONString(img));
        }
        customerService.updateByPrimaryKeySelective(customer);
        cacheReloadHandler.customerInfoReload(customer.getUser_id());
        return new MutationResult();
    }

    /**
     * 消息列表
     */
    @Override
    public List<MessageContent> queryMessageList(Customer customer, int page_index, int count) {
        logger.info("api-method:queryMessageList:params customer:{},page_index:{},count:{}", customer, page_index, count);
        List<MessageContent> messageContents = new ArrayList<>();
        MessageExample messageExample = new MessageExample();
        List<Long> ids = new ArrayList<>();
        ids.add(customer.getUser_id());
        ids.add(0l);
        messageExample.or()
                .andUser_idIn(ids)
                .andNotify_datetimeLessThan(new Date())
                .andDeletedEqualTo(0)
                .andSend_typeEqualTo(1)
                .andApp_typeEqualTo(4)
                .andApp_platformEqualTo(0);
        messageExample.setOrderByClause(MessageExample.C.notify_datetime + " DESC");
        List<Message> messages = messageService.selectByExample(messageExample, page_index, count).getList();
        if (messages.size() != 0) {
            messageContents = messages.stream().map((Message message) -> {
                MessageContent messageContent = new MessageContent();
                String content = message.getMsg_content();
                if (StringUtils.isNotEmpty(content)) {
                    messageContent.setNotifyDateTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(message.getNotify_datetime()));
                    MessageContent.ContentMsg msg = JSON.parseObject(content, MessageContent.ContentMsg.class);
                    try {
                        if (msg != null && msg.getHref() != null) {
                            msg.setHref(URLEncoder.encode(msg.getHref(), "UTF-8"));
                        }
                    } catch (Exception e) {
                        logger.error("error{}", e.getMessage());
                    }
                    messageContent.setContentMsg(msg);

                }
                BeanUtils.copyProperties(message, messageContent);
                return messageContent;
            }).collect(Collectors.toList());
        }
        logger.info("api-method:queryMessageList:process messageContents:{}", messageContents.size());
        return messageContents;
    }

    /**
     * 获取一个用户的优惠券中优惠最大的优惠券
     *
     * @param customer
     * @param psku_id
     * @param num
     * @return
     */
    @Override
    public CouponResponse theBestCoupon(Customer customer, Long psku_id, int num) {
        logger.info("api-method:theBestCoupon:params customer:{},psku_id:{},num:{}", customer, psku_id, num);
        //根据商品筛选出来的优惠券
        List<CouponResponse> couponResponses = userCouponList(customer, psku_id, num, 1, 100);
        TreeMap<Integer, Long> maps = new TreeMap<>();
        if (couponResponses.size() >= 1) {
            couponResponses.stream().forEach(t -> {
                ApiResponse<Integer> price = recalculatePrice(customer, psku_id, t.getCoupon_receive_id(), num);
                ProSku proSku = proSkuService.selectByPrimaryKey(psku_id);
                //获取到优惠金额
                Integer pricet = price.getT();
                Integer couponPrice = proSku.getPrice() * num - pricet;
                maps.put(couponPrice, t.getCoupon_receive_id());
            });
            Long couponReceiveId = maps.lastEntry().getValue();
            for (CouponResponse couponResponse : couponResponses) {
                if (couponResponse.getCoupon_receive_id().equals(couponReceiveId)) {
                    logger.info("api-method:theBestCoupon:process couponResponse:{}", couponResponse);
                    return couponResponse;
                }
            }
        }
        return null;
    }

    /**
     * 实时获取预约数量接口
     *
     * @param product_id
     * @return
     */
    @Override
    public Integer getNumberOfAppointments(Long product_id) {
        logger.info("api-method:getNumberOfAppointments:params product_id:{}", product_id);
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.or().andProduct_idEqualTo(product_id);
        Integer num = orderItemService.xSumNum(orderItemExample);
        Product product = productService.selectByPrimaryKey(product_id);
        Integer sum = 0;
        if (product != null) {
            sum = num + product.getBase_buyed();
        }
        cacheReloadHandler.productDetailReload(product_id);
        logger.info("api-method:getNumberOfAppointments:process sum:{}", sum);
        return sum;
    }


}



