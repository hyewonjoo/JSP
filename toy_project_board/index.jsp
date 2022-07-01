<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Toy Project</title>
<%@ include file= "/WEB-INF/views/inc/asset.jsp" %>
</head>
<style>

section > h1{
text-align:center;
}

img{
	width:450px;
	margin-left:150px;
	
}

section>h3 {
text-align:center;
}

section>h6 {
text-align:center;
}



</style>
<body>
	
  <main>
  		<%@include file="/WEB-INF/views/inc/header.jsp" %>
		<section>
			<h1>시작페이지</h1>
			<img src="/ToyProject/pic/4812731.png">
			<h3><게시판을 만들어보자></h3>
			<h6>앞으로 추가 될 기능</h6>
			<h6>1.검색 기능</h6>
			<h6>2.답변 수정 기능</h6>
			<h6>3.페이징 기능</h6>
		</section>
	</main>	
	
	<script>
  
	
	
	</script>
</body>
</html>