package cn.edu.bupt.service;

import cn.edu.bupt.common.security.DeviceAuthResult;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceCredentals;
import cn.edu.bupt.pojo.DeviceCredentials;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/19.
 */
public interface DeviceCredentialsService {

    DeviceCredentials findDeviceCredentialsByDeviceId(UUID deviceId);

    DeviceCredentials updateDeviceCredentials(DeviceCredentials deviceCredentials);

    DeviceCredentials createDeviceCredentials(DeviceCredentials deviceCredentials);

    void deleteDeviceCredentials(DeviceCredentials deviceCredentials);

    DeviceCredentials findDeviceCredentialsByToken(String token);
}
