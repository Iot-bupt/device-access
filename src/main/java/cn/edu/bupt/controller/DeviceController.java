package cn.edu.bupt.controller;

import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.utils.StringUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class DeviceController extends BaseController {
    public static final String DEVICE_ID = "deviceId";


    @RequestMapping(value = "/device/{deviceId}", method = RequestMethod.GET)
    @ResponseBody
    public Device getDeviceById(@PathVariable(DEVICE_ID) String strDeviceId) throws Exception {
        if (StringUtil.isEmpty(strDeviceId)) {
            throw new Exception("can't be empty");
        }
        try {
            Device device = deviceService.findDeviceById(toUUID(strDeviceId));
            return device;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/createDevice", method = RequestMethod.POST)
    @ResponseBody
    public Device saveDevice(Device device) throws Exception {
        try {
            Device savedDevice = checkNotNull(deviceService.saveDevice(device));
            return savedDevice;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/device/{deviceId}", method = RequestMethod.DELETE)
    public void deleteDevice(@PathVariable(DEVICE_ID) String strDeviceId) throws Exception {
        if (StringUtil.isEmpty(strDeviceId)) {
            throw new Exception("can't be empty");
        }
        try {
            deviceService.deleteDevice(toUUID(strDeviceId));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @RequestMapping(value = "/tenantDevices/{tenantId}", params = {"limit"}, method = RequestMethod.GET)
    @ResponseBody
    public TextPageData<Device> getTenantDevices(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception {
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            return checkNotNull(deviceService.findDevicesByTenantId(tenantId, pageLink));
        } catch (Exception e) {
            throw null;
        }
    }

    @RequestMapping(value = "/devices/{tenantId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteDevicesByTenantId(@PathVariable("tenantId") Integer tenantId) throws Exception {
        try {
            deviceService.deleteDevicesByTenantId(tenantId);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @RequestMapping(value = "/parentDevices/{parentDeviceId}",params = {"limit"}, method = RequestMethod.GET)
    @ResponseBody
    public List<Device> getDevicesByParentDeviceId(
            @PathVariable("parentDeviceId") String parentDeviceId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            return checkNotNull(deviceService.findDeviceByParentDeviceId(parentDeviceId, pageLink));
        }catch(Exception e){
            return null;
        }
    }


}
