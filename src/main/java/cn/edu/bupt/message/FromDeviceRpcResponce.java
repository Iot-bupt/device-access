package cn.edu.bupt.message;

/**
 * Created by Administrator on 2018/4/16.
 */
public class FromDeviceRpcResponce implements FromDeviceMsg{
    private final int requestId;
    private final String data;

    public FromDeviceRpcResponce(int id,String data){
        requestId = id;
        this.data = data;
    }
    @Override
    public String getMsgType() {
        return MsgType.FROM_DEVICE_RPC_RESPONCE;
    }
}
