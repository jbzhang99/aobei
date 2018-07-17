package custom.bean;

import com.aobei.train.model.Channel;
import com.aobei.train.model.ChannelType;

/**
 * Created by adminL on 2018/6/19.
 */
public class ChannelAndType{

    Integer channel_type_id;

    String channel_type_name;

    String channel_name;

    String code;

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    @Override
    public String toString() {
        return "ChannelAndType{" +
                "channel_type_id=" + channel_type_id +
                ", channel_type_name='" + channel_type_name + '\'' +
                ", channel_name='" + channel_name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getChannel_type_id() {
        return channel_type_id;
    }

    public void setChannel_type_id(Integer channel_type_id) {
        this.channel_type_id = channel_type_id;
    }

    public String getChannel_type_name() {
        return channel_type_name;
    }

    public void setChannel_type_name(String channel_type_name) {
        this.channel_type_name = channel_type_name;
    }
}
