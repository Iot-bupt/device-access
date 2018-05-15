package cn.edu.bupt.websocket;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class WebSocketWrapper {

    static protected Set<String> subscribeDevices =  new HashSet<>();

   /* public WebSocketWrapper() {
        this.subscribedevices =  new HashSet<>();
    }*/
}
