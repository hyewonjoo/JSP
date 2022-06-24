<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.test.jsp.DBUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

//1.데이터 가져오기(?seq=5)
//2.DB작업 > select를 해야함 (where seq=5) >기존 사람의 내역이 자동으로 채워져야함
//3. 결과 > 폼태그 기본값 설정하기
//4. 수정해놓은 값 >editok.jsp로 전송



//1.
String seq=request.getParameter("seq");

//2.
DBUtil util=new DBUtil();

Connection conn=null;
PreparedStatement stat=null;
ResultSet rs= null;


//2-1 db연결
conn=util.open();

String sql="select*from tblAddress where seq=?";

stat=conn.prepareStatement(sql);
stat.setString(1,seq);//물음표 채워넣기

rs=stat.executeQuery(); //rs에 반환값(레코드값) 담기

//rs값 꺼내놓기
String name="";
String age="";
String gender="";
String tel="";
String address="";

while(rs.next()){
	name=rs.getString("name");
	age=rs.getString("age");
	gender=rs.getString("gender");
	tel=rs.getString("tel");
	address=rs.getString("address");
}


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
<!-- template.jsp >add.jsp >edit.jsp -->
	<main>
	   <header>
			<%@ include file= "inc/header.jsp" %>
		</header>
		<section>
			 <div class ="section content">
			 	<form method="POST" action="editok.jsp">
			       <table class="table table-bordered">
				 		<tr>
				 			<th>이름</th>
				 			<td><input type="text" name="name" required class="form-control short" ></td>
				 		</tr>
				 		
				 		<tr>
				 			<th>나이</th>
				 			<td><input type="text" name="age" required min="18" max="120" class="form-control short" ></td>
				 		</tr>
				 		
				 		
				 		<tr>
				 			<th>성별</th>
				 			<td>
				 				<select name="gender" class="form-control short">
				 					<option value="m">남자</option>
				 					<option value="f">여자</option>
				 				</select>
				 			</td>
				 		</tr>
				 		
				 		
				 		<tr>
				 			<th>전화</th>
				 			<td><input type="text" name="tel" required class="form-control middle"></td>
				 		</tr>
				 		
				  		<tr>
				 			<th>주소</th>
				 			<td><input type="text" name="address" required class="form-control"></td>
				 		</tr>
				 
				 	</table>
				 	<div class ="btns">
				 	    <input type="button" value="돌아가기" class="btn btn-secondary" onclick="location.href='/JSPTest/address/list.jsp';">
				 		<input type="submit" value="추가하기" class="btn btn-primary">
				 	</div>
				 	
				 	<!-- 히든태그 :editok.jsp에게 넘겨줄 seq를 담고있음 -->
				 	<input type="hidden" name="seq" value="<%= seq %>">
			 	</form>
			 	
			 	
			
			 </div>
		</section>
	
	</main>
	
	<script>
	$('input[name=name]').val('<%=name %>');
	$('input[name=age]').val('<%=age %>');
	$('input[name=gender]').val('<%=gender %>');
	$('input[name=tel]').val('<%=tel %>');
	$('input[name=address]').val('<%=address %>');
	
	</script>
</body>
</html>