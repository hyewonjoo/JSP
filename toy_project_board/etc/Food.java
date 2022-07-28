package com.test.toy.etc;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/etc/food.do")
public class Food extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Food.java
		//1. DB 작업 > DAO 위임 > select
		//2. 결과
		//3. JSP 호출하기 + 전달하기
		
		//1. + 2.
		EtcDAO dao = new EtcDAO();
		
		//db에 저장되있던 것들을 list로 담아줌
		ArrayList<FoodDTO> list = dao.listFood();
		
		//3.
		req.setAttribute("list", list);		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/etc/food.jsp");
		dispatcher.forward(req, resp);
	}

}





















