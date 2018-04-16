package cn.edu.bupt.actor.actors;

import akka.actor.UntypedActor;
import cn.edu.bupt.actor.service.ActorSystemContext;

/**
 * Created by Administrator on 2018/4/16.
 */
public abstract class ContextAwareActor extends UntypedActor {
    public static final int ENTITY_PACK_LIMIT = 1024;

    protected final ActorSystemContext systemContext;

    public ContextAwareActor(ActorSystemContext systemContext) {
        super();
        this.systemContext = systemContext;
    }
}
