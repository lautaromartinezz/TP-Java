<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Puesto" %>
<%@ page import="util.HtmlUtil" %>
<%
	String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title">Puestos</h1>
	<div class="page-actions">
		<a href="<%= ctx %>/puesto?new=true" class="btn btn-primary">
			<i data-lucide="plus" class="icon-sm"></i>
			Nuevo puesto
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
	LinkedList<Puesto> puestos = (LinkedList<Puesto>) request.getAttribute("puestos");
	if (puestos == null || puestos.isEmpty()) {
%>
	<div class="empty-state">
		<i data-lucide="briefcase" class="empty-state-icon"></i>
		<p>No hay puestos cargados todavia.</p>
	</div>
<%
	} else {
%>
	<div class="table-wrapper mt-3">
		<table class="table">
			<thead>
				<tr>
					<th>ID</th>
					<th>Nombre</th>
					<th>Descripcion</th>
					<th>Acciones</th>
				</tr>
			</thead>
			<tbody>
				<%
					for (Puesto p : puestos) {
				%>
				<tr>
					<td><%= p.getId() %></td>
					<td><%= HtmlUtil.escapeHtml(p.getNombre()) %></td>
					<td><%= HtmlUtil.escapeHtml(p.getDescripcion()) %></td>
					<td class="actions">
						<a href="<%= ctx %>/puesto?id=<%= p.getId() %>" class="btn btn-ghost">
							<i data-lucide="pencil" class="icon-sm"></i>
							Editar
						</a>
						<button type="button" class="btn btn-danger" onclick="eliminar('<%= p.getId() %>', '<%= HtmlUtil.escapeJs(p.getNombre()) %>')">
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
	function eliminar(id, nombre) {
		if (!confirm('Eliminar ' + nombre + '?')) { return; }
		fetch('<%= ctx %>/puesto?id=' + id, { method: 'DELETE' })
			.then(function() { location.href = '<%= ctx %>/puesto'; });
	}
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
