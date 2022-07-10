package com.test.toy.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/board/goodbad.do")
public class GoodBad extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//GoodBad.java
		//1. 데이터 가져오기
		//2. DB 작업 > DAO 위임 > update
		//3. 결과
		//4. 피드백
		
		
		HttpSession session = req.getSession();
		
		//1.
		String seq = req.getParameter("seq");
		String good = req.getParameter("good");
		String bad = req.getParameter("bad");
		
		
		
		//2. + 3
		BoardDAO dao = new BoardDAO();
		
		GoodBadDTO dto = new GoodBadDTO();
		
		dto.setBseq(seq); //seq를 넣어야하는거 주의 *****
		dto.setGood(good);
		dto.setBad(bad);
		dto.setId((String)session.getAttribute("auth"));
		
		
		
		//result에 dto를 업데이트한 값(db업로드 성공 유무)을 넣어줌 
		int result = dao.updateGoodBad(dto);

		
		//4.
		if (result == 1) {
		
			resp.sendRedirect(String.format("/ToyProject/board/view.do?seq=%s", seq));
			
		} else {
			
			PrintWriter writer = resp.getWriter();
			
			writer.println("<html>");
			writer.println("<body>");
			writer.println("<script>");
			writer.println("alert('failed');");
			writer.println("history.back();");
			writer.println("</script>");
			writer.println("</body>");
			writer.println("</html>");
			
			writer.close();			
		}
		
		
	}

}




















