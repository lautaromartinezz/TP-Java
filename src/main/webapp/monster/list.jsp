<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Monster" %>
<%@ page import="util.HtmlUtil" %>
<%
	String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title">Monstruos</h1>
	<div class="page-actions">
		<a href="<%= ctx %>/monster?ranking=true" class="btn btn-ghost">
			<i data-lucide="trophy" class="icon-sm"></i>
			Ranking por energia
		</a>
		<a href="<%= ctx %>/monster?new=true" class="btn btn-primary">
			<i data-lucide="plus" class="icon-sm"></i>
			Nuevo monstruo
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
	LinkedList<Monster> monsters = (LinkedList<Monster>) request.getAttribute("monsters");
	if (monsters == null || monsters.isEmpty()) {
%>
	<div class="empty-state">
		<i data-lucide="users" class="empty-state-icon"></i>
		<p>No hay monstruos cargados todavia.</p>
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
					<th>Acciones</th>
				</tr>
			</thead>
			<tbody>
				<%
					for (Monster m : monsters) {
						String safeNombre = HtmlUtil.escapeHtml(m.getNombre());
						String safeJsNombre = HtmlUtil.escapeJs(m.getNombre());
				%>
				<tr>
					<td><%= m.getId() %></td>
					<td><%= safeNombre %></td>
					<td class="actions">
						<a href="<%= ctx %>/monster?id=<%= m.getId() %>" class="btn btn-ghost">
							<i data-lucide="pencil" class="icon-sm"></i>
							Editar
						</a>
						<button type="button" class="btn btn-danger" onclick="eliminar('<%= m.getId() %>', '<%= safeJsNombre %>')">
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
		fetch('<%= ctx %>/monster?id=' + id, { method: 'DELETE' })
			.then(function() { location.href = '<%= ctx %>/monster'; });
	}
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
