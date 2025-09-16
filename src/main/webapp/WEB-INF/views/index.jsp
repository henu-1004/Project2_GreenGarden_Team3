<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>k-마켓</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/index/index.css">
</head>
<body>
	<main>
		<div id="main">
			<section id="banner">
				<img
					src="${pageContext.request.contextPath}/resources/images/slider_item1.jpg"
					alt="슬라이드">
			</section>
			<jsp:include page="./common/headerWithSearch.jsp" />


			<div class="content-wrap">
				<jsp:include page="./common/aside_main.jsp" />
				<section id="main-section">
					<img
						src="${pageContext.request.contextPath}/resources/images/slider_item1.jpg"
						alt="슬라이드">
					<div class="product">
						<h2>히트상품</h2>
						<table>
							<tr>
								<c:forEach var="i" begin="1" end="7" varStatus="st">
									<td align="center"><img
										src="${pageContext.request.contextPath}/resources/images/sample_thumb.jpg"
										alt="상품">
										<p>상품명</p> <span><a>30,000원</a><a>10%</a></span><br> <span><a>27,000원</a>
										<button>무료배송</button></span></td>

									<!-- 4개마다 줄바꿈 -->
									<c:if test="${st.count % 4 == 0}">
							</tr>
							<tr>
								</c:if>
								</c:forEach>
							</tr>
						</table>
					</div>
					<div class="product">
						<h2>추천상품</h2>
						<table>
							<tr>
								<c:forEach var="i" begin="1" end="7" varStatus="st">
									<td align="center"><img
										src="${pageContext.request.contextPath}/resources/images/sample_thumb.jpg"
										alt="상품">
										<p>상품명</p> <span><a>30,000원</a><a>10%</a></span><br> <span><a>27,000원</a>
										<button>무료배송</button></span></td>

									<!-- 4개마다 줄바꿈 -->
									<c:if test="${st.count % 4 == 0}">
							</tr>
							<tr>
								</c:if>
								</c:forEach>
							</tr>
						</table>
					</div>
					<div class="product">
						<h2>최신상품</h2>
						<table>
							<tr>
								<c:forEach var="i" begin="1" end="7" varStatus="st">
									<td align="center"><img
										src="${pageContext.request.contextPath}/resources/images/sample_thumb.jpg"
										alt="상품">
										<p>상품명</p> <span><a>30,000원</a><a>10%</a></span><br> <span><a>27,000원</a>
										<button>무료배송</button></span></td>

									<!-- 4개마다 줄바꿈 -->
									<c:if test="${st.count % 4 == 0}">
							</tr>
							<tr>
								</c:if>
								</c:forEach>
							</tr>
						</table>
					</div>
					<div class="product">
						<h2>할인상품</h2>
						<table>
							<tr>
								<c:forEach var="i" begin="1" end="7" varStatus="st">
									<td align="center"><img
										src="${pageContext.request.contextPath}/resources/images/sample_thumb.jpg"
										alt="상품">
										<p>상품명</p> <span><a>30,000원</a><a>10%</a></span><br> <span><a>27,000원</a>
										<button>무료배송</button></span></td>

									<!-- 4개마다 줄바꿈 -->
									<c:if test="${st.count % 4 == 0}">
							</tr>
							<tr>
								</c:if>
								</c:forEach>
							</tr>
						</table>
					</div>

					<jsp:include page="./common/footer.jsp" />
				</section>
			</div>
		</div>
	</main>
</body>
</html>