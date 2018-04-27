package cn.edu.bupt.controller;

import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.Group;
import cn.edu.bupt.utils.StringUtil;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class GroupController extends BaseController {

    public static final String DEVICE_ID = "deviceId";
    public static final String GROUP_ID = "groupId";

    //设备层面的设备组
    @RequestMapping(value = "/devices/{groupId}",params = {"limit"}, method = RequestMethod.POST)
    public TextPageData<Device> getDevicesByGroupId(
            @PathVariable(GROUP_ID) String strGroupId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        checkParameter("groupId", strGroupId);
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            TextPageData<Device> devices = deviceService.findDevicesByGroupId(toUUID(strGroupId), pageLink);
            return devices;
        }catch(Exception e){
            return null;
        }
    }

    @RequestMapping(value = "/assign/{groupId}/{deviceId}", method = RequestMethod.GET)
    public void assignDeviceToGroup(@PathVariable(GROUP_ID) String groupId, @PathVariable(DEVICE_ID) String deviceId) throws Exception{
        try{
            deviceService.assignDeviceToGroup(UUID.fromString(groupId),UUID.fromString(deviceId));
        }catch(Exception e){
            e.getMessage();
        }
    }


    @RequestMapping(value = "/unassign/devices/{groupId}", method = RequestMethod.DELETE)
    public void unassignDevicesFromGroup(@PathVariable(GROUP_ID) String groupId) throws Exception{
        try{
            deviceService.unassignDevicesByGroupId(UUID.fromString(groupId));
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/unassign/{groupId}/{deviceId}", method = RequestMethod.DELETE)
    public void unassignDeviceByGroupId(@PathVariable(GROUP_ID) String groupId, @PathVariable(DEVICE_ID) String deviceId) throws Exception{
        try{
            deviceService.unassignDeviceFromGroup(UUID.fromString(groupId), UUID.fromString(deviceId));
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //设备组层面的设备组
    @RequestMapping(value = "/saveGroup", method = RequestMethod.POST)
    public Group saveGroup(Group group) throws Exception{
        try {
            Group savedGroup = checkNotNull(groupService.saveGroup(group));
            return savedGroup;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.DELETE)
    public void deleteGroup(@PathVariable(GROUP_ID) String strGroupId) throws Exception{
        if(StringUtil.isEmpty(strGroupId)){
            throw new Exception("can't be empty");
        }
        try{
            groupService.deleteGroup(toUUID(strGroupId));
        }catch (Exception e){
            e.getMessage();
        }
    }

    @RequestMapping(value = "/tenantGroups/{tenantId}",params = {"limit"}, method = RequestMethod.POST)
    public TextPageData<Group> getGroupsByTenantId(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            TextPageData<Group> tenantgroups = groupService.findGroupsByTenantId(tenantId, pageLink);
            return tenantgroups;
        }catch(Exception e){
            return null;
        }
    }

    @RequestMapping(value = "/customerGroups/{customerId}",params = {"limit"}, method = RequestMethod.POST)
    public TextPageData<Group> getGroupsByCustomerId(
            @PathVariable("customerId") Integer customerId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, toUUID(idOffset), textOffset);
            TextPageData<Group> customergroups = groupService.findGroupsByCustomerId(customerId, pageLink);
            return customergroups;
        }catch(Exception e){
            return null;
        }
    }




}
