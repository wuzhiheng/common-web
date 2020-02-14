package com.wonders.commonweb.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonders.commonweb.util.IConstant;
import com.wonders.commonweb.vo.ReturnMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局捕获controller的异常处理
 * 创建日期：2018-1-23下午4:40:49
 * author:wuzhiheng
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
//	@ResponseBody
	public String handler(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.error(e.getMessage());
		e.printStackTrace();
		ReturnMsg returnMsg = new ReturnMsg(IConstant.CODE_UNKNOW, e.getMessage());
		if(!isAjax(request)){
			return "error404";	//如果不是ajax请求，这里只是简单的返回404页面，实际上应该返回一个反馈错误信息的页面
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(new ObjectMapper().writeValueAsString(returnMsg));
		return null;
//		return returnMsg;
	}

	private boolean isAjax(HttpServletRequest request){
		return (request.getHeader("X-Requested-With") != null &&
				"XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
	}

	//处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常，详情继续往下看代码
	@ExceptionHandler(BindException.class)
	@ResponseBody
	public ReturnMsg BindExceptionHandler(BindException e) {
		log.error("{}",e);
//		String message = e.getBindingResult().getAllErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).get();
		String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
		return ReturnMsg.errorTip(message);
	}

	//处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ReturnMsg ConstraintViolationExceptionHandler(ConstraintViolationException e) {
		log.error("{}",e);
//		String message = e.getConstraintViolations().stream().findFirst().map(ConstraintViolation::getMessage).get();
		String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
		return ReturnMsg.errorTip(message);
	}

	//处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ReturnMsg MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
		log.error("{}",e);
//		String message = e.getBindingResult().getAllErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).get();
		String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
		return ReturnMsg.errorTip(message);
	}


}
