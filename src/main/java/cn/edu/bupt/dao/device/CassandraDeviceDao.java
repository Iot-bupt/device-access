package cn.edu.bupt.dao.device;

import cn.edu.bupt.dao.Cassandra.CassandraAbstractSearchTextDao;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Device ;
import com.datastax.driver.core.querybuilder.Select;
import org.springframework.stereotype.Component;

import java.util.*;

import static cn.edu.bupt.dao.ModelConstants.*;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

/**
 * Created by CZX on 2018/4/17.
 */
@Component
public class CassandraDeviceDao extends CassandraAbstractSearchTextDao<Device> implements DeviceDao{

    @Override
    protected Class<Device> getColumnFamilyClass() {
        return Device.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return DEVICE_COLUMN_FAMILY_NAME;
    }

    @Override
    public Device save(Device device) {
        Device savedDevice = super.save(device);
        return savedDevice;
    }

    @Override
    public List<Device> findDevicesByTenantId(UUID tenantId, TextPageLink pageLink) {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Collections.singletonList(eq(DEVICE_TENANT_ID_PROPERTY, tenantId)), pageLink);
        return devices;
    }

    @Override
    public List<Device> findDevicesByParentDeviceId(String parentDeviceId, TextPageLink pageLink) {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_TENANT_AND_PARENT_DEVICE_ID_COLUMN_FAMILY_NAME,
                Collections.singletonList(eq(DEVICE_PARENT_DEVICE_ID_PROPERTY, parentDeviceId)), pageLink);
        return devices;
    }

    @Override
    public List<Device> findDevicesByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TextPageLink pageLink) {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Arrays.asList(eq(DEVICE_CUSTOMER_ID_PROPERTY, customerId),
                        eq(DEVICE_TENANT_ID_PROPERTY, tenantId)),
                pageLink);
        return devices;
    }

    @Override
    public Optional<Device> findDeviceByTenantIdAndName(UUID tenantId, String deviceName) {
        Select select = select().from(DEVICE_BY_TENANT_AND_NAME_VIEW_NAME);
        Select.Where query = select.where();
        query.and(eq(DEVICE_TENANT_ID_PROPERTY, tenantId));
        query.and(eq(DEVICE_NAME_PROPERTY, deviceName));
        return Optional.ofNullable(findOneByStatement(query));
    }
}
