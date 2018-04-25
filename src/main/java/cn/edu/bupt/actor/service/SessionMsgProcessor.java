package cn.edu.bupt.actor.service;

import cn.edu.bupt.message.SessionAwareMsg;
import cn.edu.bupt.message.SessionCloseMsg;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface SessionMsgProcessor {
    void process(SessionAwareMsg sessionAwareMsg);
}
