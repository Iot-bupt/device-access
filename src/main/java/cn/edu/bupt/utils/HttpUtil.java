package cn.edu.bupt.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kafka.utils.Json;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2018/5/23.
 */
public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static void main(String[] agrs)throws Exception{
        JsonObject obj = getDeviceServiceDes("bupt","switch","1","control swtich");
        System.out.println(obj);
    }
    public static JsonObject getDeviceServiceDes(String manufacture, String deviceType, String model,String sericeName) throws IOException{
//        String url = "http://172.24.32.167:8000/api/v1/ability/"+manufacture+"/"+deviceType+"/"+model;
        String url = "http://39.104.84.131:8000/api/v1/ability/"+manufacture+"/"+deviceType+"/"+model;
        Request.Builder buider = new Request.Builder()
                .url(url)
                .get() ;
        Request request =  buider.build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            String str = response.body().string();
            JsonArray obj = new JsonParser().parse(str).getAsJsonArray();
            for(JsonElement ele:obj){
               String str1 =  ele.getAsJsonObject().get("abilityDes").getAsString();
               JsonObject o = new  JsonParser().parse(str1).getAsJsonObject();

               if (sericeName.equals(o.get("serviceName").getAsString())){
                    return o;
               }
            }
            return null;
        }else{
            return null;
        }
    }
}
