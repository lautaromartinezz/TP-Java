package servlet;

import java.io.IOException;
import java.util.LinkedList;

import controller.ControllerPuesto;
import entities.Puesto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/puesto")
public class PuestoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PuestoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuesto cp = new ControllerPuesto();

		String newParam = request.getParameter("new");
		if (newParam != null) {
			request.getRequestDispatcher("/puesto/form.jsp").forward(request, response);
			return;
		}

		Integer id = ParseId(request, response);
		if (id == null) { return; }

		if (id == 0) {
			LinkedList<Puesto> puestos = cp.GetAll();
			request.setAttribute("puestos", puestos);
			request.getRequestDispatcher("/puesto/list.jsp").forward(request, response);
			return;
		}

		Puesto p = cp.GetOne(id);
		if (p.getId() == 0) {
			request.setAttribute("error", "Puesto not found [id=" + id + "]");
			LinkedList<Puesto> puestos = cp.GetAll();
			request.setAttribute("puestos", puestos);
			request.getRequestDispatcher("/puesto/list.jsp").forward(request, response);
			return;
		}
		request.setAttribute("puesto", p);
		request.getRequestDispatcher("/puesto/form.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuesto cp = new ControllerPuesto();

		InitResponse(response);

		String nombre = request.getParameter("nombre");
		if (nombre == null || nombre.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'nombre' is required");
			return;
		}

		Puesto p = new Puesto();
		p.setNombre(nombre);
		p.setDescripcion(request.getParameter("descripcion"));
		cp.Add(p);

		response.sendRedirect(request.getContextPath() + "/puesto");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuesto cp = new ControllerPuesto();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		String nombre = request.getParameter("nombre");
		if (nombre == null || nombre.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'nombre' is required");
			return;
		}

		Puesto p = cp.GetOne(id);
		if (p.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Puesto not found [id=" + id + "]");
			return;
		}

		p.setNombre(nombre);
		p.setDescripcion(request.getParameter("descripcion"));
		cp.Update(p);

		response.sendRedirect(request.getContextPath() + "/puesto");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuesto cp = new ControllerPuesto();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		Puesto p = cp.GetOne(id);
		if (p.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Puesto not found [id=" + id + "]");
			return;
		}

		cp.Delete(id);

		response.sendRedirect(request.getContextPath() + "/puesto");
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
			ControllerPuesto cp = new ControllerPuesto();
			LinkedList<Puesto> puestos = cp.GetAll();
			request.setAttribute("puestos", puestos);
			request.getRequestDispatcher("/puesto/list.jsp").forward(request, response);
			return null;
		}
	}

}
