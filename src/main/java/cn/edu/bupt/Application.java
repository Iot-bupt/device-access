package cn.edu.bupt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Administrator on 2018/4/14.
 */
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
@ComponentScan("cn.edu.bupt")
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
