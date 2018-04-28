package cn.edu.bupt.controller;

import cn.edu.bupt.pojo.DeviceCredentials;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DeviceCredentialController extends BaseController {

    @RequestMapping(value = "/credential/{credential}",method = RequestMethod.POST)
    public DeviceCredentials create(@PathVariable("credential") DeviceCredentials credentials) throws Exception {
        //提交表单转变为json

        try {
            DeviceCredentials deviceCredentials = deviceCredentialsService.createDeviceCredentials(credentials);
            return deviceCredentials;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/credential/{credential}",method = RequestMethod.DELETE)
    public void delete(@PathVariable("credential") DeviceCredentials credentials) throws Exception {
        try {
           deviceCredentialsService.deleteDeviceCredentials(credentials);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/credential/{credential}",method = RequestMethod.PUT)
    public DeviceCredentials update(@PathVariable("credential") DeviceCredentials credentials) throws Exception {
        try {
            DeviceCredentials deviceCredentials = deviceCredentialsService.updateDeviceCredentials(credentials);
            return deviceCredentials;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/credentialbyid/{deviceId}",method = RequestMethod.GET)
    public DeviceCredentials getById(@PathVariable("deviceId") String deviceId) throws Exception {
        try {
            DeviceCredentials deviceCredentials = deviceCredentialsService.findDeviceCredentialsByDeviceId(toUUID(deviceId));
            return deviceCredentials;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/crednetialbytoken/{token}",method = RequestMethod.GET)
    public DeviceCredentials getByToken(@PathVariable("token") String token) throws Exception {
        try {
            DeviceCredentials deviceCredentials = deviceCredentialsService.findDeviceCredentialsByToken(token);
            return deviceCredentials;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
