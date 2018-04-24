package cn.edu.bupt.controller;

import cn.edu.bupt.actor.service.DefaultActorService;
import cn.edu.bupt.service.DeviceService;
import cn.edu.bupt.service.GroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class BaseController {

    @Autowired
    protected DeviceService deviceService;

    @Autowired
    protected GroupService groupService;

    @Autowired
    DefaultActorService actorService;

    UUID toUUID(String id) {
        return UUID.fromString(id);
    }


    <T> T checkNotNull(T reference) throws Exception{
        if (reference == null) {
            throw new Exception("Requested item wasn't found!");
        }
        return reference;
    }

    void checkParameter(String name, String param) throws Exception {
        if (StringUtils.isEmpty(param)) {
            throw new Exception("Parameter '" + name + "' can't be empty!");
        }
    }

}
