package cn.edu.bupt.controller;

import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.exception.DeviceAccessException;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class DeviceController extends BaseController {
    public static final String DEVICE_ID = "deviceId";

    //对设备的操作
    @RequestMapping(value = "/device", method = RequestMethod.POST)
    public Device saveDevice(@RequestBody String device)  {
        try {
            //将提交表单的形式转为json格式提交

            Device device1 = JSON.parseObject(device, Device.class);

            Device savedDevice = checkNotNull(deviceService.saveDevice(device1));
            return savedDevice;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/device/{deviceId}", method = RequestMethod.DELETE)
    public void deleteDevice(@PathVariable(DEVICE_ID) String strDeviceId) throws DeviceAccessException {
        if (StringUtil.isEmpty(strDeviceId)) {
            return ;
        }
        try {
            deviceService.deleteDevice(toUUID(strDeviceId));
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }


    @RequestMapping(value = "/device/{deviceId}", method = RequestMethod.GET)
    public Device getDeviceById(@PathVariable(DEVICE_ID) String strDeviceId) throws DeviceAccessException {
        if (StringUtil.isEmpty(strDeviceId)) {
//            throw handleException(new Exception("can't be empty"));
            return null ;
        }
        try {
            Device device = deviceService.findDeviceById(toUUID(strDeviceId));
            return device;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/parentdevices/{parentdeviceId}",params = {"limit"}, method = RequestMethod.GET)
    public List<Device> getDevicesByParentDeviceId(
            @PathVariable("parentdeviceId") String parentDeviceId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws DeviceAccessException{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            return checkNotNull(deviceService.findDeviceByParentDeviceId(parentDeviceId, pageLink));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //对tenant设备的操作
    @RequestMapping(value = "/tenant/devices/{tenantId}", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getTenantDevices(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws DeviceAccessException {
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            return checkNotNull(deviceService.findDevicesByTenantId(tenantId, pageLink));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/devices/{tenantId}", method = RequestMethod.DELETE)
    public void deleteDevicesByTenantId(@PathVariable("tenantId") Integer tenantId) throws DeviceAccessException {
        try {
            deviceService.deleteDevicesByTenantId(tenantId);
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }

    @RequestMapping(value="/device/{tenantId}/{name}",method = RequestMethod.GET)
    public Optional<Device> getDeviceByTenantIdAndName(@PathVariable("tenantId") Integer tenantId,
                                                       @PathVariable("name") String name) throws DeviceAccessException{
        try{
            Optional<Device> optionalDevice = deviceService.findDeviceByTenantIdAndName(tenantId, name);
            return optionalDevice;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //customer层面的设备操作
    @RequestMapping(value="/assign/customer/{deviceId}/{customerId}",method = RequestMethod.GET)
    public Device assignDeviceToCustomer(@PathVariable("deviceId") String deviceId,
                                         @PathVariable("customerId")Integer customerId) throws DeviceAccessException{

        try{
            Device device = deviceService.assignDeviceToCustomer(toUUID(deviceId), customerId);
            return device;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value="unassign/customer/{deviceId}",method = RequestMethod.DELETE)
    public Device unassignDeviceFromCustomer(@PathVariable("deviceId")String deviceId) throws DeviceAccessException{
        try{
            Device device = deviceService.unassignDeviceFromCustomer(toUUID(deviceId));
            return device;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "unassign/{tenantId}/{customerId}",method = RequestMethod.DELETE)
    public void unassignCustomerDevices(@PathVariable("tenantId") Integer tenantId,
                                        @PathVariable("customerId") Integer customerId) throws DeviceAccessException{
        try{
            deviceService.unassignCustomerDevices(tenantId, customerId);
        }catch (Exception e){
            e.printStackTrace();
            return ;
        }
    }

    @RequestMapping(value = "/customerdevices/{tenantId}/{customerId}", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getDevicesByTenantIdAndCustomerId(
            @PathVariable("tenantId") Integer tenantId,
            @PathVariable("customerId") Integer customerId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws DeviceAccessException {
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            return checkNotNull(deviceService.findDevicesByTenantIdAndCustomerId(tenantId, customerId, pageLink));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
