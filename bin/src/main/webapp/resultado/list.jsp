<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.PuestoAsignado" %>
<%@ page import="entities.PuertaAsignada" %>
<%@ page import="entities.Resultado" %>
<%@ page import="util.HtmlUtil" %>
<%
	String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title">Resultados</h1>
	<div class="page-actions">
		<a href="<%= ctx %>/resultado?asustar=true" class="btn btn-ghost">
			<i data-lucide="ghost" class="icon-sm"></i>
			Asustar a un nino
		</a>
		<a href="<%= ctx %>/resultado?new=true" class="btn btn-primary">
			<i data-lucide="plus" class="icon-sm"></i>
			Nuevo resultado
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
	LinkedList<Resultado> resultados = (LinkedList<Resultado>) request.getAttribute("resultados");
	if (resultados == null || resultados.isEmpty()) {
%>
	<div class="empty-state">
		<i data-lucide="file-text" class="empty-state-icon"></i>
		<p>No hay resultados cargados todavia.</p>
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
					<th>Nino</th>
					<th>Monstruo</th>
					<th>Puesto</th>
					<th>Puerta</th>
					<th>Energia</th>
					<th>Acciones</th>
				</tr>
			</thead>
			<tbody>
				<%
					for (Resultado r : resultados) {
						PuertaAsignada da = r.getPuertaAsignada();
						PuestoAsignado pa = da != null ? da.getPuestoAsignado() : null;
						String monstruo = pa != null && pa.getMonster() != null ? pa.getMonster().getNombre() : "";
						String puesto = pa != null && pa.getPuesto() != null ? pa.getPuesto().getNombre() : "";
						String puerta = da != null && da.getPuerta() != null ? da.getPuerta().getNumero() : "";
				%>
				<tr>
					<td><%= r.getId() %></td>
					<td><%= r.getFechaFormateada() %></td>
					<td><%= HtmlUtil.escapeHtml(r.getNombreNino()) %></td>
					<td><%= HtmlUtil.escapeHtml(monstruo) %></td>
					<td><%= HtmlUtil.escapeHtml(puesto) %></td>
					<td><%= HtmlUtil.escapeHtml(puerta) %></td>
					<td><%= r.getEnergiaGenerada() %></td>
					<td class="actions">
						<a href="<%= ctx %>/resultado?id=<%= r.getId() %>" class="btn btn-ghost">
							<i data-lucide="pencil" class="icon-sm"></i>
							Editar
						</a>
						<button type="button" class="btn btn-danger" onclick="eliminar('<%= r.getId() %>')">
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
		if (!confirm('Eliminar resultado #' + id + '?')) { return; }
		fetch('<%= ctx %>/resultado?id=' + id, { method: 'DELETE' })
			.then(function() { location.href = '<%= ctx %>/resultado'; });
	}
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
