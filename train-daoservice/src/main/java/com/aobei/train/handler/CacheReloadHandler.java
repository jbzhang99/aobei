package com.aobei.train.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CacheReloadHandler {

    @Autowired
    StringRedisTemplate template;

    /**用户信息相关**/
    /**
     * 更新顾客个人信息缓存
     *
     * @param user_id
     */
    @CacheEvict(value = "customerInfo", key = "'user_id:'+#user_id")
    public void customerInfoReload(Long user_id) {
    }

    /**
     * 更新服务人员信息缓存
     *
     * @param user_id
     */
    @CacheEvict(value = "studentInfoByUserId", key = "'user_id:'+#user_id")
    public void studentInfoByUserIdReload(Long user_id) {
    }

    /**
     * 更新合伙人个人信息
     *
     * @param user_id
     */
    @CacheEvict(value = "partnerInfoByUserId", key = "'user_id:'+#user_id")
    public void partnerInfoByUserIdReload(Long user_id) {
    }


    /*************************************************************************************************************
     *                         分割线只有华丽了才能割清楚，下面是和产品相关的缓存                                        *
     **************************************************************************************************************/
    /**
     * 首页分类，在首页分类进行变化时要进行变更，需要后台操作
     */
    @CacheEvict(value = "homeCategoryList", allEntries = true)
    public void homeCategoryListReload() {
    }

    /**
     * 根据分类查询产品列表，在产品属性或者向分类下添加产品时需要调用，需要后台操作
     */
    @CacheEvict(value = "productListByCategoryLevelCode", allEntries = true)
    public void productListByCategoryLevelCode() {
    }

    /**
     * 首页产品列表。在产品属性，sku 发生变化时需要更新，需要后台操作
     */
    @CacheEvict(value = {"homeProductList", "homePageProducts2", "recommendProducts"}, allEntries = true)
    public void homeProductListReload() {
    }

    @CacheEvict(value = {"productDetail", "productDetail2"}, key = "'product_id:'+#product_id")
    public void productDetailReload(Long product_id) {

    }

    /**
     * 产品sku，当一个产品的sku发生变化时需要调用
     */
    public void proSkuListReload(Long product_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("proSkuList:product_id:");
        builder.append(product_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /*************************************************************************************************************
     *                         分割线有的时候很任性的。我才管你其他的问题呢，下面是有关顾客地址的                           *
     **************************************************************************************************************/
    /**
     * 当用户对自己的地址进行操作后，就需要改变，不管是添加，还是修改
     *
     * @param customer_id
     */
    @CacheEvict(value = "addressList", key = "'customer_id:'+#customer_id")
    public void addressListReload(Long customer_id) {
    }

    /**
     * 当用户对自己的地址进行操作后，就需要改变，不管是添加，还是修改,
     * 与  addressListReload 同时使用
     *
     * @param customer_id
     */
    @CacheEvict(value = "getDefaultAddress", key = "'customer_id:'+#customer_id")
    public void getDefaultAddressReload(Long customer_id) {
    }
    /*************************************************************************************************************
     *                         上面的分割线，你就分俩个  下面是和优惠券相关的缓存                                       *
     **************************************************************************************************************/
    /**
     * 发生优惠券指派和使用的时要进行删除。需要后台处理
     *
     * @param customer_id
     */
    public void couponListReload(Long customer_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("couponList:customer_id:");
        builder.append(customer_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /**
     * 发生优惠券指派和使用的时要进行删除。需要后台处理
     * 与 couponListReload 一起使用
     *
     * @param customer_id
     */
    public void userCouponListReload(Long customer_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("userCouponList:customer_id:");
        builder.append(customer_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /*************************************************************************************************************
     *                         上面的两个分割线，你们都是一样的人啊，，，，哈哈，以下是订单相关                            *
     **************************************************************************************************************/
    /**
     * 顾客端订单变化。当发生订单创建，状态转化，等情况时需要进行缓存变更 需要后台操作
     *
     * @param customer_id
     */
    public void orderListReload(Long customer_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("orderList:customer_id:");
        builder.append(customer_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /**
     * 对那条订单进行了变化，要释放对应的缓存   需要后台操作。
     *
     * @param customer_id
     * @param pay_order_id
     */
    @CacheEvict(value = "orderDetail", key = "'customer_id:'+#customer_id+':pay_order_id:'+#pay_order_id")
    public void orderDetailReload(Long customer_id, String pay_order_id) {
    }

    /**
     * 对订单进行修改,有服务人员的调度发生变化时更新，需要后台操作
     */
    public void my_mission_scheduled_informationReload(Long partner_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("my_mission_scheduled_information:partner_id:");
        builder.append(partner_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /**
     * 有新订单产生，有订单发生变化，需要更新缓存 需要后台操作
     * @param partner_id
     */
    public void partner_order_listReload(Long partner_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("partner_order_list:partner_id:");
        builder.append(partner_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /**
     * 对产生变化的当前订单，进行缓存的更新，需要更新后台操作
     * @param pay_order_id
     */
    @CacheEvict(value = "partner_order_detail", key = "'pay_order_id:'+#pay_order_id")
    public void partner_order_detailReload(String pay_order_id) {
    }

    /**
     * 当订单分配到服务人员，更换服务人员，服务状态变更等情况下，需要后台操作
     * @param student_id
     */
    public void selectStuUndoneOrderReload(Long student_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("selectStuUndoneOrder:student_id:");
        builder.append(student_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /**
     * 当订单状态更新到已完成的情况时，进行更新缓存
     * @param student_id
     */
    public void selectStuCompleteOrderReload(Long student_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("selectStuCompleteOrder:student_id:");
        builder.append(student_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /**
     * 当前发生变化的订单已经分配给了服务人员的，进行缓存变更 ，需要后台操作
     * @param student_id
     * @param pay_order_id
     */
    @CacheEvict(value = "selectStuShowTaskdetail", key = "'student_id:'+#student_id+':pay_order_id:'+#pay_order_id")
    public void selectStuShowTaskdetailReload(Long student_id, String pay_order_id) {
    }

    /**
     * 当已经分配了服务人员的订发生变化时，更新缓存 ,需要后台操作
     * @param student_id
     */
    public void student_order_listReload(Long student_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("student_order_list:student_id:");
        builder.append(student_id);
        builder.append("*");
        clearKeys(builder.toString());
    }


    /*************************************************************************************************************
     *                        我也来。。。。。。。我是大家最喜欢的评论      评论相关的缓存                               *
     **************************************************************************************************************/
    /**
     * 对一个产品产生新评价的时候更新缓存
     */
    public void productEvaluateBaseReload(Long product_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("productEvaluateBase:product_id:");
        builder.append(product_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /**
     *  对一个产品产生新评价的时候更新缓存
     *  与productEvaluateBaseReload同时使用
     * @param product_id
     */
    public void productEvaluatesListReload(Long product_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("productEvaluatesList:product_id:");
        builder.append(product_id);
        builder.append("*");
        clearKeys(builder.toString());
    }


    /*************************************************************************************************************
     *                        我也来。。。。。。。我是大家最喜欢的评论      评论相关的缓存                               *
     **************************************************************************************************************/

    /**
     * banner图发生变化时需要进行变更。需要后台操作
     */
    @CacheEvict(value = "bannerList", allEntries = true)
    public void bannerListReload() {

    }

    /**
     * 服务人员数据发生变化时进行更新。需要后台操作
     * @param partner_id
     */
    public void my_employeeManagementReload(Long partner_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("my_employeeManagement:partner_id:");
        builder.append(partner_id);
        builder.append("*");
        clearKeys(builder.toString());
    }

    /**
     * 合伙人 的工作站发生变化时进行变更。需要后台操作
     *
     * @param partner_id
     */
    @CacheEvict(value = "Store_information", key = "'partner_id:'+#partner_id")
    public void store_informationReload(Long partner_id) {
    }

    protected void clearKeys(String prekey) {
        Set<String> set = template.opsForValue().getOperations().keys(prekey);
        if (set != null && set.size() > 0) {
            template.opsForValue().getOperations().delete(set);
        }
    }
}
