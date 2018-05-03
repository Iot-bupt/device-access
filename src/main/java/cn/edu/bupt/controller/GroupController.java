package cn.edu.bupt.controller;

import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.exception.DeviceAccessException;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.Group;
import cn.edu.bupt.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class GroupController extends BaseController {

    public static final String DEVICE_ID = "deviceId";
    public static final String GROUP_ID = "groupId";

    //设备层面的设备组
    @RequestMapping(value = "/group/devices/{groupId}",params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getDevicesByGroupId(
            @PathVariable(GROUP_ID) String strGroupId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws DeviceAccessException {
        try {
            checkParameter("groupId", strGroupId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            TextPageData<Device> devices = deviceService.findDevicesByGroupId(toUUID(strGroupId), pageLink);
            return devices;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/assign/group/{groupId}/{deviceId}", method = RequestMethod.GET)
    public void assignDeviceToGroup(@PathVariable(GROUP_ID) String groupId, @PathVariable(DEVICE_ID) String deviceId) throws DeviceAccessException{
        try{
            deviceService.assignDeviceToGroup(UUID.fromString(groupId),UUID.fromString(deviceId));
        }catch(Exception e){
            e.printStackTrace();
            return ;
        }
    }


    @RequestMapping(value = "/unassign/group/{groupId}", method = RequestMethod.DELETE)
    public void unassignDevicesFromGroup(@PathVariable(GROUP_ID) String groupId) throws DeviceAccessException{
        try{
            deviceService.unassignDevicesByGroupId(UUID.fromString(groupId));
        }catch(Exception e){
            e.printStackTrace();
            return ;
        }
    }


    @RequestMapping(value = "/unassign/group/{groupId}/{deviceId}", method = RequestMethod.DELETE)
    public void unassignDeviceByGroupId(@PathVariable(GROUP_ID) String groupId, @PathVariable(DEVICE_ID) String deviceId) throws DeviceAccessException{
        try{
            deviceService.unassignDeviceFromGroup(UUID.fromString(groupId), UUID.fromString(deviceId));
        }catch(Exception e){
            e.printStackTrace();
            return ;
        }
    }


    //设备组层面的设备组
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public Group saveGroup(@RequestBody String group) throws DeviceAccessException{

        //表单转变为json提交
        try {
            Group group1 = JSON.parseObject(group, Group.class);
            Group savedGroup = checkNotNull(groupService.saveGroup(group1));
            return savedGroup;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.DELETE)
    public void deleteGroup(@PathVariable(GROUP_ID) String strGroupId) throws DeviceAccessException{
        if(StringUtil.isEmpty(strGroupId)){
          return ;
        }
        try{
            groupService.deleteGroup(toUUID(strGroupId));
        }catch (Exception e){
            e.printStackTrace();
            return ;
        }
    }

    @RequestMapping(value = "/groups/tenant/{tenantId}",params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Group> getGroupsByTenantId(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws DeviceAccessException{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            TextPageData<Group> tenantgroups = groupService.findGroupsByTenantId(tenantId, pageLink);
            return tenantgroups;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/groups/customer/{customerId}",params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Group> getGroupsByCustomerId(
            @PathVariable("customerId") Integer customerId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws DeviceAccessException{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            TextPageData<Group> customergroups = groupService.findGroupsByCustomerId(customerId, pageLink);
            return customergroups;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }




}
