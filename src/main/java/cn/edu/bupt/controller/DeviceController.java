package cn.edu.bupt.controller;

import cn.edu.bupt.actor.service.FromServerMsgProcessor;
import cn.edu.bupt.dao.exception.IOTErrorCode;
import cn.edu.bupt.dao.exception.IOTException;
import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.message.BasicFromServerMsg;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.security.HttpUtil;
import cn.edu.bupt.security.model.Authority;
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

    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/deviceCount", method = RequestMethod.GET)
    public Long getTenantDeviceCount(@RequestParam Integer tenantId)  {
        return deviceService.findDevicesCount(tenantId);
    }

    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('CUSTOMER_USER')")
    @RequestMapping(value = "/customer/deviceCount", method = RequestMethod.GET)
    public Long getCustomerDeviceCount(@RequestParam Integer tenantId,@RequestParam Integer customerId) throws IOTException{
        try {
            if (getCurrentUser().getCustomerId().equals(customerId)||
                    ((getCurrentUser().getAuthority().equals(Authority.TENANT_ADMIN))&&getCurrentUser().getTenantId().equals(tenantId))) {
                return deviceService.findCustomerDevicesCount(customerId);
            }else{
                throw new IOTException("You aren't authorized to perform this operation!", IOTErrorCode.AUTHENTICATION);
            }
        }catch(Exception e){
            throw handleException(e);
        }
    }

    //对设备的操作
    //创建设备
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'saveDevice')")
    @RequestMapping(value = "/device", method = RequestMethod.POST)
    public String saveDevice(@RequestBody String device)  {
        try {
            //将提交表单的形式转为json格式提交

            Device device1 = JSON.parseObject(device, Device.class);

            Device savedDevice = checkNotNull(deviceService.saveDevice(device1));

            deviceService.sendMessage(savedDevice,"新增/更新设备："+savedDevice.getName());
            return savedDevice.toString();
        } catch (Exception e) {
            return onFail(e.toString());
        }
    }

    //改变设备站点ID
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'updateDeviceSiteId')")
    @RequestMapping(value = "/device", method = RequestMethod.PUT)
    public String updateDeviceSiteId(@RequestBody String device)  {
        try {
            //将提交表单的形式转为json格式提交

            Device device1 = JSON.parseObject(device, Device.class);
            Device changedDevice = checkNotNull(deviceService.updateDeviceSiteId(device1.getId(),device1.getSiteId()));
            return changedDevice.toString();
        } catch (Exception e) {
            return onFail(e.toString());
        }
    }

    //删除设备
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'deleteDevice')")
    @RequestMapping(value = "/device/{deviceId}", method = RequestMethod.DELETE)
    public void deleteDevice(@PathVariable(DEVICE_ID) String strDeviceId) throws Exception {
        if (StringUtil.isEmpty(strDeviceId)) {
            throw new Exception("can't be empty");
        }
        try {
            Device device = deviceService.findDeviceById(toUUID(strDeviceId));
            deviceService.deleteDevice(toUUID(strDeviceId));

            deviceService.sendMessage(device, "删除设备："+device.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }


    //通过设备ID查找设备
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDeviceById')")
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDevicesByParentDeviceId')")
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

    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getTenantDevicesCountByTextSearch')")
    @RequestMapping(value = "/tenant/devices/SearchCount/{tenantId}", method = RequestMethod.GET)
    public Long getTenantDevicesCountByTextSearch(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam String textSearch) throws Exception {
        try {
            TextPageLink pageLink = new TextPageLink(1,textSearch);
            Long count = deviceService.findDevicesCountWithTextSearch(tenantId,pageLink);
            System.out.println(count);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getCustomerDevicesCountByTextSearch')")
    @RequestMapping(value = "/customerdevices/SearchCount/{tenantId}/{customerId}", method = RequestMethod.GET)
    public Long getCustomerDevicesCountByTextSearch(
            @PathVariable("tenantId") Integer tenantId,
            @PathVariable("customerId") Integer customerId,
            @RequestParam String textSearch) throws Exception {
        try {
            TextPageLink pageLink = new TextPageLink(1,textSearch);
            Long count = deviceService.findDevicesCountWithTextSearch(tenantId,customerId,pageLink);
            System.out.println(count);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDevices')")
    @RequestMapping(value = "/tenant/devices/{tenantId}", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getTenantDevicesCount(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception {
        try {
            TextPageLink pageLink = new TextPageLink(limit, textSearch,idOffset==null?null:toUUID(idOffset), textOffset);
            TextPageData<Device> ls = deviceService.findDevicesByTenantId(tenantId, pageLink);
            TextPageLink pageLink1 = new TextPageLink(1,textSearch);
            Long count = deviceService.findDevicesCountWithTextSearch(tenantId,pageLink1);
            System.out.println(count);
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //删除tenant下的所有设备
    @PreAuthorize("#oauth2.hasScope('all') or hasPermission(null ,'deleteDevicesByTenantId')")
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDeviceByTenantIdAndName')")
    @RequestMapping(value="/device/{tenantId}/{name}",method = RequestMethod.GET)
    public Device getDeviceByTenantIdAndName(@PathVariable("tenantId") Integer tenantId,
                                                       @PathVariable("name") String name) throws Exception{
        try{
            Optional<Device> optionalDevice = deviceService.findDeviceByTenantIdAndName(tenantId, name);
            if(optionalDevice.isPresent()) {
                return optionalDevice.get();
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //customer层面的设备操作
    //分配设备给客户
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'assignDeviceToCustomer')")
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'unassignDeviceFromCustomer')")
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'unassignCustomerDevices')")
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDevicesByTenantIdAndCustomerId')")
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDevicesByTenantIdAndSiteId')")
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'assignDeviceToSite')")
    @RequestMapping(value = "/assign/site", method = RequestMethod.POST)
    public String assignDeviceToSite(@RequestBody String device)  {
        try {
            Device sitedevice = JSON.parseObject(device, Device.class);
            return checkNotNull(deviceService.saveDevice(sitedevice)).toString();
        } catch (Exception e) {
            return null;
        }
    }

    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDevicesByService')")
    @RequestMapping(value = "/{manufactuere}/{deviceType}/{model}/devices/", params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getDevicesByService(
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

    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDevices')")
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

    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getDeviceStatus')")
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

    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'suspendDevices')")
    @RequestMapping(value = "/devices/suspend/{tenantId}", method = RequestMethod.PUT)
    public void suspendDevices(@PathVariable("tenantId") Integer tenantId) throws Exception {
        deviceService.suspendedDeviceByTenantId(tenantId);
    }

    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'activateDevices')")
    @RequestMapping(value = "/devices/activate/{tenantId}", method = RequestMethod.PUT)
    public void activateDevices(@PathVariable("tenantId") Integer tenantId) throws Exception {
        deviceService.activatedDeviceByTenantId(tenantId);
    }

}
