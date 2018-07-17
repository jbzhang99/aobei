package com.aobei.train.service.impl;

import com.aobei.train.model.*;
import com.aobei.train.service.PartnerServiceitemService;

import custom.bean.Status;
import custom.util.DistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.mapper.StationMapper;
import com.aobei.train.service.StationService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import java.security.cert.CollectionCertStoreParameters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StationServiceImpl extends MbgServiceSupport<StationMapper, Long, Station, Station, StationExample> implements StationService {
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    PartnerServiceitemService partnerServiceitemService;

    @Autowired
    private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory) {
        super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(stationMapper);
    }

    @Override
    public List<Station> findNearbyStation(CustomerAddress customerAddress, int radius) {
        String lat = customerAddress.getLbs_lat();
        String lng = customerAddress.getLbs_lng();
        StationExample example = new StationExample();
        Integer city = customerAddress.getCity();
        StationExample.Criteria criteria = example.or();
        if(city!=null){
            criteria.andCityEqualTo(city);
        }
        criteria.andOnlinedEqualTo(1);
        criteria.andDeletedEqualTo(Status.DeleteStatus.no.value);//所有的数据
        List<Station> stations = selectByExample(example);
        //使用stream过滤
        stations = stations.stream().filter(t -> DistanceUtil.GetDistance(
                new Double(t.getLbs_lat() == null ? "0" : t.getLbs_lat()),
                new Double(t.getLbs_lng() == null ? "0" : t.getLbs_lng()),
                new Double(lat),
                new Double(lng)) <= radius).collect(Collectors.toList());
        return stations;
    }
    
    @Override
    public List<Station> findNearbyStationPartner(CustomerAddress customerAddress,Long partner_id, int radius) {
        String lat = customerAddress.getLbs_lat();
        String lng = customerAddress.getLbs_lng();
        StationExample example = new StationExample();
        Integer city = customerAddress.getCity();
        example.or().andCityEqualTo(city).andOnlinedEqualTo(1)
                .andDeletedEqualTo(Status.DeleteStatus.no.value)
                .andPartner_idEqualTo(partner_id);//所有的数据
        List<Station> stations = selectByExample(example);
        //使用stream过滤
        stations = stations.stream().filter(t -> DistanceUtil.GetDistance(
                new Double(t.getLbs_lat() == null ? "0" : t.getLbs_lat()),
                new Double(t.getLbs_lng() == null ? "0" : t.getLbs_lng()),
                new Double(lat),
                new Double(lng)) <= radius).collect(Collectors.toList());
        return stations;
    }

    @Override
    public List<Station> filterByProduct(Product product, List<Station> stations) {

        return stations.stream().map(t -> {
            PartnerServiceitemExample example = new PartnerServiceitemExample();
            example.or().andPartner_idEqualTo(t.getPartner_id()).andServiceitem_idEqualTo(product.getServiceitem_id());
            if (partnerServiceitemService.countByExample(example) > 0) {
                return t;
            }
            return null;
        }).filter(t -> t != null).collect(Collectors.toList());
    }

    @Override
    public List<Station> filterByPatner(Partner partner, List<Station> stations) {
        return stations.stream()
                .filter(t -> t.getPartner_id() == partner.getPartner_id())
                .collect(Collectors.toList());
    }

    @Override
    public Station randomAStation(List<Station> stations) {
        if (stations.size() == 0)
            return null;
        Random random = new Random();
        int target = random.nextInt(stations.size());
        return stations.get(target);
    }

    @Override
    public List<Station> findNearByStation(CustomerAddress customerAddress, int radius, Product product, Partner partner) {

        return filterByPatner(partner, filterByProduct(product, findNearbyStation(customerAddress, radius)));

    }

    @Override
    public List<Station> findNearByStation(CustomerAddress customerAddress, int radius, Product product) {
        return filterByProduct(product, findNearbyStation(customerAddress, radius));
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
//        return list.stream().map(n -> {
//            CourseInfoEvaluate  cie = new CourseInfoEvaluate();
//            cie.setTrainScheduleInfo(n);
//            cie.setCourseEvaluate(evaluateMap.get(n.getTrainSchedule().getTrain_schedule_id()));
//            return cie;
//        }).collect(Collectors.toList());

        list = list.stream().filter(t -> t > 50).collect(Collectors.toList());

        System.out.println(list);
    }


}