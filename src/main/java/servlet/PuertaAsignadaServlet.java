package servlet;

import java.io.IOException;
import java.util.LinkedList;

import controller.ControllerPuerta;
import controller.ControllerPuestoAsignado;
import controller.ControllerPuertaAsignada;
import entities.PuestoAsignado;
import entities.Puerta;
import entities.PuertaAsignada;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/puerta-asignada")
public class PuertaAsignadaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PuertaAsignadaServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuertaAsignada cda = new ControllerPuertaAsignada();

		String newParam = request.getParameter("new");
		if (newParam != null) {
			LoadCombos(request);
			request.getRequestDispatcher("/puerta-asignada/form.jsp").forward(request, response);
			return;
		}

		Integer id = ParseId(request, response);
		if (id == null) { return; }

		if (id == 0) {
			LinkedList<PuertaAsignada> puertasAsignadas = cda.GetAll();
			request.setAttribute("puertasAsignadas", puertasAsignadas);
			request.getRequestDispatcher("/puerta-asignada/list.jsp").forward(request, response);
			return;
		}

		PuertaAsignada da = cda.GetOne(id);
		if (da.getId() == 0) {
			request.setAttribute("error", "PuertaAsignada not found [id=" + id + "]");
			LinkedList<PuertaAsignada> puertasAsignadas = cda.GetAll();
			request.setAttribute("puertasAsignadas", puertasAsignadas);
			request.getRequestDispatcher("/puerta-asignada/list.jsp").forward(request, response);
			return;
		}
		request.setAttribute("puertaAsignada", da);
		LoadCombos(request);
		request.getRequestDispatcher("/puerta-asignada/form.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuertaAsignada cda = new ControllerPuertaAsignada();

		InitResponse(response);

		PuertaAsignada da = new PuertaAsignada();
		if (!Fill(request, response, da)) { return; }
		if (!cda.AsignacionValida(da)) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().println("Esa puerta ya esta asignada a esa asignacion de puesto");
			return;
		}
		cda.Add(da);

		response.sendRedirect(request.getContextPath() + "/puerta-asignada");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuertaAsignada cda = new ControllerPuertaAsignada();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		PuertaAsignada da = cda.GetOne(id);
		if (da.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("PuertaAsignada not found [id=" + id + "]");
			return;
		}

		if (!Fill(request, response, da)) { return; }
		if (!cda.AsignacionValida(da)) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().println("Esa puerta ya esta asignada a esa asignacion de puesto");
			return;
		}
		cda.Update(da);

		response.sendRedirect(request.getContextPath() + "/puerta-asignada");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuertaAsignada cda = new ControllerPuertaAsignada();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		PuertaAsignada da = cda.GetOne(id);
		if (da.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("PuertaAsignada not found [id=" + id + "]");
			return;
		}

		cda.Delete(id);

		response.sendRedirect(request.getContextPath() + "/puerta-asignada");
	}

	private boolean Fill(HttpServletRequest request, HttpServletResponse response, PuertaAsignada da) throws IOException {
		Integer idPuestoAsignado = ParsePositiveInt(request.getParameter("id_puesto_asignado"));
		if (idPuestoAsignado == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id_puesto_asignado' is required");
			return false;
		}

		Integer idPuerta = ParsePositiveInt(request.getParameter("id_puerta"));
		if (idPuerta == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id_puerta' is required");
			return false;
		}

		PuestoAsignado pa = new PuestoAsignado();
		pa.setId(idPuestoAsignado);

		Puerta puerta = new Puerta();
		puerta.setId(idPuerta);

		da.setPuestoAsignado(pa);
		da.setPuerta(puerta);
		return true;
	}

	private Integer ParsePositiveInt(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			int parsed = Integer.parseInt(value);
			return parsed > 0 ? parsed : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private void LoadCombos(HttpServletRequest request) {
		ControllerPuestoAsignado cpa = new ControllerPuestoAsignado();
		ControllerPuerta cp = new ControllerPuerta();
		request.setAttribute("asignaciones", cpa.GetAll());
		request.setAttribute("puertas", cp.GetAll());
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
			ControllerPuertaAsignada cda = new ControllerPuertaAsignada();
			LinkedList<PuertaAsignada> puertasAsignadas = cda.GetAll();
			request.setAttribute("puertasAsignadas", puertasAsignadas);
			request.getRequestDispatcher("/puerta-asignada/list.jsp").forward(request, response);
			return null;
		}
	}

}
