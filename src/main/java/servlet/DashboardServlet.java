package servlet;

import java.io.IOException;
import controller.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rol = (String) request.getAttribute("rolUsuario");
        if (rol == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        rol = rol.toLowerCase();

        // 2. Enrutar según el rol
        if (rol.equals("admin")) {
            request.setAttribute("totalMonstruos", new ControllerMonster().GetAll().size());
            request.setAttribute("totalPuertas", new ControllerPuerta().GetAll().size());
            request.setAttribute("totalPuestos", new ControllerPuesto().GetAll().size());
            request.setAttribute("totalPuestosAsignados", new ControllerPuestoAsignado().GetAll().size());
            request.setAttribute("totalPuertasAsignadas", new ControllerPuertaAsignada().GetAll().size());
            request.setAttribute("totalResultados", new ControllerResultado().GetAll().size());
            
            request.getRequestDispatcher("/WEB-INF/dashboard-admin.jsp").forward(request, response);

        } else if (rol.equals("operario")) {
            request.setAttribute("totalPuestosAsignados", new ControllerPuestoAsignado().GetAll().size());
            request.setAttribute("totalPuertasAsignadas", new ControllerPuertaAsignada().GetAll().size());
            
            request.getRequestDispatcher("/WEB-INF/dashboard-operario.jsp").forward(request, response);

        } else if (rol.equals("monstruo")) {
            Integer idMonstruo = (Integer) request.getAttribute("idMonstruo");
            request.setAttribute("ultimosSustos", new ControllerResultado().getTop3Historial(idMonstruo));
            request.setAttribute("historialSustos", new ControllerResultado().getHistorialSustos(idMonstruo));
            request.setAttribute("sustosBajos", 0); 
            request.setAttribute("puertasHoy", new ControllerPuertaAsignada().contarPuertasPendientes(idMonstruo));
            request.setAttribute("energiaTotal", new ControllerResultado().sumarEnergiaTotal(idMonstruo));
            request.setAttribute("listaPuertasPendientes", new ControllerPuertaAsignada().getTop3Pendientes(idMonstruo));

            request.getRequestDispatcher("/WEB-INF/dashboard-monstruo.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Rol no autorizado.");
        }
    }
}