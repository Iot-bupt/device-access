package cn.edu.bupt.actor.actors.Session;

import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.Creator;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.SessionId;
import scala.concurrent.duration.Duration;

/**
 * Created by Administrator on 2018/4/16.
 */
public class SessionActor extends ContextAwareActor{
    private final SessionId sessionId;

    private SessionActor(ActorSystemContext context,SessionId sessionId){
        super(context);
        this.sessionId = sessionId;
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(-1, Duration.Inf(),
                throwable -> {
                    if (throwable instanceof Error) {
                        return OneForOneStrategy.escalate();
                    } else {
                        return OneForOneStrategy.resume();
                    }
                });
    }

    @Override
    public void onReceive(Object message) throws Exception {

    }

    public static class ActorCreator implements Creator<SessionActor> {
        private static final long serialVersionUID = 1L;
        private final SessionId sessionId;
        private final transient ActorSystemContext context;

        public ActorCreator(ActorSystemContext context, SessionId sessionId) {
            this.context = context;
            this.sessionId = sessionId;
        }

        @Override
        public SessionActor create() throws Exception {
            return new SessionActor(context, sessionId);
        }
    }
}
