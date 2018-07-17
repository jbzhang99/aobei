package com.aobei.common.boot.event;

import com.aobei.common.bean.IGtPushData;

public class IGtPushEvent extends AbstractEvent<IGtPushData> {

    private static final long serialVersionUID = -5980229090157677802L;

    public IGtPushEvent(Object source, IGtPushData data) {
        super(source, data);
    }
}
