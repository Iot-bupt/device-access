package cn.edu.bupt.service;

import cn.edu.bupt.common.security.DeviceAuthResult;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceCredentals;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Administrator on 2018/4/14.
 */
@Service
public class DeviceAuthServiceImpl implements  DeviceAuthService {
    @Override
    public DeviceAuthResult process(DeviceCredentals credentals) {
        //需要根据具体逻辑修改，此处为了测试通过仅仅做了简单的返回
        return DeviceAuthResult.of(true, UUID.randomUUID().toString(),"errorMsg");
    }

    @Override
    public Optional<Device> findDeviceById(String deviceId) {
        //
        Device d = new Device();
        d.setTenantId(UUID.randomUUID());
        d.setId(UUID.randomUUID());
        return Optional.of(d);
    }
}
