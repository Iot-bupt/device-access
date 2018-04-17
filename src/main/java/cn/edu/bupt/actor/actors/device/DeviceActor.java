package cn.edu.bupt.actor.actors.device;

import akka.japi.Creator;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.actors.Session.SessionActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.message.FromSessionActorToDeviceActorMsg;

/**
 * Created by Administrator on 2018/4/17.
 */
public class DeviceActor extends ContextAwareActor{

    private final String tenantId;
    private final String deviceId;
    private final DeviceActorMsgProcessor processor;

    private DeviceActor(ActorSystemContext systemContext,String tenantId,String deviceId){
        super(systemContext);
        this.tenantId = tenantId;
        this.deviceId = deviceId;
        // TODO 待修改
        this.processor  = new DeviceActorMsgProcessor();
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof FromSessionActorToDeviceActorMsg){
            processor.process((FromSessionActorToDeviceActorMsg)msg);
        }
    }

    public static class ActorCreator implements Creator<DeviceActor> {
        private static final long serialVersionUID = 1L;
        private final String tenantId;
        private final String deviceId;
        private final transient ActorSystemContext context;

        public ActorCreator(ActorSystemContext context, String  tenantId,String deviceId) {
            this.context = context;
            this.tenantId = tenantId;
            this.deviceId = deviceId;
        }

        @Override
        public DeviceActor create() throws Exception {
            return new DeviceActor(context,tenantId,deviceId);
        }
    }
}
