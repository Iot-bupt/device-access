package cn.edu.bupt.dao.device;

import cn.edu.bupt.dao.Cassandra.CassandraAbstractModelDao;
import cn.edu.bupt.pojo.Device ;
import org.springframework.stereotype.Component;

import static cn.edu.bupt.dao.ModelConstants.DEVICE_COLUMN_FAMILY_NAME;

/**
 * Created by CZX on 2018/4/17.
 */
@Component
public class CassandraDeviceDao extends CassandraAbstractModelDao<Device> implements DeviceDao{

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
}
