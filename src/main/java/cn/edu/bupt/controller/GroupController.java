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


    @RequestMapping("")
    public String hello(){
        return "Hello, IOT";
    }


    @RequestMapping(value = "/saveGroup", method = RequestMethod.POST)
    @ResponseBody
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

    @RequestMapping(value = "/devices/{groupId}",params = {"limit"}, method = RequestMethod.GET)
    @ResponseBody
    public TextPageData<Device> getCustomerDevices(
            @PathVariable("groupId") String strGroupId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) UUID idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        checkParameter("groupId", strGroupId);
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset, textOffset);
            TextPageData<Device> devices = deviceService.findDevicesByGroupId(toUUID(strGroupId), pageLink);
            return devices;
        }catch(Exception e){
            return null;
        }
    }

    @RequestMapping(value = "/assign/{groupId}/{deviceId}", method = RequestMethod.GET)
    @ResponseBody
    public void assignDeviceToGroup(@PathVariable("groupId") UUID groupId, @PathVariable("deviceId") UUID deviceId) throws Exception{
        try{
            deviceService.assignDeviceToGroup(groupId,deviceId);
        }catch(Exception e){
            e.getMessage();
        }
    }

    @RequestMapping(value = "/unassign/{groupId}/{deviceId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void unassignDeviceFromGroup(@PathVariable("groupId") UUID groupId, @PathVariable("deviceId") UUID deviceId) throws Exception{
        try{
            deviceService.unassignDeviceFromGroup(groupId, deviceId);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
