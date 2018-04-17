package cn.edu.bupt.actor.actors.Session;

import akka.actor.ActorContext;
import cn.edu.bupt.message.FromSessionActorToDeviceActorMsg;
import cn.edu.bupt.message.SessionCtrlMsg;

/**
 * Created by Administrator on 2018/4/17.
 */
public interface SessionActorProcessor {
    void processSessionCtrlMsg(ActorContext context, SessionCtrlMsg msg);

    void processToDeviceActorMsg(ActorContext context, FromSessionActorToDeviceActorMsg msg);
}
