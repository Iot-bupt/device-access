package cn.edu.bupt.controller;

import cn.edu.bupt.actor.service.DefaultActorService;
import cn.edu.bupt.service.*;
import com.google.gson.JsonObject;
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

    @Autowired
    BaseAttributesService baseAttributesService;

    @Autowired
    BaseTimeseriesService baseTimeseriesService;

    @Autowired
    DeviceCredentialsService deviceCredentialsService;

    @Autowired
    BaseEventService baseEventService;


    UUID toUUID(String id) {
        if(id==null) {
            return null;
        }else {
            return UUID.fromString(id);
        }
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

    public String onFail(Exception exception) {
        return this.onFail(exception.toString()) ;
    }

    public String onFail(String msg) {
        JsonObject InfoJson = new JsonObject() ;
        InfoJson.addProperty("id", "");
        InfoJson.addProperty("response_code", 1);
        //InfoJson.addProperty("response_msg", msg);
        InfoJson.addProperty("info", "名称重复，创建失败");
        return InfoJson.toString() ;
    }

}
