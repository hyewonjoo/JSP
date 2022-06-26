<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	

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
		<h1>Todo <small>Add</small></h1>
		<!-- 1.addok.jsp에게 데이터를 전송 -->
		<form method="POST" action="addok.jsp">
		<table class="table table-bordered add">
			<tr>
				<th>할일</th>
				<td><input type="text" name="todo" class="form-control" required></td>
			</tr>	
			<tr>
				<th>중요도</th>
				<td>
					<select name='priority' class='form-control'>
						<option value="A">A</option>
						<option value="B" selected>B</option>
						<option value="C">C</option>
					</select>
				</td>
			</tr>		
		</table>
		
		<div class="btns">
			<input type="button" value=" 돌아가기 "
				class="btn btn-secondary" onclick="location.href='list.jsp';">
			<input type="submit" value="등록하기"
				class="btn btn-warning">
		</div>
		</form>
	</main>
	
	<script>
		
	</script>

</body>
</html>
















