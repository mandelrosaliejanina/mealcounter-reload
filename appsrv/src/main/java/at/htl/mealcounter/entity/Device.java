package at.htl.mealcounter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "M_DEVICE")
public class Device {

    @Id
    @Column(name = "DEVICE_ID")
    public String deviceId;

    public Device(String deviceId) {
        this.deviceId = deviceId;
    }

    public Device() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId='" + deviceId + '\'' +
                '}';
    }

}
