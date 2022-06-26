<%@page import="com.test.jsp.DBUtil"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
	
	
	//list.jsp로부터 받은 데이터 저장
	String seq = request.getParameter("seq");
	String complete = request.getParameter("complete");
	
	
	//complete가 n일경우 -> y로 바꿔줌
	if (complete.equals("n")) {
		complete = "y";
	} else {
		
	//complete가 y일 경우 -> n로 바꿔줌
		complete = "n";
	}
	
	int result = 0;
	
	try {
		
		Connection conn = null;
		PreparedStatement stat = null;
		
		conn = DBUtil.open();
		
		
		
		//db에도 수정된 값(complete)을 업로드 해줌
		String sql = "update tblTodo set complete = ? where seq = ?";
		
		stat = conn.prepareStatement(sql);
		
		stat.setString(1, complete);
		stat.setString(2, seq);
		
		result = stat.executeUpdate();
		
		
	} catch (Exception e) {
		System.out.println(e);
	}

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
		//수정이 완료되면 list.jsp로 이동
		location.href = 'list.jsp';
		
		<% } else { %>
		
		alert('수정 실패;;');
		history.back();
		
		<% } %> 
	</script>

</body>
</html>
















