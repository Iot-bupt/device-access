package cn.edu.bupt.actor.actors.device;

import cn.edu.bupt.message.*;

/**
 * Created by Administrator on 2018/4/17.
 */
public class DeviceActorMsgProcessor {
    public void process(FromSessionActorToDeviceActorMsg msg) {
        BasicToDeviceActorMsg msg1 = (BasicToDeviceActorMsg)msg;
        AdaptorToSessionActorMsg adptorMsg = msg1.getMsg();
        FromDeviceMsg fromDeviceMsg = adptorMsg.getMsg();
        switch(fromDeviceMsg.getMsgType()){
            case MsgType.POST_TELEMETRY_REQUEST:
                System.err.println("receive a telemetry msg");
                break;
            case MsgType.POST_ATTRIBUTE_REQUEST:
                System.err.println("receive a attribute msg");
                break;
            case MsgType.FROM_DEVICE_RPC_SUB:
                System.err.println("receive a rpc sub");
                break;
            case MsgType.FROM_DEVICE_RPC_UNSUB:
                System.err.println("receive a rpc un sub");
                break;
            case MsgType.FROM_DEVICE_RPC_RESPONCE:
                System.err.println("receive a rpc  responce");
                break;
            default:{
                System.err.println("unKnow msg type");
                }
        }
    }
}
