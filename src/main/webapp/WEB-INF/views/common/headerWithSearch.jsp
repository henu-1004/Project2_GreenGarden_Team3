<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>header</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/common/headerWithSearch.css">
</head>
<!--
	날짜 : 2025/09/15
    이름 : 한탁원
    내용 : headerWithSearch 초안 작성
-->
<body>
	<header>
		<div id="header-wrap">
			<div id="header-top">
				<a href="/kmarket/member/login.do">로그인</a> <a
					href="/kmarket/member/register.do">회원가입</a> <a
					href="/kmarket/mypage/list.do">마이페이지</a> <a href="#">장바구니</a>
			</div>
			<div id="header-middle">
				<a href="#"><img
					src="${pageContext.request.contextPath}/resources/images/header_logo.png"
					alt="로고1" /></a>
				<form class="search-form">
					<input type="text" placeholder="검색" />
					<button type="submit">
						<img
							src="${pageContext.request.contextPath}/resources/images/search_icon.png"
							alt="돋보기" />
					</button>
				</form>
			</div>
			<div id="header-bottom">
				<a href="#">히트상품</a> <a href="#">추천상품</a> <a href="#">최신상품</a> <a
					href="#">인기상품</a> <a href="#">할인상품</a>
			</div>
		</div>
	</header>
</body>
</html>