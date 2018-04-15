package cn.edu.bupt.transport.mqtt;

import cn.edu.bupt.actor.service.SessionMsgProcessor;
import cn.edu.bupt.common.security.DeviceTokenCredentials;
import cn.edu.bupt.message.AdaptorToSessionActorMsg;
import cn.edu.bupt.message.MsgType;
import cn.edu.bupt.message.SessionCloseMsg;
import cn.edu.bupt.service.DeviceAuthService;
import cn.edu.bupt.service.DeviceService;
import cn.edu.bupt.transport.TransportAdaptor;
import cn.edu.bupt.transport.mqtt.session.DeviceSessionCtx;
import cn.edu.bupt.utils.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_ACCEPTED;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED;
import static io.netty.handler.codec.mqtt.MqttMessageType.PINGRESP;
import static io.netty.handler.codec.mqtt.MqttQoS.AT_MOST_ONCE;

/**
 * Created by Administrator on 2018/4/13.
 */
public class MqttTransportHandler extends ChannelInboundHandlerAdapter implements GenericFutureListener<Future<? super Void>> {

    private volatile boolean connected;
    private SessionMsgProcessor processor;
    private DeviceService deviceService;
    private DeviceAuthService deviceAuthService;
    private DeviceSessionCtx deviceSessionCtx;
    private TransportAdaptor adaptor;

    public MqttTransportHandler(SessionMsgProcessor processor, DeviceService deviceService, DeviceAuthService deviceAuthService ,TransportAdaptor adaptor){
        this.processor = processor;
        this.deviceService = deviceService;
        this.deviceAuthService = deviceAuthService;
        this.adaptor = adaptor;
        this.deviceSessionCtx = new DeviceSessionCtx(deviceAuthService,adaptor);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){

        if(msg instanceof MqttMessage){
            processMqttMsg(ctx,(MqttMessage)msg);
        }
    }

    private void processMqttMsg(ChannelHandlerContext ctx, MqttMessage msg) {
        if(msg.fixedHeader() == null){
            //TODO 输出异常连接消息
            processDisconnect(ctx);
        }else{
            switch(msg.fixedHeader().messageType()){
                case CONNECT:
                    processConnect(ctx, (MqttConnectMessage) msg);
                    break;
                case PUBLISH:
                    processPublish(ctx, (MqttPublishMessage) msg);
                    break;
                case PINGREQ:
                    if (checkConnected(ctx)) {
                        ctx.writeAndFlush(new MqttMessage(new MqttFixedHeader(PINGRESP, false, AT_MOST_ONCE, false, 0)));
                    }
                    break;
                case DISCONNECT:
                    if (checkConnected(ctx)) {
                        processDisconnect(ctx);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean checkConnected(ChannelHandlerContext ctx) {
        if(connected)
            return true;
        else
            ctx.close();
        return false;
    }

    private void processPublish(ChannelHandlerContext ctx, MqttPublishMessage mqttMsg) {
        if(!checkConnected(ctx)){
            return;
        }
        String topicName = mqttMsg.variableHeader().topicName();
        int msgId = mqttMsg.variableHeader().messageId();

        processDevicePublish(ctx, mqttMsg, topicName, msgId);
    }

    private void processDevicePublish(ChannelHandlerContext ctx, MqttPublishMessage mqttMsg, String topicName, int msgId) {
        AdaptorToSessionActorMsg msg = null ;
        try{
            if(topicName.equals(MqttTopics.DEVICE_TELEMETRY_TOPIC)){
                msg = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.POST_TELEMETRY_REQUEST,mqttMsg);
            }else if(topicName.equals(MqttTopics.DEVICE_ATTRIBUTES_TOPIC)){
                msg = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.POST_ATTRIBUTE_REQUEST,mqttMsg);
            }
            //TODO 其他数据类型处理
        }catch(Exception e){
            //TODO 异常待处理
        }
        if(msg!=null){
           // processor.process(msg);
            System.out.println("received a data "+msg.getMsg().getMsgType());
        }else{

        }

    }

    private void processConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        //TODO 后期引入其他鉴权方式
        processAuthTokenConnect(ctx, msg);
    }

    private void processAuthTokenConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        String token = msg.payload().userName();
        if(StringUtil.isEmpty(token)){
            ctx.writeAndFlush(MqttMsgFactory.createMqttConnAckMsg(CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD));
            ctx.close();
        }else if(!deviceSessionCtx.login(new DeviceTokenCredentials(msg.payload().userName())) ){
            ctx.writeAndFlush(MqttMsgFactory.createMqttConnAckMsg(CONNECTION_REFUSED_NOT_AUTHORIZED));
            ctx.close();
        }else{
            ctx.writeAndFlush(MqttMsgFactory.createMqttConnAckMsg(CONNECTION_ACCEPTED));
            connected = true;
        }
    }

    private void processDisconnect(ChannelHandlerContext ctx) {
        ctx.close();
        if(connected){
            //TODO 避免抛出异常暂时关掉
          //  processor.process(SessionCloseMsg.onDisconnected(deviceSessionCtx.getSessionId()));
        }
    }


    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {
    }
}
