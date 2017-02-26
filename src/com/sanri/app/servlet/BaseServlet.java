package com.sanri.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import sanri.utils.BeanUtil;
import sanri.utils.BeanUtil.MethodFilter;
import sanri.utils.Validate;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	protected abstract String getRootPath();
	private Class<? extends BaseServlet> servletClass = null;
	private Map<String,Method> methodMap = new HashMap<String, Method>();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		String requestURI = req.getRequestURI();
		try {
			String contextPath = this.getServletContext().getContextPath();
			URI uri = new URI(requestURI);
			URI prefix = new URI(contextPath+getRootPath());
//			System.out.println(prefix.relativize(uri));
			String methodName = prefix.relativize(uri).toString();
			Method method = methodMap.get(methodName);
			method.setAccessible(true);
			method.invoke(this, req,resp);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 功能:获取客户端上传的 json<br/>
	 * 创建时间:2016-9-30下午2:41:58<br/>
	 * 作者：sanri<br/>
	 */
	public JSONObject getJson(HttpServletRequest request){
		try {
			String jsonString = IOUtils.toString(request.getInputStream(), "utf-8");
			JSONObject jsonObject = JSONObject.parseObject(jsonString);
			System.out.println(jsonString);
			return jsonObject;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 功能:设置子类用户获取所有子类方法<br/>
	 * 创建时间:2016-9-30下午2:41:10<br/>
	 * 作者：sanri<br/>
	 */
	protected  void setServletClass(Class<? extends BaseServlet> servletClass){
		this.servletClass = servletClass;
		List<Method> allMethod = BeanUtil.findMethod(servletClass, new MethodFilter() {
			@Override
			public boolean accept(Method method, String methodName) {
				if("doGet".equals(methodName) || "doPost".equals(methodName)){
					return false;
				}
				return true;
			}
		});
		if(!Validate.isEmpty(allMethod)){
			for (Method method : allMethod) {
				String methodName = method.getName();
				methodMap.put(methodName, method);
			}
		}
	}
	/**
	 * 
	 * 功能:向客户端写入字符串<br/>
	 * 创建时间:2016-10-3上午12:33:48<br/>
	 * 作者：sanri<br/>
	 */
	protected void writeString (String responeString,HttpServletResponse response){
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(responeString);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			writer.flush();
			writer.close();
		}
	}
}
