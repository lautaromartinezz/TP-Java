package servlet;

import java.io.IOException;
import java.util.LinkedList;

import controller.ControllerMiedo;
import entities.Miedo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/miedo")
public class MiedoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MiedoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMiedo cm = new ControllerMiedo();

		String newParam = request.getParameter("new");
		if (newParam != null) {
			request.getRequestDispatcher("/miedo/form.jsp").forward(request, response);
			return;
		}

		Integer id = ParseId(request, response);
		if (id == null) { return; }

		if (id == 0) {
			LinkedList<Miedo> miedos = cm.GetAll();
			request.setAttribute("miedos", miedos);
			request.getRequestDispatcher("/miedo/list.jsp").forward(request, response);
			return;
		}

		Miedo m = cm.GetOne(id);
		if (m.getId() == 0) {
			request.setAttribute("error", "Miedo no encontrado [id=" + id + "]");
			LinkedList<Miedo> miedos = cm.GetAll();
			request.setAttribute("miedos", miedos);
			request.getRequestDispatcher("/miedo/list.jsp").forward(request, response);
			return;
		}
		
		request.setAttribute("miedo", m);
		request.getRequestDispatcher("/miedo/form.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMiedo cm = new ControllerMiedo();
		InitResponse(response);
		String method = request.getParameter("_method");
		if ("PUT".equalsIgnoreCase(method)) {
			doPut(request, response);
			return;
		}
		if ("DELETE".equalsIgnoreCase(method)) {
			doDelete(request, response);
			return;
		}
		Miedo m = new Miedo();
		if (!Fill(request, response, m)) { return; }
		cm.Add(m);

		response.sendRedirect(request.getContextPath() + "/miedo");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMiedo cm = new ControllerMiedo();
		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("El parámetro 'id' es requerido");
			return;
		}

		Miedo m = cm.GetOne(id);
		if (m.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Miedo no encontrado [id=" + id + "]");
			return;
		}

		if (!Fill(request, response, m)) { return; }
		cm.Update(m);

		response.sendRedirect(request.getContextPath() + "/miedo");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerMiedo cm = new ControllerMiedo();
		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("El parámetro 'id' es requerido");
			return;
		}

		Miedo m = cm.GetOne(id);
		if (m.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Miedo no encontrado [id=" + id + "]");
			return;
		}

		cm.Delete(id);

		response.sendRedirect(request.getContextPath() + "/miedo");
	}

	private boolean Fill(HttpServletRequest request, HttpServletResponse response, Miedo m) throws IOException {
		String nombre = request.getParameter("nombre");
		if (nombre == null || nombre.trim().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("El parámetro 'nombre' es requerido");
			return false;
		}
		
		m.setNombre(nombre.trim());
		return true;
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
			response.getWriter().println("ID inválido: " + idParam);
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
			request.setAttribute("error", "ID inválido: " + idParam);
			ControllerMiedo cm = new ControllerMiedo();
			LinkedList<Miedo> miedos = cm.GetAll();
			request.setAttribute("miedos", miedos);
			request.getRequestDispatcher("/miedo/list.jsp").forward(request, response);
			return null;
		}
	}
}