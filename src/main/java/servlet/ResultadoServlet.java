package servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import controller.ControllerPuertaAsignada;
import controller.ControllerResultado;
import entities.PuertaAsignada;
import entities.Resultado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/resultado")
public class ResultadoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ResultadoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerResultado cr = new ControllerResultado();

		String newParam = request.getParameter("new");
		if (newParam != null) {
			LoadCombos(request);
			request.getRequestDispatcher("/resultado/form.jsp").forward(request, response);
			return;
		}

		String asustarParam = request.getParameter("asustar");
		if (asustarParam != null) {
			request.setAttribute("asustar", true);
			LoadCombos(request);
			request.getRequestDispatcher("/resultado/form.jsp").forward(request, response);
			return;
		}

		Integer id = ParseId(request, response);
		if (id == null) { return; }

		if (id == 0) {
			LinkedList<Resultado> resultados = cr.GetAll();
			request.setAttribute("resultados", resultados);
			request.getRequestDispatcher("/resultado/list.jsp").forward(request, response);
			return;
		}

		Resultado r = cr.GetOne(id);
		if (r.getId() == 0) {
			request.setAttribute("error", "Resultado not found [id=" + id + "]");
			LinkedList<Resultado> resultados = cr.GetAll();
			request.setAttribute("resultados", resultados);
			request.getRequestDispatcher("/resultado/list.jsp").forward(request, response);
			return;
		}
		request.setAttribute("resultado", r);
		LoadCombos(request);
		request.getRequestDispatcher("/resultado/form.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerResultado cr = new ControllerResultado();

		InitResponse(response);

		Resultado r = new Resultado();
		if (!Fill(request, response, r)) { return; }
		cr.Add(r);

		response.sendRedirect(request.getContextPath() + "/resultado");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerResultado cr = new ControllerResultado();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		Resultado r = cr.GetOne(id);
		if (r.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Resultado not found [id=" + id + "]");
			return;
		}

		if (!Fill(request, response, r)) { return; }
		cr.Update(r);

		response.sendRedirect(request.getContextPath() + "/resultado");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerResultado cr = new ControllerResultado();

		InitResponse(response);

		Integer id = ParseIdText(request, response);
		if (id == null) { return; }
		if (id == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id' is required");
			return;
		}

		Resultado r = cr.GetOne(id);
		if (r.getId() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Resultado not found [id=" + id + "]");
			return;
		}

		cr.Delete(id);

		response.sendRedirect(request.getContextPath() + "/resultado");
	}

	private boolean Fill(HttpServletRequest request, HttpServletResponse response, Resultado r) throws IOException {
		Date fecha = ParseFecha(request.getParameter("fecha"));
		if (fecha == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'fecha' is required (format yyyy-MM-ddTHH:mm)");
			return false;
		}

		String nombreNino = request.getParameter("nombre_nino");
		if (nombreNino == null || nombreNino.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'nombre_nino' is required");
			return false;
		}

		Integer idPuertaAsignada = ParsePositiveInt(request.getParameter("id_puerta_asignada"));
		if (idPuertaAsignada == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parameter 'id_puerta_asignada' is required");
			return false;
		}

		PuertaAsignada puertaAsignada = new PuertaAsignada();
		puertaAsignada.setId(idPuertaAsignada);

		r.setFecha(fecha);
		r.setNombreNino(nombreNino);
		r.setPuertaAsignada(puertaAsignada);

		Integer energia = ParsePositiveInt(request.getParameter("energia_generada"));
		r.setEnergiaGenerada(energia != null ? energia : 0);
		return true;
	}

	private Date ParseFecha(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		String[] patterns = { "yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss" };
		for (String pattern : patterns) {
			try {
				return new SimpleDateFormat(pattern).parse(value);
			} catch (ParseException e) {
				// try next pattern
			}
		}
		return null;
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
		ControllerPuertaAsignada cda = new ControllerPuertaAsignada();
		request.setAttribute("puertasAsignadas", cda.GetAll());
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
			ControllerResultado cr = new ControllerResultado();
			LinkedList<Resultado> resultados = cr.GetAll();
			request.setAttribute("resultados", resultados);
			request.getRequestDispatcher("/resultado/list.jsp").forward(request, response);
			return null;
		}
	}

}
