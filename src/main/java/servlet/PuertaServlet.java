package servlet;

import java.io.IOException;
import java.util.LinkedList;

import controller.ControllerPuerta;
import entities.Puerta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/puerta")
public class PuertaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PuertaServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
ControllerPuerta cp = new ControllerPuerta();
		controller.ControllerMiedo cMiedo = new controller.ControllerMiedo();

		String newParam = request.getParameter("new");
		if (newParam != null) {
			request.setAttribute("listaMiedos", cMiedo.GetAll());
			request.getRequestDispatcher("/puerta/form.jsp").forward(request, response);
			return;
		}
		Integer id = ParseId(request, response);
		if (id == null) { return; }

		if (id == 0) {
			LinkedList<Puerta> puertas = cp.GetAll();
			request.setAttribute("puertas", puertas);
			request.getRequestDispatcher("/puerta/list.jsp").forward(request, response);
			return;
		}

		Puerta p = cp.GetOne(id);
		if (p.getId() == 0) {
			request.setAttribute("error", "Puerta not found [id=" + id + "]");
			LinkedList<Puerta> puertas = cp.GetAll();
			request.setAttribute("puertas", puertas);
			request.getRequestDispatcher("/puerta/list.jsp").forward(request, response);
			return;
		}
		request.setAttribute("puerta", p);
		request.setAttribute("listaMiedos", cMiedo.GetAll()); 
		request.setAttribute("miedosAsignados", cp.getMiedosAsignados(id));
		request.getRequestDispatcher("/puerta/form.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuerta cp = new ControllerPuerta();

		InitResponse(response);

		String numero = request.getParameter("numero");
		if (numero == null || numero.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'numero' is required");
			return;
		}

		Puerta p = new Puerta();
		p.setNumero(numero);
		cp.Add(p);
		String[] miedosSeleccionados = request.getParameterValues("miedos");
		if (miedosSeleccionados == null) {
			miedosSeleccionados = new String[0]; 
		}

		cp.actualizarMiedos(p.getId(), miedosSeleccionados);

		response.sendRedirect(request.getContextPath() + "/puerta");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuerta cp = new ControllerPuerta();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		String numero = request.getParameter("numero");
		if (numero == null || numero.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'numero' is required");
			return;
		}

		Puerta p = cp.GetOne(id);
		if (p.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Puerta not found [id=" + id + "]");
			return;
		}

		p.setNumero(numero);
		cp.Update(p);
		String[] miedosSeleccionados = request.getParameterValues("miedos");
		if (miedosSeleccionados == null) {
			miedosSeleccionados = new String[0];
		}

		cp.actualizarMiedos(p.getId(), miedosSeleccionados);
		

		response.sendRedirect(request.getContextPath() + "/puerta");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuerta cp = new ControllerPuerta();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		Puerta p = cp.GetOne(id);
		if (p.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Puerta not found [id=" + id + "]");
			return;
		}

		cp.Delete(id);

		response.sendRedirect(request.getContextPath() + "/puerta");
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
			ControllerPuerta cp = new ControllerPuerta();
			LinkedList<Puerta> puertas = cp.GetAll();
			request.setAttribute("puertas", puertas);
			request.getRequestDispatcher("/puerta/list.jsp").forward(request, response);
			return null;
		}
	}

}
