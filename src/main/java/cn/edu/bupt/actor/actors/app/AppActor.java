package cn.edu.bupt.actor.actors.app;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.Creator;
import akka.japi.Function;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.actors.Session.SessionActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.message.FromSessionActorToDeviceActorMsg;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17.
 */
public class AppActor extends ContextAwareActor{

    private final Map<String, ActorRef> tenantActors;

    private AppActor(ActorSystemContext systemContext) {
        super(systemContext);
        this.tenantActors = new HashMap<>();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof FromSessionActorToDeviceActorMsg){
            process((FromSessionActorToDeviceActorMsg)msg);
        }
    }

    private void process(FromSessionActorToDeviceActorMsg msg) {

    }

    private final SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
        @Override
        public SupervisorStrategy.Directive apply(Throwable t) {
            if (t instanceof RuntimeException) {
                return SupervisorStrategy.restart();
            } else {
                return SupervisorStrategy.stop();
            }
        }
    });

    public static class ActorCreator implements Creator<AppActor> {
        private static final long serialVersionUID = 1L;
        private final transient ActorSystemContext context;

        public ActorCreator(ActorSystemContext context) {
            this.context = context;
        }

        @Override
        public AppActor create() throws Exception {
            return new AppActor(context);
        }
    }
}
