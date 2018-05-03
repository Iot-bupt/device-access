package cn.edu.bupt.controller;

import cn.edu.bupt.actor.service.DefaultActorService;
import cn.edu.bupt.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class BaseController {

    @Autowired
    protected DeviceService deviceService;

    @Autowired
    protected GroupService groupService;

    @Autowired
    DefaultActorService actorService;

    @Autowired
    BaseAttributesService baseAttributesService;

    @Autowired
    BaseTimeseriesService baseTimeseriesService;

    @Autowired
    DeviceCredentialsService deviceCredentialsService;

//    @Autowired
//    private DeviceAccessErrorResponseHandler errorResponseHandler;

//    @ExceptionHandler(DeviceAccessException.class)
//    public void handleThingsboardException(DeviceAccessException ex, HttpServletResponse response) {
//        errorResponseHandler.handle(ex, response);
//    }

//    DeviceAccessException handleException(Exception exception) {
//        return handleException(exception, true);
//    }
//
//    private DeviceAccessException handleException(Exception exception, boolean logException) {
//        if (logException) {
//            System.err.println(exception.getMessage());
//        }
//
//        String cause = "";
//        if (exception.getCause() != null) {
//            cause = exception.getCause().getClass().getCanonicalName();
//        }
//
//        if (exception instanceof DeviceAccessException) {
//            return (DeviceAccessException) exception;
//        } else if (exception instanceof IllegalArgumentException || exception instanceof IncorrectParameterException
//                || exception instanceof DataValidationException || cause.contains("IncorrectParameterException")) {
//            return new DeviceAccessException(exception.getMessage(), DeviceAccessErrorCode.BAD_REQUEST_PARAMS);
//        } else if (exception instanceof MessagingException) {
//            return new DeviceAccessException("Unable to send mail: " + exception.getMessage(), DeviceAccessErrorCode.GENERAL);
//        } else {
//            return new DeviceAccessException(exception.getMessage(), DeviceAccessErrorCode.GENERAL);
//        }
//    }


    UUID toUUID(String id) {
        return UUID.fromString(id);
    }


    <T> T checkNotNull(T reference) throws Exception{
        if (reference == null) {
            throw new Exception("Requested item wasn't found!");
        }
        return reference;
    }

    void checkParameter(String name, String param) throws Exception {
        if (StringUtils.isEmpty(param)) {
            throw new Exception("Parameter '" + name + "' can't be empty!");
        }
    }

}
