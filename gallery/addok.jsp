<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.test.jsp.DBUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

//1. add.jsp로부터 전송받은 파일업로드
	String path = application.getRealPath("/gallery/images");

	int size = 1024 * 1024 * 100;
	
	String filename = "";
	int result=0;

	try {
		
		MultipartRequest multi = new MultipartRequest(
											request,
											path,   
											size, 	 
											"UTF-8",
											new DefaultFileRenamePolicy()
										);		
		
		
		String subject=multi.getParameter("subject");
		String attach=multi.getFilesystemName("attach");
		
		System.out.println(path);
		
		
		//2.DB와 연결하여 add.jsp로 부터 받은 것들을 db에 저장시킴
		Connection conn=null;
		Statement stat=null;
		PreparedStatement pstat=null;
		
		conn = DBUtil.open();
		//System.out.println(conn.isClosed());-false가 떠야 성공
		
		
		//add.jsp에서 받아온 데이터들 DB에 저장
		String sql="insert into tblGallery(seq,filename,subject,regdate)values(seqAddress.nextVal,?,?,sysdate)";

		pstat=conn.prepareStatement(sql);
		
		pstat.setString(1,attach);
		pstat.setString(2,subject);

		
		result=pstat.executeUpdate();
		
		
	} catch (Exception e) {
		System.out.println(e);		
	}
	
	response.sendRedirect("list.jsp"); //파일 업로드를 하면 list.jsp로 이동
	
	

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file= "/address/inc/asset.jsp" %>
</head>
<style>


</style>
<body>
<!-- template.jsp -->
	<main>
	   <header>
			<%@ include file= "inc/header.jsp" %>
		</header>
		<section>
			 <div class ="section content">


			 </div>
		</section>
	
	</main>
	
	<script>
	
	<%if(result>0){ %>
		location.href="list.jsp";
	<%} else{ %>
		alert("추가실패");
		history.back();
	<%} %>
	
	</script>
</body>
</html>