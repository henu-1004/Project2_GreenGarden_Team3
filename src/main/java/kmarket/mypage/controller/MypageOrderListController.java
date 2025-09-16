package kmarket.mypage.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * 날짜 : 2025/09/15
 * 이름 : 한탁원
 * 내용 : 마이페이지 전체 목록 이동
 */
@WebServlet("/my/list.do")
public class MypageOrderListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/* service, logger 추가 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/* DB Logic 추가 */
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/mypage/order_list.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}