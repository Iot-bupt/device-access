package cn.edu.bupt.controller;
import cn.edu.bupt.pojo.kv.*;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@RestController
@RequestMapping("/api/v1/deviceaccess")
public class AttributeController extends BaseController{


    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getAllAttributes')")
    @RequestMapping(value="/attribute/{deviceId}", method = RequestMethod.POST)
    public void addAttribute(@PathVariable("deviceId") String deviceId, @RequestBody String attributes){
            JsonObject object = new JsonParser().parse(attributes).getAsJsonObject();
            long ts = System.currentTimeMillis();
            List<AttributeKvEntry> aKv = new ArrayList<>();
            for(Map.Entry<String,JsonElement> entry:object.getAsJsonObject().entrySet()){
                if(entry.getValue().isJsonPrimitive()){
                    KvEntry kv = parseAttribute(entry);
                    KvEntry e =  new BaseAttributeKvEntry(kv, ts);
                    aKv.add((AttributeKvEntry) e);
                }else{
                    throw new JsonSyntaxException("Can't parse value: " + entry);
                }
            }
        try{
            baseAttributesService.save(toUUID(deviceId), aKv);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static KvEntry parseAttribute(Map.Entry<String, JsonElement> entry) {
        KvEntry e = null ;
        if(entry.getValue().getAsJsonPrimitive().isNumber()){
            if(entry.getValue().getAsString().contains(".")){
                e = new DoubleDataEntry(entry.getKey(),entry.getValue().getAsDouble());
            }else{
                e = new LongDataEntry(entry.getKey(),entry.getValue().getAsLong());
            }
        }else if(entry.getValue().getAsJsonPrimitive().isString()){
            e = new StringDataEntry(entry.getKey(),entry.getValue().getAsString());
        }else if(entry.getValue().getAsJsonPrimitive().isBoolean()){
            e = new BooleanDataEntry(entry.getKey(),entry.getValue().getAsBoolean());
        }else{
            throw new JsonSyntaxException("输入的键值对不满足指定的规范");
        }
        return e;
    }

    //通过设备ID获取全部属性
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getAllAttributes')")
    @RequestMapping(value="/allattributes/{deviceId}", method = RequestMethod.GET)
    public List<AttributeKvEntry> getAllAttributes(@PathVariable("deviceId") String deviceId) throws Exception {
        try{
            ListenableFuture<List<AttributeKvEntry>> attributeKvEntry = baseAttributesService.findAll(toUUID(deviceId));
            List<AttributeKvEntry> ls = attributeKvEntry.get();
            return ls;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //设备ID和属性键获取设备属性（多个）
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getAttributes')")
    @RequestMapping(value="/attributes/{deviceId}/{attributeKeys}", method = RequestMethod.GET)
    public List<AttributeKvEntry> getAttributes(
            @PathVariable("deviceId") String deviceId, @PathVariable("attributeKeys") Collection<String> attributeKeys) throws Exception {
        try {
            ListenableFuture<List<AttributeKvEntry>> listListenableFuture =
                    baseAttributesService.find(toUUID(deviceId), attributeKeys);
            List<AttributeKvEntry> ls = listListenableFuture.get();
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //设备ID和属性键获取设备属性（单个）
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getAttribute')")
    @RequestMapping(value="/attribute/{deviceId}/{attributeKey}", method = RequestMethod.GET)
        public Optional<AttributeKvEntry> getAttribute(
                @PathVariable("deviceId") String deviceId, @PathVariable("attributeKey") String attributeKey) throws Exception {
        try {
            ListenableFuture<Optional<AttributeKvEntry>> optionalListenableFuture =
                    baseAttributesService.find(toUUID(deviceId), attributeKey);
            Optional<AttributeKvEntry> ls = optionalListenableFuture.get();
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //删除属性的键值
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'removeAllAttributes')")
    @RequestMapping(value="/allattributes/{deviceId}/{keys}",method = RequestMethod.DELETE)
    public void removeAllAttributes(
            @PathVariable("deviceId") String deviceId, @PathVariable("keys") String keys) throws Exception{
        try{
            List<String> ls = new ArrayList<>();
            ls.add(keys);
            baseAttributesService.removeAll(toUUID(deviceId), ls);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


