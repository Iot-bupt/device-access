package cn.edu.bupt.message;

import cn.edu.bupt.pojo.Device;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by Administrator on 2018/4/24.
 */
public class BasicFromServerRpcMsg  implements   FromServerRpcMsg{

    private final int requestId;
    private final String data;
    @Getter
    private final Device device;
    @Getter
    private final DeferredResult<ResponseEntity> res;


   public  BasicFromServerRpcMsg(int id,String data,Device device,DeferredResult<ResponseEntity> res){
        this.requestId = id;
        this.data = data;
        this.device = device;
        this.res = res;

   }

    @Override
    public String getDeviceId() {
        return device.getId().toString();
    }

    public String getMsgType() {
        return MsgType.FROM_SERVER_RPC_MSG;
    }

    @Override
    public int getRpcRequestId() {
        return requestId;
    }

    @Override
    public String getRpcRequestPayLoad() {
        return data;
    }

    @Override
    public String getTenantId() {
        return device.getTenantId()+"";
    }

    @Override
    public boolean requireResponce() {
       //TODO 需要根据情况更改
        return true;
    }
}
