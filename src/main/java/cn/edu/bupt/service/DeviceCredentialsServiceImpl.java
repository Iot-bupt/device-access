package cn.edu.bupt.service;

import cn.edu.bupt.dao.deviceCredentials.DeviceCredentialsDao;
import cn.edu.bupt.pojo.DeviceCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by CZX on 2018/4/19.
 */
@Service
public class DeviceCredentialsServiceImpl implements DeviceCredentialsService{

    @Autowired
    private DeviceCredentialsDao deviceCredentialsDao;

    @Autowired
    private DeviceService deviceService;

    @Override
    public DeviceCredentials findDeviceCredentialsByDeviceId(UUID deviceId) {
//        validateId(deviceId, "Incorrect deviceId " + deviceId);
        return deviceCredentialsDao.findByDeviceId(deviceId);
    }

    @Override
//    @CacheEvict(cacheNames = DEVICE_CREDENTIALS_CACHE, keyGenerator="previousDeviceCredentialsId", beforeInvocation = true)
    public DeviceCredentials updateDeviceCredentials(DeviceCredentials deviceCredentials) {
        return saveOrUpdare(deviceCredentials);
    }

    @Override
    public DeviceCredentials createDeviceCredentials(DeviceCredentials deviceCredentials) {
        return saveOrUpdare(deviceCredentials);
    }

    private DeviceCredentials saveOrUpdare(DeviceCredentials deviceCredentials) {
//        credentialsValidator.validate(deviceCredentials);
        return deviceCredentialsDao.save(deviceCredentials);
    }

    @Override
//    @CacheEvict(cacheNames = DEVICE_CREDENTIALS_CACHE, key="#deviceCredentials.credentialsId")
    public void deleteDeviceCredentials(DeviceCredentials deviceCredentials) {
        deviceCredentialsDao.removeById(deviceCredentials.getId());
    }



}
