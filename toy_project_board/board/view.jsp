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

</style>
</head>
<body>

	<main>
		<%@ include file="/WEB-INF/views/inc/header.jsp" %>
		<section>
			
			<h2>Board</h2>
			
			
			<table class="table table-bordered vertical">
				<tr>
					<th>제목</th>
					<td>${dto.subject}</td>
				</tr>
				<tr>
					<th>내용</th>
					<td style="height: 300px; vertical-align: middle;">${dto.content}</td>
				</tr>
				<tr>
					<th>이름</th>
					<td>${dto.name}${dto.id}</td>
				</tr>
				<tr>
					<th>날짜</th>
					<td>${dto.regdate}</td>
				</tr>
				<tr>
					<th>읽음</th>
					<td>${dto.readcount}</td>
				</tr>
				<tr>
					<th>번호</th>
					<td>${dto.seq}</td>
				</tr>
				<tr>
					<th>파일</th>
					<c:if test="${not empty dto.orgfilename}">
					<td><a href="/ToyProject/board/download.do?filename=${dto.filename}&orgfilename=${dto.orgfilename}">${dto.orgfilename}</a></td> <!-- 저장된 이름보다 사용자가 올린 이름을 보여주는게 더 자연스러움 -->
					</c:if>
					
					<c:if test="${empty dto.orgfilename}">
					<td>파일 없음</td>
					</c:if>
				</tr>
				<tr>
					<th>태그</th>
					<td><input type="text" name ="tags" readonly></td>
				</tr>
					<tr>
					<th>좋아요/싫어요</th>
					<td style="display: flex;">
						<form method="GET" action="/ToyProject/board/goodbad.do">
						<button class="btn btn-danger">
							<i class="fa-solid fa-heart"></i>
							좋아요 
							<span class="badge badge-primary">${dto.good}</span>
						</button>
						<input type="hidden" name="seq" value="${dto.seq}">
						<input type="hidden" name="good" value="1">
						<input type="hidden" name="bad" value="0">
						</form>
						
						<form method="GET" action="/ToyProject/board/goodbad.do">
						<button class="btn btn-dark">
							<i class="fa-solid fa-heart-crack"></i>
							싫어요
							<span class="badge badge-primary">${dto.bad}</span>
						</button>
						<input type="hidden" name="seq" value="${dto.seq}">
						<input type="hidden" name="good" value="0">
						<input type="hidden" name="bad" value="1">
						</form>
						
					</td>
				</tr>
				
			</table>
			
			<div class="btns">
			
				<input type="button" value="돌아가기" class="btn btn-secondary"
					onclick="location.href='/ToyProject/board/list.do?column=${column}&word=${word}';"> <!-- 검색 상태 유지 -->
					
					
					
				<!-- 로그인이 되어있는 상태면 볼 수 있도록 +어드민 -->	
				<c:if test="${not empty auth}">
				
				<c:if test="${not empty auth || auth == 'admin'}">
				<button class="btn btn-primary"
					onclick="location.href='/ToyProject/board/edit.do?seq=${dto.seq}';">
					<i class="fas fa-pen"></i>
					수정하기
				</button>
				
				<button class="btn btn-primary"
					onclick="location.href='/ToyProject/board/del.do?seq=${dto.seq}';">
					<i class="fas fa-pen"></i>
					삭제하기
				</button>
				</c:if>
				
				<button class="btn btn-primary" type="button"
					onclick="location.href='/ToyProject/board/add.do?reply=1&thread=${dto.thread}&depth=${dto.depth}&isSearch=${isSearch}&column=${column}&word=${word}';">
					<i class="fas fa-pen"></i>
					답변쓰기
				</button>
				</c:if>
				
			</div>
			

			<!-- 댓글 > Ajax 버전 -->
			<form id="addCommentForm">
			<table class="tblAddComment">
				<tr>
					<td>
						<textarea class="form-control" name="content" required></textarea>
					</td>
					<td>
						<button class="btn btn-primary" type="button" 
								onclick="addComment();">
							<i class="fas fa-pen"></i>
							쓰기
						</button>
					</td>
				</tr>
			</table>
			<input type="hidden" name="pseq" value="${dto.seq}">
			</form>
			
			
			<!-- 댓글 출력하는 부분 -->
			<table class="table table-bordered comment">
				<c:forEach items="${clist}" var="cdto">
				<tr>
					<td>
						<div>${cdto.content}</div>
						<div>
							<span>${cdto.regdate}</span>
							<span>${cdto.name}(${cdto.id})</span>
							<c:if test="${cdto.id == auth}">   <!-- 댓글 쓴 사람 아이디 = 세션아이디 가 동일해야만 보이도록!! -->
							<span class="btnspan"><a href="#!" onclick="delcomment(${cdto.seq});">[삭제]</a></span>
							<span class="btnspan"><a href="#!" onclick="editcomment(${cdto.seq});">[수정]</a></span>
							</c:if>
						</div>
					</td>
				</tr>
				</c:forEach>
				
			</table>
			
		</section>
	</main>
	
	
	
	
	<script>
	$('.table.comment td').mouseover(function() {
		$(this).find('.btnspan').show();
	});
	
	$('.table.comment td').mouseout(function() {
		$(this).find('.btnspan').hide();
		
		
	});	
	
	
	
		//삭제하기
		function delcomment(seq) {
			
			
			//event.target =함수가 결려있던 태그인 a태그  -> 전역변수로 빼줘야함 안그러면 event.target의 대상이 달라지는거 주의!
			let tr = $(event.target).parents('tr');
			
			
			if (confirm('delete?')) {
				
				$.ajax({
					
					type: 'POST',
					url: '/ToyProject/board/delcommentajaxok.do',
					data: 'seq=' + seq, //글번호만 서버로 넘겨주어 -> 디비에서 삭제함
					dataType: 'json',
					success: function(result) {
						
						
						if (result.result == "1") {//DB에서 삭제를 끝냈다면?
							
							
							tr.remove();///화면에서도 댓글삭제
							
						} else {
							alert('failed');
						}
						
					},
					error: function(a,b,c) {
						console.log(a,b,c);
					}
					
				});				
				
			}
			
		}
		
		
		
		
		
		
		//댓글수정하기 버튼 누르면!
		let isEdit = false; 
		
		function editcomment(seq) {
			
			if (!isEdit) {
				
				//기존의 댓글을 읽어줌
				const tempStr = $(event.target).parent().parent().prev().text();
				
				//회색 form만듦
				$(event.target).parents('tr').after(temp);
				
				isEdit = true;//동시에 편집창 두개이상 만들어지는거 방지
				
				
				
				//기존에 있는 댓글을 회색 폼으로 넣어줌***여기 다시공부(태그들 다시공부 parents,next,finde etc.)
				$(event.target).parents('tr').next().find('textarea').val(tempStr+"하하하하하하하핳"); 
				$(event.target).parents('tr').next().find('input[name=seq]').val(seq);
			}
			
		}//function editcomment
		
		

		//수정하기 회색 박스 
		const temp = `<tr id='editRow' style="background-color: #CDCDCD;">
						<td>
							<form id="editCommentForm">
							<table class="tblEditComment">
								<tr>
									<td>
										<textarea class="form-control" name="content" required id="txtcontent"></textarea>
									</td>
									<td>
										<button class="btn btn-secondary" type="button" onclick="cancelForm();">
											취소하기
										</button>
										<button class="btn btn-primary" type="button" onclick="editComment();">
											<i class="fas fa-pen"></i>
											수정하기
										</button>
									</td>
								</tr>
							</table>
							
							<input type="hidden" name="seq">
							</form>
						</td>
					</tr>`;

					
		
		//수정하기 박스에서 취소 버튼 눌렀을 경우
		function cancelForm() {
			$('#editRow').remove();
			isEdit = false;
		}
		
		

		
		
		//수정하기 버튼에서 수정하기 눌렀을 경우
		function editComment() {
			
			$.ajax({
				
				type: 'POST',
				url: '/ToyProject/board/editcommentajaxok.do',
				data: $('#editCommentForm').serialize(),
				dataType: 'json',
				success: function(result) {
					
					if (result.result == "1") {
						
						//DB에서 성공적으로 업데이트 했다면수정된 댓글을 화면에 반영하기
						//$('#txtcontent').val()  < 새로 바꾼 내용을 넣어줌 
						$('#editRow').prev().children().eq(0).children().eq(0).text($('#txtcontent').val());
						
						
						//수정이 완료되면 회색박스는 없애줌
						$('#editRow').remove();
						
					} else {
						alert('failed');
					}
					
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
				
			});
			
		}
		
		
		
		
	
		
		//댓글 쓰기(Ajax)
		function addComment() {
			
			$.ajax({
				type: 'POST',
				url: '/ToyProject/board/addcommentajaxok.do',
				data: $('#addCommentForm').serialize(),
				dataType: 'json',
				success: function(result) {
					if (result.result == "1") {
						//성공 > db에 insert가되었다면 새로 작성된 댓글을 목록에 반영하기
						
						
						
						//el이 아니라 template라서 앞에 / 해줘야하는거 주의하자 **
						let temp = `<tr>
										<td>
											<div>\${$('[name=content]').val()}</div>
											<div>
												<span>\${result.regdate}</span> 
												<span>\${result.name}(\${result.id})</span>
												<span class="btnspan"><a href="#!" onclick="delcomment(\${result.seq});">[삭제]</a></span>
												<span class="btnspan"><a href="#!" onclick="editcomment(\${result.seq});">[수정]</a></span>
											</div>
										</td>
									</tr>`;
						
									
						if ($('.comment tbody').length == 0) {
							$('.comment').append('<tbody></tbody>');
						}
						
						$('.comment tbody').prepend(temp);//temp새로 넣어주기(맨위로 :prepend)
						
						
						$('[name=content]').val('');
						
						
						//방금 생성한 댓글에도 한번 더 넣어주기**
						$('.table.comment td').mouseover(function() {
							$(this).find('.btnspan').show();
						});
						
						$('.table.comment td').mouseout(function() {
							$(this).find('.btnspan').hide();
						});
						
						
					} else {
						//실패
						alert('failed');						
					}
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			});
			
		}
		
		
		
		
		//태그*******
		//<td><input type="text" name ="tags" readonly></td>
		let tag = '';
		
		<c:forEach items="${dto.taglist}" var="tag">
			tag += '${tag},';
		</c:forEach>
	
		$('input[name=tags]').val(tag); //태그 내용을 채워줌
	
		
		
		
		
		//태그의 모양을 잡아줌
		const tagify = new Tagify(document.querySelector('input[name=tags]'), {});
		
		tagify.on('click', test);
		
		function test(e) {
			//alert(e.detail.data.value);
			
			//태그를 누르면 -> 리스트로 옮겨지면서 -> 그 태그 내용만 가지고 있는 글이 검색됨
			location.href = '/ToyProject/board/list.do?tag=' + e.detail.data.value;
		}
		
			
	</script>

</body>
</html>


















