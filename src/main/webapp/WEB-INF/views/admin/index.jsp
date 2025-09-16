<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 메인</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/admin/index.css">
</head>
<!-- 
	날짜 : 2025/09/16
	이름 : 한탁원
	내용 : 관리자 메인 작성
 -->
<body>
	<jsp:include page="./common/header.jsp" />
	<main>
		<div id="main">
			<div id="content-wrap">
				<jsp:include page="./common/aside.jsp" />
				<section id="main-section">
					<div id="title">
						<h1>관리자메인</h1>
						<p>HOME > 관리자 메인
						<p>
					</div>

					<div>
						<h2>집계현황</h2>
						<table border="1">
							<tr>
								<td>
									<img src="" alt="">
								</td>
								<td>
								
									<img src="" alt="">
								</td>
							</tr>
						</table>	
					</div>
					
					<div>
						<h2>운영현황</h2>
						<table border="1">
							<tr>
								<td>입금대기</td>
								<td>배송준비</td>
								<td>취소요청</td>
								<td>교환요청</td>
								<td>반품요청</td>
							</tr>
							<tr>
								<td>주문건수</td>
								<td>주문금액</td>
								<td>회원가입</td>
								<td>방문자 수</td>
								<td>문의 게시물</td>
							</tr>
						</table>
						
						<table border="1">
							<tr>
								<td colspan="2">입금대기</td>
								<td colspan="2">배송준비</td>
								<td colspan="2">취소요청</td>
								<td colspan="2">교환요청</td>
								<td colspan="2">반품요청</td>
							</tr>
							<tr>
								<td colspan="2">주문건수</td>
								<td colspan="2">주문금액</td>
								<td colspan="2">회원가입</td>
								<td colspan="2">방문자 수</td>
								<td colspan="2">문의 게시물</td>
							</tr>
							<tr>
								<td>오늘</td>
								<td>60</td>
								<td>오늘</td>
								<td>60</td>
								<td>오늘</td>
								<td>60</td>
								<td>오늘</td>
								<td>60</td>
								<td>오늘</td>
								<td>60</td>
							</tr>
							<tr>
								<td>어제</td>
								<td>60</td>
								<td>어제</td>
								<td>60</td>
								<td>어제</td>
								<td>60</td>
								<td>어제</td>
								<td>60</td>
								<td>어제</td>
								<td>60</td>
							</tr>
						</table>
					</div>
					
					<div>
						<div>
							<h2>공지사항</h2>
							<table border="1">
							<tr>
								<td>
									[안내] 해외 결제사칭문자주의
								</td>
								<td>
									22.10.31
								</td>
							</tr>
							</table>
						</div>
						<div>
							<h2>고객문의</h2>
							<table border="1">
							<tr>
								<td>
									[안내] 해외 결제사칭문자주의
								</td>
								<td>
									작성자
								</td>
								<td>
									22.10.31
								</td>
							</tr>
							</table>
						</div>
					</div>
				</section>
			</div>
		</div>
	</main>
	<jsp:include page="./common/footer.jsp" />

</body>
</html>