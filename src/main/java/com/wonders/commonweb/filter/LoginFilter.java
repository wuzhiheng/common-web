package com.wonders.commonweb.filter;

import com.wonders.commonweb.util.CommonUtil;
import com.wonders.commonweb.util.IConstant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 创建日期：2017-12-14下午4:30:18
 * author:wuzhiheng
 */
public class LoginFilter implements Filter {

	private String noFilterPath;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		//System.out.println(request.getRequestURI());
		
		//先判断用户有没有登录
//		FeverReportUser user = CommonUtil.getUser();
		Object user = null;
		if(user == null){
			boolean ok = false;
			for (String string : noFilterPath.split(";")) {
				if(request.getServletPath().matches(".*"+string+"$")){
					ok = true;
					break;
				}
			}
			if(ok){
				arg2.doFilter(arg0, arg1);
			}else{

				String page = (String) request.getSession().getAttribute(IConstant.FORWARD_PAGE);
				if(page == null){
					System.out.println(request.getServletPath());
					request.getSession().setAttribute(IConstant.FORWARD_PAGE,request.getServletPath());
				}
				response.sendRedirect(request.getContextPath()+"/login");
			}
		}else{
			arg2.doFilter(arg0, arg1);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.noFilterPath = arg0.getInitParameter("noFilterPath");
	}


}
