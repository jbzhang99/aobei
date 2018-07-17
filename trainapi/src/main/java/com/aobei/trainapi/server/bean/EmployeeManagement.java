package com.aobei.trainapi.server.bean;



import java.util.List;

import com.aobei.train.model.OssImg;
import com.aobei.train.model.Partner;
import com.aobei.train.model.Serviceitem;
import com.aobei.train.model.Station;
import com.aobei.train.model.Student;

/**
 * 员工管理
 * @author 15010
 *
 */
public class EmployeeManagement {
	//学员
	private StudentInfo student;
	//服务项
	private List<Serviceitem> serviceitem;
	//服务站(虚拟地址)
	private Station station;
	//合伙人
	private Partner partner;
	//头像图片
	private OssImg ossImg;
	//学员是否可以修改服务站
	private Integer stationStatus;

	public Integer getStationStatus() {
		return stationStatus;
	}

	public void setStationStatus(Integer stationStatus) {
		this.stationStatus = stationStatus;
	}

	public OssImg getOssImage() {
		return ossImg;
	}
	public void setOssImage(OssImg ossImage) {
		this.ossImg = ossImage;
	}
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public StudentInfo getStudent() {
		return student;
	}

	public void setStudent(StudentInfo student) {
		this.student = student;
	}

	public List<Serviceitem> getServiceitem() {
		return serviceitem;
	}
	public void setServiceitem(List<Serviceitem> serviceitem) {
		this.serviceitem = serviceitem;
	}
	public Station getStation() {
		return station;
	}
	public void setStation(Station station) {
		this.station = station;
	}
	
	
	
}
