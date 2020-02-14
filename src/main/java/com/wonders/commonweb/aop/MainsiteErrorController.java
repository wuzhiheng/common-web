package com.wonders.commonweb.aop;
/**
 * 创建日期：2017-12-29下午4:29:19
 * author:wuzhiheng
 */
//import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainsiteErrorController implements ErrorController {
    private static final String ERROR_PATH = "error";
    @RequestMapping(value=ERROR_PATH)
    public String handleError(){  
        return "pages/error404";
    }  
    @Override  
    public String getErrorPath() {  
        return ERROR_PATH;  
    }  
} 
