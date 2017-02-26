package com.sanri.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sanri.utils.PathUtil;

import com.alibaba.fastjson.JSONObject;

public class TestJsonServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		String classPath = PathUtil.classPath();
		String pkgPath = PathUtil.pkgPath("sanri.utils");
		String pkgPathRoot = PathUtil.pkgPath("");
		String webAppsPath = PathUtil.webAppsPath();
		String webinfPath = PathUtil.webinfPath();
		
		Map<String,String> hashMap = new HashMap<String, String>();
		hashMap.put("classPath", classPath);
		hashMap.put("pkgPath", pkgPath);
		hashMap.put("webAppsPath", webAppsPath);
		hashMap.put("webinfPath", webinfPath);
		hashMap.put("pkgPathRoot", pkgPathRoot);
		
		String jsonString = JSONObject.toJSONString(hashMap);
		writer.write(jsonString);
		
		writer.flush();
		writer.close();
	}
}
