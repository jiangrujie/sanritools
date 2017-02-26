package com.sanri.app.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sanri.image.code.VerifyCodeUtils;


public class AuthImage extends HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		//生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		//存入会话session
		HttpSession session = request.getSession(true);
		session.setAttribute("rand", verifyCode.toLowerCase());
		//生成图片
		int w = 200, h = 80;
		VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);

	}
}