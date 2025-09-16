<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>footer</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/common/footer.css">
</head>
<!--
	날짜 : 2025/09/15
    이름 : 한탁원
    내용 : footer 초안 작성
-->
<body>
	<footer>
		<div id="footer-wrap">
			<div id="footer-top">
				<a href="#">회사 소개</a> <a href="#">서비스이용약관</a> <a href="#">개인정보처리방침</a>
				<a href="#">전자금융거래약관</a>
			</div>
			<hr>
			<div>
				<table>
					<tr>
						<td><img
							src="${pageContext.request.contextPath}/resources/images/footer_logo.png"
							alt="footer_logo">
						</td>

						<td>
							<div>
								<h2>(주)케이마켓</h2> <br>
								<p>부산광역시 부산진구 중앙대로 300</p>
								<p>대표이사 : 홍길동</p>
								<p>사업등록번호 : 123-45-12345</p>
							</div>
						</td>

						<td>26층</td>

						<td>
							<h2>고객센터</h2> <br>
							<p>Tel:1234-5678 평일 09:00~18:00)</p>
							<p>전자금융거래 분쟁 담당 : 1234-1234(365일 09:00~18:00)</p>
						</td>
					</tr>
				</table>
			</div>
		</div>

	</footer>
</body>
</html>