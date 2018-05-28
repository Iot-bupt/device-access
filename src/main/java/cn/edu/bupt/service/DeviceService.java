package cn.edu.bupt.service;

import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Device;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface DeviceService {

    Device findDeviceById(UUID deviceId);

    Optional<Device> findDeviceByTenantIdAndName(Integer tenantId, String name);

    Device saveDevice(Device device);

    void assignDeviceToGroup(UUID deviceId, UUID groupId);

    Device assignDeviceToCustomer(UUID deviceId,Integer customerId);

    Device unassignDeviceFromCustomer(UUID deviceId);

    void deleteDevice(UUID deviceId);

    TextPageData<Device> findDevicesByGroupId(UUID groupId, TextPageLink pageLink);

    TextPageData<Device> findDevicesByTenantId(Integer tenantId, TextPageLink pageLink);

    void deleteDevicesByTenantId(Integer tenantId);

    void unassignDevicesByGroupId(UUID groupId);

    void unassignDeviceFromGroup(UUID deviceId, UUID groupId);

    TextPageData<Device> findDevicesByTenantIdAndCustomerId(Integer tenantId, Integer customerId, TextPageLink pageLink);

    void unassignCustomerDevices(Integer tenantId, Integer customerId);

    List<Device> findDeviceByParentDeviceId(String parentDeviceId, TextPageLink pageLink);

    TextPageData<Device> findDevicesByManufactureAndDeviceTypeAndModel(String manufacture, String deviceType, String model, TextPageLink pageLink);

    TextPageData<Device> findDevicesByTenantIdAndSiteId(Integer tenantId, Integer siteId, TextPageLink pageLink);

}
