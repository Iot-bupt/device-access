package cn.edu.bupt.controller;

import cn.edu.bupt.actor.service.RpcMsgProcessor;
import cn.edu.bupt.message.BasicFromServerRpcMsg;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.service.DeviceService;
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
                                                                 @PathVariable int requestId){
        DeferredResult<ResponseEntity> res = new DeferredResult<>();
        Device device = deviceService.findDeviceById(UUID.fromString(deviceId));
        if(device==null) {
            res.setResult(new ResponseEntity(HttpStatus.BAD_REQUEST));
            return res;
        }
        BasicFromServerRpcMsg msg = new BasicFromServerRpcMsg(requestId,data,device,res);
        rpcMsgProcessor.process(msg);
        return res;
    }
}
