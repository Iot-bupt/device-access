package cn.edu.bupt.controller;

import cn.edu.bupt.actor.service.RpcMsgProcessor;
import cn.edu.bupt.message.BasicFromServerRpcMsg;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.service.DeviceService;
import cn.edu.bupt.utils.HttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

/**
 * Created by Administrator on 2018/4/24.
 */
@RestController
@RequestMapping("/api/v1/rpc")
public class RpcController{

    @Autowired
    RpcMsgProcessor rpcMsgProcessor;

    @Autowired
    DeviceService deviceService;

    @RequestMapping(value = "/{deviceId}/{requestId}", method = RequestMethod.POST)
    public DeferredResult<ResponseEntity> sendRpcCommandToDevice(@RequestBody String data, @PathVariable String deviceId,
                                                                 @PathVariable int requestId)throws Exception{
        DeferredResult<ResponseEntity> res = new DeferredResult<>();
        Device device = deviceService.findDeviceById(UUID.fromString(deviceId));
        if(device==null) {
            res.setResult(new ResponseEntity(HttpStatus.BAD_REQUEST));
            return res;
        }
        JsonObject serviceObj = HttpUtil.getDeviceServiceDes(device.getManufacture(),device.getDeviceType(),
                device.getModel(),new JsonParser().parse(data).getAsJsonObject().get("serviceName").getAsString());
        String pid= device.getParentDeviceId();
        BasicFromServerRpcMsg msg;
        if (pid==null||"".equals(pid)){
            msg    = new BasicFromServerRpcMsg(requestId,data,device,res,serviceObj);
        }else{
            Device  pdevice = deviceService.findDeviceById(UUID.fromString(pid));
            msg    = new BasicFromServerRpcMsg(requestId,data,pdevice,res,serviceObj);
        }
        rpcMsgProcessor.process(msg);
        return res;
    }
}
