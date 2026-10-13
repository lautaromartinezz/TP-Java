<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.PuertaAsignada" %>
<%@ page import="util.HtmlUtil" %>
<%
	String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title">Puertas asignadas por dia</h1>
	<div class="page-actions">
		<a href="<%= ctx %>/puerta-asignada?new=true" class="btn btn-primary">
			<i data-lucide="plus" class="icon-sm"></i>
			Nueva puerta asignada
		</a>
	</div>
</div>

<%
	String error = (String) request.getAttribute("error");
	if (error != null) {
%>
	<p class="alert alert-error"><%= HtmlUtil.escapeHtml(error) %></p>
<%
	}
%>

<%
	LinkedList<PuertaAsignada> puertasAsignadas = (LinkedList<PuertaAsignada>) request.getAttribute("puertasAsignadas");
	if (puertasAsignadas == null || puertasAsignadas.isEmpty()) {
%>
	<div class="empty-state">
		<i data-lucide="calendar-clock" class="empty-state-icon"></i>
		<p>No hay puertas asignadas todavia.</p>
	</div>
<%
	} else {
%>
	<div class="table-wrapper mt-3">
		<table class="table">
			<thead>
				<tr>
					<th>ID</th>
					<th>Fecha</th>
					<th>Monstruo</th>
					<th>Puesto</th>
					<th>Puerta</th>
					<th>Acciones</th>
				</tr>
			</thead>
			<tbody>
				<%
					for (PuertaAsignada da : puertasAsignadas) {
						String fecha = da.getPuestoAsignado() != null ? da.getPuestoAsignado().getFechaFormateada() : "";
						String monstruo = da.getPuestoAsignado() != null && da.getPuestoAsignado().getMonster() != null ? da.getPuestoAsignado().getMonster().getNombre() : "";
						String puesto = da.getPuestoAsignado() != null && da.getPuestoAsignado().getPuesto() != null ? da.getPuestoAsignado().getPuesto().getNombre() : "";
						String puerta = da.getPuerta() != null ? da.getPuerta().getNumero() : "";
				%>
				<tr>
					<td><%= da.getId() %></td>
					<td><%= HtmlUtil.escapeHtml(fecha) %></td>
					<td><%= HtmlUtil.escapeHtml(monstruo) %></td>
					<td><%= HtmlUtil.escapeHtml(puesto) %></td>
					<td><%= HtmlUtil.escapeHtml(puerta) %></td>
					<td class="actions">
						<a href="<%= ctx %>/puerta-asignada?id=<%= da.getId() %>" class="btn btn-ghost">
							<i data-lucide="pencil" class="icon-sm"></i>
							Editar
						</a>
						<button type="button" class="btn btn-danger" onclick="eliminar('<%= da.getId() %>')">
							<i data-lucide="trash-2" class="icon-sm"></i>
							Eliminar
						</button>
					</td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
<%
	}
%>

<script>
	function eliminar(id) {
		if (!confirm('Eliminar puerta asignada #' + id + '?')) { return; }
		fetch('<%= ctx %>/puerta-asignada?id=' + id, { method: 'DELETE' })
			.then(function() { location.href = '<%= ctx %>/puerta-asignada'; });
	}
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
