package cn.edu.bupt.exception;

public class DeviceAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    private DeviceAccessErrorCode errorCode;

    public DeviceAccessException() {
        super();
    }

    public DeviceAccessException(DeviceAccessErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public DeviceAccessException(String message, DeviceAccessErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DeviceAccessException(String message, Throwable cause, DeviceAccessErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public DeviceAccessException(Throwable cause, DeviceAccessErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public DeviceAccessErrorCode getErrorCode() {
        return errorCode;
    }
}
