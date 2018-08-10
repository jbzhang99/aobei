package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aobei.common.boot.EventPublisher;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.Constant;
import custom.bean.SkuTime;
import custom.bean.TimeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class StoreServiceImpl implements StoreService {


    @Autowired
    RedisService redisService;
    @Autowired
    StudentService studentService;
    @Autowired
    ProductService productService;
    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    MetadataService metadataService;
    @Autowired
    ProSkuService proSkuService;
    @Autowired
    RobbingService robbingService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    StationService stationService;
    @Autowired
    BespeakService bespeakService;
    @Autowired
    EventPublisher publisher;

    Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);
    public static final int RELEASE = 1;
    public static final int TAKEN = 2;
    public static final int SORDER = 3;

    /**
     * @param stations
     * @param span
     * @param proSku
     * @param num
     * @return
     */
    public List<TimeModel> stationsTimeModel(List<Station> stations, int span, ProSku proSku, int num) {
        Product product = productService.selectByPrimaryKey(proSku.getProduct_id());
        Integer bs_id = proSku.getBespeak_strategy_id();
        List<TimeModel> timeModels = new ArrayList<>();
        String times = proSku.getService_times();//[{"s":16,"e":20},{"s":22,"e":26},{"s":28,"e":32},{"s":34,"e":38}]
        List<SkuTime> timesList = JSON.parseArray(times, SkuTime.class);
        //开始预约策略
        Map<String, String> map = getBespeak(bs_id);
        String day = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        if ("00:00".equals(map.get("before_hour"))) {
            day = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        Integer minutes = Integer.parseInt(map.get("after_minutes"));
        LocalDateTime localDateTime = LocalDateTime.parse(day + "T" + map.get("before_hour")
                , DateTimeFormatter.ISO_LOCAL_DATE_TIME).plusMinutes(minutes);


        List<List<Student>> students = new ArrayList<>();
        for (Station station : stations) {
            List<Student> subStudent = studentService.getStudentByStationAndProduct(station, product);
            if (subStudent.size() > 0)
                students.add(subStudent);
        }
        for (int i = 0; i < span; i++) {
            String dateTime = localDateTime.plusDays(i).format(DateTimeFormatter.ISO_LOCAL_DATE);
            TimeModel timeModel = new TimeModel();
            timeModel.setDateTime(dateTime);
            try {
                timeModel.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //timeModel.setDate(LocalDateTime.parse(dateTime,DateTimeFormatter.ISO_LOCAL_DATE).toLocalDate());
            boolean dayActive = false;
            for (SkuTime skuTime : timesList) {
                int begin = skuTime.getS();
                int end = skuTime.getE();
                num = proSku.getBuy_multiple_o2o() == 1 ? num : 1;
                String endString = null;
                if (end > 42) {
                    end = 42;
                } else {
                    endString = Constant.timeUnitList.get(end);
                }
                if (LocalDateTime.parse(dateTime + "T" + Constant.timeUnitList.get(begin)).isAfter(localDateTime)
                        || LocalDateTime.parse(dateTime + "T" + Constant.timeUnitList.get(begin)).isEqual(localDateTime)) {
                    boolean has = false;
                    for (List<Student> subStudents : students) {
                        //使用学员副本进行时间计算
                        if (isStudentHasStoreWithCopy(subStudents, dateTime, begin, end, num)) {
                            has = true;
                            dayActive = true;
                            break;
                        }
                    }
                    timeModel.addModel(Constant.timeUnitList.get(begin), endString, has);
                } else {
                    timeModel.addModel(Constant.timeUnitList.get(begin), endString, false);
                }
            }
            timeModel.setActive(dayActive);
            timeModels.add(timeModel);
        }
        return timeModels;
    }

    public List<TimeModel> studentTimeModel(Student student, int span, ProSku proSku, int num) {

        Integer bs_id = proSku.getBespeak_strategy_id();
        List<TimeModel> timeModels = new ArrayList<>();
        String times = proSku.getService_times();
        List<SkuTime> timesList = JSON.parseArray(times, SkuTime.class);
        //开始预约策略
        Map<String, String> map = getBespeak(bs_id);
        String day = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        if ("00:00".equals(map.get("before_hour"))) {
            day = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        Integer minutes = Integer.parseInt(map.get("after_minutes"));
        LocalDateTime localDateTime = LocalDateTime.parse(day + "T" + map.get("before_hour")
                , DateTimeFormatter.ISO_LOCAL_DATE_TIME).plusMinutes(minutes);

        for (int i = 0; i < span; i++) {
            String dateTime = localDateTime.plusDays(i).format(DateTimeFormatter.ISO_LOCAL_DATE);
            TimeModel timeModel = new TimeModel();
            timeModel.setDateTime(dateTime);
            for (SkuTime skuTime : timesList) {
                int begin = skuTime.getS();
                int end = skuTime.getE();
                String endString = null;
                if (end > 42) {
                    end = 42;
                } else {
                    endString = Constant.timeUnitList.get(end);
                }
                num = proSku.getBuy_multiple_o2o() == 1 ? num : 1;
                if (LocalDateTime.parse(dateTime + "T" + Constant.timeUnitList.get(begin)).isAfter(localDateTime)
                        && isStudentHasStore(student, dateTime, begin, end)) {
                    timeModel.addModel(Constant.timeUnitList.get(begin), endString, true);
                } else {
                    timeModel.addModel(Constant.timeUnitList.get(begin), endString, false);
                }
            }
            timeModels.add(timeModel);
        }
        return timeModels;
    }

    private Map<String, String> getBespeak(Integer bs_id) {
        Bespeak bespeak = bespeakService.selectByPrimaryKey(bs_id);
        List<Map<String, String>> list = new ArrayList<>();
        if (bespeak != null) {
            String json = bespeak.getBespeak_strategy();
            list = JSON.parseObject(json, new TypeReference<List<Map<String, String>>>() {
            });
        }

        //开始预约策略
        Map<String, String> map = null;
        if (list.size() > 0) {
            map = list.stream().min(Comparator.comparing(o -> o.get("before_hour"))).get();
            list = list.stream().filter(t -> LocalDateTime.parse(
                    LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "T" + t.get("before_hour")
                    , DateTimeFormatter.ISO_LOCAL_DATE_TIME).isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            if (list.size() > 0) {
                map = list.stream().min(Comparator.comparing(o -> o.get("before_hour"))).get();
            }
        }
        //如果没有找到任何的预约策略。给出一个默认策略,可预约当前一小时后的订单
        if (map == null) {
            map = new HashMap<>();
            String min = LocalDateTime.now().format(DateTimeFormatter.ISO_TIME).substring(0, 5);
            map.put("before_hour", min);
            map.put("after_minutes", "60");
        }
        return map;
    }

    /**
     * 查看一个station下是否有足够的库存可供销售
     *
     * @param station  工作站
     * @param dateTime 日期
     * @param product  产品
     * @param proSku   产品sku
     * @param num      购买的数量
     * @return
     */
    public TimeModel stationTimeModel(Station station, String dateTime, Product product, ProSku proSku, int num) {
        List<Student> students = studentService.getStudentByStationAndProduct(station, product);
        return studentsTimeModel(students, dateTime, proSku, num);
    }

    /**
     * 获取学员是的时间安排
     *
     * @param students 学员集合
     * @param dateTime 那一天
     * @param proSku   产品sku
     * @param num      服务人员数量
     * @return
     */
    public TimeModel studentsTimeModel(List<Student> students, String dateTime, ProSku proSku, int num) {
        TimeModel timeModel = new TimeModel();
        timeModel.setDateTime(dateTime);
        String times = proSku.getService_times();
        List<SkuTime> timesList = JSON.parseArray(times, SkuTime.class);
        for (SkuTime skuTime : timesList) {
            int begin = skuTime.getS();
            int end = skuTime.getE();
            Integer o2o = proSku.getBuy_multiple_o2o();
            if (o2o != 1) {
                //如果产品不提供多人服务，那么一次购买多个，也按照一个来进行计算
                num = 1;
            }
            String endString = null;
            if (end > 42) {
                end = 42;
            } else {
                endString = Constant.timeUnitList.get(end);
            }
            if (isStudentsHaveStore(students, dateTime, begin, end, num)) {
                timeModel.addModel(Constant.timeUnitList.get(begin), endString, true);
            } else {
                timeModel.addModel(Constant.timeUnitList.get(begin), endString, false);
            }
        }
        return timeModel;
    }

    @Override
    public boolean isStationsHaveStore(List<Station> stations, String datetime, Product product, Date begin, Date end, int num) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Integer endInt = 42;
        if (end != null) {
            endInt = Constant.timeUnisMap.get(format.format(end));
        }
        return isStationsHaveStore(stations, datetime, product,
                Constant.timeUnisMap.get(format.format(begin)),
                endInt, num);
    }

    @Override
    public boolean isStationsHaveStore(List<Station> stations, String datetime, Product product, int begin, int end, int num) {
        if (end > 42) {
            end = 42;
        }
        for (Station station : stations) {
            if (isStationHasStore(station, datetime, product, begin, end, num)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStationsHaveStore(List<Station> stations, String datetime, Product product, String begin, String end, int num) {
        Integer endInt = 42;
        if (end != null) {
            endInt = Constant.timeUnisMap.get(end);
        }

        return isStationsHaveStore(stations, datetime, product, Constant.timeUnisMap.get(begin), endInt, num);
    }

    @Override
    public boolean isStationHasStore(Station station, String dateTime, Product product, int begin, int end, int num) {
        List<Student> students = studentService.getStudentByStationAndProduct(station, product);
        if (end > 42) {
            end = 42;
        }
        return isStudentsHaveStore(students, dateTime, begin, end, num);
    }

    @Override
    public boolean isStationHasStore(Station station, String dateTime, Product product, Date begin, Date end, int num) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Integer endInt = 42;
        if (end != null) {
            endInt = Constant.timeUnisMap.get(format.format(end));
        }
        boolean b = isStationHasStore(station, dateTime, product, Constant.timeUnisMap.get(format.format(begin)),
                endInt, num);
        return b;
    }

    @Override
    public boolean isStationHasStore(Station station, String dateTime, Product product, String begin, String end, int num) {
        Integer endInt = 42;
        if (end != null) {
            endInt = Constant.timeUnisMap.get(end);
        }
        return isStationHasStore(station, dateTime, product, Constant.timeUnisMap.get(begin), endInt, num);
    }

    @Override
    public boolean isStudentsHaveStore(List<Student> students, String dateTime, Integer begin, Integer end, int num) {
        if (begin == null)
            return false;
        int tmp = 0;

        if (end > 42) {
            end = 42;
        }
        for (Student student : students) {
            if (isStudentHasStore(student, dateTime, begin, end)) {
                tmp += 1;
                if (tmp >= num) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean isStudentHasStore(Student student, String dateTime, Integer begin, Integer end) {
        if (begin == null)
            return false;
        if (end > 42) {
            end = 42;
        }
        String key = Constant.getStudentTaskScheduleKey(student.getStudent_id(), dateTime);
        String schedule = redisService.getStringValue(key);
        Integer[] schedules = null;
        logger.info("isStudent{},{},{}",dateTime,begin,end);
        try {
            schedules = scheduleStr(schedule, dateTime, student);
        } catch (Exception e) {
            logger.error("data of student is illegal  studentId:{}", student.getStudent_id(),e);
            return false;

        }
        //此处不在进行数据更新。只做处理
       // redisService.setStringValue(key, JSON.toJSONString(schedules), dateTime + " 23:59:59");

        return isHaveStore(schedules, begin, end, Constant.SERVICE_INTERVAL_UNIT);
    }

    @Override
    public boolean isStudentHasStore(Student student, String dateTime, Date begin, Date end) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Integer endInt = 42;
        if (end != null) {
            endInt = Constant.timeUnisMap.get(format.format(end));
        }

        return isStudentHasStore(student, dateTime, Constant.timeUnisMap.get(format.format(begin)), endInt);
    }

    @Override
    public boolean isStudentsHaveStore(List<Student> students, String dateTime, Date begin, Date end, int num) {
        if (begin == null) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Integer endInt = 42;
        if (end != null) {
            endInt = Constant.timeUnisMap.get(format.format(end));
        }
        return isStudentsHaveStore(students, dateTime, Constant.timeUnisMap.get(format.format(begin)),
                endInt, num);
    }

    @Override
    public boolean isStudentsHaveStore(List<Student> students, String dateTime, String begin, String end, int num) {
        if (begin == null)
            return false;
        Integer endInt = 42;
        if (end != null) {
            endInt = Constant.timeUnisMap.get(end);
        }
        return isStudentsHaveStore(students, dateTime, Constant.timeUnisMap.get(begin), endInt, num);
    }


    /**
     * 跟新服务人员的时间调度缓存
     *
     * @param student_id 学员
     * @param dateTime   服务日期
     * @param begin      开始
     * @param end        结束
     * @param upType     更新类型
     */
    @Override
    public void updateAvilableTimeUnits(Long student_id, String dateTime, int begin, int end, int upType) {
        if (upType != TAKEN && upType != RELEASE && upType != SORDER)
            return;
        String key = Constant.getStudentTaskScheduleKey(student_id, dateTime);
        String schedule = redisService.getStringValue(key);
        Integer[] integers = null;

        Student student = studentService.selectByPrimaryKey(student_id);
        integers = scheduleStr(schedule, dateTime, student);
        for (int i = begin; i <= end; i++) {
            if (i < 0 || i > integers.length)
                continue;
            integers[i] = upType;
        }
        schedule = JSON.toJSONString(integers);
        redisService.setStringValue(key, schedule, dateTime + " 23:59:59");


    }

    @Override
    public void updateAvilableTimeUnits(Long student_id, String dateTime, String begin, String end, int upType) {
        logger.info("api-method:updateAvilableTimeUnits student_id:{},dateTime:{},begin:{},end:{}", student_id, dateTime, begin, end);
        Integer startInt = Constant.timeUnisMap.get(begin);
        Integer endInt = Constant.timeUnisMap.get(end);
        if (startInt != null || endInt != null)
            updateAvilableTimeUnits(student_id, dateTime, startInt, endInt, upType);
    }

    @Override
    public void updateAvilableTimeUnits(Long student_id, Date begin, Date end, int upType) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = format1.format(begin);
        String beginString = format.format(begin);
        Integer startInt = Constant.timeUnisMap.get(beginString);
        Integer endInt = 42;
        if (end != null) {
            endInt = Constant.timeUnisMap.get(format.format(end));
        }
        logger.info("api-method:updateAvilableTimeUnits 2222222 student_id:{},dateTime:{},begin:{},end:{}", student_id, dateTime, begin, end);
        if (startInt != null || endInt != null)
            updateAvilableTimeUnits(student_id, dateTime, startInt, endInt, upType);
    }


    /**
     * 根据时间字符串获得能够进行服务的服务人员列表
     *
     * @param students
     * @param dateTime
     * @param begin
     * @param end
     * @return
     */
    @Override
    public List<Student> serviceProviderList(List<Student> students, String dateTime, String begin, String end, int num) {
        int intbegin = Constant.timeUnisMap.get(begin);
        Integer intEnd = 42;
        if (end != null) {
            intEnd = Constant.timeUnisMap.get(end);
        }
        return serviceProviderList(students, dateTime, intbegin, intEnd, num);
    }

    /**
     * 过滤所有能够提供服务的服务人员。得到最后能够提供该服务的服务人员
     *
     * @param students 服务人员集合
     * @param dateTime 对于那一天
     * @param begin    服务的开始标示
     * @param end      服务的结束标示
     * @return
     */
    @Override
    public List<Student> serviceProviderList(List<Student> students, String dateTime, int begin, int end, int num) {
        if (end > 42) {
            end = 42;
        }
        int end2 = end;
        return students.stream().filter(t -> isStudentHasStore(t, dateTime, begin, end2)).collect(Collectors.toList());
    }

    /**
     * 进行预占库存操作。
     * <p>
     * 采用副本的方式进行，不实际占用服务人员的库存量。
     * 需要在对应的位置进行副本的清除。
     * 1.自动取消
     * 2.接单。
     *
     * @param pay_order_id
     * @param stations
     * @param datetime
     * @param product
     * @param start
     * @param end
     * @param num
     * @return
     */
    @Override
    public boolean preTakeAvilableTimeUnits(String pay_order_id, List<Station> stations, String datetime, Product product, Date start, Date end, int num) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Integer endInt = 42;
        Integer beginInt = Constant.timeUnisMap.get(format.format(start));
        if (end != null) {
            endInt = Constant.timeUnisMap.get(format.format(end));
        }
        boolean taken = false;
        List<Student> tmpList = new ArrayList<>();
        for (Station station : stations) {
            if (taken)
                break;
            tmpList.clear();
            List<Student> students = studentService.getStudentByStationAndProduct(station, product);
            int tmp = 0;
            for (Student student : students) {
                //首先获取该服务人员的时间调度副本。
                String key = Constant.getPreTakenKey(student.getStudent_id(), datetime, beginInt, endInt);
                String schedule = redisService.getStringValue(key);
                //如果时间副本不存在使用原时间调度
                if (schedule != null) {
                    continue;
                }
                String key1 = Constant.getStudentTaskScheduleKey(student.getStudent_id(), datetime);
                schedule = redisService.getStringValue(key1);
                Integer[] schedules = null;
                try {
                    schedules = scheduleStr(schedule, datetime, student);
                } catch (Exception e) {
                    schedules = Constant.defaultAvailableTimeArrar;
                    logger.error("data of student is  illegal  studentId:{}", student.getStudent_id());
                }

                if (isHaveStore(schedules, beginInt, endInt, Constant.SERVICE_INTERVAL_UNIT)) {
                    tmpList.add(student);
                    tmp += 1;
                    if (tmp >= num) {
                        taken = true;
                        break;
                    }
                }

            }
        }
        if (tmpList.size() > 0 && taken) {
            //进行预占库存处理。
            for (Student student : tmpList) {
                String key = Constant.getPreTakenKey(student.getStudent_id(), datetime, beginInt, endInt);
                //虚拟库存占用。占用时常和正常的占用相同
                redisService.setStringValue(key, pay_order_id, datetime + " 23:59:59");
                redisService.sAdd(pay_order_id, key);
                redisService.expireAt(pay_order_id, redisService.getExpire(key, TimeUnit.SECONDS), TimeUnit.SECONDS);
            }

            return true;
        }

        return false;
    }

    /**
     * 使用虚拟库存进行库存计算
     *
     * @param students
     * @param datetime
     * @param begin
     * @param end
     * @param num
     * @return
     */
    private boolean isStudentHasStoreWithCopy(List<Student> students, String datetime, int begin, int end, int num) {
        int tmp = 0;

        if (end > 42) {
            end = 42;
        }
        for (Student student : students) {
            //首先获取该服务人员的时间调度副本。
            String key = Constant.getPreTakenKey(student.getStudent_id(), datetime, begin, end);
            String schedule = redisService.getStringValue(key);
            if (schedule != null) {
                continue;
            }
            //如果时间副本不存在使用原时间调度
            String key1 = Constant.getStudentTaskScheduleKey(student.getStudent_id(), datetime);
            schedule = redisService.getStringValue(key1);
            Integer[] schedules = null;
            try {
                schedules = scheduleStr(schedule, datetime, student);
            } catch (Exception e) {
                schedules = Constant.defaultAvailableTimeArrar;
                e.printStackTrace();
                logger.error("data of student is  illegal  studentId:{}", student.getStudent_id());
            }

            if (isHaveStore(schedules, begin, end, Constant.SERVICE_INTERVAL_UNIT)) {
                tmp += 1;
                if (tmp >= num) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isHaveStore(Integer[] arr, int begin, int end, int restUnit) {
        //边界值检测 如果开始时间是在00:00,00:30,10:00 ，结束时间在23:00,23:30 都属于非法劳动时间。
        //去掉非法劳动时间。
        if (end < begin || begin < 3) {
            return false;
        }
        if (end > 45) {
            restUnit = 0;
        }
        //连续时间：如果不可使用，就代表不能提供服务；
        for (int i = begin - (restUnit - 1); i < end + restUnit; i++) {
            if (arr[i] != 1) {
                if ((i < begin && arr[i] == 0) || (i > end && arr[i] == 0))
                    continue;
                return false;
            }
        }
        return true;
    }

    /**
     * 如果服务人员设定了上班时间。服务人员调度时间的计算
     *
     * @param student
     * @return
     */
    private static Integer[] scheduleStr(String schedule, String dateTime, Student student) {


        String times = student.getService_times();
        if (times == null) {
            if (schedule == null) {
                schedule = Constant.defaultAvailableTime;
            }
            return JSON.parseObject(schedule, Integer[].class);
        }
        Integer[] orderArray = null;

        String weeks = student.getService_cycle();
        Map<String, Integer> map = JSON.parseObject(times, new TypeReference<Map<String, Integer>>() {
        });
        Set<Integer> set = JSON.parseObject(weeks, new TypeReference<Set<Integer>>() {
        });
        Integer[] arrars = JSON.parseObject(Constant.defaultNoTime, Integer[].class);
        if (schedule != null) {
            orderArray = JSON.parseObject(schedule, Integer[].class);
        }
        int begin = map.get("s");
        int end = map.get("e");
        LocalDate date = LocalDate.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE);
        if (set.contains(date.getDayOfWeek().getValue())) {
            for (int i = 0; i < arrars.length; i++) {
                if (orderArray != null && orderArray[i] > 1) {
                    arrars[i] = orderArray[i];
                } else {
                    if (i >= begin && i <= end) {
                        arrars[i] = 1;
                    } else {
                        arrars[i] = 0;
                    }
                }
            }
        }
        return arrars;
    }


    public static void main(String[] args) {

        Student student  = new Student();
        student.setService_cycle("[\"1\",\"2\",\"6\",\"7\"]");
        student.setService_times("{\"s\":\"16\",\"e\":\"42\"}");
        Integer[] result  = scheduleStr(JSON.toJSONString(Constant.defaultStudentsStopSingle),"2018-06-09",student);
        System.out.println(JSON.toJSONString(result));
    }
}
