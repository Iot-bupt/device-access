package cn.edu.bupt.transport.mqtt.adaptor;

import cn.edu.bupt.common.SessionContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.message.*;
import cn.edu.bupt.transport.AdaptorException;
import cn.edu.bupt.transport.TransportAdaptor;
import cn.edu.bupt.transport.mqtt.session.DeviceSessionCtx;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Created by Administrator on 2018/4/14.
 */
public class JsonMqttAdaptor implements TransportAdaptor<DeviceSessionCtx,MqttMessage,MqttMessage>{
    private static final Gson GSON = new Gson();
    private static final Charset UTF8 = Charset.forName("UTF-8");


    @Override
    public AdaptorToSessionActorMsg convertToActorMsg(DeviceSessionCtx ctx, String msgType, MqttMessage inbound) throws AdaptorException {
        FromDeviceMsg msg = null;
        switch(msgType){
            case MsgType.POST_TELEMETRY_REQUEST:
                msg = convertToTelemetryUploadRequest(ctx,(MqttPublishMessage) inbound);
                break;
            case MsgType.POST_ATTRIBUTE_REQUEST:
                msg = convertToUpdateAttributesRequest(ctx, (MqttPublishMessage) inbound);
                break;
        }
        return new BasicAdapterToSessionActorMsg(ctx,msg);
    }

    @Override
    public Optional<MqttMessage> convertToAdaptorMsg(DeviceSessionCtx ctx, SessionActorToAdaptorMsg msg) throws AdaptorException {
        return null;
    }

    private FromDeviceMsg convertToUpdateAttributesRequest(DeviceSessionCtx ctx, MqttPublishMessage inbound) throws AdaptorException {
        String payLoad = validatePayload(ctx.getSessionId(),inbound.payload());
        try{
            return JsonConverter.convertToAttribute(new JsonParser().parse(payLoad),inbound.variableHeader().messageId());
        }catch(Exception e){
            throw new AdaptorException(e.toString());
        }
    }

    private FromDeviceMsg convertToTelemetryUploadRequest(DeviceSessionCtx ctx, MqttPublishMessage inbound) throws AdaptorException  {
        String payLoad = validatePayload(ctx.getSessionId(),inbound.payload());
        try {
            return JsonConverter.convertToTelemetry(new JsonParser().parse(payLoad),inbound.variableHeader().messageId());
        }catch (Exception e){
            throw new AdaptorException(e.toString());
        }
    }

    private String validatePayload(SessionId sessionId, ByteBuf payloadData) throws AdaptorException {
        try{
            String payload = payloadData.toString(UTF8);
            if(payload==null){
                throw new AdaptorException("payload is empty");
            }
            return payload;
        }finally {
            payloadData.release();
        }
    }
}
