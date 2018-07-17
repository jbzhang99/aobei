package com.aobei.trainconsole.configuration.onlinedtask;

import com.aobei.train.model.*;
import com.aobei.train.service.CouponEnvService;
import com.aobei.train.service.CouponReceiveService;
import com.aobei.train.service.CouponService;
import com.aobei.train.service.StationService;
import custom.bean.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

@Configuration
public class OnlinedTask {
	
	@Autowired
	private StationService stationService;

	@Autowired
	private CouponEnvService couponEnvService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponReceiveService receiveService;

	/**
	 * 虚拟地址在线状态调度
	 */
	@Scheduled(initialDelay = 2000, fixedDelay = 24*60*60*100)
	private void setOnlined(){
		StationExample stationExample = new StationExample();
		stationExample.or()
			.andOnlinedEqualTo(1)
			.andOffline_datetimeLessThanOrEqualTo(new Date());
		List<Station> list_station = stationService.selectByExample(stationExample);
		list_station.stream().filter(t ->t.getOffline_datetime()!=null).forEach(n ->{
			Station station = new Station();
			station.setStation_id(n.getStation_id());
			station.setOnlined(0);//下线
			stationService.updateByPrimaryKeySelective(station);
		});
	}
	@Scheduled(initialDelay = 2000, fixedDelay = 24*60*60*100)
	private void setCou_status(){
		CouponEnvExample couponEnvExample = new CouponEnvExample();
		couponEnvExample.or()
				.andStatusNotEqualTo(3)
				.andEnd_datetimeLessThanOrEqualTo(new Date());
		List<CouponEnv> couponEnvs = couponEnvService.selectByExample(couponEnvExample);
		couponEnvs.stream().filter(t ->t.getEnd_datetime()!=null).forEach( n ->{
			CouponEnv couponEnv = new CouponEnv();
			couponEnv.setCoupon_env_id(n.getCoupon_env_id());
			couponEnv.setStatus(3);
			couponEnvService.updateByPrimaryKeySelective(couponEnv);
		});
	}

//	@Scheduled(cron = "0 0 10 * * ?")
	@Scheduled(initialDelay = 2000, fixedDelay = 24*60*60*100)
	private void Coupon_valid(){
		CouponExample example = new CouponExample();
		example.or()
				.andValidNotEqualTo(2)
				.andUse_end_datetimeLessThanOrEqualTo(new Date());
		List<Coupon> coupons = couponService.selectByExample(example);
		coupons.stream().filter( t->t.getUse_end_datetime()!=null).forEach( n->{
			Coupon coupon = new Coupon();
			coupon.setCoupon_id(n.getCoupon_id());
			coupon.setValid(2);
			couponService.updateByPrimaryKeySelective(coupon);
			CouponReceiveExample receiveExample = new CouponReceiveExample();
			receiveExample.or()
					.andCoupon_idEqualTo(n.getCoupon_id())
					.andDeletedEqualTo(Status.DeleteStatus.no.value)
					.andVerificationEqualTo(0)
					.andStatusNotEqualTo(5)
					.andReceive_datetimeLessThanOrEqualTo(new Date());
			List<CouponReceive> couponReceives = receiveService.selectByExample(receiveExample);
			couponReceives.stream().filter(m -> m.getReceive_datetime()!=null).forEach( z ->{
				if(z.getStatus()!=4){//用户已使用的不修改
					CouponReceive couponReceive = new CouponReceive();
					couponReceive.setCoupon_receive_id(z.getCoupon_receive_id());
					couponReceive.setStatus(5);
					receiveService.updateByPrimaryKeySelective(couponReceive);
				}
			});
		});
	}

}



