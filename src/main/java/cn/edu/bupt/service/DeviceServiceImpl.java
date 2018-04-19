package cn.edu.bupt.service;

import cn.edu.bupt.dao.device.DeviceByGroupIdDao;
import cn.edu.bupt.dao.device.DeviceDao;
import cn.edu.bupt.dao.device.GroupDao;
import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceByGroupId;
import cn.edu.bupt.pojo.DeviceCredentials;
import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Int;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/14.
 */
@Service
public class DeviceServiceImpl implements  DeviceService{

    public static final String INCORRECT_TENANT_ID = "Incorrect tenantId ";
    public static final String INCORRECT_PAGE_LINK = "Incorrect page link ";
    public static final String INCORRECT_CUSTOMER_ID = "Incorrect customerId ";
    public static final String INCORRECT_DEVICE_ID = "Incorrect deviceId ";
    public static final String INCORRECT_GROUP_ID = "Incorrect groupId ";

    @Autowired
    private DeviceDao deviceDao;
//
//    @Autowired
//    private TenantDao tenantDao;
//
//    @Autowired
//    private CustomerDao customerDao;

    @Autowired
    private GroupDao groupDao;

//    @Autowired
//    private DeviceCredentialsService deviceCredentialsService;

    @Autowired
    private DeviceByGroupIdDao deviceByGroupIdDao;

    //*********查找组中设备**********
    @Override
    public TextPageData<Device> findDevicesByGroupId(UUID groupId, TextPageLink pageLink) {
//        validateId(groupId, INCORRECT_GROUP_ID + groupId);
//        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = new ArrayList<Device>();
        List<UUID> deviceIds = deviceByGroupIdDao.findDevicesByGroupId(groupId);
        for(UUID deviceId : deviceIds){
            devices.add(deviceDao.findById(deviceId));
        }
        return new TextPageData<>(devices, pageLink);
    }

    //******分配相应设备到对应的设备组******
    @Override
    public Device assignDeviceToGroup(UUID deviceId, UUID groupId) {
        Device device = findDeviceById(deviceId);
        DeviceByGroupId deviceByGroupId = new DeviceByGroupId(groupId,deviceId);
        deviceByGroupIdDao.save(deviceByGroupId);
        return saveDevice(device);
    }

    @Override
    public void unassignDeviceFromGroup(UUID deviceId , UUID groupId){
//        validateId(deviceId, INCORRECT_GROUP_ID + deviceId);
//        validateId(groupId, INCORRECT_GROUP_ID + groupId);
        deviceByGroupIdDao.delete(new DeviceByGroupId(groupId,deviceId));
    }

    //******Unassign设备组中的所有设备******
    @Override
    public void unassignDevicesByGroupId(UUID groupId) {
//        validateId(groupId, INCORRECT_GROUP_ID + groupId);
        deviceByGroupIdDao.deleteAllByGroupId(groupId);
    }

    //******根据parentDeviceId寻找设备******
    @Override
    public List<Device> findDeviceByParentDeviceId(String parentDeviceId, TextPageLink pageLink) {
        return deviceDao.findDevicesByParentDeviceId(parentDeviceId,pageLink);
    }

    @Override
    public Device findDeviceById(UUID deviceId) {
//        validateId(deviceId, INCORRECT_DEVICE_ID + deviceId);
        return deviceDao.findById(deviceId);
    }

    @Override
    public Optional<Device> findDeviceByTenantIdAndName(Integer tenantId, String name) {
//        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        Optional<Device> deviceOpt = deviceDao.findDeviceByTenantIdAndName(tenantId, name);
        if (deviceOpt.isPresent()) {
            return Optional.of(deviceOpt.get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Device saveDevice(Device device) {
//        deviceValidator.validate(device);
        Device savedDevice = deviceDao.save(device);
        //TODO
//        if (device.getId() == null) {
//            DeviceCredentials deviceCredentials = new DeviceCredentials();
//            deviceCredentials.setDeviceId(new DeviceId(savedDevice.getUuidId()));
//            deviceCredentials.setCredentialsType(DeviceCredentialsType.ACCESS_TOKEN);
//            deviceCredentials.setCredentialsId(RandomStringUtils.randomAlphanumeric(20));
//            deviceCredentialsService.createDeviceCredentials(deviceCredentials);
//        }
        return savedDevice;
    }

    @Override
    public Device assignDeviceToCustomer(UUID deviceId, Integer customerId) {
        Device device = findDeviceById(deviceId);
        device.setCustomerId(customerId);
        return saveDevice(device);
    }

    @Override
    public Device unassignDeviceFromCustomer(UUID deviceId) {
        Device device = findDeviceById(deviceId);
        device.setCustomerId(null);
        return saveDevice(device);
    }

    @Override
    public void deleteDevice(UUID deviceId) {
//        validateId(deviceId, INCORRECT_DEVICE_ID + deviceId);
        //TODO
//        DeviceCredentials deviceCredentials = deviceCredentialsService.findDeviceCredentialsByDeviceId(deviceId);
//        if (deviceCredentials != null) {
//            deviceCredentialsService.deleteDeviceCredentials(deviceCredentials);
//        }
        List<UUID> groupIds = deviceByGroupIdDao.findGroupsByDeviceId(deviceId);
        for(UUID groupId : groupIds){
            deviceByGroupIdDao.delete(new DeviceByGroupId(groupId,deviceId));
        }
        deviceDao.removeById(deviceId);
    }

    @Override
    public TextPageData<Device> findDevicesByTenantId(Integer tenantId, TextPageLink pageLink) {
//        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
//        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevicesByTenantId(tenantId, pageLink);
        return new TextPageData<>(devices, pageLink);
    }

    @Override
    public void deleteDevicesByTenantId(Integer tenantId) {
//        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
//        tenantDevicesRemover.removeEntities(tenantId);
    }

    @Override
    public TextPageData<Device> findDevicesByTenantIdAndCustomerId(Integer tenantId, Integer customerId, TextPageLink pageLink) {
//        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
//        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
//        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevicesByTenantIdAndCustomerId(tenantId, customerId, pageLink);
        return new TextPageData<>(devices, pageLink);
    }

    @Override
    public void unassignCustomerDevices(Integer tenantId, Integer customerId) {
//        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
//        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);

//        new CustomerDevicesUnassigner(tenantId).removeEntities(customerId);
    }

}
