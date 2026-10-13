package servlet;

import java.io.IOException;

import controller.ControllerMonster;
import controller.ControllerPuerta;
import controller.ControllerPuertaAsignada;
import controller.ControllerPuesto;
import controller.ControllerPuestoAsignado;
import controller.ControllerResultado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DashboardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("totalMonstruos", new ControllerMonster().GetAll().size());
		request.setAttribute("totalPuertas", new ControllerPuerta().GetAll().size());
		request.setAttribute("totalPuestos", new ControllerPuesto().GetAll().size());
		request.setAttribute("totalPuestosAsignados", new ControllerPuestoAsignado().GetAll().size());
		request.setAttribute("totalPuertasAsignadas", new ControllerPuertaAsignada().GetAll().size());
		request.setAttribute("totalResultados", new ControllerResultado().GetAll().size());

		request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
	}

}
