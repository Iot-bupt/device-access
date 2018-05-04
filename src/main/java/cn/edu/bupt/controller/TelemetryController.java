package cn.edu.bupt.controller;


import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.pojo.kv.TsKvQuery;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/data")
public class TelemetryController extends BaseController {

    @RequestMapping(value="alldata/{deviceId}/{queries}",method = RequestMethod.GET)
    public List<TsKvEntry> getAllData(@PathVariable("deviceId") String deviceId,
                                      @PathVariable("queries") List<TsKvQuery> queries) throws Exception {
        try{
            ListenableFuture<List<TsKvEntry>> listListenableFuture = baseTimeseriesService.findAll(toUUID(deviceId),queries);
            List<TsKvEntry> ls = listListenableFuture.get();
            return ls;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/alllatestdata/{deviceId}", method = RequestMethod.GET)
    public List<TsKvEntry> getlatestData(@PathVariable("deviceId") String deviceId)
    throws Exception{
        try{
            ListenableFuture<List<TsKvEntry>> tskventry = baseTimeseriesService.findAllLatest(toUUID(deviceId));
            List<TsKvEntry> ls = tskventry.get();
            return ls;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @RequestMapping(value = "/latestdata/{deviceId}/{keys}", method = RequestMethod.GET)
    public List<TsKvEntry> getlatestData(@PathVariable("deviceId") String deviceId
    ,@PathVariable("keys") Collection<String> keys)
            throws Exception{
        try{
            ListenableFuture<List<TsKvEntry>> tskventry = baseTimeseriesService.findLatest(toUUID(deviceId), keys);
            List<TsKvEntry> ls = tskventry.get();
            return ls;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}