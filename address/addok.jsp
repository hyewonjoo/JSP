<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.test.jsp.DBUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

//add.jsp로 받아온 데이터들을 db에 올려주는 역활


//1.인코딩처리
//2.데이터 가져오기
//3.DB작업  - 1:db연결 2:sql 3:종료
//4.마무리(feedback) 작업 -> DB추가가되었다면 성공! 아니면 실패


//1.인코딩
request.setCharacterEncoding("UTF-8");

//2.add.jsp로 부터 받아온 데이터
String name=request.getParameter("name");
String age=request.getParameter("age");
String gender=request.getParameter("gender");
String tel=request.getParameter("tel");
String address=request.getParameter("address");

int result=0;

//3.DB작업 
try{
	DBUtil util = new DBUtil();
	
	Connection conn=null;
	Statement stat=null;
	PreparedStatement pstat=null;
	
	conn =util.open();
	//System.out.println(conn.isClosed());-false가 떠야 성공
	
	
	//add.jsp에서 받아온 데이터들 DB에 저장
	String sql="insert into tblAddress(seq,name,age,address,gender,tel)values(seqAddress.nextVal,?,?,?,?,?)";

	pstat=conn.prepareStatement(sql);
	
	pstat.setString(1,name);
	pstat.setString(2,age);
	pstat.setString(3,address);
	pstat.setString(4,gender);
	pstat.setString(5,tel);
	
	result=pstat.executeUpdate();
}
catch(Exception e){
	System.out.println(e);
}

//4마무리멘트(feedback) ->script :DB에 add.jsp에서 받아온 데이터들이 잘 올라갔는지 확인
	

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