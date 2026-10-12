package servlet;

import java.io.IOException;
import java.util.LinkedList;

import controller.ControllerPuesto;
import controller.ControllerPuerta;
import entities.Puesto;
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

		String newParam = request.getParameter("new");
		if (newParam != null) {
			LoadPuestos(request);
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
		LoadPuestos(request);
		request.getRequestDispatcher("/puerta/form.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuerta cp = new ControllerPuerta();

		InitResponse(response);

		Puerta p = new Puerta();
		if (!Fill(request, response, p)) { return; }
		cp.Add(p);

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

		Puerta p = cp.GetOne(id);
		if (p.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Puerta not found [id=" + id + "]");
			return;
		}

		if (!Fill(request, response, p)) { return; }
		cp.Update(p);

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

	private boolean Fill(HttpServletRequest request, HttpServletResponse response, Puerta p) throws IOException {
		String numero = request.getParameter("numero");
		if (numero == null || numero.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'numero' is required");
			return false;
		}

		String idPuestoParam = request.getParameter("id_puesto");
		if (idPuestoParam == null || idPuestoParam.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id_puesto' is required");
			return false;
		}

		int idPuesto;
		try {
			idPuesto = Integer.parseInt(idPuestoParam);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid id_puesto: " + idPuestoParam);
			return false;
		}

		Puesto puesto = new Puesto();
		puesto.setId(idPuesto);

		p.setNumero(numero);
		p.setPuesto(puesto);
		return true;
	}

	private void LoadPuestos(HttpServletRequest request) {
		ControllerPuesto cp = new ControllerPuesto();
		request.setAttribute("puestos", cp.GetAll());
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
