package com.aobei.train.model;

import java.io.Serializable;
import java.util.Date;

public class ChannelType implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_type.channel_type_id
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    private Integer channel_type_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_type.channel_type_name
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    private String channel_type_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_type.create_datetime
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    private Date create_datetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_type
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_type.channel_type_id
     *
     * @return the value of channel_type.channel_type_id
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public Integer getChannel_type_id() {
        return channel_type_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_type.channel_type_id
     *
     * @param channel_type_id the value for channel_type.channel_type_id
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public void setChannel_type_id(Integer channel_type_id) {
        this.channel_type_id = channel_type_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_type.channel_type_name
     *
     * @return the value of channel_type.channel_type_name
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public String getChannel_type_name() {
        return channel_type_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_type.channel_type_name
     *
     * @param channel_type_name the value for channel_type.channel_type_name
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public void setChannel_type_name(String channel_type_name) {
        this.channel_type_name = channel_type_name == null ? null : channel_type_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_type.create_datetime
     *
     * @return the value of channel_type.create_datetime
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public Date getCreate_datetime() {
        return create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_type.create_datetime
     *
     * @param create_datetime the value for channel_type.create_datetime
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_type
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", channel_type_id=").append(channel_type_id);
        sb.append(", channel_type_name=").append(channel_type_name);
        sb.append(", create_datetime=").append(create_datetime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}