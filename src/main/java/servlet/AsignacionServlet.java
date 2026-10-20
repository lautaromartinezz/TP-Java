package servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.time.LocalDate;

import controller.ControllerPuerta;
import controller.ControllerPuestoAsignado;
import controller.ControllerPuertaAsignada;
import controller.ControllerMonster;
import controller.ControllerPuesto;
import entities.Monster;
import entities.Puerta;
import entities.Puesto;
import entities.PuestoAsignado;
import entities.PuertaAsignada;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/asignacion")
public class AsignacionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuesto cpuesto = new ControllerPuesto();
		ControllerMonster cmonster = new ControllerMonster();
		ControllerPuerta cpuerta = new ControllerPuerta();
		ControllerPuestoAsignado cpa = new ControllerPuestoAsignado();
		
		String fechaParam = request.getParameter("fecha");
		LocalDate fechaSolicitada = (fechaParam != null && !fechaParam.isEmpty()) ? LocalDate.parse(fechaParam) : LocalDate.now();
		LocalDate hoy = LocalDate.now();
		
		boolean esEditable = fechaSolicitada.isEqual(hoy);
		
		request.setAttribute("fechaMostrada", fechaSolicitada.toString());
		request.setAttribute("esEditable", esEditable);
		
		request.setAttribute("puestos", cpuesto.GetAll());
		request.setAttribute("monstruos", cmonster.GetAll());
		request.setAttribute("mapaCompatibles", cpuerta.getTodasLasPuertasCompatibles());
		
		java.sql.Date sqlFecha = java.sql.Date.valueOf(fechaSolicitada);
		request.setAttribute("monstruosDelDia", cpa.getMonstruosDelDia(sqlFecha));
		request.setAttribute("puertasDelDia", cpa.getPuertasDelDia(sqlFecha));
		
		request.getRequestDispatcher("/asignacion/planta.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerPuestoAsignado cpa = new ControllerPuestoAsignado();
		ControllerPuertaAsignada cda = new ControllerPuertaAsignada();

		String[] puestosActivos = request.getParameterValues("puestosActivos");
		
		if (puestosActivos != null) {
			java.sql.Date hoy = java.sql.Date.valueOf(java.time.LocalDate.now());

			for (String idPuestoStr : puestosActivos) {
				String idMonsterStr = request.getParameter("idMonster_" + idPuestoStr);
				String[] puertasSeleccionadas = request.getParameterValues("puertas_" + idPuestoStr);

				if (idMonsterStr != null && !idMonsterStr.isEmpty()) {
					PuestoAsignado pa = new PuestoAsignado();
					Puesto p = new Puesto(); p.setId(Integer.parseInt(idPuestoStr));
					Monster m = new Monster(); m.setId(Integer.parseInt(idMonsterStr));
					
					pa.setPuesto(p);
					pa.setMonster(m);
					pa.setFecha(hoy);
					
					System.out.println("--- Evaluando Estación " + idPuestoStr + " ---");

					// 1. Manejo del Padre (Estación / Monstruo)
					int idGuardado = cpa.UpdateMonstruo(pa); 
					
					if (idGuardado > 0) {
						System.out.println("-> La estación ya existía. Monstruo actualizado. ID: " + idGuardado);
						pa.setId(idGuardado); 
					} else {
						if (cpa.AsignacionValida(pa)) {
							System.out.println("-> Nueva asignación del día. Creando puesto...");
							cpa.Add(pa); 
						} else {
							System.out.println("-> RECHAZADO: El monstruo ya está en otra estación hoy.");
							continue; 
						}
					}
				
					// 2. !-- EL CAMBIO MAESTRO DE LIMPIEZA EXPRESA --!
					if (pa.getId() > 0) {
						
						// Forzamos la limpieza absoluta de TODAS las puertas de este turno en MySQL antes de guardar
						cda.DeleteByPuestoAsignado(pa.getId());
						
						if (puertasSeleccionadas != null && puertasSeleccionadas.length > 0) {
							
							java.util.HashSet<String> puertasUnicas = new java.util.HashSet<>();
							for (String idp : puertasSeleccionadas) {
								puertasUnicas.add(idp);
							}
							
							System.out.println("-> Insertando " + puertasUnicas.size() + " puertas únicas.");
							
							for (String idPuerta : puertasUnicas) {
								PuertaAsignada da = new PuertaAsignada();
								Puerta puerta = new Puerta(); 
								puerta.setId(Integer.parseInt(idPuerta));
								
								da.setPuestoAsignado(pa); 
								da.setPuerta(puerta);
								
								cda.Add(da);
							}
						} else {
							System.out.println("-> Se guardó la estación pero se dejaron 0 puertas (Limpieza exitosa y total).");
						}
					}
				}
			}
		}
		response.sendRedirect(request.getContextPath() + "/asignacion?success=true");
	}


}