package com.aobei.train.service;

import com.alibaba.fastjson.JSON;
import com.aobei.train.model.*;
import custom.bean.Constant;
import custom.bean.SkuTime;
import custom.bean.TimeModel;
import custom.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author  renpiming  2018/05/14
 * this is the third time to change the store function .
 * 1.首先将工作站按照城市来进行区分。需要将一个城市内的工作站进行缓存处理。
 * 2.筛选出的工作站，要进行标签化。工作站是否能够提供相应的服务。此处暂时进行实时处理，不进行缓存处理
 * 3.获取每个工作站中具备所选产品标签的服务人员。
 * 4.筛选一个能够服务所选产品，并且标签最少的学员，减掉该服务人员的库存。
 *   需要创建服务人员时间调度副本。如果服务人员存在调度副本，使用副本进行计算库存
 *   副本有效期：以自动拒单时间为准
 * 5.进行派单，删除服务人员时间调度副本
 * 6.如果进行接单操作，删除服务人员的时间调度副本
 * 7.如果取消订单，删除服务人员时间调度副本
 * 8.订单变更，删除服务人员时间调度副本
 * 服务人员时间副本：
 * (@key: pay_order_id_schedule + student_id)
 * (@value:[0,1,0,**************,1,0,0])
 *
 *
 * since   v0.4.0
 */

public interface StoreService {

    /**
     * 根据station列表 获取到所有的我们需要时间单元
     *
     * @param stations 工作站列表
     * @param span     时间跨度，此处和预约策略有关。需要进行另外的处理
     * @param proSku   产品sku
     * @param num
     * @return
     */
    List<TimeModel> stationsTimeModel(List<Station> stations, int span, ProSku proSku, int num);

    /**
     *  获得一个学员的可用时间列表
     * @param student
     * @param span
     * @param proSku
     * @param num
     * @return
     */
    List<TimeModel> studentTimeModel(Student student, int span, ProSku proSku, int num);
    /**
     * 获取工作站的可用时间单元。一个工作站一天的
     *
     * @param station  工作站
     * @param dateTime 那一天
     * @param product  产品
     * @param proSku   产品sku
     * @param num      数量
     * @return
     */
    TimeModel stationTimeModel(Station station, String dateTime, Product product, ProSku proSku, int num);

    /**
     * 获取一个学员集合中是否存在可满足条件的时间单元
     *
     * @param students 学员集合
     * @param dateTime 那一天
     * @param proSku   产品sku
     * @param num      服务人员数量
     * @return
     */
    TimeModel studentsTimeModel(List<Student> students, String dateTime, ProSku proSku, int num);


    /**
     *
     * @param stations
     * @param datetime
     * @param product
     * @param start
     * @param end
     * @param num
     * @return
     */
    boolean isStationsHaveStore(List<Station> stations, String datetime, Product product, Date start, Date end, int num);

    /**
     *
     * @param stations
     * @param datetime
     * @param product
     * @param start
     * @param end
     * @param num
     * @return
     */
    boolean isStationsHaveStore(List<Station> stations, String datetime, Product product, int start, int end, int num);

    /**
     *
     * @param stations
     * @param datetime
     * @param product
     * @param start
     * @param end
     * @param num
     * @return
     */
    boolean isStationsHaveStore(List<Station> stations, String datetime, Product product, String start, String end, int num);


    /**
     * 判断一个工作站中是否有能够提供服务的人员
     * @param station
     * @param dateTime
     * @param product
     * @param begin
     * @param end
     * @param num
     * @return
     */
    boolean isStationHasStore(Station station, String dateTime, Product product, int begin, int end, int num);

    /**
     * 同上
     * @param station
     * @param dateTime
     * @param product
     * @param begin
     * @param end
     * @param num
     * @return
     */
    boolean isStationHasStore(Station station, String dateTime, Product product, Date begin, Date end, int num);

    /**
     * 同上
     * @param station
     * @param dateTime
     * @param product
     * @param begin
     * @param end
     * @param num
     * @return
     */
    boolean isStationHasStore(Station station, String dateTime, Product product, String begin, String end, int num);

    boolean isStudentHasStore(Student student, String dateTime, Integer begin, Integer end);
    boolean isStudentHasStore(Student student, String dateTime, Date begin, Date end);
    /**
     * 针对一天一天中的特定时间。判断给定的学员集合是否包含能够提供服务的学员
     *
     * @param students 学员集合
     * @param dateTime 那一天
     * @param begin    开始时间
     * @param end      结束时间
     * @param num      购买数量
     * @return
     */
    boolean isStudentsHaveStore(List<Student> students, String dateTime, Integer begin, Integer end, int num);

    /**
     * 同上
     *
     * @param students
     * @param dateTime
     * @param begin    日期格式   2018-04-01 08:00
     * @param end
     * @param num
     * @return
     */
    boolean isStudentsHaveStore(List<Student> students, String dateTime, Date begin, Date end, int num);

    /**
     * 同上
     *
     * @param students
     * @param dateTime
     * @param begin    字符串格式 "08:00"
     * @param end
     * @param num
     * @return
     */
    boolean isStudentsHaveStore(List<Student> students, String dateTime, String begin, String end, int num);


    /**
     * 更新服务人员时间调度缓存
     * @param student_id 学员
     * @param dateTime 服务日期
     * @param start  开始
     * @param end 结束
     */
    void updateAvilableTimeUnits(Long student_id, String dateTime, int start, int end, int upType);

    /**
     * 重载方法，更新服务人员时间调度缓存
     * @param student_id
     * @param dateTime
     * @param start
     * @param end
     */
    void updateAvilableTimeUnits(Long student_id, String dateTime, String start, String end, int upType);

    /**
     * 重载方法，更新服务人员时间调度缓存
     * @param student_id
     * @param start
     * @param end
     */
    void updateAvilableTimeUnits(Long student_id, Date start, Date end, int upType);


    /**
     * 获得可针用的服务人员。
     * @param students 服务人员列表
     * @param dateTime 服务日期
     * @param start 开始
     * @param end 结束
     * @return
     */
    List<Student> serviceProviderList(List<Student> students, String dateTime, String start, String end, int num);

    /**
     * 重载方法。获得可用的服务人员
     * @param students
     * @param dateTime
     * @param start
     * @param end
     * @return
     */
    List<Student> serviceProviderList(List<Student> students, String dateTime, int start, int end, int num);


    boolean preTakeAvilableTimeUnits(String pay_order_id, List<Station> stations, String datetime, Product product, Date start, Date end, int num);



    default List<TimeModel> meargeTimeModel(List<TimeModel> timeModels, int span, ProSku proSku) {
        Map<String, TimeModel> map = new HashMap<>();
        for (TimeModel timeModel : timeModels) {
            TimeModel tempTimeModel = map.get(timeModel.getDateTime());
            if (tempTimeModel == null) {
                tempTimeModel = new TimeModel();
                tempTimeModel.setDateTime(timeModel.getDateTime());
            }
            Set<TimeModel.Model> models = timeModel.getModels();
            for (TimeModel.Model model : models) {
                tempTimeModel.addModel(model.getStart(), model.getEnd(), model.isActive());
            }

            map.put(timeModel.getDateTime(), tempTimeModel);
        }
        List<String> daysSapn = DateUtil.daysSpan(span, false);
        timeModels.clear();
        for (String day : daysSapn) {
            if (map.get(day) != null)
                timeModels.add(map.get(day));
            else {
                //填充数据

                TimeModel timeModel = new TimeModel();
                timeModel.setDateTime(day);
                if (proSku == null) {
                    timeModel.addModel("08:00", "12:00", false);
                    timeModel.addModel("13:00", "17:00", false);
                } else {
                    String times = proSku.getService_times();
                    List<SkuTime> timesList = JSON.parseArray(times,SkuTime.class);
                    for (SkuTime skutime : timesList) {
                        timeModel.addModel(Constant.timeUnitList.get(skutime.getS()),
                                Constant.timeUnitList.get(skutime.getE()), false);
                    }
                }
                timeModel.setActive(false);
                try {
                    timeModel.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(day));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeModels.add(timeModel);
            }
        }
        return timeModels;
    }

}
