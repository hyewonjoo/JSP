package com.test.toy.board;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/board/addok.do")
public class AddOk extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//AddOk.java
		//1. 인코딩
		//2. 데이터 가져오기(subject, content)
		//3. DB 작업 > DAO 위임 > insert
		//4. 결과
		//5. JSP 호출하기
		
		HttpSession session = req.getSession();
		
		//1.
		req.setCharacterEncoding("UTF-8");
		
		
		//1.5 파일업로드
		String path =req.getRealPath("/files");
		int size = 1024*1024*100;
		
		MultipartRequest multi = null;
		
		
		try {
			multi=new MultipartRequest(
					req,
					path,
					size,
					"UTF-8",
					new DefaultFileRenamePolicy()				
					);
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//업로드 된 파일처리
		String filename=multi.getFilesystemName("attach");
		String orgfilename=multi.getOriginalFileName("attach");
		
		
		
		//2. 
		String subject = multi.getParameter("subject");
		String content = multi.getParameter("content");
		String reply=multi.getParameter("reply"); //현재 새글 작성중인지? 답변글 작성중인지?
		
		
		int thread =-1;
		int depth=-1;
		
		
		BoardDAO dao = new BoardDAO();
		
		
		if(reply.equals("")) {
		//새글=reply가 넘어오지 않음->list.do에서 새글 쓰기
		//현존하는 게시글 중에서 가장 큰 스레드 값을 찾아서 그 값에 1000한 값을
		//스레드에 넘어줌
		thread=dao.getMaxTread() +1000;
		
		depth=0;
		
		}else {
		//답변글=reply=1가 넘어와서 null이 아니게됨
		int parentThread = Integer.parseInt(multi.getParameter("thread"));
		int parentDepth =Integer.parseInt(multi.getParameter("depth"));
	   
		//이전 새글의 스레드
		int previousThread =(int)Math.floor((parentThread-1)/1000)*1000;
		
		
		
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		map.put("parentThread",parentThread);
		map.put("previousThread", previousThread);
		
		
		
		
		//thread와 depth가 업데이트가 됨
		dao.updateThread(map);
		
		thread=parentThread-1;
		depth=parentDepth+1;
		
		
		}
		
		
	

		
		//3. 가공한 데이터들을 dto에 포장해서 넣어주기
		BoardDTO dto = new BoardDTO();
		
		dto.setSubject(subject);
		dto.setContent(content);
		//지금 접속해 있는 사람의 id를 가져옴
		dto.setId((String)session.getAttribute("auth"));
		
		dto.setThread(thread);
		dto.setDepth(depth);
		
		dto.setFilename(filename);
		dto.setOrgfilename(orgfilename);		
		
		
		
		
		
		//4.
		int result =0;
		
		//제대로 된 경로로 로그인 했을 때만 db에 업로드 할 수 있도록 함.
		if(session.getAttribute("auth") !=null) {
		 result=dao.add(dto);
		}
		
		
	
		
		
		
		//4.5 해쉬태그 작업
		String tags=multi.getParameter("tags");
		
		
		//방금 쓴 글번호 
		String seq=dao.getSeq();
		
		//태그관련 json이용하여 값 가져오기
		JSONParser parser = new JSONParser();
		
		try {
			
			//JSONArray list;
			//JSONObject obj;
			JSONArray list = (JSONArray)parser.parse(tags);
			System.out.println(list);
			
			for(Object obj:list) {
				//태그의 각각 값들을  for문을 돌면서, String tag에 넣어줌
				String tag = (String)((JSONObject)obj).get("value");
				dao.addHashTag(tag);
				
				//해쉬테그 테이블의 태그 번호를 hseq에 넣어줌
				String hseq =dao.getHashTagSeq();
				
				HashMap<String, String> map = new HashMap<String, String>();
				//글번호
				map.put("bseq", seq);
				//태그번호
				map.put("hseq", hseq);
				
				dao.addTagging(map);
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		req.setAttribute("result", result);		
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/board/addok.jsp");
		dispatcher.forward(req, resp);
	}

}



















