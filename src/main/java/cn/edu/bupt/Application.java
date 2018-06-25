package cn.edu.bupt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Administrator on 2018/4/14.
 */
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
@ComponentScan({"cn.edu.bupt", "cn.edu.bupt.websocket"})
@PropertySource({"classpath:disconf.properties"})
@ImportResource({"classpath:disconf.xml"})//引入disconf
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
