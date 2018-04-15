package cn.edu.bupt.service;

import cn.edu.bupt.common.security.DeviceAuthResult;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceCredentals;
import cn.edu.bupt.pojo.DeviceId;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/14.
 */
@Service
public class DeviceAuthServiceImpl implements  DeviceAuthService {
    @Override
    public DeviceAuthResult process(DeviceCredentals credentals) {
        return DeviceAuthResult.of(new DeviceId());
    }

    @Override
    public Optional<Device> findDeviceById(DeviceId deviceId) {
        return Optional.of(new Device());
    }
}
