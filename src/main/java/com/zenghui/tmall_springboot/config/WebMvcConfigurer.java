package com.zenghui.tmall_springboot.config;

import com.zenghui.tmall_springboot.interceptor.LoginInterceptor;
import com.zenghui.tmall_springboot.interceptor.OtherInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {     //配置适配器

    //方法上打上注解 @Bean即表示声明该方法返回的实例是受 Spring 管理的 Bean。IOC控制反转,
    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }

    @Bean
    public OtherInterceptor getOtherInterceptor(){
        return new OtherInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getOtherInterceptor())
                .addPathPatterns("/**");
       registry.addInterceptor(getLoginInterceptor())
               .addPathPatterns("/**");
    }
}
