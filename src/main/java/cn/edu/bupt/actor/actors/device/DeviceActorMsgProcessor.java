package cn.edu.bupt.actor.actors.device;

import akka.actor.ActorRef;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.entry.BasicAttributeKvEntry;
import cn.edu.bupt.message.*;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.kv.*;

import cn.edu.bupt.service.BaseAttributesService;
import cn.edu.bupt.service.BaseTimeseriesService;
import cn.edu.bupt.utils.KafkaUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

/**
 * Created by Administrator on 2018/4/17.
 */
public class DeviceActorMsgProcessor {

    private final Set<String> subscriptions;
    private final  Map<Integer,DeferredResult<ResponseEntity>> rpcRequests;
    private final ActorSystemContext actorSystemContext;

    public  DeviceActorMsgProcessor(ActorSystemContext actorSystemContext){
        rpcRequests  = new HashMap<>();
        subscriptions = new HashSet<>();
        this.actorSystemContext = actorSystemContext;
    }

    public void process(BasicToDeviceActorMsg msg) {
        BasicToDeviceActorMsg msg1 = (BasicToDeviceActorMsg)msg;
        AdaptorToSessionActorMsg adptorMsg = msg1.getMsg();
        FromDeviceMsg fromDeviceMsg = adptorMsg.getMsg();
        switch(fromDeviceMsg.getMsgType()){
            case MsgType.POST_TELEMETRY_REQUEST:
                handleTelemetryUploadRequest((TelemetryUploadMsg)fromDeviceMsg, msg1);
                System.err.println("receive a telemetry msg");
                break;
            case MsgType.POST_ATTRIBUTE_REQUEST:
                handleAttributeUploadRequest((AttributeUploadMsg)fromDeviceMsg, msg1);
                System.err.println("receive a attribute msg");
                break;
            case MsgType.FROM_DEVICE_RPC_SUB:
                System.err.println("receive a rpc sub");
                subscriptions.add(adptorMsg.getSessionId().toUidStr());
                break;
            case MsgType.FROM_DEVICE_RPC_UNSUB:
                System.err.println("receive a rpc unsub");
                subscriptions.remove(adptorMsg.getSessionId().toUidStr());
                break;
            case MsgType.FROM_DEVICE_RPC_RESPONSE:
                System.err.println("receive a rpc  response");
                int requestId = ((FromDeviceRpcResponse)fromDeviceMsg).getRequestId();
                DeferredResult result = rpcRequests.get(requestId);
                if(result == null){
                    //TODO 记录一下rpc超时
                }else{
                    result.setResult(((FromDeviceRpcResponse)fromDeviceMsg).getData());
                    rpcRequests.remove(requestId);
                }
                break;
            default:{
                System.err.println("unKnow msg type");
                }
        }
    }

    public void process(FromServerMsg msg) {
        if(msg.getMsgType().equals(MsgType.FROM_SERVER_RPC_MSG)){
            BasicFromServerRpcMsg msg1 = (BasicFromServerRpcMsg)(msg);
            int requestId = msg1.getRpcRequestId();
            if(msg1.requireResponse()){
                rpcRequests.put(requestId,msg1.getRes());
                subscriptions.forEach(sessionId->{
                    actorSystemContext.getSessionManagerActor().tell(
                            new BasicFromDeviceActorRpc(sessionId,msg1.getDevice(),msg1),
                            ActorRef.noSender()
                    );
                });
            }else{
                msg1.getRes().setResult(new ResponseEntity(HttpStatus.OK));
                subscriptions.forEach(sessionId->{
                    actorSystemContext.getSessionManagerActor().tell(
                            new BasicFromDeviceActorRpc(sessionId,msg1.getDevice(),msg1),
                            ActorRef.noSender()
                    );
                });
            }
        }
    }


     public void handleTelemetryUploadRequest(TelemetryUploadMsg msg, BasicToDeviceActorMsg msg1){
        Map<Long, List<cn.edu.bupt.common.entry.KvEntry>> data = msg.getData();
         sendDataToKafka(msg1.getDevice(),data);
        for( long ts : data.keySet()){
            UUID entityId = UUID.fromString(msg1.getDeviceId());
            List<cn.edu.bupt.common.entry.KvEntry> KvEntry = data.get(ts);
            List<TsKvEntry> ls = new ArrayList<>();
            KvEntry.forEach(entry->{
                ls.add(new BasicAdaptorTsKvEntry(ts,entry));
            });
            BaseTimeseriesService baseTimeseriesService = actorSystemContext.getBaseTimeseriesService();
            baseTimeseriesService.save(entityId, ls, 0);
        }
    }


    public void handleAttributeUploadRequest(AttributeUploadMsg msg, BasicToDeviceActorMsg msg1){
        Set<cn.edu.bupt.common.entry.KvEntry> atts = msg.getData();
        List<AttributeKvEntry> attributes = new ArrayList<>();
        atts.forEach(entry->{
            attributes.add(new BasicAdaptorAttributeKvEntry(entry,((BasicAttributeKvEntry)entry).getLastUpdateTs()));
        });
        UUID entityId = UUID.fromString(msg1.getDeviceId());
        BaseAttributesService baseAttributesService = actorSystemContext.getBaseAttributesService();
        baseAttributesService.save(entityId, attributes);
    }

    private void sendDataToKafka(Device device,Map<Long, List<cn.edu.bupt.common.entry.KvEntry>> data){
        JsonObject obj =  new JsonObject();
        obj.addProperty("deviceId",device.getId().toString());
        obj.addProperty("tenantId",device.getTenantId());
        obj.addProperty("deviceType",device.getDeviceType());
        JsonArray array = new JsonArray();
        for(Map.Entry<Long,List<cn.edu.bupt.common.entry.KvEntry>> entry:data.entrySet()){
            JsonObject temp = new JsonObject();
            long ts = entry.getKey();
            for(cn.edu.bupt.common.entry.KvEntry en:entry.getValue()){
                temp.addProperty("ts",ts);
                temp.addProperty("key",en.getKey());
                switch(en.getDataType()){
                    case "string":
                        temp.addProperty("value",en.getStrValue().get());
                        break;
                    case "boolean":
                        temp.addProperty("value",en.getBooleanValue().get());
                        break;
                    case "long":
                        temp.addProperty("value",en.getLongValue().get());
                        break;
                    case "double":
                        temp.addProperty("value",en.getDoubleValue().get());
                        break;
                }
                array.add(temp);
            }
        }
        obj.add("data",array);
        KafkaUtil.send("",obj.toString());
    }

}
