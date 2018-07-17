package com.aobei.trainconsole.util;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by adminL on 2018/5/22.
 */
@Component
public class ExportRefundBalanceAndToOss implements Callable<Integer>{


    @Autowired
    private MyFileHandleUtil myFileHandleUtil;

    @Autowired
    private DataDownloadService dataDownloadService;

    @Autowired
    private OssFileService ossFileService;


    @Autowired
    private ProductService productService;

    @Autowired
    private RefundService refundService;

    private Long data_download_id;

    private int type;

    private FallintoRefundExample fallintoRefundExample;

    public Long getData_download_id() {
        return data_download_id;
    }


    public void setData_download_id(Long data_download_id) {
        this.data_download_id = data_download_id;
    }

    public FallintoRefundExample getFallintoRefundExample() {
        return fallintoRefundExample;
    }

    public void setFallintoRefundExample(FallintoRefundExample fallintoRefundExample) {
        this.fallintoRefundExample = fallintoRefundExample;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Autowired
    private FallintoRefundService fallintoRefundService;

    @Override
    public Integer call() throws Exception {

        String titleName[] ={type==1?"结算期":"","订单号","服务单号","退款单号","类型",
                "订单生成时间","退款完成时间","订单金额","退款金额","实付金额","实收金额",
                "合伙人","合伙人ID","合伙人级别","合作起日期","合作止日期","状态"};


        List<FallintoRefund> list = fallintoRefundService.selectByExample(fallintoRefundExample);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        List<Object[]> dataList = list.stream().map( n->{
            //Product product = this.productService.selectByPrimaryKey(n.getProduct_id());
            Refund refund = refundService.selectByPrimaryKey(n.getRefund_id());
            Object[] obj = {n.getStatus()==1?n.getBalance_cycle():"",
                    n.getPay_order_id()==null?"":n.getPay_order_id().toString(),
                    n.getServiceunit_id()==null?"":n.getServiceunit_id().toString(),
                    n.getRefund_id()==null?"":n.getRefund_id().toString(),
                    "退款",
                    n.getOrder_create_datetime()==null?"":sd.format(n.getOrder_create_datetime()),
                    refund.getRefund_date()==null?"":sd.format( refund.getRefund_date()),
                    n.getPrice_total()==null?"":(n.getPrice_total()*0.01),
                    refund.getFee()==null?"":refund.getFee()*0.01,
                    n.getPrice_pay()==null?"":n.getPrice_pay()*0.01,
                    n.getPrice_compute()==null?"":n.getPrice_compute()*0.01,
                    n.getPartner_name()==null?"":n.getPartner_name().toString(),
                    n.getPartner_id()==null?"":n.getPartner_id().toString(),
                    n.getPartner_level()==null?"":n.getPartner_level(),
                    n.getCooperation_start()==null?"":sd.format(n.getCooperation_start()),//合伙人合作开始时间
                    n.getCooperation_end()==null?"":sd.format(n.getCooperation_end()),
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
