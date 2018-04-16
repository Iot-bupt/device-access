package cn.edu.bupt.actor.actors.Session;

import akka.actor.ActorRef;
import akka.japi.Creator;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.message.FormSessionToDeviceActorMsg;
import cn.edu.bupt.message.SessionAwareMsg;
import cn.edu.bupt.message.SessionCtrlMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/16.
 */
public class SessionManagerActor extends ContextAwareActor {

    private static final int INITIAL_SESSION_MAP_SIZE = 1024;
    private final Map<String, ActorRef> sessionActors;

    public SessionManagerActor(ActorSystemContext context){
        super(context);
        sessionActors = new HashMap<>(INITIAL_SESSION_MAP_SIZE);
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof SessionCtrlMsg){
            onSessionCtrlMsg((SessionCtrlMsg)msg);
        }else if(msg instanceof SessionAwareMsg){
            forwardToSessionActor((SessionAwareMsg) msg);
        }
    }

    private void onSessionCtrlMsg(SessionCtrlMsg msg) {
        String sessionIdstr = msg.getSessionId().toUidStr();
        ActorRef sessionActor = sessionActors.get(sessionIdstr);
        if(sessionActor!=null){
            sessionActor.tell(msg,ActorRef.noSender());
        }
    }

    private void forwardToSessionActor(SessionAwareMsg msg) {
        if (msg instanceof FormSessionToDeviceActorMsg) {

        }else{

        }
    }

    public static class ActorCreator implements Creator<SessionManagerActor>{
        private static final long serialVersionUID = 1L;
        private final ActorSystemContext context;

        public ActorCreator(ActorSystemContext context) {
            this.context = context;
        }
        @Override
        public SessionManagerActor create() throws Exception {
            return new SessionManagerActor(context);
        }
    }

}
