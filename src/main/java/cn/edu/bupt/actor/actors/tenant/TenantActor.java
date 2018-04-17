package cn.edu.bupt.actor.actors.tenant;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.actors.app.AppActor;
import cn.edu.bupt.actor.actors.device.DeviceActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.actor.service.DefaultActorService;
import cn.edu.bupt.message.BasicToDeviceActorMsg;
import cn.edu.bupt.message.BasicToDeviceActorSessionMsg;
import cn.edu.bupt.message.FromSessionActorToDeviceActorMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17.
 */
public class TenantActor extends ContextAwareActor {

    private final Map<String, ActorRef> deviceActors;
    private final String tenantId;

    public TenantActor(ActorSystemContext systemContext,String tenantId){
        super(systemContext);
        this.deviceActors = new HashMap<>();
        this.tenantId = tenantId;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof BasicToDeviceActorMsg){
            getOrCreateDeviceActor(((BasicToDeviceActorMsg) msg).getDeviceId()).tell(msg,ActorRef.noSender());
        }else if(msg instanceof BasicToDeviceActorSessionMsg){
            //TODO 待完成
            System.err.println("tenant actor receive a sessionControl msg");
        }
    }

    private ActorRef getOrCreateDeviceActor(String deviceId) {
        return deviceActors.computeIfAbsent(deviceId, k -> context().actorOf(Props.create(new DeviceActor.ActorCreator(systemContext, tenantId, deviceId))
                .withDispatcher(DefaultActorService.CORE_DISPATCHER_NAME), deviceId.toString()));
    }

    public static class ActorCreator implements Creator<TenantActor> {
        private static final long serialVersionUID = 1L;
        private final transient ActorSystemContext context;
        private final String tenantId;

        public ActorCreator(ActorSystemContext context,String tenantId) {
            this.context = context;
            this.tenantId = tenantId;
        }

        @Override
        public TenantActor create() throws Exception {
            return new TenantActor(context,tenantId);
        }
    }
}
