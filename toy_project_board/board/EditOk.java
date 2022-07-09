package com.test.toy.board;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/board/editok.do")
public class EditOk extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//EditOk.java
		//1. 인코딩
		//2. 데이터 가져오기(subject, content, seq)
		//3. DB 작업 > DAO 위임 > update
		//4. 결과
		//5. JSP 호출하기
		
		HttpSession session = req.getSession();
		
		
		
		//1.
		req.setCharacterEncoding("UTF-8");
		
		
		
		
		//1.5 새로운 파일을 선택했을 때..
		String path = req.getRealPath("/files");
		int size = 1024 * 1024 * 100;
		
		
		MultipartRequest multi = null;
		
		try {
			
			multi = new MultipartRequest(
											req,
											path,
											size,
											"UTF-8",
											new DefaultFileRenamePolicy()
										);
			
		} catch (Exception e) {
			System.out.println("EditOk.doPost");
			e.printStackTrace();
		}
		
		
		
		
		
		//2. eddit.ok로 부터 받은 데이터 변수에 저장
		String subject = multi.getParameter("subject");
		String content = multi.getParameter("content");
		String seq = multi.getParameter("seq");
		String filename    = multi.getFilesystemName("attach");
		String orgfilename = multi.getOriginalFileName("attach");
		
		
		
		
		
		
		
		
		//3.dto에 넣어줄 새로운 데이터드 가공 스따또
		BoardDTO dto = new BoardDTO();
		
		dto.setSubject(subject);
		dto.setContent(content);
		dto.setSeq(seq);
		
		BoardDAO dao = new BoardDAO();
		

		//기존 파일을 tempdto에 넣어줌
		BoardDTO tempdto = dao.get(seq);
		
		if (tempdto.getFilename() != null && filename != null) {
			//1.기존 파일을 삭제하고 새로운 파일로 교체 할 경우
			//1-1 기존 파일삭제
			File file = new File(path + "\\" + tempdto.getFilename());
			file.delete();
			
			//1-2새 파일 교체
			dto.setFilename(filename);
			dto.setOrgfilename(orgfilename);
		}
		else if(filename ==null && multi.getParameter("delfile").equals("y")) {
			//4. 기존파일만 삭제하고, 새로운 파일은 추가 안하고 싶을 경우 -> 2번보다 위에 놓아야함*****
			File file = new File(path + "\\" + tempdto.getFilename());
			file.delete();
			
		}
		else if(filename == null) {
			//2.기존 파일의 유무와 상관없이 새로운 파일을 추가 안했을 경우(괄호 안에 기존 파일명을 넣어줌)
			dto.setFilename(tempdto.getFilename());
			dto.setOrgfilename(tempdto.getOrgfilename());
		}
		else if(tempdto.getFilename() == null && filename != null) {
			//3. 기존파일이 없는데 새로운 파일을 넣는 경우
			dto.setFilename(filename);
			dto.setOrgfilename(orgfilename);
		}
		//*****여기까지 dto에 모든 데이털르 가공하고 dto에 넣어줌
		
		
		
		
		
		
		
		
		
		int temp = 0;
		
		if (session.getAttribute("auth") == null) {
			temp = 1; //익명 사용자
		} else if (session.getAttribute("auth") != null) { 
			//temp = 1; //실명 사용자
			
			if (session.getAttribute("auth").equals(dao.get(seq).getId())) {
				temp = 2; //글쓴 본인(***)
			} else {
				
				if (session.getAttribute("auth").toString().equals("admin")) {
					temp = 3; //관리자(***)
				} else {
					temp = 4; //타인
				}
				
			}
			
		}
				
		
		
		//4. 위에서 가공한 dto를 업데이트 한 값을 result에 넣어주고, 전달
		int result = 0;
		
		if (temp == 2 || temp == 3) {
			
			result = dao.edit(dto);
		}
		
		
		
		req.setAttribute("result", result);
		req.setAttribute("seq", seq);
		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/editok.jsp");
		dispatcher.forward(req, resp);
	}

}



















