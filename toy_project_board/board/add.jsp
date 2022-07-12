<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Toy Project</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<link rel="stylesheet" href="/ToyProject/asset/css/tagify.css"></link>
<script src="/ToyProject/asset/js/jQuery.tagify.min.js"></script>
<style>
.tags-look .tagify__dropdown__item{
  display: inline-block;
  border-radius: 3px;
  padding: .3em .5em;
  border: 1px solid #CCC;
  background: #F3F3F3;
  margin: .2em;
  font-size: .85em;
  color: black;
  transition: 0s;
}
 
.tags-look .tagify__dropdown__item--active{
  color: black;
}
 
.tags-look .tagify__dropdown__item:hover{
  background: lightyellow;
  border-color: gold;
}
</style>
</head>
<body>

	<main>
		<%@ include file="/WEB-INF/views/inc/header.jsp" %>
		<section>
			
			<h2>Board</h2>
			
			<form method="POST" action="/ToyProject/board/addok.do" enctype="multipart/form-data">
			
			<table class="table table-bordered vertical">
				<tr>
					<th>제목</th>
					<td><input type="text" name="subject" class="form-control" required></td>
				</tr>
				<tr>
					<th>내용</th>
					<td><textarea name="content" class="form-control" required></textarea></td>
				</tr>
				<tr>
					<th>첨부파일</th>
					<td><input type="file" name ="attach" class="form-control"></td>
				</tr>
				<tr>
					<!-- 태그는 required 필요없기때문에 required가 필요하지않다. 주의* -->
					<th>태그</th>
					<td><input type="text" name="tags" class="form-control"></td>
				</tr>
			</table>
			
			<div class="btns">
				<input type="button" value="돌아가기" class="btn btn-secondary"
					onclick="location.href='/ToyProject/index.do';">
				<input type="submit" value="글쓰기" class="btn btn-primary">
			</div>
			<input type="hidden" name="reply" value="${reply}">
			<input type="hidden" name="thread" value="${thread}">
			<input type="hidden" name="depth" value="${depth}">
			
			<input type="submit" value="test">
			</form>
			
		</section>
	</main>
	
	<script>

		$('input[name=tags]').tagify({
			whitelist:['aaa','bbb','ccc'],
			dropdown:{
				classname:'tags-look',
				enabled:0,
				closeOnSelect:false
			}
		});
	


		
		//선생님 코드 보면서 부족한 부분 채우기
		//json = 데이터 전달용 상자 역활
		//프로퍼티명 > 따옴표 필수
		//따옴표 > 쌍 따옴표
		//메소드 추가 불가능
		
		
	</script>

</body>
</html>


















