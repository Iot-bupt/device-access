package cn.edu.bupt.actor.actors.Session;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.DeviceAwareSessionContext;
import cn.edu.bupt.common.SessionContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.message.*;
import org.omg.PortableInterceptor.ACTIVE;

/**
 * Created by Administrator on 2018/4/17.
 */
public class MqttSessionActorProcessor implements  SessionActorProcessor{

    protected final ActorSystemContext systemContext;
    private boolean firstMsg = true;
    protected final SessionId sessionId;
    protected DeviceAwareSessionContext sessionCtx;

    public MqttSessionActorProcessor(ActorSystemContext systemContext,SessionId sessionId,DeviceAwareSessionContext sessionCtx){
        this.systemContext = systemContext;
        this.sessionId = sessionId;
        this.sessionCtx = sessionCtx;
    }

    @Override
    public void processSessionCtrlMsg(ActorContext context, SessionCtrlMsg msg) {
        if(msg instanceof SessionCloseMsg){
            cleanupSession(msg,context);
            context.parent().tell(new SessionTerminationMsg(msg.getSessionId()), ActorRef.noSender());
            context.stop(context.self());
        }
    }

    @Override
    public void processToDeviceActorMsg(ActorContext context, FromSessionActorToDeviceActorMsg msg) {
        //TODO appactor 未完成
        systemContext.getAppActor().tell(msg, ActorRef.noSender());
    }

    private void cleanupSession(SessionCtrlMsg msg, ActorContext context) {
        //TODO appactor 未完成
        systemContext.getAppActor().tell(new BasicToDeviceActorSessionMsg(msg,sessionCtx.getDevice()),ActorRef.noSender());
    }
}
