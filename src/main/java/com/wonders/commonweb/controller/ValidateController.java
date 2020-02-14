package com.wonders.commonweb.controller;

import com.wonders.commonweb.vo.StudentVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author : wuzhiheng
 * @Description :
 * @Date Created in 09:54 2020-02-14
 */
@RestController
public class ValidateController {

    @RequestMapping("valid1")
    @ResponseBody
    public Object valid1(@Validated StudentVo studentVo){
        return  studentVo;
    }

    @RequestMapping("valid2")
    @ResponseBody
    public Object valid2(@Valid StudentVo studentVo){
        return  studentVo;
    }

    @RequestMapping("valid3")
    @ResponseBody
    public Object valid3(@Validated @RequestBody StudentVo studentVo){
        return  studentVo;
    }

    @RequestMapping("valid4")
    @ResponseBody
    public Object valid4(@Valid @RequestBody StudentVo studentVo){
        return  studentVo;
    }
}
