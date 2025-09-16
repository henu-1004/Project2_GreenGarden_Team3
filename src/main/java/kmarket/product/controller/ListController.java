package kmarket.product.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * 날짜 : 2025/09/16
 * 이름 : 한탁원
 * 내용 : 상품목록 전체 목록 이동
 */
@WebServlet("/product/list.do")
public class ListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/* service, logger 추가 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/* DB Logic 추가 */
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/product/list.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}