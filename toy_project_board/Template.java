package com.test.toy;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/template.do")
public class Template extends HttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		//객체생성
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/template.jsp");

		//데이터 전송 - template.jsp에게 
		dispatcher.forward(req, resp);

	}
}