package com.aobei.trainapi.server;

import com.aobei.train.IdGenerator;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.service.*;

import custom.bean.Constant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestService {

    Logger logger  = LoggerFactory.getLogger("d");
    @Autowired
    UsersService usersService;

    @Autowired
    StudentService studentService;

    @Autowired
    OrderService orderService;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RedisService redisService;

    @Autowired
    RedisTemplate<String, Object> customerRedisTemplate;
    @Autowired
    StoreService storeService;
    @Autowired
    RedisTemplate<String, Object> template;


    @Autowired
    CacheReloadHandler handler;

    @Test
    public void testPasswordEncode() {

    }


    @Test
    public void testUser() {
        //1	testa	$2a$10$gUnLF4Y7ejeNmOwFMmj3FOY6y7jxBrJX9SfjyYwshF9/SWYeAYF1G	1	ROLE_ZLJC
        //2	testb	$2a$10$4BewvomI1ehMCRj3ztZF/uHI56X6tfNxzLBBeE0AJUkZFRaoUNYPW	1	ROLE_PARTNER
        //3	testc	$2a$10$4BewvomI1ehMCRj3ztZF/uHI56X6tfNxzLBBeE0AJUkZFRaoUNYPW	1	ROLE_TMANAGER
        UsersExample example = new UsersExample();
        example.or().andNicknameIsNotNull();
        List<Users> user = usersService.selectByExample(example);
        Assert.assertEquals(user.size(), 0);
        byte b = 1;
        Users users = new Users();
        users.setUser_id(IdGenerator.generateId());
        users.setRoles("ROLE_ZLJC");
        users.setPassword("$2a$10$gUnLF4Y7ejeNmOwFMmj3FOY6y7jxBrJX9SfjyYwshF9/SWYeAYF1G");
        users.setUsername("testa");
        users.setStatus(b);

        Users users2 = new Users();
        users2.setUser_id(IdGenerator.generateId());
        users2.setRoles("ROLE_PARTNER");
        users2.setPassword("$2a$10$gUnLF4Y7ejeNmOwFMmj3FOY6y7jxBrJX9SfjyYwshF9/SWYeAYF1G");
        users2.setUsername("testb");
        users2.setStatus(b);

        Users users3 = new Users();
        users3.setUser_id(IdGenerator.generateId());
        users3.setRoles("ROLE_TMANAGER");
        users3.setPassword("$2a$10$gUnLF4Y7ejeNmOwFMmj3FOY6y7jxBrJX9SfjyYwshF9/SWYeAYF1G");
        users3.setUsername("testc");
        users3.setStatus(b);
        usersService.insertSelective(users);
        usersService.insertSelective(users2);
        usersService.insertSelective(users3);


    }

    @Test
    public void updateOrderExpireTime() {


        List<Order> list = orderService.selectByExample(new OrderExample());
        for (Order order : list) {
            Date date = order.getCreate_datetime();
            Date expiredate = new Date(date.getTime() + 30 * 60 * 1000);
            order.setExpire_datetime(expiredate);
            orderService.updateByPrimaryKeySelective(order);
        }

    }

    @Test
    public void testMqConsumer() {


    }

    @Test
    public void errorUpdate() {

    }

    @Test
    public void testredis() {
        Customer customer = new Customer();
        customer.setChannel_id(1224);
        customer.setName("nihao");
        customer.setPhone("1860020");
        customer.setName("任丕明");
        customerRedisTemplate.opsForValue().set("customer", customer);

        System.out.println(customerRedisTemplate.opsForValue().get("customer"));


    }

    @Test
    public void testUpdatetime() {
        storeService.updateAvilableTimeUnits(11l, "2018-05-17", 10, 20, 1);
    }


    @Test
    public void testCatchAdd() {

        redisTemplate.opsForValue().set("addressList:customer_id:1234567", "111111111");
        redisTemplate.opsForValue().set("bannerList:", "ddddd");
        redisTemplate.opsForValue().set("customerInfo:user_id:1234567", "11111");
        redisTemplate.opsForValue().set("couponList:customer_id:1234567", "1111");
        redisTemplate.opsForValue().set("recommendProducts:", "1111");
        redisTemplate.opsForValue().set("homePageProducts2:", "1111");
        redisTemplate.opsForValue().set("homeProductList:", "1111");
        redisTemplate.opsForValue().set("homeCategoryList:", "111");
        redisTemplate.opsForValue().set("getDefaultAddress:customer_id:1234567", "1111");
        redisTemplate.opsForValue().set("my_employeeManagement:partner_id:1234567", "11111");
        redisTemplate.opsForValue().set("my_mission_scheduled_information:partner_id:1234567", "dddddd");
        redisTemplate.opsForValue().set("orderList:customer_id:1234567", "2222");
        redisTemplate.opsForValue().set("orderDetail:customer_id:1234567:pay_order_id:78910", "22222");
        redisTemplate.opsForValue().set("productEvaluatesList:product_id:1234567", "11111");
        redisTemplate.opsForValue().set("partner_order_detail:pay_order_id:78910", "1111");
        redisTemplate.opsForValue().set("partner_order_list:partner_id:1234567", "11111");
        redisTemplate.opsForValue().set("productEvaluateBase:product_id:1234567", "111111");
        redisTemplate.opsForValue().set("partnerInfoByUserId:user_id:1234567", "ddddd");
        redisTemplate.opsForValue().set("proSkuList:product_id:1234567", "dddd");
        redisTemplate.opsForValue().set("productListByCategoryLevelCode:", "didik");
        redisTemplate.opsForValue().set("productDetail:product_id:1234567", "dkdkd");
        redisTemplate.opsForValue().set("studentInfoByUserId:user_id:1234567", "dkdk");
        redisTemplate.opsForValue().set("Store_information:partner_id:1234567", "29499");
        redisTemplate.opsForValue().set("selectStuShowTaskdetail:student_id:1234567:pay_order_id:78910", "1e1");
        redisTemplate.opsForValue().set("selectStuCompleteOrder:student_id:1234567", "kdkn");
        redisTemplate.opsForValue().set("selectStuUndoneOrder:student_id:1234567", "kdkdk");
        redisTemplate.opsForValue().set("student_order_list:student_id:1234567", "kldkdk");
        redisTemplate.opsForValue().set("userCouponList:customer_id:1234567", "kkdkdk");


    }


    @Test
    public void testCatchReload() {

        handler.addressListReload(1234567l);

        handler.bannerListReload();

        handler.customerInfoReload(1234567l);
        handler.couponListReload(1234567l);

        handler.homeProductListReload();
        handler.homeCategoryListReload();

        handler.getDefaultAddressReload(1234567l);


        handler.my_employeeManagementReload(1234567l);
        handler.my_mission_scheduled_informationReload(1234567l);


        handler.orderListReload(1234567l);
        handler.orderDetailReload(1234567l, "78910");

        handler.productEvaluatesListReload(1234567l);
        handler.partner_order_detailReload("78910");
        handler.partner_order_listReload(1234567l);
        handler.productEvaluateBaseReload(1234567l);
        handler.partnerInfoByUserIdReload(1234567l);
        handler.proSkuListReload(1234567l);
        handler.productListByCategoryLevelCode();
        handler.productDetailReload(1234567l);

        handler.studentInfoByUserIdReload(1234567l);
        handler.store_informationReload(1234567l);
        handler.selectStuShowTaskdetailReload(1234567l, "78910");
        handler.selectStuCompleteOrderReload(1234567l);
        handler.selectStuUndoneOrderReload(1234567l);
        handler.student_order_listReload(1234567l);


        handler.userCouponListReload(1234567l);

    }


    @Test
    public void catchreload() {
        handler.userCouponListReload(1099040188304236544L);
    }


    @Test
    public void testIncrese() {
        redisTemplate.opsForValue().increment("key1111", -1);
    }

    @Test
    public void testRedisType() {

        redisTemplate.opsForHash().put("order_id", "123", "12345");


    }


    @Test
    public void teststore() {

        redisTemplate.opsForValue().set(Constant.getStudentTaskScheduleKey(1067677206111346688L,"2018--08-16"),Constant.defaultAvailableTime);
        Student student = new Student();
        student.setStudent_id(1067677206111346688L);
        student.setService_cycle("[\"4\",\"5\"]");
        student.setService_times("{\"s\":\"40\",\"e\":\"42\"}");
        boolean store  = storeService.isStudentHasStore(student,"2018-08-16",8,12);

        logger.info("+++++++++++++++++++{}",store);


    }


}