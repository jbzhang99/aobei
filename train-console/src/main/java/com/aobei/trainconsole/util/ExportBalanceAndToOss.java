package com.aobei.trainconsole.util;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by adminL on 2018/5/22.
 */
@Component
public class ExportBalanceAndToOss implements Callable<Integer>{


    @Autowired
    private MyFileHandleUtil myFileHandleUtil;

    @Autowired
    private DataDownloadService dataDownloadService;

    @Autowired
    private OssFileService ossFileService;

    @Autowired
    private BalanceOrderService balanceOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FallintoService fallintoService;

    private Long data_download_id;

    private int type;

    private BalanceOrderExample balanceOrderExample;

    public Long getData_download_id() {
        return data_download_id;
    }


    public void setData_download_id(Long data_download_id) {
        this.data_download_id = data_download_id;
    }


    public void setBalanceOrderExample(BalanceOrderExample balanceOrderExample) {
        this.balanceOrderExample = balanceOrderExample;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public Integer call() throws Exception {

        String titleName[] ={type==1?"结算期":"","订单号","服务单号","类型","商品",
                "订单生成时间","服务人员完成时间","订单金额","实付金额",
                "优惠券","礼券","积分","促销优惠金额",
                "合伙人","合伙人ID","合伙人级别","结算策略名称","结算策略类型","合作起日期","合作止日期","结算策略开始时间","结算策略结束时间",
        "订单待结算金额(合伙人)","平台服务费","状态"};

        List<BalanceOrder> list =
                balanceOrderService.selectByExample(balanceOrderExample);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        List<Object[]> dataList = list.stream().map( n->{
            FallintoExample fallintoExample = new FallintoExample();
            List<Fallinto> fallintoList = fallintoService.selectByExample(fallintoExample);
            List<String> balanceType=new ArrayList<>();
            if(!fallintoList.isEmpty()){
                fallintoList.stream().forEach(fallinto -> {
                    if(n.getFallinto_id().equals(fallinto.getFallinto_id())){
                        switch (fallinto.getFallinto_type()){
                            case 1:
                                balanceType.add("底价");
                                break;
                            case 2:
                                balanceType.add("比例");
                                break;
                            case 3:
                                balanceType.add("单数阶梯");
                                break;
                            case 4:
                                balanceType.add("金额阶梯");
                                break;
                            case 5:
                                balanceType.add("客单价阶梯");
                                break;
                        }
                    }
                });
            }
            Product product = this.productService.selectByPrimaryKey(n.getProduct_id());
            Object[] obj = {n.getStatus()==1?n.getBalance_cycle():"",
                    n.getPay_order_id()==null?"":n.getPay_order_id().toString(),
                    n.getServiceunit_id()==null?"":n.getServiceunit_id().toString(),
                    n.getBalance_type()==1?"完成":"",
                    product.getName(),
                    n.getOrder_create_datetime()==null?"":sd.format(n.getOrder_create_datetime()),
                    n.getWork_finish_datetime()==null?"":sd.format(n.getWork_finish_datetime()),
                    n.getPrice_total()==null?"":(n.getPrice_total()*0.01),
                    n.getPrice_pay()==null?"":n.getPrice_pay()*0.01,
                    n.getPrice_discount()==null?"":n.getPrice_discount()*0.01,
                    "",
                    "",
                    "",
                    n.getPartner_name()==null?"":n.getPartner_name().toString(),
                    n.getPartner_id()==null?"":n.getPartner_id().toString(),
                    n.getPartner_level()==null?"":n.getPartner_level(),
                    n.getFallinto_name()==null?"":n.getFallinto_name(),
                    balanceType.isEmpty()?"":balanceType.get(0),
                    n.getCooperation_start()==null?"":sd.format(n.getCooperation_start()),//合伙人合作开始时间
                    n.getCooperation_end()==null?"":sd.format(n.getCooperation_end()),
                    n.getCreate_datetime()==null?"":sd.format(n.getCreate_datetime()),
                    n.getBalance_datetime()==null?"":sd.format(n.getBalance_datetime()),
                    n.getPartner_balance_fee()==null?"":n.getPartner_balance_fee()*0.01,
                    n.getBalance_fee()==null?"":n.getBalance_fee()*0.01,
                    n.getStatus()==1?"待结算":"挂起"
                    };//合伙人合作结束时间
            return obj;
        }).collect(Collectors.toList());


        WriteExcel ex = new WriteExcel(titleName, dataList);
        Map<String, String> file_map = myFileHandleUtil.file_upload(ex.export(), PathUtil.PathType.file_order_excel);
        OssFile ossFile = new OssFile();
        ossFile.setOss_file_id(IdGenerator.generateId());
        ossFile.setName(file_map.get("file_name"));
        ossFile.setUrl(file_map.get("file_url"));
        ossFile.setFormat(file_map.get("file_format"));
        ossFile.setCreate_time(new Date());
        ossFile.setBucket(file_map.get("bucketName"));
        ossFile.setAccess_permissions("private");
        ossFileService.insertSelective(ossFile);
//		Thread.sleep(30000);
        DataDownload dataDownload = new DataDownload();
        dataDownload.setData_download_id(data_download_id);
        dataDownload.setOss_file(file_map.get("file_url"));
        dataDownload.setStatus(1);
        int i = dataDownloadService.updateByPrimaryKeySelective(dataDownload);
        return i;
    }

}
