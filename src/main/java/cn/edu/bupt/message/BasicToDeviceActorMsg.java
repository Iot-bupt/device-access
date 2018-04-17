package cn.edu.bupt.message;

import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.pojo.Device;

/**
 * Created by Administrator on 2018/4/17.
 */
public class BasicToDeviceActorMsg implements FromSessionActorToDeviceActorMsg{

    AdaptorToSessionActorMsg msg;
    Device device;
    public BasicToDeviceActorMsg(AdaptorToSessionActorMsg msg,Device device){
        this.msg = msg;
        this.device = device;
    }
    public AdaptorToSessionActorMsg getMsg(){
        return msg;
    }

    @Override
    public String getDeviceId() {

        return device.getDeviceId();
    }
    @Override
    public String getTenantId() {
        return device.getTenantId();
    }
    @Override
    public SessionId getSessionId() {
        return msg.getSessionId();
    }
}
