package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class OssVideo implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oss_video.oss_video_id
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private Long oss_video_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oss_video.name
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oss_video.effect
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private String effect;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oss_video.url
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private String url;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oss_video.format
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private String format;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oss_video.bucket
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private String bucket;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oss_video.access_permissions
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private String access_permissions;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oss_video.create_time
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private Date create_time;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table oss_video
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table oss_video
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oss_video.oss_video_id
     *
     * @return the value of oss_video.oss_video_id
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public Long getOss_video_id() {
        return oss_video_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oss_video.oss_video_id
     *
     * @param oss_video_id the value for oss_video.oss_video_id
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void setOss_video_id(Long oss_video_id) {
        this.oss_video_id = oss_video_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oss_video.name
     *
     * @return the value of oss_video.name
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oss_video.name
     *
     * @param name the value for oss_video.name
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oss_video.effect
     *
     * @return the value of oss_video.effect
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public String getEffect() {
        return effect;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oss_video.effect
     *
     * @param effect the value for oss_video.effect
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void setEffect(String effect) {
        this.effect = effect == null ? null : effect.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oss_video.url
     *
     * @return the value of oss_video.url
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oss_video.url
     *
     * @param url the value for oss_video.url
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oss_video.format
     *
     * @return the value of oss_video.format
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public String getFormat() {
        return format;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oss_video.format
     *
     * @param format the value for oss_video.format
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oss_video.bucket
     *
     * @return the value of oss_video.bucket
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oss_video.bucket
     *
     * @param bucket the value for oss_video.bucket
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void setBucket(String bucket) {
        this.bucket = bucket == null ? null : bucket.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oss_video.access_permissions
     *
     * @return the value of oss_video.access_permissions
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public String getAccess_permissions() {
        return access_permissions;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oss_video.access_permissions
     *
     * @param access_permissions the value for oss_video.access_permissions
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void setAccess_permissions(String access_permissions) {
        this.access_permissions = access_permissions == null ? null : access_permissions.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oss_video.create_time
     *
     * @return the value of oss_video.create_time
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oss_video.create_time
     *
     * @param create_time the value for oss_video.create_time
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oss_video
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oss_video
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oss_video
     *
     * @mbg.generated Mon Apr 02 18:25:32 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", oss_video_id=").append(oss_video_id);
        sb.append(", name=").append(name);
        sb.append(", effect=").append(effect);
        sb.append(", url=").append(url);
        sb.append(", format=").append(format);
        sb.append(", bucket=").append(bucket);
        sb.append(", access_permissions=").append(access_permissions);
        sb.append(", create_time=").append(create_time);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}