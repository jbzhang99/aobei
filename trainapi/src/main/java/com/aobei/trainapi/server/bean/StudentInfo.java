package com.aobei.trainapi.server.bean;

import java.util.ArrayList;
import java.util.List;

import com.aobei.train.model.Serviceitem;
import com.aobei.train.model.Student;

public class StudentInfo extends Student{

	private List<Img> imgs = new ArrayList<>();
	
	private List<Serviceitem> serviceItems;
	
	private Img img;

	private List<StudentImgInfo> imgUrl;

	private String gradeDesc;

	public String getGradeDesc() {
		return gradeDesc;
	}

	public void setGradeDesc(String gradeDesc) {
		this.gradeDesc = gradeDesc;
	}

	public List<StudentImgInfo> getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(List<StudentImgInfo> imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Img getImg() {
		return img;
	}

	public void setImg(Img img) {
		this.img = img;
	}

	public List<Img> getImgs() {
		return imgs;
	}

	public void setImgs(List<Img> imgs) {
		this.imgs = imgs;
	}

	public List<Serviceitem> getServiceItems() {
		return serviceItems;
	}

	public void setServiceItems(List<Serviceitem> serviceItems) {
		this.serviceItems = serviceItems;
	}

	
	
	
}
