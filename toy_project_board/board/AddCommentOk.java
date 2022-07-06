package com.test.toy.board;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/board/addcommentok.do")
public class AddCommentOk extends HttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		
		HttpSession session = req.getSession();
		//1.
		req.setCharacterEncoding("UTF-8");
		
		//2.
		String content =req.getParameter("content");
		String pseq=req.getParameter("pseq");
		
		//3.
		CommentDTO dto = new CommentDTO();
		
		
		
		BoardDAO dao=new BoardDAO();
		
		dto.setContent(content);
		dto.setPseq(pseq);
		dto.setId((String)session.getAttribute("auth"));
		
		int result = dao.addComment(dto);
	
		
		req.setAttribute("result", result);		
		req.setAttribute("pseq", pseq);
		
		//검색관련된 데이터들은 나중에 검색 기능 만들고 추가해주기
		
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/addcomment.jsp");
		dispatcher.forward(req, resp);
		

		
		


	}
}
