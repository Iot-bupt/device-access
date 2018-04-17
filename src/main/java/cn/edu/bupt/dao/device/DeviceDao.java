package cn.edu.bupt.dao.device;

import cn.edu.bupt.dao.Dao;
import cn.edu.bupt.pojo.Device;

/**
 * Created by CZX on 2018/4/17.
 */
public interface DeviceDao extends Dao<Device> {
    Device save(Device device);
}
