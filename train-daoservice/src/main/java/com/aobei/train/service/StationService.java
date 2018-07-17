package com.aobei.train.service;

import com.aobei.train.model.*;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

import java.util.List;

public interface StationService extends MbgReadService<Long, Station, Station, StationExample>,MbgWriteService<Long, Station, Station, StationExample>,MbgUpsertService<Long, Station, Station, StationExample>{

    /**
     * 根据用提交的地址和坐标，找出所有附近的商家，目前仅支持数据库访问的方式查找。
     * 缓存方式的需要后期再添加--
     * @param customerAddress
     * @param radius
     * @return
     */
    List<Station> findNearbyStation(CustomerAddress customerAddress,int radius);
    /**
     * 该合伙人下的所有服务站点
     * @param customerAddress
     * @param radius
     * @return
     */
    List<Station> findNearbyStationPartner(CustomerAddress customerAddress,Long partner_id,int radius);
    /**
     * 根据用户提交的服务信息过滤出支持该服务的所有商家
     * @param product
     * @param stations
     * @return
     */
    List<Station> filterByProduct(Product product,List<Station> stations);

    /**
     * 根据用户提交的合伙人信息过滤附近的服务商家
     * @param partner
     * @param stations
     * @return
     */
    List<Station> filterByPatner(Partner partner,List<Station> stations);
    /**
     * 随机获取一个满足条件的商家。可以用来进行服务的指派工作
     * @param stations
     * @return
     */
    Station randomAStation(List<Station> stations);

    /**
     * 一步到位，直接顾虑出满足产品，满足合伙人的所有商家
     * @param customerAddress
     * @param radius
     * @param product
     * @param partner
     * @return
     */
    List<Station> findNearByStation(CustomerAddress customerAddress,int radius,Product product,Partner partner);

    /**
     * 根据商品获得所有station
     * @param customerAddress
     * @param radius
     * @param product
     * @return
     */
    List<Station> findNearByStation(CustomerAddress customerAddress,int radius,Product product);

}