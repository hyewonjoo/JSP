<%@page import="com.test.jsp.DBUtil"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	//1.add.jsp로 전송받은 데이터 저장
	request.setCharacterEncoding("UTF-8");

	
	String todo = request.getParameter("todo");
	String priority = request.getParameter("priority");
	
	
	int result = 0;
	
	try {
		//2.db와 연결하여, db의 tbltodo에게 add.jsp로 받은 데이터를 insert 시켜줌		
		Connection conn = null;
		PreparedStatement stat = null;
		
		conn = DBUtil.open();
				
		
		
		String sql = "insert into tblTodo (seq, todo, regdate, priority, complete) values (seqTodo.nextval, ?, default, ?, default)";
		
		stat = conn.prepareStatement(sql);
		
		stat.setString(1, todo);
		stat.setString(2, priority);
		
		//반환값이 없기때문에 stat.executeUpdate()사용
		result = stat.executeUpdate();
		
		
	} catch (Exception e) {
		System.out.println(e);
	}
	
	//3. add가 안된다면 실패를, 되었다면 성공을 alert시킴
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="/todo/inc/asset.jsp" %>
<style>

</style>
</head>
<body>
	
	<script>
		<% if (result > 0) { %>
		
		location.href = 'list.jsp';
		
		<% } else { %>
		
		alert('추가 실패;;');
		history.back();
		
		<% } %> 
	</script>

</body>
</html>
















