package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class CmsBanner implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.cms_banner_id
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private Long cms_banner_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.img_cover
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private String img_cover;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.title
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.intro
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private String intro;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.app
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private String app;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.online_datetime
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private Date online_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.offline_datetime
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private Date offline_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.serial_number
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private Integer serial_number;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.sign
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private Integer sign;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.deleted
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private Integer deleted;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.position
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private String position;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.href
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private String href;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_banner.type
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cms_banner
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cms_banner
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.cms_banner_id
     *
     * @return the value of cms_banner.cms_banner_id
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public Long getCms_banner_id() {
        return cms_banner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.cms_banner_id
     *
     * @param cms_banner_id the value for cms_banner.cms_banner_id
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setCms_banner_id(Long cms_banner_id) {
        this.cms_banner_id = cms_banner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.img_cover
     *
     * @return the value of cms_banner.img_cover
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public String getImg_cover() {
        return img_cover;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.img_cover
     *
     * @param img_cover the value for cms_banner.img_cover
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setImg_cover(String img_cover) {
        this.img_cover = img_cover == null ? null : img_cover.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.title
     *
     * @return the value of cms_banner.title
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.title
     *
     * @param title the value for cms_banner.title
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.intro
     *
     * @return the value of cms_banner.intro
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public String getIntro() {
        return intro;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.intro
     *
     * @param intro the value for cms_banner.intro
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.app
     *
     * @return the value of cms_banner.app
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public String getApp() {
        return app;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.app
     *
     * @param app the value for cms_banner.app
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setApp(String app) {
        this.app = app == null ? null : app.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.online_datetime
     *
     * @return the value of cms_banner.online_datetime
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public Date getOnline_datetime() {
        return online_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.online_datetime
     *
     * @param online_datetime the value for cms_banner.online_datetime
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setOnline_datetime(Date online_datetime) {
        this.online_datetime = online_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.offline_datetime
     *
     * @return the value of cms_banner.offline_datetime
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public Date getOffline_datetime() {
        return offline_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.offline_datetime
     *
     * @param offline_datetime the value for cms_banner.offline_datetime
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setOffline_datetime(Date offline_datetime) {
        this.offline_datetime = offline_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.serial_number
     *
     * @return the value of cms_banner.serial_number
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public Integer getSerial_number() {
        return serial_number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.serial_number
     *
     * @param serial_number the value for cms_banner.serial_number
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setSerial_number(Integer serial_number) {
        this.serial_number = serial_number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.sign
     *
     * @return the value of cms_banner.sign
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public Integer getSign() {
        return sign;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.sign
     *
     * @param sign the value for cms_banner.sign
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setSign(Integer sign) {
        this.sign = sign;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.deleted
     *
     * @return the value of cms_banner.deleted
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.deleted
     *
     * @param deleted the value for cms_banner.deleted
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.position
     *
     * @return the value of cms_banner.position
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public String getPosition() {
        return position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.position
     *
     * @param position the value for cms_banner.position
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.href
     *
     * @return the value of cms_banner.href
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public String getHref() {
        return href;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.href
     *
     * @param href the value for cms_banner.href
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setHref(String href) {
        this.href = href == null ? null : href.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_banner.type
     *
     * @return the value of cms_banner.type
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_banner.type
     *
     * @param type the value for cms_banner.type
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_banner
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_banner
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_banner
     *
     * @mbg.generated Tue May 22 16:54:43 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", cms_banner_id=").append(cms_banner_id);
        sb.append(", img_cover=").append(img_cover);
        sb.append(", title=").append(title);
        sb.append(", intro=").append(intro);
        sb.append(", app=").append(app);
        sb.append(", online_datetime=").append(online_datetime);
        sb.append(", offline_datetime=").append(offline_datetime);
        sb.append(", serial_number=").append(serial_number);
        sb.append(", sign=").append(sign);
        sb.append(", deleted=").append(deleted);
        sb.append(", position=").append(position);
        sb.append(", href=").append(href);
        sb.append(", type=").append(type);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}