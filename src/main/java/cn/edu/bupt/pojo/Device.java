package cn.edu.bupt.pojo;

import lombok.Data;

/**
 * Created by Administrator on 2018/4/13.
 */
@Data
public class Device {

    private String DeviceId;
    private String tenantId;
    private String customerId;
    private String name;
    private String manufacturer;
    private String deviceType;
    private String model;
    private String parentDeviceId;
    private String status;
    private String location;
}
