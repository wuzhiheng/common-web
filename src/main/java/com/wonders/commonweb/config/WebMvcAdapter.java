package com.wonders.commonweb.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.File;

/**
 * @Author : wuzhiheng
 * @Description :
 * @Date Created in 下午10:08 2018/4/6
 */
//@Configuration
public class WebMvcAdapter extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File file = new File("background");
        if(!file.exists())
            file.mkdir();
        registry.addResourceHandler("/background/**")
                .addResourceLocations("file:"+file.getAbsolutePath()+"/");
    }
}
