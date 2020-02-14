package com.wonders.commonweb.config;

import com.wonders.commonweb.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : wuzhiheng
 * @Description :
 * @Date Created in 上午11:12 2018/4/20
 */
//@Configuration
public class LoginConfig {

    @Value("${filter.noFilterPath}")
    private String noFilterPath;




    @Bean
    public FilterRegistrationBean loginFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LoginFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("noFilterPath", noFilterPath);
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }



}
