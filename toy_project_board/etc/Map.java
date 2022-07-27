package com.test.toy.etc;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/etc/map.do")
public class Map extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String no =req.getParameter("no");
		//객체생성
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/etc/map0"+no+".jsp");

		//데이터 전송 - template.jsp에게 
		dispatcher.forward(req, resp);

	}
}