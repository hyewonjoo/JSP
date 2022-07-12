package com.test.toy.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/board/list.do")
public class List extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//List.java
		//1. DB 작업 > DAO 위임 > select
		//2. 결과
		//3. JSP 호출하기 + 결과 전달하기
		
		
		req.setCharacterEncoding("UTF-8");
		
		//list.jsp의 폼태그로부터 받은 변수 저장
		String column = req.getParameter("column");
		String word = req.getParameter("word");
		String isSearch = "n";
		String tag = req.getParameter("tag");
		
		
		//검색을 안했다면 isSearch n
		if ((column == null || word == null)
				|| (column == "" || word == "")) {
			isSearch = "n";
		} else {
			isSearch = "y"; //검색 상태인경우
		}
		
		

		HashMap<String,String> map = new HashMap<String,String>();  //*************
		
		map.put("column", column);
		map.put("word", word);
		map.put("isSearch", isSearch);
		map.put("tag", tag);
		
		
		
		
		
		
		

		
		
		
		//페이징
		int nowPage = 0; 	//현재 페이지 번호(= page)
		int begin = 0;
		int end = 0;
		int pageSize = 10;	//한페이지 당 출력할 게시물 수
		
		int totalCount = 0; //총 게시물 수
		int totalPage = 0;	//총 페이지 수
		
		
		
		//list.do > list.do?page=1
		//list.do?page=3		
		
		
		//어디서 받은 페이지인가..?(답: list.jsp)
		String page = req.getParameter("page");
		
		if (page == null || page == "") nowPage = 1;
		else nowPage = Integer.parseInt(page);
		
		
		//nowPage > 현재 보게될 페이지 번호
		//list.do?page=1 > where rnum between 1(begin) and 10(end)
		//list.do?page=2 > where rnum between 11 and 20
		//list.do?page=3 > where rnum between 21 and 30
		
		begin = ((nowPage - 1) * pageSize) + 1;
		end = begin + pageSize - 1;
		
		
		map.put("begin", begin + "");
		map.put("end", end + "");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		HttpSession session = req.getSession();
		
		
		
		//1. + 2.
		BoardDAO dao = new BoardDAO();
		
		ArrayList<BoardDTO> list = dao.list(map);//******** map을 넣고 -> 결과값을 10개씩 list뽑아줌!!!!!!!!!****************

		
		
		//2.5
		//- 위에서 return 받은 list들을 가공시킨다.
		Calendar now = Calendar.getInstance();
		String strNow = String.format("%tF", now);// "2022-06-29"
		
		for (BoardDTO dto : list) {
			
			//시분초 자르기
			if (dto.getRegdate().startsWith(strNow)) {
				//오늘
				dto.setRegdate(dto.getRegdate().substring(0,11));
			} else {
				//어제 이전
				dto.setRegdate(dto.getRegdate().substring(0, 10));
			}
			
			
			//제목이 길면 자르기
			if (dto.getSubject().length() > 25) {
				dto.setSubject(dto.getSubject().substring(0, 25) + "..");
			}
			
			//태그 비활성화
			dto.setSubject(dto.getSubject().replace("<", "&lt;").replace(">", "&gt;"));
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//2.6 총 페이지 수 구하기
		//- 총 게시물 수 > 527
		//- 총 페이지 수 > 527 / 10 = 52.7 페이지 > 53페이지 
		totalCount = dao.getTotalCount(map);   //******주의
		totalPage = (int)Math.ceil((double)totalCount / pageSize);
		
		
		String pagebar = "";
		
		int blockSize = 10;	//한번에 보여질 페이지 개수
		int n = 0;			//시작 페이지 번호
		int loop = 0;		//루프
		
		pagebar = "";
		
		
		//list.do?page=1
		//1 2 3 4 5 6 7 8 9 10
		
		//list.do?page=3
		//1 2 3 4 5 6 7 8 9 10
		
		//list.do?page=10
		//1 2 3 4 5 6 7 8 9 10
		
		//list.do?page=11
		//11 12 13 14 15 16 17 18 19 20
		
		
		loop = 1;
		n = ((nowPage - 1) / blockSize) * blockSize + 1;

		
		
		
		pagebar += "<ul class=\"pagination\">";
		
		
		
		//이전페이지
		if (n == 1) {//첫번째 블락인지(1~10) ->클릭못하게 막음
			pagebar += String.format(" <li class=\"page-item\">\r\n"
					+ "		      <a class=\"page-link\" href=\"#!\" aria-label=\"Previous\">\r\n"
					+ "		        <span aria-hidden=\"true\">&laquo;</span>\r\n"
					+ "		      </a>\r\n"
					+ "		    </li> "
					);
		} else {//나머지 클릭 O
			pagebar += String.format(" <li class=\"page-item\">\r\n"
					+ "		      <a class=\"page-link\" href=\"/ToyProject/board/list.do?page=%d\" aria-label=\"Previous\">\r\n"
					+ "		        <span aria-hidden=\"true\">&laquo;</span>\r\n"
					+ "		      </a>\r\n"
					+ "		    </li> "
					, n - 1
					);
		}
		
		
		
		
		
		//가운데 출력되는 페이지 번호
		while (!(loop > blockSize || n > totalPage)) {
			
			if (n == nowPage) {
				pagebar += String.format(" <li class=\"page-item active\"><a class=\"page-link\" href=\"#!\">%d</a></li> "
						, n);
			} else {
				pagebar += String.format(" <li class=\"page-item\"><a class=\"page-link\" href=\"/ToyProject/board/list.do?page=%d\">%d</a></li> "
						, n
						, n);
			}
			
			loop++;
			n++;
		}
		

		
		
		
		
		
		//다음페이지
		if (n > totalPage) {//마지막 블락 ->클릭X
			pagebar += String.format(" <li class=\"page-item\">\r\n"
					+ "		      <a class=\"page-link\" href=\"#!\" aria-label=\"Next\">\r\n"
					+ "		        <span aria-hidden=\"true\">&raquo;</span>\r\n"
					+ "		      </a>\r\n"
					+ "		    </li> "
					);
		} else {//나머지 클릭 O
			pagebar += String.format(" <li class=\"page-item\">\r\n"
					+ "		      <a class=\"page-link\" href=\"/ToyProject/board/list.do?page=%d\" aria-label=\"Next\">\r\n"
					+ "		        <span aria-hidden=\"true\">&raquo;</span>\r\n"
					+ "		      </a>\r\n"
					+ "		    </li> "
					, n
					);
		}
		
		
		pagebar += "</ul>";
		
		
		
		
		
		
		
		
		
		
		
		//list.jsp에게 줄 변수 전달~
		//2.7 새로고침 조회수 증가 방지 ->리스트 페이지에오면 다시 read가 n으로됨 
		session.setAttribute("read", "n");
		

		
		//3.
		req.setAttribute("list", list);
		req.setAttribute("map", map);
		
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("totalPage", totalPage);
		
		req.setAttribute("nowPage", nowPage);
		
		req.setAttribute("pagebar", pagebar);


		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		dispatcher.forward(req, resp);
	}

}



















