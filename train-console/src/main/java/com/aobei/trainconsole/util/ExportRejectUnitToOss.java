package com.aobei.trainconsole.util;

import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.DeductMoneyInfo;
import custom.bean.OrderInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mr_bl on 2018/7/2.
 */
@Component
public class ExportRejectUnitToOss implements Callable<Integer> {

    private Long data_download_id;

    private RejectRecordExample rejectRecordExample;

    public Long getData_download_id() {
        return data_download_id;
    }

    public void setData_download_id(Long data_download_id) {
        this.data_download_id = data_download_id;
    }

    public RejectRecordExample getRejectRecordExample() {
        return rejectRecordExample;
    }

    public void setRejectRecordExample(RejectRecordExample rejectRecordExample) {
        this.rejectRecordExample = rejectRecordExample;
    }

    @Autowired
    private RejectRecordService rejectRecordService;

    @Autowired
    private MyFileHandleUtil myFileHandleUtil;

    @Autowired
    private DataDownloadService dataDownloadService;

    @Autowired
    private OssFileService ossFileService;

    @Autowired
    private PartnerService partnerService;

    @Override
    public Integer call() throws Exception {
        List<Partner> partners = partnerService.selectByExample(new PartnerExample());
        Map<Long, Partner> partnerMap = partners.stream().collect(Collectors.toMap(Partner::getPartner_id, Function.identity()));
        List<RejectRecord> records = rejectRecordService.selectByExample(rejectRecordExample);

        String[] rowsName = new String[]{"订单号", "服务名称","用户姓名","顾客电话","服务地址",
                                    "服务价格","实付金额","拒单类型","拒单原因","合伙人公司名","预约时间"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<Object[]> dataList = records.stream().map(n ->{
            Integer reject_type = n.getReject_type();
            String reject = "";
            switch (reject_type){
                case 0:
                    reject = "被动拒单";
                    break;
                case 1:
                    reject = "主动拒单";
                    break;
                case 2:
                    reject = "订单改派拒单";
                    break;
                default:
                    reject = "";
                    break;
            }
            Object[] obj = {n.getPay_order_id(),n.getServer_name(),n.getCus_username(),n.getCus_phone(),n.getCus_address(),
                            n.getPrice_total()/100d+"",n.getPrice_pay()/100d+"",reject,
                            n.getReject_info(),partnerMap.get(n.getPartner_id()).getName(),n.getServer_datetime() == null ? "" : sdf.format(n.getServer_datetime())};
            return obj;
        }).collect(Collectors.toList());

        WriteExcel ex = new WriteExcel(rowsName, dataList);
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
