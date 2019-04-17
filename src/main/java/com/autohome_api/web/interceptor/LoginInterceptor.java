package com.autohome_api.web.interceptor;

import com.autohome_api.dto.LoginLogs;
import com.autohome_api.dto.User;
import com.autohome_api.service.LoginLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private LoginLogsService loginLogService;

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String uri = request.getRequestURI();
		if(uri.indexOf("/fileResultReport") != -1) {
			if(user != null) {
				LoginLogs loginlog = new LoginLogs();
				loginlog.setLoginName(user.getName());
				loginlog.setLoginIp(request.getRemoteAddr());
				loginlog.setLoginTime(new Date());
				try {
					loginLogService.insertLoginLogs(loginlog);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		return true;
	}

}
