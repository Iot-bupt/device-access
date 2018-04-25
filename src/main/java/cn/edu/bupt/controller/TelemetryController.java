package cn.edu.bupt.controller;


import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.service.BaseTimeseriesService;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/data")
public class TelemetryController extends BaseController {

    @RequestMapping(value = "/getlatestData/{deviceId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ListenableFuture<List<TsKvEntry>> getlatestData(@PathVariable("deviceId") UUID deviceId)
    throws Exception{
        BaseTimeseriesService baseTimeseriesService = new BaseTimeseriesService();
        try{
            ListenableFuture<List<TsKvEntry>> tskventry = baseTimeseriesService.findAllLatest(deviceId);
            return tskventry;
        }catch(Exception e){
            return null;
        }
    }


}