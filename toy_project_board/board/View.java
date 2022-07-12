package com.test.toy.board;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/board/view.do")
public class View extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//View.java
		//1.데이터를 가져오세요
		//2.DB작업 > DAO위임 > select
		//3.결과
		//4.JSP 호출하기 + 결과 전달하기
		
		HttpSession session=req.getSession();
		
		//1. 뷰에 접근하려면 seq를 전달해야함!!
		String seq=req.getParameter("seq");
		String isSearch = req.getParameter("isSearch");
		String column = req.getParameter("column");
		String word = req.getParameter("word");
		
		
		//2.+3
		BoardDAO dao = new BoardDAO();
		
		
		
		
		//3.2 조회수증가(새로고침할대는 조회수가 증가 할 수 없도록)
		if(session.getAttribute("read")== null||
				session.getAttribute("read").toString().equals("n")) {
		dao.updateReadcount(seq); //조회수 한개 올라감
		session.setAttribute("read", "y");
		}
		
		
		
		//db에 저장된 좋아요 싫어요 관련 정보들을 -> dao.get(tempdto)를 통해 dto에 담아줌 -> 나중에 출력용 
		BoardDTO tempdto = new BoardDTO();
		tempdto.setSeq(seq);
		tempdto.setId((String)session.getAttribute("auth"));
		
		BoardDTO dto = dao.get(tempdto); //***********태그까지 같이 돌려줌**

		
		
		
		
		
		
		//3.5 -view.jsp출력 데이터 조작 : 위에서 반환받은 dto를 가공시켜줌
		//- 태그 비활성화
		dto.setSubject(dto.getSubject().replace("<", "&lt;").replace(">", "&gt;"));
		dto.setContent(dto.getContent().replace("<", "&lt;").replace(">", "&gt;"));
		
		//- 출력 데이터 조작하기
		dto.setContent(dto.getContent().replace("\r\n", "<br>"));
		
		
		//- 검색어 표시하기
		if (isSearch.equals("y") && column.equals("content")||isSearch.equals("y") && column.equals("subject")) {
			
			//안녕하세요. 홍길동입니다.
			//안녕하세요. <span style="background-color:yellow;color:red;">홍길동</span>입니다.
			
			dto.setContent(dto.getContent().replace(word, "<span style=\"background-color:yellow;color:red;\">" + word + "</span>"));
			dto.setSubject(dto.getSubject().replace(word, "<span style=\"background-color:yellow;color:red;\">" + word + "</span>"));
		}
		
		
		
		//첨부파일이 이미지 > 내용과 함께 출력
		if (dto.getFilename() != null
				&& (dto.getFilename().toLowerCase().endsWith(".jpg")
					|| dto.getFilename().toLowerCase().endsWith(".jpeg")
					|| dto.getFilename().toLowerCase().endsWith(".gif")
					|| dto.getFilename().toLowerCase().endsWith(".png"))) {
			
			
		dto.setContent(dto.getContent()+
					String.format("<div style='margin-top:15px;'><img src='/ToyProject/files/%s' id='imgAttach' "
							+ "margin-top:30px;></div>",dto.getFilename()));
	
		}
		
		
		
		//3.7 댓글 목록 가져오기
		ArrayList<CommentDTO> clist = dao.listComment(seq);//*************
		

		
		//4.가공된 dto를 돌려줌
		req.setAttribute("dto", dto);
		
		//댓글
		req.setAttribute("clist", clist);
		
		
		//return된 dto에 들어가있지않아서 따로 보내줘야함(상태유지)
		req.setAttribute("isSearch", isSearch);
		req.setAttribute("column", column);
		req.setAttribute("word", word);
		

		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/view.jsp");
		dispatcher.forward(req, resp);
	}

}



















