<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.test.jsp.DBUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
	//1.db에 있는 table의 정보드를 전부 select 시킴
	Connection conn = null;
	Statement stat = null;
	ResultSet rs = null;
	
	conn = DBUtil.open();
	
	String sql = "select * from tblTodo order by regdate asc";
	
	stat = conn.createStatement();
	
	//반환값 존재
	rs = stat.executeQuery(sql);

%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Todo</title>
<%@ include file="/todo/inc/asset.jsp" %>
<style>

</style>
</head>
<body>

	<main>
		<h1>Todo <small>List</small></h1>
		
		<table class="table table-bordered list">
			<tr>
				<th>★</th>
				<th>할일</th>
				<th>날짜</th>
			</tr>
			<% 
				while (rs.next()) { 
					String temp = "";
					if (rs.getString("complete").equals("y")) {
						//complete가 y면 -> class=complete를 줘서 밑줄이 그어지게 해줌
						temp = "  class=\"complete\"  ";
					}%>
			<tr<%= temp %>>
				<td><%= rs.getString("priority") %></td>
				<td onclick="change(<%= rs.getString("seq") %>, '<%= rs.getString("complete") %>');"><%= rs.getString("todo") %></td>
				<td><%= rs.getString("regdate") %></td>
			</tr>
			
			
			<% } %>
		</table>
		
		<div class="btns">
			<input type="button" value="등록하기"
				class="btn btn-warning" onclick="location.href='add.jsp';">
		</div>
	</main>
	
	<script>
		//td클릭을 했을 경우 실행되는 함수 -> change.jsp에게 데이터(seq,complete)를 전송
		function change(seq, complete) {
			location.href = 'change.jsp?seq=' + seq + '&complete=' + complete;
		}
	</script>

</body>
</html>
















