<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/admin/product/list.css">
</head>
<!-- 
	날짜 : 2025/09/16
	이름 : 한탁원
	내용 : 상품 목록 작성
 -->
<body>
	<jsp:include page="../common/header.jsp" />
	<main>
		<div id="main">
			<div id="content-wrap">
				<jsp:include page="../common/aside.jsp" />
				<section id="main-section">
					<div id="title">
						<h1>관리자메인</h1>
						<p>HOME > 관리자 메인
						<p>
					</div>
				</section>
			</div>
		</div>
	</main>
	<jsp:include page="../common/footer.jsp" />

</body>
</html>