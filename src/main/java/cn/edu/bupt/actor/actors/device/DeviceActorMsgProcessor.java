package cn.edu.bupt.actor.actors.device;

import akka.actor.ActorRef;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.entry.KvEntry;
import cn.edu.bupt.message.*;
import cn.edu.bupt.pojo.kv.AttributeKvEntry;
import cn.edu.bupt.pojo.kv.BasicTsKvEntry;
import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.service.BaseAttributesService;
import cn.edu.bupt.service.BaseTimeseriesService;
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
            case MsgType.FROM_DEVICE_RPC_RESPONCE:
                System.err.println("receive a rpc  responce");
                int requestId = ((FromDeviceRpcResponce)fromDeviceMsg).getRequestId();
                DeferredResult result = rpcRequests.get(requestId);
                if(result == null){
                    //TODO 记录一下rpc超时
                }else{
                    result.setResult(((FromDeviceRpcResponce)fromDeviceMsg).getData());
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
            if(msg1.requireResponce()){
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
        Map<Long, List<KvEntry>> data = msg.getData();
        for( long ttl : data.keySet()){
            UUID entityId = UUID.fromString(msg1.getDeviceId());
            List<KvEntry> KvEntry =  data.get(ttl);
          //  BaseTimeseriesService baseTimeseriesService = new BaseTimeseriesService();
          //  actorSystemContext.get
            List<TsKvEntry> ls = new ArrayList<>();
            KvEntry.forEach(entry->{
                ls.add((TsKvEntry)entry);
            });
            BaseTimeseriesService  baseTimeseriesService =  actorSystemContext.getBaseTimeseriesService();
            baseTimeseriesService.save(entityId, ls, ttl);
        }
    }


    public void handleAttributeUploadRequest(AttributeUploadMsg msg, BasicToDeviceActorMsg msg1){
        List<AttributeKvEntry> kvEntries = (List)msg.getData();
        UUID entityId = UUID.fromString(msg1.getDeviceId());
        BaseAttributesService baseAttributesService = new BaseAttributesService();
        baseAttributesService.save(entityId, kvEntries);
    }

}
