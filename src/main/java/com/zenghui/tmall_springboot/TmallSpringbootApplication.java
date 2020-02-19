package com.zenghui.tmall_springboot;

import com.zenghui.tmall_springboot.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//@SpringBootConfiguration
//@EnableAutoConfiguration
//@ComponentScan
@SpringBootApplication
@EnableCaching      //用于缓存启动
public class TmallSpringbootApplication {
    static {    //检查端口6379是否启动。 6379 就是 Redis 服务器使用的端口。如果未启动，那么就会退出 springboot。
        PortUtil.checkPort(6379,"Redis 服务端",true);
    }
    public static void main(String[] args) {
        SpringApplication.run(TmallSpringbootApplication.class, args);
    }

}
