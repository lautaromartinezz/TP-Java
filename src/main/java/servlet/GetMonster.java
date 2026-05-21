package servlet;

import java.io.IOException;
import java.util.LinkedList;

import controller.ControllerMonster;
import entities.Monster;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetMonster")
public class GetMonster extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetMonster() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMonster cm = new ControllerMonster();
		LinkedList<Monster> monsters = cm.GetAll();
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		for (Monster m : monsters) {
			response.getWriter().println(m.toString());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
