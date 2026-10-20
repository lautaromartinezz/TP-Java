<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.PuestoAsignado" %>
<%@ page import="util.HtmlUtil" %>
<%
	String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title">Puestos asignados</h1>
	<div class="page-actions">
		<a href="<%= ctx %>/puesto-asignado?new=true" class="btn btn-primary">
			<i data-lucide="plus" class="icon-sm"></i>
			Nueva asignacion
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
	LinkedList<PuestoAsignado> asignaciones = (LinkedList<PuestoAsignado>) request.getAttribute("asignaciones");
	if (asignaciones == null || asignaciones.isEmpty()) {
%>
	<div class="empty-state">
		<i data-lucide="calendar-check" class="empty-state-icon"></i>
		<p>No hay monstruos asignados a puestos todavia.</p>
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
					<th>Acciones</th>
				</tr>
			</thead>
			<tbody>
				<%
					for (PuestoAsignado pa : asignaciones) {
				%>
				<tr>
					<td><%= pa.getId() %></td>
					<td><%= pa.getFechaFormateada() %></td>
					<td><%= HtmlUtil.escapeHtml(pa.getMonster() != null ? pa.getMonster().getNombre() : "") %></td>
					<td><%= HtmlUtil.escapeHtml(pa.getPuesto() != null ? pa.getPuesto().getNombre() : "") %></td>
					<td class="actions">
						<a href="<%= ctx %>/puesto-asignado?id=<%= pa.getId() %>" class="btn btn-ghost">
							<i data-lucide="pencil" class="icon-sm"></i>
							Editar
						</a>
						<button type="button" class="btn btn-danger" onclick="eliminar('<%= pa.getId() %>')">
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
		if (!confirm('Eliminar asignacion #' + id + '?')) { return; }
		fetch('<%= ctx %>/puesto-asignado?id=' + id, { method: 'DELETE' })
			.then(function() { location.href = '<%= ctx %>/puesto-asignado'; });
	}
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
