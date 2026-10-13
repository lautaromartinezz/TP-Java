package servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import controller.ControllerMonster;
import controller.ControllerPuesto;
import controller.ControllerPuestoAsignado;
import entities.Monster;
import entities.Puesto;
import entities.PuestoAsignado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/puesto-asignado")
public class PuestoAsignadoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PuestoAsignadoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuestoAsignado cpa = new ControllerPuestoAsignado();

		String newParam = request.getParameter("new");
		if (newParam != null) {
			LoadCombos(request);
			request.getRequestDispatcher("/puesto-asignado/form.jsp").forward(request, response);
			return;
		}

		Integer id = ParseId(request, response);
		if (id == null) { return; }

		if (id == 0) {
			LinkedList<PuestoAsignado> asignaciones = cpa.GetAll();
			request.setAttribute("asignaciones", asignaciones);
			request.getRequestDispatcher("/puesto-asignado/list.jsp").forward(request, response);
			return;
		}

		PuestoAsignado pa = cpa.GetOne(id);
		if (pa.getId() == 0) {
			request.setAttribute("error", "PuestoAsignado not found [id=" + id + "]");
			LinkedList<PuestoAsignado> asignaciones = cpa.GetAll();
			request.setAttribute("asignaciones", asignaciones);
			request.getRequestDispatcher("/puesto-asignado/list.jsp").forward(request, response);
			return;
		}
		request.setAttribute("asignacion", pa);
		LoadCombos(request);
		request.getRequestDispatcher("/puesto-asignado/form.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuestoAsignado cpa = new ControllerPuestoAsignado();

		InitResponse(response);

		PuestoAsignado pa = new PuestoAsignado();
		if (!Fill(request, response, pa)) { return; }
		if (!cpa.AsignacionValida(pa)) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().println("El monstruo ya tiene un puesto asignado en esa fecha");
			return;
		}
		cpa.Add(pa);

		response.sendRedirect(request.getContextPath() + "/puesto-asignado");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuestoAsignado cpa = new ControllerPuestoAsignado();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		PuestoAsignado pa = cpa.GetOne(id);
		if (pa.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("PuestoAsignado not found [id=" + id + "]");
			return;
		}

		if (!Fill(request, response, pa)) { return; }
		if (!cpa.AsignacionValida(pa)) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().println("El monstruo ya tiene un puesto asignado en esa fecha");
			return;
		}
		cpa.Update(pa);

		response.sendRedirect(request.getContextPath() + "/puesto-asignado");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuestoAsignado cpa = new ControllerPuestoAsignado();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		PuestoAsignado pa = cpa.GetOne(id);
		if (pa.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("PuestoAsignado not found [id=" + id + "]");
			return;
		}

		cpa.Delete(id);

		response.sendRedirect(request.getContextPath() + "/puesto-asignado");
	}

	private boolean Fill(HttpServletRequest request, HttpServletResponse response, PuestoAsignado pa) throws IOException {
		Date fecha = ParseFecha(request.getParameter("fecha"));
		if (fecha == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'fecha' is required (format yyyy-MM-dd)");
			return false;
		}

		Integer idMonster = ParsePositiveInt(request.getParameter("id_monster"));
		if (idMonster == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id_monster' is required");
			return false;
		}

		Integer idPuesto = ParsePositiveInt(request.getParameter("id_puesto"));
		if (idPuesto == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id_puesto' is required");
			return false;
		}

		Monster monster = new Monster();
		monster.setId(idMonster);

		Puesto puesto = new Puesto();
		puesto.setId(idPuesto);

		pa.setFecha(fecha);
		pa.setMonster(monster);
		pa.setPuesto(puesto);
		return true;
	}

	private Date ParseFecha(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (ParseException e) {
			return null;
		}
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
		ControllerMonster cm = new ControllerMonster();
		ControllerPuesto cp = new ControllerPuesto();
		request.setAttribute("monsters", cm.GetAll());
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
			ControllerPuestoAsignado cpa = new ControllerPuestoAsignado();
			LinkedList<PuestoAsignado> asignaciones = cpa.GetAll();
			request.setAttribute("asignaciones", asignaciones);
			request.getRequestDispatcher("/puesto-asignado/list.jsp").forward(request, response);
			return null;
		}
	}

}
