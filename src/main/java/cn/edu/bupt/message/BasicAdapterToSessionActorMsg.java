package cn.edu.bupt.message;

import cn.edu.bupt.common.DeviceAwareSessionContext;
import cn.edu.bupt.common.SessionId;

/**
 * Created by Administrator on 2018/4/14.
 */
public class BasicAdapterToSessionActorMsg implements AdaptorToSessionActorMsg{
    private final  FromDeviceMsg msg;
    private final DeviceAwareSessionContext context;
    public BasicAdapterToSessionActorMsg(DeviceAwareSessionContext context,FromDeviceMsg msg){
        this.msg = msg;
        this.context = context;
    }
    @Override
    public FromDeviceMsg getMsg() {
        return msg;
    }

    @Override
    public SessionId getSessionId() {
        return context.getSessionId();
    }
}
