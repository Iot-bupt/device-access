package cn.edu.bupt.controller;

import cn.edu.bupt.actor.service.FromServerMsgProcessor;
import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.message.BasicFromServerMsg;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.security.HttpUtil;
import cn.edu.bupt.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/v1/deviceaccess")
public class DeviceController extends BaseController {
    @Autowired
    FromServerMsgProcessor fromServerMsgProcessor;

    public static final String DEVICE_ID = "deviceId";

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(@RequestParam String token)  {
        return HttpUtil.checkToken(token);
    }

    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public String test2()  {
        return "Hello";
    }

    @RequestMapping(value = "/tenant/deviceCount/{tenantId}", method = RequestMethod.GET)
    public Long getTenantDeviceCount(@PathVariable("tenantId") Integer tenantId)  {
        return deviceService.findDevicesCount(tenantId);
    }
    //对设备的操作
    //创建设备
    @RequestMapping(value = "/device", method = RequestMethod.POST)
    public String saveDevice(@RequestBody String device)  {
        try {
            //将提交表单的形式转为json格式提交

            Device device1 = JSON.parseObject(device, Device.class);

            Device savedDevice = checkNotNull(deviceService.saveDevice(device1));

            deviceService.sendMessage(savedDevice,"新增"+savedDevice.getName()+"设备");
            return savedDevice.toString();
        } catch (Exception e) {
            return onFail(e.toString());
        }
    }

    //删除设备
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/device/{deviceId}", method = RequestMethod.DELETE)
    public void deleteDevice(@PathVariable(DEVICE_ID) String strDeviceId) throws Exception {
        if (StringUtil.isEmpty(strDeviceId)) {
            throw new Exception("can't be empty");
        }
        try {
            Device device = deviceService.findDeviceById(toUUID(strDeviceId));
            deviceService.deleteDevice(toUUID(strDeviceId));

            deviceService.sendMessage(device, "删除"+device.getName()+"设备");
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }


    //通过设备ID查找设备
    @RequestMapping(value = "/device/{deviceId}", method = RequestMethod.GET)
    public Device getDeviceById(@PathVariable(DEVICE_ID) String strDeviceId) throws Exception {
        if (StringUtil.isEmpty(strDeviceId)) {
           throw new Exception("can't be empty");
        }
        try {
            Device device = deviceService.findDeviceById(toUUID(strDeviceId));
            return device;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //通过父设备ID查找设备
    @RequestMapping(value = "/parentdevices/{parentdeviceId}",params = {"limit"}, method = RequestMethod.GET)
    public List<Device> getDevicesByParentDeviceId(
            @PathVariable("parentdeviceId") String parentDeviceId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset==null?null:toUUID(idOffset), textOffset);
            return checkNotNull(deviceService.findDeviceByParentDeviceId(parentDeviceId, pageLink));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //对tenant设备的操作
    //获取tenant下的所有设备
    @RequestMapping(value = "/tenant/devices/{tenantId}", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getTenantDevices(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception {
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch,idOffset==null?null:toUUID(idOffset), textOffset);
            TextPageData<Device> ls = deviceService.findDevicesByTenantId(tenantId, pageLink);
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //删除tenant下的所有设备
    @RequestMapping(value = "/devices/{tenantId}", method = RequestMethod.DELETE)
    public void deleteDevicesByTenantId(@PathVariable("tenantId") Integer tenantId) throws Exception {
        try {
            deviceService.deleteDevicesByTenantId(tenantId);
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }

    //通过tenantID和Name查找设备
    @RequestMapping(value="/device/{tenantId}/{name}",method = RequestMethod.GET)
    public Optional<Device> getDeviceByTenantIdAndName(@PathVariable("tenantId") Integer tenantId,
                                                       @PathVariable("name") String name) throws Exception{
        try{
            Optional<Device> optionalDevice = deviceService.findDeviceByTenantIdAndName(tenantId, name);
            return optionalDevice;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //customer层面的设备操作
    //分配设备给客户
    @RequestMapping(value="/assign/customer/{deviceId}/{customerId}",method = RequestMethod.GET)
    public Device assignDeviceToCustomer(@PathVariable("deviceId") String deviceId,
                                         @PathVariable("customerId")Integer customerId) throws Exception{

        try{
            Device device = deviceService.assignDeviceToCustomer(toUUID(deviceId), customerId);
            return device;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //取消分配某个设备给客户
    @RequestMapping(value="/unassign/customer/{deviceId}",method = RequestMethod.DELETE)
    public Device unassignDeviceFromCustomer(@PathVariable("deviceId")String deviceId) throws Exception{
        try{
            Device device = deviceService.unassignDeviceFromCustomer(toUUID(deviceId));
            return device;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //取消分配客户的所有设备
    @RequestMapping(value = "/unassign/{tenantId}/{customerId}",method = RequestMethod.DELETE)
    public void unassignCustomerDevices(@PathVariable("tenantId") Integer tenantId,
                                        @PathVariable("customerId") Integer customerId) throws Exception{
        try{
            deviceService.unassignCustomerDevices(tenantId, customerId);
        }catch (Exception e){
            e.printStackTrace();
            return ;
        }
    }

    //获取客户的所有设备
    @RequestMapping(value = "/customerdevices/{tenantId}/{customerId}", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getDevicesByTenantIdAndCustomerId(
            @PathVariable("tenantId") Integer tenantId,
            @PathVariable("customerId") Integer customerId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception {
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset==null?null:toUUID(idOffset), textOffset);
            return checkNotNull(deviceService.findDevicesByTenantIdAndCustomerId(tenantId, customerId, pageLink));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //获取站点下的所有设备
    @RequestMapping(value = "/sitedevices/{tenantId}/{siteId}", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getDevicesByTenantIdAndSiteId(
            @PathVariable("tenantId") Integer tenantId,
            @PathVariable("siteId") Integer siteId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception {
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset==null?null:toUUID(idOffset), textOffset);
            return checkNotNull(deviceService.findDevicesByTenantIdAndSiteId(tenantId, siteId, pageLink));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //分配设备到站点，即更新设备
    @RequestMapping(value = "/assign/site", method = RequestMethod.POST)
    public String assignDeviceToSite(@RequestBody String device)  {
        try {
            Device sitedevice = JSON.parseObject(device, Device.class);
            return checkNotNull(deviceService.saveDevice(sitedevice)).toString();
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/{manufactuere}/{deviceType}/{model}/devices/", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getDevicesByTenantIdAndCustomerId(
            @PathVariable("manufactuere") String manufactuere,
            @PathVariable("deviceType") String deviceType,
            @PathVariable("model") String model,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset
    ){
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset==null?null:toUUID(idOffset), textOffset);
            return checkNotNull( deviceService.findDevicesByManufactureAndDeviceTypeAndModel(
                    manufactuere, deviceType,model,pageLink
            ));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/devices/{tenantId}", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getDevices(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception {
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch,idOffset==null?null:toUUID(idOffset), textOffset);
            TextPageData<Device> ls = deviceService.findDevices(tenantId,pageLink);
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/device/status/{tenantId}", method = RequestMethod.POST)
    public DeferredResult<ResponseEntity> getDeviceStatus(@RequestBody String devices, @PathVariable Integer tenantId){
        DeferredResult<ResponseEntity> res = new DeferredResult<>();

        try{
            JsonObject jsonObject = (JsonObject)new JsonParser().parse(devices);
            List<String> deviceIds = new ArrayList<>();
            JsonArray Dids = jsonObject.getAsJsonArray("deviceId");
            for(JsonElement element : Dids){
                deviceIds.add(element.getAsString());
            }

            BasicFromServerMsg msg = new BasicFromServerMsg(tenantId.toString(),deviceIds,res);
            fromServerMsgProcessor.process(msg);

            return res;

        }catch (Exception e){
            return null;
        }
    }

}
