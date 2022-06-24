<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

//1.데이터 가져오기(?seq=5)
//2.delok.jsp?seq=5 전달하기 -> 진짜 삭제는 del.ok에서 삭제


//1
String seq=request.getParameter("seq");


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
			 	<div class ="btns">
				 	    <input type="button" value="돌아가기" class="btn btn-secondary" onclick="location.href='/JSPTest/address/list.jsp';">
				 		<input type="button" value="삭제하기" class="btn btn-primary" onclick="location.href='/JSPTest/address/delok.jsp?seq=<%=seq %>';">
				 	</div>
			 </div>
		</section>
	
	</main>
	
	<script>
	
	
	</script>
</body>
</html>