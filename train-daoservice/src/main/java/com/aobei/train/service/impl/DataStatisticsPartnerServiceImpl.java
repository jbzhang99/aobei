package com.aobei.train.service.impl;

import com.aobei.train.mapper.*;
import com.aobei.train.model.*;
import com.aobei.train.service.DataStatisticsPartnerService;
import com.aobei.train.service.bean.PurchasePartnerStatisticsData;
import custom.bean.DataStatisticsCustomData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataStatisticsPartnerServiceImpl implements DataStatisticsPartnerService {

    @Autowired
    private DataStatisticsPartnerMapper dataStatisticsPartnerMapper;

    @Autowired
    private PartnerMapper partnerMapper;


    @Override
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithDay(Date startDate, Date endDate) {
        PartnerExample partnerExample=new PartnerExample();
        partnerExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list=dataStatisticsPartnerMapper.regStatisticsWithDay(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusDays(1);
            i++;
        }

        return list;
    }

    @Override
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithWeek(Date startDate, Date endDate) {
        PartnerExample partnerExample=new PartnerExample();
        partnerExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list=dataStatisticsPartnerMapper.regStatisticsWithWeek(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy/ww周"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusWeeks(1);
            i++;
        }
        return list;
    }

    /**
     * 地图数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<DataStatisticsCustomData> incrementingRegStatisticsWithMonth(Date startDate, Date endDate) {
        PartnerExample partnerExample=new PartnerExample();
        partnerExample.or().andCdateLessThan(startDate);
        List<DataStatisticsCustomData> list=dataStatisticsPartnerMapper.regStatisticsWithMonth(startDate, endDate);
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        Map<String, Long> map = list.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        int i = 0;
        while (startLocalDateTime.isBefore(endLocalDateTime)) {
            String key = startLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            if (!map.containsKey(key)) {
                // 补充空位日期
                DataStatisticsCustomData temp = new DataStatisticsCustomData();
                temp.setDateStr(key);
                temp.setNum(0L);
                list.add(i, temp);
            }
            startLocalDateTime = startLocalDateTime.plusMonths(1);
            i++;
        }
        return list;
    }

    @Override
    public List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithDay(Date startDate, Date endDate) {
        //合伙人总数
        List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithDay(startDate, endDate);
        //合伙人派单数
        List<DataStatisticsCustomData> listData1 = dataStatisticsPartnerMapper.sendOrdersPartnerStatisticsWithDay(startDate, endDate);
        //合伙人拒单数
        List<DataStatisticsCustomData> listData2 = dataStatisticsPartnerMapper.singleOrdersPartnerStatisticsWithDay(startDate, endDate);

        //所有合伙人
        List<Partner> partners = this.partnerMapper.selectByExample(new PartnerExample());

        //每个合伙人的派单数
        List<DataStatisticsCustomData> sendList = dataStatisticsPartnerMapper.oneSendOrdersPartnerStatisticsWithDay(startDate, endDate);
        //每个合伙人的拒单数
        List<DataStatisticsCustomData> singleList = dataStatisticsPartnerMapper.oneSingleOrdersPartnerStatisticsWithDay(startDate, endDate);

        Map<String, Long> sendMap = sendList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> singleMap = singleList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchasePartnerStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchasePartnerStatisticsData pcsdObj=new PurchasePartnerStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            //封装左边数据
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setSendOrdersTotalNum(stepDate1);
            long stepDate2 = map2.get(item.getDateStr()) == null ? 0L : map2.get(item.getDateStr());
            pcsdObj.setSingleOrdersTotalNum(stepDate2);
            if (stepDate2 == 0L) {
                pcsdObj.setOrderRate("0");
            } else {
                String purchasePercent = String.valueOf(Math.round((stepDate1 * 1.00) / (stepDate1 +stepDate2) * 100)).replaceAll("\\.0+$", "");
                pcsdObj.setOrderRate(purchasePercent);
            }
            List<DataStatisticsSinglePartnerData> dpList=new ArrayList<>();
            //封装右边数据
            partners.stream().forEach(partner -> {
                String dpKey=item.getDateStr()+" "+partner.getPartner_id();
                DataStatisticsSinglePartnerData dp=new DataStatisticsSinglePartnerData();
                dp.setDateStr(item.getDateStr());
                dp.setId(partner.getPartner_id());
                dp.setName(partner.getName());
                //派单数
                if(sendMap.containsKey(dpKey)){
                    dp.setSendNum(sendMap.get(dpKey));
                }else{
                    dp.setSendNum(0L);
                }
                //拒单数
                if(singleMap.containsKey(dpKey)){
                    dp.setSingleNum(singleMap.get(dpKey));
                }else{
                    dp.setSingleNum(0L);
                }
                //接单率
                if (dp.getSendNum()+dp.getSingleNum() == 0L) {
                    dp.setOrderRate("0");
                } else {
                    String purchasePercent = String.valueOf(Math.round((dp.getSendNum() * 1.00) / (dp.getSendNum() + dp.getSingleNum()) * 100)).replaceAll("\\.0+$", "");
                    dp.setOrderRate(purchasePercent);
                }
                dpList.add(dp);
            });
            pcsdObj.setList(dpList);
            list.add(pcsdObj);
        }
        return list;
    }

    @Override
    public List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithWeek(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithWeek(startDate, endDate);
        //合伙人派单数
        List<DataStatisticsCustomData> listData1 = dataStatisticsPartnerMapper.sendOrdersPartnerStatisticsWithWeek(startDate, endDate);
        //合伙人拒单数
        List<DataStatisticsCustomData> listData2 = dataStatisticsPartnerMapper.singleOrdersPartnerStatisticsWithWeek(startDate, endDate);
        //所有合伙人
        List<Partner> partners = this.partnerMapper.selectByExample(new PartnerExample());
        //每个合伙人的派单数
        List<DataStatisticsCustomData> sendList = dataStatisticsPartnerMapper.oneSendOrdersPartnerStatisticsWithWeek(startDate, endDate);
        //每个合伙人的拒单数
        List<DataStatisticsCustomData> singleList = dataStatisticsPartnerMapper.oneSingleOrdersPartnerStatisticsWithWeek(startDate, endDate);

        Map<String, Long> sendMap = sendList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> singleMap = singleList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchasePartnerStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchasePartnerStatisticsData pcsdObj=new PurchasePartnerStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            //封装左边数据
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setSendOrdersTotalNum(stepDate1);
            long stepDate2 = map2.get(item.getDateStr()) == null ? 0L : map2.get(item.getDateStr());
            pcsdObj.setSingleOrdersTotalNum(stepDate2);
            if (stepDate2 == 0L) {
                pcsdObj.setOrderRate("0");
            } else {
                String purchasePercent = String.valueOf(Math.round((stepDate1 * 1.00) / (stepDate1 +stepDate2) * 100)).replaceAll("\\.0+$", "");
                pcsdObj.setOrderRate(purchasePercent);
            }
            List<DataStatisticsSinglePartnerData> dpList=new ArrayList<>();
            //封装右边数据
            partners.stream().forEach(partner -> {
                String dpKey=item.getDateStr()+" "+partner.getPartner_id();
                DataStatisticsSinglePartnerData dp=new DataStatisticsSinglePartnerData();
                dp.setDateStr(item.getDateStr());
                dp.setId(partner.getPartner_id());
                dp.setName(partner.getName());
                //派单数
                if(sendMap.containsKey(dpKey)){
                    dp.setSendNum(sendMap.get(dpKey));
                }else{
                    dp.setSendNum(0L);
                }
                //拒单数
                if(singleMap.containsKey(dpKey)){
                    dp.setSingleNum(singleMap.get(dpKey));
                }else{
                    dp.setSingleNum(0L);
                }
                //接单率
                if (dp.getSendNum()+dp.getSingleNum() == 0L) {
                    dp.setOrderRate("0");
                } else {
                    String purchasePercent = String.valueOf(Math.round((dp.getSendNum() * 1.00) / (dp.getSendNum() + dp.getSingleNum()) * 100)).replaceAll("\\.0+$", "");
                    dp.setOrderRate(purchasePercent);
                }
                dpList.add(dp);
            });
            pcsdObj.setList(dpList);
            list.add(pcsdObj);
        }
        return list;
    }

    @Override
    public List<PurchasePartnerStatisticsData> purchasePartnerStatisticsDataWithMonth(Date startDate, Date endDate) {
        List<DataStatisticsCustomData> listData0 = incrementingRegStatisticsWithMonth(startDate, endDate);

        //合伙人派单数
        List<DataStatisticsCustomData> listData1 = dataStatisticsPartnerMapper.sendOrdersPartnerStatisticsWithMonth(startDate, endDate);
        //合伙人拒单数
        List<DataStatisticsCustomData> listData2 = dataStatisticsPartnerMapper.singleOrdersPartnerStatisticsWithMonth(startDate, endDate);
        //所有合伙人
        List<Partner> partners = this.partnerMapper.selectByExample(new PartnerExample());
        //每个合伙人的派单数
        List<DataStatisticsCustomData> sendList = dataStatisticsPartnerMapper.oneSendOrdersPartnerStatisticsWithMonth(startDate, endDate);
        //每个合伙人的拒单数
        List<DataStatisticsCustomData> singleList = dataStatisticsPartnerMapper.oneSingleOrdersPartnerStatisticsWithMonth(startDate, endDate);

        Map<String, Long> sendMap = sendList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> singleMap = singleList.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        Map<String, Long> map1 = listData1.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));
        Map<String, Long> map2 = listData2.stream().collect(Collectors.toMap(DataStatisticsCustomData::getDateStr, DataStatisticsCustomData::getNum));

        List<PurchasePartnerStatisticsData> list = new ArrayList<>();
        for (DataStatisticsCustomData item : listData0) {
            PurchasePartnerStatisticsData pcsdObj=new PurchasePartnerStatisticsData();
            pcsdObj.setDateStr(item.getDateStr());
            pcsdObj.setTotalCustomNum(item.getNum());
            //封装左边数据
            long stepDate1 = map1.get(item.getDateStr()) == null ? 0L : map1.get(item.getDateStr());
            pcsdObj.setSendOrdersTotalNum(stepDate1);
            long stepDate2 = map2.get(item.getDateStr()) == null ? 0L : map2.get(item.getDateStr());
            pcsdObj.setSingleOrdersTotalNum(stepDate2);
            if (stepDate2 == 0L) {
                pcsdObj.setOrderRate("0");
            } else {
                String purchasePercent = String.valueOf(Math.round((stepDate1 * 1.00) / (stepDate1 +stepDate2) * 100)).replaceAll("\\.0+$", "");
                pcsdObj.setOrderRate(purchasePercent);
            }
            List<DataStatisticsSinglePartnerData> dpList=new ArrayList<>();
            //封装右边数据
            partners.stream().forEach(partner -> {
                String dpKey=item.getDateStr()+" "+partner.getPartner_id();
                DataStatisticsSinglePartnerData dp=new DataStatisticsSinglePartnerData();
                dp.setDateStr(item.getDateStr());
                dp.setId(partner.getPartner_id());
                dp.setName(partner.getName());
                //派单数
                if(sendMap.containsKey(dpKey)){
                    dp.setSendNum(sendMap.get(dpKey));
                }else{
                    dp.setSendNum(0L);
                }
                //拒单数
                if(singleMap.containsKey(dpKey)){
                    dp.setSingleNum(singleMap.get(dpKey));
                }else{
                    dp.setSingleNum(0L);
                }
                //接单率
                if (dp.getSendNum()+dp.getSingleNum() == 0L) {
                    dp.setOrderRate("0");
                } else {
                    String purchasePercent = String.valueOf(Math.round((dp.getSendNum() * 1.00) / (dp.getSendNum() + dp.getSingleNum()) * 100)).replaceAll("\\.0+$", "");
                    dp.setOrderRate(purchasePercent);
                }
                dpList.add(dp);
            });
            pcsdObj.setList(dpList);
            list.add(pcsdObj);
        }
        return list;
    }

}
