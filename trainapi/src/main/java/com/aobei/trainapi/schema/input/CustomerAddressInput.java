package com.aobei.trainapi.schema.input;

import com.aobei.train.model.CustomerAddress;

public class CustomerAddressInput {
    Long customer_address_id;
    String username;
    String phone;
    String province;
    String city;
    String district;
    String address;
    String lbs_lat;
    String lbs_lng;
    String sub_address;
    int default_address;

    public Long getCustomer_address_id() {
        return customer_address_id;
    }

    public void setCustomer_address_id(Long customer_address_id) {
        this.customer_address_id = customer_address_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String area) {
        this.district = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLbs_lat() {
        return lbs_lat;
    }

    public void setLbs_lat(String lbs_lat) {
        this.lbs_lat = lbs_lat;
    }

    public String getLbs_lng() {
        return lbs_lng;
    }

    public void setLbs_lng(String lbs_lng) {
        this.lbs_lng = lbs_lng;
    }

    public String getSub_address() {
        return sub_address;
    }

    public void setSub_address(String sub_address) {
        this.sub_address = sub_address;
    }

    public int getDefault_address() {
        return default_address;
    }

    public void setDefault_address(int default_address) {
        this.default_address = default_address;
    }
}
