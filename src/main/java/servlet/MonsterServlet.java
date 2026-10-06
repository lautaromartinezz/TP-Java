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

@WebServlet("/monster")
public class MonsterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MonsterServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMonster cm = new ControllerMonster();

		String newParam = request.getParameter("new");
		if (newParam != null) {
			request.getRequestDispatcher("/monster/form.jsp").forward(request, response);
			return;
		}

		Integer id = ParseId(request, response);
		if (id == null) { return; }

		if (id == 0) {
			LinkedList<Monster> monsters = cm.GetAll();
			request.setAttribute("monsters", monsters);
			request.getRequestDispatcher("/monster/list.jsp").forward(request, response);
			return;
		}

		Monster m = cm.GetOne(id);
		if (m.getId() == 0) {
			request.setAttribute("error", "Monster not found [id=" + id + "]");
			LinkedList<Monster> monsters = cm.GetAll();
			request.setAttribute("monsters", monsters);
			request.getRequestDispatcher("/monster/list.jsp").forward(request, response);
			return;
		}
		request.setAttribute("monster", m);
		request.getRequestDispatcher("/monster/form.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMonster cm = new ControllerMonster();

		InitResponse(response);

		String name = request.getParameter("name");
		if (name == null || name.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'name' is required");
			return;
		}

		Monster m = new Monster();
		m.setName(name);
		cm.Add(m);

		response.sendRedirect(request.getContextPath() + "/monster");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMonster cm = new ControllerMonster();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		String name = request.getParameter("name");
		if (name == null || name.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'name' is required");
			return;
		}

		Monster m = cm.GetOne(id);
		if (m.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Monster not found [id=" + id + "]");
			return;
		}

		m.setName(name);
		cm.Update(m);

		response.sendRedirect(request.getContextPath() + "/monster");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMonster cm = new ControllerMonster();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		Monster m = cm.GetOne(id);
		if (m.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Monster not found [id=" + id + "]");
			return;
		}

		cm.Delete(id);

		response.sendRedirect(request.getContextPath() + "/monster");
	}

	private void InitResponse(HttpServletResponse response) {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
	}

	private Integer ParseIdText(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String idParam = request.getParameter("id");
		if (idParam == null || idParam.isEmpty()) {
			return 0;
		}
		try {
			return Integer.parseInt(idParam);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid id: " + idParam);
			return null;
		}
	}

	private Integer ParseId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idParam = request.getParameter("id");
		if (idParam == null || idParam.isEmpty()) {
			return 0;
		}
		try {
			return Integer.parseInt(idParam);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Invalid id: " + idParam);
			ControllerMonster cm = new ControllerMonster();
			LinkedList<Monster> monsters = cm.GetAll();
			request.setAttribute("monsters", monsters);
			request.getRequestDispatcher("/monster/list.jsp").forward(request, response);
			return null;
		}
	}

}
