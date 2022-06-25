<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.test.jsp.DBUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%


//1.del.jsp로부터 데이터 가져오기
//2.DB에서 삭제해주기 
//3.마무리(feedback) 작업 -> DB추가가되었다면 성공! 아니면 실패



//1.del.jsp로부터 seq받아오기
String seq=request.getParameter("seq");


//2.DB작업 
int result=0;
try{
	DBUtil util = new DBUtil();
	
	Connection conn=null;
	Statement stat=null;
	PreparedStatement pstat=null;
	
	conn =util.open();
	//System.out.println(conn.isClosed());-false가 떠야 성공
	
	
	
	String sql="delete from tblGallery where seq=?";

	pstat=conn.prepareStatement(sql);
	
	pstat.setString(1,seq);

	
	result=pstat.executeUpdate(); //성공하면 result 1 -> 실패 result-0
}
catch(Exception e){
	System.out.println(e);
}

//3마무리멘트(feedback) ->script :삭제가 되었으면 1, 실패했으면 0
	

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
		alert("수정실패");
		history.back();
	<%} %>
	
	</script>
</body>
</html>