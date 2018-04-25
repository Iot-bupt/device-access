package cn.edu.bupt.controller;


import cn.edu.bupt.pojo.kv.AttributeKvEntry;
import cn.edu.bupt.service.BaseAttributesService;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attribute")
public class AttributeController extends BaseController{

    @RequestMapping(value="/getAllAttributes/{deviceId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ListenableFuture<List<AttributeKvEntry>> getAllAttributes(@PathVariable("deviceId") UUID deviceId) throws Exception{
        BaseAttributesService baseAttributesService = new BaseAttributesService();
        try{
            ListenableFuture<List<AttributeKvEntry>> attributeKvEntry = baseAttributesService.findAll(deviceId);
            return attributeKvEntry;
        }catch (Exception e){
            return null;
        }
    }


}
