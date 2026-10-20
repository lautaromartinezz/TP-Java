<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Puerta" %>
<%@ page import="util.HtmlUtil" %>
<%
	String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title">Puertas</h1>
	<div class="page-actions">
		<a href="<%= ctx %>/puerta?new=true" class="btn btn-primary">
			<i data-lucide="plus" class="icon-sm"></i>
			Nueva puerta
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
	LinkedList<Puerta> puertas = (LinkedList<Puerta>) request.getAttribute("puertas");
	if (puertas == null || puertas.isEmpty()) {
%>
	<div class="empty-state">
		<i data-lucide="door-open" class="empty-state-icon"></i>
		<p>No hay puertas cargadas todavia.</p>
	</div>
<%
	} else {
%>
	<div class="table-wrapper mt-3">
		<table class="table">
			<thead>
				<tr>
					<th>ID</th>
					<th>Numero</th>
					<th>Acciones</th>
				</tr>
			</thead>
			<tbody>
				<%
					for (Puerta p : puertas) {
				%>
				<tr>
					<td><%= p.getId() %></td>
					<td><%= HtmlUtil.escapeHtml(p.getNumero()) %></td>
					<td class="actions">
						<a href="<%= ctx %>/puerta?id=<%= p.getId() %>" class="btn btn-ghost">
							<i data-lucide="pencil" class="icon-sm"></i>
							Editar
						</a>
						<button type="button" class="btn btn-danger" onclick="eliminar('<%= p.getId() %>', '<%= HtmlUtil.escapeJs(p.getNumero()) %>')">
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
	function eliminar(id, numero) {
		if (!confirm('Eliminar puerta ' + numero + '?')) { return; }
		fetch('<%= ctx %>/puerta?id=' + id, { method: 'DELETE' })
			.then(function() { location.href = '<%= ctx %>/puerta'; });
	}
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
