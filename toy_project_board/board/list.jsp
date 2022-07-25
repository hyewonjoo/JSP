<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Toy Project</title>
<%@ include file="/WEB-INF/views/inc/asset.jsp" %>
<style>

</style>
</head>
<body>

	<main>
		<%@ include file="/WEB-INF/views/inc/header.jsp" %>
		<section>
			
			<h2>Board</h2>
			<div style="text-align :center; margin-bottom:5px; color:cornflowerblue;">
			    총 ${list.size()}의 결과물이 발견되었습니다.
			</div>
			
			<table class="table table-bordered horizontal">
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>이름</th>
					<th>날짜</th>
					<th>읽음</th>
				</tr>
				
				<!-- list안에하나씩 꺼내어 dto변수에 담는다 -->
				<c:forEach items="${list}" var="dto">
				<tr>
					<td>${dto.seq}</td>
					<td>
						<c:if test="${dto.depth > 0}"> <!-- 답변글이라면 -->
						<span style="margin-left: ${dto.depth * 20}px;">→</span>
						</c:if>	
						
					
						<a href="/ToyProject/board/view.do?seq=${dto.seq}&isSearch=${map.isSearch}&column=${map.column}&word=${map.word}">${dto.subject}</a>
			
			
						<c:if test="${(dto.isnew * 24) < 3 }">
						<span style="color: tomato;">new</span>
						</c:if>
					
						<c:if test="${dto.commentcount>0 }">
						<span class ="badge badge-primary">${dto.commentcount}</span>
						</c:if>
						
						
						<c:if test="${not empty dto.filename}">
	<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-arrow-down" viewBox="0 0 16 16">
  <path d="M8.5 6.5a.5.5 0 0 0-1 0v3.793L6.354 9.146a.5.5 0 1 0-.708.708l2 2a.5.5 0 0 0 .708 0l2-2a.5.5 0 0 0-.708-.708L8.5 10.293V6.5z"/>
  <path d="M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2zM9.5 3A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.5v2z"/>
</svg>
						</c:if>
						
						
					</td>
					<td>${dto.name}</td>
					<td>${dto.regdate}</td>
					<td>${dto.readcount}</td>
				</tr>
				</c:forEach>
							
							
				<c:if test="${list.size()==0}">
				<tr>
					<td colspan="5">게시물이 없습니다.</td>
				</tr>
				</c:if>
			</table>
			
		
			<!-- 페이징 -->
			<div style="text-align: center;">
				${pagebar}
			</div>
			
			
			<div>
				<!-- 검색하는 용도로 만든 폼은 주로 겟방식으로 폼을 날림 -->
				<form method ="GET" action="/ToyProject/board/list.do">
				<table class="search">
					<tr>
						<td>
							<select name="column">
								<option value="subject">제목</option>
								<option value="content">내용</option>
								<option value="name">이름</option>
							</select>
						</td>
						<td>
							<input type="text" name="word" class="form-control" required>
						</td>
						<td>
							<button class="btn btn-primary">검색하기</button>
							<c:if test="${map.isSearch == 'y'}">
							<button class="btn btn-secondary" type="button"
								onclick="location.href='/ToyProject/board/list.do';">
								중단하기
							</button>
							</c:if>
						</td>
					</tr>
				</table>
				</form>
			</div>
			
			
			<!-- 로그인이 되어있는 상태면 list페이지에서 쓰기 버튼을 보여줌 -->
			<c:if test="${not empty auth}">
			<div class="btns">
				<button type="button" class="btn btn-primary"
					onclick="location.href='/ToyProject/board/add.do';">
					<i class="fas fa-pen"></i>
					쓰기
				</button>
			</div>
			</c:if>
			
			
			
		</section>
	</main>
	
	<script>
	//검색창에 검색기록 남아있도록 상태유지
	<c:if test="${map.isSearch == 'y'}">
	$('select[name=column]').val('${map.column}');
	$('input[name=word]').val('${map.word}');
	</c:if>
	
	
	$("#pagebar").change(function() {
		//$(this).val() :누른 페이지 태그
		location.href = '/ToyProject/board/list.do?page=' + $(this).val() + "&column=${map.column}&word=${map.word}";
	});
	
	
	
	$('#pagebar').val(${nowPage});
	</script>

</body>
</html>


















