package com.jfzy.base;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfzy.service.OssUserService;
import com.jfzy.service.bo.OssUserBo;
import com.jfzy.service.bo.OssUserTypeEnum;
import com.jfzy.web.vo.SimpleResponseVo;

@Component
@Configuration
public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private OssUserService ossUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;

			AuthCheck authCheck = handlerMethod.getMethodAnnotation(AuthCheck.class);

			if (authCheck == null) {
				authCheck = handlerMethod.getBean().getClass().getAnnotation(AuthCheck.class);
			}

			if (authCheck == null) {
				return true;
			} else {
				processCookie(request);
				AuthInfo info = getInfo(request);
				if (info != null) {
					String[] privileges = authCheck.privileges();

					// 按照权限验证权限
					if (privileges != null && privileges.length > 0) {
						for (int i = 0; i < privileges.length; i++) {
							if (info.getPrivileges().contains(privileges[i])) {
								return true;
							}
						}
					} else if (privileges.length == 0) {// only check login
						return true;
					}
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=utf-8");
					response.getWriter().write(objToJsonString(new SimpleResponseVo(402, "权限不够")));
					response.flushBuffer();
				} else {// 未登陆
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=utf-8");
					response.getWriter().write(objToJsonString(new SimpleResponseVo(401, "未登录")));
					response.flushBuffer();

				}

				return false;
			}
		}

		return true;

	}

	private void processCookie(HttpServletRequest request) {
		if (request.getSession() != null) {
			HttpSession session = request.getSession();
			AuthInfo info = (AuthInfo) session.getAttribute(SessionConstants.SESSION_KEY_AUTH_INFO);
			if (info == null) {
				// recover
				Token t = CookieUtil.getAuthFromCookie(request);
				if (t != null) {
					int userId = t.getUserId();
					if (t.getType() == OssUserTypeEnum.USER.getId()) {
						OssUserBo user = ossUserService.getUserById(userId);
						AuthInfo authInfo = new AuthInfo();
						authInfo.setUserId(userId);
						authInfo.setPrivileges(Arrays.asList(new String[] { user.getRole() }));
						session.setAttribute(SessionConstants.SESSION_KEY_AUTH_INFO, authInfo);
					} else if (t.getType() == OssUserTypeEnum.LAWYER.getId()) {
						AuthInfo authInfo = new AuthInfo();
						authInfo.setPrivileges(Arrays.asList(new String[] { "lawyer" }));
						authInfo.setUserId(userId);
						session.setAttribute(SessionConstants.SESSION_KEY_AUTH_INFO, authInfo);
					}
				}
			}
		}
	}

	private AuthInfo getInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null) {
			AuthInfo info = (AuthInfo) session.getAttribute(SessionConstants.SESSION_KEY_AUTH_INFO);
			return info;
		} else {

			return null;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	private static String objToJsonString(Object obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		try {
			json = objectMapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {

		} catch (JsonMappingException e) {

		} catch (IOException e) {

		}
		return json;
	}
}
