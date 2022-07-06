package com.test.toy.board;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/board/add.do")
public class Add extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Add.java
		
		//add.do>null
		//add.do?reply=1 > 1
		//add.do?reply= > ""
		
		
		//질문
		//list에서 add.do로 갈때는 reply,thread,depth 전송안하는데 그 부분은 오류가 아닌지..?
		String reply = req.getParameter("reply");
		String thread= req.getParameter("thread");
		String depth= req.getParameter("depth");
		
		
		
		
		BoardDAO dao = new BoardDAO();
		
		//해쉬테그 목록 가여오기 - 자동완성을 위해서
		ArrayList<String> taglist =dao.taglist();
		
		

		req.setAttribute("reply", reply);
		req.setAttribute("thread", thread);
		req.setAttribute("depth", depth);
		req.setAttribute("taglist", taglist);
		
	
		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/add.jsp");
		dispatcher.forward(req, resp);
	}

}



















