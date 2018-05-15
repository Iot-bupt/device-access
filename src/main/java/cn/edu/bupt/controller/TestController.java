package cn.edu.bupt.controller;

import cn.edu.bupt.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class TestController {

    @Autowired
    WebSocketServer webSocketServer;

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    public String getDevice(){
       return webSocketServer.deviceId;
    }
}
